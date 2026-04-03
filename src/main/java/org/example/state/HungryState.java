package org.example.state;

import org.example.context.VirtualPet;
import org.example.strategy.FeedStrategy;
import org.example.strategy.PetStrategy;
import org.example.strategy.PlayStrategy;

public class HungryState implements PetState {
    @Override
    public String handle(VirtualPet pet, PetStrategy strategy) {
        String message;
        if (strategy instanceof FeedStrategy) {
            message = "🍔 Your hungry pet devours the food eagerly!";
            pet.addMessage(message);
            strategy.execute(pet);
            if (pet.getFullness() >= 80) {
                String stateChange = "✨ Your pet is full and happy again!";
                pet.addMessage(stateChange);
                pet.setState(new HappyState());
            }
        } else if (strategy instanceof PlayStrategy) {
            message = "💀 Your pet is too hungry to play and becomes sick!";
            pet.addMessage(message);
            pet.setState(new SickState());
        } else {
            message = "🍽️ Your hungry pet only wants food, not medicine!";
            pet.addMessage(message);
        }
        return message;
    }

    @Override
    public String getName() { return "HUNGRY"; }
    
    @Override
    public String getEmoji() { return "🥺"; }
}