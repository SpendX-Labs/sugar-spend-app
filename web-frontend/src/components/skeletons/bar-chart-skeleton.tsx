import { Skeleton } from "../ui/skeleton";

export default function BarChartSkeleton() {
  return (
    <div className="space-y-4">
      {/* Bar Chart Header */}
      <Skeleton className="h-6 w-1/4 rounded" />{" "}
      {/* Placeholder for chart title */}
      {/* Bar Chart Skeleton */}
      <div className="flex items-end space-x-2">
        <Skeleton className="h-32 w-8 rounded" />{" "}
        {/* Placeholder for a tall bar */}
        <Skeleton className="h-24 w-8 rounded" />{" "}
        {/* Placeholder for a medium bar */}
        <Skeleton className="h-16 w-8 rounded" />{" "}
        {/* Placeholder for a short bar */}
        <Skeleton className="h-40 w-8 rounded" />{" "}
        {/* Placeholder for a tall bar */}
        <Skeleton className="h-28 w-8 rounded" />{" "}
        {/* Placeholder for a medium bar */}
        <Skeleton className="h-20 w-8 rounded" />{" "}
        {/* Placeholder for a short bar */}
      </div>
      {/* X-axis Labels */}
      <div className="flex justify-between">
        <Skeleton className="h-4 w-8 rounded" /> {/* Placeholder for label */}
        <Skeleton className="h-4 w-8 rounded" /> {/* Placeholder for label */}
        <Skeleton className="h-4 w-8 rounded" /> {/* Placeholder for label */}
        <Skeleton className="h-4 w-8 rounded" /> {/* Placeholder for label */}
        <Skeleton className="h-4 w-8 rounded" /> {/* Placeholder for label */}
        <Skeleton className="h-4 w-8 rounded" /> {/* Placeholder for label */}
      </div>
    </div>
  );
}
