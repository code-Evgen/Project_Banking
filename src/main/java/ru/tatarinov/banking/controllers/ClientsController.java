package ru.tatarinov.banking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("client", clientService.getClientById(id));
        model.addAttribute("cardsId", cardService.getCardIdByClientId(id));
        return ("clients/show");
    }
}
