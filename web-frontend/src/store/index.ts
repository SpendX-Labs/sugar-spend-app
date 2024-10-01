import type { Action, ThunkAction } from "@reduxjs/toolkit";
import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { authApi } from "./auth/auth-api";
import { authSlice } from "./auth/auth-slice";
import { creditCardApi } from "./credit-card/credit-card-api";
import { expenseApi } from "./expense/expense-api";
import { sidebarSlice } from "./sidebar/sidebar-slice";
import { bankAccountApi } from "./bank-account/bank-account-api";
import { incomeApi } from "./income/income-api";
import { budgetApi } from "./budget/budget-api";

const rootReducer = combineReducers({
  auth: authSlice.reducer,
  sidebar: sidebarSlice.reducer,
  [authApi.reducerPath]: authApi.reducer,
  [bankAccountApi.reducerPath]: bankAccountApi.reducer,
  [budgetApi.reducerPath]: budgetApi.reducer,
  [creditCardApi.reducerPath]: creditCardApi.reducer,
  [expenseApi.reducerPath]: expenseApi.reducer,
  [incomeApi.reducerPath]: incomeApi.reducer,
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
        incomeApi.middleware
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
