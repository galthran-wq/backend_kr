package com.flowershop.application.command;

import com.flowershop.api.command.DeleteFlower;
import com.flowershop.api.command.DeleteFlowerResult;
import com.flowershop.application.exception.BadRequestException;
import com.flowershop.application.exception.ForbiddenException;
import com.flowershop.application.exception.NotFoundException;
import com.flowershop.bus.CommandHandler;
import com.flowershop.domain.model.Flower;
import com.flowershop.domain.model.User;
import com.flowershop.domain.repository.FlowerRepository;
import com.flowershop.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// todo permissions owner
@RequiredArgsConstructor
@Service
public class DeleteFlowerHandler implements CommandHandler<DeleteFlowerResult, DeleteFlower> {

    private final FlowerRepository flowerRepository;
    private final UserRepository userRepository;

    @Override
    public DeleteFlowerResult handle(DeleteFlower command) {
        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        if (!currentUser.getIs_owner()) {
            throw ForbiddenException.forbidden("you are not an owner");
        }

        Flower flowerBySlug = flowerRepository.findBySlug(command.getSlug())
                .orElseThrow(() -> NotFoundException.notFound("No flower with slug \"[slug=%s]\" found", command.getSlug()));

        flowerRepository.delete(flowerBySlug);
        return new DeleteFlowerResult();
    }
}
