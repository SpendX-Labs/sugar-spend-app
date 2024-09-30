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
  CASH = "CASH",
  BANK = "BANK",
  CREDITCARD = "CREDITCARD",
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

export enum BankAccountType {
  SAVINGS = "SAVINGS",
  CURRENT = "CURRENT",
}

export type BankAccount = {
  id?: number;
  bankName: string;
  accountType: BankAccountType;
  last4Digit: string;
  debitCardLast4Digit: string;
};

export type Income = {
  id?: number;
  incomeType: string;
  cashFlowDetails: CashFlowDetails;
  amount: number;
  dateOfEvent: string;
  timeOfEvent: string;
  message: string;
};
