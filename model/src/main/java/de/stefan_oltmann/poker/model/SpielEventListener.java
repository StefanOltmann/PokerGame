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
 * Der SpielEventListener ist dem Spiel sehr ähnlich, da jede
 * Änderung an einem Spiel über diesen Listener weitergegeben wird.
 * 
 * Theoretisch könnte dasselbe Interface Spiel auch für den Listener
 * verwendet werden. Aus Gründen der Klarheit existiert dieses Interface
 * aber dennoch separat.
 */
public interface SpielEventListener {

    /* Beitritt, Aussitzen und Verlassen */

    /** Ein neuer Spieler ist dem Spiel beigetreten. */
    void onPlayerSatIn(Spieler spieler, int platzNummer, int chips);

    /** Der Spieler sitzt aus, ist aber noch da; z.B. Disconnect */
    void onPlayerSatOut(Spieler spieler);

    /** Der parametrisierte Spieler hat den Tisch verlassen. */
    void onPlayerLeft(Spieler spieler);

    /* Dealer- bzw. SpielContoller-Aktionen */

    /** Der Dealer hat verdeckt allen Spielern Karten ausgeteilt. (Animation) */
    void onHoleCardsDealt();

    /** Der Dealer teilt die ersten drei Gemeinschaftskarten aus. */
    void onFlopDealt(Karte flop1, Karte flop2, Karte flop3);

    /** Der Dealer teilt die vierte Gemeinschaftskarte aus. */
    void onTurnDealt(Karte turn);

    /** Der Dealer teilt die fünfte und letzte Gemeinschaftskarte aus. */
    void onRiverDealt(Karte river);

    /** Geld verteilen && Board aufräumen */
    void onHandEnded();

    /** Der Dealer-Button wird an den parametrisierten Spieler gegeben. */
    void onButtonGesetzt(Spieler spieler);

    /** Dieser Spieler muss jetzt handeln und hat dafür X Sekunden Zeit. */
    void onAktiverSpielerGesetzt(Spieler spieler, int sekunden);

    /** Dieser Spieler muss als letzter handeln. */
    void onLetztenSpielerGesetzt(Spieler spieler);

    /** Bei einem Sit & Go ein neues Blind-Level verkünden. */
    void onBlindLevelChanged(BlindHoehe blindHoehe);

    /* Spieler-Aktionen */

    /** Der Spieler schmeißt seine Karten weg. */
    void onPlayerFold(Spieler spieler);

    /** Der Spieler schiebt. */
    void onPlayerCheck(Spieler spieler);

    /** Der Spieler bietet. */
    void onPlayerBet(Spieler spieler, int chips);

    /** Der Spieler raised. */
    void onPlayerRaise(Spieler spieler, int chips);

    /** Der parametrisierte Spieler zeigt seine Karten. (Showdown) */
    /*
     * Über diese Methode wird dem aktuellen Benutzer auch die Karten seines
     * Spielers am Tisch gezeigt.
     */
    void onHoleCardsShown(Spieler spieler, Karte holeCard1, Karte holeCard2);

}
