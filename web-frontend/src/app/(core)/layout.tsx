import Header from "@/components/layout/header";
import ProtectedRoute from "@/hoc/protected-route";
import Sidebar from "@/components/layout/sidebar";

export default function HomeLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="flex">
      <ProtectedRoute>
        <Sidebar />
        <main className="w-full flex-1 overflow-hidden">
          <Header />
          {children}
        </main>
      </ProtectedRoute>
    </div>
  );
}
