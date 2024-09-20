"use client";

import { getValidAuthTokens } from "@/lib/cookies";
import { useGetAuthDataQuery } from "@/store/auth/authApi";
import { useEffect } from "react";
import { logout, selectUserName } from "@/store/auth/authSlice";
import { useAppDispatch, useAppSelector } from "@/hooks/use-app";
import { useRouter } from "next/navigation";

type Props = {
  children?: React.ReactNode;
};

export const AuthWrapper = ({ children }: Props) => {
  const dispatch = useAppDispatch();
  const userName = useAppSelector(selectUserName);
  const router = useRouter();

  const { token } = getValidAuthTokens();

  const { data, error, isLoading } = useGetAuthDataQuery(
    { token: token || "" },
    {
      skip: !!userName || !token,
    }
  );

  useEffect(() => {
    if (!token) {
      router.push("/login");
      dispatch(logout());
    }
  }, [token, router.push]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return children;
};
