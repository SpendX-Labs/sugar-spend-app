import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

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
