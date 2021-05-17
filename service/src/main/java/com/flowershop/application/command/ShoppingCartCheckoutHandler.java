package com.flowershop.application.command;

import com.flowershop.api.command.ShoppingCartCheckout;
import com.flowershop.api.command.ShoppingCartCheckoutResult;
import com.flowershop.application.exception.BadRequestException;
import com.flowershop.bus.CommandHandler;
import com.flowershop.domain.model.Flower;
import com.flowershop.domain.model.FlowerProduct;
import com.flowershop.domain.model.ShoppingCart;
import com.flowershop.domain.model.User;
import com.flowershop.domain.repository.FlowerProductRepository;
import com.flowershop.domain.repository.FlowerRepository;
import com.flowershop.domain.repository.ShoppingCartRepository;
import com.flowershop.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartCheckoutHandler implements CommandHandler<ShoppingCartCheckoutResult, ShoppingCartCheckout> {

    private final UserRepository userRepository;
    private final FlowerProductRepository flowerProductRepository;
    private final FlowerRepository flowerRepository;

    @Override
    public ShoppingCartCheckoutResult handle(ShoppingCartCheckout command) {
        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        ShoppingCart shoppingCart = currentUser.getShoppingCart();

        // ensure shopping cart is not empty
        if (shoppingCart == null || shoppingCart.getFlowerProducts().size() == 0) {
            throw BadRequestException.badRequest("shopping cart is empty");
        }

        // ensure availability in stock
        for (FlowerProduct flowerProduct : shoppingCart.getFlowerProducts()) {
            if (flowerProduct.getAmount() > flowerProduct.getFlower().getAvailableAmount())
                throw BadRequestException.badRequest(
                        "only [availableAmount=%s] of flower [slug=%s] is available",
                        flowerProduct.getFlower().getAvailableAmount(),
                        flowerProduct.getFlower().getSlug());
        }

        // move from shopping cart to holdings
        for (FlowerProduct flowerProduct : shoppingCart.getFlowerProducts()) {
            flowerProduct.setHolder(currentUser);
            flowerProduct.setShoppingCart(null);
            flowerProductRepository.save(flowerProduct);

            // update product amount
            Flower flower = flowerProduct.getFlower();
            flower.setAvailableAmount(
                    flower.getAvailableAmount() - flowerProduct.getAmount()
            );
            flowerRepository.save(flower);
        }

        return new ShoppingCartCheckoutResult();
    }
}
