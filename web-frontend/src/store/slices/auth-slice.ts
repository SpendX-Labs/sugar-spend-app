import { setCookie, deleteCookie } from "cookies-next";
import { createSlice } from "@reduxjs/toolkit";
import { authApi } from "../apis/auth-api";
import { COOKIES_TOKEN_NAME } from "@/lib/constants";

export type AuthState = {
  token: string;
  username: string;
};

const initialState: Partial<AuthState> = {};

const handleAuthSuccess = (_state: any, { payload }: any) => {
  const token = payload.token;
  const toBase64 = Buffer.from(token).toString("base64");

  setCookie(COOKIES_TOKEN_NAME, toBase64, {
    maxAge: 30 * 24 * 60 * 60,
    path: "/",
  });

  return payload;
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    logout: () => {
      deleteCookie(COOKIES_TOKEN_NAME);
      return {};
    },
  },
  extraReducers: (builder) => {
    builder
      .addMatcher(authApi.endpoints.login.matchFulfilled, handleAuthSuccess)
      .addMatcher(
        authApi.endpoints.verifyOtp.matchFulfilled,
        handleAuthSuccess
      );
  },
  selectors: {
    selectUserName: (auth) => auth.username,
  },
});

export const { logout } = authSlice.actions;
export const { selectUserName } = authSlice.selectors;
