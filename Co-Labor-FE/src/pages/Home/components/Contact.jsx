import React from 'react';
import styled from 'styled-components';
import useScrollFadeIn from '../../../hooks/fade_in';

import DoHyunImg from '../../../assets/profile/DoHyun.jpg';
import HanUlImg from '../../../assets/profile/HanUl.jpg';
import JunHwaImg from '../../../assets/profile/JunHwa.jpg';
import MoonKiImg from '../../../assets/profile/MoonKi.jpg';

const Contact = () => {
  return (
    <BaseContainer>
      <Title>Contact</Title>
      <Dohyun />
      <JunHwa />
      <HanUl />
      <MoonKi />
    </BaseContainer>
  );
};

export default Contact;

const Dohyun = () => {
  return (
    <ContentContainer>
      <Photo $src={DoHyunImg}></Photo>
      <TextWrapper>
        <Name>김도현</Name>
        <Role>프론트엔드</Role>
      </TextWrapper>
      <ContactWrapper>
        <a href="https://github.com/kdhqwe1030">
          <Icon src="https://skillicons.dev/icons?i=github" />
        </a>
        <a href="mailto:kdhqwe1030@gmail.com">
          <Icon src="https://skillicons.dev/icons?i=gmail" />
        </a>
        <a href="https://instagram.com/no_dohyun">
          <Icon src="https://skillicons.dev/icons?i=instagram" />
        </a>
      </ContactWrapper>
    </ContentContainer>
  );
};
const JunHwa = () => {
  return (
    <ContentContainer>
      <Photo $src={JunHwaImg}></Photo>
      <TextWrapper>
        <Name>조준화</Name>
        <Role>백엔드 </Role>
      </TextWrapper>
      <ContactWrapper>
        <a href="https://github.com/jjj5306">
          <Icon src="https://skillicons.dev/icons?i=github" />
        </a>
        <a href="mailto:jjj5306@naver.com">
          <Icon src="https://skillicons.dev/icons?i=gmail" />
        </a>
        <a href="https://instagram.com/jo__ojun">
          <Icon src="https://skillicons.dev/icons?i=instagram" />
        </a>
      </ContactWrapper>
    </ContentContainer>
  );
};
const HanUl = () => {
  return (
    <ContentContainer>
      <Photo $src={HanUlImg}></Photo>
      <TextWrapper>
        <Name>정한울</Name>
        <Role>백엔드</Role>
      </TextWrapper>
      <ContactWrapper>
        <a href="https://github.com/jho7535">
          <Icon src="https://skillicons.dev/icons?i=github" />
        </a>
        <a href="mailto:jho7535@naver.com">
          <Icon src="https://skillicons.dev/icons?i=gmail" />
        </a>
        <a href="https://instagram.com/00.08.24_jho">
          <Icon src="https://skillicons.dev/icons?i=instagram" />
        </a>
      </ContactWrapper>
    </ContentContainer>
  );
};
const MoonKi = () => {
  return (
    <ContentContainer>
      <Photo $src={MoonKiImg}></Photo>
      <TextWrapper>
        <Name>김문기</Name>
        <Role>백엔드</Role>
      </TextWrapper>
      <ContactWrapper>
        <a href="https://github.com/mk-isos">
          <Icon src="https://skillicons.dev/icons?i=github" />
        </a>
        <a href="mailto:mkisos0316@gmail.com">
          <Icon src="https://skillicons.dev/icons?i=gmail" />
        </a>
        <a href="https://www.instagram.com/mk_isos">
          <Icon src="https://skillicons.dev/icons?i=instagram" />
        </a>
      </ContactWrapper>
    </ContentContainer>
  );
};
const BaseContainer = styled.div`
  width: 100%;
  height: calc(100vh - 81px);
  display: flex;
  align-items: center;
  justify-content: space-evenly;

  background-color: #f5f5f5;
  position: relative;
`;

const ContentContainer = styled.div`
  width: 230px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 20px;
  border: 2px solid #a8a8a8;
  border-radius: 15px;
  padding: 35px 30px;
`;
const Title = styled.div`
  position: absolute;
  top: 5%;
  left: 11%;
  font-size: 34px;
  font-weight: 400;
  color: #191f28;
`;
const Photo = styled.div`
  border-radius: 50%;
  width: 150px;
  height: 150px;
  background-image: ${({ $src }) => `url(${$src})`};
  background-size: cover;
`;
const Name = styled.div`
  font-size: 18px;
  font-weight: 500;
  color: #191f28;

  width: 100%;
  text-align: center;
`;
const Role = styled.div`
  font-size: 16px;
  font-weight: 400;
  color: #191f28;

  width: 100%;
  text-align: center;
`;
const TextWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 5px;
`;
const ContactWrapper = styled.div`
  display: flex;
  gap: 20px;
`;
const Icon = styled.img`
  width: 36px;
  height: 36px;
`;
