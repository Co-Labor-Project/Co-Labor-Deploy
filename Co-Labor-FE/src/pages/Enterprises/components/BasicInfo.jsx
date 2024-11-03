import React from 'react';
import styled from 'styled-components';
const BasicInfo = ({
  img,
  name,
  address1,
  address2,
  address3,
  phone_number,
  type,
  description,
}) => {
  return (
    <BaseContainer>
      <EnterpriseImg src={img} alt={name} />
      <TextContainer>
        <TextWrapper>
          <TextItem>주소</TextItem>{' '}
          <TextContent>
            {address1} {address2} {address3}
          </TextContent>
        </TextWrapper>
        <TextWrapper>
          <TextItem>전화번호</TextItem>
          <TextContent> {phone_number}</TextContent>
        </TextWrapper>
        <TextWrapper>
          <TextItem>분류</TextItem> <TextContent>{type}</TextContent>
        </TextWrapper>
        <TextWrapper>
          <TextItem>설명 </TextItem> <TextContent>{description}</TextContent>
        </TextWrapper>
      </TextContainer>
    </BaseContainer>
  );
};

export default BasicInfo;

const BaseContainer = styled.div`
  display: flex;
  align-items: center;
  margin: 70px 0px;
  width: 1100px;
  gap: 100px;
  padding-left: 150px;
  padding-right: 80px;
`;

const EnterpriseImg = styled.img`
  width: 150px;
  height: 150px;
`;

const TextContainer = styled.div`
  font-size: 20px;
  line-height: 45px;

  flex-grow: 1;
  padding: 18px 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
  max-width: 1000px;
`;
const TextWrapper = styled.div`
  display: flex;
  gap: 10px;
`;
const TextItem = styled.div`
  font-size: 20px;
  font-weight: 700;
  line-height: 45px;
  width: 100px;
  min-width: 100px;
`;
const TextContent = styled.div`
  font-size: 18px;
  line-height: 45px;
`;
