"use client";

import { Checkbox } from "@/components/ui/checkbox";
import { Income } from "@/lib/types";
import { ColumnDef } from "@tanstack/react-table";
import { CellAction } from "./cell-action";
import { format } from "date-fns";

export const columns: ColumnDef<Income>[] = [
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
    accessorKey: "incomeType",
    header: "INCOME TYPE",
  },
  {
    accessorKey: "cashFlowDetails.cashFlowName",
    header: "ACCOUNT NAME",
  },
  {
    accessorKey: "amount",
    header: "AMOUNT",
  },
  {
    accessorKey: "dateOfEvent",
    header: "DATE",
    cell: (info) => format(new Date(info.getValue() as string), "dd-MM-yyyy"),
  },
  {
    accessorKey: "timeOfEvent",
    header: "TIME",
  },
  {
    accessorKey: "message",
    header: "Message",
  },
  {
    id: "actions",
    cell: ({ row }) => <CellAction data={row.original} />,
  },
];
