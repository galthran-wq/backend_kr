package com.resitplatform.rest;

import com.resitplatform.api.command.ScheduleResit;
import com.resitplatform.api.command.SignOffResit;
import com.resitplatform.api.command.SignOnResit;
import com.resitplatform.api.command.UpdateResit;
import com.resitplatform.api.dto.ResitDto;
import com.resitplatform.api.operation.ResitClient;
import com.resitplatform.application.service.SlugService;
import com.resitplatform.rest.auth.AuthSupport;
import com.resitplatform.rest.support.FeignBasedRestTest;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class ResitApiTest extends FeignBasedRestTest {

    public static final String TEST_NAME = "test-name";
    public static final String TEST_SLUG = "test-name";
    public static final String TEST_IMAGE = "test-image";
    public static final String TEST_DESCRIPTION = "test-description";
    public static final String ALTERED_NAME = "altered-name";
    public static final String ALTERED_DESCRIPTION = "altered-description";
    public static final String ALTERED_IMAGE = "test-image";

    @Autowired
    private AuthSupport auth;

    @Autowired
    private ResitClient resitClient;

    @Autowired
    private SlugService slugService;

    @AfterEach
    void afterEach() {
        auth.logout();
    }

    // general
    @Test
    void should_forbidWriteActionsForNonTeacher() {
        auth.register().login();

        UpdateResit updateCommand = UpdateResit.builder()
                .slug(TEST_SLUG)
                .image(ALTERED_IMAGE)
                .name(ALTERED_NAME)
                .description(ALTERED_DESCRIPTION)
                .build();

        ScheduleResit scheduleResit = getScheduleResitCommand();

        FeignException scheduleException = catchThrowableOfType(
                () -> resitClient.schedule(scheduleResit),
                FeignException.class
        );

        FeignException updateException = catchThrowableOfType(
                () -> resitClient.updateBySlug(updateCommand.getName(), updateCommand),
                FeignException.class
        );

        FeignException cancelException = catchThrowableOfType(
                () -> resitClient.cancelBySlug(scheduleResit.getName()),
                FeignException.class
        );

        for (FeignException exception: new ArrayList<>(Arrays.asList(scheduleException, updateException, cancelException))) {
            assertThat(exception.status()).isEqualTo(HttpStatus.FORBIDDEN.value());
            assertThat(exception).isNotNull();
        }

    }
    //
    // scheduling
    @Test
    void should_returnCorrectResitData_onSchedule() {
        AuthSupport.RegisteredUser user = auth.registerTeacher().login();

        ScheduleResit scheduleResitCommand = getScheduleResitCommand();
        ResitDto resit = resitClient.schedule(scheduleResitCommand).getResit();

        assertThat(resit.getSlug()).isEqualTo(
                slugService.makeSlug(scheduleResitCommand.getName())
        );
        assertThat(resit.getName()).isEqualTo(scheduleResitCommand.getName());
        assertThat(resit.getTeacherName()).isEqualTo(user.getUsername());
        assertThat(resit.getDescription()).isEqualTo(scheduleResitCommand.getDescription());
        assertThat(resit.getImage()).isEqualTo(scheduleResitCommand.getImage());
        assertThat(resit.getParticipants()).isEmpty();
    }

    @Test
    void should_forbidResitWithExistingName_onSchedule() {
        auth.registerTeacher().login();

        ScheduleResit scheduleResitCommand = getScheduleResitCommand();
        resitClient.schedule(scheduleResitCommand);

        // try schedule the same one
        FeignException scheduleException = catchThrowableOfType(
                () -> resitClient.schedule(scheduleResitCommand),
                FeignException.class
        );
        assertThat(scheduleException.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    //
    // canceling
    @Test
    void should_returnCorrectResitData_onCancel() {
        auth.registerTeacher().login();

        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        resitClient.cancelBySlug(created.getSlug());

        // assert that it is no more present
        FeignException exception = catchThrowableOfType(
                () -> resitClient.findBySlug(created.getSlug()),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_forbidNonresponsibleTeachers_onCancel() {
        auth.registerTeacher().login();

        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        // login as another teacher
        auth.registerTeacher().login();
        FeignException cancelException = catchThrowableOfType(
                () -> resitClient.cancelBySlug(created.getSlug()),
                FeignException.class
        );
        assertThat(cancelException.status()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    void should_returnCorrectNotExistingStatus_onCancel() {
        auth.registerTeacher().login();

        FeignException exception = catchThrowableOfType(
                () -> resitClient.cancelBySlug("not-existing"),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
    //
    // updating
    @Test
    void should_returnCorrectResitData_onUpdate() {
        AuthSupport.RegisteredUser user = auth.registerTeacher().login();

        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        UpdateResit updateCommand = UpdateResit.builder()
                .image(ALTERED_IMAGE)
                .name(ALTERED_NAME)
                .description(ALTERED_DESCRIPTION)
                .build();

        ResitDto updated = resitClient.updateBySlug(created.getSlug(), updateCommand).getResit();
        assertThat(updated.getSlug()).isEqualTo(slugService.makeSlug(ALTERED_NAME));
        assertThat(updated.getTeacherName()).isEqualTo(user.getUsername());
        assertThat(updated.getName()).isEqualTo(ALTERED_NAME);
        assertThat(updated.getDescription()).isEqualTo(ALTERED_DESCRIPTION);
        assertThat(updated.getImage()).isEqualTo(ALTERED_IMAGE);
    }

    @Test
    void should_forbidNonresponsibleTeachers_onUpdate() {
        auth.registerTeacher().login();

        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        UpdateResit updateCommand = UpdateResit.builder()
                .image(ALTERED_IMAGE)
                .name(ALTERED_NAME)
                .description(ALTERED_DESCRIPTION)
                .build();

        // login as another teacher
        auth.registerTeacher().login();
        FeignException cancelException = catchThrowableOfType(
                () -> resitClient.updateBySlug(created.getSlug(), updateCommand),
                FeignException.class
        );
        assertThat(cancelException.status()).isEqualTo(HttpStatus.FORBIDDEN.value());

    }
    //
    //sign on resit
    @Test
    void should_returnCorrectData_onResitSignOn() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        AuthSupport.RegisteredUser user = auth.register().login();
        ResitDto resit = resitClient.signOn(created.getSlug(), new SignOnResit()).getResit();
        assertThat(resit.getParticipants().length).isEqualTo(1);
        assertThat(resit.getParticipants()[0].getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void should_refuseTeachers_onSignOn() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        FeignException signOnException = catchThrowableOfType(
                () -> resitClient.signOn(created.getSlug(), new SignOnResit()),
                FeignException.class
        );
        assertThat(signOnException.status()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void should_returnCorrectNotExistingStatus_onSignOn() {
        auth.register().login();

        FeignException exception = catchThrowableOfType(
                () -> resitClient.signOn("not-existing", new SignOnResit()),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_refuseAlreadySignedOn_onSignOn() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        AuthSupport.RegisteredUser user = auth.register().login();
        resitClient.signOn(created.getSlug(), new SignOnResit());
        FeignException exception = catchThrowableOfType(
                () -> resitClient.signOn(created.getSlug(), new SignOnResit()),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }
    //
    // sign off resit
    @Test
    void should_returnCorrectData_onResitSignOff() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        auth.register().login();
        resitClient.signOn(created.getSlug(), new SignOnResit());
        ResitDto resit = resitClient.signOff(created.getSlug(), new SignOffResit()).getResit();

        assertThat(resit.getParticipants()).isEmpty();
    }

    @Test
    void should_returnCorrectNotExistingStatus_onSignOff() {
        auth.register().login();

        FeignException exception = catchThrowableOfType(
                () -> resitClient.signOff("not-existing", new SignOffResit()),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_refuseNotSignedOn_onSignOff() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        auth.register().login();
        FeignException exception = catchThrowableOfType(
                () -> resitClient.signOff(created.getSlug(), new SignOffResit()),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }
    //
    // find resit by slug test
    @Test
    void should_returnCorrectResitData_onSearchBySlug() {
        auth.registerTeacher().login();
        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        auth.register().login();
        ResitDto foundResit = resitClient.findBySlug(created.getSlug()).getResit();
        assertThat(foundResit.getSlug()).isEqualTo(created.getSlug());
    }

    @Test
    void should_return404forNotExisting_onSearchBySlug() {
        auth.register().login();
        FeignException exception = catchThrowableOfType(
                () -> resitClient.findBySlug("not-existing"),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static ScheduleResit getScheduleResitCommand() {
        return ScheduleResit.builder()
                .name(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .image(UUID.randomUUID().toString())
                .build();
    }

}
