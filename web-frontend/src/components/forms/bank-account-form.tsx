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
import { BankAccountType } from "@/lib/types";
import {
  useAddBankAccountMutation,
  useDeleteBankAccountMutation,
  useEditBankAccountMutation,
} from "@/store/bank-account/bank-account-api";
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

const formSchema = z.object({
  bankName: z.string().min(1, { message: "Please select a credit card" }),
  accountType: z.nativeEnum(BankAccountType, {
    errorMap: () => ({
      message: "Invalid bank account type",
    }),
  }),
  last4Digit: z.string().length(4),
  debitCardLast4Digit: z.string().length(4),
});

type BankAccountFormValues = z.infer<typeof formSchema>;

interface BankAccountFormProps {
  id: string | number;
  bankName: string | null;
  accountType: BankAccountType | null;
  last4Digit: string | null;
  debitCardLast4Digit: string | null;
}

export const BankAccountForm: React.FC<BankAccountFormProps> = ({
  id,
  bankName,
  accountType,
  last4Digit,
  debitCardLast4Digit,
}) => {
  const [addBankAccount] = useAddBankAccountMutation();
  const [deleteBankAccount] = useDeleteBankAccountMutation();
  const [editBankAccount] = useEditBankAccountMutation();
  const router = useRouter();
  const { toast } = useToast();
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const initialData =
    id && bankName && accountType && last4Digit
      ? {
          id,
          bankName,
          accountType,
          last4Digit,
          debitCardLast4Digit,
        }
      : null;

  const title = initialData ? "Edit Bank Account" : "Add Bank Account";
  const description = initialData
    ? "Edit a bank account."
    : "Add a new bank account";
  const toastMessage = initialData
    ? "Bank account updated."
    : "Bank account created.";
  const action = initialData ? "Save changes" : "Add";

  const defaultValues = initialData
    ? initialData
    : {
        bankName: "",
        accountType: BankAccountType.SAVINGS,
        last4Digit: "",
        debitCardLast4Digit: "",
      };

  const form = useForm<any>({
    resolver: zodResolver(formSchema),
    defaultValues,
  });

  const onSubmit = async (data: BankAccountFormValues) => {
    try {
      setLoading(true);
      if (initialData) {
        await editBankAccount({
          ...data,
          id: Number(initialData.id),
        }).unwrap();
      } else {
        await addBankAccount(data).unwrap();
      }
      router.refresh();
      router.push(`/bank-account`);
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
      await deleteBankAccount(initialData?.id ? Number(initialData.id) : 0);
      router.refresh();
      router.push(`/bank-account`);
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
              name="bankAccountType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Account Type</FormLabel>
                  <Select
                    disabled={loading}
                    onValueChange={field.onChange}
                    value={field.value}
                    defaultValue={BankAccountType.SAVINGS}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue
                          defaultValue={BankAccountType.SAVINGS}
                          placeholder="Select Account Type"
                        />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {Object.values(BankAccountType).map((value) => (
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
              name="bankName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Bank Name</FormLabel>
                  <FormControl>
                    <Input
                      disabled={loading}
                      placeholder="Bank Name"
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
                  <FormLabel>Last 4 Digit (Bank Account)</FormLabel>
                  <FormControl>
                    <Input
                      disabled={loading}
                      placeholder="Account Number LAst 4 Digits"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="debitCardLast4Digit"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Last 4 Digits (Debit Card)</FormLabel>
                  <FormControl>
                    <Input
                      disabled={loading}
                      placeholder="Debit Card Last 4 Digits"
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
