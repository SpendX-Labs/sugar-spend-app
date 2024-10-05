"use client";

import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Heading } from "@/components/ui/heading";
import { Input } from "@/components/ui/input";
import { Separator } from "@/components/ui/separator";
import { zodResolver } from "@hookform/resolvers/zod";
import { Loan, LoanType } from "@/lib/types";
import { useGetCreditCardsQuery } from "@/store/apis/credit-card-api";
import {
  useAddLoanMutation,
  useEditLoanMutation,
  useDeleteLoanMutation,
} from "@/store/apis/loan-api";
import { Trash } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { useRouter } from "next/navigation";
import * as z from "zod";
import { AlertModal } from "../modal/alert-modal";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../ui/select";
import { useToast } from "../ui/use-toast";
import { Checkbox } from "../ui/checkbox";

enum LenderType {
  CREDIT_CARD = "CreditCard",
  CUSTOM_LENDER = "CustomLender",
}

const formSchema = z.object({
  lenderType: z.enum([LenderType.CREDIT_CARD, LenderType.CUSTOM_LENDER]),
  lenderName: z
    .string()
    .min(1, { message: "Please select a lender name" })
    .optional()
    .or(z.literal("")),
  last4Digit: z.string().length(4).optional().or(z.literal("")),
  creditCardName: z.string().optional(),
  noCostEmi: z.boolean().default(false),
  loanType: z.enum([LoanType.REDUCING, LoanType.OTHER_TYPES]),
  totalAmount: z.coerce
    .number()
    .positive({ message: "Total amount must be greater than 0" }),
  interestRate: z.coerce
    .number()
    .positive({ message: "Interest rate must be greater than 0" }),
  tenure: z.coerce
    .number()
    .positive({ message: "Tenure must be greater than 0" }),
  loanStartDate: z
    .string()
    .refine((value) => /^\d{4}-\d{2}-\d{2}$/.test(value), {
      message: "Loan start date should be in the format YYYY-MM-DD",
    }),
  emiDateOfMonth: z.coerce
    .number()
    .min(1)
    .max(31, { message: "EMI date must be between 1 and 31" }),
});

type LoanFormValues = z.infer<typeof formSchema>;

interface LoanFormProps {
  id: string;
  creditCardId?: number;
  lenderId?: number;
  lenderName?: string;
  creditCardName?: string;
  last4Digit?: string;
  totalAmount?: number;
  loanType?: LoanType;
  interestRate?: number;
  noCostEmi?: string;
  tenure?: number;
  remainingTenure?: number;
  loanStartDate?: string;
  emiDateOfMonth?: number;
  principalAmount?: number;
  interestAmount?: number;
  updateLock?: boolean;
  remainingAmount?: number;
  remainingPrincipalAmount?: number;
  remainingInterestAmount?: number;
  emiAmount?: number;
}

