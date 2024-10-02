"use client";

import { Button } from "@/components/ui/button";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { useAppDispatch, useAppSelector } from "@/hooks/use-app";
import { cn } from "@/lib/utils";
import {
  selectMonth,
  selectYear,
  setMonth,
  setYear,
} from "@/store/slices/month-year-slice";
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
  const dispatch = useAppDispatch();
  const selectedMonth = useAppSelector(selectMonth);
  const selectedYear = useAppSelector(selectYear);

  const handleMonthChange = (month: string) => {
    dispatch(setMonth(month));
  };

  const handleYearChange = (year: number) => {
    dispatch(setYear(year));
    if (!selectedMonth) {
      dispatch(setMonth(""));
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
              selectedMonth ? (
                `${selectedMonth} ${selectedYear}`
              ) : (
                `${selectedYear}`
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
                  const year = new Date().getFullYear() + index;
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
                value={selectedMonth || ""}
                onChange={(e) => handleMonthChange(e.target.value || "")}
                className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring focus:ring-blue-500"
              >
                <option value="">Select Month</option>
                {months.map((month, index) => (
                  <option key={index} value={month}>
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
