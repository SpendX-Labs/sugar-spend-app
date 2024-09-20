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
import { useLoginMutation } from "@/store/auth/authApi";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter, useSearchParams } from "next/navigation";
import { useForm } from "react-hook-form";
import * as z from "zod";

const loginSchema = z.object({
  username: z.string().min(1, "Username is required"),
  password: z.string().min(6, "Password must be at least 6 characters"),
});

type LoginFormValue = z.infer<typeof loginSchema>;

const LoginForm = () => {
  const [login, { isLoading, data, error }] = useLoginMutation();
  const router = useRouter();
  const searchParams = useSearchParams();
  const callbackUrl = searchParams.get("callbackUrl");
  const form = useForm<LoginFormValue>({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = async (data: { username: string; password: string }) => {
    await login({
      username: data.username,
      password: data.password,
    }).unwrap();
  };

  if (data) router.push(callbackUrl || "/dashboard");

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="w-full space-y-2">
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

        <Button className="ml-auto w-full" type="submit" disabled={isLoading}>
          {isLoading ? "Logging in..." : "Login"}
        </Button>
        {error && <p>Error: Login failed</p>}
      </form>
    </Form>
  );
};

export default LoginForm;
