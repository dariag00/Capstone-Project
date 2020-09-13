package com.kloso.capstoneproject;

import com.kloso.capstoneproject.data.model.Expense;
import com.kloso.capstoneproject.data.model.Participant;
import com.kloso.capstoneproject.data.model.Transaction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class BalanceCalculatorTest {

    @Before
    public void setUp(){

    }


    private Object[] calculateBalancesParameters(){
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant("Paco"));
        participants.add(new Participant("Manuel"));
        participants.add(new Participant("Maria"));

        List<Expense> expenses = new ArrayList<>();

        Expense expense = new Expense();
        expense.setAmount(new BigDecimal("200").toString());
        expense.setPaidBy(participants.get(0));
        expense.setPaidFor(participants);
        expenses.add(expense);

        expense = new Expense();
        expense.setAmount(new BigDecimal("150").toString());
        expense.setPaidBy(participants.get(1));
        expense.setPaidFor(participants);
        expenses.add(expense);

        return new Object[]{ expenses, participants};
    }

    @Test
    @Parameters(method = "calculateBalancesParameters")
    public void testCalculateBalances(List<Expense> expenseList, List<Participant> participantList){
        List<Transaction> transactions = BalanceCalculator.calculateBalances(expenseList, participantList);

        Assert.assertEquals(2, transactions.size());
    }

}
