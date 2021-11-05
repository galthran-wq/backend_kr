package com.resitplatform.application.command;

import com.resitplatform.api.command.UpdateResit;
import com.resitplatform.api.command.UpdateResitResult;
import com.resitplatform.application.FlowerAssembler;
import com.resitplatform.application.exception.BadRequestException;
import com.resitplatform.application.exception.ForbiddenException;
import com.resitplatform.application.exception.NotFoundException;
import com.resitplatform.bus.CommandHandler;
import com.resitplatform.domain.model.Flower;
import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.FlowerRepository;
import com.resitplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateFlowerHandler implements CommandHandler<UpdateResitResult, UpdateResit> {

    private final FlowerRepository flowerRepository;
    private final UserRepository userRepository;

    @Override
    public UpdateResitResult handle(UpdateResit command) {
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
        return new UpdateResitResult(FlowerAssembler.assemble(flowerBuilder.build()));
    }
}
