import React from 'react';
import styled from 'styled-components';

const MainTitle = ({
  text,
  isReview = false,
  addReview,
  isWrite = false,
  isLogin = false,
  isOwner = false,
  DeleteNotice,
}) => {
  const buttonText = isWrite ? '작성 취소' : '리뷰 작성';
  return (
    <Title>
      <TitleText>{text}</TitleText>
      {isReview && isLogin && (
        <AddReview onClick={addReview} $isWrite={isWrite}>
          {buttonText}
        </AddReview>
      )}
      {isOwner && (
        <AddReview onClick={DeleteNotice} $isWrite={true}>
          채용공고 삭제
        </AddReview>
      )}
    </Title>
  );
};

export default MainTitle;

const Title = styled.div`
  width: 1100px;
  border-bottom: 3px solid #379c57;
  padding-bottom: 15px;
  padding-left: 20px;
  display: flex;
  position: relative;
`;
const TitleText = styled.div`
  font-size: 26px;
  font-weight: bold;
`;
const AddReview = styled.button`
  background-color: white;
  position: absolute;
  font-size: 20px;
  font-weight: 590;
  border: 2px solid
    ${({ $isWrite }) => ($isWrite ? 'rgb(248, 61, 61)' : '#2c88f8')};
  border-radius: 10px;
  color: ${({ $isWrite }) => ($isWrite ? 'rgb(248, 61, 61)' : '#2c88f8')};
  text-align: center;
  padding: 8px 20px;
  bottom: 10px;
  right: 10px;
  transition: all 0.3s ease-in-out;
  &:hover {
    transform: scale(1.15);
    background-color: ${({ $isWrite }) =>
      $isWrite ? 'rgb(248, 61, 61)' : '#2c88f8'};
    color: white;
  }
  &:active {
    transform: scale(1.35);
  }
`;
