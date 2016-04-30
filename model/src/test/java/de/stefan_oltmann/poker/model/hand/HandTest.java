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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import de.stefan_oltmann.poker.model.hand.Hand.Kombination;
import de.stefan_oltmann.poker.model.hand.Karte.Farbe;

public class HandTest extends TestCase {

    /*
     * Hilfs-Methode "findeStrasse"
     */

    public void testFindeStrasse_keine() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_ZWEI,
                Karte.HERZ_DREI,
                Karte.HERZ_VIER,
                Karte.HERZ_ACHT,
                Karte.HERZ_NEUN);

        Collections.shuffle(karten);

        List<Karte> kartenDerStrasse = Hand.findeStrasse(karten);

        assertEquals(null, kartenDerStrasse);
    }

    public void testFindeStrasse_einfacherFall() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_NEUN,
                // --
                Karte.HERZ_SIEBEN,
                Karte.HERZ_SECHS,
                Karte.HERZ_FUENF,
                Karte.HERZ_VIER,
                Karte.HERZ_DREI,
                // --
                Karte.HERZ_ZWEI);

        Collections.shuffle(karten);

        List<Karte> kartenDerStrasse = Hand.findeStrasse(karten);

        assertEquals(Arrays.asList(
                Karte.HERZ_SIEBEN,
                Karte.HERZ_SECHS,
                Karte.HERZ_FUENF,
                Karte.HERZ_VIER,
                Karte.HERZ_DREI),
                kartenDerStrasse);
    }

    public void testFindeStrasse_mitAssAls1() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_KOENIG,
                Karte.KARO_ACHT,
                // --
                Karte.HERZ_FUENF,
                Karte.HERZ_VIER,
                Karte.HERZ_DREI,
                Karte.HERZ_ZWEI,
                Karte.HERZ_ASS);

        Collections.shuffle(karten);

        List<Karte> kartenDerStrasse = Hand.findeStrasse(karten);

        assertEquals(Arrays.asList(
                Karte.HERZ_FUENF,
                Karte.HERZ_VIER,
                Karte.HERZ_DREI,
                Karte.HERZ_ZWEI,
                Karte.HERZ_ASS),
                kartenDerStrasse);
    }

    /*
     * In diesem Fall soll die PIK-5 gegen die HERZ-5 getauscht werden, um den
     * Royal Flush zu ermöglichen.
     */
    public void testFindeStrasse_farbOptimierung() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_ZEHN,
                // --
                Karte.HERZ_SIEBEN,
                Karte.HERZ_SECHS,
                Karte.PIK_FUENF,
                Karte.HERZ_FUENF,
                Karte.HERZ_VIER,
                Karte.HERZ_DREI);

        Collections.shuffle(karten);

        List<Karte> kartenDerStrasse = Hand.findeStrasse(karten);

        assertEquals(Arrays.asList(
                Karte.HERZ_SIEBEN,
                Karte.HERZ_SECHS,
                Karte.HERZ_FUENF,
                Karte.HERZ_VIER,
                Karte.HERZ_DREI),
                kartenDerStrasse);
    }

    /*
     * Hilfs-Methode "findeFlush"
     */

    public void testFindeFlush_kein() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_ASS,
                Karte.HERZ_ZEHN,
                Karte.PIK_FUENF,
                Karte.HERZ_VIER,
                Karte.PIK_DREI,
                Karte.HERZ_ZWEI);

        Collections.shuffle(karten);

        Map<Farbe, List<Karte>> kartenProFarbeMap =
                Karte.erstelleKartenProFarbeMap(karten);

        List<Karte> kartenDesFlush = Hand.findeFlush(kartenProFarbeMap);

        assertEquals(null, kartenDesFlush);
    }

    public void testFindeFlush() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_ASS,
                Karte.HERZ_ZEHN,
                Karte.HERZ_FUENF,
                Karte.HERZ_VIER,
                Karte.HERZ_DREI);

        Map<Farbe, List<Karte>> kartenProFarbeMap =
                Karte.erstelleKartenProFarbeMap(karten);

        List<Karte> kartenDesFlush = Hand.findeFlush(kartenProFarbeMap);

        assertEquals(Arrays.asList(
                Karte.HERZ_ASS,
                Karte.HERZ_ZEHN,
                Karte.HERZ_FUENF,
                Karte.HERZ_VIER,
                Karte.HERZ_DREI),
                kartenDesFlush);
    }

    /*
     * -----------------------------------------------------------
     */

    /*
     * 01) Höchste Karte
     */

    public void testBerechneHoechsteHand_HoechsteKarte() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_ASS,
                Karte.KARO_SIEBEN,
                Karte.HERZ_SECHS,
                Karte.KARO_BUBE,
                Karte.PIK_ACHT,
                Karte.KARO_DAME);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.HOECHSTE_KARTE, hand.getKombination());
        assertEquals(Arrays.asList(
                Karte.HERZ_ASS,
                Karte.KARO_DAME,
                Karte.KARO_BUBE,
                Karte.PIK_ACHT,
                Karte.KARO_SIEBEN), hand.getKarten());
    }

    public void testBerechneHoechsteHand_HoechsteKarte_StartHand() {

        List<Karte> karten = Arrays.asList(
                Karte.PIK_ACHT,
                Karte.HERZ_BUBE);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.HOECHSTE_KARTE, hand.getKombination());
        assertEquals(Arrays.asList(
                Karte.HERZ_BUBE,
                Karte.PIK_ACHT), hand.getKarten());
    }

    /*
     * 02) Ein Paare
     */

    public void testBerechneHoechsteHand_EinPaar() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_ZEHN,
                Karte.KARO_SIEBEN,
                Karte.PIK_SIEBEN,
                Karte.HERZ_SECHS,
                Karte.KARO_BUBE,
                Karte.PIK_ACHT,
                Karte.KARO_DAME);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.EIN_PAAR, hand.getKombination());
        assertEquals(Arrays.asList(
                Karte.PIK_SIEBEN,
                Karte.KARO_SIEBEN,
                Karte.KARO_DAME,
                Karte.KARO_BUBE,
                Karte.HERZ_ZEHN), hand.getKarten());
    }

    /*
     * 03) Zwei Paare
     */

    public void testBerechneHoechsteHand_ZweiPaare() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_ZEHN,
                Karte.KARO_SIEBEN,
                Karte.PIK_SIEBEN,
                Karte.HERZ_SECHS,
                Karte.KARO_SECHS,
                Karte.PIK_ACHT,
                Karte.KARO_DAME);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.ZWEI_PAARE, hand.getKombination());
        assertEquals(Arrays.asList(
                Karte.PIK_SIEBEN,
                Karte.KARO_SIEBEN,
                Karte.HERZ_SECHS,
                Karte.KARO_SECHS,
                Karte.KARO_DAME), hand.getKarten());
    }

    /*
     * 04) Drilling
     */

    public void testBerechneHoechsteHand_Drilling() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_ZEHN,
                Karte.KARO_SIEBEN,
                Karte.PIK_SIEBEN,
                Karte.HERZ_SIEBEN,
                Karte.HERZ_SECHS,
                Karte.PIK_ACHT,
                Karte.KARO_DAME);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.DRILLING, hand.getKombination());
        assertEquals(Arrays.asList(
                Karte.PIK_SIEBEN,
                Karte.HERZ_SIEBEN,
                Karte.KARO_SIEBEN,
                Karte.KARO_DAME,
                Karte.HERZ_ZEHN), hand.getKarten());
    }

    public void testBerechneHoechsteHand_Drilling_wenigerKarten() {

        List<Karte> karten = Arrays.asList(
                Karte.KARO_SIEBEN,
                Karte.PIK_SIEBEN,
                Karte.HERZ_SIEBEN,
                Karte.KARO_DAME);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.DRILLING, hand.getKombination());
        assertEquals(Arrays.asList(
                Karte.PIK_SIEBEN,
                Karte.HERZ_SIEBEN,
                Karte.KARO_SIEBEN,
                Karte.KARO_DAME), hand.getKarten());
    }

    /*
     * 05) Strasse / Straight
     */

    public void testBerechneHoechsteHand_Strasse() {

        List<Karte> karten = Arrays.asList(
                Karte.PIK_ZEHN,
                Karte.HERZ_NEUN,
                Karte.KARO_ACHT,
                Karte.HERZ_SIEBEN,
                Karte.HERZ_SECHS,
                Karte.PIK_ACHT,
                Karte.KARO_DAME);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.STRASSE, hand.getKombination());
        assertEquals(Arrays.asList(
                Karte.PIK_ZEHN,
                Karte.HERZ_NEUN,
                Karte.PIK_ACHT,
                Karte.HERZ_SIEBEN,
                Karte.HERZ_SECHS), hand.getKarten());
    }

    /*
     * 06) Flush
     */

    public void testBerechneHoechsteHand_Flush() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_ASS,
                Karte.HERZ_ZEHN,
                Karte.HERZ_FUENF,
                Karte.KARO_DREI,
                Karte.KARO_VIER,
                Karte.HERZ_VIER,
                Karte.HERZ_DREI);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.FLUSH, hand.getKombination());
    }

    /*
     * 07) Full House
     */

    public void testBerechneHoechsteHand_FullHouse() {

        List<Karte> karten = Arrays.asList(
                Karte.PIK_ASS,
                Karte.HERZ_ASS,
                Karte.KARO_ASS,
                Karte.HERZ_ACHT,
                Karte.KARO_ACHT,
                // Niedriges Paar wird ignoriert:
                Karte.HERZ_SECHS,
                Karte.KARO_SECHS);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.FULL_HOUSE, hand.getKombination());
        assertEquals(Arrays.asList(
                Karte.PIK_ASS,
                Karte.HERZ_ASS,
                Karte.KARO_ASS,
                Karte.HERZ_ACHT,
                Karte.KARO_ACHT), hand.getKarten());
    }

    /*
     * 08) Vierling
     */

    public void testBerechneHoechsteHand_Vierling_ohneKicker() {

        List<Karte> karten = Arrays.asList(
                Karte.KREUZ_ASS,
                Karte.PIK_ASS,
                Karte.HERZ_ASS,
                Karte.KARO_ASS);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.VIERLING, hand.getKombination());
    }

    public void testBerechneHoechsteHand_Vierling_mitKicker() {

        List<Karte> karten = Arrays.asList(
                Karte.KREUZ_ASS,
                Karte.PIK_ASS,
                Karte.HERZ_ASS,
                Karte.KARO_ASS,
                Karte.PIK_SIEBEN,
                Karte.KARO_DREI);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.VIERLING, hand.getKombination());
    }

    /*
     * 09) Straigth Flush
     */

    public void testBerechneHoechsteHand_StraigthFlush() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_NEUN,
                Karte.HERZ_ACHT,
                Karte.HERZ_SIEBEN,
                Karte.HERZ_SECHS,
                Karte.HERZ_FUENF);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.STRAIGTH_FLUSH, hand.getKombination());
    }

    /*
     * 10) Royal Flush
     */

    public void testBerechneHoechsteHand_RoyalFlush() {

        List<Karte> karten = Arrays.asList(
                Karte.HERZ_ASS,
                Karte.HERZ_KOENIG,
                Karte.HERZ_DAME,
                Karte.HERZ_BUBE,
                Karte.HERZ_ZEHN);

        Collections.shuffle(karten);

        Hand hand = Hand.berechneBesteHand(karten);

        assertNotNull(hand);
        assertEquals(Kombination.ROYAL_FLUSH, hand.getKombination());
    }
}
