"use client";
import { Button } from "@/components/ui/button";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { cn } from "@/lib/utils";
import { CalendarIcon } from "@radix-ui/react-icons";
import * as React from "react";

const months = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

export function MonthYearPicker({
  className,
}: React.HTMLAttributes<HTMLDivElement>) {
  const [selectedMonth, setSelectedMonth] = React.useState<number | undefined>(
    undefined
  );
  const [selectedYear, setSelectedYear] = React.useState<number | undefined>(
    new Date().getFullYear()
  );

  const handleMonthChange = (month: number | undefined) => {
    setSelectedMonth(month);
  };

  const handleYearChange = (year: number) => {
    setSelectedYear(year);
    // If year is selected and month is not, set selectedMonth to undefined
    if (!selectedMonth) {
      setSelectedMonth(undefined);
    }
  };

  return (
    <div className={cn("grid gap-2", className)}>
      <Popover>
        <PopoverTrigger asChild>
          <Button
            id="month-year"
            variant={"outline"}
            className={cn(
              "w-[160px] justify-start text-left font-normal",
              !selectedYear && "text-muted-foreground"
            )}
          >
            <CalendarIcon className="mr-2 h-4 w-4" />
            {selectedYear ? (
              selectedMonth !== undefined ? (
                `${months[selectedMonth]} ${selectedYear}`
              ) : (
                `${selectedYear}` // Display only year if month is not selected
              )
            ) : (
              <span>Select Month & Year</span>
            )}
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-auto p-0" align="end">
          <div className="flex flex-col gap-2 p-4">
            <div>
              <label className="block text-sm font-medium">Select Year:</label>
              <select
                value={selectedYear}
                onChange={(e) => handleYearChange(Number(e.target.value))}
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500"
              >
                {[...Array(11)].map((_, index) => {
                  const year = new Date().getFullYear() + index; // Next 10 years
                  return (
                    <option key={year} value={year}>
                      {year}
                    </option>
                  );
                })}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium">Select Month:</label>
              <select
                value={selectedMonth !== undefined ? selectedMonth : ""}
                onChange={(e) =>
                  handleMonthChange(
                    e.target.value ? Number(e.target.value) : undefined
                  )
                }
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500"
              >
                <option value="">Select Month</option>
                {months.map((month, index) => (
                  <option key={index} value={index}>
                    {month}
                  </option>
                ))}
              </select>
            </div>
          </div>
        </PopoverContent>
      </Popover>
    </div>
  );
}
