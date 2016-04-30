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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Karte {

    KREUZ_ASS(Farbe.KREUZ, Wert.ASS),
    KREUZ_KOENIG(Farbe.KREUZ, Wert.KOENIG),
    KREUZ_DAME(Farbe.KREUZ, Wert.DAME),
    KREUZ_BUBE(Farbe.KREUZ, Wert.BUBE),
    KREUZ_ZEHN(Farbe.KREUZ, Wert.ZEHN),
    KREUZ_NEUN(Farbe.KREUZ, Wert.NEUN),
    KREUZ_ACHT(Farbe.KREUZ, Wert.ACHT),
    KREUZ_SIEBEN(Farbe.KREUZ, Wert.SIEBEN),
    KREUZ_SECHS(Farbe.KREUZ, Wert.SECHS),
    KREUZ_FUENF(Farbe.KREUZ, Wert.FUENF),
    KREUZ_VIER(Farbe.KREUZ, Wert.VIER),
    KREUZ_DREI(Farbe.KREUZ, Wert.DREI),
    KREUZ_ZWEI(Farbe.KREUZ, Wert.ZWEI),

    PIK_ASS(Farbe.PIK, Wert.ASS),
    PIK_KOENIG(Farbe.PIK, Wert.KOENIG),
    PIK_DAME(Farbe.PIK, Wert.DAME),
    PIK_BUBE(Farbe.PIK, Wert.BUBE),
    PIK_ZEHN(Farbe.PIK, Wert.ZEHN),
    PIK_NEUN(Farbe.PIK, Wert.NEUN),
    PIK_ACHT(Farbe.PIK, Wert.ACHT),
    PIK_SIEBEN(Farbe.PIK, Wert.SIEBEN),
    PIK_SECHS(Farbe.PIK, Wert.SECHS),
    PIK_FUENF(Farbe.PIK, Wert.FUENF),
    PIK_VIER(Farbe.PIK, Wert.VIER),
    PIK_DREI(Farbe.PIK, Wert.DREI),
    PIK_ZWEI(Farbe.PIK, Wert.ZWEI),

    HERZ_ASS(Farbe.HERZ, Wert.ASS),
    HERZ_KOENIG(Farbe.HERZ, Wert.KOENIG),
    HERZ_DAME(Farbe.HERZ, Wert.DAME),
    HERZ_BUBE(Farbe.HERZ, Wert.BUBE),
    HERZ_ZEHN(Farbe.HERZ, Wert.ZEHN),
    HERZ_NEUN(Farbe.HERZ, Wert.NEUN),
    HERZ_ACHT(Farbe.HERZ, Wert.ACHT),
    HERZ_SIEBEN(Farbe.HERZ, Wert.SIEBEN),
    HERZ_SECHS(Farbe.HERZ, Wert.SECHS),
    HERZ_FUENF(Farbe.HERZ, Wert.FUENF),
    HERZ_VIER(Farbe.HERZ, Wert.VIER),
    HERZ_DREI(Farbe.HERZ, Wert.DREI),
    HERZ_ZWEI(Farbe.HERZ, Wert.ZWEI),

    KARO_ASS(Farbe.KARO, Wert.ASS),
    KARO_KOENIG(Farbe.KARO, Wert.KOENIG),
    KARO_DAME(Farbe.KARO, Wert.DAME),
    KARO_BUBE(Farbe.KARO, Wert.BUBE),
    KARO_ZEHN(Farbe.KARO, Wert.ZEHN),
    KARO_NEUN(Farbe.KARO, Wert.NEUN),
    KARO_ACHT(Farbe.KARO, Wert.ACHT),
    KARO_SIEBEN(Farbe.KARO, Wert.SIEBEN),
    KARO_SECHS(Farbe.KARO, Wert.SECHS),
    KARO_FUENF(Farbe.KARO, Wert.FUENF),
    KARO_VIER(Farbe.KARO, Wert.VIER),
    KARO_DREI(Farbe.KARO, Wert.DREI),
    KARO_ZWEI(Farbe.KARO, Wert.ZWEI);

    public final Farbe farbe;
    public final Wert wert;

    public static final Comparator<Karte> WERT_COMPARATOR;

    static {

        WERT_COMPARATOR = new Comparator<Karte>() {
            @Override
            public int compare(Karte o1, Karte o2) {

                int result = o1.wert.compareTo(o2.wert);

                if (result == 0)
                    result = o1.farbe.compareTo(o2.farbe);

                return result;
            }
        };
    }

    private Karte(Farbe farbe, Wert wert) {
        this.farbe = farbe;
        this.wert = wert;
    }

    public static List<Karte> getSortiertNachWert(Collection<Karte> karten) {

        List<Karte> sortierteKarten = new ArrayList<Karte>(karten);

        Collections.sort(sortierteKarten, WERT_COMPARATOR);

        return sortierteKarten;
    }

    public static Map<Farbe, List<Karte>> erstelleKartenProFarbeMap(
            List<Karte> sortierteKarten) {

        Map<Farbe, List<Karte>> kartenProFarbeMap = new HashMap<>();

        for (Karte karte : sortierteKarten) {

            List<Karte> kartenDieserFarbe = kartenProFarbeMap.get(karte.farbe);

            if (kartenDieserFarbe == null)
                kartenProFarbeMap.put(karte.farbe,
                        kartenDieserFarbe = new ArrayList<Karte>());

            kartenDieserFarbe.add(karte);
        }

        return kartenProFarbeMap;
    }

    public static Map<Wert, List<Karte>> erstelleKartenProWertMap(
            List<Karte> sortierteKarten) {

        Map<Wert, List<Karte>> kartenProWertMap = new HashMap<>();

        for (Karte karte : sortierteKarten) {

            List<Karte> kartenDiesesWerts = kartenProWertMap.get(karte.wert);

            if (kartenDiesesWerts == null)
                kartenProWertMap.put(karte.wert,
                        kartenDiesesWerts = new ArrayList<Karte>());

            kartenDiesesWerts.add(karte);
        }

        return kartenProWertMap;
    }

    public static List<Karte> findeKartenMitWert(Collection<Karte> karten, Wert wert) {

        List<Karte> kartenMitWert = new ArrayList<Karte>();

        for (Karte karte : karten)
            if (wert == karte.wert)
                kartenMitWert.add(karte);

        return kartenMitWert;
    }

    public static enum Farbe {

        KREUZ,
        PIK,
        HERZ,
        KARO;
    }

    public static enum Wert {

        /* Ordinal = absteigender Wert */
        ASS,
        KOENIG,
        DAME,
        BUBE,
        ZEHN,
        NEUN,
        ACHT,
        SIEBEN,
        SECHS,
        FUENF,
        VIER,
        DREI,
        ZWEI;
    }

    @Override
    public String toString() {
        return farbe + " " + wert;
    }
}
