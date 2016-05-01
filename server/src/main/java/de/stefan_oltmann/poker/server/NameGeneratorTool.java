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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Gibt einen zufälligen Namen zurück.
 * Die Liste der Namen wurde hier gefunden:
 * http://names.mongabay.com/male_names_alpha.htm
 */
public class NameGeneratorTool {

    private static final List<String> RANDOM_NAMES = new ArrayList<>();

    private static Random random = new Random();

    static {

        InputStream inputStream = NameGeneratorTool.class.getResourceAsStream("random_names.txt");

        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine())
            RANDOM_NAMES.add(scanner.nextLine());

        scanner.close();
    }

    public static String getRandomNickname() {

        int index = random.nextInt(RANDOM_NAMES.size());

        return RANDOM_NAMES.get(index);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++)
            System.out.println(getRandomNickname());
    }
}
