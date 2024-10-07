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
import { CURRENCY_RUPEE_SYMBOL, monthNameToNumber } from "@/lib/constants";
import { generateMonthlyExpenses, generateYearlyExpenses } from "@/lib/utils";
import { useGetExpenseReportQuery } from "@/store/apis/budget-api";
import { selectMonth, selectYear } from "@/store/slices/month-year-slice";
import * as React from "react";
import { Bar, BarChart, CartesianGrid, XAxis } from "recharts";
import BarChartSkeleton from "../skeletons/bar-chart-skeleton";

const AUTO = "auto";
const DIRECT = "direct";
const TOTAL = "total";

const chartConfig = {
  views: {
    label: `Amount (${CURRENCY_RUPEE_SYMBOL})`,
  },
  [AUTO]: {
    label: "Credit Card Expense",
    color: "hsl(var(--chart-1))",
  },
  [DIRECT]: {
    label: "Direct Payments",
    color: "hsl(var(--chart-2))",
  },
  [TOTAL]: {
    label: "Total",
    color: "hsl(var(--chart-3))",
  },
} satisfies ChartConfig;

export function DailyExpenseChart() {
  const month = useAppSelector(selectMonth);
  const year = useAppSelector(selectYear);
  const {
    data: expenseRes,
    error,
    isLoading,
  } = useGetExpenseReportQuery({
    year,
    month,
  });

  const [activeChart, setActiveChart] =
    React.useState<keyof typeof chartConfig>(TOTAL);

  if (isLoading) return <BarChartSkeleton />;
  if (error) return <div>Error Occured</div>;

  const total = {
    [AUTO]: expenseRes?.cardSpendAmount || 0,
    [DIRECT]: expenseRes?.manualSpendAmount || 0,
  };

  return (
    <Card>
      <CardHeader className="flex flex-col items-stretch space-y-0 space-x-[1px] border-b p-0 sm:flex-row">
        <button
          data-active={activeChart === TOTAL}
          className="flex flex-1 flex-col justify-center gap-1 px-6 py-5 data-[active=false]:bg-muted/50 sm:py-6"
          onClick={() => setActiveChart(TOTAL)}
        >
          <CardTitle>
            {monthNameToNumber[month] ? "Daily" : "Monthly"} Expense
          </CardTitle>
          <CardDescription>
            Showing expenses per {monthNameToNumber[month] ? "day" : "month"}
          </CardDescription>
        </button>
        <div className="flex space-x-[1px]">
          {[AUTO, DIRECT].map((key) => {
            const chart = key as keyof typeof chartConfig;
            return (
              <button
                key={chart}
                data-active={activeChart === chart}
                className="text-nowrap relative flex flex-1 flex-col justify-center gap-1 border-t px-6 py-4 text-left even:border-l data-[active=false]:bg-muted/50 sm:border-l sm:border-t-0 sm:px-8 sm:py-6"
                onClick={() => setActiveChart(chart)}
              >
                <span className="text-xs text-muted-foreground">
                  {chartConfig[chart].label}
                </span>
                <span className="text-lg font-bold leading-none sm:text-xl">
                  {CURRENCY_RUPEE_SYMBOL}{" "}
                  {total[key as keyof typeof total].toLocaleString()}
                </span>
              </button>
            );
          })}
        </div>
      </CardHeader>
      {monthNameToNumber[month] ? (
        <CardContent className="px-2 sm:p-6">
          <ChartContainer
            config={chartConfig}
            className="aspect-auto h-[280px] w-full"
          >
            <BarChart
              accessibilityLayer
              data={generateMonthlyExpenses(
                month,
                year,
                expenseRes?.timeBasedSummary || []
              )}
              margin={{
                left: 12,
                right: 12,
              }}
            >
              <CartesianGrid vertical={false} />
              <XAxis
                dataKey="date"
                tickLine={false}
                axisLine={false}
                tickMargin={8}
                minTickGap={32}
                tickFormatter={(value) => {
                  const date = new Date(value);
                  return date.toLocaleDateString("en-US", {
                    month: "short",
                    day: "numeric",
                  });
                }}
              />
              <ChartTooltip
                content={
                  <ChartTooltipContent
                    className="w-[150px]"
                    nameKey="views"
                    labelFormatter={(value) => {
                      return new Date(value).toLocaleDateString("en-US", {
                        month: "short",
                        day: "numeric",
                        year: "numeric",
                      });
                    }}
                  />
                }
              />
              <Bar dataKey={activeChart} fill={`var(--color-${activeChart})`} />
            </BarChart>
          </ChartContainer>
        </CardContent>
      ) : (
        <CardContent className="px-2 sm:p-6">
          <ChartContainer
            config={chartConfig}
            className="aspect-auto h-[280px] w-full"
          >
            <BarChart
              accessibilityLayer
              data={generateYearlyExpenses(
                year,
                expenseRes?.timeBasedSummary || []
              )}
              margin={{
                left: 12,
                right: 12,
              }}
            >
              <CartesianGrid vertical={false} />
              <XAxis
                dataKey="month"
                tickLine={false}
                axisLine={false}
                tickMargin={8}
                minTickGap={32}
                tickFormatter={(value) => value.slice(0, 3)}
              />
              <ChartTooltip
                content={
                  <ChartTooltipContent className="w-[150px]" nameKey="views" />
                }
              />
              <Bar dataKey={activeChart} fill={`var(--color-${activeChart})`} />
            </BarChart>
          </ChartContainer>
        </CardContent>
      )}
    </Card>
  );
}
