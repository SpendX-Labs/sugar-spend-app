import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { CreditCard } from "@/lib/types";

type CreditCardRequestBody = {
  id?: number;
  bankName: string;
  creditCardName: string;
  statementDate: number;
  dueDate: number;
  last4Digit: string;
};

type CreditCardsResponse = {
  total: number;
  offset: number;
  limit: number;
  data: CreditCard[];
};

const creditCardUrl = "/app/credit-card";

export const creditCardApi = createApi({
  reducerPath: "creditCardApi",
  baseQuery: authBaseQuery,
  endpoints: (builder) => ({
    addCreditCard: builder.mutation<any, CreditCardRequestBody>({
      query: (data) => ({
        url: creditCardUrl,
        method: "POST",
        body: data,
        responseHandler: "text",
      }),
    }),
    editCreditCard: builder.mutation<any, CreditCardRequestBody>({
      query: (data) => ({
        url: `${creditCardUrl}/${data.id}`,
        method: "PATCH",
        body: data,
        responseHandler: "text",
      }),
    }),
    getCreditCards: builder.query<CreditCardsResponse, void>({
      query: () => ({
        url: creditCardUrl,
        method: "GET",
      }),
    }),
  }),
});

export const {
  useAddCreditCardMutation,
  useEditCreditCardMutation,
  useGetCreditCardsQuery,
} = creditCardApi;
