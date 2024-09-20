import type { Action, ThunkAction } from "@reduxjs/toolkit";
import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { authSlice } from "./auth/authSlice";
import { authApi } from "./auth/authApi";

const rootReducer = combineReducers({
  auth: authSlice.reducer,
  [authApi.reducerPath]: authApi.reducer,
});
export type RootState = ReturnType<AppStore["getState"]>;

export const makeStore = () => {
  return configureStore({
    reducer: rootReducer,
    middleware: (getDefaultMiddleware) => {
      return getDefaultMiddleware().concat(authApi.middleware);
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
