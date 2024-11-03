import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import styled, { keyframes } from 'styled-components';
import useEmpty from '../../../hooks/useEmpty';
import { FadeInContainer } from '../../../components/FadeIn';

const EnterpriseItem = ({
  photo,
  name,
  address1,
  address2,
  enterprise_id,
  type,
}) => {
  const nav = useNavigate();
  const parms = useParams();
  const isObjEmpty = useEmpty(parms);
  if (!photo) {
    photo = 'https://cdn-icons-png.flaticon.com/512/4091/4091968.png'; //회사 아이콘 제작자: xnimrodx - Flaticon
  }

  const clickHandler = () => {
    if (isObjEmpty) {
      nav(`/Company/${enterprise_id}`);
    } else {
      nav(`/Search/${parms.keyword}/${enterprise_id}`);
    }
  };

  return (
    <FadeInContainer>
      <Container onClick={clickHandler}>
        <CompanyImg $photo={photo} />
        <InfoName>{name}</InfoName>
        <Info> 분류 | {type}</Info>
        <Info>
          주소 | {address1} {address2}
        </Info>
      </Container>
    </FadeInContainer>
  );
};

export default EnterpriseItem;

const Container = styled.div`
  margin: 20px 10px;
  padding: 10px 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
  width: 200px;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
  border: 0px solid #58c179;
  max-width: 200px;
  vertical-align: middle;

  &:hover {
    border: 3px solid #58c179;
    transform: scale(1.1);
  }
`;

const CompanyImg = styled.div`
  width: 100%;
  height: 70px;
  background-size: contain;
  background-position: center;
  background-repeat: no-repeat;
  background-image: url(${(props) => props.$photo});
`;

const InfoName = styled.div`
  font-size: 18px;
  font-weight: bold;
  margin-top: 12px;
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const Info = styled.div`
  margin-top: 8px;
`;
