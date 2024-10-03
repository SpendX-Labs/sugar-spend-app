import TotalExpenseCard from "@/components/cards/total-expense-card";
import AvailableBalanceCard from "@/components/cards/available-balance-card";
import NextMonthDeductionsCard from "@/components/cards/next-month-deductions-card";
import MutualFundsCard from "@/components/cards/mututal-funds-card copy";
import { MonthlyProjectionChart } from "@/components/charts/monthly-projection-chart";
import { DailyExpenseChart } from "@/components/charts/daily-expense-chart";
import { UpcomingDeductionChart } from "@/components/charts/upcoming-deduction-chart";
import PageContainer from "@/components/layout/page-container";
import { MonthYearPicker } from "@/components/month-year-picker";
import { RecentTransactions } from "@/components/recent-transactions";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import AutoDebitExpenseCard from "@/components/cards/auto-debit-expense-card";
import { NextMonthDeductionsTable } from "@/components/tables/next-month-deductions-table";

export default function HomePage() {
  return (
    <PageContainer scrollable={true}>
      <div className="space-y-2">
        <div className="flex items-center justify-between space-y-2">
          <h2 className="text-2xl font-bold tracking-tight">
            Hi, Welcome back 👋
          </h2>
          <div className="hidden items-center space-x-2 md:flex">
            <MonthYearPicker />
            <Button>Download</Button>
          </div>
        </div>
        <Tabs defaultValue="overview" className="space-y-4">
          <TabsList>
            <TabsTrigger value="overview">Overview</TabsTrigger>
            <TabsTrigger value="projection">Analytics</TabsTrigger>
          </TabsList>
          <TabsContent value="overview" className="space-y-4">
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
              <TotalExpenseCard />
              <AutoDebitExpenseCard />
              <AvailableBalanceCard />
              <NextMonthDeductionsCard />
              {/* <MutualFundsCard /> */}
            </div>
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-7">
              <div className="col-span-4">
                <DailyExpenseChart />
              </div>
              <div className="col-span-4 md:col-span-3">
                <UpcomingDeductionChart />
              </div>
              <div className="col-span-4 h-[440px] md:min-h-[200x] md:h-full">
                {/* <MonthlyProjectionChart /> */}
                <NextMonthDeductionsTable />
              </div>
              <div className="col-span-4 md:col-span-3 h-[440px]">
                <RecentTransactions />
              </div>
            </div>
          </TabsContent>
        </Tabs>
      </div>
    </PageContainer>
  );
}
