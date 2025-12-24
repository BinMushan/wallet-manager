package com.example.moneymanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.nio.file.Files;

public class HelloController {

    @FXML
    private Label balanceLabel;

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<String> creditComboBox;

    @FXML
    private ComboBox<String> debitComboBox;

    @FXML
    private ComboBox<String> expenseComboBox;

    @FXML
    private ComboBox<String> incomeComboBox;

    @FXML
    private ListView<String> historyListView;

    @FXML
    private Button resetButton;

    private double balance = 0.0;
    private ObservableList<String> history = FXCollections.observableArrayList();

    // FILE PATHS
    private final File balanceFile = new File("balance.txt");
    private final File historyFile = new File("history.txt");


    @FXML
    public void initialize() {
        historyListView.setItems(history);

        loadBalance();
        loadHistory();

        updateBalanceLabel();

        // Pre-populate ComboBoxes
        incomeComboBox.setItems(FXCollections.observableArrayList("Salary", "Gift", "Business", "Other"));
        expenseComboBox.setItems(FXCollections.observableArrayList("Food", "Transport", "Bills", "Shopping", "Other"));
        creditComboBox.setItems(FXCollections.observableArrayList("Bank Loan", "Refund","Cash Deposits", "Other"));
        debitComboBox.setItems(FXCollections.observableArrayList("Online Payments", "EMI", "Bank Charge", "Other"));

        incomeComboBox.setValue("Select Income");
        expenseComboBox.setValue("Select Expense");
        creditComboBox.setValue("Credit Type");
        debitComboBox.setValue("Debit Type");
    }

    // -------------------BUTTON FUNCTIONS-----------------------

    @FXML
    private void addIncome() {
        if (incomeComboBox.getSelectionModel().getSelectedIndex() != -1) {
            processTransaction("Income", true, incomeComboBox);
        } else {
            showAlert("No Category", "Please select an Income type.");
        }
    }

    @FXML
    private void addExpense() {
        if (expenseComboBox.getSelectionModel().getSelectedIndex() != -1) {
            processTransaction("Expense", false, expenseComboBox);
        } else {
            showAlert("No Category", "Please select an Expense type.");
        }
    }

    @FXML
    private void addCredit() {
        if (creditComboBox.getSelectionModel().getSelectedIndex() != -1) {
            processTransaction("Credit", true, creditComboBox);
        } else {
            showAlert("No Category", "Please select a Credit type.");
        }
    }

    @FXML
    private void addDebit() {
        if (debitComboBox.getSelectionModel().getSelectedIndex() != -1) {
            processTransaction("Debit", false, debitComboBox);
        } else {
            showAlert("No Category", "Please select a Debit type.");
        }
    }

    @FXML
    private void reset() {
        balance = 0.0;
        history.clear();
        saveBalance();
        saveHistory();
        updateBalanceLabel();
    }

    // -------------------PROCESS TRANSACTION-----------------------

    private void processTransaction(String type, boolean isAddition, ComboBox<String> comboBox) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                showAlert("Invalid Amount", "Amount must be a positive number.");
                return;
            }

            String category = comboBox.getValue();
            if (category == null) {
                showAlert("No Category", "Please select a category.");
                return;
            }

            String sign = isAddition ? "+" : "-";
            balance += isAddition ? amount : -amount;

            String entry = String.format("%s%.0f | %s | %s", sign, amount, type, category);

            history.add(0, entry);

            updateBalanceLabel();
            amountField.clear();

            saveBalance();
            saveHistory();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for amount.");
        }
    }

    // -------------------UPDATE BALANCE LABEL-----------------------

    private void updateBalanceLabel() {
        balanceLabel.setText(String.format("Current Balance: %.2f", balance));
    }

    // -------------------FILE HANDLING SYSTEM-----------------------

    private void saveBalance() {
        try (FileWriter writer = new FileWriter(balanceFile)) {
            writer.write(String.valueOf(balance));
        } catch (IOException e) {
            showAlert("File Error", "Could not save balance.");
        }
    }

    private void loadBalance() {
        if (!balanceFile.exists()) {
            return;
        }
        try {
            String value = Files.readString(balanceFile.toPath());
            balance = Double.parseDouble(value.trim());
        } catch (Exception e) {
            balance = 0.0;
        }
    }

    private void saveHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile))) {
            for (String entry : history) {
                writer.write(entry);
                writer.newLine();
            }
        } catch (IOException e) {
            showAlert("File Error", "Could not save history.");
        }
    }

    private void loadHistory() {
        if (!historyFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            showAlert("File Error", "Could not load history.");
        }
    }

    // -------------------ALERT-----------------------

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
