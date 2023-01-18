package ru.tatarinov.banking.model;

import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "card")
public class Card {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "balance")
    private float balance;

    @ManyToOne
    @JoinColumn(name = "clientID", referencedColumnName = "id")
    private Client owner;

    @OneToMany(mappedBy = "source")
    private List<Transaction> transactionSource;

    @OneToMany(mappedBy = "destination")
    private List<Transaction> transactionDestination;

    public Card() {
    }

    public Card(int id, float balance) {
        this.id = id;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public List<Transaction> getTransactionSource() {
        return transactionSource;
    }

    public void setTransactionSource(List<Transaction> transactionSource) {
        this.transactionSource = transactionSource;
    }

    public List<Transaction> getTransactionDestination() {
        return transactionDestination;
    }

    public void setTransactionDestination(List<Transaction> transactionDestination) {
        this.transactionDestination = transactionDestination;
    }
}
