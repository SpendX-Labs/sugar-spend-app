"use client";

import { useAppSelector } from "@/hooks/use-app";
import { CURRENCY_RUPEE_SYMBOL } from "@/lib/constants";
import { useGetExpenseReportQuery } from "@/store/apis/budget-api";
import { selectMonth, selectYear } from "@/store/slices/month-year-slice";
import { Card, CardContent, CardHeader, CardTitle } from "../ui/card";

const AvailableBalanceCard: React.FC = () => {
  const month = useAppSelector(selectMonth);
  const year = useAppSelector(selectYear);
  const { data, error, isLoading } = useGetExpenseReportQuery({
    year,
    month,
  });

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error Occured</div>;

  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium">Available Balance</CardTitle>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeLinecap="round"
          strokeLinejoin="round"
          strokeWidth="2"
          className="h-4 w-4 text-muted-foreground"
        >
          <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />
          <circle cx="9" cy="7" r="4" />
          <path d="M22 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75" />
        </svg>
      </CardHeader>
      <CardContent>
        <div className="text-2xl font-bold">
          {CURRENCY_RUPEE_SYMBOL} {data?.availableAmount.toLocaleString()}
        </div>
        <p className="text-xs text-muted-foreground">+180.1% from last month</p>
      </CardContent>
    </Card>
  );
};

export default AvailableBalanceCard;
