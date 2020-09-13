package com.kloso.capstoneproject;

import com.kloso.capstoneproject.data.model.Expense;
import com.kloso.capstoneproject.data.model.Participant;

import java.util.List;

public class BalanceCalculator {


    private static void calculateNetBalancesOfParticipants(List<Expense> expenses, List<Participant> participants){
        for(Expense expense : expenses){
            Participant payerParcitipant = participants.get(participants.indexOf(expense.getPaidBy()));
            payerParcitipant.addBalance(expense.getAmount());

            for(Participant owingParticipant : expense.getPaidFor()){
                Participant owingP = participants.get(participants.indexOf(owingParticipant));
                owingP.addBalance(- expense.getAmount() / expense.getPaidFor().size());
            }
        }

        for(Participant participant : participants){
            System.out.println(participant.getName() + " balance: " + participant.getNetBalance());
        }

    }

    public static void calculateBalances(List<Expense> expenses, List<Participant> participants){
        calculateNetBalancesOfParticipants(expenses, participants);
    }

}
