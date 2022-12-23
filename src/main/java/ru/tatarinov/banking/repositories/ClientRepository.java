package ru.tatarinov.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tatarinov.banking.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
