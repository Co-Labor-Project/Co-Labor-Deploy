import React from 'react';
import styled from 'styled-components';
import { FadeInContainer } from '../../../components/FadeIn';
import useScrollFadeIn from '../../../hooks/fade_in';
import { Arrow, ArrowWrapper } from './Arrow';

const MainIntroduce = ({ onClick }) => {
  const fadeInProps = useScrollFadeIn('up', 3);

  return (
    <BaseContainer>
      <div {...fadeInProps}>
        <Wrapper>
          <Text>기업 정보 및 채용 정보, 법률 상담과 지원 센터까지</Text>
          <Text>한국에서의 안정적인 정착을 위한</Text>
          <TextWrap>
            <Text>모든 솔루션을 </Text>&nbsp;&nbsp;
            <Color className="notranslate">&apos;Co Labor&apos; </Color>
            &nbsp;&nbsp;
            <Text>에서 만나보세요</Text>
          </TextWrap>
        </Wrapper>
      </div>
      <ArrowWrapper>
        <Arrow isMain={false} onClick={onClick} />
      </ArrowWrapper>
    </BaseContainer>
  );
};

export default MainIntroduce;

const BaseContainer = styled.div`
  width: 100%;
  height: calc(100vh - 81px);

  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9fafb;
  position: relative;
`;
const Text = styled.div`
  font-size: 32px;
  font-weight: 600;
  line-height: 1.7;
  color: #191f28;
`;
const TextWrap = styled.div`
  display: flex;
  align-items: center;
`;
const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
`;

const Color = styled.span`
  font-size: 40px;
  font-weight: 600;
  /* line-height: 1.7; */
  color: #0d6e47;
`;
