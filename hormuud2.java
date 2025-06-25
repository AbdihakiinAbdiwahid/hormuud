import java.util.*;
import java.io.*;
public class hormuud2 {
    static Scanner sc = new Scanner(System.in);
    static final String CORRECT_PIN = "2025";
    static double balance = 0.0;
    static LinkedList<String> transactions = new LinkedList<>();

    static final String BALANCE_FILE = "balance.txt";
    static final String TRANSACTIONS_FILE = "transactions.txt";

    public static void main(String[] args) {
        loadBalance();
        loadTransactions();

        System.out.println("Fadlan geli USSD  code ");
        String ussd = sc.nextLine();

        if (!ussd.equals("*770#")) {
            System.out.println("USSD code khalad. Barnaamijku wuu xirmayaa.");
            return;
        }

        if (!verifyPIN()) {
            System.out.println("3 jeer PIN khaldan, barnaamijku wuu xirmayaa.");
            return;
        }

        while (true) {
            System.out.println("\n--- EVC PLUS Menu ---");
            System.out.println("1. Itus Harraagaga");
            System.out.println("2. Dir Lacag");
            System.out.println("3. Iibso Kaarka Hadalka");
            System.out.println("4. Bixi Biil");
            System.out.println("5. Uwareeji EVC PLUS");
            System.out.println("6. Warbixin Kooban");
            System.out.println("7. Salaam Bank Info");
            System.out.println("8. Mareynta Account");
            System.out.println("9. Bill Payment Dhammaan");
            System.out.println("10. Ka Bax");
            System.out.print("Fadlan dooro adeeg: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> showBalance();
                case 2 -> sendMoney();
                case 3 -> buyCredit();
                case 4 -> payBill();
                case 5 -> transferEVC();
                case 6 -> showTransactions();
                case 7 -> showSalaamBankInfo();
                case 8 -> accountManagement();
                case 9 -> payAllBills();
                case 10 -> {
                    System.out.println("Mahadsanid, Nabad gelyo!");
                    saveData();
                    return;
                }
                default -> System.out.println("Fadlan doorasho sax ah geli.");
            }
        }
    }

    static void loadBalance() {
        try (BufferedReader br = new BufferedReader(new FileReader(BALANCE_FILE))) {
            String line = br.readLine();
            balance = (line != null) ? Double.parseDouble(line) : 1500.0;
        } catch (Exception e) {
            balance = 1500.0;
        }
    }

