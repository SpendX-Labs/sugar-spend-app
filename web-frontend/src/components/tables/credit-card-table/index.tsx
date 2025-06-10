"use client";

import { Button } from "@/components/ui/button";
import { Heading } from "@/components/ui/heading";
import { Separator } from "@/components/ui/separator";
import { CreditCard } from "@/lib/types";
import { useGetCreditCardsQuery } from "@/store/apis/credit-card-api";
import { Plus } from "lucide-react";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { columns } from "./columns";
import React from "react";
import {
  PaginationState,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useReactTable,
} from "@tanstack/react-table";
import { DataPaginationTable } from "../data-pagination-table";
import DataTableSkeleton from "@/components/skeletons/data-table-skeleton";

// Custom hook for debounced value
function useDebounce<T>(value: T, delay: number): T {
  const [debouncedValue, setDebouncedValue] = React.useState<T>(value);

  React.useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    return () => {
      clearTimeout(handler);
    };
  }, [value, delay]);

  return debouncedValue;
}

export const CreditCardTable = () => {
  const pathname = usePathname();
  const router = useRouter();
  const searchParams = useSearchParams();
  const offset = Number(searchParams.get("offset")) || 0;
  const limit = Number(searchParams.get("limit")) || 10;
  const initialSearchBy = searchParams.get("searchBy") || "";

  // Search state and debounced search
  const [searchValue, setSearchValue] = React.useState(initialSearchBy);
  const debouncedSearchValue = useDebounce(searchValue, 500); // 500ms delay

  const {
    data: creditCardRes,
    error,
    isLoading,
    refetch,
  } = useGetCreditCardsQuery({
    offset,
    limit,
    searchBy: debouncedSearchValue,
  });
  
  const totalCreditCards = creditCardRes?.total || 0;
  const pageCount = Math.ceil(totalCreditCards / limit);
  const creditCards: CreditCard[] = creditCardRes?.data || [];

  const createQueryString = React.useCallback(
    (params: Record<string, string | number | null>) => {
      const newSearchParams = new URLSearchParams(searchParams?.toString());

      for (const [key, value] of Object.entries(params)) {
        if (value === null || value === "") {
          newSearchParams.delete(key);
        } else {
          newSearchParams.set(key, String(value));
        }
      }

      return newSearchParams.toString();
    },
    [searchParams]
  );

  const [{ pageIndex, pageSize }, setPagination] =
    React.useState<PaginationState>({
      pageIndex: offset,
      pageSize: limit,
    });

  const table = useReactTable({
    data: creditCards,
    columns,
    pageCount,
    getCoreRowModel: getCoreRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getSortedRowModel: getSortedRowModel(),
    state: {
      pagination: { pageIndex, pageSize },
    },
    onPaginationChange: setPagination,
    getPaginationRowModel: getPaginationRowModel(),
    manualPagination: true,
    manualFiltering: true,
    manualSorting: true,
  });

  // Handle search input change
  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setSearchValue(value);
    
    // Reset to first page when searching
    setPagination(prev => ({ ...prev, pageIndex: 0 }));
  };

  // Clear search
  const handleClearSearch = () => {
    setSearchValue("");
  };

  // Update URL when debounced search value changes
  React.useEffect(() => {

    router.push(
      `${pathname}?${createQueryString({
        offset: pageIndex,
        limit: pageSize,
        searchBy: debouncedSearchValue || null,
      })}`,
      {
        scroll: false,
      }
    );
    refetch();
  }, [pageIndex, pageSize, debouncedSearchValue, refetch]);

  if (isLoading) return <DataTableSkeleton columns={columns.length} />;
  if (error) return <div>Error fetching credit cards</div>;

  return (
    <>
      <div className="flex items-start justify-between">
        <Heading
          title={
            "Credit Cards" + (creditCardRes?.total ? `(${creditCardRes.total})` : "")
          }
          description="Manage your credit cards"
        />
        <Button
          className="text-xs md:text-sm"
          onClick={() => router.push(`/credit-card/add`)}
        >
          <Plus className="mr-2 h-4 w-4" /> Add New
        </Button>
      </div>
      <Separator />
      
      <DataPaginationTable
        table={table}
        pageSizeOptions={[10, 20, 30, 40, 50]}
        searchKey="creditCard"
        searchValue={searchValue}
        onSearchChange={handleSearchChange}
        onClearSearch={handleClearSearch}
        searchPlaceholder="Search credit cards by bank or card name..."
        debouncedSearchValue={debouncedSearchValue}
        showSearchIndicator={true}
      />
    </>
  );
};