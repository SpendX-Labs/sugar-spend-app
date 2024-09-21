import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useGetAuthDataQuery } from "@/store/auth/auth-api";
import { getValidAuthTokens } from "@/lib/cookies";

export function useRedirectAuth() {
  const router = useRouter();
  const { data: user, isLoading } = useGetAuthDataQuery();

  const { token } = getValidAuthTokens();

  useEffect(() => {
    if (token && user?.enabled) {
      router.push("/dashboard");
    }
  }, [token, user, isLoading, router]);
}
