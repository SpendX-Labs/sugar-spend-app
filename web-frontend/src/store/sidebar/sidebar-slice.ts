import { createSlice } from "@reduxjs/toolkit";

export const sidebarSlice = createSlice({
  name: "sidebar",
  initialState: {
    isMinimized: false,
  },
  reducers: {
    toggleSidebar: (state) => {
      state.isMinimized = !state.isMinimized;
    },
  },
  selectors: {
    selectIsMinimized: (sidebar) => sidebar.isMinimized,
  },
});

export const { toggleSidebar } = sidebarSlice.actions;
export const { selectIsMinimized } = sidebarSlice.selectors;
