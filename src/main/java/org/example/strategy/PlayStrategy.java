package org.example.strategy;

import org.example.context.VirtualPet;

public class PlayStrategy implements PetStrategy {
    @Override
    public void execute(VirtualPet pet) { 
        pet.changeStats(-40, 20); 
        pet.addMessage("📊 Fullness -40, Happiness +20");
    }

    @Override
    public String getName() { return "Play 🎾"; }
}