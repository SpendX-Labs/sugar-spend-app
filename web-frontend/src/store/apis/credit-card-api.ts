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

type GetCreditCardsParams = {
  offset: number;
  limit: number;
  sortBy?: string;
  sortOrder?: string;
  searchBy?: string;
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
    getCreditCards: builder.query<CreditCardsResponse, GetCreditCardsParams>({
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
          url: `${creditCardUrl}?${queryParams}`,
          method: "GET",
        };
      },
      providesTags: ["CreditCard"],
    }),
    getAllCreditCards: builder.query<CreditCard[], void>({
      query: () => {
        return {
          url: `${creditCardUrl}/all`,
          method: "GET",
        };
      },
      providesTags: ["CreditCard"],
    }),
  }),
});

export const {
  useAddCreditCardMutation,
  useDeleteCreditCardMutation,
  useEditCreditCardMutation,
  useGetCreditCardsQuery,
  useGetAllCreditCardsQuery
} = creditCardApi;