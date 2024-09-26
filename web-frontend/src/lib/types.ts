import { Icons } from "@/components/icons";

export interface NavItem {
  title: string;
  href?: string;
  disabled?: boolean;
  external?: boolean;
  icon?: keyof typeof Icons;
  label?: string;
  description?: string;
}

export type CreditCard = {
  id: number;
  bankName: string;
  creditCardName: string;
  statementDate: number;
  dueDate: number;
  last4Digit: number;
};

export type Expense = {
  id?: number;
  creditCardId?: number;
  creditCardName: string;
  amount: number;
  expenseDate: string;
  expenseTime: string;
  reason: string;
};
