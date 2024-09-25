import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { Expense } from "@/lib/types";
import { createQueryString } from "@/lib/utils";

type ExpenseRequestBody = {
  id?: number;
  bankName: string;
  expenseName: string;
  statementDate: number;
  dueDate: number;
  last4Digit: string;
};

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
    addExpense: builder.mutation<any, ExpenseRequestBody>({
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
    editExpense: builder.mutation<any, ExpenseRequestBody>({
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
      { page: number; size: number }
    >({
      query: ({ page, size }) => ({
        url: `${expenseUrl}?${createQueryString({ page, size })}`,
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
