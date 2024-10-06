"use client";

import { Button } from "@/components/ui/button";
import { Heading } from "@/components/ui/heading";
import { Separator } from "@/components/ui/separator";
import { BankAccount } from "@/lib/types";
import { useGetBankAccountsQuery } from "@/store/apis/bank-account-api";
import {
  PaginationState,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  useReactTable,
} from "@tanstack/react-table";
import { Plus } from "lucide-react";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import React from "react";
import { columns } from "./columns";
import { DataPaginationTable } from "../data-pagination-table";
import DataTableSkeleton from "@/components/skeletons/data-table-skeleton";

export const BankAccountTable = () => {
  const pathname = usePathname();
  const router = useRouter();
  const searchParams = useSearchParams();
  const offset = Number(searchParams.get("offset")) || 0;
  const limit = Number(searchParams.get("limit")) || 10;

  const {
    data: bankAccountRes,
    error,
    isLoading,
    refetch,
  } = useGetBankAccountsQuery({
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

  const totalBankAccounts = bankAccountRes?.total || 0;
  const pageCount = Math.ceil(totalBankAccounts / limit);
  const bankAccounts: BankAccount[] = bankAccountRes?.data || [];

  const table = useReactTable({
    data: bankAccountRes?.data || [],
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
  if (error) return <div>Error fetching bank accounts</div>;

  return (
    <>
      <div className="flex items-start justify-between">
        <Heading
          title={
            "BankAccounts" +
            (bankAccounts.length ? `(${bankAccounts.length})` : "")
          }
          description="Manage your bankAccounts"
        />
        <Button
          className="text-xs md:text-sm"
          onClick={() => router.push(`/bank-account/add`)}
        >
          <Plus className="mr-2 h-4 w-4" /> Add New
        </Button>
      </div>
      <Separator />
      <DataPaginationTable
        table={table}
        pageSizeOptions={[10, 20, 30, 40, 50]}
        searchKey="bankAccount"
      />
    </>
  );
};
