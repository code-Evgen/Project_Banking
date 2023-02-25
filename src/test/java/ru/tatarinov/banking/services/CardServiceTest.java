package ru.tatarinov.banking.services;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Client;
import ru.tatarinov.banking.model.Transaction;
import ru.tatarinov.banking.repositories.CardRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CardServiceTest {
    @Autowired
    private CardService cardService;

    @MockBean
    private ClientService clientService;
    @MockBean
    private CardRepository cardRepository;
    @MockBean
    private TransactionService transactionService;

    @Test
    void createCardSuccessTest() {
        int clientId = 1;
        Card card = new Card();

        Mockito.doReturn(new Client())
                .when(clientService)
                .getClientById(1);

        boolean isCardCreated = cardService.createCard(clientId, card);

        Assert.assertTrue(isCardCreated);
        Assert.assertNotNull(card.getOwner());

        Mockito.verify(cardRepository, Mockito.times(1)).save(card);
    }

    @Test
    void createCardFailTest() {
        int clientId = 1;
        Card card = new Card();

        Mockito.doReturn(null)
                .when(clientService)
                .getClientById(1);

        boolean isCardCreated = cardService.createCard(clientId, card);

        Assert.assertFalse(isCardCreated);
        Mockito.verify(cardRepository, Mockito.times(0)).save(ArgumentMatchers.any(Card.class));
    }

    @Test
    void refillSuccessTest() {
        int cardId = 1;
        float amount = 10;

        Card card = new Card();
        card.setId(cardId);
        card.setBalance(100);

        Mockito.doReturn(Optional.of(card))
                .when(cardRepository)
                .findById(cardId);

        boolean isRefillCompleted = cardService.refill(1, 10);

        Assert.assertTrue(isRefillCompleted);
        Assert.assertEquals(110, card.getBalance(), 0);
        Mockito.verify(cardRepository, Mockito.times(1)).save(card);
    }

    @Test
    void refillFailTest() {
        int cardId = 1;
        float amount = 10;

        boolean isRefillCompleted = cardService.refill(1, 10);

        Assert.assertFalse(isRefillCompleted);
        Mockito.verify(cardRepository, Mockito.times(0)).save(ArgumentMatchers.any(Card.class));
    }

    @Test
    void proceedTransferringSuccessTest() {
        Transaction transaction = new Transaction();
        Card sourceCard = new Card();
        Card destinationCard = new Card();
        sourceCard.setId(1);
        sourceCard.setBalance(100);

        destinationCard.setId(2);
        destinationCard.setBalance(100);
        transaction.setSource(sourceCard);
        transaction.setDestination(destinationCard);

        transaction.setAmount(20);

        Mockito.doReturn(Optional.of(sourceCard))
                .when(cardRepository)
                .findById(sourceCard.getId());

        Mockito.doReturn(Optional.of(destinationCard))
                .when(cardRepository)
                .findById(destinationCard.getId());

        boolean isProceedTransferring = cardService.proceedTransferring(transaction);

        Assert.assertTrue(isProceedTransferring);
        Assert.assertEquals(80, sourceCard.getBalance(), 0);
        Assert.assertEquals(120, destinationCard.getBalance(), 0);
        Mockito.verify(transactionService, Mockito.times(1)).saveTransaction(transaction);
    }

    @Test
    void proceedTransferringFailTest() {
        Transaction transaction = new Transaction();
        Card sourceCard = new Card();
        Card destinationCard = new Card();
        sourceCard.setId(1);
        sourceCard.setBalance(100);

        destinationCard.setId(2);
        destinationCard.setBalance(100);
        transaction.setSource(sourceCard);
        transaction.setDestination(destinationCard);

        transaction.setAmount(20);

        boolean isProceedTransferring = cardService.proceedTransferring(transaction);

        Assert.assertFalse(isProceedTransferring);
        Assert.assertEquals(100, sourceCard.getBalance(), 0);
        Assert.assertEquals(100, destinationCard.getBalance(), 0);
        Mockito.verify(transactionService, Mockito.times(0)).saveTransaction(ArgumentMatchers.any(Transaction.class));
    }
}