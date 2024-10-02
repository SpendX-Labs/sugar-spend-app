import { CreditCardsResponse } from "@/store/apis/credit-card-api";
import { CreditCard } from "./types";

export function simulateApi(simulatedData: CreditCardsResponse) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(simulatedData);
    }, 1000);
  });
}

const creditCards: CreditCard[] = [
  {
    id: 1,
    bankName: "FirstBank",
    creditCardName: "PlatinumCard",
    statementDate: 10,
    dueDate: 30,
    last4Digit: 1234,
  },
  {
    id: 2,
    bankName: "SecondBank",
    creditCardName: "GoldCard",
    statementDate: 15,
    dueDate: 5,
    last4Digit: 1234,
  },
  {
    id: 3,
    bankName: "ThirdBank",
    creditCardName: "SilverCard",
    statementDate: 20,
    dueDate: 10,
    last4Digit: 1234,
  },
  {
    id: 4,
    bankName: "TestBank",
    creditCardName: "RewardsCard",
    statementDate: 5,
    dueDate: 25,
    last4Digit: 1234,
  },
  {
    id: 5,
    bankName: "AnotherBank",
    creditCardName: "BusinessCard",
    statementDate: 18,
    dueDate: 8,
    last4Digit: 1234,
  },
  {
    id: 6,
    bankName: "ExampleBank",
    creditCardName: "CashbackCard",
    statementDate: 12,
    dueDate: 2,
    last4Digit: 1234,
  },
  {
    id: 7,
    bankName: "SampleBank",
    creditCardName: "TravelCard",
    statementDate: 25,
    dueDate: 15,
    last4Digit: 1234,
  },
  {
    id: 8,
    bankName: "TestBank",
    creditCardName: "PremiumCard",
    statementDate: 16,
    dueDate: 6,
    last4Digit: 1234,
  },
  {
    id: 9,
    bankName: "TrialBank",
    creditCardName: "BasicCard",
    statementDate: 11,
    dueDate: 1,
    last4Digit: 1234,
  },
  {
    id: 10,
    bankName: "MockBank",
    creditCardName: "StandardCard",
    statementDate: 22,
    dueDate: 12,
    last4Digit: 1234,
  },
  {
    id: 11,
    bankName: "TrialBank",
    creditCardName: "BasicCard",
    statementDate: 11,
    dueDate: 1,
    last4Digit: 1234,
  },
  {
    id: 12,
    bankName: "MockBank",
    creditCardName: "StandardCard",
    statementDate: 22,
    dueDate: 12,
    last4Digit: 1234,
  },
];

export const creditCardsResponse: CreditCardsResponse = {
  total: 100,
  offset: 0,
  limit: 10,
  data: creditCards.splice(0, 10),
};

const bankAccounts = [
  {
    id: 1,
    bankName: "Wells Fargo",
    accountType: "savings",
    last4Digit: "8342",
    debitCardLast4Digit: "9845",
  },
  {
    id: 2,
    bankName: "Chase Bank",
    accountType: "current",
    last4Digit: "4321",
    debitCardLast4Digit: "2309",
  },
  {
    id: 3,
    bankName: "Bank of America",
    accountType: "savings",
    last4Digit: "2345",
    debitCardLast4Digit: "5409",
  },
  {
    id: 4,
    bankName: "Citibank",
    accountType: "current",
    last4Digit: "1923",
    debitCardLast4Digit: "8473",
  },
  {
    id: 5,
    bankName: "Capital One",
    accountType: "savings",
    last4Digit: "3847",
    debitCardLast4Digit: "2938",
  },
];

export const bankAccountsResponse = {
  total: 100,
  offset: 0,
  limit: 10,
  data: bankAccounts,
};
