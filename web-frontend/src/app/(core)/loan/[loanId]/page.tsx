import { Breadcrumbs } from "@/components/breadcrumbs";
import { LoanForm } from "@/components/forms/loan-form";
import { ScrollArea } from "@/components/ui/scroll-area";
import { CashFlowDetails, CashFlowType, LoanType } from "@/lib/types";
import { FC } from "react";

interface LoanPageProps {
  params: {
    loanId: string;
  };
  searchParams: {
    creditCardId?: number;
    lenderId?: number;
    lenderName?: string;
    creditCardName?: string;
    last4Digit?: string;
    totalAmount?: number;
    loanType?: LoanType;
    interestRate?: number;
    noCostEmi?: string;
    tenure?: number;
    remainingTenure?: number;
    loanStartDate?: string;
    emiDateOfMonth?: number;
    principalAmount?: number;
    interestAmount?: number;
    updateLock?: boolean;
    remainingAmount?: number;
    remainingPrincipalAmount?: number;
    remainingInterestAmount?: number;
    emiAmount?: number;
  };
}

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Loan", link: "/loan" },
  { title: "Update", link: "/loan/add" },
];

const Page: FC<LoanPageProps> = ({ params, searchParams }) => {
  const { loanId } = params;
  return (
    <ScrollArea className="h-full">
      <div className="flex-1 space-y-4 p-8">
        <Breadcrumbs items={breadcrumbItems} />
        <LoanForm id={loanId} {...searchParams} />
      </div>
    </ScrollArea>
  );
};

export default Page;
