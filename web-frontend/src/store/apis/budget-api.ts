import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { ExpenseReport, NextMonthReport } from "@/lib/types";
import { createQueryString } from "@/lib/utils";

const budgetUrl = "/app/budget";

export const budgetApi = createApi({
  reducerPath: "budgetApi",
  baseQuery: authBaseQuery,
  tagTypes: ["Budget"],
  endpoints: (builder) => ({
    getExpenseReport: builder.query<
      ExpenseReport,
      { month: string; year: number }
    >({
      query: ({ month, year = 2024 }) => ({
        url: `${budgetUrl}/expense-report?${createQueryString({
          month,
          year,
        })}`,
        method: "GET",
      }),
      providesTags: ["Budget"],
    }),
    getNextMonthReport: builder.query<NextMonthReport, void>({
      query: () => ({
        url: `${budgetUrl}/next-month-report`,
        method: "GET",
      }),
      providesTags: ["Budget"],
    }),
  }),
});

export const { useGetExpenseReportQuery, useGetNextMonthReportQuery } =
  budgetApi;
