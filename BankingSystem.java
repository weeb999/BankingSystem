import java.sql.*;
import java.util.Scanner;

public class BankingSystem {
    private Connection connection;
    private Statement statement;

    public BankingSystem() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "password");

            // Create a statement object
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    public void createAccount(String accountNumber, String accountHolder, double initialBalance) {
        try {
            // Create a new account
            String query = "INSERT INTO accounts (account_number, account_holder, balance) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, accountHolder);
            pstmt.setDouble(3, initialBalance);
            pstmt.executeUpdate();

            System.out.println("Account created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    public void deposit(String accountNumber, double amount) {
        try {
            // Retrieve the current balance
            String query = "SELECT balance FROM accounts WHERE account_number = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            double currentBalance = rs.getDouble("balance");

            // Update the balance
            query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setDouble(1, currentBalance + amount);
            pstmt.setString(2, accountNumber);
            pstmt.executeUpdate();

            System.out.println("Deposit successful!");
        } catch (SQLException e) {
            System.out.println("Error depositing: " + e.getMessage());
        }
    }

    public void withdraw(String accountNumber, double amount) {
        try {
            // Retrieve the current balance
            String query = "SELECT balance FROM accounts WHERE account_number = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            double currentBalance = rs.getDouble("balance");

            // Check if sufficient funds are available
            if (currentBalance >= amount) {
                // Update the balance
                query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setDouble(1, currentBalance - amount);
                pstmt.setString(2, accountNumber);
                pstmt.executeUpdate();

                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Insufficient funds!");
            }
        } catch (SQLException e) {
            System.out.println("Error withdrawing: " + e.getMessage());
        }
    }

    public void checkBalance(String accountNumber) {
        try {
            // Retrieve the current balance
            String query = "SELECT balance FROM accounts WHERE account_number = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            double currentBalance = rs.getDouble("balance");

            System.out.println("Current balance: " + currentBalance);
        } catch (SQLException e) {
            System.out.println("Error checking balance: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        BankingSystem bankSystem = new BankingSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check balance");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.next();
                    System.out.print("Enter account holder: ");
                    String accountHolder = scanner.next();
                    System.out.print("Enter initial balance: ");
                    double initialBalance = scanner.nextDouble();
                    bankSystem.createAccount(accountNumber, accountHolder, initialBalance);
                    break;
                case 2:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.next();
                    System.out.print("Enter amount to deposit: ");
                    double amount = scanner.nextDouble();
                    bankSystem.deposit(accountNumber, amount);
                    break;
                case 3:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.next();
                    System.out.print("Enter amount to withdraw: ");
                    amount = scanner.nextDouble();
                    bankSystem.withdraw(accountNumber, amount);
                    break;
            }
        }
    }
}