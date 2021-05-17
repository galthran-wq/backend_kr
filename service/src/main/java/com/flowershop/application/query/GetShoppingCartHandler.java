package com.flowershop.application.query;

import com.flowershop.api.query.GetShoppingCart;
import com.flowershop.api.query.GetShoppingCartResult;
import com.flowershop.application.ShoppingCartAssembler;
import com.flowershop.application.exception.BadRequestException;
import com.flowershop.bus.QueryHandler;
import com.flowershop.domain.model.ShoppingCart;
import com.flowershop.domain.model.User;
import com.flowershop.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GetShoppingCartHandler implements QueryHandler<GetShoppingCartResult, GetShoppingCart> {

    private final UserRepository userRepository;

    @Override
    public GetShoppingCartResult handle(GetShoppingCart query) {
        User user = userRepository.findByUsername(query.getCurrentUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", query.getCurrentUsername()));

        ShoppingCart userShoppingCart = user.getShoppingCart();

        return new GetShoppingCartResult(ShoppingCartAssembler.assemble(userShoppingCart));
    }
}
