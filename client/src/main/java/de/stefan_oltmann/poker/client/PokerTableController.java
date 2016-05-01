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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import de.stefan_oltmann.poker.model.BlindHoehe;
import de.stefan_oltmann.poker.model.Spiel;
import de.stefan_oltmann.poker.model.SpielEventListener;
import de.stefan_oltmann.poker.model.Spieler;
import de.stefan_oltmann.poker.model.hand.Karte;

public class PokerTableController implements Initializable, SpielEventListener {

    private Spiel spiel;

    private Map<Spieler, Integer> platzNummernMap = new HashMap<>();

    private Map<Integer, Text> playerNameLabelPerPlatznummerMap = new HashMap<>();
    private Map<Integer, Text> playerMoneyLabelPerPlatznummerMap = new HashMap<>();

    public void setSpiel(Spiel spiel) {
        this.spiel = spiel;
    }

    /*
     * Spieler 1
     */

    @FXML
    private Button sitInPlayer1;

    @FXML
    private Text playerName1;

    @FXML
    private Text playerMoney1;

    /*
     * Spieler 2
     */

    @FXML
    private Button sitInPlayer2;

    @FXML
    private Text playerName2;

    @FXML
    private Text playerMoney2;

    /*
     * Spieler 3
     */

    @FXML
    private Button sitInPlayer3;

    @FXML
    private Text playerName3;

    @FXML
    private Text playerMoney3;

    /*
     * Spieler 4
     */

    @FXML
    private Button sitInPlayer4;

    @FXML
    private Text playerName4;

    @FXML
    private Text playerMoney4;

    /*
     * Spieler 5
     */

    @FXML
    private Button sitInPlayer5;

    @FXML
    private Text playerName5;

    @FXML
    private Text playerMoney5;

    /*
     * Spieler 6
     */

    @FXML
    private Button sitInPlayer6;

    @FXML
    private Text playerName6;

    @FXML
    private Text playerMoney6;

    /*
     * Fold/Check/Bet/Raise-Controls
     */

    @FXML
    private Button foldButton;

    @FXML
    private Button checkButton;

    @FXML
    private Button betButton;

    @FXML
    private Slider betSlider;

    @FXML
    private Label betAmountLabel;

    /*
     * Events der Controls
     */

    @FXML
    private void foldButtonPressed(ActionEvent event) {
        System.out.println("FOLD");
    }

    @FXML
    private void checkButtonPressed(ActionEvent event) {
        System.out.println("CHECK");
    }

    @FXML
    private void betButtonPressed(ActionEvent event) {
        System.out.println("BET");
    }

    @FXML
    private void minBetButtonPressed(ActionEvent event) {

        betSlider.setValue(10);
    }

    @FXML
    private void halfPotBetButtonPressed(ActionEvent event) {

        betSlider.setValue(25);
    }

    @FXML
    private void potBetButtonPressed(ActionEvent event) {

        betSlider.setValue(50);
    }

    @FXML
    private void allInBetButtonPressed(ActionEvent event) {

        betSlider.setValue(100);
    }

    /*
     * Actions zur Spielteilnahme
     */

    @FXML
    private void sitInPlayer1(ActionEvent event) {

        spiel.sitIn(null, 0, 0);
    }

    @FXML
    private void sitInPlayer2(ActionEvent event) {

        spiel.sitIn(null, 1, 0);
    }

    @FXML
    private void sitInPlayer3(ActionEvent event) {

        spiel.sitIn(null, 2, 0);
    }

    @FXML
    private void sitInPlayer4(ActionEvent event) {

        spiel.sitIn(null, 3, 0);
    }

    @FXML
    private void sitInPlayer5(ActionEvent event) {

        spiel.sitIn(null, 4, 0);
    }

    @FXML
    private void sitInPlayer6(ActionEvent event) {

        spiel.sitIn(null, 5, 0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /*
         * Zur einfacheren Verwaltung die Controls per Platznummer in die Map
         * schreiben.
         */

        playerNameLabelPerPlatznummerMap.put(0, playerName1);
        playerNameLabelPerPlatznummerMap.put(1, playerName2);
        playerNameLabelPerPlatznummerMap.put(2, playerName3);
        playerNameLabelPerPlatznummerMap.put(3, playerName4);
        playerNameLabelPerPlatznummerMap.put(4, playerName5);
        playerNameLabelPerPlatznummerMap.put(5, playerName6);

        playerMoneyLabelPerPlatznummerMap.put(0, playerMoney1);
        playerMoneyLabelPerPlatznummerMap.put(1, playerMoney2);
        playerMoneyLabelPerPlatznummerMap.put(2, playerMoney3);
        playerMoneyLabelPerPlatznummerMap.put(3, playerMoney4);
        playerMoneyLabelPerPlatznummerMap.put(4, playerMoney5);
        playerMoneyLabelPerPlatznummerMap.put(5, playerMoney6);

        /* Der Wert des Bet-Sliders soll auf dem Label daneben angezeigt werden. */
        betSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                betAmountLabel.setText(" " + newValue.intValue() + " ");
            }
        });
    }

    /*
     * SpielEventListener: Aktualisierung der Oberfläche
     */

    @Override
    public void onPlayerSatIn(Spieler spieler, int platzNummer, int chips) {

        String nickName = spieler.getAccount().getNickname();

        if (nickName == null)
            nickName = "John Doe";

        platzNummernMap.put(spieler, platzNummer);

        Text playerNameLabel = playerNameLabelPerPlatznummerMap.get(platzNummer);
        Text playerMoneyLabel = playerMoneyLabelPerPlatznummerMap.get(platzNummer);

        playerNameLabel.setText(nickName);
        playerMoneyLabel.setText(" " + chips + " ");
    }

    @Override
    public void onPlayerSatOut(Spieler spieler) {
    }

    @Override
    public void onPlayerLeft(Spieler spieler) {

        int platzNummer = platzNummernMap.get(spieler);

        Text playerNameLabel = playerNameLabelPerPlatznummerMap.get(platzNummer);
        Text playerMoneyLabel = playerMoneyLabelPerPlatznummerMap.get(platzNummer);

        playerNameLabel.setText("Freier Platz");
        playerMoneyLabel.setText("SIT IN");
    }

    @Override
    public void onHoleCardsDealt() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlopDealt(Karte flop1, Karte flop2, Karte flop3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTurnDealt(Karte turn) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRiverDealt(Karte river) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onHandEnded() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onButtonGesetzt(Spieler spieler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAktiverSpielerGesetzt(Spieler spieler, int sekunden) {

    }

    @Override
    public void onLetztenSpielerGesetzt(Spieler spieler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBlindLevelChanged(BlindHoehe blindHoehe) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerFold(Spieler spieler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerCheck(Spieler spieler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerBet(Spieler spieler, int chips) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerRaise(Spieler spieler, int chips) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onHoleCardsShown(Spieler spieler, Karte holeCard1, Karte holeCard2) {
        // TODO Auto-generated method stub

    }
}
