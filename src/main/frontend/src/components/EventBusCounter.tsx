import React, {useState} from "react";

const EventBusCounter: React.FC = () => {
  const [counter, setCounter] = useState(0);

  return (
    <>
      <span>Counter value: {counter}</span>
      <div>
        <input onClick={() => setCounter(counter + 1)} type="button" value="Increment"/>
      </div>
    </>
  );
};

export default EventBusCounter;
