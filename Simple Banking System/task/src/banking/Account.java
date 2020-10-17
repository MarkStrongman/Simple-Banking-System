package banking;

import java.util.Random;

public class Account {
    private String cardNumber;
    private String pin;
    private int balance;

    public Account() {
        this.pin = generatePin();
        this.cardNumber = generateCardNumber();
        this.balance = 0;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getPin() {
        return this.pin;
    }

    public int getBalance() {
        return balance;
    }

    ////////////////////methods////////////////////////
    private String generatePin() {
        Random generatePin = new Random();
        int randomPin = generatePin.nextInt(9999);
        String pinR = String.format("%04d", randomPin);
        if (pinR.length()==3) {
            pinR = "0"+pinR;
        }
        return pinR;
    }

    private String generateCardNumber() {
        String bankID = "400000";
        Random generateAcc = new Random();
        int randomAcc = generateAcc.nextInt(999999999);
        int helperAcc = randomAcc;
        int sum = 0;
        int odd = 1;
        int lastDigit = 0;
        while (randomAcc > 0) {
            lastDigit = 0;
            lastDigit = randomAcc % 10;
            if (odd % 2 != 0) {
                lastDigit = lastDigit * 2;
            }
            if (lastDigit > 9) {
                lastDigit = lastDigit - 9;
            }
            sum = sum + lastDigit;
            randomAcc = randomAcc / 10;
            odd += 1;
        }
        sum = sum + 8;
        int checksum = 0;
        while (true) {
            if (sum % 10 == 0) {
                break;
            }
            checksum += 1;
            sum = sum + 1;
        }

        String accountNumber = String.format("%09d", helperAcc);
        String checkSum = String.valueOf(checksum);

        return bankID + accountNumber + checkSum;
    }

    @Override
    public String toString() {
        return ("Your card has been created \n" +
                "Your card number: \n" +
                this.cardNumber + "\n" +
                "Your card PIN: \n" +
                this.pin);
    }
}

