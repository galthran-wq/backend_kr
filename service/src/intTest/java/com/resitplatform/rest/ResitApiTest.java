package com.resitplatform.rest;

import com.resitplatform.api.command.ScheduleResit;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

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
            System.out.println(exception.status());
            assertThat(exception.status()).isEqualTo(403);
            assertThat(exception).isNotNull();
        }

    }

    @Test
    void should_returnCorrectResitData() {
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
    }
//
    @Test
    void should_returnCorrectResitData_when_deleteByTeacher() {
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
    void should_returnCorrectResitData_when_deleteNotExistingByTeacher() {
        auth.registerTeacher().login();

        FeignException exception = catchThrowableOfType(
                () -> resitClient.cancelBySlug("not-existing"),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_returnCorrectResitData_when_updateByOwner() {
        auth.registerTeacher().login();

        ResitDto created = resitClient.schedule(getScheduleResitCommand()).getResit();

        UpdateResit updateCommand = UpdateResit.builder()
                .image(ALTERED_IMAGE)
                .name(ALTERED_NAME)
                .description(ALTERED_DESCRIPTION)
                .build();

        ResitDto updated = resitClient.updateBySlug(created.getSlug(), updateCommand).getResit();
        assertThat(updated.getSlug()).isEqualTo(slugService.makeSlug(ALTERED_NAME));
        assertThat(updated.getName()).isEqualTo(ALTERED_NAME);
        assertThat(updated.getDescription()).isEqualTo(ALTERED_DESCRIPTION);
        assertThat(updated.getImage()).isEqualTo(ALTERED_IMAGE);
    }

    public static ScheduleResit getScheduleResitCommand() {
        return ScheduleResit.builder()
                .name(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .image(UUID.randomUUID().toString())
                .build();
    }

}
