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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import de.stefan_oltmann.poker.model.Account;
import de.stefan_oltmann.poker.model.BlindHoehe;
import de.stefan_oltmann.poker.model.SpielEventListener;
import de.stefan_oltmann.poker.model.SpielImpl;
import de.stefan_oltmann.poker.model.Spieler;
import de.stefan_oltmann.poker.model.SpielerStatus;
import de.stefan_oltmann.poker.model.hand.Karte;

public class SpielControllerTest extends TestCase {

    public static final Account ALICE = new Account("TEST1", "Alice", 1500);
    public static final Account BOB = new Account("TEST2", "Bob", 1500);
    public static final Account EVE = new Account("TEST3", "Eve", 1500);

    /*
     * TODO FIXME Unfertig
     */
    public void testSpielMitDreiSpielern() {

        SpielImpl spiel = new SpielImpl("123");

        SpielLog spielLog = new SpielLog();
        spiel.addListener(spielLog);

        SpielController spielController = new SpielController(spiel);

        Spieler spielerAlice = new Spieler("1", ALICE.getId(), spiel.getId());
        Spieler spielerBob = new Spieler("2", BOB.getId(), spiel.getId());
        Spieler spielerEve = new Spieler("3", EVE.getId(), spiel.getId());

        /*
         * Status-Prüfung vor Start
         */

        assertEquals(0, spiel.getAnzahlAktiverSpieler());
        assertFalse(spielController.isSpielLaueft());
        assertNull(spiel.getDealer());
        assertNull(spiel.getAktiverSpieler());

        /* Drei Spieler treten bei uns warten auf die nächste Runde. */

        spiel.sitIn(spielerAlice, 1, 1500);
        assertEquals(1, spiel.getAnzahlAktiverSpieler());
        assertFalse(spielController.isSpielLaueft());
        assertEquals("sitIn Spieler[Account[TEST1:Alice]@Spiel[123]] platz=1 chips=1500", spielLog.getNextMessage());
        assertEquals(SpielerStatus.WARTET_AUF_NAECHSTE_RUNDE, spielerAlice.getStatus());

        spiel.sitIn(spielerBob, 2, 1500);
        assertEquals(2, spiel.getAnzahlAktiverSpieler());
        assertTrue(spielController.isSpielLaueft());
        assertEquals("sitIn Spieler[Account[TEST2:Bob]@Spiel[123]] platz=2 chips=1500", spielLog.getNextMessage());
        assertEquals(SpielerStatus.WARTET_AUF_NAECHSTE_RUNDE, spielerBob.getStatus());

        spiel.sitIn(spielerEve, 3, 1500);
        assertEquals(3, spiel.getAnzahlAktiverSpieler());
        assertTrue(spielController.isSpielLaueft());
        assertEquals("sitIn Spieler[Account[TEST3:Eve]@Spiel[123]] platz=3 chips=1500", spielLog.getNextMessage());
        assertEquals(SpielerStatus.WARTET_AUF_NAECHSTE_RUNDE, spielerEve.getStatus());

        /*
         * Das Spiel startet
         */

        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
        }

        /* Alice wird als Button ausgewählt */
        assertEquals("setzeButton Spieler[Account[TEST1:Alice]@Spiel[123]]", spielLog.getNextMessage());

        try {
            Thread.sleep(9999);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class SpielLog implements SpielEventListener {

        private List<String> messages = new ArrayList<String>();

        // public List<String> getMessages() {
        // return messages;
        // }

        public int counter = -1;

        public String getNextMessage() {

            this.counter++;

            if (counter >= messages.size())
                return null;

            return messages.get(counter);
        }

        private void addMessage(String message) {
            this.messages.add(message);
            System.out.println("addMessage(): " + message);
        }

        @Override
        public void onPlayerSatIn(Spieler spieler, int platzNummer, int chips) {
            addMessage("sitIn " + spieler + " platz=" + platzNummer + " chips=" + chips);
        }

        @Override
        public void onPlayerSatOut(Spieler spieler) {
            addMessage("sitOut " + spieler);
        }

        @Override
        public void onPlayerLeft(Spieler spieler) {
            addMessage("leave " + spieler);
        }

        @Override
        public void onHoleCardsDealt() {
            addMessage("dealHoleCards");
        }

        @Override
        public void onFlopDealt(Karte flop1, Karte flop2, Karte flop3) {
            addMessage("dealFlop " + flop1 + " " + flop2 + " " + flop3);
        }

        @Override
        public void onTurnDealt(Karte turn) {
            addMessage("dealTurn " + turn);
        }

        @Override
        public void onRiverDealt(Karte river) {
            addMessage("dealRiver " + river);
        }

        @Override
        public void onHandEnded() {
            addMessage("endHand");
        }

        @Override
        public void onButtonGesetzt(Spieler spieler) {
            addMessage("setzeButton " + spieler);
        }

        @Override
        public void onAktiverSpielerGesetzt(Spieler spieler, int sekunden) {
            addMessage("setzeAktivenSpieler " + spieler);
        }

        @Override
        public void onLetztenSpielerGesetzt(Spieler spieler) {
            addMessage("setzeLetztenSpieler " + spieler);
        }

        @Override
        public void onBlindLevelChanged(BlindHoehe blindHoehe) {
            addMessage("changeBlindLevel " + blindHoehe);
        }

        @Override
        public void onPlayerFold(Spieler spieler) {
            addMessage("fold " + spieler);
        }

        @Override
        public void onPlayerCheck(Spieler spieler) {
            addMessage("check " + spieler);
        }

        @Override
        public void onPlayerBet(Spieler spieler, int chips) {
            addMessage("bet " + spieler + " chips=" + chips);
        }

        @Override
        public void onPlayerRaise(Spieler spieler, int chips) {
            addMessage("raise " + spieler + " chips=" + chips);
        }

        @Override
        public void onHoleCardsShown(Spieler spieler, Karte holeCard1, Karte holeCard2) {
            addMessage("showHoleCards " + holeCard1 + " " + holeCard2);
        }
    }

}
