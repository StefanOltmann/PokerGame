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
package de.stefan_oltmann.poker.commons;

import com.google.gson.Gson;

import de.stefan_oltmann.poker.model.BlindHoehe;
import de.stefan_oltmann.poker.model.Spiel;
import de.stefan_oltmann.poker.model.SpielEventListener;
import de.stefan_oltmann.poker.model.Spieler;
import de.stefan_oltmann.poker.model.dto.ServerMessage;
import de.stefan_oltmann.poker.model.dto.ServerMessage.MessageType;
import de.stefan_oltmann.poker.model.hand.Karte;

/**
 * Hier passieren zwei Dinge:
 * Weitergabe an das lokale Spiel zur Aktualisierung sowie
 * Weitergabe an alle verbundenen WebSockets.
 * 
 * Aus Sicht des Clients ist dies ein Spiel.
 * Aus Sicht des Servers ist dies ein Listener, der verbuchte
 * Änderungen an seiner Spiel-Instanz an alle Clients weitergibt.
 */
public class ClientJSonEventSender implements Spiel {

    private String spielId;
    private MessageSender messageSender;

    private Gson gson = new Gson();

    /*
     * Wir verlangen im Konstruktor lediglich die ID des Spiels und
     * nicht das Spiel selber, damit klar ist, dass wir keine Änderungen
     * am Spiel vornehmen werden und auch keine Methoden darauf aufrufen.
     * Wir wollen die Spiel-ID lediglich als Metadaten mitgeben.
     */
    public ClientJSonEventSender(String spielId, MessageSender messageSender) {
        this.spielId = spielId;
        this.messageSender = messageSender;
    }

    private static ServerMessage createMessage(String spielId, Spieler spieler) {

        ServerMessage serverMessage = new ServerMessage();
        serverMessage.setSpielId(spielId);

        /*
         * Einige Aktionen wie Austeilung des Flops gelten für alle
         * Spieler und er ist daher nicht gesetzt.
         * 
         * Der Client lässt den Spieler grundsätzlich weg, weil dieser aus
         * Sicherheitsgründen sowieso vom Server festgestellt und gesetzt wird.
         */
        if (spieler != null)
            serverMessage.setAccountId(spieler.getAccount().getId());

        return serverMessage;
    }

    @Override
    public void addListener(SpielEventListener listener) {
        throw new RuntimeException("Ungültiger Aufruf.");
    }

    @Override
    public void removeListener(SpielEventListener listener) {
        throw new RuntimeException("Ungültiger Aufruf.");
    }

    @Override
    public void sitIn(Spieler spieler, int platzNummer, int chips) {

        ServerMessage serverMessage = createMessage(spielId, spieler);
        serverMessage.setTyp(MessageType.SIT_IN);
        serverMessage.setPlatzNummer(platzNummer);
        serverMessage.setChips(chips);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void sitOut(Spieler spieler) {

        ServerMessage serverMessage = createMessage(spielId, spieler);
        serverMessage.setTyp(MessageType.SIT_OUT);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void leave(Spieler spieler) {

        ServerMessage serverMessage = createMessage(spielId, spieler);
        serverMessage.setTyp(MessageType.LEAVE);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void dealHoleCards() {

        ServerMessage serverMessage = createMessage(spielId, null);
        serverMessage.setTyp(MessageType.DEAL_HOLE_CARDS);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void dealFlop(Karte flop1, Karte flop2, Karte flop3) {

        ServerMessage serverMessage = createMessage(spielId, null);
        serverMessage.setTyp(MessageType.DEAL_FLOP);
        serverMessage.setFlop1(flop1);
        serverMessage.setFlop2(flop2);
        serverMessage.setFlop3(flop3);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void dealTurn(Karte turn) {

        ServerMessage serverMessage = createMessage(spielId, null);
        serverMessage.setTyp(MessageType.DEAL_TURN);
        serverMessage.setTurn(turn);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void dealRiver(Karte river) {

        ServerMessage serverMessage = createMessage(spielId, null);
        serverMessage.setTyp(MessageType.DEAL_RIVER);
        serverMessage.setTurn(river);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void endHand() {

        ServerMessage serverMessage = createMessage(spielId, null);
        serverMessage.setTyp(MessageType.END_HAND);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void setzeButton(Spieler spieler) {

        ServerMessage serverMessage = createMessage(spielId, null);
        serverMessage.setTyp(MessageType.SETZE_BUTTON);
        serverMessage.setButton(spieler);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void setzeAktivenSpieler(Spieler spieler, int sekunden) {

        ServerMessage serverMessage = createMessage(spielId, null);
        serverMessage.setTyp(MessageType.SETZE_AKTIVEN_SPIELER);
        serverMessage.setAktiverSpieler(spieler);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void setzeLetztenSpieler(Spieler spieler) {

        ServerMessage serverMessage = createMessage(spielId, null);
        serverMessage.setTyp(MessageType.SETZE_LETZTEN_SPIELER);
        serverMessage.setLetzterSpieler(spieler);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void changeBlindLevel(BlindHoehe blindHoehe) {

        ServerMessage serverMessage = createMessage(spielId, null);
        serverMessage.setTyp(MessageType.CHANGE_BLIND_LEVEL);
        serverMessage.setBlindHoehe(blindHoehe);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void fold(Spieler spieler) {

        ServerMessage serverMessage = createMessage(spielId, spieler);
        serverMessage.setTyp(MessageType.FOLD);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void check(Spieler spieler) {

        ServerMessage serverMessage = createMessage(spielId, spieler);
        serverMessage.setTyp(MessageType.CHECK);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void bet(Spieler spieler, int chips) {

        ServerMessage serverMessage = createMessage(spielId, spieler);
        serverMessage.setTyp(MessageType.BET);
        serverMessage.setChips(chips);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void raise(Spieler spieler, int chips) {

        ServerMessage serverMessage = createMessage(spielId, spieler);
        serverMessage.setTyp(MessageType.RAISE);
        serverMessage.setChips(chips);

        messageSender.send(gson.toJson(serverMessage));
    }

    @Override
    public void showHoleCards(Spieler spieler, Karte holeCard1, Karte holeCard2) {

        ServerMessage serverMessage = createMessage(spielId, spieler);
        serverMessage.setTyp(MessageType.SHOW_HOLE_CARDS);
        serverMessage.setHoleCard1(holeCard1);
        serverMessage.setHoleCard2(holeCard2);

        messageSender.send(gson.toJson(serverMessage));
    }
}
