package org.example.state;

import org.example.context.VirtualPet;
import org.example.strategy.PetStrategy;

public interface PetState {
    String handle(VirtualPet pet, PetStrategy strategy);
    String getName();
    String getEmoji();
}