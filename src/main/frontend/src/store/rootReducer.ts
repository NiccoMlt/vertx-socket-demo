import { combineReducers } from '@reduxjs/toolkit';

const rootReducer = combineReducers({
  // TODO
});

/** Root Redux store types. */
export type RootState = ReturnType<typeof rootReducer>;

export default rootReducer;
