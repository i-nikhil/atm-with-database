package com.example.ATMStimulationSoftware;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<Transactions,Integer>
{
}
