"use client";

import { CURRENCY_RUPEE_SYMBOL } from "@/lib/constants";
import { useGetNextMonthReportQuery } from "@/store/apis/budget-api";
import { Card, CardContent, CardHeader, CardTitle } from "../ui/card";
import SmartCardSkeleton from "../skeletons/smart-card-skeleton";
import { BadgeIndianRupee } from "lucide-react";

const NextMonthDeductionsCard: React.FC = () => {
  const { data, error, isLoading } = useGetNextMonthReportQuery();

  if (isLoading) return <SmartCardSkeleton />;
  if (error) return <div>Error Occured</div>;

  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium">
          Next Month Auto Deductions
        </CardTitle>
        <BadgeIndianRupee className="h-4 w-4 text-muted-foreground" />
      </CardHeader>
      <CardContent>
        <div className="text-2xl font-bold">
          {CURRENCY_RUPEE_SYMBOL} {data?.totalAmount.toLocaleString()}
        </div>
        <p className="text-xs text-muted-foreground">+19% from this month</p>
      </CardContent>
    </Card>
  );
};

export default NextMonthDeductionsCard;
