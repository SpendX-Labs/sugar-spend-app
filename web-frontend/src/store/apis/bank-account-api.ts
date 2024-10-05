import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { BankAccount } from "@/lib/types";
import { createQueryString } from "@/lib/utils";

type BankAccountRequestBody = Omit<BankAccount, "id"> & { id?: number };

type BankAccountsResponse = {
  total: number;
  offset: number;
  limit: number;
  data: BankAccount[];
};

const bankAccountUrl = "/app/bank-account";

export const bankAccountApi = createApi({
  reducerPath: "bankAccountApi",
  baseQuery: authBaseQuery,
  tagTypes: ["BankAccount"],
  endpoints: (builder) => ({
    addBankAccount: builder.mutation<any, BankAccount>({
      query: (data) => ({
        url: bankAccountUrl,
        method: "POST",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["BankAccount"],
    }),
    deleteBankAccount: builder.mutation<any, number>({
      query: (id) => ({
        url: `${bankAccountUrl}/${id}`,
        method: "DELETE",
        responseHandler: "text",
      }),
      invalidatesTags: ["BankAccount"],
    }),
    editBankAccount: builder.mutation<any, BankAccount>({
      query: (data) => ({
        url: `${bankAccountUrl}/${data.id}`,
        method: "PATCH",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["BankAccount"],
    }),
    getBankAccounts: builder.query<
      BankAccountsResponse,
      { offset: number; limit: number }
    >({
      query: ({ offset, limit }) => ({
        url: `${bankAccountUrl}?${createQueryString({ offset, limit })}`,
        method: "GET",
      }),
      providesTags: ["BankAccount"],
    }),
  }),
});

export const {
  useAddBankAccountMutation,
  useDeleteBankAccountMutation,
  useEditBankAccountMutation,
  useGetBankAccountsQuery,
} = bankAccountApi;
