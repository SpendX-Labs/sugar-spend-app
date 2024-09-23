import Link from "next/link";
import { buttonVariants } from "@/components/ui/button";
import { cn, getRandomQuote } from "@/lib/utils";

export default function AuthLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const randomQuote = getRandomQuote();

  return (
    <div className="relative h-screen flex-col items-center justify-center md:grid lg:max-w-none lg:grid-cols-2 lg:px-0">
      <Link
        href="/examples/authentication"
        className={cn(
          buttonVariants({ variant: "ghost" }),
          "absolute right-4 top-4 hidden md:right-8 md:top-8"
        )}
      >
        Login
      </Link>
      <div className="relative hidden h-full flex-col bg-muted p-10 text-white lg:flex dark:border-r">
        <div className="absolute inset-0 bg-zinc-900" />
        <div className="relative z-20 flex items-center text-lg font-medium">
          <svg viewBox="0 0 24 24" fill="currentColor" height="24" width="24">
            <path d="M24 12c0 6.627-5.373 12-12 12S0 18.627 0 12c0-1.826.407-3.555 1.137-5.105a9.784 9.784 0 00-.704 3.591c0 5.434 4.387 6.22 6.254 6.203 2.837-.026 6.154-1.416 8.948-3.991l-.471 2.65c-.106.605.29 1.138.896 1.142h.25c.627 0 1.073-.511 1.186-1.143l1.006-5.662c.12-.628-.293-1.14-.921-1.14h-5.673c-.63 0-1.207.334-1.32.968l-.044.255c-.09.603.33 1.057.931 1.057h2.96a9.48 9.48 0 00-.142.139c-2.04 1.93-4.556 2.988-6.64 2.988-2.08 0-4.41-1.313-4.41-4.269C3.243 3.555 8.99 0 12 0c6.614 0 12 5.373 12 12" />
          </svg>
          <span className="ml-2">SpendX</span>
        </div>
        <div className="relative z-20 mt-auto">
          <blockquote className="space-y-2">
            <p className="text-xl">&ldquo; {randomQuote.quote} &rdquo;</p>
            <footer className="text-sm"> - {randomQuote.author}</footer>
          </blockquote>
        </div>
      </div>
      <div className="flex h-full items-center p-4 lg:p-8">
        <div className="mx-auto flex w-full flex-col justify-center space-y-6 sm:w-[350px]">
          {children}
          <p className="px-8 text-center text-sm text-muted-foreground">
            By clicking continue, you agree to our{" "}
            <Link
              href="/terms"
              className="underline underline-offset-4 hover:text-primary"
            >
              Terms of Service
            </Link>{" "}
            and{" "}
            <Link
              href="/privacy"
              className="underline underline-offset-4 hover:text-primary"
            >
              Privacy Policy
            </Link>
            .
          </p>
        </div>
      </div>
    </div>
  );
}
