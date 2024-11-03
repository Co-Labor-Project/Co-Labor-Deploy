import { useState } from 'react';
import styled from 'styled-components';

export const SetStarRate = ({ rating = 0, onRatingChange }) => {
  const STAR_IDX_ARR = ['first', 'second', 'third', 'fourth', 'last'];
  const [currentRating, setCurrentRating] = useState(rating);

  const handleStarClick = (index) => {
    const newRating = index + 1;
    setCurrentRating(newRating);
    onRatingChange(newRating);
  };

  return (
    <WrapContainer>
      <StarRateWrap>
        {STAR_IDX_ARR.map((item, idx) => (
          <span
            className="star_icon"
            key={`${item}_${idx}`}
            onClick={() => handleStarClick(idx)}
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="40"
              height="39"
              viewBox="0 0 14 13"
              fill={idx < currentRating ? '#8bcc9f' : '#cacaca'}
            >
              <path
                d="M9,2l2.163,4.279L16,6.969,12.5,10.3l.826,4.7L9,12.779,4.674,15,5.5,10.3,2,6.969l4.837-.69Z"
                transform="translate(-2 -2)"
              />
            </svg>
          </span>
        ))}
      </StarRateWrap>
      <Score>{currentRating}</Score>
    </WrapContainer>
  );
};

export const SetBarGraph = ({ rating = 0, onRatingChange }) => {
  const Bar_IDX_ARR = ['first', 'second', 'third', 'fourth', 'last'];
  const [currentRating, setCurrentRating] = useState(rating);

  const handleBarClick = (index) => {
    const newRating = index + 1;
    setCurrentRating(newRating);
    onRatingChange(newRating);
  };

  return (
    <BarWrapContainer>
      <BarGraphWrap>
        {Bar_IDX_ARR.map((item, idx) => (
          <span
            className="bar_icon"
            key={`${item}_${idx}`}
            onClick={() => handleBarClick(idx)}
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="40"
              height="10"
              viewBox="0 0 40 10"
            >
              <rect width="40" height="10" fill="#e0e0e0" />
              <rect
                width={idx < currentRating ? '40' : '0'}
                height="10"
                fill="#8bcc9f"
              />
            </svg>
          </span>
        ))}
      </BarGraphWrap>
      <Score>{currentRating}</Score> {/* 현재 rating 값 표시 */}
    </BarWrapContainer>
  );
};

const BarGraphWrap = styled.div`
  display: flex;
  width: 100%;
  .bar_icon {
    display: inline-flex;
    margin-right: 5px;
    cursor: pointer;
  }
`;

const WrapContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
`;
const Score = styled.div`
  font-size: 18px;
  font-weight: 600;
  text-align: center;
  height: 100%;
  width: 100%;
`;
const BarWrapContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;
const StarRateWrap = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  .star_icon {
    display: inline-flex;
    margin-right: 5px;
    margin-top: 13px;
    cursor: pointer;
  }
`;
