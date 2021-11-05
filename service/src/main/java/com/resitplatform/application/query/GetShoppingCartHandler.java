package com.resitplatform.application.query;

import com.resitplatform.api.query.GetShoppingCart;
import com.resitplatform.api.query.GetShoppingCartResult;
import com.resitplatform.application.ShoppingCartAssembler;
import com.resitplatform.application.exception.BadRequestException;
import com.resitplatform.bus.QueryHandler;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.UserRepository;
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
