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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import de.stefan_oltmann.poker.model.Account;
import de.stefan_oltmann.poker.server.IdGeneratorTool;

public class DataAccessServiceMapDbImpl implements DataAccessService {

    private static DataAccessServiceMapDbImpl instance;

    public static final File MAPDB_FILE = new File("data");

    private Map<String, Account> alleAccountsMap;

    private DB db;

    private DataAccessServiceMapDbImpl() {

        db = DBMaker.newFileDB(MAPDB_FILE).make();

        alleAccountsMap = db.getTreeMap("accounts");

        db.commit();
    }

    @Override
    protected void finalize() throws Throwable {

        this.db.close();

        super.finalize();
    }

    public static DataAccessServiceMapDbImpl getInstance() {

        if (instance == null)
            instance = new DataAccessServiceMapDbImpl();

        return instance;
    }

    /*
     * Account-Verwaltung
     */

    @Override
    public Account erstelleNeuenAccount() {

        Account account = new Account();

        account.setId(IdGeneratorTool.generateId());
        account.setNickname("John Doe");
        account.setChipsGesamt(10000);

        alleAccountsMap.put(account.getId(), account);

        db.commit();

        return account;
    }

    @Override
    public List<Account> findAllAccounts() {

        return new ArrayList<Account>(alleAccountsMap.values());
    }

    @Override
    public Account findAccountById(String spielerId) {

        return alleAccountsMap.get(spielerId);
    }

    @Override
    public void setzeNickname(String accountId, String nickname) {

        Account account = findAccountById(accountId);

        if (account != null) {
            account.setNickname(nickname);
            alleAccountsMap.put(accountId, account);
            db.commit();
        }
    }

    @Override
    public void aendereChips(String accountId, int changeAmount) {

        Account account = findAccountById(accountId);

        if (account != null) {
            account.setChipsGesamt(account.getChipsGesamt() + changeAmount);
            alleAccountsMap.put(accountId, account);
            db.commit();
        }
    }
}
