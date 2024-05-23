/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package magazine_service;

import java.io.Serializable;

/**
 * The PaymentMethod class represents the payment method of a customer in the magazine service.
 * It contains information about the type of payment (bank account or credit/debit card) and the account number.
 * 
 * @author Zack Cornfield
 */
public class PaymentMethod implements Serializable {    
    /**
     * Enum representing different types of payment methods.
     */
    public enum PaymentType {
        BANK_ACCOUNT,
        CREDIT_DEBIT_CARD
    }
    
    private PaymentType paymentType;
    private String accountNumber;
    
    /**
     * Default constructor for PaymentMethod class.
     * Initializes the payment type as credit/debit card and account number as empty string.
     */
    public PaymentMethod() {
        this.paymentType = PaymentType.CREDIT_DEBIT_CARD; 
        this.accountNumber = ""; 
    }
    
    /**
     * Parameterized constructor for PaymentMethod class.
     * 
     * @param type The type of payment method.
     * @param accountNumber The account number associated with the payment method.
     */
    public PaymentMethod(PaymentType type, String accountNumber) {
        this.paymentType = type;
        this.accountNumber = accountNumber;
    }

    // Getters and setters...

    /**
     * Retrieves the type of payment method.
     * 
     * @return The type of payment method.
     */
    public PaymentType getPaymentType() {
        return paymentType;
    }

    /**
     * Sets the type of payment method.
     * 
     * @param type The type of payment method to set.
     */
    public void setPaymentType(PaymentType type) {
        this.paymentType = type;
    }

    /**
     * Retrieves the account number associated with the payment method.
     * 
     * @return The account number associated with the payment method.
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the account number associated with the payment method.
     * 
     * @param accountNumber The account number to set.
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
