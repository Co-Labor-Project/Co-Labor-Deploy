import React from 'react';
import styled from 'styled-components';
import useScrollFadeIn from '../../../hooks/fade_in';
import GoToButton from './GoToButton';
import { Arrow, ArrowWrapper } from './Arrow';
import LegalChat from '../../../assets/legalChat.gif';

const LegalChatInfo = ({ onClick }) => {
  return (
    <BaseContainer>
      <ContentWrapper>
        <LeftContainer>
          <MainContainer />
          <SubWrap>
            <SubContainer />
            <GoToButton text="법률상담 바로가기" url="/legalChat" />
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

export default LegalChatInfo;
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
        <MainText>법률 상담 챗봇</MainText>
        <div>
          <MainSideText>한국에서의 법적 문제, 이제 손쉽게 상담</MainSideText>
          <MainSideText>
            <span className="notranslate">Co Labor</span> &nbsp;챗봇으로
            상담부터 판결까지
          </MainSideText>
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
          외국인 근로자들이 직장에서 마주할 수 있는 법적 고민을 해결해드립니다.
        </SubText>
        <SubText> 불합리한 상황을 언제든지 챗봇과 상담하고, </SubText>
        <SubText>한국에서의 권리와 의무를 명확히 파악하세요.</SubText>
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
        <Img src={LegalChat} alt="map" />
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
