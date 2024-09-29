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

export enum CashFlowType {
  CASH = "Cash",
  BANK = "Bank",
  CREDITCARD = "CreditCard",
}

export type CreditCard = {
  id: number;
  bankName: string;
  creditCardName: string;
  statementDate: number;
  dueDate: number;
  last4Digit: number;
};

export type CashFlowDetails = {
  cashFlowId: number;
  cashFlowName: string;
};

export type Expense = {
  id?: number;
  cashFlowDetails: CashFlowDetails;
  amount: number;
  expenseDate: string;
  expenseTime: string;
  expenseType: CashFlowType | string;
  reason?: string;
};
