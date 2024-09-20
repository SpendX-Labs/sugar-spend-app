import SignupForm from "@/components/forms/signup-form";
import { Button } from "@/components/ui/button";
import Link from "next/link";

export default function SignupPage() {
  return (
    <>
      <div className="flex flex-col space-y-2 text-center">
        <h1 className="text-2xl font-semibold tracking-tight">
          Create account!
        </h1>
      </div>
      <SignupForm />
      <div className="relative">
        <div className="absolute inset-0 flex items-center">
          <span className="w-full border-t" />
        </div>
        <div className="relative flex justify-center text-xs uppercase">
          <span className="bg-background px-2 text-muted-foreground">
            Already have an account
          </span>
        </div>
      </div>
      <Link href="/">
        <Button className="w-full" variant="outline" type="button">
          Login
        </Button>
      </Link>
    </>
  );
}
