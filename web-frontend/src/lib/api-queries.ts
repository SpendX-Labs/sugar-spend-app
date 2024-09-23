import {
  fetchBaseQuery,
  FetchArgs,
  BaseQueryApi,
} from "@reduxjs/toolkit/query/react";
import { getValidAuthTokens } from "./cookies";

const REQUEST_TIMEOUT = 10000;
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export const authBaseQuery = async (
  args: FetchArgs,
  api: BaseQueryApi,
  extraOptions?: { [key: string]: unknown }
) => {
  const { token } = getValidAuthTokens();

  const headers = {
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    "Content-Type": "application/json",
  };

  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), REQUEST_TIMEOUT);

  const fetchOptions = {
    ...extraOptions,
    signal: controller.signal,
    headers,
  };

  try {
    const response = await fetchBaseQuery({
      baseUrl: API_BASE_URL,
      fetchFn: (arg) => fetch(arg, fetchOptions),
    })(args, api, { ...extraOptions });

    clearTimeout(timeoutId);
    return response;
  } catch (error) {
    clearTimeout(timeoutId);
    return { error: { status: "FETCH_ERROR", message: "Request timed out" } };
  }
};
