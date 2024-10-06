"use client";

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart";
import { useAppSelector } from "@/hooks/use-app";
import { CURRENCY_RUPEE_SYMBOL, monthNumberToName } from "@/lib/constants";
import { useGetExpenseReportQuery } from "@/store/apis/budget-api";
import { selectMonth, selectYear } from "@/store/slices/month-year-slice";
import * as React from "react";
import { Label, Pie, PieChart } from "recharts";
import PieChartSkeleton from "../skeletons/pie-chart-skeleton";

export function UpcomingDeductionChart() {
  const month = useAppSelector(selectMonth);
  const year = useAppSelector(selectYear);
  const {
    data: expenseReport,
    error,
    isLoading,
  } = useGetExpenseReportQuery({
    year,
    month,
  });

  const chartData = expenseReport?.remainingAutoDebit.details.map(
    (remainingAutoDebit, index) => ({
      category: remainingAutoDebit.id.toString(),
      amount: remainingAutoDebit.actualAmount,
      fill: `hsl(var(--chart-${(index % 5) + 1}))`,
    })
  );

  const chartConfig =
    expenseReport?.remainingAutoDebit.details.reduce(
      (acc, obj) => {
        (acc as Record<string, { label: string }>)[obj.id.toString()] = {
          label: obj.lender + " " + CURRENCY_RUPEE_SYMBOL,
        };
        return acc;
      },
      {
        amount: {
          label: "Amount",
        },
      }
    ) ?? ({} satisfies ChartConfig);

  if (isLoading) return <PieChartSkeleton />;
  if (error) return <div>Error Occured</div>;

  return (
    <Card className="flex flex-col">
      <CardHeader className="items-center pb-0">
        <CardTitle>Upcoming Deductions</CardTitle>
        <CardDescription>
          This Month ({monthNumberToName[new Date().getMonth()]} 2024)
        </CardDescription>
      </CardHeader>
      <CardContent className="flex-1 pb-0">
        <ChartContainer
          config={chartConfig}
          className="mx-auto aspect-square max-h-[360px]"
        >
          <PieChart>
            <ChartTooltip
              cursor={false}
              content={<ChartTooltipContent hideLabel />}
            />
            <Pie
              data={chartData}
              dataKey="amount"
              nameKey="category"
              innerRadius={60}
              strokeWidth={5}
            >
              <Label
                content={({ viewBox }) => {
                  if (viewBox && "cx" in viewBox && "cy" in viewBox) {
                    return (
                      <text
                        x={viewBox.cx}
                        y={viewBox.cy}
                        textAnchor="middle"
                        dominantBaseline="middle"
                      >
                        <tspan
                          x={viewBox.cx}
                          y={viewBox.cy}
                          className="fill-foreground text-3xl font-bold"
                        >
                          {CURRENCY_RUPEE_SYMBOL}
                          {expenseReport?.remainingAutoDebit.totalAmount.toLocaleString()}
                        </tspan>
                        <tspan
                          x={viewBox.cx}
                          y={(viewBox.cy || 0) + 24}
                          className="fill-muted-foreground"
                        >
                          Total Deductions
                        </tspan>
                      </text>
                    );
                  }
                }}
              />
            </Pie>
          </PieChart>
        </ChartContainer>
      </CardContent>
    </Card>
  );
}
