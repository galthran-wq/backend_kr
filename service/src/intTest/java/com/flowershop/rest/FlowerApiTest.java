package com.flowershop.rest;

import com.flowershop.api.command.AddFlower;
import com.flowershop.api.command.UpdateFlower;
import com.flowershop.api.dto.FlowerDto;
import com.flowershop.api.operation.FlowerClient;
import com.flowershop.rest.auth.AuthSupport;
import com.flowershop.rest.support.FeignBasedRestTest;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class FlowerApiTest extends FeignBasedRestTest {

    public static final String TEST_NAME = "test-name";
    public static final String TEST_DESCRIPTION = "test-description";
    public static final Double TEST_PRICE = 0.1;
    public static final String TEST_IMAGE = "test-image";
    public static final String ALTERED_NAME = "altered-name";
    public static final String ALTERED_DESCRIPTION = "altered-description";
    public static final Double ALTERED_PRICE = 1.0;
    public static final String ALTERED_IMAGE = "altered-image";

    @Autowired
    private AuthSupport auth;

    @Autowired
    private FlowerClient flowerClient;

    @AfterEach
    void afterEach() {
        auth.logout();
    }

    @Test
    void should_forbidWriteActionsForNonOwners() {
        auth.register().login();

        UpdateFlower updateCommand = UpdateFlower.builder()
                .image(ALTERED_IMAGE)
                .name(ALTERED_NAME)
                .description(ALTERED_DESCRIPTION)
                .price(ALTERED_PRICE)
                .build();
        AddFlower addCommand = addFlowerCommand();

        FeignException addException = catchThrowableOfType(
                () -> flowerClient.add(addCommand),
                FeignException.class
        );

        FeignException updateException = catchThrowableOfType(
                () -> flowerClient.updateBySlug(updateCommand.getName(), updateCommand),
                FeignException.class
        );

        FeignException deleteException = catchThrowableOfType(
                () -> flowerClient.deleteBySlug(addCommand.getName()),
                FeignException.class
        );

        for (FeignException exception: new ArrayList<>(Arrays.asList(addException, updateException, deleteException))) {
            assertThat(exception.status()).isEqualTo(403);
            assertThat(exception).isNotNull();
        }

    }

    @Test
    void should_returnCorrectFlowerData() {
        auth.registerOwner().login();

        AddFlower command = addFlowerCommand();
        FlowerDto flower = flowerClient.add(command).getFlower();

        assertThat(flower.getSlug()).isEqualTo(command.getName());
        assertThat(flower.getName()).isEqualTo(command.getName());
        assertThat(flower.getDescription()).isEqualTo(command.getDescription());
        assertThat(flower.getPrice()).isEqualTo(command.getPrice());
        assertThat(flower.getImage()).isEqualTo(command.getImage());
    }

    @Test
    void should_returnCorrectFlowerData_when_deleteByOwner() {
        auth.registerOwner().login();

        FlowerDto created = flowerClient.add(addFlowerCommand()).getFlower();

        flowerClient.deleteBySlug(created.getSlug());

        FeignException exception = catchThrowableOfType(
                () -> flowerClient.findBySlug(created.getSlug()),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    void should_returnCorrectFlowerData_when_deleteNotExistingByOwner() {
        auth.registerOwner().login();

        FeignException exception = catchThrowableOfType(
                () -> flowerClient.deleteBySlug("not-existing"),
                FeignException.class
        );

        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_returnCorrectFlowerData_when_updateByOwner() {
        auth.registerOwner().login();

        FlowerDto created = flowerClient.add(addFlowerCommand()).getFlower();

        UpdateFlower updateCommand = UpdateFlower.builder()
                .image(ALTERED_IMAGE)
                .name(ALTERED_NAME)
                .description(ALTERED_DESCRIPTION)
                .price(ALTERED_PRICE)
                .build();

        FlowerDto updated = flowerClient.updateBySlug(created.getSlug(), updateCommand).getFlower();
// todo
//        assertThat(updated.getSlug()).isEqualTo(ALTERED_NAME);
        assertThat(updated.getName()).isEqualTo(ALTERED_NAME);
        assertThat(updated.getDescription()).isEqualTo(ALTERED_DESCRIPTION);
        assertThat(updated.getPrice()).isEqualTo(ALTERED_PRICE);
        assertThat(updated.getImage()).isEqualTo(ALTERED_IMAGE);
    }

    private static AddFlower addFlowerCommand() {
        return AddFlower.builder()
                .name(UUID.randomUUID().toString())
                .description(UUID.randomUUID().toString())
                .price(new Random().nextDouble())
                .image(UUID.randomUUID().toString())
                .build();
    }

}
