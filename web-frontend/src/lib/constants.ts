import { NavItem } from "./types";

export const CURRENCY_RUPEE_SYMBOL = "₹";

export const COOKIES_TOKEN_NAME = "auth_token";

export const navItems: NavItem[] = [
  {
    title: "Dashboard",
    href: "/",
    icon: "dashboard",
    label: "Dashboard",
  },
  {
    title: "Bank Account",
    href: "/bank-account",
    icon: "bankAccount",
    label: "BankAccount",
  },
  {
    title: "Credit Card",
    href: "/credit-card",
    icon: "creditCard",
    label: "CreditCard",
  },
  {
    title: "Transactions",
    href: "/transaction",
    icon: "expenses",
    label: "Transaction",
  },
  {
    title: "Loan",
    href: "/loan",
    icon: "loan",
    label: "Loan",
  },
  {
    title: "Mutual Fund",
    href: "/mutual-fund",
    icon: "mutualFunds",
    label: "Mutual Fund",
  },
  // {
  //   title: "Profile",
  //   href: "/profile",
  //   icon: "user",
  //   label: "profile",
  // },
  // {
  //   title: "Login",
  //   href: "/login",
  //   icon: "login",
  //   label: "login",
  // },
];

export const JANUARY = "January";
export const FEBRUARY = "February";
export const MARCH = "March";
export const APRIL = "April";
export const MAY = "May";
export const JUNE = "June";
export const JULY = "July";
export const AUGUST = "August";
export const SEPTEMBER = "September";
export const OCTOBER = "October";
export const NOVEMBER = "November";
export const DECEMBER = "December";

export const monthNumberToName: { [key: number]: string } = {
  1: JANUARY,
  2: FEBRUARY,
  3: MARCH,
  4: APRIL,
  5: MAY,
  6: JUNE,
  7: JULY,
  8: AUGUST,
  9: SEPTEMBER,
  10: OCTOBER,
  11: NOVEMBER,
  12: DECEMBER,
};

export const monthNameToNumber: { [key: string]: number } = {
  [JANUARY]: 1,
  [FEBRUARY]: 2,
  [MARCH]: 3,
  [APRIL]: 4,
  [MAY]: 5,
  [JUNE]: 6,
  [JULY]: 7,
  [AUGUST]: 8,
  [SEPTEMBER]: 9,
  [OCTOBER]: 10,
  [NOVEMBER]: 11,
  [DECEMBER]: 12,
};
