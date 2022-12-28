package ru.tatarinov.banking.model;

import org.springframework.stereotype.Component;

@Component
public class Transfer {
    private int cardFrom;
    private int cardTo;
    private int amount;

    public Transfer() {
    }

    public Transfer(int cardFrom, int cardTo, int amount) {
        this.cardFrom = cardFrom;
        this.cardTo = cardTo;
        this.amount = amount;
    }

    public int getCardFrom() {
        return cardFrom;
    }

    public void setCardFrom(int cardFrom) {
        this.cardFrom = cardFrom;
    }

    public int getCardTo() {
        return cardTo;
    }

    public void setCardTo(int cardTo) {
        this.cardTo = cardTo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
