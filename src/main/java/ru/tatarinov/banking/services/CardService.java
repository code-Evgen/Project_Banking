package ru.tatarinov.banking.services;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Client;
import ru.tatarinov.banking.model.Transaction;
import ru.tatarinov.banking.repositories.CardRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class CardService {
    private final CardRepository cardRepository;
    private final ClientService clientService;
    private final TransactionService transactionService;

    public CardService(CardRepository cardRepository, ClientService clientService, TransactionService transactionService) {
        this.cardRepository = cardRepository;
        this.clientService = clientService;
        this.transactionService = transactionService;
    }

    public Card getCardById(int id){
        return cardRepository.findById(id).orElse(null);
    }

    public List<Integer> getCardIdByClientId(int id){
        Client client = clientService.getClientById(id);
        if (client != null){
            Hibernate.initialize(client.getCards());
            List<Card> cards = client.getCards();
            List<Integer> cardsId = new ArrayList<>();
            for(Card card : cards){
                cardsId.add(card.getId());
            }
            cardsId.sort(Comparator.comparingInt(o -> o));
            return cardsId;
        }
        else
            return Collections.emptyList();
    }

    public Client getClientByCardId(int id){
        Optional<Card> card = cardRepository.findById(id);
        if (card.isPresent()){
            return card.get().getOwner();
        }
        return null;
    }

    @Transactional
    public void refill(int id, float amount){
        Card card = cardRepository.findById(id).orElse(null);
        System.out.println(card.getId());
        if (card != null){
            System.out.println(card.getBalance());
            card.setBalance(card.getBalance() + amount);
            System.out.println(card.getBalance());
        }
        cardRepository.save(card);
    }

    @Transactional
    public void createCard(int clientId, Card card){
        //card.setId(0);
        Client client = clientService.getClientById(clientId);
        if (client != null){
            card.setOwner(client);
            System.out.println(card.getId());
            cardRepository.save(card);
        }
    }

    public List<Card> getCardsByClientId(int clientId){
        Client client = clientService.getClientById(clientId);
        if (client != null){
            return cardRepository.findAllByOwnerOrderById(client);
        }
        return null;
    }

    @Transactional
    public void proceedTransferring(Transaction transaction){
        Card sourceCard = transaction.getSource();
        Card destinationCard = transaction.getDestination();
        Optional<Card> cardFrom = cardRepository.findById(sourceCard.getId());
        Optional<Card> cardTo = cardRepository.findById(destinationCard.getId());
        if (cardFrom.isPresent() && cardTo.isPresent()){
            cardFrom.get().setBalance(cardFrom.get().getBalance() - transaction.getAmount());
            cardTo.get().setBalance(cardTo.get().getBalance() + transaction.getAmount());

            transaction.setTimestamp(new Date());
            transactionService.saveTransaction(transaction);
        }
    }

}
