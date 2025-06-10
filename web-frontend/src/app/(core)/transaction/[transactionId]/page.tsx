import { Breadcrumbs } from "@/components/breadcrumbs";
import { TransactionForm } from "@/components/forms/transaction-form";
import { ScrollArea } from "@/components/ui/scroll-area";
import { CashFlowType, TransactionType } from "@/lib/types";
import { format } from "date-fns";
import { FC } from "react";

interface TransactionPageProps {
  params: {
    transactionId: string;
  };
  searchParams: {
    cashFlowId?: string;
    transactionType: TransactionType;
    amount?: string;
    transactionDate?: string;
    transactionTime?: string;
    cashFlowType?: CashFlowType;
    note?: string;
  };
}

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Transaction", link: "/transaction" },
  { title: "Update", link: "/transaction/add" },
];

const Page: FC<TransactionPageProps> = ({ params, searchParams }) => {
  const { transactionId } = params;
  const {
    cashFlowId = null,
    transactionType = null,
    amount = null,
    transactionDate = null,
    cashFlowType = null,
    note = null,
  } = searchParams;
  return (
    <ScrollArea className="h-full">
      <div className="flex-1 space-y-4 p-8">
        <Breadcrumbs items={breadcrumbItems} />
        <TransactionForm
          id={transactionId}
          cashFlowId={cashFlowId}
          transactionType={transactionType}
          amount={amount}
          transactionDate={
            new Date(transactionDate || "").valueOf()
              ? format(new Date(transactionDate || ""), "yyyy-MM-dd")
              : null
          }
          cashFlowType={cashFlowType}
          note={note}
        />
      </div>
    </ScrollArea>
  );
};

export default Page;
