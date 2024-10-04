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
  cashFlowId: number | null;
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

export type RemainingAutoDebitDetail = {
  id: number;
  lender: string;
  last4Digit: string;
  budgetMonth: string;
  budgetYear: number;
  actualAmount: number;
  remainingAmount: number;
  updateDate: string;
  dueDate: string;
};

export type RemainingAutoDebit = {
  totalAmount: number;
  remainingAmount: number;
  details: RemainingAutoDebitDetail[];
};

export type TimeBasedSummary = {
  dataKey: string;
  manualAmount: number | null;
  cardAmount: number | null;
};

export type ExpenseReport = {
  totalExpense: number;
  autoDebitAmount: number;
  manualSpendAmount: number;
  cardSpendAmount: number;
  totalImcome: number;
  availableAmount: number;
  remainingAutoDebit: RemainingAutoDebit;
  timeBasedSummary: TimeBasedSummary[];
};

export type BudgetDetail = {
  id: number;
  lender: string;
  last4Digit: string;
  budgetMonth: string;
  budgetYear: number;
  actualAmount: number;
  remainingAmount: number;
  updateDate: string;
  dueDate: string;
};

export type NextMonthReport = {
  totalAmount: number;
  remainingAmount: number;
  details: BudgetDetail[];
};

export enum LoanType {
  REDUCING = "REDUCING",
  OTHER_TYPES = "OTHER_TYPES",
}

export type Loan = {
  id: number;
  creditCardId?: number | null;
  lenderId?: number;
  lenderName?: string;
  creditCardName?: string;
  last4Digit?: string;
  totalAmount: number;
  loanType: LoanType;
  interestRate: number;
  noCostEmi: boolean;
  tenure: number;
  remainingTenure: number;
  loanStartDate: string;
  emiDateOfMonth: number;
  principalAmount: number;
  interestAmount: number;
  updateLock: boolean;
  remainingAmount: number;
  remainingPrincipalAmount: number;
  remainingInterestAmount: number;
  emiAmount: number;
};
