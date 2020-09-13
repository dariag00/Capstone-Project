package com.kloso.capstoneproject;

import android.util.Log;

import com.kloso.capstoneproject.data.model.Expense;
import com.kloso.capstoneproject.data.model.Participant;
import com.kloso.capstoneproject.data.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BalanceCalculator {

    private static final String TAG = BalanceCalculator.class.getName();

    private static List<Participant> copyParticipants;


    private static void calculateNetBalancesOfParticipants(List<Expense> expenses, List<Participant> participants){
        int processedExpenses = 0;
        for(Expense expense : expenses){
            processedExpenses++;
            if(!expense.isProcessed()) {
                Participant payerParticipant = participants.get(participants.indexOf(expense.getPaidBy()));
                payerParticipant.addBalance(new BigDecimal(expense.getAmount()));

                System.out.println("Payer Balance: " + payerParticipant.getNetBalance());

                for (Participant owingParticipant : expense.getPaidFor()) {
                    Participant owingP = participants.get(participants.indexOf(owingParticipant));
                    owingP.addBalance(new BigDecimal(expense.getAmount()).negate().divide(new BigDecimal(String.valueOf(expense.getPaidFor().size())), 2, RoundingMode.HALF_EVEN));
                }
                expense.setProcessed(true);
            }
        }

        for(Participant participant : participants){
            System.out.println(participant.getName() + " balance: " + participant.getNetBalance());
        }

        Log.i(TAG, "Processed " + processedExpenses + " items.");

    }

    public static List<Transaction> calculateBalances(List<Expense> expenses, List<Participant> participants){

        List<Transaction> transactionList = new ArrayList<>();

        calculateNetBalancesOfParticipants(expenses, participants);

        copyParticipants = new ArrayList<>(participants);


        while(copyParticipants.size() > 1){

            //First we sort the list
            orderParticipantsByNetBalance(copyParticipants);

            //Then we get the two participants with the hightest and lowest net balance and create a transaction between them.
            Participant mostDebtorParticipant = copyParticipants.get(copyParticipants.size() - 1);
            Participant mostOwedParticipant = copyParticipants.get(0);

            Transaction transaction = new Transaction();
            transaction.setPayer(mostDebtorParticipant);
            transaction.setReceiver(mostOwedParticipant);

            BigDecimal amountToPay = new BigDecimal(mostDebtorParticipant.getNetBalance());
            BigDecimal amountToReceive = new BigDecimal(mostOwedParticipant.getNetBalance());

            int compareValue = amountToPay.compareTo(amountToReceive);

            //After that, we remove the ones whose balance ended up being 0 and adjust their balances if necessary
            if(compareValue == 1){
                copyParticipants.remove(0);
                transaction.setBalance(amountToReceive);
                mostDebtorParticipant.setNetBalance(amountToPay.add(amountToReceive).toString());
            } else if (compareValue == -1){
                copyParticipants.remove(copyParticipants.size() - 1);
                transaction.setBalance(amountToPay);
                mostOwedParticipant.setNetBalance(amountToReceive.subtract(amountToPay).toString());
            } else {
                //Quantities are the same
                copyParticipants.remove(0);
                copyParticipants.remove(copyParticipants.size() - 1);
                transaction.setBalance(amountToPay);
            }

            transactionList.add(transaction);

        }

        return transactionList;
    }

    private static void orderParticipantsByNetBalance(List<Participant> participants){
        Collections.sort(participants, Participant::compareTo);
    }

}
