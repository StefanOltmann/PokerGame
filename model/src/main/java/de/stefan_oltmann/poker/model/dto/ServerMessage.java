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
package de.stefan_oltmann.poker.model.dto;

import de.stefan_oltmann.poker.model.BlindHoehe;
import de.stefan_oltmann.poker.model.Spiel;
import de.stefan_oltmann.poker.model.Spieler;
import de.stefan_oltmann.poker.model.hand.Karte;

public class ServerMessage {

    private MessageType typ;

    /* Gesetzt wenn es um ein Spiel geht. */
    private String spielId;
    private String spielerId;

    /* Gesetzt beim Typ ERROR */
    private String message;

    /*
     * Gesetzt, wenn es um Login oder Erstellung
     * eines Accounts geht.
     */
    private String accountId;

    /** Gesetzt bei Typ SIT_IN */
    private Integer platzNummer;

    /** Gesetzt bei Typ SIT_IN oder CHANGE_NICK */
    private String nickName;

    /** Gesetzte bei SIT_IN, BET, RAISE und ASK_BALANCE */
    private Integer chips;

    /** Gesetzt bei DEAL_FLOP */
    private Karte flop1;
    private Karte flop2;
    private Karte flop3;

    /** Gesetzt bei DEAL_TURN */
    private Karte turn;

    /** Gesetzt bei DEAL_RIVER */
    private Karte river;

    /** Gesetzte bei SHOW_HOLE_CARDS */
    private Karte holeCard1;
    private Karte holeCard2;

    /** Gesetzt bei SETZE_BUTTON */
    private Spieler button;

    /** Gesetzt bei SETZE_AKTIVEN_SPIELER */
    private Spieler aktiverSpieler;

    /** Gesetzt bei SETZE_LETZTEN_SPIELER */
    private Spieler letzterSpieler;

    /** Gesetzte bei CHANGE_BLIND_LEVEL */
    private BlindHoehe blindHoehe;

    /*
     * Getters + Setters
     */

    public MessageType getTyp() {
        return typ;
    }

    public void setTyp(MessageType typ) {
        this.typ = typ;
    }

    public String getSpielId() {
        return spielId;
    }

    public String getSpielerId() {
        return spielerId;
    }

    public void setSpielerId(String spielerId) {
        this.spielerId = spielerId;
    }

