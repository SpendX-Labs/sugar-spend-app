import { NavItem } from "./types";

export const COOKIES_TOKEN_NAME = "auth_token";

export const navItems: NavItem[] = [
  {
    title: "Budget",
    href: "/",
    icon: "dashboard",
    label: "Budget",
  },
  {
    title: "Mutual Funds",
    href: "/mutual-fund",
    icon: "mutualFunds",
    label: "Mutual Funds",
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
    title: "All Mutual Funds",
    href: "/all-mutual-fund",
    icon: "allMutualFunds",
    label: "All Mutual Funds",
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
