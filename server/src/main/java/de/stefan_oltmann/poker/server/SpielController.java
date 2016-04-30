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

import de.stefan_oltmann.poker.model.BlindHoehe;
import de.stefan_oltmann.poker.model.SpielEventListener;
import de.stefan_oltmann.poker.model.SpielImpl;
import de.stefan_oltmann.poker.model.SpielPhase;
import de.stefan_oltmann.poker.model.Spieler;
import de.stefan_oltmann.poker.model.SpielerStatus;
import de.stefan_oltmann.poker.model.hand.Deck;
import de.stefan_oltmann.poker.model.hand.Karte;

/*
 * Der SpielController ist für die Durchführung des Spiels verantwortlich
 * so wie der Kasino-Mitarbeiter am Tisch. Er mischt und teilt die Karten aus, zieht
 * die Blinds ein, reicht den Button weiter und schmeißt inaktive Spieler raus.
 * 
 * Er wird bei Offline-Spielen direkt an die lokale Instanz gehangen oder sonst
 * nur serverseitig eingesetzt. Er ist ein Spiellistener und startet seinen
 * Thread, wenn mindestens zwei Spieler am Tisch sitzen - das bekommt er als
 * Listener ja mit.
 * 
 * Der Controller muss direkt mit dem echten Spiel reden, da dieses
 * über das Interface hinaus noch Util-Methoden verwendet.
 */
public class SpielController implements SpielEventListener {

    public static final long REAKTIONSZEIT_SPIELER_MS = 15 * 1000;

    /* Das Lock-Objekt für diesen Thread */
    private Object wartezustand = new Object();

    /** Das zu verwaltende Spiel */
    private SpielImpl spiel;

    private Deck deck = new Deck();

    /**
     * Das Spiel ist zu Anfang natürlich nicht gestartet.
     * Die Variable ist volatile, damit der entsprechende Thread
     * auch wieder stoppt, wenn alle Spieler das Spiel verlassen haben.
     */
    private volatile boolean spielLaeuft = false;

    /*
     * Konstruktor
     */

    public SpielController(SpielImpl spiel) {
        this.spiel = spiel;
        this.spiel.addListener(this);
    }

    /*
     * 
     */

    public boolean isSpielLaueft() {
        return spielLaeuft;
    }

    /*
     * Listener-Methoden
     */

    @Override
    public void onPlayerSatIn(Spieler spieler, int platzNummer, int chips) {

        /*
         * Wenn der Gameloop noch nicht im Gange ist,
         * diesen jetzt starten.
         */
        if (!spielLaeuft && spiel.getAnzahlAktiverSpieler() >= SpielImpl.MIN_SPIELER) {
            this.spielLaeuft = true;
            new Thread(new SpielRunnable()).start();
        }
    }

    @Override
    public void onPlayerSatOut(Spieler spieler) {

        /* TODO FIXME Gleiches Verhalten wie bei Verlassen des Spiels? */
        onPlayerLeft(spieler);
    }

    @Override
    public void onPlayerLeft(Spieler spieler) {

        /* Zu wenig Spieler: Nach dem nächsten Durchlauf anhalten. */
        if (spielLaeuft && spiel.getAnzahlAktiverSpieler() <= SpielImpl.MIN_SPIELER) {

            spielLaeuft = false;

            /*
             * TODO Wie wird das behandelt, wenn Leute zwischendurch
             * einfach aussteigen? Strategie für saubere Terminierung
             * implementieren.
             */

            spiel.endHand();
        }
    }

    @Override
    public void onHoleCardsDealt() {
    }

    @Override
    public void onFlopDealt(Karte flop1, Karte flop2, Karte flop3) {
    }

    @Override
    public void onTurnDealt(Karte turn) {
    }

    @Override
    public void onRiverDealt(Karte river) {
    }

    @Override
    public void onHandEnded() {
    }

    @Override
    public void onButtonGesetzt(Spieler spieler) {
    }

