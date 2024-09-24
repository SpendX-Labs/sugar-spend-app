import { Breadcrumbs } from "@/components/breadcrumbs";
import PageContainer from "@/components/layout/page-container";
import { columns } from "@/components/tables/credit-card-table/columns";
import { CreditCardTable } from "@/components/tables/credit-card-table";
import { buttonVariants } from "@/components/ui/button";
import { Heading } from "@/components/ui/heading";
import { Separator } from "@/components/ui/separator";
import { cn } from "@/lib/utils";
import { Plus } from "lucide-react";
import Link from "next/link";
import { CreditCard } from "@/lib/types";
import { creditCardsResponse } from "@/lib/simulates";

const breadcrumbItems = [
  { title: "Dashboard", link: "/dashboard" },
  { title: "Credit Card", link: "/dashboard/credit-card" },
];

type paramsProps = {
  searchParams: {
    [key: string]: string | string[] | undefined;
  };
};

export default async function page({ searchParams }: paramsProps) {
  const page = Number(searchParams.page) || 1;
  const pageLimit = Number(searchParams.limit) || 10;

  const creditCardRes = await creditCardsResponse;
  const totalUsers = creditCardRes.total; //1000
  const pageCount = Math.ceil(totalUsers / pageLimit);
  const creditCard: CreditCard[] = creditCardRes.creditCards || [];

  return (
    <PageContainer>
      <div className="space-y-4">
        <Breadcrumbs items={breadcrumbItems} />

        <div className="flex items-start justify-between">
          <Heading
            title={`Credit Card (${totalUsers})`}
            description="Manage credit cards"
          />

          <Link
            href={"/dashboard/credit-card/new"}
            className={cn(buttonVariants({ variant: "default" }))}
          >
            <Plus className="mr-2 h-4 w-4" /> Add New
          </Link>
        </div>
        <Separator />

        <CreditCardTable
          searchKey="card"
          pageNo={page}
          columns={columns}
          totalUsers={totalUsers}
          data={creditCard}
          pageCount={pageCount}
        />
      </div>
    </PageContainer>
  );
}
