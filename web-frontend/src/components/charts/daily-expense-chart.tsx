"use client";

import * as React from "react";
import { Bar, BarChart, CartesianGrid, XAxis } from "recharts";

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
import { CURRENCY_RUPEE_SYMBOL } from "@/lib/constants";
import { currentMonthExpense } from "@/lib/data";

export const description = "An interactive bar chart";

const AUTO = "auto";
const DIRECT = "direct";
const TOTAL = "total";

const chartConfig = {
  views: {
    label: `Amount (${CURRENCY_RUPEE_SYMBOL})`,
  },
  [AUTO]: {
    label: "Auto Debits",
    color: "hsl(var(--chart-2))",
  },
  [DIRECT]: {
    label: "Direct Payments",
    color: "hsl(var(--chart-3))",
  },
  [TOTAL]: {
    label: "Total",
    color: "hsl(var(--chart-1))",
  },
} satisfies ChartConfig;

export function DailyExpenseChart() {
  const chartData = currentMonthExpense.map((data) => ({
    ...data,
    total: data.auto + data.direct,
  }));

  const [activeChart, setActiveChart] =
    React.useState<keyof typeof chartConfig>(TOTAL);

  console.log(chartData);
  console.log(activeChart);

  const total = React.useMemo(
    () => ({
      [AUTO]: chartData.reduce((acc, curr) => acc + curr.auto, 0),
      [DIRECT]: chartData.reduce((acc, curr) => acc + curr.direct, 0),
    }),
    []
  );

  return (
    <Card>
      <CardHeader className="flex flex-col items-stretch space-y-0 space-x-[1px] border-b p-0 sm:flex-row">
        <button
          data-active={activeChart === TOTAL}
          className="flex flex-1 flex-col justify-center gap-1 px-6 py-5 data-[active=false]:bg-muted/50 sm:py-6"
          onClick={() => setActiveChart(TOTAL)}
        >
          <CardTitle>Daily Expense</CardTitle>
          <CardDescription>Showing expenses per day</CardDescription>
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
      <CardContent className="px-2 sm:p-6">
        <ChartContainer
          config={chartConfig}
          className="aspect-auto h-[280px] w-full"
        >
          <BarChart
            accessibilityLayer
            data={chartData}
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
    </Card>
  );
}
