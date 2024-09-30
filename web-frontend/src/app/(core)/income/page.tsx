import { Breadcrumbs } from "@/components/breadcrumbs";
import PageContainer from "@/components/layout/page-container";
import { IncomeTable } from "@/components/tables/income-table";

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Income", link: "/income" },
];
export default function page() {
  return (
    <PageContainer>
      <div className="space-y-4">
        <Breadcrumbs items={breadcrumbItems} />
        <IncomeTable />
      </div>
    </PageContainer>
  );
}
