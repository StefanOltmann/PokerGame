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

import java.io.Serializable;

/**
 * Der Account ist quasi der Login eines Benutzers.
 * Dieser ist von der Entität "Spieler" zu trennen,
 * da der Benutzer gleichzeitig an beliebig vielen
 * Spielen teilnehmen kann ("Multi-Tabling").
 */
@SuppressWarnings("serial")
public class Account implements Serializable {

    /**
     * Die Account-ID eines Spielers ist eine quasi nicht
     * zu erratende GUID und gleichzeitig Login und Passwort
     * für den Benutzer.
     * 
     * Die Account-ID wird daher für andere Spieler geheimgehalten.
     * Diese bekommen lediglich die Spieler-ID zu sehen.
     * Der Server stellt sicher, dass nur der erzeugende Account
     * die Spieler-ID auch verwenden kann.
     */
    private String accountId;

    /**
     * Den anderen Spielern an einem Tisch wird lediglich der
     * Nickname des Benutzers mitgeteilt. Diesen kann der
     * Benutzer jederzeit ändern.
     */
    private String nickname;

    /**
     * Dies ist die Menge an Chips, mit denen sich ein Benutzer
     * in Spiele einkaufen kann.
     */
    private long chipsGesamt;

    /*
     * Konstruktor
     */

    public Account() {
    }

    public Account(String accountId) {
        this.accountId = accountId;
    }

    public Account(String accountId, String nickname, long chipsGesamt) {
        this.accountId = accountId;
        this.nickname = nickname;
        this.chipsGesamt = chipsGesamt;
    }

    /*
     * Getters + Setters
     */

    public String getId() {
        return accountId;
    }

    public void setId(String id) {
        this.accountId = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getChipsGesamt() {
        return chipsGesamt;
    }

    public void setChipsGesamt(long chipsGesamt) {
        this.chipsGesamt = chipsGesamt;
    }

    /*
     * toString()
     */

    @Override
    public String toString() {
        return "Account[" + accountId + ":" + nickname + "]";
    }
}
