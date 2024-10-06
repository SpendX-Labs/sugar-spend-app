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
import { useRedirectAuth } from "@/hooks/use-redirect-auth";
import { passwordSchema } from "@/lib/utils";
import { useLoginMutation } from "@/store/apis/auth-api";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter, useSearchParams } from "next/navigation";
import { useForm } from "react-hook-form";
import * as z from "zod";

const loginSchema = z.object({
  username: z.string().min(1, "Username or email is required"),
  password: passwordSchema,
});

type LoginFormValue = z.infer<typeof loginSchema>;

const LoginForm = () => {
  useRedirectAuth();
  const [login, { isLoading, isSuccess, isError }] = useLoginMutation();
  const router = useRouter();
  const searchParams = useSearchParams();
  const callbackUrl = searchParams.get("callbackUrl");
  const form = useForm<LoginFormValue>({
    resolver: zodResolver(loginSchema),
  });

  if (isSuccess) router.push(callbackUrl || "/");

  const onSubmit = (data: { username: string; password: string }) => {
    login(data);
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="w-full space-y-2">
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
        {isError && <p>Error: Login failed</p>}
      </form>
    </Form>
  );
};

export default LoginForm;
