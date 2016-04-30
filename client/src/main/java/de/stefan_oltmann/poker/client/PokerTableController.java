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
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import de.stefan_oltmann.poker.model.BlindHoehe;
import de.stefan_oltmann.poker.model.Spiel;
import de.stefan_oltmann.poker.model.SpielEventListener;
import de.stefan_oltmann.poker.model.Spieler;
import de.stefan_oltmann.poker.model.hand.Karte;

public class PokerTableController implements Initializable, SpielEventListener {

    private Spiel spiel;

    public void setSpiel(Spiel spiel) {
        this.spiel = spiel;
    }

    @FXML
    private Button foldButton;

    @FXML
    private Button checkButton;

    @FXML
    private Button betButton;

    @FXML
    private Button sitDownPlayer1;

    @FXML
    private Button sitDownPlayer2;

    @FXML
    private Button sitDownPlayer3;

    @FXML
    private Button sitDownPlayer4;

    @FXML
    private Button sitDownPlayer5;

    @FXML
    private Button sitDownPlayer6;

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
    private void sitDownPlayer1(ActionEvent event) {

        spiel.sitIn(null, 0, 0);
    }

    @FXML
    private void sitDownPlayer2(ActionEvent event) {

        spiel.sitIn(null, 1, 0);
    }

    @FXML
    private void sitDownPlayer3(ActionEvent event) {

        spiel.sitIn(null, 2, 0);
    }

    @FXML
    private void sitDownPlayer4(ActionEvent event) {

        spiel.sitIn(null, 3, 0);
    }

    @FXML
    private void sitDownPlayer5(ActionEvent event) {

        spiel.sitIn(null, 4, 0);
    }

    @FXML
    private void sitDownPlayer6(ActionEvent event) {

        spiel.sitIn(null, 5, 0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Hier muss nichts gemacht werden.
    }

    /*
     * SpielEventListener: Aktualisierung der Oberfläche
     */

    @Override
    public void onPlayerSatIn(Spieler spieler, int platzNummer, int chips) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerSatOut(Spieler spieler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerLeft(Spieler spieler) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

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
