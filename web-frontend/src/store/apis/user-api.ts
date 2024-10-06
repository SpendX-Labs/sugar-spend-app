import { authBaseQuery } from "@/lib/api-queries";
import { UserInfo } from "@/lib/types";
import { createApi } from "@reduxjs/toolkit/query/react";

const userUrl = "/settings/user";

export const userApi = createApi({
  reducerPath: "userApi",
  baseQuery: authBaseQuery,
  tagTypes: ["User"],
  endpoints: (builder) => ({
    updateBasicInfo: builder.mutation<
      UserInfo,
      { fullName: string; phonenumber: string }
    >({
      query: (data) => ({
        url: userUrl + "/userinfo",
        method: "PATCH",
        body: data,
      }),
      invalidatesTags: ["User"],
    }),
    updateEmail: builder.mutation<any, { emailId: string }>({
      query: (data) => ({
        url: userUrl + "/email/" + data.emailId,
        method: "PATCH",
      }),
      invalidatesTags: ["User"],
    }),
    verifyEmail: builder.mutation<any, { otp: string }>({
      query: (data) => ({
        url: userUrl + "/email/verify/" + data.otp,
        method: "PATCH",
      }),
      invalidatesTags: ["User"],
    }),
    updatePassword: builder.mutation<
      any,
      {
        currentPassword: string;
        newPassword: string;
        confirmPassword: string;
      }
    >({
      query: (data) => ({
        url: userUrl + "/password",
        method: "PATCH",
        body: data,
      }),
      invalidatesTags: ["User"],
    }),
  }),
});

export const {
  useUpdateBasicInfoMutation,
  useUpdatePasswordMutation,
  useUpdateEmailMutation,
  useVerifyEmailMutation,
} = userApi;
