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
import java.util.Stack;

/**
 * Ein Kartendeck (kurz "Deck") ist eine Liste von Karten,
 * die zufällig gemischt sind.
 */
public class Deck {

    private Stack<Karte> karten;

    public Deck() {
        karten = new Stack<Karte>();
        neuMischen();
    }

    public Stack<Karte> getKarten() {
        return karten;
    }

    public void setKarten(Stack<Karte> karten) {
        this.karten = karten;
    }

    public void neuMischen() {

        karten.clear();
        karten.addAll(Arrays.asList(Karte.values()));
        Collections.shuffle(karten);
    }

    public Karte hebeKarteAb() {
        return karten.pop();
    }
}
