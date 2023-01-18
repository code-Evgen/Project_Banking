package ru.tatarinov.banking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.services.CardService;
import ru.tatarinov.banking.services.TransactionService;

import java.sql.Time;
import java.util.Date;

@Controller
@RequestMapping(value = "/cards")
public class CardController {
    private final CardService cardService;
    private final TransactionService transactionService;

    @Autowired
    public CardController(CardService cardService, TransactionService transactionService) {
        this.cardService = cardService;
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("card",cardService.getCardById(id));
        model.addAttribute("client", cardService.getClientByCardId(id));
        return "/cards/show";
    }

    @GetMapping("/{id}/addMoney")
    public String addMoney(@PathVariable("id") int id, Model model){
        model.addAttribute("card", cardService.getCardById(id));
        return "/cards/addMoney";
    }

    @PatchMapping("/{id}")
    public String refill(@PathVariable("id") int id, @RequestParam("addingAmount") float amount){
        cardService.refill(id, amount);
        return ("redirect:/cards/" + id);
    }

    @PostMapping("/new/{clientId}")
    public String addCard(@PathVariable("clientId") int id, @ModelAttribute("card") Card card){
        System.out.println("qqq - " + card.getId());
        System.out.println("qqq - " + card.getBalance());
        cardService.createCard(id, card);
        return ("redirect:/clients/" + id);
    }

    @GetMapping("/{id}/history")
    public String showHistory(@PathVariable("id") int id, @RequestParam(value = "filter", required = false) String filter,
                                                          @RequestParam(value = "destinationId", required = false) Integer destinationId,
                                                          @RequestParam(value = "amount", required = false, defaultValue = "0") Float amount,
                                                          @RequestParam(value = "date", required = false) String date,
                              Model model){
        Card sourceCard = cardService.getCardById(id);

        if (filter != null) {
            switch (filter) {
                case "destination": {
                    Card destinationCard = cardService.getCardById(destinationId);
                    model.addAttribute("transactions", transactionService.getTransactionsByCardFilterByDestination(sourceCard, destinationCard));
                    break;
                }

                case "amountAfter": {
                    model.addAttribute("transactions", transactionService.getTransactionsByCardFilterByAmountAfter(sourceCard, amount));
                    break;
                }

                case "amountBefore": {
                    model.addAttribute("transactions", transactionService.getTransactionsByCardFilterByAmountBefore(sourceCard, amount));
                    break;
                }

                case "dateAfter": {
                    Date searchDate = new Date();
                    String[] dateMass = new String[3];
                    dateMass = date.split("-");
                    searchDate.setYear(Integer.parseInt(dateMass[0]) - 1900);
                    searchDate.setMonth(Integer.parseInt(dateMass[1]) - 1);
                    searchDate.setDate(Integer.parseInt(dateMass[2]));
                    searchDate.setHours(0);
                    searchDate.setMinutes(0);
                    searchDate.setSeconds(0);

                    model.addAttribute("transactions", transactionService.getTransactionsByCardFilterByTimestampAfter(sourceCard, searchDate));
                    break;
                }

                case "dateBefore": {
                    Date searchDate = new Date();
                    String[] dateMass = new String[3];
                    dateMass = date.split("-");
                    searchDate.setYear(Integer.parseInt(dateMass[0]) - 1900);
                    searchDate.setMonth(Integer.parseInt(dateMass[1]) - 1);
                    searchDate.setDate(Integer.parseInt(dateMass[2]));
                    searchDate.setHours(0);
                    searchDate.setMinutes(0);
                    searchDate.setSeconds(0);

                    model.addAttribute("transactions", transactionService.getTransactionsByCardFilterByTimestampBefore(sourceCard, searchDate));
                    break;
                }
            }
        }
        else{
            model.addAttribute("transactions", transactionService.getTransactionsByCard(sourceCard));
        }

        model.addAttribute("cardId", id);
        return "/cards/history";
    }
}
