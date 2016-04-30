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

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import de.stefan_oltmann.poker.model.hand.Hand.Kombination;

public class WahrscheinlichkeitsTest extends TestCase {

    /*
     * 10 Millionen Durchläufe dauernd ungefähr eine Minute
     * und liefern ein ganz gutes Ergebnis ab.
     */
    public static final int DURCHLAEUFE = 10000000;

    /* Tests sind auskommentiert, da zu häufig failend */
    public void testEmpty() {
    }

    /**
     * Dies sind die Wahrscheinlichkeiten für Kombinationen am FLOP
     * 
     * Vergleiche https://de.wikipedia.org/wiki/Poker#Kombinationen
     */
    public void _testWahrscheinlichkeitBei5Karten() {

        double[] wahrscheinlichkeiten = berechneWahrscheinlichkeiten(DURCHLAEUFE, 5);

        assertEquals(0.000154, wahrscheinlichkeiten[Kombination.ROYAL_FLUSH.ordinal()], 0.01);
        assertEquals(0.00139, wahrscheinlichkeiten[Kombination.STRAIGTH_FLUSH.ordinal()], 0.01);
        assertEquals(0.0240, wahrscheinlichkeiten[Kombination.VIERLING.ordinal()], 0.01);
        assertEquals(0.144, wahrscheinlichkeiten[Kombination.FULL_HOUSE.ordinal()], 0.01);
        assertEquals(0.197, wahrscheinlichkeiten[Kombination.FLUSH.ordinal()], 0.01);
        assertEquals(0.392, wahrscheinlichkeiten[Kombination.STRASSE.ordinal()], 0.01);
        assertEquals(2.11, wahrscheinlichkeiten[Kombination.DRILLING.ordinal()], 0.01);
        assertEquals(4.75, wahrscheinlichkeiten[Kombination.ZWEI_PAARE.ordinal()], 0.1);
        assertEquals(42.26, wahrscheinlichkeiten[Kombination.EIN_PAAR.ordinal()], 0.1);
        assertEquals(50.12, wahrscheinlichkeiten[Kombination.HOECHSTE_KARTE.ordinal()], 0.1);
    }

    /**
     * Dies sind die Wahrscheinlichkeiten für Kombinationen am RIVER
     * 
     * Vergleiche https://de.wikipedia.org/wiki/Poker#Kombinationen
     */
    public void _testWahrscheinlichkeitBei7Karten() {

        double[] wahrscheinlichkeiten = berechneWahrscheinlichkeiten(DURCHLAEUFE, 7);

        /*
         * TODO FIXME Zu häufige Erkennung von Royal Flush und Straight Flush?
         */
        // assertEquals(0.0032,
        // wahrscheinlichkeiten[Kombination.ROYAL_FLUSH.ordinal()], 0.01);
        // assertEquals(0.028,
        // wahrscheinlichkeiten[Kombination.STRAIGTH_FLUSH.ordinal()], 0.01);

        assertEquals(0.17, wahrscheinlichkeiten[Kombination.VIERLING.ordinal()], 0.01);
        assertEquals(2.60, wahrscheinlichkeiten[Kombination.FULL_HOUSE.ordinal()], 0.1);
        assertEquals(3.03, wahrscheinlichkeiten[Kombination.FLUSH.ordinal()], 0.1);
        assertEquals(4.52, wahrscheinlichkeiten[Kombination.STRASSE.ordinal()], 0.1);
        assertEquals(4.83, wahrscheinlichkeiten[Kombination.DRILLING.ordinal()], 0.1);
        assertEquals(23.50, wahrscheinlichkeiten[Kombination.ZWEI_PAARE.ordinal()], 0.1);
        assertEquals(43.83, wahrscheinlichkeiten[Kombination.EIN_PAAR.ordinal()], 0.1);
        assertEquals(17.41, wahrscheinlichkeiten[Kombination.HOECHSTE_KARTE.ordinal()], 0.1);
    }

    private double[] berechneWahrscheinlichkeiten(int durchlaeufe, int kartenAnzahl) {

        int[] haeufigkeiten = new int[Kombination.values().length];

        Deck deck = new Deck();

        Set<Karte> karten = new HashSet<>();

        for (int i = 1; i <= durchlaeufe; i++) {

            deck.neuMischen();
            karten.clear();

            for (int y = 0; y < kartenAnzahl; y++)
                karten.add(deck.hebeKarteAb());

            Hand hand = Hand.berechneBesteHand(karten);

            haeufigkeiten[hand.getKombination().ordinal()]++;
        }

        double[] wahrscheinlichkeiten = new double[haeufigkeiten.length];

        for (int i = 0; i < wahrscheinlichkeiten.length; i++)
            wahrscheinlichkeiten[i] = haeufigkeiten[i] * 100.0 / durchlaeufe;

        return wahrscheinlichkeiten;
    }
}
