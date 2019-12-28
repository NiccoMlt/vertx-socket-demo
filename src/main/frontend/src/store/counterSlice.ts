import { createSlice } from '@reduxjs/toolkit';

const counterSlice = createSlice({
  name: 'editor',
  initialState: 0,
  reducers: {
    increment: (state: number) => state + 1,
  },
});

export const { increment } = counterSlice.actions;

export default counterSlice.reducer;
