package org.example.context;

import org.example.context.VirtualPet.PetStatusResponse;
import org.example.strategy.FeedStrategy;
import org.example.strategy.HealStrategy;
import org.example.strategy.PetStrategy;
import org.example.strategy.PlayStrategy;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/pet")
public class PetController {

    private final VirtualPet pet;

    public PetController(VirtualPet pet) {
        this.pet = pet;
    }

    @GetMapping("/status")
    public PetStatusResponse getStatus() {
        return new PetStatusResponse(
            pet.getStateName(),
            pet.getStateEmoji(),
            pet.getFullness(),
            pet.getHappiness(),
            pet.getMessageLog().isEmpty() 
                ? Collections.singletonList("🏠 Welcome! Your virtual pet is waiting for you!") 
                : pet.getMessageLog()
        );
    }

    @PostMapping("/action")
    public PetStatusResponse performAction(@RequestParam("actionType") String actionType) {
        PetStrategy strategy = switch (actionType.toUpperCase()) {
            case "FEED" -> new FeedStrategy();
            case "PLAY" -> new PlayStrategy();
            case "HEAL" -> new HealStrategy();
            default -> throw new IllegalArgumentException("Unknown action: " + actionType);
        };

        pet.apply(strategy);

        return new PetStatusResponse(
            pet.getStateName(),
            pet.getStateEmoji(),
            pet.getFullness(),
            pet.getHappiness(),
            pet.getMessageLog()
        );
    }

    @PostMapping("/reset")
    public PetStatusResponse resetPet() {
        pet.reset();
        return new PetStatusResponse(
            pet.getStateName(),
            pet.getStateEmoji(),
            pet.getFullness(),
            pet.getHappiness(),
            pet.getMessageLog()
        );
    }
}
