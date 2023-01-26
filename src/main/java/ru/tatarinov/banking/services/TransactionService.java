package ru.tatarinov.banking.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Transaction;
import ru.tatarinov.banking.repositories.TransactionRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void saveTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByCard(Card sourceCard){
        if (sourceCard != null) {
//            return transactionRepository.findAllBySourceOrderByTimestamp(sourceCard);
            return transactionRepository.findAllBySourceOrDestinationOrderByTimestamp(sourceCard, sourceCard);
        }
        return null;
    }

    public List<Transaction> getTransactionsByCardFilterByDestination(Card sourceCard, Card destinationCard){
        return transactionRepository.findAllBySourceAndDestinationOrderByTimestamp(sourceCard, destinationCard);
    }

    public List<Transaction> getTransactionsByCardFilterByAmountAfter(Card sourceCard, Float amount){
        return transactionRepository.findAllBySourceAndAmountAfter(sourceCard, amount);
    }

    public List<Transaction> getTransactionsByCardFilterByAmountBefore(Card sourceCard, Float amount){
        return transactionRepository.findAllBySourceAndAmountBefore(sourceCard, amount);
    }

    public List<Transaction> getTransactionsByCardFilterByTimestampAfter(Card sourceCard, Date date){
        return transactionRepository.findAllBySourceAndTimestampAfter(sourceCard, date);
    }

    public List<Transaction> getTransactionsByCardFilterByTimestampBefore(Card sourceCard, Date date){
        return transactionRepository.findAllBySourceAndTimestampBefore(sourceCard, date);
    }
}
