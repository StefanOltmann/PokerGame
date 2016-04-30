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
package de.stefan_oltmann.poker.model.hand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.stefan_oltmann.poker.model.hand.Karte.Farbe;
import de.stefan_oltmann.poker.model.hand.Karte.Wert;

public class Hand {

    private Kombination kombination;
    private List<Karte> karten;

    private Hand(Kombination kombination, List<Karte> karten) {

        if (karten.size() > 5)
            throw new IllegalArgumentException("Eine darf maximal aus 5 Karten bestehen.");

        this.kombination = kombination;
        this.karten = karten;
    }

    public Kombination getKombination() {
        return kombination;
    }

    public List<Karte> getKarten() {
        return Collections.unmodifiableList(karten);
    }

    /*
     * Es soll eine Straße mit der höchsten Karte gefunden werden, die
     * idealerweise auch nur aus einer Farbe besteht (= Royal Flush).
     */
    static List<Karte> findeStrasse(
            List<Karte> sortierteKarten) {

        if (sortierteKarten.size() < 2 || sortierteKarten.size() > 7)
            throw new IllegalArgumentException(
                    "Die Anzahl der zu prüfenden Karten muss zwischen 2 und 7 liegen.");

        /* Weniger als 5 Karten können nie eine Straße sein. */
        if (sortierteKarten.size() < 5)
            return null;

        List<Karte> zusammenhaengendeKarten = null;

        for (Wert wert : Wert.values()) {

            List<Karte> kartenMitWert = Karte.findeKartenMitWert(sortierteKarten, wert);

            for (Karte karteMitWert : kartenMitWert) {

                /*
                 * Wurde ein hoher Wert gefunden? Dann mal gucken, ob von diesem
                 * ausgehend eine Straße gebaut werden kann.
                 */

                zusammenhaengendeKarten =
                        findeNachfolgendeKartenFuerStrasse(sortierteKarten, karteMitWert);

                /*
                 * TODO Hier ist sicherlich noch Optimierung für den Edge Case
                 * drin, dass eine zweite Farbe derselben Karte zu einer Royal
                 * Flush Straße führt.
                 */
                if (zusammenhaengendeKarten != null)
                    return zusammenhaengendeKarten;
            }
        }

        return null;
    }

    private static List<Karte> findeNachfolgendeKartenFuerStrasse(
            List<Karte> sortierteKarten, Karte karte) {

        /* Weniger als 5 Karten können nie eine Straße sein. */
        if (sortierteKarten.size() < 5)
            return null;

        List<Karte> zusammenhaengendeKarten = new ArrayList<Karte>();

        boolean skip = true;

        List<Wert> werte = new ArrayList<Karte.Wert>(Arrays.asList(Wert.values()));

        /*
         * Sonderfall bei Straßen: Ass kann auch die 1 sein!
         */
        werte.add(Wert.ASS);

        for (Wert wert : werte) {

            if (karte.wert == wert)
                skip = false;

            if (skip)
                continue;

            List<Karte> nachfolgeKarten = Karte.findeKartenMitWert(sortierteKarten, wert);

            if (nachfolgeKarten.size() == 1) {

                zusammenhaengendeKarten.addAll(nachfolgeKarten);

            } else if (nachfolgeKarten.size() > 1) {

                Karte gewaehlteNachfolgeKarte = nachfolgeKarten.get(0);

                /*
                 * Kann die Nachfolge-Karte durch eine Karte in passender Farbe
                 * getauscht werden? Wenn ja, dies tun.
                 */
                for (Karte nachfolgeKarte : nachfolgeKarten)
                    if (nachfolgeKarte.farbe == karte.farbe)
                        gewaehlteNachfolgeKarte = nachfolgeKarte;

                zusammenhaengendeKarten.add(gewaehlteNachfolgeKarte);

            } else
                break;
        }

        int anzahl = zusammenhaengendeKarten.size();

        if (anzahl >= 5)
            return anzahl > 5 ? zusammenhaengendeKarten.subList(0, 5) : zusammenhaengendeKarten;
        else
            return null;
    }

