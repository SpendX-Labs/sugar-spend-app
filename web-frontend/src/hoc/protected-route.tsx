"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useGetAuthDataQuery } from "@/store/apis/auth-api";
import PageSkeleton from "@/components/skeletons/page-skeleton";

const ProtectedRoute: React.FC<React.PropsWithChildren<any>> = ({
  children,
}) => {
  const router = useRouter();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const { data: user, isLoading } = useGetAuthDataQuery();

  useEffect(() => {
    if (user?.enabled) {
      setIsLoggedIn(true);
    } else if (!isLoading) {
      router.push("/login");
    }
  }, [user, router, isLoading]);

  if (isLoading) {
    return <PageSkeleton />;
  }

  return isLoggedIn ? children : null;
};

export default ProtectedRoute;
