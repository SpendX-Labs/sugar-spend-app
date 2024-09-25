"use client";

import { Breadcrumbs } from "@/components/breadcrumbs";
import PageContainer from "@/components/layout/page-container";
import { columns } from "@/components/tables/credit-card-table/columns";
import { CreditCardTable } from "@/components/tables/credit-card-table";
import { buttonVariants } from "@/components/ui/button";
import { Heading } from "@/components/ui/heading";
import { Separator } from "@/components/ui/separator";
import { cn } from "@/lib/utils";
import { CreditCard } from "@/lib/types";
import { useGetCreditCardsQuery } from "@/store/credit-card/credit-card-api";
import { Plus } from "lucide-react";
import Link from "next/link";

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Credit Card", link: "/credit-card" },
];

type paramsProps = {
  searchParams: {
    [key: string]: string | string[] | undefined;
  };
};

export default function page({ searchParams }: paramsProps) {
  const page = Number(searchParams.page) || 1;
  const pageLimit = Number(searchParams.limit) || 10;

  const { data: creditCardRes } = useGetCreditCardsQuery();
  const totalCreditCards = creditCardRes?.total || 0;
  const pageCount = Math.ceil(totalCreditCards / pageLimit);
  const creditCards: CreditCard[] = creditCardRes?.data || [];

  return (
    <PageContainer>
      <div className="space-y-4">
        <Breadcrumbs items={breadcrumbItems} />

        <div className="flex items-start justify-between">
          <Heading
            title={`Credit Card (${totalCreditCards})`}
            description="Manage credit cards"
          />

          <Link
            href={"/credit-card/add"}
            className={cn(buttonVariants({ variant: "default" }))}
          >
            <Plus className="mr-2 h-4 w-4" /> Add New
          </Link>
        </div>
        <Separator />

        <CreditCardTable
          searchKey="creditCardName"
          pageNo={page}
          columns={columns}
          totalCount={totalCreditCards}
          data={creditCards}
          pageCount={pageCount}
        />
      </div>
    </PageContainer>
  );
}
