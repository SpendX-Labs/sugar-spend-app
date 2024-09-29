import { Breadcrumbs } from "@/components/breadcrumbs";
import { BankAccountForm } from "@/components/forms/bank-account-form";
import { ScrollArea } from "@/components/ui/scroll-area";
import { BankAccount } from "@/lib/types";
import { FC } from "react";

interface BankAccountPageProps {
  params: {
    bankAccountId: string;
  };
  searchParams: BankAccount;
}

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "BankAccount", link: "/bank-account" },
  { title: "Update", link: "/bank-account/add" },
];

const Page: FC<BankAccountPageProps> = ({ params, searchParams }) => {
  const { bankAccountId } = params;
  const {
    bankName = null,
    accountType = null,
    last4Digit = null,
    debitCardLast4Digit = null,
  } = searchParams;
  return (
    <ScrollArea className="h-full">
      <div className="flex-1 space-y-4 p-8">
        <Breadcrumbs items={breadcrumbItems} />
        <BankAccountForm
          id={bankAccountId}
          bankName={bankName}
          accountType={accountType}
          last4Digit={last4Digit}
          debitCardLast4Digit={debitCardLast4Digit}
        />
      </div>
    </ScrollArea>
  );
};

export default Page;