    static void loadTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                transactions.add(line);
            }
        } catch (Exception e) {}
    }

    static void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BALANCE_FILE))) {
            pw.println(balance);
        } catch (IOException e) {
            System.out.println("Error saving balance.");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (String tr : transactions) {
                pw.println(tr);
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions.");
        }
    }

    static boolean verifyPIN() {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Fadlan geli  PIN-kaaga: ");
            if (sc.nextLine().equals(CORRECT_PIN)) return true;
            attempts++;
            System.out.println("PIN khaldan, isku day mar kale.");
        }
        return false;
    }

    static boolean confirmPIN() {
        System.out.print("Fadlan mar kale geli PIN-ka: ");
        return sc.nextLine().equals(CORRECT_PIN);
    }

    static void showBalance() {
        System.out.printf("Harraagaaga waa: %.2f USD\n", balance);
    }

    static void sendMoney() {
        System.out.print("Geli lambarka qofka: ");
        String number = sc.nextLine();
        System.out.print("Geli lacagta: ");
        double amount = getDoubleInput();

        if (amount > balance) {
            System.out.println("Lacag kugu filan ma jirto."); return;
        }
        if (!confirmPIN()) {
            System.out.println("PIN khaldan. Lacag lama dirin."); return;
        }
        balance -= amount;
        addTransaction("Diris: " + amount + " USD --> " + number);
        System.out.println("Lacagta waa la diray.");
    }

    static void buyCredit() {
        System.out.println("--- Iibso Kaarka Hadalka ---");
        System.out.println("1. Ku Shubo Air Time");
        System.out.println("2. Ugu Shub Qof Kale");
        System.out.println("3. MIFI Packages");
        System.out.println("4. Ku Shubo Internet");
        System.out.println("5. Ugu Shub Qof Kale (MMT)");
        System.out.print("Dooro adeeg: ");
        int choice = getIntInput();

        switch (choice) {
            case 1 : buy("Air Time");
            case 2 : buy("Ugu shub qof kale");
            case 3 : buy("MIFI Packages");
            case 4 : buy("Internet");
            case 5 : buy("Ugu shub MMT");
            default : System.out.println("Doorasho khaldan.");
        }
    }

    static void buy(String type) {
        System.out.print("Geli lacagta: ");
        double amount = getDoubleInput();
        if (amount > balance) {
            System.out.println("Lacag kugu filan ma jirto.");
            return;
        }
        balance -= amount;
        addTransaction("Iibsasho " + type + ": " + amount + " USD");
        System.out.println(type + " waa lagu guuleystay.");
    }

    static void payBill() {
        System.out.println("--- Bixi Biil ---");
        System.out.println("1. Post Paid");
        System.out.println("2. Ku Iibso");
        System.out.print("Dooro nooca biilka: ");
        int choice = getIntInput();

        switch (choice) {
            case 1 : paySpecificBill("Post Paid");
            case 2 : paySpecificBill("Ku Iibso");
            default : System.out.println("Doorasho khaldan.");
        }
    }

    static void paySpecificBill(String type) {
        System.out.print("Geli magaca adeegga: ");
        String name = sc.nextLine();
        System.out.print("Geli lacagta: ");
        double amount = getDoubleInput();
        if (amount > balance) {
            System.out.println("Lacag kugu filan ma jirto.");
            return;
        }
        balance -= amount;
        addTransaction("Bixinta biil " + type + ": " + name + " - " + amount + " USD");
        System.out.println("Biilka " + type + " waa la bixiyay.");
    }

    static void transferEVC() {
        System.out.println("--- Uwareeji EVC ---");
        System.out.println("1. Fadlan keliya Mobile ka");
        System.out.print("Dooro: ");
        int choice = getIntInput();

        if (choice == 1) {
            System.out.print("Geli lambarka: ");
            String number = sc.nextLine();
            System.out.print("Geli lacagta: ");
            double amount = getDoubleInput();
            if (amount > balance) {
                System.out.println("Lacag kugu filan ma jirto.");
                return;
            }
            balance -= amount;
            addTransaction("Uwareejin Mobile: " + number + " - " + amount + " USD");
            System.out.println("Lacag waa la wareejiyay.");
        } else {
            System.out.println("Doorasho khaldan.");
        }
    }

    static void showTransactions() {
        System.out.println("--- Warbixin Kooban ---");
        System.out.println("1. Last Action");
        System.out.println("2. Wareejintii u dambeysay");
        System.out.println("3. Last 3 Actions");
        System.out.println("4. Email Me My Activity");
        System.out.print("Dooro adeeg: ");
        int choice = getIntInput();

        switch (choice) {
            case 1 : {
                if (!transactions.isEmpty())
                    System.out.println("Ugu dambeyn: " + transactions.getLast());
                else
                    System.out.println("Macaamilo ma jiraan.");
            }
            case 2 : showLastTransfer();
            case 3 : {
                System.out.println("3-dii Macaamil ee u dambeysay:");
                transactions.forEach(System.out::println);
            }
            case 4 : System.out.println("Email loo diray (la hubo). (Demo)");
            default : System.out.println("Doorasho khaldan.");
        }
    }

    static void showLastTransfer() {
        for (int i = transactions.size() - 1; i >= 0; i--) {
            if (transactions.get(i).contains("Uwareejin")) {
                System.out.println("Wareejintii u dambeysay: " + transactions.get(i));
                return;
            }
        }
        System.out.println("Wax wareejin ah lagama helin.");
    }

    static void showSalaamBankInfo() {
        System.out.println("--- Salaam Bank Info ---");
        System.out.println("1. Itus Haraaga");
        System.out.println("2. Lacag Dhigasho");
        System.out.println("3. Lacag Ka Wareeji");
        System.out.print("Dooro: ");
        int choice = getIntInput();

        switch (choice) {
            case 1 : showBalance();
            case 2 : {
                System.out.print("Geli lacagta: ");
                double amount = getDoubleInput();
                balance += amount;
                addTransaction("Lacag la dhigay Salaam Bank: " + amount + " USD");
            }
            case 3 : {
                System.out.print("Geli lacagta laga wareejinayo: ");
                double amount = getDoubleInput();
                if (amount > balance) {
                    System.out.println("Harraaga kuma filna.");
                    return;
                }
                balance -= amount;
                addTransaction("Lacag laga wareejiyay Salaam Bank: " + amount + " USD");
            }
            default : System.out.println("Doorasho aan sax ahayn.");
        }
    }

    static void accountManagement() {
        System.out.println("--- Mareynta Account ---");
        System.out.println("1. Bedel Lambarka Sirta ah");
        System.out.println("2. Bedel Luuqada");
        System.out.println("3. Wargelin Mobile-ka lumay");
        System.out.println("4. Xir Lacagta");
        System.out.println("5. U celi Lacagta Qaldantay");
        System.out.print("Dooro adeeg: ");
        int choice = getIntInput();

        switch (choice) {
            case 1 : System.out.println("Lambarka sirta ah waa la bedelay. (Demo)");
            case 2 : System.out.println("Luuqada waa la bedelay. (Demo)");
            case 3 : System.out.println("Mobile lumay wargelin waa la sameeyay.");
            case 4 : System.out.println("Lacagta waa la xiray si KMG ah.");
            case 5 : System.out.println("Lacagtii qaldantay waa la celiyay (demo).");
            default : System.out.println("Doorasho khaldan.");
        }
    }

    static void payAllBills() {
        System.out.println("--- Bill Payment Dhammaan ---");
        System.out.println("1. Itus Haraaga Biilka");
        System.out.println("2. Wada Bixi Biilka");
        System.out.println("3. Qeyb Ka Bixi Biilka");
        System.out.print("Dooro adeeg: ");
        int choice = getIntInput();

        switch (choice) {
            case 1 : System.out.println("Biilkaaga harraagiisu waa: $45.75 USD");
            case 2 : paySpecificBill("Dhammaan");
            case 3 : paySpecificBill("Qeyb");
            default : System.out.println("Doorasho khaldan.");
        }
    }

    static void addTransaction(String trans) {
        if (transactions.size() == 3) transactions.removeFirst();
        transactions.add(trans);
    }

    static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Fadlan geli nambar sax ah: ");
            }
        }
    }

    static double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Fadlan geli nambar sax ah: ");
            }
        }
    }
}