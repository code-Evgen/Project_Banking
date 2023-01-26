package ru.tatarinov.banking.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "source", referencedColumnName = "id")
    private Card source;

    @ManyToOne
    @JoinColumn(name = "destination", referencedColumnName = "id")
    private Card destination;

    @Column(name = "amount")
//    @Min(value = 0, message = "Сумма должна быть больше 0")
    private float amount;

    @Column(name = "time")
    private Date timestamp;

    public Transaction() {
    }

    public Transaction(int id, float amount, Date timestamp) {
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Card getSource() {
        return source;
    }

    public void setSource(Card source) {
        this.source = source;
    }

    public Card getDestination() {
        return destination;
    }

    public void setDestination(Card destination) {
        this.destination = destination;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
