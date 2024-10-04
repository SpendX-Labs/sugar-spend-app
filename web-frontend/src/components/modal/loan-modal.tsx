"use client";
import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Loan } from "@/lib/types";
import { Label, Pie, PieChart } from "recharts";
import { ScrollArea } from "../ui/scroll-area";
import { Dialog, DialogContent, DialogFooter, DialogTitle } from "../ui/dialog";
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "../ui/chart";
import { CURRENCY_RUPEE_SYMBOL } from "@/lib/constants";
import { format } from "date-fns";

const chartConfig1 = {
  amount: {
    label: "Amount",
  },
  principal: {
    label: "Principal",
    color: "hsl(var(--chart-1))",
  },
  interest: {
    label: "Interest",
    color: "hsl(var(--chart-2))",
  },
} satisfies ChartConfig;

const chartConfig2 = {
  amount: {
    label: "Amount",
  },
  remaining: {
    label: "Remaining",
    color: "hsl(var(--chart-3))",
  },
  paid: {
    label: "Paid",
    color: "hsl(var(--chart-4))",
  },
} satisfies ChartConfig;

interface LoanModalProps {
  loan: Loan;
  isOpen: boolean;
  onClose: () => void;
}

export const LoanModal: React.FC<LoanModalProps> = ({
  loan,
  isOpen,
  onClose,
}) => {
  const {
    lenderName,
    creditCardName,
    last4Digit,
    totalAmount,
    loanType,
    interestRate,
    noCostEmi,
    tenure,
    loanStartDate,
    emiDateOfMonth,
    principalAmount,
    interestAmount,
    remainingPrincipalAmount,
    remainingInterestAmount,
    emiAmount,
  } = loan;
  const [isMounted, setIsMounted] = useState(false);

  const chartData1 = [
    {
      category: "principal",
      amount: principalAmount,
      fill: "var(--color-principal)",
    },
    {
      category: "interest",
      amount: interestAmount,
      fill: "var(--color-interest)",
    },
  ];

  const chartData2 = [
    {
      category: "remaining",
      amount: remainingPrincipalAmount,
      fill: "var(--color-remaining)",
    },
    {
      category: "paid",
      amount: remainingInterestAmount,
      fill: "var(--color-paid)",
    },
  ];

  const loanDetails = [
    {
      label: creditCardName ? "Credit Card Name" : "Lender Name",
      value: creditCardName ? creditCardName : lenderName,
    },
    { label: "Last 4 Digits", value: last4Digit || "N/A" },
    { label: "Loan Type", value: loanType },
    { label: "Interest Rate", value: `${interestRate}%` },
    { label: "No Cost EMI", value: noCostEmi ? "Yes" : "No" },
    { label: "Tenure (Months)", value: tenure },
    {
      label: "Loan Start Date",
      value: format(new Date(loanStartDate), "dd-MM-yyyy"),
    },
    { label: "EMI Date of Month", value: emiDateOfMonth },
    { label: "EMI Amount", value: `₹${emiAmount}` },
  ];
  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) {
    return null;
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-fit">
        <DialogTitle>Loan Details</DialogTitle>
        <ScrollArea>
          <div className="grid grid-cols-1 md:grid-cols-2">
            <div className="p-4 rounded-md">
              <ChartContainer
                config={chartConfig1}
                className="mx-auto aspect-square max-h-[250px]"
              >
                <PieChart>
                  <ChartTooltip
                    cursor={false}
                    content={<ChartTooltipContent hideLabel />}
                  />
                  <Pie
                    data={chartData1}
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
                                className="fill-foreground text-2xl font-bold"
                              >
                                {CURRENCY_RUPEE_SYMBOL}
                                {totalAmount.toLocaleString()}
                              </tspan>
                              <tspan
                                x={viewBox.cx}
                                y={(viewBox.cy || 0) + 24}
                                className="fill-muted-foreground"
                              >
                                Total Amount
                              </tspan>
                            </text>
                          );
                        }
                      }}
                    />
                  </Pie>
                </PieChart>
              </ChartContainer>
            </div>

            <div className="p-4 rounded-md">
              <ChartContainer
                config={chartConfig2}
                className="mx-auto aspect-square max-h-[250px]"
              >
                <PieChart>
                  <ChartTooltip
                    cursor={false}
                    content={<ChartTooltipContent hideLabel />}
                  />
                  <Pie
                    data={chartData2}
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
                                className="fill-foreground text-2xl font-bold"
                              >
                                {CURRENCY_RUPEE_SYMBOL}
                                {(
                                  remainingInterestAmount +
                                  remainingPrincipalAmount
                                ).toLocaleString()}
                              </tspan>
                              <tspan
                                x={viewBox.cx}
                                y={(viewBox.cy || 0) + 24}
                                className="fill-muted-foreground"
                              >
                                Remaining Amount
                              </tspan>
                            </text>
                          );
                        }
                      }}
                    />
                  </Pie>
                </PieChart>
              </ChartContainer>
            </div>
          </div>
          <div className="grid grid-cols-3 gap-4">
            {loanDetails.map(({ label, value }) => (
              <div className="ml-4 space-y-1">
                <p className="text-sm text-muted-foreground">{label}</p>
                <p className="text-sm font-medium leading-none">{value}</p>
              </div>
            ))}
          </div>
        </ScrollArea>
        <DialogFooter>
          <Button variant="secondary" onClick={onClose}>
            Close
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
