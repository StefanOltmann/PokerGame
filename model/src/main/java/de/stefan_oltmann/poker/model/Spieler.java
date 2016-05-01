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

import de.stefan_oltmann.poker.model.hand.Karte;

/**
 * Eine Spieler-Entität besteht aus der Kombination
 * von einem Account und dem Spiel, an dem er teilnimmt.
 * Dies können mehere gleichzeitig sein.
 */
public class Spieler {

    /*
     * Identifizierung des Spielers
     */
    private String id;
    private String nickName;
    private String spielId;

    /**
     * Dies ist die Information, ob der Spieler
     * noch in der Hand ist oder bereits gefoldet hat.
     * Diese Variable steuert auch, ob in der GUI für
     * diesen Spieler noch verdeckte Karten angedeutet werden.
     */
    private boolean hatKarten;

    /**
     * Dies sind die für den Benutzer sichtbaren, aufgedeckten
     * Karten dieses Spielers. Für den Spieler des eigenen Accounts
     * sind diese immer sichtbar. Für Spieler anderer Mitspieler-Accounts
     * werden diese sichtbar, sobald er die Karten aufdeckt.
     * 
     * Der Server kennt immer alle Karten aller Mitspieler, da sie
     * von seiner Seite aus aufgedeckt werden.
     */
    private Karte holeCard1;
    private Karte holeCard2;

    private int chipsImStack;
    private int chipsGesetzt;
    private SpielerStatus status = SpielerStatus.WARTET;

    /*
     * Konstruktor
     */

    public Spieler(String id, String nickName, String spielId) {
        this.id = id;
        this.nickName = nickName;
        this.spielId = spielId;
    }

    /*
     * Methoden
     */

    public void setzeChips(int chips) {

        if (chips > chipsImStack)
            throw new IllegalArgumentException(
                    "Spieler kann nicht " + chips + " Chips setzen, da er nicht soviel hat: " + chipsImStack);

        this.chipsImStack -= chips;
        this.chipsGesetzt += chips;
    }

    /*
     * Getters + Setters
     */

    public String getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public String getSpielId() {
        return spielId;
    }

    public boolean isHatKarten() {
        return hatKarten;
    }

    public void setHatKartenBekommen() {
        this.hatKarten = true;
    }

    public void kartenZurueckgeben() {
        this.hatKarten = false;
        this.holeCard1 = null;
        this.holeCard2 = null;
    }

    public Karte getHoleCard1() {
        return holeCard1;
    }

    public void setHoleCard1(Karte holeCard1) {
        this.holeCard1 = holeCard1;
    }

    public Karte getHoleCard2() {
        return holeCard2;
    }

    public void setHoleCard2(Karte holeCard2) {
        this.holeCard2 = holeCard2;
    }

    public int getChipsImStack() {
        return chipsImStack;
    }

    public void setChipsImStack(int chipsImStack) {
        this.chipsImStack = chipsImStack;
    }

    public int getChipsGesetzt() {
        return chipsGesetzt;
    }

    public void setChipsGesetzt(int chipsGesetzt) {
        this.chipsGesetzt = chipsGesetzt;
    }

    public SpielerStatus getStatus() {
        return status;
    }

    public void setStatus(SpielerStatus status) {
        this.status = status;
    }

    /*
     * toString()
     */

    @Override
    public String toString() {
        return "Spieler [id=" + id + ", nickName=" + nickName + ", spielId=" + spielId + "]";
    }
}