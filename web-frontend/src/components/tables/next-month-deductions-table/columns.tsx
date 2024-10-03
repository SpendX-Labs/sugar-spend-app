"use client";

import { Checkbox } from "@/components/ui/checkbox";
import { BudgetDetail } from "@/lib/types";
import { ColumnDef } from "@tanstack/react-table";
import { CellAction } from "./cell-action";
import { format } from "date-fns";

export const columns: ColumnDef<BudgetDetail>[] = [
  {
    accessorKey: "lender",
    header: "LENDER",
  },
  {
    accessorKey: "last4Digit",
    header: "ENDING NUMBER",
    cell: (info) => "XXXX " + info.getValue(),
  },
  {
    accessorKey: "actualAmount",
    header: "AMOUNT",
  },
  {
    accessorKey: "dueDate",
    header: "DUE DATE",
    cell: (info) => format(new Date(info.getValue() as string), "dd-MM-yyyy"),
  },
];
