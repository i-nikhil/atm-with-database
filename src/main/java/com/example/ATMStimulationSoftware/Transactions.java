package com.example.ATMStimulationSoftware;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transactions
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int t_id;

    @Column
    int sender;

    @Column
    int receiver;

    @Column
    double amount;

    @Column
    Date date;

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
