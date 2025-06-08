import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { BankAccount } from "@/lib/types";
import { createQueryString } from "@/lib/utils";

type BankAccountRequestBody = {
  id?: number;
  bankName: string;
  accountType: string;
  last4Digit: string;
  debitCardLast4Digit: string;
};

export type BankAccountsResponse = {
  total: number;
  offset: number;
  limit: number;
  data: BankAccount[];
};

type GetBankAccountsParams = {
  offset: number;
  limit: number;
  sortBy?: string;
  sortOrder?: string;
  searchBy?: string;
};

const bankAccountUrl = "/app/bank-account";

export const bankAccountApi = createApi({
  reducerPath: "bankAccountApi",
  baseQuery: authBaseQuery,
  tagTypes: ["BankAccount"],
  endpoints: (builder) => ({
    addBankAccount: builder.mutation<any, BankAccountRequestBody>({
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
    editBankAccount: builder.mutation<any, BankAccountRequestBody>({
      query: (data) => ({
        url: `${bankAccountUrl}/${data.id}`,
        method: "PATCH",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["BankAccount"],
    }),
    getBankAccounts: builder.query<BankAccountsResponse, GetBankAccountsParams>({
      query: ({ 
        offset, 
        limit, 
        searchBy 
      }) => {
        
        // Build query parameters object
        const queryParamsObj: Record<string, any> = {
          offset,
          limit,
        };

        // Only add searchBy if it has a value
        if (searchBy && searchBy.trim() !== "") {
          queryParamsObj.searchBy = searchBy.trim();
        }
        
        const queryParams = createQueryString(queryParamsObj);
        
        return {
          url: `${bankAccountUrl}?${queryParams}`,
          method: "GET",
        };
      },
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