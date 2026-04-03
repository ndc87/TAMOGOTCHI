package org.example.strategy;

import org.example.context.VirtualPet;

public class HealStrategy implements PetStrategy {
    @Override
    public void execute(VirtualPet pet) { 
        pet.changeStats(0, -10); 
        pet.addMessage("📊 Happiness -10 (bitter medicine)");
    }

    @Override
    public String getName() { return "Heal 💊"; }
}