import { getCookie } from "cookies-next";
import { COOKIES_TOKEN_NAME } from "./constants";

const getAuthCookie = (name: string) => {
  const cookie = getCookie(name);

  if (!cookie) return undefined;

  return Buffer.from(cookie, "base64").toString("ascii");
};

export const getValidAuthTokens = () => {
  const token = getAuthCookie(COOKIES_TOKEN_NAME);

  return { token };
};
