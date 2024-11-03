import React from 'react';
import styled, { keyframes } from 'styled-components';
import { FadeInContainer } from '../../../components/FadeIn';

const ChooseMode = ({
  setOptionCenter,
  moveToCurrentPosition,
  click,
  setClick,
}) => {
  return (
    <FadeInContainer>
      <BaseContainer>
        <Button onClick={moveToCurrentPosition}>🟠 내위치</Button>
        <Button
          onClick={() => {
            setOptionCenter(false);
            setClick(!click);
          }}
        >
          🏬 지원센터
        </Button>
        <Button
          onClick={() => {
            setOptionCenter(true);
          }}
        >
          🏥 병원
        </Button>
      </BaseContainer>{' '}
    </FadeInContainer>
  );
};

export default ChooseMode;

const BaseContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 20px;
  position: relative;
  right: 30vw;
  top: 20px;
`;

const Button = styled.div`
  padding: 10px 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: white;
  border-radius: 25px;
  border: 2px solid #41b365;
  transition: all 0.3s ease-in-out;
  cursor: pointer;
  &:hover {
    border: 2.3px solid #47c770;
    font-size: 16px;
    transform: scale(1.2);
  }
`;
