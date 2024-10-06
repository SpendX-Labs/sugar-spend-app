import { Breadcrumbs } from "@/components/breadcrumbs";
import { IncomeForm } from "@/components/forms/income-form";
import { ScrollArea } from "@/components/ui/scroll-area";
import { CashFlowType } from "@/lib/types";
import { format } from "date-fns";
import { FC } from "react";

interface IncomePageProps {
  params: {
    incomeId: string;
  };
  searchParams: {
    cashFlowId?: string;
    amount?: string;
    dateOfEvent?: string;
    timeOfEvent?: string;
    incomeType?: CashFlowType;
    message?: string;
  };
}

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Income", link: "/income" },
  { title: "Update", link: "/income/add" },
];

const Page: FC<IncomePageProps> = ({ params, searchParams }) => {
  const { incomeId } = params;
  const {
    cashFlowId = null,
    amount = null,
    dateOfEvent = null,
    timeOfEvent = null,
    incomeType = null,
    message = null,
  } = searchParams;

  return (
    <ScrollArea className="h-full">
      <div className="flex-1 space-y-4 p-8">
        <Breadcrumbs items={breadcrumbItems} />
        <IncomeForm
          id={incomeId}
          cashFlowId={cashFlowId}
          amount={amount}
          dateOfEvent={
            new Date(dateOfEvent || "").valueOf()
              ? format(new Date(dateOfEvent || ""), "yyyy-MM-dd")
              : null
          }
          timeOfEvent={
            timeOfEvent
              ? timeOfEvent.split(":")[0] + ":" + timeOfEvent.split(":")[1]
              : null
          }
          incomeType={incomeType}
          message={message}
        />
      </div>
    </ScrollArea>
  );
};

export default Page;
