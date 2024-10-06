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
import { Input } from "@/components/ui/input";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import * as z from "zod";
import { useToast } from "@/components/ui/use-toast";
import { useUserInfoQuery } from "@/store/apis/auth-api";
import {
  useUpdateEmailMutation,
  useVerifyEmailMutation,
} from "@/store/apis/user-api";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../ui/card";
import { Modal } from "../ui/modal";

const formSchema = z.object({
  email: z.string().email("Invalid email address"),
});

type UserEmailFormValues = z.infer<typeof formSchema>;

export const UpdateEmailForm = () => {
  const router = useRouter();
  const { toast } = useToast();
  const [isOtpModalOpen, setIsOtpModalOpen] = useState(false);
  const [otp, setOtp] = useState("");
  const { data: userInfo, isLoading: userInfoLoading } = useUserInfoQuery();
  const [
    updateEmail,
    { isLoading: updateEmailLoading, isSuccess: updateEmailSuccess },
  ] = useUpdateEmailMutation();
  const [
    verifyOtp,
    {
      isLoading: verifyOtpLoading,
      isSuccess: verifyOtpSuccess,
      isError: verifyOtpError,
    },
  ] = useVerifyEmailMutation();

  const form = useForm<UserEmailFormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: userInfo?.email || "",
    },
  });

  useEffect(() => {
    if (userInfo) {
      form.reset({
        email: userInfo?.email || "",
      });
    }
  }, [userInfo, form]);

  const onSubmit = async (data: UserEmailFormValues) => {
    try {
      await updateEmail({ emailId: data.email }).unwrap();
      setIsOtpModalOpen(true);
    } catch (error: any) {
      toast({
        variant: "destructive",
        title: "Uh oh! Something went wrong.",
        description: "There was a problem updating your email.",
      });
    }
  };

  const handleOtpSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!updateEmailSuccess) return;
    try {
      await verifyOtp({ otp }).unwrap();
      setIsOtpModalOpen(false);
    } catch (error: any) {
      toast({
        variant: "destructive",
        title: "Uh oh! Something went wrong.",
        description: "There was a problem updating your email.",
      });
    }
  };

  useEffect(() => {
    if (updateEmailSuccess) {
      if (verifyOtpSuccess) {
        router.push("/");
      } else {
        setIsOtpModalOpen(true);
      }
    }
  }, [updateEmailSuccess, verifyOtpSuccess]);

  return (
    <>
      <Modal
        title="OTP Verification"
        description="Please enter the OTP sent to your email/phone."
        isOpen={isOtpModalOpen}
        onClose={() => setIsOtpModalOpen(false)}
      >
        <form onSubmit={handleOtpSubmit} className="space-y-2">
          <Input
            type="text"
            placeholder="Enter OTP"
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
            required
          />
          <Button type="submit" disabled={verifyOtpLoading}>
            {verifyOtpLoading ? "Verifying..." : "Verify OTP"}
          </Button>
        </form>
      </Modal>
      <Card>
        <CardHeader>
          <CardTitle>Email</CardTitle>
          <CardDescription>Update your email</CardDescription>
        </CardHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)}>
            <CardContent className="w-full space-y-4">
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Email</FormLabel>
                    <FormControl>
                      <Input
                        type="email"
                        placeholder="Enter your email..."
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </CardContent>
            <CardFooter>
              <Button
                disabled={userInfoLoading || updateEmailLoading}
                className="ml-auto"
                type="submit"
              >
                {updateEmailLoading ? "Saving..." : "Save Changes"}
              </Button>
            </CardFooter>
          </form>
        </Form>
      </Card>
    </>
  );
};
