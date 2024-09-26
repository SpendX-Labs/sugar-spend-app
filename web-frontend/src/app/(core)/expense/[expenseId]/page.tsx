import { Breadcrumbs } from "@/components/breadcrumbs";
import { ExpenseForm } from "@/components/forms/expense-form";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FC } from "react";

interface UserPageProps {
  params: {
    expenseId: string;
  };
}

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Credit Card", link: "/expense" },
  { title: "Update", link: "/expense/add" },
];

const Page: FC<UserPageProps> = ({ params }) => {
  const { expenseId: expenseId } = params;

  return (
    <ScrollArea className="h-full">
      <div className="flex-1 space-y-4 p-8">
        <Breadcrumbs items={breadcrumbItems} />
        <ExpenseForm id={expenseId} />
      </div>
    </ScrollArea>
  );
};

export default Page;
