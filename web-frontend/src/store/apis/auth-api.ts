import { authBaseQuery } from "@/lib/api-queries";
import { createApi } from "@reduxjs/toolkit/query/react";
import { logout } from "../slices/auth-slice";
import { UserInfo } from "@/lib/types";

export type LoginRequestBody = { username: string; password: string };

export type LoginResponse = {
  message: string;
  status: boolean;
  token: string;
  userDetails: {
    enabled: boolean;
    password: string;
    accountNonExpired: boolean;
    credentialsNonExpired: boolean;
    username: string;
    authorities: {
      authority: string;
    }[];
    accountNonLocked: boolean;
  };
};

export type SignupRequestBody = {
  username: string;
  emailId: string;
  password: string;
  phoneNumber: string;
  fullName: string;
  otp?: string;
};

export type SignupResponse = {
  messag: string;
  status: boolean;
  username: string;
  emailI: string;
};

export type AuthDataResponse = {
  enabled: boolean;
  password: string;
  accountNonExpired: boolean;
  credentialsNonExpired: boolean;
  username: string;
  authorities: {
    authority: string;
  }[];
  accountNonLocked: boolean;
};

export const authApi = createApi({
  reducerPath: "authApi",
  baseQuery: authBaseQuery,
  endpoints: (builder) => ({
    getAuthData: builder.query<AuthDataResponse, void>({
      query: () => ({
        url: "/auth/userinfo",
        method: "GET",
      }),
    }),
    login: builder.mutation<LoginResponse, LoginRequestBody>({
      query: (data) => ({
        url: "/auth/authenticate",
        method: "POST",
        body: data,
      }),
    }),
    logout: builder.mutation<void, void>({
      query: () => ({
        url: "/auth/logout",
        method: "POST",
      }),
      onQueryStarted: async (_arg, { dispatch, queryFulfilled }) => {
        try {
          await queryFulfilled;
          dispatch(logout());
        } catch (err) {}
      },
    }),
    signup: builder.mutation<SignupResponse, SignupRequestBody>({
      query: (data) => ({
        url: "/auth/signup",
        method: "POST",
        body: data,
      }),
    }),
    userInfo: builder.query<UserInfo, void>({
      query: () => ({
        url: "/auth/userinfo",
        method: "GET",
      }),
    }),
    verifyOtp: builder.mutation<SignupResponse, SignupRequestBody>({
      query: (data) => ({
        url: `/auth/verifyotp`,
        method: "POST",
        body: data,
      }),
    }),
    forgetPassword: builder.mutation<UserInfo, LoginRequestBody>({
      query: (data) => ({
        url: `/auth/forget-password`,
        method: "POST",
        body: data,
      }),
    }),
    forgetPasswordVerifyOtp: builder.mutation<
      UserInfo,
      { username: string; otp: string }
    >({
      query: (data) => ({
        url: `/auth/password-verifyotp`,
        method: "POST",
        body: data,
      }),
    }),
  }),
});

export const {
  useGetAuthDataQuery,
  useLoginMutation,
  useLogoutMutation,
  useSignupMutation,
  useUserInfoQuery,
  useVerifyOtpMutation,
  useForgetPasswordMutation,
  useForgetPasswordVerifyOtpMutation,
} = authApi;
