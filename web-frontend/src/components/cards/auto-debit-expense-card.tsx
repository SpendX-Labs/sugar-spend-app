"use client";

import { useAppSelector } from "@/hooks/use-app";
import { CURRENCY_RUPEE_SYMBOL } from "@/lib/constants";
import { useGetExpenseReportQuery } from "@/store/apis/budget-api";
import { selectMonth, selectYear } from "@/store/slices/month-year-slice";
import { Card, CardContent, CardHeader, CardTitle } from "../ui/card";
import SmartCardSkeleton from "../skeletons/smart-card-skeleton";

const AutoDebitExpenseCard: React.FC = () => {
  const month = useAppSelector(selectMonth);
  const year = useAppSelector(selectYear);
  const { data, error, isLoading } = useGetExpenseReportQuery({
    year,
    month,
  });

  if (isLoading) return <SmartCardSkeleton />;
  if (error) return <div>Error Occured</div>;

  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium">
          Auto Debit Expense
        </CardTitle>
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
          <rect width="20" height="14" x="2" y="5" rx="2" />
          <path d="M2 10h20" />
        </svg>
      </CardHeader>
      <CardContent>
        <div className="text-2xl font-bold">
          {CURRENCY_RUPEE_SYMBOL} {data?.autoDebitAmount.toLocaleString()}
        </div>
        <p className="text-xs text-muted-foreground">+20.1% from last month</p>
      </CardContent>
    </Card>
  );
};

export default AutoDebitExpenseCard;
