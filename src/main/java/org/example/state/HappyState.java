package org.example.state;

import org.example.context.VirtualPet;
import org.example.strategy.FeedStrategy;
import org.example.strategy.PetStrategy;
import org.example.strategy.PlayStrategy;

public class HappyState implements PetState {
    @Override
    public String handle(VirtualPet pet, PetStrategy strategy) {
        String message;
        if (strategy instanceof PlayStrategy) {
            message = "🎉 Your pet is dancing and jumping around happily!";
            pet.addMessage(message);
            strategy.execute(pet);
            if (pet.getFullness() < 50) {
                String stateChange = "⚠️ Playing too much made your pet hungry!";
                pet.addMessage(stateChange);
                pet.setState(new HungryState());
            }
        } else if (strategy instanceof FeedStrategy) {
            message = "😋 Your happy pet eats the food with great appetite!";
            pet.addMessage(message);
            strategy.execute(pet);
        } else {
            message = "💚 Your pet is healthy and refuses medicine!";
            pet.addMessage(message);
        }
        return message;
    }

    @Override
    public String getName() { return "HAPPY"; }
    
    @Override
    public String getEmoji() { return "🐶"; }
}