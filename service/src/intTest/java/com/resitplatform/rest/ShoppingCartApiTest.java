package com.resitplatform.rest;

import com.resitplatform.api.command.AddFlower;
import com.resitplatform.api.command.AddToShoppingCart;
import com.resitplatform.api.command.RemoveFromShoppingCart;
import com.resitplatform.api.dto.FlowerDto;
import com.resitplatform.api.dto.ShoppingCartDto;
import com.resitplatform.api.operation.FlowerClient;
import com.resitplatform.api.operation.ShoppingCartClient;
import com.resitplatform.domain.model.FlowerProduct;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.UserRepository;
import com.resitplatform.rest.auth.AuthSupport;
import com.resitplatform.rest.support.FeignBasedRestTest;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class ShoppingCartApiTest extends FeignBasedRestTest {

    public static final int TEST_AMOUNT = 2;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthSupport auth;

    @Autowired
    private ShoppingCartClient shoppingCartClient;

    @Autowired
    private FlowerClient flowerClient;

    @AfterEach
    void afterEach() {
        auth.logout();
    }

    @Test
    void should_returnCorrectShoppingCartData_when_added() {
        assertThat(FlowerApiTest.TEST_AVAILABLE_AMOUNT).isGreaterThanOrEqualTo(TEST_AMOUNT);

        auth.registerOwner().login();

        AddFlower addFlower = FlowerApiTest.addFlowerCommand();
        FlowerDto flower = flowerClient.add(addFlower).getFlower();

        AddToShoppingCart addToShoppingCartCommand = AddToShoppingCart.builder()
                .amount(TEST_AMOUNT)
                .flowerSlug(flower.getSlug())
                .build();

        ShoppingCartDto shoppingCart = shoppingCartClient.add(addToShoppingCartCommand).getShoppingCart();

        assertThat(shoppingCart.getFlowerProducts()).hasSize(1);
        assertThat(shoppingCart.getTotalPrice()).isEqualTo(TEST_AMOUNT * addFlower.getPrice());
        assertThat(shoppingCart.getFlowerProducts()[0].getFlower().getSlug()).isEqualTo(addFlower.getName());
        assertThat(shoppingCart.getFlowerProducts()[0].getTotalProductPrice()).isEqualTo(shoppingCart.getTotalPrice());
        assertThat(shoppingCart.getFlowerProducts()[0].getTotalAmount()).isEqualTo(TEST_AMOUNT);

        shoppingCart = shoppingCartClient.add(addToShoppingCartCommand).getShoppingCart();
        assertThat(shoppingCart.getFlowerProducts()).hasSize(1);
        assertThat(shoppingCart.getTotalPrice()).isEqualTo(2 * TEST_AMOUNT * addFlower.getPrice());
        assertThat(shoppingCart.getFlowerProducts()[0].getFlower().getSlug()).isEqualTo(addFlower.getName());
        assertThat(shoppingCart.getFlowerProducts()[0].getTotalProductPrice()).isEqualTo(shoppingCart.getTotalPrice());
        assertThat(shoppingCart.getFlowerProducts()[0].getTotalAmount()).isEqualTo(2 * TEST_AMOUNT);

    }

    @Test
    void should_return400_when_amount_exceeded_when_added() {
        auth.registerOwner().login();

        AddFlower addFlower = FlowerApiTest.addFlowerCommand();
        FlowerDto flower = flowerClient.add(addFlower).getFlower();

        AddToShoppingCart addToShoppingCartCommand = AddToShoppingCart.builder()
                .amount(FlowerApiTest.TEST_AVAILABLE_AMOUNT + 1)
                .flowerSlug(flower.getSlug())
                .build();

        FeignException exception = catchThrowableOfType(
                () -> shoppingCartClient.add(addToShoppingCartCommand),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void should_returnCorrectShoppingCartData_when_removed() {
        auth.registerOwner().login();

        AddFlower addFlower = FlowerApiTest.addFlowerCommand();
        FlowerDto flower = flowerClient.add(addFlower).getFlower();

        AddToShoppingCart addToShoppingCartCommand = AddToShoppingCart.builder()
                .amount(TEST_AMOUNT)
                .flowerSlug(flower.getSlug())
                .build();

        RemoveFromShoppingCart removeFromShoppingCartCommand = RemoveFromShoppingCart.builder()
                .amountToRemove(TEST_AMOUNT - 1)
                .flowerSlug(addFlower.getName())
                .build();

        shoppingCartClient.add(addToShoppingCartCommand);
        ShoppingCartDto shoppingCart = shoppingCartClient.remove(removeFromShoppingCartCommand).getUpdatedShoppingCart();

        assertThat(shoppingCart.getFlowerProducts()).hasSize(1);
        assertThat(shoppingCart.getTotalPrice()).isEqualTo((TEST_AMOUNT - 1) * addFlower.getPrice());
        assertThat(shoppingCart.getFlowerProducts()[0].getFlower().getSlug()).isEqualTo(addFlower.getName());
        assertThat(shoppingCart.getFlowerProducts()[0].getTotalAmount()).isEqualTo(TEST_AMOUNT - 1);

        shoppingCart = shoppingCartClient.remove(removeFromShoppingCartCommand).getUpdatedShoppingCart();
        assertThat(shoppingCart.getFlowerProducts()).hasSize(0);
        assertThat(shoppingCart.getTotalPrice()).isEqualTo(0.0);

    }

    @Test
    void should_return404WhenFlowerNotFound() {
        auth.registerOwner().login();

        AddToShoppingCart addToShoppingCartCommand = AddToShoppingCart.builder()
                .amount(TEST_AMOUNT)
                .flowerSlug("DOES_NOT_EXIST")
                .build();

        FeignException exception = catchThrowableOfType(
                () -> shoppingCartClient.add(addToShoppingCartCommand),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());

        RemoveFromShoppingCart removeFromShoppingCartCommand = RemoveFromShoppingCart.builder()
                .amountToRemove(TEST_AMOUNT - 1)
                .flowerSlug("DOES_NOT_EXIST")
                .build();

        exception = catchThrowableOfType(
                () -> shoppingCartClient.remove(removeFromShoppingCartCommand),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_return400WhenFlowerIsNotInTheCart_when_removed() {
        auth.registerOwner().login();

        AddFlower addFlower = FlowerApiTest.addFlowerCommand();
        FlowerDto flower = flowerClient.add(addFlower).getFlower();

        RemoveFromShoppingCart removeFromShoppingCartCommand = RemoveFromShoppingCart.builder()
                .amountToRemove(TEST_AMOUNT - 1)
                .flowerSlug(addFlower.getName())
                .build();

        FeignException exception = catchThrowableOfType(
                () -> shoppingCartClient.remove(removeFromShoppingCartCommand),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void should_returnCorrectData_when_checkout() {
        AuthSupport.RegisteredUser registeredUser = auth.registerOwner();
        registeredUser.login();

        AddFlower addFlower = FlowerApiTest.addFlowerCommand();
        FlowerDto flower = flowerClient.add(addFlower).getFlower();

        AddToShoppingCart addToShoppingCartCommand = AddToShoppingCart.builder()
                .amount(TEST_AMOUNT)
                .flowerSlug(flower.getSlug())
                .build();

        shoppingCartClient.add(addToShoppingCartCommand);
        shoppingCartClient.checkout();

        User user = userRepository.findByUsername(registeredUser.getUsername()).orElseThrow();

        assertThat(((FlowerProduct)user.getObtainedFlowerProducts().toArray()[0]).getAmount()).isEqualTo(TEST_AMOUNT);
        assertThat(((FlowerProduct)user.getObtainedFlowerProducts().toArray()[0]).getFlower().getSlug()).isEqualTo(flower.getSlug());
        assertThat(user.getShoppingCart().getFlowerProducts()).isEmpty();
    }

    @Test
    void should_return400WhenCartIsEmpty_when_checkout() {
        AuthSupport.RegisteredUser registeredUser = auth.registerOwner();
        registeredUser.login();

        FeignException exception = catchThrowableOfType(
                () -> shoppingCartClient.checkout(),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    void should_return400WhenRequestedMoreThanAvailable_when_checkout() {
        AuthSupport.RegisteredUser registeredUser1 = auth.registerOwner();
        AuthSupport.RegisteredUser registeredUser2 = auth.register();

        registeredUser1.login();
        AddFlower addFlower = FlowerApiTest.addFlowerCommand();
        FlowerDto flower = flowerClient.add(addFlower).getFlower();

        // first user adds to the cart
        AddToShoppingCart addToShoppingCartCommand = AddToShoppingCart.builder()
                .amount(flower.getAvailableAmount())
                .flowerSlug(flower.getSlug())
                .build();
        shoppingCartClient.add(addToShoppingCartCommand);

        registeredUser2.login();
        // second user adds to the cart
        shoppingCartClient.add(addToShoppingCartCommand);
        // second user checks out
        shoppingCartClient.checkout();

        registeredUser1.login();
        // first user tries to checkout - not available in stock - get 400;

        FeignException exception = catchThrowableOfType(
                () -> shoppingCartClient.checkout(),
                FeignException.class
        );
        assertThat(exception.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
