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

        copyParticipants = copyParticipantsList(participants);

        int iteration = 0;
        while(copyParticipants.size() > 1){

            Log.i(TAG, "calculateBalances: Iteration nº" + iteration);

            //First we sort the list
            orderParticipantsByNetBalance(copyParticipants);

            Log.i(TAG, "Copy Participants: [");
            for(Participant participant : copyParticipants){
                Log.i(TAG, participant.getName() + ":" + participant.getNetBalance());
            }
            Log.i(TAG, "]");

            //Then we get the two participants with the hightest and lowest net balance and create a transaction between them.
            Participant mostDebtorParticipant = copyParticipants.get(0);
            Participant mostOwedParticipant = copyParticipants.get(copyParticipants.size() - 1);

            Log.i(TAG, "Debtor: " + mostDebtorParticipant.getName() + ":" + mostDebtorParticipant.getNetBalance()
                    +  " Owed: " + mostOwedParticipant.getName() + ":" + mostOwedParticipant.getNetBalance());

            Transaction transaction = new Transaction();
            transaction.setPayer(mostDebtorParticipant);
            transaction.setReceiver(mostOwedParticipant);

            BigDecimal amountToPay = new BigDecimal(mostDebtorParticipant.getNetBalance());
            BigDecimal amountToReceive = new BigDecimal(mostOwedParticipant.getNetBalance());

            Log.i(TAG, "calculateBalances: Amount To Pay: " + amountToPay.toString() + ", Amount to Receive: " + amountToReceive.toString());

            int compareValue = amountToPay.abs().compareTo(amountToReceive.abs() );

            //After that, we remove the ones whose balance ended up being 0 and adjust their balances if necessary
            if(compareValue == 1){
                Log.i(TAG, "Amount to Pay is bigger. Setting amount to receive as the balance and adding it to the amount to pay");
                copyParticipants.remove(mostOwedParticipant);
                transaction.setBalance(amountToReceive.toString());
                mostDebtorParticipant.setNetBalance(amountToPay.add(amountToReceive).toString());
            } else if (compareValue == 1){
                Log.i(TAG, "Amount to Receive is bigger. Setting amount to pay as the balance and substracting it from the amount to receive");
                copyParticipants.remove(mostDebtorParticipant);
                transaction.setBalance(amountToPay.toString());
                mostOwedParticipant.setNetBalance(amountToReceive.subtract(amountToPay).toString());
            } else {
                //Quantities are the same
                copyParticipants.remove(mostOwedParticipant);
                copyParticipants.remove(mostDebtorParticipant);
                transaction.setBalance(amountToPay.toString());
            }

            Log.i(TAG, "Updated Participants: [");
            for(Participant participant : copyParticipants){
                Log.i(TAG, participant.getName() + ":" + participant.getNetBalance());
            }
            Log.i(TAG, "]");

            transactionList.add(transaction);

        }

        return transactionList;
    }

    private static void orderParticipantsByNetBalance(List<Participant> participants){
        Collections.sort(participants, Participant::compareTo);
    }

    private static List<Participant> copyParticipantsList(List<Participant> participants){
        List<Participant> copy = new ArrayList<>(participants.size());
        for(Participant participant : participants){
            if(!participant.getNetBalance().equals("0"))
                copy.add(new Participant(participant));
        }
        return copy;
    }

}
