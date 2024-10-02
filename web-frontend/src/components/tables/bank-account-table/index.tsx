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

export const BankAccountTable = () => {
  const pathname = usePathname();
  const router = useRouter();
  const searchParams = useSearchParams();
  const page = Number(searchParams.get("page")) || 1;
  const size = Number(searchParams.get("size")) || 10;

  const {
    data: bankAccountRes,
    error,
    isLoading,
  } = useGetBankAccountsQuery({
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
    data: bankAccountRes?.data || [],
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
  if (error) return <div>Error fetching bank accounts</div>;

  const totalBankAccounts = bankAccountRes?.total || 0;
  const pageCount = Math.ceil(totalBankAccounts / size);
  const bankAccounts: BankAccount[] = bankAccountRes?.data || [];

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
