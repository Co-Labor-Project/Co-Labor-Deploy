// components/BarGraph.jsx
import React from "react";
import styled from "styled-components";
import useBarGraph from "../hooks/useBarGraph";

const BarGraph = ({ rating }) => {
  const Bar_IDX_ARR = ["first", "second", "third", "fourth", "last"];
  const ratesResArr = useBarGraph(rating);

  return (
    <BarGraphWrap>
      {Bar_IDX_ARR.map((item, idx) => (
        <span className="bar_icon" key={`${item}_${idx}`}>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="40"
            height="10"
            viewBox="0 0 40 10"
          >
            <rect width="40" height="10" fill="#e0e0e0" />
            <rect width={ratesResArr[idx]} height="10" fill="#8bcc9f" />
          </svg>
        </span>
      ))}
      <span>{rating}</span>
    </BarGraphWrap>
  );
};

export default BarGraph;

const BarGraphWrap = styled.div`
  display: flex;
  width: 100%;
  margin-top: 1px;
  .bar_icon {
    display: inline-flex;
    margin-right: 5px;
    margin-top: 5px;
  }
`;
