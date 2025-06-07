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
import { BankAccount, CashFlowType, CreditCard, TransactionType } from "@/lib/types";
import { useGetCreditCardsQuery } from "@/store/apis/credit-card-api";
import {
  useAddTransactionMutation,
  useDeleteTransactionMutation,
  useEditTransactionMutation,
} from "@/store/apis/transaction-api";
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
import { useGetBankAccountsQuery } from "@/store/apis/bank-account-api";
import { mergeBankAccountDetails, mergeCreditCardDetails } from "@/lib/utils";

const formSchema = z.object({
  cashFlowId: z.string().optional(),
  transactionType: z
    .string()
    .refine(
      (value) => Object.values(TransactionType).includes(value as TransactionType),
      {
        message: "Invalid transaction type",
      }
    ),
  amount: z.coerce
    .number()
    .positive({ message: "Statement Date must be greater than 0" }),
  transactionDate: z.string().refine((value) => /^\d{4}-\d{2}-\d{2}$/.test(value), {
    message: "Start date should be in the format YYYY-MM-DD",
  }),
  cashFlowType: z
    .string()
    .refine(
      (value) => Object.values(CashFlowType).includes(value as CashFlowType),
      {
        message: "Invalid cashFlow type",
      }
    ),
  note: z.string().default(""),
});

type TransactionFormValues = z.infer<typeof formSchema>;

interface TransactionFormProps {
  id: string | number;
  cashFlowId: string | null;
  amount: string | null;
  transactionDate: string | null;
  cashFlowType: CashFlowType | null;
  transactionType: TransactionType | null;
  note: string | null;
}

export const TransactionForm: React.FC<TransactionFormProps> = ({
  id,
  cashFlowId,
  amount,
  transactionDate,
  cashFlowType,
  transactionType,
  note,
}) => {
  const [addTransaction] = useAddTransactionMutation();
  const [deleteTransaction] = useDeleteTransactionMutation();
  const [editTransaction] = useEditTransactionMutation();
  const { data: bankAccountRes } = useGetBankAccountsQuery({
    offset: 0,
    limit: 10,
  });
  const bankAccounts: BankAccount[] = bankAccountRes?.data || [];
  const { data: creditCardRes } = useGetCreditCardsQuery({
    offset: 0,
    limit: 10,
  });
  const creditCards: CreditCard[] = creditCardRes?.data || [];
  const [selectedTransactionType, setSelectedTransactionType] = useState<TransactionType>(
    transactionType === TransactionType.CREDITCARD
      ? TransactionType.CREDITCARD
      : transactionType === TransactionType.BANK
      ? TransactionType.BANK
      : TransactionType.CASH
  );
  const [selectedCashFlowType, setSelectedCashFlowType] = useState<CashFlowType>(
    cashFlowType === CashFlowType.CREDIT
      ? CashFlowType.CREDIT
      : CashFlowType.DEBIT
  );
  const router = useRouter();
  const { toast } = useToast();
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const initialData =
    id && cashFlowId && amount && transactionDate && cashFlowType && transactionType
      ? {
          id,
          cashFlowId,
          transactionType,
          amount,
          transactionDate,
          cashFlowType,
          note,
        }
      : null;

  const title = initialData ? "Edit Transaction" : "Add Transaction";
  const description = initialData ? "Edit an transaction." : "Add a new transaction";
  const toastMessage = initialData ? "Transaction updated." : "Transaction created.";
  const action = initialData ? "Save changes" : "Add";

  const form = useForm<any>({
    resolver: zodResolver(formSchema),
    defaultValues: initialData || {
      bankName: "",
      transactionName: "",
      transactionType: selectedTransactionType,
      cashFlowType: selectedCashFlowType,
      cashFlowId: "",
      statementDate: null,
      dueDate: null,
      last4Digit: "",
      reason: "",
    },
  });

  const getCashFlowName = (data: TransactionFormValues): string => {
    if (data.cashFlowType === TransactionType.BANK) {
      return (
        bankAccounts.filter(
          (bankAccount) => bankAccount.id === data.cashFlowId
        )?.[0]?.bankName || ""
      );
    }
    if (data.cashFlowType === TransactionType.CREDITCARD) {
      return (
        creditCards.filter(
          (creditCard) => creditCard.id.toString() === data.cashFlowId
        )?.[0]?.creditCardName || ""
      );
    }
    return "";
  };

  const onSubmit = async (data: TransactionFormValues) => {
    try {
      setLoading(true);
      if (initialData) {
        await editTransaction({
          ...data,
          id: Number(initialData.id),
          cashFlowType: selectedCashFlowType,
          transactionType: selectedTransactionType,
          cashFlowDetails: {
            cashFlowId: Number(data.cashFlowId) || null,
            cashFlowName: getCashFlowName(data),
          },
          transactionDate: new Date(data.transactionDate).toISOString(),
        }).unwrap();
      } else {
        await addTransaction({
          ...data,
          transactionType: selectedTransactionType,
          cashFlowDetails: {
            cashFlowId: Number(data.cashFlowId) || null,
            cashFlowName: getCashFlowName(data),
          },
          transactionDate: new Date(data.transactionDate).toISOString(),
        }).unwrap();
      }
      router.refresh();
      router.push(`/transaction`);
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
      await deleteTransaction(initialData?.id ? Number(initialData.id) : 0);
      router.refresh();
      router.push(`/transaction`);
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
              name="cashFlowType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Cashflow Type</FormLabel>
                  <Select
                    disabled={loading}
                    onValueChange={(value) => {
                      field.onChange(value);
                      if(value === "DEBIT") {
                        setSelectedCashFlowType(CashFlowType.DEBIT);
                      } else {
                        setSelectedCashFlowType(CashFlowType.CREDIT);
                      }
                    }}
                    value={field.value}
                    defaultValue={CashFlowType.DEBIT}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue
                          defaultValue={CashFlowType.DEBIT}
                          placeholder="Select Transaction Type"
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
              name="transactionType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Transaction Type</FormLabel>
                  <Select
                    disabled={loading}
                    onValueChange={(value) => {
                      field.onChange(value);
                      if(value === "BANK") {
                        setSelectedTransactionType(TransactionType.BANK);
                      } else if(value === "CREDITCARD") {
                        setSelectedTransactionType(TransactionType.CREDITCARD);
                      } else {
                        setSelectedTransactionType(TransactionType.CASH);
                      }
                    }}
                    value={field.value}
                    defaultValue={TransactionType.BANK}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue
                          defaultValue={TransactionType.BANK}
                          placeholder="Select CashFlow Type"
                        />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {Object.values(TransactionType).map((value) => (
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
              name="cashFlowId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Account Name</FormLabel>
                  {selectedTransactionType === TransactionType.BANK ? (
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
                            key={bankAccount.id}
                            value={bankAccount.id?.toString() || ""}
                          >
                            {mergeBankAccountDetails(bankAccount)}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  ) : (
                    <Select
                      disabled={loading || selectedTransactionType === TransactionType.CASH}
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
                            key={creditCard.id}
                            value={creditCard.id.toString() || ""}
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
              name="transactionDate"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Transaction date</FormLabel>
                  <FormControl>
                    <Input type="date" disabled={loading} {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="note"
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
