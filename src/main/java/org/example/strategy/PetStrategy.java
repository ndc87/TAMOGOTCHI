package org.example.strategy;

import org.example.context.VirtualPet;

public interface PetStrategy {
    void execute(VirtualPet pet);
    String getName();
}