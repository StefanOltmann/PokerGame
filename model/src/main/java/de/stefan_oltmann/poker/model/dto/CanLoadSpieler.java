package de.stefan_oltmann.poker.model.dto;

import de.stefan_oltmann.poker.model.Spieler;

public interface CanLoadSpieler {

    Spieler findSpielerById(String spielerId);

}
