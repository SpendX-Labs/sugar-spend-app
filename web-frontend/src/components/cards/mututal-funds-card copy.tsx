"use client";

import { CURRENCY_RUPEE_SYMBOL } from "@/lib/constants";
import { useGetExpenseReportQuery } from "@/store/budget/budget-api";
import { Card, CardContent, CardHeader, CardTitle } from "../ui/card";

const MutualFundsCard: React.FC = () => {
  const { data, error, isLoading } = useGetExpenseReportQuery({
    year: 2024,
    month: "",
  });

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error Occured</div>;

  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium">Mututal Funds</CardTitle>
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
          <path d="M22 12h-4l-3 9L9 3l-3 9H2" />
        </svg>
      </CardHeader>
      <CardContent>
        <div className="text-2xl font-bold">{CURRENCY_RUPEE_SYMBOL} 573</div>
        <p className="text-xs text-muted-foreground">+201 since last hour</p>
      </CardContent>
    </Card>
  );
};

export default MutualFundsCard;
