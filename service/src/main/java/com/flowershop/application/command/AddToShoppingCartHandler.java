package com.flowershop.application.command;

import com.flowershop.api.command.AddToShoppingCart;
import com.flowershop.api.command.AddToShoppingCartResult;
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

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AddToShoppingCartHandler implements CommandHandler<AddToShoppingCartResult, AddToShoppingCart> {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final FlowerRepository flowerRepository;
    private final FlowerProductRepository flowerProductRepository;

    @Override
    public AddToShoppingCartResult handle(AddToShoppingCart command) {
        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        Flower flower = flowerRepository.findBySlug(command.getFlowerSlug())
                .orElseThrow(() -> NotFoundException.notFound("flower [slug=%s] does not exist", command.getFlowerSlug()));

        ShoppingCart userShoppingCart = currentUser.getShoppingCart();

        if (userShoppingCart == null) {
            userShoppingCart = shoppingCartRepository.save(ShoppingCart.builder()
                    .buyer(currentUser)
                    .id(UUID.randomUUID())
                    .build());
        }

        // try to find this flower in products
        FlowerProduct flowerProduct = null;

        for (FlowerProduct tempFlowerProduct : userShoppingCart.getFlowerProducts()) {
            if (tempFlowerProduct.getFlower().getId() == flower.getId())
                flowerProduct = tempFlowerProduct;
        }
        // not found - create
        if (flowerProduct == null) {
            flowerProduct = flowerProductRepository.save(FlowerProduct
                    .builder()
                    .shoppingCart(userShoppingCart)
                    .flower(flower)
                    .amount(0)
                    .id(UUID.randomUUID())
                    .build());

            userShoppingCart.getFlowerProducts().add(flowerProduct);
            shoppingCartRepository.save(userShoppingCart);
        }

        // check correct amount
        if (flower.getAvailableAmount() < command.getAmount() + flowerProduct.getAmount()) {
            throw BadRequestException.badRequest("requested amount exceeds available in stock");
        }

        // update
        flowerProduct.setAmount(flowerProduct.getAmount() + command.getAmount());
        flowerProductRepository.save(flowerProduct);

        return new AddToShoppingCartResult(ShoppingCartAssembler.assemble(userShoppingCart));
    }
}
