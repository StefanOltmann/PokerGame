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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
import de.stefan_oltmann.poker.model.SpielImpl;
import de.stefan_oltmann.poker.model.Spieler;
import de.stefan_oltmann.poker.model.dto.CanLoadSpieler;
import de.stefan_oltmann.poker.model.dto.ServerMessage;
import de.stefan_oltmann.poker.model.dto.ServerMessage.MessageType;

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

    private String accountId;

    private File accountIdFile = new File(System.getProperty("user.home") + "/stefans_poker_accountid");

    private Gson gson = new Gson();

    private String findAccountId() {

        if (accountIdFile.exists() && accountIdFile.length() > 0) {

            try {

                Scanner scanner = new Scanner(accountIdFile);

                String accountId = scanner.next();

                scanner.close();

                return accountId;

            } catch (FileNotFoundException fnfe) {
                /* Kann durch vorherige Prüfung nicht auftreten. */
                return null;
            }
        }

        return null;
    }

    private void writeAccountId(String accountId) throws FileNotFoundException {

        PrintWriter printWriter = new PrintWriter(accountIdFile);
        printWriter.write(accountId);
        printWriter.flush();
        printWriter.close();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Stefan Oltmann Poker Prototype");

        this.accountId = findAccountId();

        /*
         * Dummy Spiel, solange es keine Tisch-Auswahl und Erstellungsmenüs
         * dafür gibt.
         */
        SpielImpl spiel = new SpielImpl("1");

        final CanLoadSpieler canLoadSpieler = new CanLoadSpieler() {

            @Override
            public Spieler findSpielerById(String spielerId) {

                Spieler spieler = bekannteSpielerMap.get(spielerId);

                /*
                 * Wenn der Spieler nicht in der Map steht, hat der Client
                 * eine SIT-IN-Nachricht verpasst.
                 */
                if (spieler == null)
                    throw new IllegalStateException("Unbekannter Spieler ID " + spielerId);

                return spieler;
            }
        };

        webSocketClient = new WebSocketClient(new URI("ws://localhost:8887"), new Draft_17()) {

            @Override
            public void onMessage(String message) {

                ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);

                System.out.println("MESSAGE RECEIVED: " + message + " -> " + serverMessage);

                if (serverMessage.getTyp() == MessageType.ERROR) {
                    System.err.println("Server meldet Fehler: " + serverMessage.getMessage());
                    return;
                }

                if (serverMessage.getTyp() == MessageType.LOGIN) {
                    System.out.println("LOGIN am Server erfolgreich.");
                    return;
                }

                if (serverMessage.getTyp() == MessageType.CREATE_ACCOUNT) {

                    System.out.println("Account-Erstellung erfolgreich: " + serverMessage.getAccountId());

                    try {
                        writeAccountId(serverMessage.getAccountId());
                    } catch (FileNotFoundException fnfe) {
                        // TODO FIXME Was tun, wenn wir die ID nicht schreiben
                        // können?
                        fnfe.printStackTrace();
                    }

                    return;
                }

                /*
                 * Beim Sit-In die Information über den Spieler für unsere
                 * bekannteSpielerMap mitschneiden.
                 */
                if (serverMessage.getTyp() == MessageType.SIT_IN) {

                    bekannteSpielerMap.put(serverMessage.getSpielerId(),
                            new Spieler(serverMessage.getSpielerId(), serverMessage.getNickName(), serverMessage.getSpielId()));
                }

                /*
                 * Aktualisiere das lokale Spiel aufgrund der Rückmeldung vom
                 * Server.
                 */
                serverMessage.fuehreAusAuf(spiel, canLoadSpieler);
            }

            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("SERVER CONNECTION ESTABLISHED");

                /*
                 * Jetzt, da wir verbunden sind: Logge mit bestehender ID ein
                 * oder versuche vom Server eine Account-ID zu erhalten.
                 */
                if (accountId != null) {

                    ServerMessage serverMessage = new ServerMessage();
                    serverMessage.setTyp(MessageType.LOGIN);
                    serverMessage.setAccountId(accountId);

                    send(gson.toJson(serverMessage));

                } else {

                    ServerMessage serverMessage = new ServerMessage();
                    serverMessage.setTyp(MessageType.CREATE_ACCOUNT);

                    send(gson.toJson(serverMessage));
                }
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
