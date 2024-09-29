import { Breadcrumbs } from "@/components/breadcrumbs";
import PageContainer from "@/components/layout/page-container";
import { ExpenseTable } from "@/components/tables/expense-table";

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Expense", link: "/expense" },
];
export default function page() {
  return (
    <PageContainer>
      <div className="space-y-4">
        <Breadcrumbs items={breadcrumbItems} />
        <ExpenseTable />
      </div>
    </PageContainer>
  );
}
