package banking;

import java.sql.*;
import java.util.Scanner;

public class Main extends Account {
    public static void main(String args[]) throws Exception {
        String dbName = args[1];
        Scanner scanner = new Scanner(System.in);
        Scanner balanceScan = new Scanner(System.in);
        Database db = new Database();
        Ui ui = new Ui(db);
        db.create(dbName);
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        outer:
        while (true) {
            ui.menu();
            String choice = ui.choice(scanner);
            System.out.println();
            if (choice.equals("0")) {
                System.out.println();
                System.out.println("Bye!");
                break;
            }
            if (choice.equals("1")) {
                ui.createAccount();
                System.out.println();
            }
            if (choice.equals("2")) {
                System.out.println("Enter your card number: ");
                String acNr = scanner.nextLine();
                System.out.println("Enter your PIN: ");
                String acPin = scanner.nextLine();
                System.out.println();
                ResultSet login = ui.accountLogin(dbName, acNr);
                String accNr = "";
                String accPin = "";
                if (!login.next()) {
                    ui.balanceLoginUnsuccessful();
                } else {
                    accNr = login.getString("number");
                    accPin = login.getString("pin");
                }
                do {
                    if (accNr.equals(acNr) && accPin.equals(acPin)) {
                        System.out.println("You have successfully logged in!");
                        System.out.println();
                        login.close();
                        while (true) {
                            ui.balanceLoginSuccessful();
                            String inputBalanceMenu = balanceScan.nextLine();
                            if (inputBalanceMenu.equals("1")) {
                                System.out.println();
                                ui.balanceInfo(dbName, acNr);
                            }
                            if (inputBalanceMenu.equals("2")) {
                                System.out.println("Enter income: ");
                                int balanceToAdd = Integer.valueOf(balanceScan.nextLine());
                                ui.balanceAdd(dbName, balanceToAdd, accNr);
                                System.out.println();
                            }
                            if (inputBalanceMenu.equals("3")) {
                                System.out.println("Transfer");
                                System.out.println("Enter card number:");
                                String transferToAccount = scanner.nextLine();
                                ui.transfer(dbName, accNr, transferToAccount);
                                System.out.println();
                            }
                            if (inputBalanceMenu.equals("4")) {
                                ui.closeAccount(dbName, accNr);
                                System.out.println();
                                break;
                            }
                            if (inputBalanceMenu.equals("5")) {
                                System.out.println();
                                break;
                            }
                            if (inputBalanceMenu.equals("0")) {
                                System.out.println("Bye!");
                                break outer;
                            }
                        }
                    }
                } while (login.next());
            }
        }
    }
}


