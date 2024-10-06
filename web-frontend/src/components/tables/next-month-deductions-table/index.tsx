"use client";

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { useGetNextMonthReportQuery } from "@/store/apis/budget-api";
import {
  PaginationState,
  flexRender,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  useReactTable,
} from "@tanstack/react-table";
import React from "react";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { columns } from "./columns";
import DataTableSkeleton from "@/components/skeletons/data-table-skeleton";

export const NextMonthDeductionsTable = () => {
  const pathname = usePathname();
  const router = useRouter();

  const {
    data: nextMonthDeductionRes,
    error,
    isLoading,
  } = useGetNextMonthReportQuery();

  const table = useReactTable({
    data: nextMonthDeductionRes?.details || [],
    columns,
    getCoreRowModel: getCoreRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    manualPagination: true,
    manualFiltering: true,
  });

  if (isLoading) return <DataTableSkeleton pagination={false} />;
  if (error) return <div>Error fetching credit cards</div>;

  const totalNextMonthDeductions = nextMonthDeductionRes?.details.length || 0;

  return (
    <Card className="flex flex-col h-full">
      <CardHeader>
        <CardTitle>Next Month Deductions</CardTitle>
        <CardDescription>
          List of deductions ({totalNextMonthDeductions})
        </CardDescription>
      </CardHeader>
      <CardContent className="flex-1">
        <ScrollArea className="rounded-md border">
          <Table className="relative">
            <TableHeader>
              {table.getHeaderGroups().map((headerGroup) => (
                <TableRow key={headerGroup.id}>
                  {headerGroup.headers.map((header) => {
                    return (
                      <TableHead key={header.id}>
                        {header.isPlaceholder
                          ? null
                          : flexRender(
                              header.column.columnDef.header,
                              header.getContext()
                            )}
                      </TableHead>
                    );
                  })}
                </TableRow>
              ))}
            </TableHeader>
            <TableBody>
              {table.getRowModel().rows?.length ? (
                table.getRowModel().rows.map((row) => (
                  <TableRow
                    key={row.id}
                    data-state={row.getIsSelected() && "selected"}
                  >
                    {row.getVisibleCells().map((cell) => (
                      <TableCell key={cell.id}>
                        {flexRender(
                          cell.column.columnDef.cell,
                          cell.getContext()
                        )}
                      </TableCell>
                    ))}
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell
                    colSpan={columns.length}
                    className="h-24 text-center"
                  >
                    No results.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
          <ScrollBar orientation="vertical" />
        </ScrollArea>
      </CardContent>
    </Card>
  );
};
