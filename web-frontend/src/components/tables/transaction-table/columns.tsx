"use client";

import { Checkbox } from "@/components/ui/checkbox";
import { CashFlowType, Transaction, TransactionType } from "@/lib/types";
import { ColumnDef } from "@tanstack/react-table";
import { CellAction } from "./cell-action"
import { format } from "date-fns";
import { ArrowUpDown } from "lucide-react";
import { Button } from "@/components/ui/button";

export const columns: ColumnDef<Transaction>[] = [
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
    accessorKey: "transactionType",
    header: "TRANSACTION TYPE",
    enableSorting: false,
  },
  {
    accessorKey: "cashFlowDetails.cashFlowName",
    header: "ACCOUNT NAME",
    enableSorting: false,
  },
  {
    accessorKey: "amount",
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
          className="p-0 h-auto font-semibold hover:bg-transparent"
        >
          AMOUNT
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      )
    },
    cell: ({ row }) => {
        const amount = row.getValue("amount") as number;
        const cashFlowTupe = row.original.cashFlowType as CashFlowType;
        return (
        <div className={`font-semibold px-2 py-1 rounded ${
            cashFlowTupe === CashFlowType.CREDIT
            ? 'text-green-700' 
            : 'text-red-700'
        }`}>
            {amount?.toLocaleString()}
        </div>
        );
    },
  },
  {
    accessorKey: "transactionDate",
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
          className="p-0 h-auto font-semibold hover:bg-transparent"
        >
          DATE
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      )
    },
    cell: (info) => format(new Date(info.getValue() as string), "dd-MM-yyyy"),
  },
  {
    accessorKey: "note",
    header: "NOTE",
    enableSorting: false,
  },
  {
    id: "actions",
    cell: ({ row }) => <CellAction data={row.original} />,
  },
];