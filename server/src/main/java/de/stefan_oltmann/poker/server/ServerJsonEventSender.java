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
package de.stefan_oltmann.poker.server;

import de.stefan_oltmann.poker.commons.ClientJSonEventSender;
import de.stefan_oltmann.poker.commons.MessageSender;
import de.stefan_oltmann.poker.model.BlindHoehe;
import de.stefan_oltmann.poker.model.SpielEventListener;
import de.stefan_oltmann.poker.model.Spieler;
import de.stefan_oltmann.poker.model.hand.Karte;

public class ServerJsonEventSender extends ClientJSonEventSender implements SpielEventListener {

    public ServerJsonEventSender(String spielId, MessageSender messageSender) {
        super(spielId, messageSender);
    }

    @Override
    public void onPlayerSatIn(Spieler spieler, int platzNummer, int chips) {
        sitIn(spieler, platzNummer, chips);
    }

    @Override
    public void onPlayerSatOut(Spieler spieler) {
        sitOut(spieler);
    }

    @Override
    public void onPlayerLeft(Spieler spieler) {
        leave(spieler);
    }

    @Override
    public void onHoleCardsDealt() {
        dealHoleCards();
    }

    @Override
    public void onFlopDealt(Karte flop1, Karte flop2, Karte flop3) {
        dealFlop(flop1, flop2, flop3);
    }

    @Override
    public void onTurnDealt(Karte turn) {
        dealTurn(turn);
    }

    @Override
    public void onRiverDealt(Karte river) {
        dealRiver(river);
    }

    @Override
    public void onHandEnded() {
        endHand();
    }

    @Override
    public void onButtonGesetzt(Spieler spieler) {
        setzeButton(spieler);
    }

    @Override
    public void onAktiverSpielerGesetzt(Spieler spieler, int sekunden) {
        setzeAktivenSpieler(spieler, sekunden);
    }

    @Override
    public void onLetztenSpielerGesetzt(Spieler spieler) {
        setzeLetztenSpieler(spieler);
    }

    @Override
    public void onBlindLevelChanged(BlindHoehe blindHoehe) {
        changeBlindLevel(blindHoehe);
    }

    @Override
    public void onPlayerFold(Spieler spieler) {
        fold(spieler);
    }

    @Override
    public void onPlayerCheck(Spieler spieler) {
        check(spieler);
    }

    @Override
    public void onPlayerBet(Spieler spieler, int chips) {
        bet(spieler, chips);
    }

    @Override
    public void onPlayerRaise(Spieler spieler, int chips) {
        raise(spieler, chips);
    }

    @Override
    public void onHoleCardsShown(Spieler spieler, Karte holeCard1, Karte holeCard2) {
        showHoleCards(spieler, holeCard1, holeCard2);
    }
}
