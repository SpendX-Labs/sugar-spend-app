import { setCookie, deleteCookie } from "cookies-next";
import { createSlice } from "@reduxjs/toolkit";
import { authApi } from "./auth-api";
import { COOKIES_TOKEN_NAME } from "@/lib/constants";

export type AuthState = {
  token: string;
  username: string;
};

const initialState: Partial<AuthState> = {};

const setAuthCookie = (token: string, name: string) => {
  const toBase64 = Buffer.from(token).toString("base64");

  setCookie(name, toBase64, {
    maxAge: 30 * 24 * 60 * 60,
    path: "/",
  });
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
    builder.addMatcher(
      authApi.endpoints.login.matchFulfilled,
      (_state, { payload }) => {
        setAuthCookie(payload.token, COOKIES_TOKEN_NAME);
        return payload;
      }
    );
  },
  selectors: {
    selectUserName: (auth) => auth.username,
  },
});

export const { logout } = authSlice.actions;
export const { selectUserName } = authSlice.selectors;
