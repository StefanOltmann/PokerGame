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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;

import de.stefan_oltmann.poker.commons.MessageSender;
import de.stefan_oltmann.poker.model.Account;
import de.stefan_oltmann.poker.model.SpielImpl;
import de.stefan_oltmann.poker.model.Spieler;
import de.stefan_oltmann.poker.model.dto.CanLoadSpieler;
import de.stefan_oltmann.poker.model.dto.ServerMessage;
import de.stefan_oltmann.poker.model.dto.ServerMessage.MessageType;
import de.stefan_oltmann.poker.server.persistence.DataAccessService;
import de.stefan_oltmann.poker.server.persistence.DataAccessServiceMapDbImpl;

public class PokerServer extends WebSocketServer implements MessageSender {

    private DataAccessService dataAccessService = DataAccessServiceMapDbImpl.getInstance();

    private SpielImpl spiel;

    private Map<WebSocket, Account> accountMap = new HashMap<>();

    private Gson gson = new Gson();

    public PokerServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));

        /* Dummy Instanz des Spiels */
        spiel = new SpielImpl("1");

        /*
         * Alle Änderungen am lokalen Spiel zwecks Synchronisation an alle
         * Clients senden.
         */
        spiel.addListener(new ServerJsonEventSender(spiel.getId(), this));

        /* SpielController fügt sicht selber als Listener hinzu. */
        new SpielController(spiel);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Neue Verbindung: " + conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Verbindung getrennt: " + conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {

        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);

        System.out.println("RECEIVED " + conn + " = " + message + " -> " + serverMessage);

        Account account = accountMap.get(conn);

        // TODO FIXME Fake-Account erstellen, damit gespielt werden kann.
        if (account == null)
            accountMap.put(conn, account = dataAccessService.erstelleNeuenAccount());

        CanLoadSpieler canLoadSpieler = new CanLoadSpieler() {

            @Override
            public Spieler findSpielerById(String accountId, String spielId) {

                /*
                 * TODO Spieler aus Registry nehmen
                 */

                return null;
            }
        };

        if (serverMessage.getTyp() == MessageType.SIT_IN) {

            int platzNummer = serverMessage.getPlatzNummer();

            int buyIn = serverMessage.getChips();

            /*
             * Wenn der Platz bereits besetzt ist, kann kein
             * Spieler für diesen Tisch kreiert werden.
             */
            if (spiel.getSpielerAn(platzNummer) == null) {

                Spieler spieler = new Spieler(account, spiel);

                account.setChipsGesamt(account.getChipsGesamt() - buyIn);

                spiel.sitIn(spieler, platzNummer, buyIn);
            }

        } else {

            serverMessage.fuehreAusAuf(spiel, canLoadSpieler);
        }
    }

    public void onFragment(WebSocket conn, Framedata fragment) {
        /* Nicht implementiert */
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        int port = 8887;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
        }

        PokerServer s = new PokerServer(port);
        s.start();

        System.out.println("PokerServer gestartet.");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void send(String message) {

        Collection<WebSocket> con = connections();

        synchronized (con) {

            for (WebSocket webSocket : con)
                webSocket.send(message);
        }
    }
}
