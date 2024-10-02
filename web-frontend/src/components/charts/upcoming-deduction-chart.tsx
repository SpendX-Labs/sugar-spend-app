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

const chartData = [
  { category: "cards", amount: 500, fill: "var(--color-cards)" },
  { category: "loans", amount: 1200, fill: "var(--color-loans)" },
  { category: "others", amount: 300, fill: "var(--color-others)" },
];

const chartConfig = {
  amount: {
    label: "Amount",
  },
  cards: {
    label: "Cards",
    color: "hsl(var(--chart-1))",
  },
  loans: {
    label: "Loans",
    color: "hsl(var(--chart-2))",
  },
  others: {
    label: "Others",
    color: "hsl(var(--chart-3))",
  },
} satisfies ChartConfig;

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

  if (isLoading) return <div>Loading...</div>;
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
