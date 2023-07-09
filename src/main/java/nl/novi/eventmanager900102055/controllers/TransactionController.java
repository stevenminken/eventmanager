package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.services.TransactionService;

public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
