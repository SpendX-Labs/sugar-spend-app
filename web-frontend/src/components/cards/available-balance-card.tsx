"use client";

import { useAppSelector } from "@/hooks/use-app";
import { CURRENCY_RUPEE_SYMBOL } from "@/lib/constants";
import { useGetExpenseReportQuery } from "@/store/apis/budget-api";
import { selectMonth, selectYear } from "@/store/slices/month-year-slice";
import { Card, CardContent, CardHeader, CardTitle } from "../ui/card";
import SmartCardSkeleton from "../skeletons/smart-card-skeleton";
import { IndianRupee } from "lucide-react";

const AvailableBalanceCard: React.FC = () => {
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
        <CardTitle className="text-sm font-medium">Available Balance</CardTitle>
        <IndianRupee className="h-4 w-4 text-muted-foreground" />
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
