import { Breadcrumbs } from "@/components/breadcrumbs";
import { ExpenseForm } from "@/components/forms/expense-form";
import { ScrollArea } from "@/components/ui/scroll-area";
import { CashFlowType } from "@/lib/types";
import { format } from "date-fns";
import { FC } from "react";

interface ExpensePageProps {
  params: {
    expenseId: string;
  };
  searchParams: {
    cashFlowId?: string;
    amount?: string;
    expenseDate?: string;
    expenseTime?: string;
    expenseType?: CashFlowType;
    reason?: string;
  };
}

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Expense", link: "/expense" },
  { title: "Update", link: "/expense/add" },
];

const Page: FC<ExpensePageProps> = ({ params, searchParams }) => {
  const { expenseId } = params;
  const {
    cashFlowId = null,
    amount = null,
    expenseDate = null,
    expenseTime = null,
    expenseType = null,
    reason = null,
  } = searchParams;
  return (
    <ScrollArea className="h-full">
      <div className="flex-1 space-y-4 p-8">
        <Breadcrumbs items={breadcrumbItems} />
        <ExpenseForm
          id={expenseId}
          cashFlowId={cashFlowId}
          amount={amount}
          expenseDate={
            new Date(expenseDate || "").valueOf()
              ? format(new Date(expenseDate || ""), "yyyy-MM-dd")
              : null
          }
          expenseTime={
            expenseDate
              ? expenseDate.split(":")[0] + ":" + expenseDate.split(":")[1]
              : null
          }
          expenseType={expenseType}
          reason={reason}
        />
      </div>
    </ScrollArea>
  );
};

export default Page;
