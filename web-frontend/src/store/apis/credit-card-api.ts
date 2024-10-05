import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { CreditCard } from "@/lib/types";
import { createQueryString } from "@/lib/utils";

type CreditCardRequestBody = {
  id?: number;
  bankName: string;
  creditCardName: string;
  statementDate: number;
  dueDate: number;
  last4Digit: string;
};

export type CreditCardsResponse = {
  total: number;
  offset: number;
  limit: number;
  data: CreditCard[];
};

const creditCardUrl = "/app/credit-card";

export const creditCardApi = createApi({
  reducerPath: "creditCardApi",
  baseQuery: authBaseQuery,
  tagTypes: ["CreditCard"],
  endpoints: (builder) => ({
    addCreditCard: builder.mutation<any, CreditCardRequestBody>({
      query: (data) => ({
        url: creditCardUrl,
        method: "POST",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["CreditCard"],
    }),
    deleteCreditCard: builder.mutation<any, number>({
      query: (id) => ({
        url: `${creditCardUrl}/${id}`,
        method: "DELETE",
        responseHandler: "text",
      }),
      invalidatesTags: ["CreditCard"],
    }),
    editCreditCard: builder.mutation<any, CreditCardRequestBody>({
      query: (data) => ({
        url: `${creditCardUrl}/${data.id}`,
        method: "PATCH",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["CreditCard"],
    }),
    getCreditCards: builder.query<
      CreditCardsResponse,
      { offset: number; limit: number }
    >({
      query: ({ offset, limit }) => ({
        url: `${creditCardUrl}?${createQueryString({ offset, limit })}`,
        method: "GET",
      }),
      providesTags: ["CreditCard"],
    }),
  }),
});

export const {
  useAddCreditCardMutation,
  useDeleteCreditCardMutation,
  useEditCreditCardMutation,
  useGetCreditCardsQuery,
} = creditCardApi;
