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
package de.stefan_oltmann.poker.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;

import de.stefan_oltmann.poker.commons.ClientJSonEventSender;
import de.stefan_oltmann.poker.commons.MessageSender;
import de.stefan_oltmann.poker.model.Account;
import de.stefan_oltmann.poker.model.SpielImpl;
import de.stefan_oltmann.poker.model.Spieler;
import de.stefan_oltmann.poker.model.dto.CanLoadSpieler;
import de.stefan_oltmann.poker.model.dto.ServerMessage;

public class PokerClient extends Application implements MessageSender {

    private WebSocketClient webSocketClient;

    /*
     * Hier werden alle Spieler mit denen der Client Kontakt seit
     * Start hatte vermerkt. Es gibt keine Methode zum Abruf aller
     * Spieler-Informationen an einem Tisch.... Hmm...
     * 
     * TODO Ist das ein gutes Konzept?
     */
    private Map<String, Spieler> bekannteSpielerMap = new HashMap<>();

    /*
     * Informationen, die seit Start über fremde Accounts gesammelt
     * werden konnten.
     */
    private Map<String, Account> bekannteAccountsMap = new HashMap<>();

    private Gson gson = new Gson();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Stefan Oltmann Poker Prototype");

        /*
         * Dummy Spiel, solange es keine Tisch-Auswahl und Erstellungsmenüs
         * dafür gibt.
         */
        SpielImpl spiel = new SpielImpl("1");

        final CanLoadSpieler canLoadSpieler = new CanLoadSpieler() {

            @Override
            public Spieler findSpielerById(String accountId, String spielId) {

                String spielerId = accountId + "_" + spielId;

                Spieler spieler = bekannteSpielerMap.get(spielerId);

                if (spieler == null) {

                    Account account = bekannteAccountsMap.get(accountId);

                    if (account == null)
                        bekannteAccountsMap.put(accountId, account = new Account(accountId, "John Doe", 0));

                    bekannteSpielerMap.put(spielerId, spieler = new Spieler(account, spiel));
                }

                return spieler;
            }
        };

        webSocketClient = new WebSocketClient(new URI("ws://localhost:8887"), new Draft_17()) {

            @Override
            public void onMessage(String message) {

                ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);

                System.out.println("MESSAGE RECEIVED: " + message + " -> " + serverMessage);

                /*
                 * Aktualisiere das lokale Spiel aufgrund der Rückmeldung vom
                 * Server.
                 */
                serverMessage.fuehreAusAuf(spiel, canLoadSpieler);
            }

            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("SERVER CONNECTION ESTABLISHED");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.err.println("SERVER DISCONNECTED");
            }

            @Override
            public void onError(Exception ex) {
                System.err.println("Serverfehler: " + ex.getMessage());
                ex.printStackTrace();
            }
        };

        webSocketClient.connect();

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream("pokertable.fxml"));
        PokerTableController pokerTableController = (PokerTableController) loader.getController();

        /*
         * 
         */
        ClientJSonEventSender eventSender = new ClientJSonEventSender(spiel.getId(), this);

        /*
         * Der UI Controller bearbeitet den JSonEventSender, der das Interface
         * "Spiel" implementiert.
         */
        pokerTableController.setSpiel(eventSender);

        /*
         * Der UI Controller bekommt Änderungen seines Zustands über den
         * Server mitgeteilt und wendet diese direkt bei sich an.
         * 
         * Dadurch, dass diese über den Listener des Servers kommen können wir
         * sicher sein, dass der Server sie auch wirklich verbucht hat und es
         * kommt zu keinen inkonsistenten Zuständen aufgrund verlorener
         * Nachrichten.
         * 
         * Als Beispiel geht eine Bet an den JSonEventSender (implementiert
         * "Spiel"), wird vom Server verbucht, allen Clients (inklusive dieser
         * Instanz) mitgeteilt und die Oberfläche entsprechend aktualisiert.
         */
        spiel.addListener(pokerTableController);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {

                /* WebSocket wieder schließen. */
                webSocketClient.close();

                /* Ganze Anwendung beenden. */
                Platform.exit();
            }
        });
    }

    @Override
    public void send(String message) {
        webSocketClient.send(message);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
