package banking;

import org.sqlite.SQLiteDataSource;
import org.w3c.dom.ls.LSOutput;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Ui {
    private Scanner scanner;
    private Database db;

    public Ui(Database db) {
        this.db = db;
    }

    public String choice(Scanner scanner) {
        this.scanner = scanner;
        String inputChoice = scanner.nextLine();
        return inputChoice;
    }

    public void menu() {
        System.out.println("1. Create account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    public void createAccount() throws SQLException {
        Account account = new Account();
        System.out.println("Your card have been created");
        System.out.println("Your card number: ");
        System.out.println(account.getCardNumber());
        System.out.println("Your card PIN: ");
        System.out.println(account.getPin());
        db.update("card.s3db", account.getCardNumber(), account.getPin());
    }

    public ResultSet accountLogin(String dbName, String accountNumber) throws Exception {
        try {
            ResultSet account = db.getAccountCred(dbName, accountNumber);
            return account;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    private AutoCloseable accountLogin() {
        return null;
    }

    public void balanceInfo(String dbName, String number) {
        try {
            ResultSet balance = db.getAccountBalance(dbName, number);
            System.out.println("Balance: " + balance.getInt("balance"));
            System.out.println();
            balance.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void balanceAdd(String dbName, int balanceToAdd, String accNumber) throws SQLException {
        try {
            db.addIncome(dbName, balanceToAdd, accNumber);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void transfer(String dbName, String fromAccount, String toAccount) {
        try {
            db.transferMoney(dbName, fromAccount, toAccount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeAccount(String dbName, String accNumber) throws SQLException {
        try {
            db.removeAccount(dbName, accNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void balanceLoginSuccessful() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    public void balanceLoginUnsuccessful() {
        System.out.println("Wrong card number or PIN!");
        System.out.println();
    }

}
