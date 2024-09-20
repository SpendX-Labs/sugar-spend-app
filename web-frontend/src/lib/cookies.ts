import { getCookie } from "cookies-next";

// helpers to get cookies
const getAuthCookie = (name: string) => {
  const cookie = getCookie(name);

  if (!cookie) return undefined;

  return Buffer.from(cookie, "base64").toString("ascii");
};

export const getValidAuthTokens = () => {
  const token = getAuthCookie("auth_token");

  return { token };
};
