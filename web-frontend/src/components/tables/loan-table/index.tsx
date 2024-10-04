"use client";

import { Button } from "@/components/ui/button";
import { Heading } from "@/components/ui/heading";
import { Separator } from "@/components/ui/separator";
import { Loan } from "@/lib/types";
import { useGetLoansQuery } from "@/store/apis/loan-api";
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

export const LoanTable = () => {
  const pathname = usePathname();
  const router = useRouter();
  const searchParams = useSearchParams();
  const page = Number(searchParams.get("page")) || 1;
  const size = Number(searchParams.get("size")) || 10;

  const {
    data: loanRes,
    error,
    isLoading,
  } = useGetLoansQuery({
    page: page - 1,
    size,
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
      pageIndex: page - 1,
      pageSize: size,
    });

  React.useEffect(() => {
    router.push(
      `${pathname}?${createQueryString({
        page: pageIndex + 1,
        size: pageSize,
      })}`,
      {
        scroll: false,
      }
    );
  }, [pageIndex, pageSize]);

  const table = useReactTable({
    data: loanRes?.data || [],
    columns,
    pageCount: page ?? -1,
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

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error fetching credit cards</div>;

  const totalLoans = loanRes?.total || 0;
  const pageCount = Math.ceil(totalLoans / size);
  const loans: Loan[] = loanRes?.data || [];

  return (
    <>
      <div className="flex items-start justify-between">
        <Heading
          title={"Loans" + (loans.length ? `(${loans.length})` : "")}
          description="Manage your loans"
        />
        <Button
          className="text-xs md:text-sm"
          onClick={() => router.push(`/loan/add`)}
        >
          <Plus className="mr-2 h-4 w-4" /> Add New
        </Button>
      </div>
      <Separator />
      <DataPaginationTable
        table={table}
        pageSizeOptions={[10, 20, 30, 40, 50]}
        searchKey="loan"
      />
    </>
  );
};
