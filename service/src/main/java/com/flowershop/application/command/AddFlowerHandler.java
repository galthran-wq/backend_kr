package com.flowershop.application.command;

import com.flowershop.api.command.AddFlower;
import com.flowershop.api.command.AddFlowerResult;
import com.flowershop.application.FlowerAssembler;
import com.flowershop.application.exception.BadRequestException;
import com.flowershop.application.exception.ForbiddenException;
import com.flowershop.application.service.SlugService;
import com.flowershop.bus.CommandHandler;
import com.flowershop.domain.model.Flower;
import com.flowershop.domain.model.User;
import com.flowershop.domain.repository.FlowerRepository;
import com.flowershop.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AddFlowerHandler implements CommandHandler<AddFlowerResult, AddFlower> {
    private final FlowerRepository flowerRepository;
    private final UserRepository userRepository;
    private final SlugService slugService;

    @Transactional
    @Override
    public AddFlowerResult handle(AddFlower command) {
        User currentUser = userRepository.findByUsername(command.getCurrentUsername())
                .orElseThrow(() -> BadRequestException.badRequest("user [name=%s] does not exist", command.getCurrentUsername()));

        if (!currentUser.getIs_owner()) {
            throw ForbiddenException.forbidden("you are not an owner");
        }

        // check if such name already exists
        Optional<Flower> flowerByNameOptional = flowerRepository.findByName(command.getName());
        if (flowerByNameOptional.isPresent()) {
            throw BadRequestException.badRequest("flower [name=%s] already exists", command.getName());
        }

        Flower.FlowerBuilder flowerBuilder = Flower.builder()
                .image(command.getImage())
                .availableAmount(command.getAvailableAmount())
                .id(UUID.randomUUID())
                .slug(slugService.makeSlug(command.getName()))
                .name(command.getName())
                .description(command.getDescription())
                .price(command.getPrice());

        Flower flower = flowerRepository.save(flowerBuilder.build());

        return new AddFlowerResult(FlowerAssembler.assemble(flower));
    }

}
