import React from 'react';
import styled from 'styled-components';
import useScrollFadeIn from '../../../hooks/fade_in';
import GoToButton from './GoToButton';
import { Arrow, ArrowWrapper } from './Arrow';
import GifInfo from '../../../assets/jobInfo.gif';

const JobInfo = ({ onClick }) => {
  return (
    <BaseContainer>
      <ContentWrapper>
        <LeftContainer>
          <MainContainer />
          <SubWrap>
            <SubContainer />
            <ButtonWrap>
              <GoToButton text="기업정보 바로가기" url="/enterprises" />
              <GoToButton text="채용공고 바로가기" url="/jobNotice" />
            </ButtonWrap>
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

export default JobInfo;
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
const ButtonWrap = styled.div`
  display: flex;
  gap: 2px;
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
        <MainText>기업 정보 & 채용 공고</MainText>
        <div>
          <MainSideText>믿을 수 있는 기업 정보와 일자리,</MainSideText>
          <MainSideText>외국인 근로자들을 위한 채용 기회</MainSideText>
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
          &quot;다양한 기업의 세부 정보와 리뷰 평가를 통해 적합한 회사를
          찾아보세요.
        </SubText>
        <SubText> 경력과 직무, 근무 조건에 맞춘 채용 공고를 확인하고, </SubText>
        <SubText>내게 꼭 맞는 일자리를 빠르게 찾을 수 있습니다.&quot;</SubText>
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
        <Img src={GifInfo} alt="map" />
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
