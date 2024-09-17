import React, { useState } from "react";
import { Grid } from "@mui/material";
import BarChartView from "../components/budgetViewer/BarChartView";
import BudgetViewGrid from "../components/budgetViewer/BudgetViewGrid";
import Numberview from "../components/budgetViewer/Numberview";
import PieChartView from "../components/budgetViewer/PieChartView";
import RecentTrans from "../components/budgetViewer/RecentTrans";

const BudgetViewer = () => {
    const today = new Date();
    today.setMonth(today.getMonth());
    const [selectedMonth, setSelectedMonth] = useState(today.toLocaleString("en-US", { month: "long" }));
    const [selectedYear, setSelectedYear] = useState(today.getFullYear());
    const [isSaved, setSaved] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [disableMonth, setDisableMonth] = useState(false);

    return (
        <div>
            <Grid container paddingTop={6}>
                <Grid item xs={3}>
                    <PieChartView selectedMonth={selectedMonth} setSelectedMonth={setSelectedMonth}
                        selectedYear={selectedYear} setSelectedYear={setSelectedYear}
                        isSaved={isSaved} setSaved={setSaved}
                        isLoading={isLoading} setIsLoading={setIsLoading}
                        disableMonth={disableMonth} setDisableMonth={setDisableMonth} />
                </Grid>
                <Grid item xs={6}>
                    <BarChartView selectedMonth={selectedMonth} setSelectedMonth={setSelectedMonth}
                        selectedYear={selectedYear} setSelectedYear={setSelectedYear}
                        isSaved={isSaved} setSaved={setSaved}
                        isLoading={isLoading} setIsLoading={setIsLoading}
                        disableMonth={disableMonth} setDisableMonth={setDisableMonth} />
                </Grid>
                <Grid item xs={3}>
                    <RecentTrans />
                </Grid>
                <Grid item xs={8}>
                    <BudgetViewGrid selectedMonth={selectedMonth} setSelectedMonth={setSelectedMonth}
                        selectedYear={selectedYear} setSelectedYear={setSelectedYear}
                        isSaved={isSaved} setSaved={setSaved}
                        isLoading={isLoading} setIsLoading={setIsLoading}
                        disableMonth={disableMonth} setDisableMonth={setDisableMonth} />
                </Grid>
                <Grid item xs={4}>
                    <Numberview selectedMonth={selectedMonth} setSelectedMonth={setSelectedMonth}
                        selectedYear={selectedYear} setSelectedYear={setSelectedYear}
                        isSaved={isSaved} setSaved={setSaved}
                        isLoading={isLoading} setIsLoading={setIsLoading}
                        disableMonth={disableMonth} setDisableMonth={setDisableMonth} />
                </Grid>
            </Grid>

        </div>
    );

}

export default BudgetViewer;