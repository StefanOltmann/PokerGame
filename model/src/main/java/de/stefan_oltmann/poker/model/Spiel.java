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
 * Das Spiel ist die zentrale Entität, die jeweils vom Server als auch vom
 * Client gehalten und durch Events aktualisiert wird. Über dieses Interface
 * "Spiel" wird es manipuliert und auf Änderungen kann durch Registrieren eines
 * SpielEventListeners gehört werden.
 * 
 * Der Client-Code ist deshalb gegen ein Interface implementiert, weil
 * er im Offline-Modus (und für Unit Tests) direkt mit der SpielImpl redet
 * und ansonsten transparent durch WebSockets.
 * 
 * Dieses Interface ist eine Auflistung der möglichen Aktionen in einem
 * Pokerspiel.
 */
public interface Spiel {

    /* Listener-Verwaltung */

    void removeListener(SpielEventListener listener);

    void addListener(SpielEventListener listener);

    /* Beitritt, Aussitzen und Verlassen */

    /** Ein neuer Spieler ist dem Spiel beigetreten. */
    void sitIn(Spieler spieler, int platzNummer, int chips);

    /** Der Spieler sitzt aus, ist aber noch da; z.B. Disconnect */
    void sitOut(Spieler spieler);

    /** Der parametrisierte Spieler hat den Tisch verlassen. */
    void leave(Spieler spieler);

    /* Dealer- bzw. SpielContoller-Aktionen */

    /** Der Dealer hat verdeckt allen Spielern Karten ausgeteilt. (Animation) */
    void dealHoleCards();

    /** Der Dealer teilt die ersten drei Gemeinschaftskarten aus. */
    void dealFlop(Karte flop1, Karte flop2, Karte flop3);

    /** Der Dealer teilt die vierte Gemeinschaftskarte aus. */
    void dealTurn(Karte turn);

    /** Der Dealer teilt die fünfte und letzte Gemeinschaftskarte aus. */
    void dealRiver(Karte river);

    /** Geld verteilen && Board aufräumen */
    void endHand();

    /** Der Dealer-Button wird an den parametrisierten Spieler gegeben. */
    void setzeButton(Spieler spieler);

    /** Dieser Spieler muss jetzt handeln und hat dafür X Sekunden Zeit. */
    void setzeAktivenSpieler(Spieler spieler, int sekunden);

    /** Dieser Spieler muss als letzter handeln. */
    void setzeLetztenSpieler(Spieler spieler);

    /** Bei einem Sit & Go ein neues Blind-Level verkünden. */
    void changeBlindLevel(BlindHoehe blindHoehe);

    /* Spieler-Aktionen */

    /** Der Spieler schmeißt seine Karten weg. */
    void fold(Spieler spieler);

    /** Der Spieler schiebt. */
    void check(Spieler spieler);

    /** Der Spieler bietet. */
    void bet(Spieler spieler, int chips);

    /** Der Spieler raised. */
    void raise(Spieler spieler, int chips);

    /** Der parametrisierte Spieler zeigt seine Karten. (Showdown) */
    /*
     * Über diese Methode wird dem aktuellen Benutzer auch die Karten seines
     * Spielers am Tisch gezeigt.
     */
    void showHoleCards(Spieler spieler, Karte holeCard1, Karte holeCard2);

}
