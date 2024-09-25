import { CreditCardsResponse } from "@/store/credit-card/credit-card-api";
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
  },
  {
    id: 2,
    bankName: "SecondBank",
    creditCardName: "GoldCard",
    statementDate: 15,
    dueDate: 5,
  },
  {
    id: 3,
    bankName: "ThirdBank",
    creditCardName: "SilverCard",
    statementDate: 20,
    dueDate: 10,
  },
  {
    id: 4,
    bankName: "TestBank",
    creditCardName: "RewardsCard",
    statementDate: 5,
    dueDate: 25,
  },
  {
    id: 5,
    bankName: "AnotherBank",
    creditCardName: "BusinessCard",
    statementDate: 18,
    dueDate: 8,
  },
  {
    id: 6,
    bankName: "ExampleBank",
    creditCardName: "CashbackCard",
    statementDate: 12,
    dueDate: 2,
  },
  {
    id: 7,
    bankName: "SampleBank",
    creditCardName: "TravelCard",
    statementDate: 25,
    dueDate: 15,
  },
  {
    id: 8,
    bankName: "TestBank",
    creditCardName: "PremiumCard",
    statementDate: 16,
    dueDate: 6,
  },
  {
    id: 9,
    bankName: "TrialBank",
    creditCardName: "BasicCard",
    statementDate: 11,
    dueDate: 1,
  },
  {
    id: 10,
    bankName: "MockBank",
    creditCardName: "StandardCard",
    statementDate: 22,
    dueDate: 12,
  },
  {
    id: 11,
    bankName: "TrialBank",
    creditCardName: "BasicCard",
    statementDate: 11,
    dueDate: 1,
  },
  {
    id: 12,
    bankName: "MockBank",
    creditCardName: "StandardCard",
    statementDate: 22,
    dueDate: 12,
  },
];

export const creditCardsResponse: CreditCardsResponse = {
  total: 100,
  offset: 0,
  limit: 10,
  data: creditCards.splice(0, 10),
};