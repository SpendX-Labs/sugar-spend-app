import { Breadcrumbs } from "@/components/breadcrumbs";
import PageContainer from "@/components/layout/page-container";
import { BankAccountTable } from "@/components/tables/bank-account-table";

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Bank Account", link: "/bank-account" },
];
export default function page() {
  return (
    <PageContainer>
      <div className="space-y-4">
        <Breadcrumbs items={breadcrumbItems} />
        <BankAccountTable />
      </div>
    </PageContainer>
  );
}
