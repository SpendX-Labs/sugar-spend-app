import { Breadcrumbs } from "@/components/breadcrumbs";
import { ExpenseForm } from "@/components/forms/expense-form";
import { ScrollArea } from "@/components/ui/scroll-area";
import { CashFlowDetails, CashFlowType } from "@/lib/types";
import { FC } from "react";

interface ExpensePageProps {
  params: {
    expenseId: string;
  };
  searchParams: {
    cashFlowDetails?: CashFlowDetails | null;
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
    cashFlowDetails = null,
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
          cashFlowDetails={cashFlowDetails}
          amount={amount}
          expenseDate={expenseDate}
          expenseTime={expenseTime}
          expenseType={expenseType}
          reason={reason}
        />
      </div>
    </ScrollArea>
  );
};

export default Page;
