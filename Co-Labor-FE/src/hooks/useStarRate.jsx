import { useState, useEffect } from "react";

const useStarRate = (averageRate) => {
  const [ratesResArr, setRatesResArr] = useState([0, 0, 0, 0, 0]);
  const STAR_IDX_ARR = ["first", "second", "third", "fourth", "last"];

  const calcStarRates = (AVR_RATE) => {
    let tempStarRatesArr = [0, 0, 0, 0, 0];
    let starVerScore = (AVR_RATE / 5) * 70;
    let idx = 0;
    while (starVerScore > 14) {
      tempStarRatesArr[idx] = 14;
      idx += 1;
      starVerScore -= 14;
    }
    tempStarRatesArr[idx] = starVerScore;
    return tempStarRatesArr;
  };

  useEffect(() => {
    setRatesResArr(calcStarRates(averageRate));
  }, [averageRate]);

  return ratesResArr;
};

export default useStarRate;
