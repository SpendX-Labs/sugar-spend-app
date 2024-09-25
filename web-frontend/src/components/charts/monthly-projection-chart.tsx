"use client";

import { TrendingUp } from "lucide-react";
import { Line, LineChart, CartesianGrid, XAxis } from "recharts";

import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart";

const CURR_MONTH = "April";
const PREV_MONTH = "May";

const chartData = [
  { day: "1", may: 0, april: 0 },
  { day: "2", may: 15000, april: 0 },
  { day: "3", may: 0, april: 0 },
  { day: "4", may: 0, april: 0 },
  { day: "5", may: 0, april: 25000 },
  { day: "6", may: 0, april: 0 },
  { day: "7", may: 0, april: 0 },
  { day: "8", may: 0, april: 0 },
  { day: "9", may: 0, april: 0 },
  { day: "10", may: 0, april: 0 },
  { day: "11", may: 0, april: 0 },
  { day: "12", may: 0, april: 0 },
  { day: "13", may: 0, april: 0 },
  { day: "14", may: 0, april: 0 },
  { day: "15", may: 0, april: 12000 },
  { day: "16", may: 20000, april: 0 },
  { day: "17", may: 0, april: 0 },
  { day: "18", may: 0, april: 0 },
  { day: "19", may: 0, april: 0 },
  { day: "20", may: 0, april: 0 },
  { day: "21", may: 0, april: 0 },
  { day: "22", may: 0, april: 0 },
  { day: "23", may: 0, april: 0 },
  { day: "24", may: 0, april: 0 },
  { day: "25", may: 0, april: 0 },
  { day: "26", may: 0, april: 28000 },
  { day: "27", may: 0, april: 0 },
  { day: "28", may: 0, april: 0 },
  { day: "29", may: 0, april: 0 },
  { day: "30", may: 0, april: 0 },
  { day: "31", may: 0, april: null },
];

const chartConfig = {
  [PREV_MONTH.toLowerCase()]: {
    label: PREV_MONTH,
    color: "hsl(var(--chart-1))",
  },
  [CURR_MONTH.toLowerCase()]: {
    label: CURR_MONTH,
    color: "hsl(var(--chart-2))",
  },
} satisfies ChartConfig;

export function MonthlyProjectionChart() {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Projection from Last Month</CardTitle>
        <CardDescription>Comparing expenses from last month</CardDescription>
      </CardHeader>
      <CardContent>
        <ChartContainer
          config={chartConfig}
          className="aspect-auto h-[310px] w-full"
        >
          <LineChart
            accessibilityLayer
            data={chartData}
            margin={{
              left: 12,
              right: 12,
            }}
          >
            <CartesianGrid vertical={false} />
            <XAxis
              dataKey="day"
              tickLine={false}
              axisLine={false}
              tickMargin={8}
              tickFormatter={(value) => value.slice(0, 3)}
            />
            <ChartTooltip
              cursor={false}
              content={<ChartTooltipContent indicator="dot" />}
            />
            <Line
              dataKey={CURR_MONTH.toLowerCase()}
              type="monotone"
              fill={`var(--color-${CURR_MONTH.toLowerCase()})`}
              fillOpacity={0.4}
              stroke={`var(--color-${CURR_MONTH.toLowerCase()})`}
            />
            <Line
              dataKey={PREV_MONTH.toLowerCase()}
              type="monotone"
              fill={`var(--color-${PREV_MONTH.toLowerCase()})`}
              fillOpacity={0.4}
              stroke={`var(--color-${PREV_MONTH.toLowerCase()})`}
            />
          </LineChart>
        </ChartContainer>
      </CardContent>
      <CardFooter>
        <div className="flex w-full items-start gap-2 text-sm">
          <div className="grid gap-2">
            <div className="flex items-center gap-2 font-medium leading-none">
              +5.2% this month <TrendingUp className="h-4 w-4" />
            </div>
            <div className="flex items-center gap-2 leading-none text-muted-foreground">
              {PREV_MONTH} - {CURR_MONTH} 2024
            </div>
          </div>
        </div>
      </CardFooter>
    </Card>
  );
}