    static List<Karte> findeFlush(
            Map<Farbe, List<Karte>> kartenProFarbeMap) {

        for (Map.Entry<Farbe, List<Karte>> entry : kartenProFarbeMap.entrySet()) {

            List<Karte> kartenEinerFarbe = entry.getValue();

            int anzahl = kartenEinerFarbe.size();
            if (anzahl >= 5)
                return anzahl > 5 ? kartenEinerFarbe.subList(0, 5) : kartenEinerFarbe;
        }

        return null;
    }

    public static Hand berechneBesteHand(Collection<Karte> karten) {

        if (karten.size() < 2 || karten.size() > 7)
            throw new IllegalArgumentException(
                    "Die Anzahl der zu prüfenden Karten muss zwischen 2 und 7 liegen.");

        List<Karte> sortierteKarten = Karte.getSortiertNachWert(karten);

        Map<Farbe, List<Karte>> kartenProFarbeMap =
                Karte.erstelleKartenProFarbeMap(sortierteKarten);

        List<Karte> kartenFuerStrasse = findeStrasse(sortierteKarten);

        List<Karte> kartenFuerFlush = findeFlush(kartenProFarbeMap);

        /*
         * 10) Royal Flush
         * 09) Straight Flush
         */

        if (kartenFuerFlush != null && kartenFuerStrasse != null) {

            /* Die Karten der Strasse sind wichtiger */
            boolean royal = kartenFuerStrasse.get(0).wert == Wert.ASS;

            if (royal)
                return new Hand(Kombination.ROYAL_FLUSH, kartenFuerStrasse);
            else
                return new Hand(Kombination.STRAIGTH_FLUSH, kartenFuerStrasse);
        }

        /*
         * 08) Vierling
         */

        Map<Wert, List<Karte>> kartenProWertMap =
                Karte.erstelleKartenProWertMap(sortierteKarten);

        for (Map.Entry<Wert, List<Karte>> entry : kartenProWertMap.entrySet()) {

            List<Karte> kartenDesWerts = entry.getValue();

            if (kartenDesWerts.size() == 4) {

                List<Karte> relevanteKarten = new ArrayList<Karte>();
                relevanteKarten.addAll(kartenDesWerts);

                List<Karte> uebrigeKarten = new ArrayList<Karte>(sortierteKarten);
                uebrigeKarten.removeAll(kartenDesWerts);

                if (!uebrigeKarten.isEmpty())
                    relevanteKarten.add(uebrigeKarten.get(0));

                return new Hand(Kombination.VIERLING, kartenDesWerts);
            }
        }

        /*
         * 07) Full House
         */

        List<Karte> kartenFuerDrilling = null;
        List<Karte> kartenFuerHohesPaar = null;
        List<Karte> kartenFuerNiedrigesPaar = null;

        for (Map.Entry<Wert, List<Karte>> entry : kartenProWertMap.entrySet()) {

            List<Karte> kartenDesWerts = entry.getValue();

            /* Drei Karten? Klar ein Drilling. */
            if (kartenDesWerts.size() == 3)
                kartenFuerDrilling = kartenDesWerts;

            /* Zwei Karten? Ein Zwilling. */
            if (kartenDesWerts.size() == 2) {

                /*
                 * Erst einmal nehmen wir an, dass dies das höhere Paar ist,
                 * wenn wir sonst noch nichts gefunden haben. Ansonsten müssen
                 * wir prüfen, wie es mit dem zweiten Paar aussieht.
                 */
                if (kartenFuerHohesPaar == null) {

                    kartenFuerHohesPaar = kartenDesWerts;

                } else {

                    boolean neuGefundenesPaarHatHoeherenWert =
                            kartenFuerHohesPaar.get(0).wert.ordinal() > kartenDesWerts.get(0).wert.ordinal();

                    if (neuGefundenesPaarHatHoeherenWert) {
                        kartenFuerNiedrigesPaar = kartenFuerHohesPaar;
                        kartenFuerHohesPaar = kartenDesWerts;
                    } else
                        kartenFuerNiedrigesPaar = kartenDesWerts;
                }
            }
        }

        if (kartenFuerDrilling != null && kartenFuerHohesPaar != null) {

            List<Karte> relevanteKarten = new ArrayList<Karte>();
            relevanteKarten.addAll(kartenFuerDrilling);
            relevanteKarten.addAll(kartenFuerHohesPaar);

            return new Hand(Kombination.FULL_HOUSE, relevanteKarten);
        }

        /*
         * 06) Flush
         */

        if (kartenFuerFlush != null)
            return new Hand(Kombination.FLUSH, kartenFuerFlush);

        /*
         * 05) Strasse
         */

        if (kartenFuerStrasse != null)
            return new Hand(Kombination.STRASSE, kartenFuerStrasse);

        /*
         * 04) Drilling
         */

        if (kartenFuerDrilling != null) {

            List<Karte> relevanteKarten = new ArrayList<Karte>();
            relevanteKarten.addAll(kartenFuerDrilling);

            List<Karte> uebrigeKarten = new ArrayList<Karte>(sortierteKarten);
            uebrigeKarten.removeAll(relevanteKarten);

            if (uebrigeKarten.size() > 1)
                relevanteKarten.addAll(uebrigeKarten.subList(0, 2));
            else if (!uebrigeKarten.isEmpty())
                relevanteKarten.add(uebrigeKarten.get(0));

            return new Hand(Kombination.DRILLING, relevanteKarten);
        }

        /*
         * 03) Zwei Paare
         */

        if (kartenFuerHohesPaar != null && kartenFuerNiedrigesPaar != null) {

            List<Karte> relevanteKarten = new ArrayList<Karte>();
            relevanteKarten.addAll(kartenFuerHohesPaar);
            relevanteKarten.addAll(kartenFuerNiedrigesPaar);

            List<Karte> uebrigeKarten = new ArrayList<Karte>(sortierteKarten);
            uebrigeKarten.removeAll(relevanteKarten);

            relevanteKarten.add(uebrigeKarten.get(0));

            return new Hand(Kombination.ZWEI_PAARE, relevanteKarten);
        }

        /*
         * 02) Ein Paar
         */

        if (kartenFuerHohesPaar != null) {

            List<Karte> relevanteKarten = new ArrayList<Karte>();
            relevanteKarten.addAll(kartenFuerHohesPaar);

            List<Karte> uebrigeKarten = new ArrayList<Karte>(sortierteKarten);
            uebrigeKarten.removeAll(kartenFuerHohesPaar);

            relevanteKarten.addAll(uebrigeKarten.subList(0, 3));

            return new Hand(Kombination.EIN_PAAR, relevanteKarten);
        }

        /*
         * 01) Höchste Karte
         */

        List<Karte> uebrigeKarten = new ArrayList<Karte>(sortierteKarten);

        return new Hand(Kombination.HOECHSTE_KARTE,
                uebrigeKarten.subList(0, Math.min(uebrigeKarten.size(), 5)));
    }

    @Override
    public String toString() {
        return "Hand " + kombination + " (" + karten + ")";
    }

    public static enum Kombination {

        ROYAL_FLUSH("Royal Flush", 10),
        STRAIGTH_FLUSH("Straigth Flush", 9),
        VIERLING("Vierling", 8),
        FULL_HOUSE("Full House", 7),
        FLUSH("Flush", 6),
        STRASSE("Straße", 5),
        DRILLING("Drilling", 4),
        ZWEI_PAARE("Zwei Paare", 3),
        EIN_PAAR("Ein Paar", 2),
        HOECHSTE_KARTE("Höchste Karte", 1);

        private String bezeichnung;
        private int wert;

        private Kombination(String bezeichnung, int wert) {
            this.bezeichnung = bezeichnung;
            this.wert = wert;
        }

        public String getBezeichnung() {
            return bezeichnung;
        }

        public int getWert() {
            return wert;
        }

        @Override
        public String toString() {
            return bezeichnung;
        }
    }
}
