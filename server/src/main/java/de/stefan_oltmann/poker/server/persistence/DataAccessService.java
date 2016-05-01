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
package de.stefan_oltmann.poker.server.persistence;

import java.util.Map;

import de.stefan_oltmann.poker.model.Account;

/**
 * Über den DataAccessService wird alles gespeichert, was persistent sein muss.
 * Dazu gehört natürlich die Information über die Accounts und deren hart
 * erspielten Chips. Laufende Spiele hingegen müssen nicht persistiert werden,
 * weil es währenddessen keine Server-Neustarts geben wird. Kein Spiel wird
 * während einer Wartungsphase pausiert und dann wieder aufgenommen. Viel
 * eher wird der Server in einen Shutdown-Modus gestellt und keine neuen
 * Spiele starten.
 */
public interface DataAccessService {

    /*
     * Account-Verwaltung
     */

    Account erstelleNeuenAccount();

    Map<String, Account> findAllAccounts();

    Account findAccountById(String accountId);

    void setzeNickname(String accountId, String nickname);

    void aendereChips(String accountId, int changeAmount);

}
