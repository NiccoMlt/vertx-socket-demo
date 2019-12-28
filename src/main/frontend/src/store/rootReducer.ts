import { combineReducers } from '@reduxjs/toolkit';
import counterSlice from './counterSlice';

const rootReducer = combineReducers({
  counter: counterSlice,
});

/** Root Redux store types. */
export type RootState = ReturnType<typeof rootReducer>;

export default rootReducer;
