package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.TransactionDto;
import nl.novi.eventmanager900102055.models.Transaction;
import nl.novi.eventmanager900102055.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionDto> findAllTransactions() {
        List<Transaction> transactionList = transactionRepository.findAll();
        return transferTransactionListToTransactionDtoList(transactionList);
    }

    public TransactionDto createTransaction(TransactionDto transactionDto) {
        Transaction transaction = transferTransactionDtoToTransaction(transactionDto);
        return transferTransactionToTransactionDto(transactionRepository.save(transaction));
    }

    public TransactionDto findTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            return null;
        }
        return transferTransactionToTransactionDto(transaction);
    }

    public TransactionDto updateTransaction(Long id, TransactionDto transactionDto) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            return null;
        }
        transaction.setDateOfPurchase(transactionDto.getDateOfPurchase());
        transaction.setPaymentMethod(transactionDto.getPaymentMethod());
        return transferTransactionToTransactionDto(transactionRepository.save(transaction));
    }

    public boolean deleteTransaction(Long id) {
        if(transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<TransactionDto> transferTransactionListToTransactionDtoList(List<Transaction> transactionList) {
        List<TransactionDto> transactionDtoList = new ArrayList<>();

        for (Transaction transaction : transactionList) {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setId(transaction.getId());
            transactionDto.setDateOfPurchase(transaction.getDateOfPurchase());
            transactionDto.setPaymentMethod(transaction.getPaymentMethod());
            transactionDtoList.add(transactionDto);
        }
        return transactionDtoList;
    }

    public Transaction transferTransactionDtoToTransaction(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionDto.getId());
        transaction.setDateOfPurchase(transactionDto.getDateOfPurchase());
        transaction.setPaymentMethod(transactionDto.getPaymentMethod());

        return transaction;
    }

    public TransactionDto transferTransactionToTransactionDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();

        transactionDto.setId(transaction.getId());
        transactionDto.setDateOfPurchase(transaction.getDateOfPurchase());
        transactionDto.setPaymentMethod(transaction.getPaymentMethod());

        return transactionDto;
    }
}
