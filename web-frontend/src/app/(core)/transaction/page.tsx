import { Breadcrumbs } from "@/components/breadcrumbs";
import PageContainer from "@/components/layout/page-container";
import { TransactionTable } from "@/components/tables/transaction-table";

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Transaction", link: "/transaction" },
];
export default function page() {
  return (
    <PageContainer>
      <div className="space-y-4">
        <Breadcrumbs items={breadcrumbItems} />
        <TransactionTable />
      </div>
    </PageContainer>
  );
}
