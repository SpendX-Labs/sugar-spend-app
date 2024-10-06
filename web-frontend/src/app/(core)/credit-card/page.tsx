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
import { useGetCreditCardsQuery } from "@/store/apis/credit-card-api";
import { Plus } from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";
import React from "react";
import DataTableSkeleton from "@/components/skeletons/data-table-skeleton";

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Credit Card", link: "/credit-card" },
];

export default function page() {
  const searchParams = useSearchParams();
  const offset = Number(searchParams.get("offset")) || 0;
  const limit = Number(searchParams.get("limit")) || 10;

  const {
    data: creditCardRes,
    error,
    isLoading,
    refetch,
  } = useGetCreditCardsQuery({
    offset,
    limit,
  });

  React.useEffect(() => {
    refetch();
  }, [offset, limit, refetch]);

  if (isLoading) return <DataTableSkeleton columns={columns.length} />;
  if (error) return <div>Error fetching credit cards</div>;

  const totalCreditCards = creditCardRes?.total || 0;
  const pageCount = Math.ceil(totalCreditCards / limit);
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
          columns={columns}
          data={creditCards}
          pageCount={pageCount}
        />
      </div>
    </PageContainer>
  );
}
