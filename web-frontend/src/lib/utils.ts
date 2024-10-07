import { clsx, type ClassValue } from "clsx";
import { format } from "date-fns";
import { twMerge } from "tailwind-merge";
import { monthNameToNumber, monthNumberToName } from "./constants";
import { BankAccount, CreditCard } from "./types";
import { z } from "zod";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

type Quote = {
  quote: string;
  author: string;
};

const quotes: Quote[] = [
  {
    quote:
      "Money, like emotions, is something you must control to keep your life on the right track.",
    author: "Natasha Munson",
  },
  {
    quote:
      "It’s not your salary that makes you rich, it’s your spending habits.",
    author: "Charles A. Jaffe",
  },
  {
    quote: "Beware of little expenses; a small leak will sink a great ship.",
    author: "Benjamin Franklin",
  },
  {
    quote:
      "Tracking your expenses is like taking control of your financial destiny.",
    author: "Chris Hogan",
  },
  {
    quote:
      "It is not how much we have, but how much we enjoy, that makes happiness.",
    author: "Charles Spurgeon",
  },
  {
    quote: "The habit of managing money is more important than the amount.",
    author: "T. Harv Eker",
  },
  {
    quote:
      "Do not save what is left after spending, but spend what is left after saving.",
    author: "Warren Buffett",
  },
  {
    quote:
      "Too many people spend money they haven’t earned, to buy things they don’t want, to impress people they don’t like.",
    author: "Will Rogers",
  },
];

export const getRandomQuote = (): Quote =>
  quotes[Math.floor(Math.random() * quotes.length)];

export const createQueryString = (params: Record<string, any>): string => {
  const queryString = new URLSearchParams();

  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null) {
      queryString.append(key, String(value));
    }
  });

  return queryString.toString();
};

export const generateMonthlyExpenses = (
  month: string,
  year: number,
  inputData: any[]
) => {
  const monthNumber = monthNameToNumber[month];

  if (!monthNumber) return [];

  const daysInMonth = new Date(year, monthNumber, 0).getDate();
  const expenseMap = new Map<
    string,
    { manualAmount: number; cardAmount: number }
  >();

  inputData.forEach((item) => {
    if (!new Date(item.dataKey).valueOf()) return;
    const formattedDate = format(new Date(item.dataKey), "yyyy-MM-dd");
    expenseMap.set(formattedDate, {
      manualAmount: item.manualAmount,
      cardAmount: item.cardAmount,
    });
  });

  const result = [];
  for (let day = 1; day <= daysInMonth; day++) {
    const date = `${year}-${String(monthNumber).padStart(2, "0")}-${String(
      day
    ).padStart(2, "0")}`;
    const expense = expenseMap.get(date) || {
      manualAmount: 0,
      cardAmount: 0,
    };

    result.push({
      date,
      auto: expense.cardAmount,
      direct: expense.manualAmount,
      total: expense.cardAmount + expense.manualAmount,
    });
  }

  return result;
};

export const generateYearlyExpenses = (year: number, inputData: any[]) => {
  const yearlyData: {
    [key: number]: { manualAmount: number; cardAmount: number };
  } = {};

  for (let m = 1; m <= 12; m++) {
    yearlyData[m] = { manualAmount: 0, cardAmount: 0 };
  }

  inputData.forEach((item) => {
    const monthNumber = monthNameToNumber[item.dataKey];
    if (!monthNumber) return;
    yearlyData[monthNumber].manualAmount += item.manualAmount || 0;
    yearlyData[monthNumber].cardAmount += item.cardAmount || 0;
  });

  const result = [];
  for (let m = 1; m <= 12; m++) {
    result.push({
      month: monthNumberToName[m],
      auto: yearlyData[m].cardAmount,
      direct: yearlyData[m].manualAmount,
      total: yearlyData[m].cardAmount + yearlyData[m].manualAmount,
    });
  }

  return result;
};

export const mergeBankAccountDetails = (bankAccount: BankAccount) => {
  return bankAccount.bankName + " (XXXX " + bankAccount.last4Digit + ")";
};

export const mergeCreditCardDetails = (creditCard: CreditCard) => {
  return creditCard.bankName + " (XXXX " + creditCard.last4Digit + ")";
};

export const convertMonthsToYearsMonths = (totalMonths: number) => {
  const years = Math.floor(totalMonths / 12);
  const months = totalMonths % 12;

  return `${years} year(s) ${months} month(s)`;
};

const MIN_PASSWORD_LENGTH = 8;

export const passwordSchema = z
  .string()
  .min(
    MIN_PASSWORD_LENGTH,
    `Password must be at least ${MIN_PASSWORD_LENGTH} characters long.`
  )
  .regex(/[A-Z]/, "Password must contain at least one uppercase letter.")
  .regex(/[a-z]/, "Password must contain at least one lowercase letter.")
  .regex(/[0-9]/, "Password must contain at least one digit.")
  .regex(
    /[@#$%^&+=]/,
    "Password must contain at least one special character (@#$%^&+=)."
  );
