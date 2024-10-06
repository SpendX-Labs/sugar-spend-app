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
import {
  useForgetPasswordMutation,
  useForgetPasswordVerifyOtpMutation,
} from "@/store/apis/auth-api";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter, useSearchParams } from "next/navigation";
import { useForm } from "react-hook-form";
import * as z from "zod";
import { Modal } from "../ui/modal";
import { useEffect, useState } from "react";
import { passwordSchema } from "@/lib/utils";

const forgetPasswordSchema = z
  .object({
    username: z.string().min(1, "Username or email is required"),
    newPassword: passwordSchema,
    confirmPassword: passwordSchema,
  })
  .refine((data) => data.newPassword === data.confirmPassword, {
    message: "Passwords must match",
    path: ["confirmPassword"],
  });

type ForgetPasswordFormValue = z.infer<typeof forgetPasswordSchema>;

const ForgetPasswordForm = () => {
  const [
    forgetPassword,
    {
      isLoading: forgetPasswordLoading,
      isSuccess: forgetPasswordSuccess,
      isError: forgetPasswordError,
    },
  ] = useForgetPasswordMutation();
  const [
    verifyOtp,
    {
      isLoading: verifyOtpLoading,
      isSuccess: verifyOtpSuccess,
      isError: verifyOtpError,
    },
  ] = useForgetPasswordVerifyOtpMutation();
  const router = useRouter();
  const searchParams = useSearchParams();
  const callbackUrl = searchParams.get("callbackUrl");
  const [isOtpModalOpen, setIsOtpModalOpen] = useState(false);
  const [username, setUsername] = useState<string | undefined>();
  const [otp, setOtp] = useState("");
  const form = useForm<ForgetPasswordFormValue>({
    resolver: zodResolver(forgetPasswordSchema),
  });

  const onSubmit = (data: ForgetPasswordFormValue) => {
    forgetPassword({
      username: data.username,
      password: data.newPassword,
    });
    setUsername(username);
  };

  const handleOtpSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!username) return;

    verifyOtp({
      username,
      otp,
    });
    setIsOtpModalOpen(false);
  };

  useEffect(() => {
    if (forgetPasswordSuccess) {
      if (verifyOtpSuccess) {
        router.push(callbackUrl || "/");
      } else {
        setIsOtpModalOpen(true);
      }
    }
  }, [forgetPasswordSuccess, verifyOtpSuccess]);

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
            Verify OTP
          </Button>
        </form>
      </Modal>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="w-full space-y-2"
        >
          <FormField
            control={form.control}
            name="username"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Username or Email</FormLabel>
                <FormControl>
                  <Input
                    type="text"
                    placeholder="Enter your username or Email..."
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="newPassword"
            render={({ field }) => (
              <FormItem>
                <FormLabel>New Password</FormLabel>
                <FormControl>
                  <Input
                    type="password"
                    placeholder="Enter your new password..."
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="confirmPassword"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Confirm Password</FormLabel>
                <FormControl>
                  <Input
                    type="password"
                    placeholder="Re-enter your new password..."
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <Button
            className="ml-auto w-full"
            type="submit"
            disabled={forgetPasswordLoading}
          >
            {forgetPasswordLoading ? "Verifying user..." : "Get OTP"}
          </Button>
          {forgetPasswordError && <p>Error: Forget Password failed</p>}
        </form>
      </Form>
    </>
  );
};

export default ForgetPasswordForm;