export const LoanForm: React.FC<LoanFormProps> = ({
  id,
  lenderName,
  creditCardName,
  last4Digit,
  totalAmount,
  loanType,
  interestRate,
  noCostEmi,
  tenure,
  loanStartDate,
  emiDateOfMonth,
  interestAmount,
}) => {
  const [addLoan] = useAddLoanMutation();
  const [deleteLoan] = useDeleteLoanMutation();
  const [editLoan] = useEditLoanMutation();
  const { data: creditCardRes } = useGetCreditCardsQuery({
    offset: 0,
    limit: 100,
  });
  const creditCards = creditCardRes?.data || [];
  const router = useRouter();
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [selectedLenderType, setSelectedLenderType] = useState<LenderType>(
    creditCardName ? LenderType.CREDIT_CARD : LenderType.CUSTOM_LENDER
  );
  const { toast } = useToast();

  const isEditing =
    id &&
    totalAmount &&
    loanType &&
    interestRate &&
    tenure &&
    loanStartDate &&
    emiDateOfMonth;
  const title = isEditing ? "Edit Loan" : "Add Loan";
  const toastMessage = isEditing ? "Loan updated." : "Loan created.";
  const action = isEditing ? "Save changes" : "Add";

  const form = useForm<LoanFormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      lenderType: selectedLenderType,
      creditCardName: creditCardName || "",
      lenderName: lenderName || undefined,
      last4Digit: last4Digit || undefined,
      noCostEmi: noCostEmi === "true" ? true : false,
      loanType: loanType || LoanType.REDUCING,
      totalAmount: totalAmount || 0,
      interestRate: interestAmount || 0,
      tenure: tenure || 0,
      loanStartDate: loanStartDate || "",
      emiDateOfMonth: emiDateOfMonth || 0,
    },
  });

  const onSubmit = async (data: LoanFormValues) => {
    try {
      setLoading(true);
      const payload = {
        ...data,
        creditCardId:
          selectedLenderType === LenderType.CREDIT_CARD
            ? creditCards.filter(
                (cc) => cc.creditCardName === data.creditCardName
              )?.[0].id
            : undefined,
        creditCardName:
          selectedLenderType === LenderType.CREDIT_CARD
            ? data.creditCardName
            : undefined,
        lenderName:
          selectedLenderType === LenderType.CUSTOM_LENDER
            ? data.lenderName
            : undefined,
        last4Digit:
          selectedLenderType === LenderType.CUSTOM_LENDER
            ? data.last4Digit
            : undefined,
      };
      if (isEditing) {
        await editLoan({
          ...payload,
          id: Number(id),
        }).unwrap();
      } else {
        await addLoan(payload).unwrap();
      }
      router.refresh();
      router.push(`/loan`);
      toast({ variant: "default", title: toastMessage });
    } catch (error) {
      toast({
        variant: "destructive",
        title: "Uh oh! Something went wrong.",
        description: "There was a problem with your request.",
      });
    } finally {
      setLoading(false);
    }
  };

  const onDelete = async () => {
    try {
      setLoading(true);
      await deleteLoan(id ? Number(id) : 0);
      router.refresh();
      router.push(`/loan`);
    } catch (error) {
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
        onConfirm={onDelete}
        loading={loading}
      />
      <div className="flex items-center justify-between">
        <Heading
          title={title}
          description={isEditing ? "Edit a loan." : "Add a new loan."}
        />
        {isEditing && (
          <Button
            disabled={loading}
            variant="destructive"
            size="sm"
            onClick={() => setOpen(true)}
          >
            <Trash className="h-4 w-4" />
          </Button>
        )}
      </div>
      <Separator />
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="w-full space-y-8"
        >
          <div className="gap-8 md:grid md:grid-cols-3">
            <FormField
              control={form.control}
              name="lenderType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Lender Type</FormLabel>
                  <Select
                    disabled={loading}
                    onValueChange={(value) => {
                      field.onChange(value);
                      setSelectedLenderType(value as LenderType);
                    }}
                    value={field.value}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select Lender Type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {Object.values(LenderType).map((value) => (
                        <SelectItem key={value} value={value}>
                          {value}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
            <div className="col-span-2"></div>
            <FormField
              control={form.control}
              name="lenderName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Lender Name</FormLabel>
                  <FormControl>
                    <Input
                      disabled={
                        loading ||
                        selectedLenderType != LenderType.CUSTOM_LENDER
                      }
                      placeholder="Lender Name"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="last4Digit"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Account Number(Last 4 digit)</FormLabel>
                  <FormControl>
                    <Input
                      disabled={
                        loading ||
                        selectedLenderType !== LenderType.CUSTOM_LENDER
                      }
                      placeholder="Last Four Digit"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="creditCardName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Credit Card Name</FormLabel>
                  <Select
                    disabled={
                      loading || selectedLenderType != LenderType.CREDIT_CARD
                    }
                    onValueChange={field.onChange}
                    value={field.value}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue
                          defaultValue={field.value}
                          placeholder="Select a Credit Card Name"
                        />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {creditCards.map((creditCard) => (
                        <SelectItem
                          key={creditCard.creditCardName}
                          value={creditCard.creditCardName}
                        >
                          {creditCard.creditCardName} ({creditCard.last4Digit})
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="totalAmount"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Total Amount</FormLabel>
                  <FormControl>
                    <Input
                      disabled={loading}
                      placeholder="Total Amount"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="loanType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Loan Type</FormLabel>
                  <Select
                    disabled={loading}
                    onValueChange={field.onChange}
                    value={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select Loan Type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {Object.values(LoanType).map((value) => (
                        <SelectItem key={value} value={value}>
                          {value}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="interestRate"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Interest Rate (%)</FormLabel>
                  <FormControl>
                    <Input
                      disabled={loading}
                      placeholder="Interest Rate"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="noCostEmi"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <Checkbox
                      checked={field.value}
                      onCheckedChange={field.onChange}
                      disabled={loading}
                    />
                  </FormControl>
                  <FormLabel className="ml-4">No Cost EMI</FormLabel>
                  <FormMessage />
                </FormItem>
              )}
            />
            <div className="col-span-2"></div>
            <FormField
              control={form.control}
              name="tenure"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Tenure (Months)</FormLabel>
                  <FormControl>
                    <Input disabled={loading} placeholder="Tenure" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="loanStartDate"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Loan Start Date</FormLabel>
                  <FormControl>
                    <Input type="date" disabled={loading} {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="emiDateOfMonth"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>EMI Date of Month</FormLabel>
                  <FormControl>
                    <Input
                      disabled={loading}
                      placeholder="EMI Date (1-31)"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <Button disabled={loading} type="submit" className="ml-auto min-w-72">
            {loading ? "Loading..." : action}
          </Button>
        </form>
      </Form>
    </>
  );
};
