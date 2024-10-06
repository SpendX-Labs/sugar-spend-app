import { Skeleton } from "../ui/skeleton";

export default function MainSectionSkeleton() {
  return (
    <>
      <div className="flex-1 p-6">
        <div className="flex items-center justify-between">
          <Skeleton className="h-8 w-1/4 rounded" />{" "}
          <Skeleton className="h-8 w-20 rounded" />{" "}
        </div>

        <div className="mt-6 space-y-4">
          <Skeleton className="h-64 w-full rounded" />{" "}
          <Skeleton className="h-32 w-full rounded" />{" "}
          <Skeleton className="h-32 w-full rounded" />{" "}
        </div>
      </div>
    </>
  );
}
