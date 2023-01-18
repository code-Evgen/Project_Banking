package ru.tatarinov.banking.Security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Client;
import ru.tatarinov.banking.services.CardService;

import java.util.List;

@Component
public class WebSecurity {
    private final CardService cardService;

    public WebSecurity(CardService cardService) {
        this.cardService = cardService;
    }

    public boolean checkUserId(Authentication authentication, int id){
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
            int clientId = clientDetails.getClient().getId();
            if (clientId == id)
                return true;

        }
        return false;
    }

    public boolean checkCardId(Authentication authentication, int id){
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
            Client client = clientDetails.getClient();
            List<Card> cardList = cardService.getCardsByClientId(client.getId());
            if (cardList.stream().anyMatch((s) -> s.getId() == id))
                return true;
        }
        return false;
    }
}
