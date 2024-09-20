import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export interface LoginResponse {
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
}

export interface UserDetails {
  enabled: boolean;
  password: string;
  accountNonExpired: boolean;
  credentialsNonExpired: boolean;
  username: string;
  authorities: {
    authority: string;
  }[];
  accountNonLocked: boolean;
}

export const authApi = createApi({
  reducerPath: "authApi",
  baseQuery: fetchBaseQuery({
    baseUrl: process.env.NEXT_PUBLIC_API_BASE_URL,
  }),
  endpoints: (builder) => ({
    login: builder.mutation<
      LoginResponse,
      { username: string; password: string }
    >({
      query: ({ username, password }) => ({
        url: "/auth/authenticate",
        method: "POST",
        body: {
          username,
          password,
        },
      }),
    }),
    getAuthData: builder.query<UserDetails, { token: string }>({
      query: ({ token }) => {
        console.log("I am gettting called");
        return {
          url: "/auth/userinfo",
          method: "GET",
          headers: {
            Authorization: token,
          },
        };
      },
    }),
  }),
});

export const { useLoginMutation, useGetAuthDataQuery } = authApi;
