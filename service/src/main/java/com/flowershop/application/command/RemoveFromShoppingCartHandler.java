package com.flowershop.application.command;

import com.flowershop.api.command.RemoveFromShoppingCart;
import com.flowershop.api.command.RemoveFromShoppingCartResult;
import com.flowershop.application.ShoppingCartAssembler;
import com.flowershop.application.exception.BadRequestException;
import com.flowershop.application.exception.NotFoundException;
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
public class RemoveFromShoppingCartHandler implements CommandHandler<RemoveFromShoppingCartResult, RemoveFromShoppingCart> {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final FlowerRepository flowerRepository;
    private final FlowerProductRepository flowerProductRepository;

    @Override
    public RemoveFromShoppingCartResult handle(RemoveFromShoppingCart command) {
        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        Flower flower = flowerRepository.findBySlug(command.getFlowerSlug())
                .orElseThrow(() -> NotFoundException.notFound("flower [slug=%s] does not exist", command.getFlowerSlug()));

        ShoppingCart userShoppingCart = currentUser.getShoppingCart();

        // define whether flower is in the cart
        FlowerProduct correspondingFlowerProduct = null;

        if (userShoppingCart != null) {
            for (FlowerProduct flowerProduct : userShoppingCart.getFlowerProducts()) {
                if (flowerProduct.getFlower().getSlug().equals(flower.getSlug())) {
                    correspondingFlowerProduct = flowerProduct;
                }
            }
        }

        if (correspondingFlowerProduct == null)
            throw BadRequestException.badRequest("no flower [slug=%s] found in the cart", flower.getSlug());

        if (command.getAmountToRemove() >= correspondingFlowerProduct.getAmount()) {
            userShoppingCart.getFlowerProducts().remove(correspondingFlowerProduct);
            shoppingCartRepository.save(userShoppingCart);
        }
        else {
            correspondingFlowerProduct.setAmount(
                    correspondingFlowerProduct.getAmount() - command.getAmountToRemove()
            );
            flowerProductRepository.save(correspondingFlowerProduct);
        }

        return new RemoveFromShoppingCartResult(ShoppingCartAssembler.assemble(userShoppingCart));
    }
}
