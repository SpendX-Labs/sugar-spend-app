"use client";

import { Breadcrumbs } from "@/components/breadcrumbs";
import PageContainer from "@/components/layout/page-container";
import { CreditCardTable } from "@/components/tables/credit-card-table";
import React from "react";

const breadcrumbItems = [
  { title: "Dashboard", link: "/" },
  { title: "Credit Card", link: "/credit-card" },
];

export default function page() {
  return (
    <PageContainer>
      <div className="space-y-4">
        <Breadcrumbs items={breadcrumbItems} />
        <CreditCardTable />
      </div>
    </PageContainer>
  );
}
