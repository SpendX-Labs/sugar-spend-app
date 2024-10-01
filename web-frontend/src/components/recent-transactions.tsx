"use client";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { CURRENCY_RUPEE_SYMBOL } from "@/lib/constants";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "./ui/card";
import { useGetExpensesQuery } from "@/store/expense/expense-api";
import { format } from "date-fns";
import { useGetIncomesQuery } from "@/store/income/income-api";

export function RecentTransactions() {
  const {
    data: expenseRes,
    error: expenseError,
    isLoading: isExpenseLoading,
  } = useGetExpensesQuery({
    page: 0,
    size: 10,
  });
  const {
    data: incomeRes,
    error: incomeError,
    isLoading: isIncomeLoading,
  } = useGetIncomesQuery({
    page: 0,
    size: 10,
  });

  if (isExpenseLoading || isIncomeLoading) return <div>Loading...</div>;
  if (expenseError || incomeError) return <div>Error Occured</div>;

  const expenses = expenseRes?.data || [];
  const incomes = incomeRes?.data || [];

  const totalTransactions = (expenseRes?.total || 0) + (incomeRes?.total || 0);
  const transactions = expenses
    .map((expense) => ({
      amount: expense.amount,
      cashFlowType: expense.expenseType,
      date: expense.expenseDate,
      time: expense.expenseTime,
      message: expense.reason,
      transactionType: "EXPENSE",
    }))
    .concat(
      incomes.map((income) => ({
        amount: income.amount,
        cashFlowType: income.incomeType,
        date: income.dateOfEvent,
        time: income.timeOfEvent,
        message: income.message,
        transactionType: "INCOME",
      }))
    )
    .sort((a, b) => {
      if (a.date == b.date ? a.time < b.time : a.date < b.date) return 1;
      return -1;
    });

  return (
    <Card className="h-full">
      <CardHeader>
        <CardTitle>Recent Transactions(Expense)</CardTitle>
        <CardDescription>
          You made {totalTransactions} transactions(expenses) this month.
        </CardDescription>
      </CardHeader>
      <CardContent className="space-y-8">
        {transactions.map((transaction, index) => (
          <div className="flex items-center" key={"transaction-" + index}>
            <Avatar className="h-9 w-9">
              {/* Todo: Add Cash flow pngs */}
              <AvatarImage src="/avatars/01.png" alt="Avatar" />
              <AvatarFallback>
                {transaction.cashFlowType.slice(0, 3)}
              </AvatarFallback>
            </Avatar>
            <div className="ml-4 space-y-1">
              <p className="text-sm font-medium leading-none">
                {transaction.message}
              </p>
              <p className="text-sm text-muted-foreground">
                {format(new Date(transaction.date), "dd-MM-yyyy")}
              </p>
            </div>
            <div
              className={`ml-auto font-medium ${
                transaction.transactionType === "INCOME"
                  ? "text-green-400"
                  : "text-red-400"
              }`}
            >
              {transaction.transactionType === "INCOME" ? "+" : "-"}
              {CURRENCY_RUPEE_SYMBOL}
              {transaction.amount}
            </div>
          </div>
        ))}
      </CardContent>
    </Card>
  );
}
