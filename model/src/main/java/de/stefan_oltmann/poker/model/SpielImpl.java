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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.stefan_oltmann.poker.model.hand.Karte;

/**
 * Dies ist der Zustand des aktuellen Spiels, der sowohl
 * vom Server als auch vom Client gehalten wird. Über kurze
 * Nachrichten zwischen Server und Client aktualisiert jeder
 * seine lokale Sicht auf dieses Spiel ähnlich wie man früher
 * per Brief gegeneinander Schach gespielt hat und jeder der
 * Spieler sein eigenes Schachbrett vor sich hatte und nur
 * die mitgeteilten Änderungen durchführt hat.
 * 
 * Diese Instanz ist ein Listener, der auf Änderungen reagiert,
 * die entweder vom Server selber durchgeführt oder vom Client
 * nur gesehen werden; z.B. die Austeilung des Flops.
 */
public class SpielImpl implements Spiel {

    public static final int MIN_SPIELER = 2;
    public static final int MAX_SPIELER = 6;

    private String id;

    /**
     * Events werden ausgelöst, sobald am Spiel was geändert wurde.
     * Die Listener sind der Client für Aktualisierung der Oberfläche,
     * der Server für Weitergabe von Änderungen an die Clients und
     * der SpielController zur Durchführung der Spiellogik.
     */
    private List<SpielEventListener> listeners;

    private SpielPhase phase = SpielPhase.WARTE_AUF_SPIELER;

    private BlindHoehe blindHoehe = BlindHoehe.BLIND_1_2;

    /**
     * Die Liste aller Spieler. Der Index ist die Platznummer,
     * weshalb hier NULL-Einträge enthalten sind.
     */
    private List<Spieler> alleSpieler;

    /** Ein Zähler zur Steuerung, ob gespielt wird. */
    private int anzahlAktiverSpieler = 0;

    /** Der Spieler mit dem Button */
    private Spieler dealer;

    /** Der Spieler, der gerade an der Reihe ist und handeln muss. */
    private Spieler aktiverSpieler;

    /**
     * Der letzte Spieler, der in der aktuellen Wettrunde handeln kann.
     * Falls niemand zwischendurch gebettet/geraised hat ist dies PRE_FLOP der
     * Big Blind bzw. nach dem FLOP der Dealer und andernfalls der Vorgänger
     * von demjenigen Spieler, der zuletzt geraised / gebettet hat.
     */
    private Spieler letzterSpieler;

    private Karte flop1;
    private Karte flop2;
    private Karte flop3;
    private Karte turn;
    private Karte river;

    private int chipsImPot;
    private int potHoehe;

    /* Konstruktor */

    public SpielImpl(String id) {
        this.id = id;

        /*
         * Der Index beschreibt die Platz-Nummer.
         */
        this.alleSpieler = new ArrayList<>(MAX_SPIELER);
        for (int i = 0; i < MAX_SPIELER; i++)
            alleSpieler.add(i, null);

        this.listeners = new ArrayList<>();
    }

    /*
     * **********************
     * Listener-Verwaltung
     * **********************
     */

