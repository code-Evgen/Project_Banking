package ru.tatarinov.banking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.tatarinov.banking.services.CardService;

@Controller
@RequestMapping(value = "/cards")
public class CardController {
    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("card",cardService.getCardById(id));
        return "/cards/show";
    }

    @GetMapping("{id}/addMoney")
    public String addMoney(@PathVariable("id") int id, Model model){
        model.addAttribute("card", cardService.getCardById(id));
        return "/cards/addMoney";
    }

    @PatchMapping("{id}")
    public String refill(@PathVariable("id") int id, @RequestParam("addingAmount") float amount){
        cardService.refill(id, amount);
        return ("redirect:/cards/" + id);
    }
}
