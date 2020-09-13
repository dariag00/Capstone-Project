package com.kloso.capstoneproject;

import android.util.Log;

import com.kloso.capstoneproject.data.model.Expense;
import com.kloso.capstoneproject.data.model.Participant;

import java.util.List;

public class BalanceCalculator {

    private static final String TAG = BalanceCalculator.class.getName();


    private static void calculateNetBalancesOfParticipants(List<Expense> expenses, List<Participant> participants){
        int processedExpenses = 0;
        for(Expense expense : expenses){
            processedExpenses++;
            if(!expense.isProcessed()) {
                Participant payerParticipant = participants.get(participants.indexOf(expense.getPaidBy()));
                payerParticipant.addBalance(expense.getAmount());

                for (Participant owingParticipant : expense.getPaidFor()) {
                    Participant owingP = participants.get(participants.indexOf(owingParticipant));
                    owingP.addBalance(-expense.getAmount() / expense.getPaidFor().size());
                }
                expense.setProcessed(true);
            }
        }

        for(Participant participant : participants){
            System.out.println(participant.getName() + " balance: " + participant.getNetBalance());
        }

        Log.i(TAG, "Processed " + processedExpenses + " items.");

    }

    public static void calculateBalances(List<Expense> expenses, List<Participant> participants){
        calculateNetBalancesOfParticipants(expenses, participants);
    }

}
