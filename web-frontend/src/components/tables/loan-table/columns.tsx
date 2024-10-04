"use client";

import { Checkbox } from "@/components/ui/checkbox";
import { Loan } from "@/lib/types";
import { ColumnDef } from "@tanstack/react-table";
import { CellAction } from "./cell-action";
import { convertMonthsToYearsMonths } from "@/lib/utils";

export const columns: ColumnDef<Loan>[] = [
  {
    id: "select",
    header: ({ table }) => (
      <Checkbox
        checked={table.getIsAllPageRowsSelected()}
        onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
        aria-label="Select all"
      />
    ),
    cell: ({ row }) => (
      <Checkbox
        checked={row.getIsSelected()}
        onCheckedChange={(value) => row.toggleSelected(!!value)}
        aria-label="Select row"
      />
    ),
    enableSorting: false,
    enableHiding: false,
  },
  {
    accessorKey: "creditCardNameOrlenderName",
    header: "CARD / LENDER",
    cell: (info) => {
      const row = info.row.original;
      return row.creditCardName ? row.creditCardName : row.lenderName;
    },
  },
  {
    accessorKey: "totalAmount",
    header: "LOAN AMOUNT",
    cell: (info) => `₹${info.getValue()?.toLocaleString()}`,
  },
  {
    accessorKey: "tenure",
    header: "TENURE (Months)",
    cell: (info) => convertMonthsToYearsMonths(info.getValue() as number),
  },
  {
    accessorKey: "emiAmount",
    header: "EMI AMOUNT",
    cell: (info) => `₹${info.getValue()?.toLocaleString()}`,
  },
  {
    accessorKey: "noCostEmi",
    header: "NO COST EMI",
    cell: (info) => (info.getValue() ? "Yes" : "No"),
  },
  {
    id: "actions",
    cell: ({ row }) => <CellAction data={row.original} />,
  },
];
