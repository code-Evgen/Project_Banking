package ru.tatarinov.banking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.tatarinov.banking.Security.ClientDetails;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Transaction;
import ru.tatarinov.banking.services.CardService;
import ru.tatarinov.banking.services.ClientService;

@Controller
@RequestMapping(value = "/clients")
public class ClientsController {
    private final ClientService clientService;
    private final CardService cardService;

    @Autowired
    public ClientsController(ClientService clientService, CardService cardService) {
        this.clientService = clientService;
        this.cardService = cardService;
    }

    @GetMapping("/default")
    public String defaultURL(Authentication authentication){
        ClientDetails clientDetails = (ClientDetails)authentication.getPrincipal();
        int id = clientDetails.getClient().getId();
        return  ("redirect:/clients/" + id);
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("card") Card card){
        model.addAttribute("client", clientService.getClientById(id));
        model.addAttribute("cardsId", cardService.getCardIdByClientId(id));
        return ("clients/show");
    }

    @GetMapping("/{clientId}/transaction")
    public String transfer(@PathVariable("clientId") int clientId, Model model, @ModelAttribute("transaction") Transaction transaction){
        model.addAttribute("cards",cardService.getCardsByClientId(clientId));
        model.addAttribute("clientId", clientId);
        //model.addAttribute("amount", 0);
        return "clients/transaction";
    }

    @PatchMapping("/{clientId}/transactionConfirmation")
    public String transferConfirmation(@PathVariable("clientId") int clientId, @ModelAttribute("transaction") Transaction transaction, Model model){
        model.addAttribute("client", cardService.getClientByCardId(transaction.getDestination().getId()));
        return "clients/transactionConfirmation";
    }

    @PatchMapping("/{clientId}/transactionProceeding")
    public String transferProceeding(@PathVariable("clientId") int clientId, @ModelAttribute("transaction") Transaction transaction, Model model){
        model.addAttribute("clientId", clientId);
        cardService.proceedTransferring(transaction);
        return "clients/transactionResult";
    }
}
