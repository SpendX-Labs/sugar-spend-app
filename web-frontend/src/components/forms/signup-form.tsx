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
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import * as z from "zod";
import { Modal } from "../ui/modal";
import { useSignupMutation, useVerifyOtpMutation } from "@/store/auth/auth-api";
import { useRouter } from "next/navigation";

const signupSchema = z
  .object({
    username: z.string().min(1, "Username is required"),
    fullname: z.string().min(1, "Full name is required"),
    email: z.string().email("Invalid email address"),
    password: z.string().min(6, "Password must be at least 6 characters"),
    confirmPassword: z
      .string()
      .min(6, "Password must be at least 6 characters"),
    phoneNumber: z
      .string()
      .regex(/^\d+$/, "Invalid phone number")
      .min(10, "Phone number must be at least 10 digits"),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords must match",
    path: ["confirmPassword"],
  });

type SignupFormValue = z.infer<typeof signupSchema>;

const SignupForm = () => {
  const router = useRouter();
  const [isOtpModalOpen, setIsOtpModalOpen] = useState(false);
  const [signupData, setSignupData] = useState<SignupFormValue | undefined>();
  const [otp, setOtp] = useState("");
  const form = useForm<SignupFormValue>({
    resolver: zodResolver(signupSchema),
  });
  const [
    signup,
    {
      isLoading: signupLoading,
      isSuccess: signupSuccess,
      isError: signupError,
    },
  ] = useSignupMutation();
  const [
    verifyOtp,
    {
      isLoading: verifyOtpLoading,
      isSuccess: verifyOtpSuccess,
      isError: verifyOtpError,
    },
  ] = useVerifyOtpMutation();

  const onSubmit = (data: SignupFormValue) => {
    signup({
      username: data.username,
      emailId: data.email,
      fullName: data.fullname,
      password: data.password,
      phoneNumber: data.phoneNumber,
    });
    setSignupData(data);
  };

  useEffect(() => {
    if (signupSuccess) {
      if (verifyOtpSuccess) {
        router.push("/");
      } else {
        setIsOtpModalOpen(true);
      }
    }
  }, [signupSuccess, verifyOtpSuccess]);

  const handleOtpSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!signupData) return;

    verifyOtp({
      username: signupData.username,
      emailId: signupData.email,
      fullName: signupData.fullname,
      password: signupData.password,
      phoneNumber: signupData.phoneNumber,
      otp,
    });
    setIsOtpModalOpen(false);
  };

  return (
    <>
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
                <FormLabel>Username</FormLabel>
                <FormControl>
                  <Input
                    type="text"
                    placeholder="Enter your username..."
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="fullname"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Full Name</FormLabel>
                <FormControl>
                  <Input
                    type="text"
                    placeholder="Enter your full name..."
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

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

          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Password</FormLabel>
                <FormControl>
                  <Input
                    type="password"
                    placeholder="Enter your password..."
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
                    placeholder="Confirm your password..."
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="phoneNumber"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Phone Number</FormLabel>
                <FormControl>
                  <Input
                    type="tel"
                    placeholder="Enter your phone number..."
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
            disabled={signupLoading}
          >
            Sign Up
          </Button>
        </form>
      </Form>
      {/* OTP Modal */}
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
    </>
  );
};

export default SignupForm;
