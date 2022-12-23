package ru.tatarinov.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tatarinov.banking.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

}
