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
import { useRouter } from "next/navigation";
import * as z from "zod";
import { useToast } from "@/components/ui/use-toast";
import { useUserInfoQuery } from "@/store/apis/auth-api";
import { useUpdateBasicInfoMutation } from "@/store/apis/user-api";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../ui/card";
import { useEffect } from "react";

const formSchema = z.object({
  fullName: z.string().min(1, { message: "Full name is required" }),
  username: z.string().min(1, { message: "Username is required" }),
  phoneNumber: z
    .string()
    .regex(/^\d+$/, "Invalid phone number")
    .min(10, "Phone number must be at least 10 digits"),
});

type UserFormValues = z.infer<typeof formSchema>;

export const UserBasicInfoForm = () => {
  const { data: user, isLoading, isError } = useUserInfoQuery();
  const [updateUser] = useUpdateBasicInfoMutation();
  const router = useRouter();
  const { toast } = useToast();

  const form = useForm<UserFormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      fullName: user?.fullName || "",
      username: user?.username || "",
      phoneNumber: user?.phoneNumber || "",
    },
  });

  useEffect(() => {
    if (user) {
      form.reset({
        fullName: user.fullName || "",
        username: user.username || "",
        phoneNumber: user.phoneNumber || "",
      });
    }
  }, [user, form]);

  const onSubmit = async (data: UserFormValues) => {
    try {
      await updateUser({
        fullName: data.fullName,
        phonenumber: data.phoneNumber,
      }).unwrap();
      router.refresh();
      toast({
        variant: "default",
        title: "Profile updated successfully",
      });
    } catch (error: any) {
      toast({
        variant: "destructive",
        title: "Uh oh! Something went wrong.",
        description: "There was a problem updating your profile.",
      });
    }
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>Basic Info</CardTitle>
        <CardDescription>Update your basic information</CardDescription>
      </CardHeader>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)}>
          <CardContent className="w-full space-y-4">
            <FormField
              control={form.control}
              name="username"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Username</FormLabel>
                  <FormControl>
                    <Input disabled={true} placeholder="Username" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="fullName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Full Name</FormLabel>
                  <FormControl>
                    <Input
                      disabled={isLoading}
                      placeholder="Full Name"
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
          </CardContent>
          <CardFooter>
            <Button disabled={isLoading} className="ml-auto" type="submit">
              Save Changes
            </Button>
          </CardFooter>
        </form>
      </Form>
    </Card>
  );
};
