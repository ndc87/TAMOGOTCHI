package org.example.strategy;

import org.example.context.VirtualPet;

public class FeedStrategy implements PetStrategy {
    @Override
    public void execute(VirtualPet pet) { 
        pet.changeStats(30, 5); 
        pet.addMessage("📊 Fullness +30, Happiness +5");
    }

    @Override
    public String getName() { return "Feed 🍔"; }
}