    @Override
    public void onAktiverSpielerGesetzt(Spieler spieler, int sekunden) {
    }

    @Override
    public void onLetztenSpielerGesetzt(Spieler spieler) {
    }

    @Override
    public void onBlindLevelChanged(BlindHoehe blindHoehe) {
    }

    @Override
    public void onPlayerFold(Spieler spieler) {
        wartezustand.notify();
    }

    @Override
    public void onPlayerCheck(Spieler spieler) {
        wartezustand.notify();
    }

    @Override
    public void onPlayerBet(Spieler spieler, int chips) {

        spiel.setzeLetztenSpieler(spiel.findAktivenSpielerVor(spieler));

        wartezustand.notify();
    }

    @Override
    public void onPlayerRaise(Spieler spieler, int chips) {

        spiel.setzeLetztenSpieler(spiel.findAktivenSpielerVor(spieler));

        wartezustand.notify();
    }

    @Override
    public void onHoleCardsShown(Spieler spieler, Karte holeCard1, Karte holeCard2) {
    }

    /*
     * Spielphasen
     */

    private void starteNeueRunde() {

        if (spiel.getPhase() != SpielPhase.WARTE_AUF_SPIELER)
            throw new IllegalStateException("Kann in dieser Phase keine neue Runde starten: " + spiel.getPhase());

        if (spiel.getAlleSpieler().size() < SpielImpl.MIN_SPIELER)
            throw new IllegalStateException("Zu wenig Spieler: " + spiel.getAlleSpieler().size());

        /* Zum Start der nächsten Runde das Deck neu mischen. */
        this.deck.neuMischen();

        /*
         * Da die nächste Runde jetzt beginnt alle Spieler, die im
         * Wartezustand sind, mit in diese Runde nehmen.
         */
        for (Spieler spieler : spiel.getAlleSpieler())
            if (spieler != null && spieler.getStatus() == SpielerStatus.WARTET_AUF_NAECHSTE_RUNDE)
                spieler.setStatus(SpielerStatus.WARTET);

        waehleEinenDealerAusWennNoetig();

        /*
         * TODO Sondersituation beim Heads Up: Hier
         * ist der Dealer immer der Small Blind.
         */

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        /* Small Blind setzen */
        Spieler smallBlind = spiel.findAktivenSpielerNach(spiel.getDealer());
        spiel.setzeAktivenSpieler(smallBlind, 1);
        // TODO eigene smallBlind Aktion?
        spiel.bet(smallBlind, spiel.getBlindHoehe().getSmallBlind());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        /* Big Blind setzen */
        Spieler bigBlind = spiel.findAktivenSpielerNach(smallBlind);
        spiel.setzeAktivenSpieler(bigBlind, 1);
        spiel.bet(bigBlind, spiel.getBlindHoehe().getBigBlind());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        /* Karten verteilen */
        spiel.dealHoleCards();

        /* Setze UTG */
        Spieler underTheGun = spiel.findAktivenSpielerNach(bigBlind);
        spiel.setzeAktivenSpieler(underTheGun, 15);

        /* Vor dem Flop ist der Big Blind der letzte, der handeln kann. */
        spiel.setzeLetztenSpieler(bigBlind);
    }

    private void waehleEinenDealerAusWennNoetig() {

        if (spiel.getDealer() == null) {

            for (Spieler spieler : spiel.getAlleSpieler()) {
                if (spieler != null && spieler.getStatus().isKannInDerRundeHandeln()) {
                    spiel.setzeButton(spieler);
                    break;
                }
            }
        }
    }

    /**
     * Der Flop sind die ersten drei Karten
     */
    private void dealFlop() {

        /* Burn Card */
        deck.hebeKarteAb();

        /* Der Flop */
        spiel.dealFlop(deck.hebeKarteAb(), deck.hebeKarteAb(), deck.hebeKarteAb());

        /* UTG muss jetzt handeln und der Dealer ist die letzte Person */
        Spieler underTheGun = spiel.findAktivenSpielerNach(spiel.getDealer());
        spiel.setzeAktivenSpieler(underTheGun, 15);
        spiel.setzeLetztenSpieler(spiel.getDealer());
    }

