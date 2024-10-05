"use client";

import { AlertModal } from "@/components/modal/alert-modal";
import { LoanModal } from "@/components/modal/loan-modal";
import { Button } from "@/components/ui/button";
import { toast } from "@/components/ui/use-toast";
import { Loan } from "@/lib/types";
import { createQueryString } from "@/lib/utils";
import { useDeleteLoanMutation } from "@/store/apis/loan-api";
import { Edit, Trash, View } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface CellActionProps {
  data: Loan;
}

export const CellAction: React.FC<CellActionProps> = ({ data }) => {
  const [deleteLoan] = useDeleteLoanMutation();
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [open, setOpen] = useState(false);
  const [showLoanDetails, setShowLoanDetails] = useState(false);

  const onConfirm = async () => {
    try {
      setLoading(true);
      if (!data.id) return;
      await deleteLoan(data.id);
      router.refresh();
    } catch (error: any) {
      toast({
        variant: "destructive",
        title: "Uh oh! Something went wrong.",
        description: "There was a problem with your request.",
      });
    } finally {
      setLoading(false);
      setOpen(false);
    }
  };

  return (
    <>
      <AlertModal
        isOpen={open}
        onClose={() => setOpen(false)}
        onConfirm={onConfirm}
        loading={loading}
      />
      <LoanModal
        loan={data}
        isOpen={showLoanDetails}
        onClose={() => setShowLoanDetails(false)}
      />
      <Button
        variant="ghost"
        className="h-8 w-8 p-0"
        onClick={() => setShowLoanDetails(true)}
      >
        <span className="sr-only">View</span>
        <View className="h-4 w-4" />
      </Button>
      <Button
        variant="ghost"
        className="h-8 w-8 p-0"
        onClick={() =>
          router.push(`/loan/${data.id}?${createQueryString(data)}`)
        }
      >
        <span className="sr-only">Update</span>
        <Edit className="h-4 w-4" />
      </Button>
      <Button
        variant="ghost"
        className="h-8 w-8 p-0"
        onClick={() => setOpen(true)}
      >
        <span className="sr-only">Delete</span>
        <Trash className="h-4 w-4" />
      </Button>
    </>
  );
};
