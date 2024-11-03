import React from 'react';
import styled from 'styled-components';
const SupportCenterItem = ({ name, address, phone }) => {
  return (
    <BaseContainer>
      <TextWrapper>
        <TextItem>이름</TextItem>
        <ContentText> {name}</ContentText>
      </TextWrapper>
      <TextWrapper>
        <TextItem>주소</TextItem>
        <ContentText> {address}</ContentText>
      </TextWrapper>
      <TextWrapper>
        <TextItem>전화번호</TextItem>
        <ContentText> {phone}</ContentText>
      </TextWrapper>
    </BaseContainer>
  );
};

export default SupportCenterItem;

const BaseContainer = styled.div`
  border-radius: 15px;
  padding: 10px 25px;
  box-shadow: 0px 14px 24px rgba(0, 0, 0, 0.08);
  background-color: white;
  margin: 10px 10px;
  transition: all 0.3s ease-in;
  border: 0px solid var(--primary-color);
  cursor: pointer;

  &:hover {
    margin: 10px 0px;
    padding: 15px 15px;
    border: 2px solid var(--primary-color);
  }
`;

const ContentText = styled.div`
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const TextWrapper = styled.div`
  display: flex;
  gap: 15px;
`;
const TextItem = styled.div`
  width: 80px;
  font-weight: 600;
`;
