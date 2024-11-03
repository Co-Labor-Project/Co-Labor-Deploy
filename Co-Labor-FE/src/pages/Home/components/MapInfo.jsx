import React from 'react';
import styled from 'styled-components';
import useScrollFadeIn from '../../../hooks/fade_in';
import GoToButton from './GoToButton';
import { Arrow, ArrowWrapper } from './Arrow';
import SuuportMap from '../../../assets/suuportMap.gif';

const MapInfo = ({ onClick }) => {
  return (
    <BaseContainer>
      <ContentWrapper>
        <LeftContainer>
          <MainContainer />
          <SubWrap>
            <SubContainer />
            <GoToButton text="지원센터 바로가기" url="/support" />
          </SubWrap>
        </LeftContainer>
        <RightContainer>
          <Example />
        </RightContainer>
        <ArrowWrapper>
          <Arrow isMain={false} onClick={onClick} />
        </ArrowWrapper>
      </ContentWrapper>
    </BaseContainer>
  );
};

export default MapInfo;
const BaseContainer = styled.div`
  width: 100%;
  height: calc(100vh - 81px);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
`;
const SubWrap = styled.div`
  display: flex;
  flex-direction: column;
  gap: 6px;
`;
const ContentWrapper = styled.div`
  width: 80%;
  height: 100%;
  display: flex;
  align-items: center;
`;
const LeftContainer = styled.div`
  width: 40%;
  height: 80%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;
const RightContainer = styled.div`
  width: 60%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
`;
const MainContainer = () => {
  const fadeInProps = useScrollFadeIn('right', 1.5);
  return (
    <div {...fadeInProps}>
      <MainTextWrap>
        <MainText>지원센터 및 병원 위치 서비스</MainText>
        <div>
          <MainSideText>가까운 지원센터와 병원을 한눈에 파악</MainSideText>
          <MainSideText>위치 기반 지도 서비스 제공</MainSideText>
        </div>
      </MainTextWrap>
    </div>
  );
};
const MainTextWrap = styled.div`
  display: flex;
  flex-direction: column;
  gap: 20px;
`;
const MainText = styled.div`
  font-size: 22px;
  font-weight: 600;
  color: #148d7f;
`;
const MainSideText = styled.div`
  font-size: 26px;
  font-weight: 600;
  color: #191f28;
`;
const SubContainer = () => {
  const fadeInProps = useScrollFadeIn('right', 3);

  return (
    <div {...fadeInProps}>
      <SubTextWrap>
        <SubText>
          내 주변에 있는 외국인 근로자 지원센터와 병원을 쉽게 확인하세요.
        </SubText>
        <SubText>
          도움이 필요할 때, 가장 가까운 지원센터와 병원을 찾아보세요.
        </SubText>
      </SubTextWrap>
    </div>
  );
};

const SubText = styled.div`
  color: rgb(107, 118, 132);
`;

const SubTextWrap = styled.div`
  display: flex;
  flex-direction: column;
  gap: 5px;
`;

const Example = () => {
  const fadeInProps = useScrollFadeIn('left', 2);

  return (
    <div {...fadeInProps}>
      <ExampleContainer>
        <Img src={SuuportMap} alt="map" />
      </ExampleContainer>
    </div>
  );
};

const ExampleContainer = styled.div`
  width: 40vw;
  height: 40vh;
  border-radius: 25px;
  border: 3px solid #333;
  box-shadow: 14px 14px 40px rgba(0, 0, 0, 0.2);
`;
const Img = styled.img`
  width: 100%;
  height: 100%;
  border-radius: 25px;
`;
