import React from 'react';
import styled from 'styled-components';
export const Arrow = ({ isMain, onClick }) => {
  return <ArrowContainer $isMain={isMain} onClick={onClick}></ArrowContainer>;
};

const bounce = `
  @keyframes bounce {
    0%, 100% {
      transform: translateY(0) rotate(-45deg);
    }
    50% {
      transform: translateY(-10px) rotate(-45deg);
    }
  }
`;
const ArrowContainer = styled.div`
  position: absolute;
  bottom: 5%;
  left: 50%;
  width: 24px;
  height: 24px;
  margin-left: -12px;
  border-left: 2px solid ${({ $isMain }) => ($isMain ? '#fff' : '#000')};
  border-bottom: 2px solid ${({ $isMain }) => ($isMain ? '#fff' : '#000')};
  transform: rotate(-45deg);
  animation: bounce 2s infinite;
  ${bounce}
  cursor: pointer;
`;
export const ArrowWrapper = styled.div`
  pointer-events: auto;
  z-index: 200;
`;
