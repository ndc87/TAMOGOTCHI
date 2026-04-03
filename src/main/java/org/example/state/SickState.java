package org.example.state;

import org.example.context.VirtualPet;
import org.example.strategy.HealStrategy;
import org.example.strategy.PetStrategy;

public class SickState implements PetState {
    @Override
    public String handle(VirtualPet pet, PetStrategy strategy) {
        String message;
        if (strategy instanceof HealStrategy) {
            message = "💊 Your pet takes the medicine and is recovering...";
            pet.addMessage(message);
            strategy.execute(pet);
            String stateChange = "🩹 After recovery, your pet feels hungry.";
            pet.addMessage(stateChange);
            pet.setState(new HungryState());
        } else {
            message = "😵 Your sick pet is too weak and refuses everything except medicine!";
            pet.addMessage(message);
        }
        return message;
    }

    @Override
    public String getName() { return "SICK"; }
    
    @Override
    public String getEmoji() { return "🤒"; }
}