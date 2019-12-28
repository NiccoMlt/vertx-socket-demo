import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { increment } from '../store/counterSlice';
import { RootState } from '../store/rootReducer';

const EventBusCounter: React.FC = () => {
  const counter = useSelector((state: RootState) => state.counter);
  const dispatch = useDispatch();

  return (
    <>
      <span>
        Counter value:&nbsp;
        {counter}
      </span>
      <div>
        <input onClick={() => dispatch(increment())} type="button" value="Increment" />
      </div>
    </>
  );
};

export default EventBusCounter;
