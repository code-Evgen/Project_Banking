package ru.tatarinov.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Client;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    List<Card> findAllByOwnerOrderById(Client client);
}
