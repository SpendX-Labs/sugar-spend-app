import { Skeleton } from "../ui/skeleton";

interface FormSkeletonProps {
  columns?: number; // Number of columns
  rows?: number; // Number of rows
}

const FormSkeleton: React.FC<FormSkeletonProps> = ({
  columns = 3,
  rows = 3,
}) => {
  return (
    <div className="flex flex-col space-y-4 p-4">
      {/* Generate rows */}
      {Array.from({ length: rows }).map((_, rowIndex) => (
        <div
          key={rowIndex}
          className={`grid grid-cols-1 md:grid-cols-${columns} gap-4`}
        >
          {/* Generate columns */}
          {Array.from({ length: columns }).map((_, colIndex) => (
            <div key={colIndex} className="flex flex-col space-y-2">
              <Skeleton className="h-4 w-1/2" /> {/* Label */}
              <Skeleton className="h-10 w-full rounded" /> {/* Input Field */}
            </div>
          ))}
        </div>
      ))}

      {/* Submit Button Skeleton */}
      <div className="flex justify-center">
        <Skeleton className="h-10 w-32 rounded" /> {/* Submit Button */}
      </div>
    </div>
  );
};

export default FormSkeleton;
