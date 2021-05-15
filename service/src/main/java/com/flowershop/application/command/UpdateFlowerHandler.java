package com.flowershop.application.command;

import com.flowershop.api.command.UpdateFlower;
import com.flowershop.api.command.UpdateFlowerResult;
import com.flowershop.application.FlowerAssembler;
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

@RequiredArgsConstructor
@Service
public class UpdateFlowerHandler implements CommandHandler<UpdateFlowerResult, UpdateFlower> {

    private final FlowerRepository flowerRepository;
    private final UserRepository userRepository;

    @Override
    public UpdateFlowerResult handle(UpdateFlower command) {
        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        if (!currentUser.getIs_owner()) {
            throw ForbiddenException.forbidden("you are not an owner");
        }

        Flower flowerBySlug = flowerRepository.findBySlug(command.getSlug())
                .orElseThrow(() -> NotFoundException.notFound("No flower with slug \"[slug=%s]\" found", command.getSlug()));

        Flower.FlowerBuilder flowerBuilder = flowerBySlug.toBuilder()
                .image(command.getImage())
                .availableAmount(command.getAvailableAmount())
                .price(command.getPrice())
                .description(command.getDescription())
                .slug(command.getSlug())
                .name(command.getName());

        flowerRepository.save(flowerBuilder.build());
        return new UpdateFlowerResult(FlowerAssembler.assemble(flowerBuilder.build()));
    }
}
