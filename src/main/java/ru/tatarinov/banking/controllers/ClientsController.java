package ru.tatarinov.banking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Transfer;
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

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("card") Card card){
        model.addAttribute("client", clientService.getClientById(id));
        model.addAttribute("cardsId", cardService.getCardIdByClientId(id));
        return ("clients/show");
    }

    @GetMapping("/{clientId}/transfer")
    public String transfer(@PathVariable("clientId") int clientId, Model model, @ModelAttribute("transfer") Transfer transfer){
        model.addAttribute("cards",cardService.getCardByClientId(clientId));
        model.addAttribute("clientId", clientId);
        //model.addAttribute("amount", 0);
        return "clients/transfer";
    }

    @PatchMapping("/{clientId}/transferConfirmation")
    public String transferConfirmation(@PathVariable("clientId") int clientId, @ModelAttribute("transfer") Transfer transfer, Model model){
        model.addAttribute("client", cardService.getClientByCardId(transfer.getCardTo()));
        return "clients/transferConfirmation";
    }

    @PatchMapping("/{clientId}/transferProceeding")
    public String transferProceeding(@PathVariable("clientId") int clientId, @ModelAttribute("transfer") Transfer transfer, Model model){
        model.addAttribute("clientId", clientId);
        cardService.proceedTransferring(transfer.getCardFrom(), transfer.getCardTo(), transfer.getAmount());
        return "clients/transferResult";
    }
}
