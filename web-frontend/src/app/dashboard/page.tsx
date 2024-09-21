"use client";

import ProtectedRoute from "@/components/layout/protected-route";
import { Button } from "@/components/ui/button";
import { useLogoutMutation } from "@/store/auth/auth-api";
import { useRouter } from "next/navigation";

export default function Dashboard() {
  const [logout] = useLogoutMutation();
  const router = useRouter();
  return (
    <ProtectedRoute>
      <div>
        <h1>Dashboard protected page</h1>
        <Button
          onClick={async () => {
            await logout();
            router.push("/login");
          }}
        >
          Log Out
        </Button>
      </div>
    </ProtectedRoute>
  );
}
