// FadeIn 애니메이션 정의
import styled, { keyframes } from 'styled-components';
const fadeIn = keyframes`
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
`;

export const FadeInContainer = styled.div`
  animation: ${fadeIn} 1s ease-in-out;
`;
