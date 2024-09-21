import LoginForm from "@/components/forms/login-form";
import { Button } from "@/components/ui/button";
import Link from "next/link";

export default function LoginPge() {
  return (
    <>
      <div className="flex flex-col space-y-2 text-center">
        <h1 className="text-2xl font-semibold tracking-tight">Welcome!</h1>
        <p className="text-sm text-muted-foreground">Login to your account</p>
      </div>
      <LoginForm />
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
