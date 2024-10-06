import { Skeleton } from "../ui/skeleton";

interface DataTableSkeletonProps {
  columns?: number;
  rows?: number;
  pagination?: boolean;
}

export default function DataTableSkeleton({
  columns = 5,
  rows = 10,
  pagination = true,
}: DataTableSkeletonProps) {
  return (
    <div className="flex flex-col space-y-4">
      {/* Search Input Skeleton */}
      <div className="w-full md:max-w-sm mb-4">
        <Skeleton className="h-8 w-full rounded" />
      </div>

      {/* Table Skeleton */}
      <div className="h-[calc(80vh-220px)] rounded-md border overflow-auto">
        <div className="flex mb-2">
          {Array.from({ length: columns }).map((_, index) => (
            <Skeleton
              key={`header-${index}`}
              className="h-6 w-32 rounded mb-2"
            />
          ))}
        </div>

        <div>
          {Array.from({ length: rows }).map((_, rowIndex) => (
            <div key={`row-${rowIndex}`} className="flex mb-2">
              {Array.from({ length: columns }).map((_, colIndex) => (
                <Skeleton
                  key={`cell-${rowIndex}-${colIndex}`}
                  className="h-8 w-32 rounded"
                /> // Table cells
              ))}
            </div>
          ))}
        </div>
      </div>

      {/* Pagination Skeleton */}
      {pagination && (
        <div className="flex flex-col items-center justify-end gap-2 space-x-2 py-4 sm:flex-row w-full">
          <div className="flex-1 text-sm text-muted-foreground">
            <Skeleton className="h-4 w-32" />
          </div>

          <div className="flex flex-col items-center gap-4 sm:flex-row sm:gap-6 lg:gap-8">
            <div className="flex items-center space-x-2">
              <p className="whitespace-nowrap text-sm font-medium">
                Rows per page
              </p>
              <Skeleton className="h-8 w-[70px]" />
            </div>
          </div>

          <div className="flex items-center space-x-2">
            <div className="flex w-[100px] items-center justify-center text-sm font-medium">
              <Skeleton className="h-4 w-24" /> {/* Skeleton for page number */}
            </div>
            <div className="flex space-x-2">
              <div className="h-4 w-4 rounded border border-muted-foreground flex items-center justify-center">
                <Skeleton className="h-4 w-4" />
              </div>
              <div className="h-4 w-4 rounded border border-muted-foreground flex items-center justify-center">
                <Skeleton className="h-4 w-4" />
              </div>
              <div className="h-4 w-4 rounded border border-muted-foreground flex items-center justify-center">
                <Skeleton className="h-4 w-4" />
              </div>
              <div className="h-4 w-4 rounded border border-muted-foreground flex items-center justify-center">
                <Skeleton className="h-4 w-4" />
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
