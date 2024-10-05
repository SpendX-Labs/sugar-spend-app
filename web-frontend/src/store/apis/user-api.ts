import { authBaseQuery } from "@/lib/api-queries";
import { UserInfo } from "@/lib/types";
import { createApi } from "@reduxjs/toolkit/query/react";

const userUrl = "/settings/user";

export const userApi = createApi({
  reducerPath: "userApi",
  baseQuery: authBaseQuery,
  tagTypes: ["User"],
  endpoints: (builder) => ({
    updateBasicInfo: builder.mutation<UserInfo, any>({
      query: (data) => ({
        url: userUrl,
        method: "POST",
        body: data,
        responseHandler: "text",
      }),
      invalidatesTags: ["User"],
    }),
  }),
});

export const { useUpdateBasicInfoMutation } = userApi;
