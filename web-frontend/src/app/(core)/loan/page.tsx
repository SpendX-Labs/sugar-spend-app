import { Breadcrumbs } from "@/components/breadcrumbs";
import PageContainer from "@/components/layout/page-container";
import { LoanTable } from "@/components/tables/loan-table";

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Loan", link: "/loan" },
];
export default function page() {
  return (
    <PageContainer>
      <div className="space-y-4">
        <Breadcrumbs items={breadcrumbItems} />
        <LoanTable />
      </div>
    </PageContainer>
  );
}
