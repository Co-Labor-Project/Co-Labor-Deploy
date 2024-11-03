import styled, { keyframes } from 'styled-components';

export const BackGroundField = styled.div`
  width: 100%;
  max-width: 1400px;
  display: flex;
  align-items: center;
  flex-direction: column;
  flex-wrap: wrap;
  margin: auto;
  margin-top: 30px;
`;

export const LoadingWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 130px;
  gap: 10px;
`;
const rotate = keyframes`
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  `;

const opacity = keyframes`
    0% {
      opacity: 0;
    }
    50% {
      opacity: 1;
    }
    100% {
      opacity: 0;
    }
  `;

export const LoadingText = styled.div`
  white-space: nowrap;
  animation: ${opacity} 2s linear infinite;
`;
export const LoadingSpinner = styled.div`
  width: 40px;
  height: 40px;
  border: 5px solid #3498db;
  border-top: 5px solid transparent;
  border-radius: 50%;
  animation: ${rotate} 1s linear infinite;
`;
