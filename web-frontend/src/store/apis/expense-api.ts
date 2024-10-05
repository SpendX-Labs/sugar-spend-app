import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { Expense } from "@/lib/types";
import { createQueryString } from "@/lib/utils";

type ExpensesResponse = {
  total: number;
  offset: number;
  limit: number;
  data: Expense[];
};

const expenseUrl = "/app/expense";

export const expenseApi = createApi({
  reducerPath: "expenseApi",
  baseQuery: authBaseQuery,
  tagTypes: ["Expense"],
  endpoints: (builder) => ({
    addExpense: builder.mutation<any, Expense>({
      query: (data) => ({
        url: expenseUrl,
        method: "POST",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["Expense"],
    }),
    deleteExpense: builder.mutation<any, number>({
      query: (id) => ({
        url: `${expenseUrl}/${id}`,
        method: "DELETE",
        responseHandler: "text",
      }),
      invalidatesTags: ["Expense"],
    }),
    editExpense: builder.mutation<any, Expense>({
      query: (data) => ({
        url: `${expenseUrl}/${data.id}`,
        method: "PATCH",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["Expense"],
    }),
    getExpenses: builder.query<
      ExpensesResponse,
      { offset: number; limit: number }
    >({
      query: ({ offset, limit }) => ({
        url: `${expenseUrl}?${createQueryString({ offset, limit })}`,
        method: "GET",
      }),
      providesTags: ["Expense"],
    }),
  }),
});

export const {
  useAddExpenseMutation,
  useDeleteExpenseMutation,
  useEditExpenseMutation,
  useGetExpensesQuery,
} = expenseApi;
