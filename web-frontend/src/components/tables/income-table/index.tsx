"use client";

import { Button } from "@/components/ui/button";
import { Heading } from "@/components/ui/heading";
import { Separator } from "@/components/ui/separator";
import { Income } from "@/lib/types";
import { useGetIncomesQuery } from "@/store/apis/income-api";
import { Plus } from "lucide-react";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { columns } from "./columns";
import React from "react";
import {
  PaginationState,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  useReactTable,
} from "@tanstack/react-table";
import { DataPaginationTable } from "../data-pagination-table";
import DataTableSkeleton from "@/components/skeletons/data-table-skeleton";

export const IncomeTable = () => {
  const pathname = usePathname();
  const router = useRouter();
  const searchParams = useSearchParams();
  const offset = Number(searchParams.get("offset")) || 0;
  const limit = Number(searchParams.get("limit")) || 10;

  const {
    data: incomeRes,
    error,
    isLoading,
    refetch,
  } = useGetIncomesQuery({
    offset: offset,
    limit,
  });

  const createQueryString = React.useCallback(
    (params: Record<string, string | number | null>) => {
      const newSearchParams = new URLSearchParams(searchParams?.toString());

      for (const [key, value] of Object.entries(params)) {
        if (value === null) {
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

  const totalIncomes = incomeRes?.total || 0;
  const pageCount = Math.ceil(totalIncomes / limit);
  const incomes: Income[] = incomeRes?.data || [];

  const table = useReactTable({
    data: incomeRes?.data || [],
    columns,
    pageCount,
    getCoreRowModel: getCoreRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    state: {
      pagination: { pageIndex, pageSize },
    },
    onPaginationChange: setPagination,
    getPaginationRowModel: getPaginationRowModel(),
    manualPagination: true,
    manualFiltering: true,
  });

  React.useEffect(() => {
    router.push(
      `${pathname}?${createQueryString({
        offset: pageIndex,
        limit: pageSize,
      })}`,
      {
        scroll: false,
      }
    );
    refetch();
  }, [pageIndex, pageSize, refetch]);

  if (isLoading) return <DataTableSkeleton columns={columns.length} />;
  if (error) return <div>Error fetching credit cards</div>;

  return (
    <>
      <div className="flex items-start justify-between">
        <Heading
          title={"Incomes" + (incomes.length ? `(${incomes.length})` : "")}
          description="Manage your incomes"
        />
        <Button
          className="text-xs md:text-sm"
          onClick={() => router.push(`/income/add`)}
        >
          <Plus className="mr-2 h-4 w-4" /> Add New
        </Button>
      </div>
      <Separator />
      <DataPaginationTable
        table={table}
        pageSizeOptions={[10, 20, 30, 40, 50]}
        searchKey="income"
      />
    </>
  );
};