    /**
     * Der Turn ist die 4. Gemeinschaftskarte
     */
    private void dealTurn() {

        /* Burn Card */
        deck.hebeKarteAb();

        /* Der Turn */
        spiel.dealTurn(deck.hebeKarteAb());

        /* UTG muss jetzt handeln und der Dealer ist die letzte Person */
        Spieler underTheGun = spiel.findAktivenSpielerNach(spiel.getDealer());
        spiel.setzeAktivenSpieler(underTheGun, 15);
        spiel.setzeLetztenSpieler(spiel.getDealer());
    }

    /**
     * Der River ist die letzte Gemeinschaftskarte
     */
    private void dealRiver() {

        /* Burn Card */
        deck.hebeKarteAb();

        /* Der River */
        spiel.dealRiver(deck.hebeKarteAb());

        /* UTG muss jetzt handeln und der Dealer ist die letzte Person */
        Spieler underTheGun = spiel.findAktivenSpielerNach(spiel.getDealer());
        spiel.setzeAktivenSpieler(underTheGun, 15);
        spiel.setzeLetztenSpieler(spiel.getDealer());
    }

    /*
     * Der Thread zur Durchführung der Spiel-Logik
     */

    private class SpielRunnable implements Runnable {

        @Override
        public void run() {

            if (spiel.getPhase() != SpielPhase.WARTE_AUF_SPIELER)
                throw new IllegalStateException(
                        "Spiel kann nicht in dieser Phase angestartet werden: " + spiel.getPhase());

            System.out.println("Spiel begonnen: " + spiel.getAlleSpieler());

            synchronized (wartezustand) {

                while (spielLaeuft) {

                    if (spiel.getPhase() != SpielPhase.WARTE_AUF_SPIELER)
                        throw new IllegalStateException("Falsche Phase: " + spiel.getPhase());

                    starteNeueRunde();

                    fuehreWettrundeDurch(); // 1. Wettrunde

                    dealFlop(); // 1.-3. Gemeinschaftskarte

                    fuehreWettrundeDurch(); // 2. Wettrunde

                    dealTurn(); // 4. Gemeinschaftskarte

                    fuehreWettrundeDurch(); // 3. Wettrunde

                    dealRiver(); // 5. Gemeinschaftskarte

                    fuehreWettrundeDurch(); // 4. Wettrunde

                    spiel.endHand();

                    /*
                     * Spiel stoppen, falls die Anzahl aktiver Spieler
                     * unterschritten wird.
                     */
                    if (spiel.getAnzahlAktiverSpieler() < SpielImpl.MIN_SPIELER)
                        spielLaeuft = false;
                }
            }

            System.out.println("Spiel beendet: " + spiel.getAlleSpieler());
        }

        private void fuehreWettrundeDurch() {

            while (true) {

                Spieler spieler = spiel.getAktiverSpieler();

                spieler = spiel.findAktivenSpielerNach(spieler);

                /* Runde vorbei -> Nächste Phase */
                if (spieler == null)
                    break;

                long startTime = System.currentTimeMillis();

                try {
                    wartezustand.wait(REAKTIONSZEIT_SPIELER_MS);
                } catch (InterruptedException e) {
                }

                long endTime = System.currentTimeMillis();

                SpielerStatus status = spieler.getStatus();

                System.out.println("Reaktion von Spieler " + spieler + " in " + (endTime - startTime) + "ms : " + status);

                /* Falls er nicht reagiert hat, einen Standard setzen. */
                switch (status) {
                case MUSS_CHECKEN_ODER_BETTEN:
                    spiel.check(spieler);
                    break;
                case MUSS_CALLEN_ODER_RAISEN:
                    spiel.fold(spieler);
                    break;
                default:
                    break;
                }
            }
        }
    }
}
