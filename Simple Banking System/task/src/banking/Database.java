package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class Database {

    public Connection connect(String dbName) {
        String url = "jdbc:sqlite:" + dbName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try {
            Connection connection = dataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            return null;
        }
    }

    public void create(String dbName) throws SQLException {
        Connection con = null;
        Statement statement = null;
        try {
            con = connect(dbName);
            statement = con.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER NOT NULL," +
                    "number TEXT," +
                    "pin TEXT," +
                    "balance INTEGER DEFAULT 0, " +
                    "PRIMARY KEY(id))"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void update(String dbName, String number, String pin) throws SQLException {
        Connection con = null;
        Statement statement = null;
        try {
            con = connect(dbName);
            statement = con.createStatement();
            statement.executeUpdate("INSERT INTO card (number, pin, balance) VALUES " +
                    "(" + number + ", " + pin + ", 0)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public ResultSet getAccountBalance(String dbName, String number) throws SQLException {
        Connection con = null;
        Statement statement = null;
        ResultSet myRs = null;
        try {
            con = connect(dbName);
            statement = con.createStatement();
            myRs = statement.executeQuery("SELECT * FROM card WHERE number = " + number);
            if (myRs.isClosed()) {
                return null;
            }
            return myRs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getAccountCred(String dbName, String number) throws SQLException {
        Connection con = null;
        Statement statement = null;
        ResultSet myRs = null;
        try {
            con = connect(dbName);
            statement = con.createStatement();
            myRs = statement.executeQuery("SELECT * FROM card WHERE number = " + number);
            return myRs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addIncome(String dbName, int incomeToAdd, String number) throws SQLException {
        Connection con = null;
        Statement statement = null;
        try {
            con = connect(dbName);
            statement = con.createStatement();
            statement.executeUpdate("UPDATE card SET balance = balance + " + incomeToAdd + " WHERE number = " + number);
            System.out.println("Income was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            statement.close();
        }
    }

    public void removeAccount(String dbName, String number) throws SQLException {
        Connection con = null;
        Statement statement = null;
        try {
            con = connect(dbName);
            statement = con.createStatement();
            statement.executeUpdate("DELETE FROM card WHERE number = " + number);
            System.out.println("The account has been closed!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            statement.close();
        }
    }

    public void transferMoney(String dbName, String fromNumber, String toNumber) throws SQLException {
        Connection con = null;
        Statement statement = null;
        ResultSet myRs = null;
        int fromBalance = 0;
        Scanner scanner = new Scanner(System.in);
        try {
            con = connect(dbName);
            myRs = getAccountBalance(dbName, fromNumber);
            fromBalance = myRs.getInt("balance");
            myRs.close();
            statement = con.createStatement();
            myRs = statement.executeQuery("SELECT * FROM card WHERE number = " + toNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (fromNumber.equals(toNumber)) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }
        boolean accountExists = false;
        while (myRs.next()) {
            String transferAccount = myRs.getString("number");
            if (transferAccount.equals(toNumber)) {
                accountExists = true;
                break;
            }
        }
        if (!isLuhm(toNumber)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return;
        }
        if (!accountExists) {
            System.out.println("Such a card does not exist.");
            return;
        }
        System.out.println("Enter how much money you want to transfer: ");
        int amount = Integer.parseInt(scanner.nextLine());
        if (fromBalance < amount) {
            statement.close();
            myRs.close();
            System.out.println("Not enough money!");
            return;
        }
        try {
            statement.close();
            myRs.close();
            statement = con.createStatement();
            statement.executeUpdate("UPDATE card SET balance = balance + " + amount + " WHERE number = " + toNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.close();
            myRs.close();
            statement = con.createStatement();
            statement.executeUpdate("UPDATE card SET balance = balance - " + amount + " WHERE number = " + fromNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Success!");
        return;
    }

/*    public Boolean isLuhm(String cardNumber) {
        int digits = cardNumber.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = digits - 1; i >= 0; i--) {
            int d = cardNumber.charAt(i) - '0';
            if (isSecond == true) {
                d = d * 2;
                nSum += d / 10;
                nSum += d % 10;

                isSecond = !isSecond;
            }
            return (nSum % 10 == 0);
        }

        return false;
    }*/

    public static boolean isLuhm(String cardNumber) {
        // int array for processing the cardNumber
        int[] cardIntArray = new int[cardNumber.length()];
        for (int i = 0; i < cardNumber.length(); i++) {
            char c = cardNumber.charAt(i);
            cardIntArray[i] = Integer.parseInt("" + c);
        }
        for (int i = cardIntArray.length - 2; i >= 0; i = i - 2) {
            int num = cardIntArray[i];
            num = num * 2;  // step 1
            if (num > 9) {
                num = num % 10 + num / 10;  // step 2
            }
            cardIntArray[i] = num;
        }
        int sum = sumDigits(cardIntArray);  // step 3
        if (sum % 10 == 0)  // step 4
        {
            return true;
        }
        return false;
    }
    public static int sumDigits(int[] arr) {
        return Arrays.stream(arr).sum();
    }

}
