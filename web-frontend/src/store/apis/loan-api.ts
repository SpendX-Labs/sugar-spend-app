import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { Loan, LoanType } from "@/lib/types";
import { createQueryString } from "@/lib/utils";

type LoansResponse = {
  total: number;
  offset: number;
  limit: number;
  data: Loan[];
};

type LoanPayload = {
  id?: number;
  creditCardId?: number | null;
  creditCardName?: string;
  lenderName?: string;
  last4Digit?: string;
  noCostEmi: boolean;
  loanType: LoanType;
  totalAmount: number;
  interestRate: number;
  tenure: number;
  loanStartDate: string;
  emiDateOfMonth: number;
};

const loanUrl = "/app/loan";

export const loanApi = createApi({
  reducerPath: "loanApi",
  baseQuery: authBaseQuery,
  tagTypes: ["Loan"],
  endpoints: (builder) => ({
    addLoan: builder.mutation<any, LoanPayload>({
      query: (data) => ({
        url: loanUrl,
        method: "POST",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["Loan"],
    }),
    deleteLoan: builder.mutation<any, number>({
      query: (id) => ({
        url: `${loanUrl}/${id}`,
        method: "DELETE",
        responseHandler: "text",
      }),
      invalidatesTags: ["Loan"],
    }),
    editLoan: builder.mutation<any, LoanPayload>({
      query: (data) => ({
        url: `${loanUrl}/${data.id}`,
        method: "PATCH",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["Loan"],
    }),
    getLoans: builder.query<LoansResponse, { offset: number; limit: number }>({
      query: ({ offset, limit }) => ({
        url: `${loanUrl}?${createQueryString({ offset, limit })}`,
        method: "GET",
      }),
      providesTags: ["Loan"],
    }),
  }),
});

export const {
  useAddLoanMutation,
  useDeleteLoanMutation,
  useEditLoanMutation,
  useGetLoansQuery,
} = loanApi;
