import { AuthWrapper } from "@/components/layout/auth-wrapper";

export default function Dashboard() {
  return (
    <AuthWrapper>
      <div>
        <h1>Dashboard protected page</h1>
      </div>
    </AuthWrapper>
  );
}
