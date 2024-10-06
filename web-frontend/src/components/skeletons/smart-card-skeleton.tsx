import { Card, CardContent, CardHeader } from "../ui/card";
import { Skeleton } from "../ui/skeleton";

export default function SmartCardSkeleton() {
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <Skeleton className="h-4 w-24 rounded" />

        <Skeleton className="h-4 w-4 rounded-full" />
      </CardHeader>

      <CardContent>
        <Skeleton className="h-6 w-32 rounded" />

        <Skeleton className="h-3 w-28 mt-2 rounded" />
      </CardContent>
    </Card>
  );
}
