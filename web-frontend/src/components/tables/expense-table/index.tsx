"use client";

import { Button } from "@/components/ui/button";
import { DataTable } from "@/components/ui/data-table";
import { Heading } from "@/components/ui/heading";
import { Separator } from "@/components/ui/separator";
import { Expense } from "@/lib/types";
import { useGetExpensesQuery } from "@/store/expense/expense-api";
import { Plus } from "lucide-react";
import { useRouter, useSearchParams } from "next/navigation";
import { columns } from "./columns";

export const ExpenseTable = () => {
  const router = useRouter();
  const searchParams = useSearchParams();
  const page = Number(searchParams.get("page")) || 1;
  const size = Number(searchParams.get("size")) || 10;

  const {
    data: expenseRes,
    error,
    isLoading,
  } = useGetExpensesQuery({
    page: page - 1,
    size,
  });

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error fetching credit cards</div>;

  const totalExpenses = expenseRes?.total || 0;
  const pageCount = Math.ceil(totalExpenses / size);
  const expenses: Expense[] = expenseRes?.data || [];

  return (
    <>
      <div className="flex items-start justify-between">
        <Heading
          title={"Expenses" + (expenses.length ? `(${expenses.length})` : "")}
          description="Manage your expenses"
        />
        <Button
          className="text-xs md:text-sm"
          onClick={() => router.push(`/expense/add`)}
        >
          <Plus className="mr-2 h-4 w-4" /> Add New
        </Button>
      </div>
      <Separator />
      <DataTable searchKey="name" columns={columns} data={expenses} />
    </>
  );
};