    public void setSpielId(String spielId) {
        this.spielId = spielId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPlatzNummer() {
        return platzNummer;
    }

    public void setPlatzNummer(Integer platzNummer) {
        this.platzNummer = platzNummer;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getChips() {
        return chips;
    }

    public void setChips(Integer chips) {
        this.chips = chips;
    }

    public Karte getFlop1() {
        return flop1;
    }

    public void setFlop1(Karte flop1) {
        this.flop1 = flop1;
    }

    public Karte getFlop2() {
        return flop2;
    }

    public void setFlop2(Karte flop2) {
        this.flop2 = flop2;
    }

    public Karte getFlop3() {
        return flop3;
    }

    public void setFlop3(Karte flop3) {
        this.flop3 = flop3;
    }

    public Karte getTurn() {
        return turn;
    }

    public void setTurn(Karte turn) {
        this.turn = turn;
    }

    public Karte getRiver() {
        return river;
    }

    public void setRiver(Karte river) {
        this.river = river;
    }

    public Karte getHoleCard1() {
        return holeCard1;
    }

    public void setHoleCard1(Karte holeCard1) {
        this.holeCard1 = holeCard1;
    }

    public Karte getHoleCard2() {
        return holeCard2;
    }

    public void setHoleCard2(Karte holeCard2) {
        this.holeCard2 = holeCard2;
    }

    public Spieler getButton() {
        return button;
    }

    public void setButton(Spieler button) {
        this.button = button;
    }

    public Spieler getAktiverSpieler() {
        return aktiverSpieler;
    }

    public void setAktiverSpieler(Spieler aktiverSpieler) {
        this.aktiverSpieler = aktiverSpieler;
    }

    public Spieler getLetzterSpieler() {
        return letzterSpieler;
    }

    public void setLetzterSpieler(Spieler letzterSpieler) {
        this.letzterSpieler = letzterSpieler;
    }

    public BlindHoehe getBlindHoehe() {
        return blindHoehe;
    }

    public void setBlindHoehe(BlindHoehe blindHoehe) {
        this.blindHoehe = blindHoehe;
    }

    /*
     * Innere Klassen
     */

    public enum MessageType {

        /* Allgemein */

        ERROR,

        /* Account-Verwaltung */

        /* Erstellung eines neuen Accounts */
        CREATE_ACCOUNT,

        /* Login mit bestehendem Account */
        LOGIN,

        /* Abfrage der verfügbaren Chips */
        ASK_BALANCE,

        /* Änderung des Nickname */
        CHANGE_NICK,

        /* Beitritt, Aussitzen und Verlassen */

        /** Beitritt zu einem Spiel */
        SIT_IN,

        /** Ausitzen */
        SIT_OUT,

        /** Verlassen des Spiels/Tisches */
        LEAVE,

        /* Dealer- bzw. SpielContoller-Aktionen */

        /**
         * Der Dealer hat verdeckt allen Spielern Karten ausgeteilt. (Animation)
         */
        DEAL_HOLE_CARDS,

        /** Der Dealer teilt die ersten drei Gemeinschaftskarten aus. */
        DEAL_FLOP,

        /** Der Dealer teilt die vierte Gemeinschaftskarte aus. */
        DEAL_TURN,

        /** Der Dealer teilt die fünfte und letzte Gemeinschaftskarte aus. */
        DEAL_RIVER,

        /** Geld verteilen && Board aufräumen */
        END_HAND,

        /** Der Dealer-Button wird an den parametrisierten Spieler gegeben. */
        SETZE_BUTTON,

        /** Dieser Spieler muss jetzt handeln und hat dafür X Sekunden Zeit. */
        SETZE_AKTIVEN_SPIELER,

        /** Dieser Spieler muss als letzter handeln. */
        SETZE_LETZTEN_SPIELER,

        /** Bei einem Sit&Go ein neues Blind-Level verkünden. */
        CHANGE_BLIND_LEVEL,

        /* Spieler-Aktionen */

        /** Der Spieler schmeißt seine Karten weg. */
        FOLD,

        /** Der Spieler schiebt. */
        CHECK,

        /** Der Spieler bietet. */
        BET,

        /** Der Spieler raised. */
        RAISE,

        /** Der parametrisierte Spieler zeigt seine Karten. (Showdown) */
        SHOW_HOLE_CARDS
    }

    public void fuehreAusAuf(Spiel spiel, CanLoadSpieler canLoadSpieler) {

        /*
         * TODO Implemenent
         */

        Spieler spieler = canLoadSpieler.findSpielerById(spielerId);

        switch (typ) {

        /* Beitritt, Aussitzen und Verlassen */

        case SIT_IN:
            spiel.sitIn(spieler, platzNummer, chips);
            break;

        case SIT_OUT:
            spiel.sitOut(spieler);
            break;

        case LEAVE:
            spiel.leave(spieler);
            break;

        /* Dealer- bzw. SpielContoller-Aktionen */

        case DEAL_HOLE_CARDS:
            spiel.dealHoleCards();
            break;

        case DEAL_FLOP:
            spiel.dealFlop(flop1, flop2, flop3);
            break;

        case DEAL_TURN:
            spiel.dealTurn(turn);
            break;

        case DEAL_RIVER:
            spiel.dealRiver(river);
            break;

        case END_HAND:
            spiel.endHand();
            break;

        case SETZE_BUTTON:
            spiel.setzeButton(spieler);
            break;

        case SETZE_AKTIVEN_SPIELER:
            spiel.setzeAktivenSpieler(spieler, 15);
            break;

        case SETZE_LETZTEN_SPIELER:
            spiel.setzeLetztenSpieler(spieler);
            break;

        case CHANGE_BLIND_LEVEL:
            spiel.changeBlindLevel(blindHoehe);
            break;

        /* Spieler-Aktionen */

        case FOLD:
            spiel.fold(spieler);
            break;

        case CHECK:
            spiel.check(spieler);
            break;

        case BET:
            spiel.bet(spieler, chips);
            break;

        case RAISE:
            spiel.raise(spieler, chips);
            break;

        case SHOW_HOLE_CARDS:
            spiel.showHoleCards(spieler, holeCard1, holeCard2);
            break;

        default:
            throw new IllegalStateException("Die Nachricht " + this
                    + " kann nicht auf das Spiel angewendet werden.");
        }
    }

    /*
     * toString()
     */

    @Override
    public String toString() {
        return "ServerMessage [typ=" + typ + ", spielId=" + spielId + ", spielerId=" + spielerId + ", message=" + message + ", accountId=" + accountId + ", platzNummer=" + platzNummer + ", nickName=" + nickName + ", chips=" + chips + ", flop1=" + flop1 + ", flop2=" + flop2 + ", flop3=" + flop3 + ", turn=" + turn + ", river=" + river + ", holeCard1=" + holeCard1 + ", holeCard2=" + holeCard2 + ", button=" + button + ", aktiverSpieler=" + aktiverSpieler + ", letzterSpieler=" + letzterSpieler + ", blindHoehe=" + blindHoehe + "]";
    }
}
