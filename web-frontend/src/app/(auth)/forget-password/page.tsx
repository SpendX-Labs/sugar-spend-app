import ForgetPasswordForm from "@/components/forms/forget-password-form";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { Suspense } from "react";

export default function ForgetPasswordPage() {
  return (
    <>
      <div className="flex flex-col space-y-2 text-center">
        <h1 className="text-2xl font-semibold tracking-tight">
          Forget Password!
        </h1>
        <p className="text-sm text-muted-foreground">Update your password</p>
      </div>
      <Suspense fallback={<div>Loading...</div>}>
        <ForgetPasswordForm />
      </Suspense>
      <Link href="/login">
        <Button variant="link">{"<- xGo to Login"} </Button>
      </Link>
      <div className="relative">
        <div className="absolute inset-0 flex items-center">
          <span className="w-full border-t" />
        </div>
        <div className="relative flex justify-center text-xs uppercase">
          <span className="bg-background px-2 text-muted-foreground">
            Don't have an account?
          </span>
        </div>
      </div>
      <Link href="/signup">
        <Button className="w-full" variant="outline" type="button">
          Sign Up
        </Button>
      </Link>
    </>
  );
}
