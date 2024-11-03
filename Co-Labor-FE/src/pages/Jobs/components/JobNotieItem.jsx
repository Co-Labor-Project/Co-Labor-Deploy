import React, { useState, useEffect } from 'react';
import useScrollFadeIn from '../../../hooks/fade_in';
import { useNavigate, useParams } from 'react-router-dom';
import useEmpty from '../../../hooks/useEmpty';
import styled from 'styled-components';
import { FadeInContainer } from '../../../components/FadeIn';

const JobNotieItem = ({
  imageName,
  title,
  requirement,
  enterprise,
  job_id,
  jobRole,
  experience,
  employmentType,
  address1,
  address2,
  address3,
  skills,
  deadDate,
}) => {
  const fadeInProps = useScrollFadeIn('up', 1);
  const nav = useNavigate();
  const parms = useParams();
  const isObjEmpty = useEmpty(parms);
  const name = enterprise?.name || 'No Enterprise Name';
  const defaultImage =
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8Gn8yBWZsQEVzdXIx-qFWrYYlphEWWnG4Og&s';
  const [imageSrc, setImageSrc] = useState(defaultImage);

  useEffect(() => {
    const checkImage = async () => {
      if (!imageName) {
        return;
      }

      const url = `/api/jobs/images/${imageName}`;
      try {
        const response = await fetch(url);
        if (response.ok) {
          setImageSrc(url);
        } else if (response.status === 404) {
          const fallbackUrl = `/api/jobs/images/${imageName}`;
          const fallbackResponse = await fetch(fallbackUrl);
          if (fallbackResponse.ok) {
            setImageSrc(fallbackUrl);
          } else {
            setImageSrc(defaultImage);
          }
        } else {
          setImageSrc(defaultImage);
        }
      } catch (error) {
        setImageSrc(defaultImage);
      }
    };

    checkImage();
  }, [imageName]);

  const clickHandler = () => {
    //console.log(job_id);
    if (
      isObjEmpty ||
      parms.keyword === 'undefined' ||
      parms.keyword === undefined
    ) {
      nav(`/JobNotice/${job_id}`);
    } else {
      nav(`/Search/${parms.keyword}/jobNotice/${job_id}`);
    }
  };

  return (
    <FadeInContainer>
      <BaseContainer onClick={clickHandler}>
        <EnterpriseName>{name}</EnterpriseName>
        <Img src={imageSrc} alt={title} />
        <TextTitle>{title}</TextTitle>
        <Description>
          <TextWrapper>
            <TextItem>마감 기한</TextItem>
            <TextContent> {deadDate}</TextContent>
          </TextWrapper>
          <TextWrapper>
            <TextItem>직무</TextItem> <TextContent>{jobRole}</TextContent>
          </TextWrapper>
          <TextWrapper>
            <TextItem>고용형태 </TextItem>{' '}
            <TextContent>{employmentType}</TextContent>
          </TextWrapper>
          <TextWrapper>
            <TextItem>근무지역 </TextItem>{' '}
            <TextContent>
              {address1}&nbsp;&nbsp;
              {address2}&nbsp;&nbsp;
            </TextContent>
          </TextWrapper>
        </Description>
      </BaseContainer>
    </FadeInContainer>
  );
};

export default JobNotieItem;

const BaseContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 10px;
  margin: 30px 8px;
  padding: 18px 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  border-radius: 15px;
  transition: all 0.1s ease-in-out;
  border: 0px solid #58c179;
  cursor: pointer;
  height: 400px;
  width: 260px;

  &:hover {
    border: 2.5px solid #58c179;
    transform: scale(1.05);
  }
`;
const TextTitle = styled.div`
  margin-top: 3px;
  font-size: 18px;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;
const TextWrapper = styled.div`
  display: flex;
`;
const TextItem = styled.div`
  font-size: 13px;
  font-weight: 600;
  line-height: 25px;
  width: 100px;
  min-width: 100px;
`;
const TextContent = styled.div`
  font-size: 13px;
  line-height: 25px;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;
const EnterpriseName = styled.div`
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #666;
`;

const Imgwrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;
const Img = styled.img`
  border-radius: 12px;
  width: 100%;
  height: 140px;
  max-height: 140px;
`;
const Description = styled.div`
  margin-top: 3px;
`;
