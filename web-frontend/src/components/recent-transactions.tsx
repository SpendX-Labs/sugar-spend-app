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
import { useGetTransactionsQuery } from "@/store/apis/transaction-api";
import { format } from "date-fns";
import { CashFlowType, TransactionType } from "@/lib/types";

export function RecentTransactions() {
  const {
    data: transactionRes,
    error: transactionError,
    isLoading: isTransactionLoading,
  } = useGetTransactionsQuery({
    offset: 0,
    limit: 5,
  });

  if (isTransactionLoading) return <div>Loading...</div>;
  if (transactionError) return <div>Error Occured</div>;

  const transactions = transactionRes?.data || [];
  const recentTransactionCount = transactionRes?.total 
  ? (transactionRes.total > 5 ? 5 : transactionRes.total)
  : 0;

  return (
    <Card className="h-full">
      <CardHeader>
        <CardTitle>Recent Transactions(Transaction)</CardTitle>
        <CardDescription>
          You made {recentTransactionCount} transactions this month.
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
                {transaction.note && transaction.note.trim() !== "" ? transaction.note : "Other"}
              </p>
              <p className="text-sm text-muted-foreground">
                {format(new Date(transaction.transactionDate), "dd-MM-yyyy")}
              </p>
            </div>
            <div
              className={`ml-auto font-medium ${
                transaction.cashFlowType === CashFlowType.CREDIT
                  ? "text-green-400"
                  : "text-red-400"
              }`}
            >
              {transaction.cashFlowType === CashFlowType.CREDIT ? "+" : "-"}
              {CURRENCY_RUPEE_SYMBOL}
              {transaction.amount}
            </div>
          </div>
        ))}
      </CardContent>
    </Card>
  );
}
