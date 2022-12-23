package ru.tatarinov.banking.services;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Client;
import ru.tatarinov.banking.repositories.CardRepository;
import ru.tatarinov.banking.repositories.ClientRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CardService {
    private final CardRepository cardRepository;
    private final ClientRepository clientRepository;

    public CardService(CardRepository cardRepository, ClientRepository clientRepository) {
        this.cardRepository = cardRepository;
        this.clientRepository = clientRepository;
    }

    public Card getCardById(int id){
        return cardRepository.findById(id).orElse(null);
    }

    public List<Integer> getCardIdByClientId(int id){
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()){
            Hibernate.initialize(client.get().getCards());
            List<Card> cards = client.get().getCards();
            List<Integer> cardsId = new ArrayList<>();
            for(Card card : cards){
                cardsId.add(card.getId());
            }
            return cardsId;
        }
        else
            return Collections.emptyList();
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
}
