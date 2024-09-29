import { NavItem } from "./types";

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
    title: "Expense",
    href: "/expense",
    icon: "expenses",
    label: "Expense",
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
