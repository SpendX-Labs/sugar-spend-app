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
import {
  CashFlowDetails,
  CashFlowType,
  BankAccount,
  CreditCard,
} from "@/lib/types";
import { useGetBankAccountsQuery } from "@/store/apis/bank-account-api";
import {
  useAddIncomeMutation,
  useDeleteIncomeMutation,
  useEditIncomeMutation,
} from "@/store/apis/income-api";
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
import { Textarea } from "../ui/textarea";
import { useGetCreditCardsQuery } from "@/store/apis/credit-card-api";
import { mergeBankAccountDetails, mergeCreditCardDetails } from "@/lib/utils";

const formSchema = z.object({
  cashFlowName: z
    .string()
    .min(1, { message: "Please select a account name" })
    .optional(),
  amount: z.coerce
    .number()
    .positive({ message: "Statement Date must be greater than 0" }),
  dateOfEvent: z.string().refine((value) => /^\d{4}-\d{2}-\d{2}$/.test(value), {
    message: "Start date should be in the format YYYY-MM-DD",
  }),
  timeOfEvent: z
    .string()
    .refine((value) => /^([01]\d|2[0-3]):([0-5]\d)$/.test(value), {
      message: "Time should be in the format HH:mm",
    }),
  incomeType: z
    .string()
    .refine(
      (value) => Object.values(CashFlowType).includes(value as CashFlowType),
      {
        message: "Invalid income type",
      }
    ),
  message: z.string().default(""),
});

type IncomeFormValues = z.infer<typeof formSchema>;

interface IncomeFormProps {
  id: string | number;
  cashFlowDetails: CashFlowDetails | null;
  amount: string | null;
  dateOfEvent: string | null;
  timeOfEvent: string | null;
  incomeType: CashFlowType | null;
  message: string | null;
}

export const IncomeForm: React.FC<IncomeFormProps> = ({
  id,
  cashFlowDetails,
  amount,
  dateOfEvent,
  timeOfEvent,
  incomeType,
  message,
}) => {
  const [addIncome] = useAddIncomeMutation();
  const [deleteIncome] = useDeleteIncomeMutation();
  const [editIncome] = useEditIncomeMutation();
  const { data: bankAccountRes } = useGetBankAccountsQuery({
    page: 0,
    size: 100,
  });
  const bankAccounts: BankAccount[] = bankAccountRes?.data || [];
  const { data: creditCardRes } = useGetCreditCardsQuery({
    page: 0,
    size: 100,
  });
  const creditCards: CreditCard[] = creditCardRes?.data || [];
  const [selectedType, setSelectedType] = useState<CashFlowType | string>(
    CashFlowType.BANK
  );
  const router = useRouter();
  const { toast } = useToast();
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const initialData =
    id && cashFlowDetails && amount && dateOfEvent && timeOfEvent && incomeType
      ? {
          id,
          cashFlowDetails,
          amount,
          dateOfEvent,
          timeOfEvent,
          incomeType,
          message,
        }
      : null;

  const title = initialData ? "Edit Income" : "Add Income";
  const description = initialData ? "Edit an income." : "Add a new income";
  const toastMessage = initialData ? "Income updated." : "Income created.";
  const action = initialData ? "Save changes" : "Add";

  const defaultValues = initialData
    ? {
        ...initialData,
        incomeName: initialData.cashFlowDetails.cashFlowName,
      }
    : {
        incomeType: "",
        incomeName: "",
        amount: null,
        dateOfEvent: "",
        timeOfEvent: "",
        message: "",
      };

  const form = useForm<any>({
    resolver: zodResolver(formSchema),
    defaultValues,
  });

  const getCashFlowId = (data: IncomeFormValues): number => {
    if (data.incomeType === CashFlowType.BANK) {
      return (
        bankAccounts.filter(
          (bankAccount) => bankAccount.bankName === data.cashFlowName
        )?.[0]?.id || -1
      );
    }
    if (data.incomeType === CashFlowType.CREDITCARD) {
      return (
        creditCards.filter(
          (creditCard) => creditCard.creditCardName === data.cashFlowName
        )?.[0]?.id || -1
      );
    }
    return -1;
  };

  const onSubmit = async (data: IncomeFormValues) => {
    try {
      setLoading(true);
      if (initialData) {
        await editIncome({
          ...data,
          id: Number(initialData.id),
          cashFlowDetails: {
            cashFlowId: getCashFlowId(data),
            cashFlowName: data.cashFlowName || "",
          },
          dateOfEvent: new Date(data.dateOfEvent).toISOString(),
        }).unwrap();
      } else {
        await addIncome({
          ...data,
          cashFlowDetails: {
            cashFlowId: getCashFlowId(data),
            cashFlowName: data.cashFlowName || "",
          },
          dateOfEvent: new Date(data.dateOfEvent).toISOString(),
        }).unwrap();
      }
      router.refresh();
      router.push(`/income`);
      toast({
        variant: "default",
        title: toastMessage,
      });
    } catch (error: any) {
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
      await deleteIncome(initialData?.id ? Number(initialData.id) : 0);
      router.refresh();
      router.push(`/income`);
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
        onConfirm={onDelete}
        loading={loading}
      />
      <div className="flex items-center justify-between">
        <Heading title={title} description={description} />
        {initialData && (
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
              name="incomeType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Income Type</FormLabel>
                  <Select
                    disabled={loading}
                    onValueChange={(value) => {
                      field.onChange(value);
                      setSelectedType(value);
                    }}
                    value={field.value}
                    defaultValue={CashFlowType.BANK}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue
                          defaultValue={CashFlowType.BANK}
                          placeholder="Select Income Type"
                        />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {Object.values(CashFlowType).map((value) => (
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
              name="cashFlowName"
              disabled={selectedType === CashFlowType.CASH}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Account Name</FormLabel>
                  {selectedType === CashFlowType.BANK ? (
                    <Select
                      disabled={loading}
                      onValueChange={field.onChange}
                      value={field.value}
                      defaultValue={field.value}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue
                            defaultValue={field.value}
                            placeholder="Select a Bank Name"
                          />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {bankAccounts.map((bankAccount) => (
                          <SelectItem
                            key={bankAccount.bankName}
                            value={bankAccount.bankName}
                          >
                            {mergeBankAccountDetails(bankAccount)}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  ) : (
                    <Select
                      disabled={loading || selectedType === CashFlowType.CASH}
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
                            {mergeCreditCardDetails(creditCard)}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  )}
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="amount"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Amount</FormLabel>
                  <FormControl>
                    <Input disabled={loading} placeholder="Amount" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="dateOfEvent"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Income date</FormLabel>
                  <FormControl>
                    <Input type="date" disabled={loading} {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="timeOfEvent"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Income Time</FormLabel>
                  <FormControl>
                    <Input type="time" disabled={loading} {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="message"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Reason</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Description"
                      disabled={loading}
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <Button disabled={loading} className="ml-auto" type="submit">
            {action}
          </Button>
        </form>
      </Form>
    </>
  );
};
