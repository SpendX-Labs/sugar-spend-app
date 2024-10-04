import type { Action, ThunkAction } from "@reduxjs/toolkit";
import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { authApi } from "./apis/auth-api";
import { bankAccountApi } from "./apis/bank-account-api";
import { budgetApi } from "./apis/budget-api";
import { creditCardApi } from "./apis/credit-card-api";
import { expenseApi } from "./apis/expense-api";
import { incomeApi } from "./apis/income-api";
import { authSlice } from "./slices/auth-slice";
import { monthYearSlice } from "./slices/month-year-slice";
import { sidebarSlice } from "./slices/sidebar-slice";
import { loanApi } from "./apis/loan-api";

const rootReducer = combineReducers({
  auth: authSlice.reducer,
  monthYear: monthYearSlice.reducer,
  sidebar: sidebarSlice.reducer,
  [authApi.reducerPath]: authApi.reducer,
  [bankAccountApi.reducerPath]: bankAccountApi.reducer,
  [budgetApi.reducerPath]: budgetApi.reducer,
  [creditCardApi.reducerPath]: creditCardApi.reducer,
  [expenseApi.reducerPath]: expenseApi.reducer,
  [incomeApi.reducerPath]: incomeApi.reducer,
  [loanApi.reducerPath]: loanApi.reducer,
});
export type RootState = ReturnType<AppStore["getState"]>;

export const makeStore = () => {
  return configureStore({
    reducer: rootReducer,
    middleware: (getDefaultMiddleware) => {
      return getDefaultMiddleware().concat(
        authApi.middleware,
        bankAccountApi.middleware,
        budgetApi.middleware,
        creditCardApi.middleware,
        expenseApi.middleware,
        incomeApi.middleware,
        loanApi.middleware
      );
    },
  });
};

export type AppStore = ReturnType<typeof makeStore>;
export type AppDispatch = AppStore["dispatch"];
export type AppThunk<ThunkReturnType = void> = ThunkAction<
  ThunkReturnType,
  RootState,
  unknown,
  Action
>;
