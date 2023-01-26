package ru.tatarinov.banking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tatarinov.banking.Security.ClientDetails;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Transaction;
import ru.tatarinov.banking.services.CardService;
import ru.tatarinov.banking.services.ClientService;
import ru.tatarinov.banking.util.TransactionValidator;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/clients")
public class ClientsController {
    private final ClientService clientService;
    private final CardService cardService;
    private final TransactionValidator transactionValidator;

    @Autowired
    public ClientsController(ClientService clientService, CardService cardService, TransactionValidator transactionValidator) {
        this.clientService = clientService;
        this.cardService = cardService;
        this.transactionValidator = transactionValidator;
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
    public String transferConfirmation(@PathVariable("clientId") int clientId, @ModelAttribute("transaction") @Valid Transaction transaction, BindingResult bindingResult, Model model) {
        transactionValidator.validate(transaction, bindingResult);
        if (bindingResult.hasErrors()){
            model.addAttribute("cards",cardService.getCardsByClientId(clientId));
            return ("clients/transaction");
        }
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
