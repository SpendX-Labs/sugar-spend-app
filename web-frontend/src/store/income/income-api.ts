import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { Income } from "@/lib/types";
import { createQueryString } from "@/lib/utils";

type IncomesResponse = {
  total: number;
  offset: number;
  limit: number;
  data: Income[];
};

const incomeUrl = "/app/income";

export const incomeApi = createApi({
  reducerPath: "incomeApi",
  baseQuery: authBaseQuery,
  tagTypes: ["Income"],
  endpoints: (builder) => ({
    addIncome: builder.mutation<any, Income>({
      query: (data) => ({
        url: incomeUrl,
        method: "POST",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["Income"],
    }),
    deleteIncome: builder.mutation<any, number>({
      query: (id) => ({
        url: `${incomeUrl}/${id}`,
        method: "DELETE",
        responseHandler: "text",
      }),
      invalidatesTags: ["Income"],
    }),
    editIncome: builder.mutation<any, Income>({
      query: (data) => ({
        url: `${incomeUrl}/${data.id}`,
        method: "PATCH",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["Income"],
    }),
    getIncomes: builder.query<IncomesResponse, { page: number; size: number }>({
      query: ({ page, size }) => ({
        url: `${incomeUrl}?${createQueryString({ page, size })}`,
        method: "GET",
      }),
      providesTags: ["Income"],
    }),
  }),
});

export const {
  useAddIncomeMutation,
  useDeleteIncomeMutation,
  useEditIncomeMutation,
  useGetIncomesQuery,
} = incomeApi;
