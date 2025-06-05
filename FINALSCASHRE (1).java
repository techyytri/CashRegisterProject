import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FINALSCASHRE {
    static Scanner scan = new Scanner(System.in);
    static ArrayList<String> userName = new ArrayList<>();
    static ArrayList<String> userPassword = new ArrayList<>();
    
    static ArrayList<String> proList = new ArrayList<>();
    static ArrayList<Double> prcList = new ArrayList<>();
    static ArrayList<Integer> qtList = new ArrayList<>();

    
    static final String USERNAMEREGEX = "^[a-zA-Z0-9]{5,20}$";
    static final String PASSWORDREGEX = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$";

    public static void userSignUp() {
        while (true) {
            System.out.print("Create Username (5-20 alphanumeric characters): ");
            String usName = scan.nextLine();
            
            if (!usName.matches(USERNAMEREGEX)) {
                System.out.println("Invalid username format. Try again.");
                continue;
            }

            System.out.print("Create Password (8-20 characters, 1 uppercase, 1 number): ");
            String userPass = scan.nextLine();
            
            if (!userPass.matches(PASSWORDREGEX)) {
                System.out.println("Invalid password format. Try again.");
                continue;
            }

            userName.add(usName);
            userPassword.add(userPass);
            
            System.out.println("Account created successfully!");
            break;
        }
    }

    public static String userLogIn() {
        boolean isLoggedIn = false;
        
        String cashierName = "";
        
        while (!isLoggedIn) {
            System.out.print("Enter Username: ");
            String user = scan.nextLine();
            System.out.print("Enter Password: ");
            String pass = scan.nextLine();

            for (int i = 0; i < userName.size(); i++) {
                if (userName.get(i).equals(user) && userPassword.get(i).equals(pass)) {
                    isLoggedIn = true;
                    cashierName = user;
                    break;
                }
            }

            if (!isLoggedIn) {
                System.out.println("Incorrect username or password. Try again.");
            } else {
                System.out.println("Login successful!");
            }
        }
        return cashierName;
    }

    public static void addProduct() {
        
        System.out.println("--------------------");
        System.out.print("Enter Product Name: ");
        
        String prodName = scan.nextLine();
        proList.add(prodName);

        try {
            System.out.print("Enter Product Price: ");
            double proPrice = scan.nextDouble();
            prcList.add(proPrice);

            System.out.print("Enter Product Quantity: ");
            int proQt = scan.nextInt();
            qtList.add(proQt);
            scan.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter numeric values.");
            scan.nextLine();
        }
    }

    public static void updateProduct() {
        display();
        
        System.out.print("Enter Product Name to Update Quantity: ");
        String prodName = scan.nextLine();
        
        boolean found = false;

        for (int i = 0; i < proList.size(); i++) {
            if (proList.get(i).equalsIgnoreCase(prodName)) {
                try {
                    System.out.print("Enter New Quantity: ");
                    int newQuantity = scan.nextInt();
                    scan.nextLine();
                    qtList.set(i, newQuantity);
                    found = true;
                    System.out.println("Product quantity updated successfully!");
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scan.nextLine();
                }
                break;
            }
        }

        if (!found) {
            System.out.println("Product not found.");
        }
    }

    public static void remProd() {
        display();
        
        System.out.print("Enter Product Name to Remove: ");
        String prodName = scan.nextLine();
        
        boolean found = false;

        for (int i = 0; i < proList.size(); i++) {
            if (proList.get(i).equalsIgnoreCase(prodName)) {
                proList.remove(i);
                prcList.remove(i);
                qtList.remove(i);
                found = true;
                System.out.println("Product removed successfully!");
                break;
            }
        }

        if (!found) {
            System.out.println("Product not found.");
        }
    }

    public static double display() {
        System.out.println("\nItem Purchased | Qty | Price");
        System.out.println("--------------------------------");

        double totalAmount = 0;
        for (int i = 0; i < proList.size(); i++) {
            System.out.printf("%-15s | %-3d | %-6.2f\n", proList.get(i), qtList.get(i), prcList.get(i));
            totalAmount += qtList.get(i) * prcList.get(i);
        }

        System.out.println("\nTotal Amount: " + totalAmount);
        return totalAmount;
    }

    public static void proPayment(String cashierName) {
        display();
        try {
            System.out.print("Enter Payment: ");
            double pay = scan.nextDouble();
            scan.nextLine();
            double change = pay - display();

            if (pay >= display()) {
                System.out.println("===========================");
                System.out.printf("Change: %.2f\n", change);
                System.out.println("===========================");
                System.out.println("Thank you for purchasing!");
                saveTransaction(cashierName, display());
            } else {
                System.out.println("Invalid payment. Not enough money.");
            }
        } catch (Exception e) {
            System.out.println("Invalid payment input.");
            scan.nextLine();
        }
    }

    public static void saveTransaction(String cashierName, double totalAmount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.txt", true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

            writer.write("Date: " + now.format(formatter));
            writer.newLine();
            writer.write("Cashier Name: " + cashierName);
            writer.newLine();
            writer.write("Item Purchased | Qty | Price");
            writer.newLine();

            for (int i = 0; i < proList.size(); i++) {
                writer.write(proList.get(i) + "  " + qtList.get(i) + "  " + prcList.get(i));
                writer.newLine();
            }

            writer.write("Total Amount: " + totalAmount);
            writer.newLine();
            writer.write("-------------------------------------------");
            writer.newLine();

            System.out.println("Transaction saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving transaction.");
        }
    }

    public static void showTransactions() {
        System.out.println("\n======== Transaction History ========");
        try (BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("No transactions found.");
        }
    }

    public static void main(String[] args) {
        System.out.println("===! Welcome to CASHREGISTER !=== ");
        userSignUp();
        String cashierName = userLogIn();

        boolean continueShopping = true;
        while (continueShopping) {
            System.out.println("\n===============================");
            System.out.println("1. Add Product");
            System.out.println("2. Update Quantity");
            System.out.println("3. Remove Product");
            System.out.println("4. Display Orders");
            System.out.println("5. Checkout");
            System.out.println("6. Show Transaction History");
            System.out.println("7. Exit");
            System.out.println("===============================");
            
            System.out.print("Enter your choice: ");
            int choice = scan.nextInt();
            scan.nextLine();
            
            switch (choice) {
                case 1: addProduct(); 
                    break;
                case 2: updateProduct(); 
                    break;
                case 3: remProd(); 
                    break;
                case 4: display(); 
                    break;
                case 5: proPayment(cashierName); 
                    break;
                case 6: showTransactions(); 
                    break;
                case 7: System.out.println("Thank you for using CASHREGISTER!"); 
                    continueShopping = false; 
                    break;
                default: System.out.println("Invalid input. Try again."); 
                    break;
            }
        }
    }
}
