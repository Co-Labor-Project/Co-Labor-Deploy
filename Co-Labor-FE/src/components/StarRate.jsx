import React from "react";
import styled from "styled-components";
import useStarRate from "../hooks/useStarRate"; // 커스텀 훅 import

const StarRate = ({ rating }) => {
  const STAR_IDX_ARR = ["first", "second", "third", "fourth", "last"];
  const ratesResArr = useStarRate(rating); // 커스텀 훅 사용

  return (
    <StarRateWrap>
      {STAR_IDX_ARR.map((item, idx) => (
        <span className="star_icon" key={`${item}_${idx}`}>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="40"
            height="39"
            viewBox="0 0 14 13"
            fill="#cacaca"
          >
            <clipPath id={`${item}StarClip`}>
              <rect width={`${ratesResArr[idx]}`} height="39" />
            </clipPath>
            <path
              id={`${item}Star`}
              d="M9,2l2.163,4.279L16,6.969,12.5,10.3l.826,4.7L9,12.779,4.674,15,5.5,10.3,2,6.969l4.837-.69Z"
              transform="translate(-2 -2)"
            />
            <use
              clipPath={`url(#${item}StarClip)`}
              href={`#${item}Star`}
              fill="#8bcc9f"
            />
          </svg>
        </span>
      ))}
    </StarRateWrap>
  );
};

export default StarRate;

const StarRateWrap = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  .star_icon {
    display: inline-flex;
    margin-right: 5px;
    margin-top: 13px;
  }
`;
