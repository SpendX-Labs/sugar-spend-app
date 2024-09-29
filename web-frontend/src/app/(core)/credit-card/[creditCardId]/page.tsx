import { Breadcrumbs } from "@/components/breadcrumbs";
import { AddCreditCardForm } from "@/components/forms/add-credit-card-form";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FC } from "react";

interface UserPageProps {
  params: {
    creditCardId: string;
  };
}

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Credit Card", link: "/credit-card" },
  { title: "Add", link: "/credit-card/add" },
];

const Page: FC<UserPageProps> = ({ params }) => {
  const { creditCardId: creditCardId } = params;

  return (
    <ScrollArea className="h-full">
      <div className="flex-1 space-y-4 p-8">
        <Breadcrumbs items={breadcrumbItems} />
        <AddCreditCardForm id={creditCardId} />
      </div>
    </ScrollArea>
  );
};

export default Page;
