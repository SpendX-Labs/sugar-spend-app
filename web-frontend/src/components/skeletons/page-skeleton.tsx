import { Skeleton } from "../ui/skeleton";
import MainSectionSkeleton from "./main-section-skeleton";

export default function PageSkeleton() {
  return (
    <div className="flex h-screen w-screen">
      {/* Sidebar */}
      <div className="w-64 pl-4">
        <Skeleton className="h-10 w-10 mb-6 rounded-xl" />
        <Skeleton className="h-10 w-5/6 mb-4 rounded" />
        <Skeleton className="h-10 w-5/6 mb-4 rounded" />
        <Skeleton className="h-10 w-5/6 mb-4 rounded" />
        <Skeleton className="h-10 w-5/6 mb-4 rounded" />
        <Skeleton className="h-10 w-5/6 mb-4 rounded" />
        <Skeleton className="h-10 w-5/6 mb-4 rounded" />
      </div>

      {/* Main Content */}
      <MainSectionSkeleton />
    </div>
  );
}
