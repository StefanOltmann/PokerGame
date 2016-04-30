/*****************************************************************************
 * Stefans Poker Game                                                        *
 *                                                                           *
 * Copyright (C) 2016 Stefan Oltmann                                         *
 *                                                                           *
 * Contact : pokergame@stefan-oltmann.de                                     *
 * Homepage: http://www.stefan-oltmann.de/                                   *      
 *                                                                           *
 * This program is free software: you can redistribute it and/or modify      *
 * it under the terms of the GNU Affero General Public License as            *
 * published by the Free Software Foundation, either version 3 of the        *
 * License, or (at your option) any later version.                           *
 *                                                                           *
 * This program is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of            *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the              *
 * GNU Affero General Public License for more details.                       *
 *                                                                           *
 * You should have received a copy of the GNU Affero General Public License  *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.     *
 *****************************************************************************/
package de.stefan_oltmann.poker.model;

public enum SpielerStatus {

    /**
     * Der Spieler ist zwischendurch beigetreten
     * und wartet auf das Ende der aktuellen Runde
     */
    WARTET_AUF_NAECHSTE_RUNDE,

    /**
     * Der Spieler nimmt gerade nicht an der Hand teil,
     * sitzt ansonsten aber am Tisch.
     * 
     * In einem Turnier zahlt ein solcher Spieler auch
     * die Blinds weiter, im Cash Game nicht.
     */
    SITZT_AUS,

    /**
     * Der Spieler ist gerade noch nicht an der Reihe.
     */
    WARTET,

    /** Hat reagiert */
    HAT_GECALLED,
    HAT_GEWETTET,
    HAT_GERAISED,
    HAT_GECHECKED,
    HAT_GEFOLDED,

    /** Muss reagieren */
    MUSS_CALLEN_ODER_RAISEN,
    MUSS_CHECKEN_ODER_BETTEN,

    /** Der Spieler hat verloren und guckt nur noch zu. */
    ELIMINIERT;

    public boolean isKannInDerRundeHandeln() {

        return this != SITZT_AUS
                && this != HAT_GEFOLDED
                && this != ELIMINIERT;
    }

    public boolean isMussReagieren() {

        return this != MUSS_CALLEN_ODER_RAISEN
                && this != MUSS_CHECKEN_ODER_BETTEN;
    }
}
