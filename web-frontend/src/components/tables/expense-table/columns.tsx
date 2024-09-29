"use client";

import { Checkbox } from "@/components/ui/checkbox";
import { Expense } from "@/lib/types";
import { ColumnDef } from "@tanstack/react-table";
import { CellAction } from "./cell-action";
import { format } from "date-fns";

export const columns: ColumnDef<Expense>[] = [
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
    accessorKey: "expenseType",
    header: "EXPENSE TYPE",
  },
  {
    accessorKey: "cashFlowDetails.cashFlowName",
    header: "CREDIT CARD",
  },
  {
    accessorKey: "amount",
    header: "AMOUNT",
  },
  {
    accessorKey: "expenseDate",
    header: "DATE",
    cell: (info) => format(new Date(info.getValue() as string), "dd-MM-yyyy"),
  },
  {
    accessorKey: "expenseTime",
    header: "TIME",
  },
  {
    accessorKey: "reason",
    header: "Reason",
  },
  {
    id: "actions",
    cell: ({ row }) => <CellAction data={row.original} />,
  },
];
