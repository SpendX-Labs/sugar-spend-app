import { Skeleton } from "../ui/skeleton";

export default function PieChartSkeleton() {
  return (
    <div className="flex flex-col items-center justify-center">
      <div className="mt-4 space-y-2 w-48">
        <Skeleton className="h-4 w-full rounded" />
        <Skeleton className="h-4 w-full rounded" />
      </div>
      <Skeleton className="h-48 w-48 rounded-full" />
      <div className="mt-4 space-y-2 w-48">
        <Skeleton className="h-4 w-full rounded" />
        <Skeleton className="h-4 w-full rounded" />
      </div>
    </div>
  );
}
