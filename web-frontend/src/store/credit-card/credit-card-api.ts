import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { CreditCard } from "@/lib/types";

export type CreditCardsResponse = {
  total: number;
  offset: number;
  limit: number;
  data: CreditCard[];
};

export const creditCardApi = createApi({
  reducerPath: "creditCardApi",
  baseQuery: authBaseQuery,
  endpoints: (builder) => ({
    getCreditCards: builder.query<CreditCardsResponse, void>({
      query: () => ({
        url: "/app/credit-card",
        method: "GET",
      }),
    }),
  }),
});

export const { useGetCreditCardsQuery } = creditCardApi;
