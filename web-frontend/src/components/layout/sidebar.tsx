"use client";

import { DashboardNav } from "@/components/dashboard-nav";
import { cn } from "@/lib/utils";
import { ChevronLeft } from "lucide-react";
import Link from "next/link";
import { navItems } from "@/lib/constants";
import { useAppDispatch, useAppSelector } from "@/hooks/use-app";
import { selectIsMinimized, toggleSidebar } from "@/store/slices/sidebar-slice";

type SidebarProps = {
  className?: string;
};

export default function Sidebar({ className }: SidebarProps) {
  const isMinimized = useAppSelector(selectIsMinimized);
  const dispatch = useAppDispatch();

  const handleToggle = () => {
    dispatch(toggleSidebar());
  };

  return (
    <aside
      className={cn(
        `relative  hidden h-screen flex-none border-r bg-card transition-[width] duration-500 md:block`,
        !isMinimized ? "w-72" : "w-[72px]",
        className
      )}
    >
      <div className="hidden p-5 pt-10 lg:block">
        <Link
          href={"https://github.com/Kiranism/next-shadcn-dashboard-starter"}
          target="_blank"
        >
          <svg viewBox="0 0 24 24" fill="currentColor" height="24" width="24">
            <path d="M24 12c0 6.627-5.373 12-12 12S0 18.627 0 12c0-1.826.407-3.555 1.137-5.105a9.784 9.784 0 00-.704 3.591c0 5.434 4.387 6.22 6.254 6.203 2.837-.026 6.154-1.416 8.948-3.991l-.471 2.65c-.106.605.29 1.138.896 1.142h.25c.627 0 1.073-.511 1.186-1.143l1.006-5.662c.12-.628-.293-1.14-.921-1.14h-5.673c-.63 0-1.207.334-1.32.968l-.044.255c-.09.603.33 1.057.931 1.057h2.96a9.48 9.48 0 00-.142.139c-2.04 1.93-4.556 2.988-6.64 2.988-2.08 0-4.41-1.313-4.41-4.269C3.243 3.555 8.99 0 12 0c6.614 0 12 5.373 12 12" />
          </svg>
        </Link>
      </div>
      <ChevronLeft
        className={cn(
          "absolute -right-3 top-10 z-50  cursor-pointer rounded-full border bg-background text-3xl text-foreground",
          isMinimized && "rotate-180"
        )}
        onClick={handleToggle}
      />
      <div className="space-y-4 py-4">
        <div className="px-3 py-2">
          <div className="mt-3 space-y-1">
            <DashboardNav items={navItems} />
          </div>
        </div>
      </div>
    </aside>
  );
}
