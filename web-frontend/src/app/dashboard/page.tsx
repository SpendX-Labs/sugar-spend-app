"use client";

import { Button } from "@/components/ui/button";
import { useLogoutMutation } from "@/store/auth/auth-api";
import { useRouter } from "next/navigation";

export default function Dashboard() {
  const [fetchLogout] = useLogoutMutation();
  const router = useRouter();
  return (
    <div>
      <h1>Dashboard protected page</h1>
      <Button
        onClick={() => {
          fetchLogout();
          router.push("/");
        }}
      >
        Log Out
      </Button>
    </div>
  );
}
