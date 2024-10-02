import { monthNumberToName } from "@/lib/constants";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface MonthYearState {
  month: string;
  year: number;
}

const initialState: MonthYearState = {
  month: monthNumberToName[new Date().getMonth() + 1],
  year: new Date().getFullYear(),
};

export const monthYearSlice = createSlice({
  name: "monthYear",
  initialState,
  reducers: {
    setMonth: (state, action: PayloadAction<string>) => {
      state.month = action.payload;
    },
    setYear: (state, action: PayloadAction<number>) => {
      state.year = action.payload;
    },
  },
  selectors: {
    selectMonth: (monthYear) => monthYear.month,
    selectYear: (monthYear) => monthYear.year,
  },
});

export const { setMonth, setYear } = monthYearSlice.actions;
export const { selectMonth, selectYear } = monthYearSlice.selectors;
