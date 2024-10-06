import PageSkeleton from "@/components/skeletons/page-skeleton";
import React from "react";

interface Props {}

const loading = () => {
  return <PageSkeleton />;
};

export default loading;
