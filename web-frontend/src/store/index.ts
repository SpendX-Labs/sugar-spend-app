import type { Action, ThunkAction } from "@reduxjs/toolkit";
import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { authSlice } from "./auth/auth-slice";
import { authApi } from "./auth/auth-api";
import { sidebarSlice } from "./sidebar/sidebar-slice";

const rootReducer = combineReducers({
  auth: authSlice.reducer,
  sidebar: sidebarSlice.reducer,
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
