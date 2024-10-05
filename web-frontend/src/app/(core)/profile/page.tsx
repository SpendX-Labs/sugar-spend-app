import { Breadcrumbs } from "@/components/breadcrumbs";
import { UserBasicInfoForm } from "@/components/forms/basic-info-form";
import { UpdateEmailForm } from "@/components/forms/update-email-form";
import { UpdatePasswordForm } from "@/components/forms/update-password-form";
import PageContainer from "@/components/layout/page-container";
import { LoanTable } from "@/components/tables/loan-table";

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Profile", link: "/profile" },
];
export default function page() {
  return (
    <PageContainer>
      <div className="space-y-4">
        <Breadcrumbs items={breadcrumbItems} />
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-7">
          <div className="col-span-2">
            <UserBasicInfoForm />
          </div>
          <div className="col-span-4 md:col-span-2">
            <UpdatePasswordForm />
          </div>
          <div className="col-span-3">
            <UpdateEmailForm />
          </div>
        </div>
      </div>
    </PageContainer>
  );
}
