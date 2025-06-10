import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { Transaction } from "@/lib/types";
import { createQueryString } from "@/lib/utils";

type TransactionResponse = {
  total: number;
  offset: number;
  limit: number;
  data: Transaction[];
};

type GetTransactionsParams = {
  offset: number;
  limit: number;
  sortBy?: string;
  sortOrder?: string;
  searchBy?: string;
};

const transactionUrl = "/app/transaction";

export const transactionApi = createApi({
  reducerPath: "transactionApi",
  baseQuery: authBaseQuery,
  tagTypes: ["Transaction"],
  endpoints: (builder) => ({
    addTransaction: builder.mutation<any, Transaction>({
      query: (data) => ({
        url: transactionUrl,
        method: "POST",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["Transaction"],
    }),
    deleteTransaction: builder.mutation<any, number>({
      query: (id) => ({
        url: `${transactionUrl}/${id}`,
        method: "DELETE",
        responseHandler: "text",
      }),
      invalidatesTags: ["Transaction"],
    }),
    editTransaction: builder.mutation<any, Transaction>({
      query: (data) => ({
        url: `${transactionUrl}/${data.id}`,
        method: "PATCH",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["Transaction"],
    }),
    getTransactions: builder.query<TransactionResponse, GetTransactionsParams>({
      query: ({ 
        offset, 
        limit, 
        sortBy = "transactionDate", 
        sortOrder = "desc",
        searchBy 
      }) => {
        // Convert sortOrder to uppercase to match your API format
        const orderBy = sortOrder.toUpperCase();
        
        // Build query parameters object
        const queryParamsObj: Record<string, any> = {
          offset,
          limit,
          sortColumn: sortBy,
          orderBy,
        };

        // Only add searchBy if it has a value
        if (searchBy && searchBy.trim() !== "") {
          queryParamsObj.searchBy = searchBy.trim();
        }
        
        const queryParams = createQueryString(queryParamsObj);
        
        return {
          url: `${transactionUrl}?${queryParams}`,
          method: "GET",
        };
      },
      providesTags: ["Transaction"],
    }),
  }),
});

export const {
  useAddTransactionMutation,
  useDeleteTransactionMutation,
  useEditTransactionMutation,
  useGetTransactionsQuery,
} = transactionApi;