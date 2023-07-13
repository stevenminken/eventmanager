package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.dtos.TicketDto;
import nl.novi.eventmanager900102055.dtos.TransactionDto;
import nl.novi.eventmanager900102055.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @GetMapping
    public ResponseEntity<List<TransactionDto>> findAllTransactions() {
        List<TransactionDto> TransactionDtoList;
        TransactionDtoList = transactionService.findAllTransactions();
        return ResponseEntity.ok().body(TransactionDtoList);
    }
}
