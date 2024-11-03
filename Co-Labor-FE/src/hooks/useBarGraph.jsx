// hooks/useBarGraph.js
import { useState, useEffect } from "react";

const useBarGraph = (averageRate) => {
  const [ratesResArr, setRatesResArr] = useState([0, 0, 0, 0, 0]);

  const calcBarRates = (AVR_RATE) => {
    let tempBarRatesArr = [0, 0, 0, 0, 0];
    let fullBars = Math.floor(AVR_RATE);
    let partialBar = (AVR_RATE % 1) * 40; // 40이 전체 막대 길이

    for (let i = 0; i < fullBars; i++) {
      tempBarRatesArr[i] = 40;
    }
    if (fullBars < 5) {
      tempBarRatesArr[fullBars] = partialBar;
    }

    return tempBarRatesArr;
  };

  useEffect(() => {
    setRatesResArr(calcBarRates(averageRate));
  }, [averageRate]);

  return ratesResArr;
};

export default useBarGraph;