    @Override
    public void removeListener(SpielEventListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void addListener(SpielEventListener listener) {
        this.listeners.add(listener);
    }

    /*
     * **********************
     * Listener-Methoden
     * **********************
     */

    /* Beitritt, Aussitzen und Verlassen */

    @Override
    public void sitIn(Spieler spieler, int platzNummer, int chips) {

        if (alleSpieler.get(platzNummer) != null)
            throw new IllegalArgumentException("Platz #" + platzNummer + " ist bereits besetzt.");

        alleSpieler.set(platzNummer, spieler);
        spieler.setChipsImStack(chips);

        spieler.setStatus(SpielerStatus.WARTET_AUF_NAECHSTE_RUNDE);

        this.anzahlAktiverSpieler++;

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onPlayerSatIn(spieler, platzNummer, chips);
    }

    @Override
    public void sitOut(Spieler spieler) {

        spieler.setStatus(SpielerStatus.SITZT_AUS);

        this.anzahlAktiverSpieler--;

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onPlayerSatOut(spieler);
    }

    @Override
    public void leave(Spieler spieler) {

        if (!alleSpieler.contains(spieler))
            throw new IllegalArgumentException("Spieler " + spieler + " ist gar nicht im Spiel.");

        spieler.setStatus(SpielerStatus.SITZT_AUS);

        /*
         * Geld des Spielers wandert direkt in den Pot,
         * wenn er geht.
         */
        chipsImPot += spieler.getChipsGesetzt();

        alleSpieler.remove(spieler);

        this.anzahlAktiverSpieler--;

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onPlayerLeft(spieler);
    }

    /* Dealer- bzw. SpielContoller-Aktionen */

    @Override
    public void dealHoleCards() {

        for (Spieler spieler : alleSpieler)
            if (spieler != null)
                spieler.setHatKartenBekommen();

        this.phase = SpielPhase.PRE_FLOP;

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onHoleCardsDealt();
    }

    @Override
    public void dealFlop(Karte flop1, Karte flop2, Karte flop3) {

        if (phase != SpielPhase.PRE_FLOP)
            throw new IllegalStateException("Falsche Ausgangsphase: " + phase + " != PRE_FLOP");

        this.flop1 = flop1;
        this.flop2 = flop2;
        this.flop3 = flop3;

        this.phase = SpielPhase.FLOP;

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onFlopDealt(flop1, flop2, flop3);
    }

    @Override
    public void dealTurn(Karte turn) {

        if (phase != SpielPhase.FLOP)
            throw new IllegalStateException("Falsche Ausgangsphase: " + phase + " != FLOP");

        this.turn = turn;

        this.phase = SpielPhase.TURN;

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onTurnDealt(turn);
    }

    @Override
    public void dealRiver(Karte river) {

        if (phase != SpielPhase.TURN)
            throw new IllegalStateException("Falsche Ausgangsphase: " + phase + " != TURN");

        this.river = river;

        this.phase = SpielPhase.RIVER;

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onRiverDealt(river);
    }

    @Override
    public void endHand() {

        uebertrageChipsInDenPot();

        /*
         * TODO Auszahlung an den Gewinner
         */

        for (Spieler spieler : alleSpieler) {

            if (spieler == null)
                continue;

            spieler.kartenZurueckgeben();
            spieler.setStatus(SpielerStatus.WARTET);
        }

        this.flop1 = null;
        this.flop2 = null;
        this.flop3 = null;
        this.turn = null;
        this.river = null;

        this.phase = SpielPhase.WARTE_AUF_SPIELER;

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onHandEnded();
    }

    private void uebertrageChipsInDenPot() {

        for (Spieler spieler : alleSpieler) {

            if (spieler == null)
                continue;

            chipsImPot = (chipsImPot + spieler.getChipsGesetzt());
            spieler.setChipsGesetzt(0);
        }
    }

    @Override
    public void setzeButton(Spieler spieler) {

        this.dealer = spieler;

        /*
         * TODO Blinds zahlen lassen
         */

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onButtonGesetzt(spieler);
    }

    @Override
    public void setzeAktivenSpieler(Spieler spieler, int sekunden) {

        this.aktiverSpieler = spieler;

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onAktiverSpielerGesetzt(spieler, sekunden);
    }

    @Override
    public void setzeLetztenSpieler(Spieler spieler) {

        this.letzterSpieler = spieler;

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onLetztenSpielerGesetzt(spieler);
    }

    @Override
    public void changeBlindLevel(BlindHoehe blindHoehe) {

        this.blindHoehe = blindHoehe;

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onBlindLevelChanged(blindHoehe);
    }

    /* Spieler-Aktionen */

    @Override
    public void fold(Spieler spieler) {

        stelleSicherDassAktiverSpielerHandelt(spieler);

        spieler.setStatus(SpielerStatus.HAT_GEFOLDED);

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onPlayerFold(spieler);
    }

    @Override
    public void check(Spieler spieler) {

        stelleSicherDassAktiverSpielerHandelt(spieler);

        if (!spieler.getStatus().isKannInDerRundeHandeln())
            throw new IllegalArgumentException("Spieler " + spieler + " ist bereits ausgeschieden.");

        spieler.setStatus(SpielerStatus.HAT_GECHECKED);

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onPlayerCheck(spieler);
    }

    @Override
    public void bet(Spieler spieler, int chips) {

        stelleSicherDassAktiverSpielerHandelt(spieler);

        if (!spieler.getStatus().isKannInDerRundeHandeln())
            throw new IllegalArgumentException("Spieler " + spieler + " ist bereits ausgeschieden.");

        spieler.setzeChips(chips);

        spieler.setStatus(SpielerStatus.HAT_GEWETTET);

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onPlayerBet(spieler, chips);
    }

    @Override
    public void raise(Spieler spieler, int chips) {

        stelleSicherDassAktiverSpielerHandelt(spieler);

        if (!spieler.getStatus().isKannInDerRundeHandeln())
            throw new IllegalArgumentException("Spieler " + spieler + " ist bereits ausgeschieden.");

        spieler.setzeChips(chips);

        spieler.setStatus(SpielerStatus.HAT_GERAISED);

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onPlayerRaise(spieler, chips);
    }

    @Override
    public void showHoleCards(Spieler spieler, Karte holeCard1, Karte holeCard2) {

        spieler.setHoleCard1(holeCard1);
        spieler.setHoleCard2(holeCard2);

        /* Listener informieren. */
        for (SpielEventListener listener : listeners)
            listener.onHoleCardsShown(spieler, holeCard1, holeCard2);
    }

    /*
     * Util-Methoden
     */

    /**
     * Sicherstellung, dass auch wirklich der aktive Spieler gerade bettet,
     * raised oder folded und niemand außer der Reihe.
     */
    private void stelleSicherDassAktiverSpielerHandelt(Spieler spieler) {

        if (spieler != aktiverSpieler)
            throw new IllegalArgumentException("Spieler " + spieler + " ist nicht an der Reihe, sondern " + aktiverSpieler);
    }

    /**
     * Diese Methode wird benötigt um den nächsten Spieler zu identifzieren,
     * der jetzt handeln muss. Dabei müssen Spieler, die aussitzen oder bereits
     * All-In sind oder aus anderen Gründen nicht mehr handeln können,
     * übersprungen werden.
     */
    public Spieler findAktivenSpielerNach(Spieler spieler) {

        if (spieler == null)
            throw new IllegalArgumentException("Parameter 'spieler' darf nicht NULL sein.");

        /* Es gibt keinen weiteren Spieler, weil er selbst der letzte ist. */
        if (spieler.equals(letzterSpieler))
            return null;

        int platzNummerDesAktivenSpielers = alleSpieler.indexOf(spieler);

        for (int platzNummer = platzNummerDesAktivenSpielers + 1; platzNummer < alleSpieler.size() + 1; platzNummer++) {

            /* Wenn wir am Ende angekommen sind geht es wieder vorne los. */
            if (platzNummer == alleSpieler.size())
                platzNummer = 0;

            Spieler spielerAnPlatz = alleSpieler.get(platzNummer);

            if (spielerAnPlatz != null && spielerAnPlatz.getStatus().isKannInDerRundeHandeln())
                return spielerAnPlatz;
        }

        /* Wenn keiner mehr handlungsfähig ist, weil z.B. All-In endet die Runde */
        return null;
    }

    /**
     * Diese Methode identifiert den letzten handlungsfähigen Vorgänger zu einem
     * bestimmten Spieler. Dies wird dann benötigt, wenn jemand eine Bet oder
     * ein Raise gemacht hat geguckt werden muss, wer jetzt als letztes handeln
     * muss. Dies ist nicht immer der direkte Vorgänger: Falls dieser z.B.
     * All-In gegangen ist, ist es sein Vorgänger, weil er nach einem All-In
     * keinerlei Handlungsoptionen mehr hat.
     */
    public Spieler findAktivenSpielerVor(Spieler spieler) {

        if (spieler == null)
            throw new IllegalArgumentException("Parameter 'spieler' darf nicht NULL sein.");

        /* Es gibt keinen weiteren Spieler, weil er selbst der letzte ist. */
        if (spieler.equals(letzterSpieler))
            return null;

        int platzNummerDesAktivenSpielers = alleSpieler.indexOf(spieler);

        for (int platzNummer = platzNummerDesAktivenSpielers - 1; platzNummer < alleSpieler.size() + 1; platzNummer--) {

            /*
             * Wenn wir am Anfang angekommen sind, springen wir wieder auf das
             * Ende.
             */
            if (platzNummer == 0)
                platzNummer = alleSpieler.size();

            Spieler spielerAnPlatz = alleSpieler.get(platzNummer);

            if (spielerAnPlatz != null && spielerAnPlatz.getStatus().isKannInDerRundeHandeln())
                return spielerAnPlatz;
        }

        /* Wenn keiner mehr handlungsfähig ist, weil z.B. All-In endet die Runde */
        return null;
    }

    /*
     * **********************
     * Getter über Spielstatus
     * **********************
     */

    public String getId() {
        return id;
    }

    public SpielPhase getPhase() {
        return phase;
    }

    public BlindHoehe getBlindHoehe() {
        return blindHoehe;
    }

    public Spieler getDealer() {
        return dealer;
    }

    public Spieler getAktiverSpieler() {
        return aktiverSpieler;
    }

    public Spieler getLetzterSpieler() {
        return letzterSpieler;
    }

    public int getAnzahlAktiverSpieler() {
        return anzahlAktiverSpieler;
    }

    public List<Spieler> getAlleSpieler() {
        return Collections.unmodifiableList(alleSpieler);
    }

    public int getAnzahlSpielerPlaetze() {
        return alleSpieler.size();
    }

    public Spieler getSpielerAn(int platzNummer) {
        return alleSpieler.get(platzNummer);
    }

    public Karte getFlop1() {
        return flop1;
    }

    public Karte getFlop2() {
        return flop2;
    }

    public Karte getFlop3() {
        return flop3;
    }

    public Karte getTurn() {
        return turn;
    }

    public Karte getRiver() {
        return river;
    }

    public int getChipsImPot() {
        return chipsImPot;
    }

    public int getPotHoehe() {
        return potHoehe;
    }

    /*
     * toString()
     */

    @Override
    public String toString() {
        return "Spiel[" + id + "]";
    }
}
