import { useEffect } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { useGetAuthDataQuery } from "@/store/apis/auth-api";
import { getValidAuthTokens } from "@/lib/cookies";

export function useRedirectAuth() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const callbackUrl = searchParams.get("callbackUrl");
  const { data: user, isLoading } = useGetAuthDataQuery();

  const { token } = getValidAuthTokens();

  useEffect(() => {
    if (token && user?.enabled) {
      router.push(callbackUrl || "/");
    }
  }, [token, user, isLoading, router]);
}
