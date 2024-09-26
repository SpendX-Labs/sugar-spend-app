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
import { CreditCard } from "@/lib/types";
import { useGetCreditCardsQuery } from "@/store/credit-card/credit-card-api";
import {
  useAddExpenseMutation,
  useDeleteExpenseMutation,
  useEditExpenseMutation,
} from "@/store/expense/expense-api";
import { Trash } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { useRouter, useSearchParams } from "next/navigation";
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

const formSchema = z.object({
  creditCardName: z.string().min(1, { message: "Please select a credit card" }),
  amount: z.coerce
    .number()
    .positive({ message: "Statement Date must be greater than 0" }),
  expenseDate: z.string().refine((value) => /^\d{4}-\d{2}-\d{2}$/.test(value), {
    message: "Start date should be in the format YYYY-MM-DD",
  }),
  expenseTime: z
    .string()
    .refine((value) => /^([01]\d|2[0-3]):([0-5]\d)$/.test(value), {
      message: "Time should be in the format HH:mm",
    }),
  reason: z.string().default(""),
});

type ExpenseFormValues = z.infer<typeof formSchema>;

interface ExpenseFormProps {
  id: string | number;
}

export const ExpenseForm: React.FC<ExpenseFormProps> = ({ id }) => {
  const [addExpense] = useAddExpenseMutation();
  const [deleteExpense] = useDeleteExpenseMutation();
  const [editExpense] = useEditExpenseMutation();
  const { data: creditCardRes } = useGetCreditCardsQuery({
    page: 0,
    size: 100,
  });
  const creditCards: CreditCard[] = creditCardRes?.data || [];
  const router = useRouter();
  const { toast } = useToast();
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const searchParams = useSearchParams();
  const creditCardName = searchParams.get("creditCardName");
  const amount = searchParams.get("amount");
  const expenseDate = searchParams.get("expenseDate");
  const expenseTime = searchParams.get("expenseTime");
  const reason = searchParams.get("reason");
  const initialData =
    creditCardName && amount && expenseDate && expenseTime
      ? {
          id,
          creditCardName,
          amount,
          expenseDate,
          expenseTime,
          reason,
        }
      : null;

  const title = initialData ? "Edit Credit Card" : "Add Credit Card";
  const description = initialData ? "Edit an expense." : "Add a new expense";
  const toastMessage = initialData ? "Expense updated." : "Expense created.";
  const action = initialData ? "Save changes" : "Add";

  const defaultValues = initialData
    ? initialData
    : {
        bankName: "",
        expenseName: "",
        statementDate: null,
        dueDate: null,
        last4Digit: "",
      };

  const form = useForm<any>({
    resolver: zodResolver(formSchema),
    defaultValues,
  });

  const onSubmit = async (data: ExpenseFormValues) => {
    try {
      setLoading(true);
      if (initialData) {
        await editExpense({
          ...data,
          id: Number(initialData.id),
          expenseDate: new Date(data.expenseDate).toISOString(),
        }).unwrap();
      } else {
        await addExpense({
          ...data,
          expenseDate: new Date(data.expenseDate).toISOString(),
        }).unwrap();
      }
      router.refresh();
      router.push(`/expense`);
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
      await deleteExpense(initialData?.id ? Number(initialData.id) : 0);
      router.refresh();
      router.push(`/expense`);
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
              name="creditCardName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Credit Card</FormLabel>
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
                          placeholder="Select a Credit Card"
                        />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {creditCards.map((creditCard) => (
                        <SelectItem
                          key={creditCard.creditCardName}
                          value={creditCard.creditCardName}
                        >
                          {creditCard.creditCardName}
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
              name="expenseDate"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Expense date</FormLabel>
                  <FormControl>
                    <Input type="date" disabled={loading} {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="expenseTime"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Expense Time</FormLabel>
                  <FormControl>
                    <Input type="time" disabled={loading} {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <div className="col-start-3 row-start-1 row-span-2">
              <FormField
                control={form.control}
                name="reason"
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
          </div>
          <Button disabled={loading} className="ml-auto" type="submit">
            {action}
          </Button>
        </form>
      </Form>
    </>
  );
};
