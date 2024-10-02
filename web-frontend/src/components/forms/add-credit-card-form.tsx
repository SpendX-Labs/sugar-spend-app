"use client";

import * as z from "zod";
import { useState } from "react";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { Trash } from "lucide-react";
import { useRouter, useSearchParams } from "next/navigation";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Separator } from "@/components/ui/separator";
import { Heading } from "@/components/ui/heading";
import { useToast } from "../ui/use-toast";
import {
  useAddCreditCardMutation,
  useDeleteCreditCardMutation,
  useEditCreditCardMutation,
} from "@/store/apis/credit-card-api";
import { AlertModal } from "../modal/alert-modal";

const formSchema = z.object({
  bankName: z
    .string()
    .min(3, { message: "Bank Name must be at least 3 characters" }),
  creditCardName: z
    .string()
    .min(3, { message: "Credit Card Name must be at least 3 characters" }),
  statementDate: z.coerce
    .number()
    .positive({ message: "Statement Date must be greater than 0" })
    .max(28, { message: "Statement Date must be less than or equal to 28" }),
  dueDate: z.coerce
    .number()
    .positive({ message: "Due Date must be greater than 0" })
    .max(28, { message: "Due Date must be less than or equal to 28" }),
  last4Digit: z
    .string()
    .length(4, { message: "Input must be exactly 4 digits" })
    .regex(/^\d{4}$/, { message: "Input must be numeric" }),
});

type AddCreditCardFormValues = z.infer<typeof formSchema>;

interface AddCreditCardFormProps {
  id: string | number;
}

export const AddCreditCardForm: React.FC<AddCreditCardFormProps> = ({ id }) => {
  const [addCreditCard] = useAddCreditCardMutation();
  const [deleteCreditCard] = useDeleteCreditCardMutation();
  const [editCreditCard] = useEditCreditCardMutation();
  const router = useRouter();
  const { toast } = useToast();
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const searchParams = useSearchParams();
  const bankName = searchParams.get("bankName");
  const creditCardName = searchParams.get("creditCardName");
  const statementDate = searchParams.get("statementDate");
  const dueDate = searchParams.get("dueDate");
  const last4Digit = searchParams.get("last4Digit");
  const initialData =
    bankName &&
    creditCardName &&
    statementDate &&
    dueDate &&
    statementDate &&
    last4Digit
      ? {
          id,
          bankName,
          creditCardName,
          statementDate,
          dueDate,
          last4Digit,
        }
      : null;

  const title = initialData ? "Edit Credit Card" : "Add Credit Card";
  const description = initialData
    ? "Edit a credit card."
    : "Add a new credit card";
  const toastMessage = initialData
    ? "Credit Card updated."
    : "Credit Card created.";
  const action = initialData ? "Save changes" : "Add";

  const defaultValues = initialData
    ? initialData
    : {
        bankName: "",
        creditCardName: "",
        statementDate: null,
        dueDate: null,
        last4Digit: "",
      };

  const form = useForm<any>({
    resolver: zodResolver(formSchema),
    defaultValues,
  });

  const onSubmit = async (data: AddCreditCardFormValues) => {
    try {
      setLoading(true);
      if (initialData) {
        await editCreditCard({
          id: Number(initialData.id),
          ...data,
        }).unwrap();
      } else {
        await addCreditCard(data).unwrap();
      }
      router.refresh();
      router.push(`/credit-card`);
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
      await deleteCreditCard(initialData?.id ? Number(initialData.id) : 0);
      router.refresh();
      router.push(`/credit-card`);
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
              name="bankName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Bank Name</FormLabel>
                  <FormControl>
                    <Input
                      disabled={loading}
                      placeholder="Bank name"
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
                  <FormControl>
                    <Input
                      disabled={loading}
                      placeholder="Credit Card name"
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
                  <FormLabel>Last Four Digit</FormLabel>
                  <FormControl>
                    <Input
                      disabled={loading}
                      placeholder="Last four digits of your card"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="statementDate"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Statement Date</FormLabel>
                  <FormControl>
                    <Input type="number" disabled={loading} {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="dueDate"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Due Date</FormLabel>
                  <FormControl>
                    <Input type="number" disabled={loading} {...field} />
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
