package ru.tatarinov.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Transaction;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllBySourceOrderByTimestamp(Card source);

    List<Transaction> findAllBySourceAndDestinationOrderByTimestamp(Card source, Card destination);

    List<Transaction> findAllBySourceAndAmountAfter(Card source, Float amount);

    List<Transaction> findAllBySourceAndAmountBefore(Card source, Float amount);

    List<Transaction> findAllBySourceAndTimestampAfter(Card source, Date date);

    List<Transaction> findAllBySourceAndTimestampBefore(Card source, Date date);
}
