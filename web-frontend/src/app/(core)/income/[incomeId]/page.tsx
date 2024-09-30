import { Breadcrumbs } from "@/components/breadcrumbs";
import { IncomeForm } from "@/components/forms/income-form";
import { ScrollArea } from "@/components/ui/scroll-area";
import { CashFlowDetails, CashFlowType } from "@/lib/types";
import { FC } from "react";

interface IncomePageProps {
  params: {
    incomeId: string;
  };
  searchParams: {
    cashFlowDetails?: CashFlowDetails | null;
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
    cashFlowDetails = null,
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
          cashFlowDetails={cashFlowDetails}
          amount={amount}
          dateOfEvent={dateOfEvent}
          timeOfEvent={timeOfEvent}
          incomeType={incomeType}
          message={message}
        />
      </div>
    </ScrollArea>
  );
};

export default Page;
