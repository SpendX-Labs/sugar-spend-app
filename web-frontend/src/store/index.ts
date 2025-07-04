import type { Action, ThunkAction } from "@reduxjs/toolkit";
import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { authApi } from "./apis/auth-api";
import { bankAccountApi } from "./apis/bank-account-api";
import { budgetApi } from "./apis/budget-api";
import { creditCardApi } from "./apis/credit-card-api";
import { transactionApi } from "./apis/transaction-api";
import { authSlice } from "./slices/auth-slice";
import { monthYearSlice } from "./slices/month-year-slice";
import { sidebarSlice } from "./slices/sidebar-slice";
import { loanApi } from "./apis/loan-api";
import { userApi } from "./apis/user-api";

const rootReducer = combineReducers({
  auth: authSlice.reducer,
  monthYear: monthYearSlice.reducer,
  sidebar: sidebarSlice.reducer,
  [authApi.reducerPath]: authApi.reducer,
  [bankAccountApi.reducerPath]: bankAccountApi.reducer,
  [budgetApi.reducerPath]: budgetApi.reducer,
  [creditCardApi.reducerPath]: creditCardApi.reducer,
  [transactionApi.reducerPath]: transactionApi.reducer,
  [loanApi.reducerPath]: loanApi.reducer,
  [userApi.reducerPath]: userApi.reducer,
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
        transactionApi.middleware,
        loanApi.middleware,
        userApi.middleware
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
