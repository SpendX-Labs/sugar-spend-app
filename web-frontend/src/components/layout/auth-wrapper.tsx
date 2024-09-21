"use client";

import { useGetAuthDataQuery } from "@/store/auth/auth-api";
import { logout } from "@/store/auth/auth-slice";
import { useAppDispatch } from "@/hooks/use-app";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

type Props = {
  children?: React.ReactNode;
};

export const AuthWrapper = ({ children }: Props) => {
  const dispatch = useAppDispatch();
  const router = useRouter();

  const { isLoading, isSuccess, isError } = useGetAuthDataQuery();

  const isAuthPage = ["/", "/signup"].includes(window.location.pathname);

  useEffect(() => {
    if (isError) {
      dispatch(logout());
      router.push("/");
    }

    if (isSuccess && isAuthPage) {
      router.push("/dashboard");
    }
  }, [isSuccess, isError, router, dispatch, isAuthPage]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return children;
};
