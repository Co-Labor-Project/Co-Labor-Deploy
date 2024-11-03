import React, { useContext, useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { JobContext, CompanyContext, LoginContext } from '../../App';
import JobNotieItem from './components/JobNotieItem';
import MainTitle from '../../components/MainTitle';
import styled from 'styled-components';
import { BackGroundField } from '../../components/CommonStyled';
import BasicInfo from '../Enterprises/components/BasicInfo';
import { Navigation, Pagination } from 'swiper/modules';
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/swiper-bundle.css';
import { whoIsIt } from '../../apis/login';
import { deleteJobNotice } from '../../apis/Notice';
const JobNoticeDetailsCenter = () => {
  const params = useParams();
  const contextData = useContext(JobContext);
  const companyContext = useContext(CompanyContext);
  const jobId = params.job_id;
  const nav = useNavigate();

  const [jobData, setJobData] = useState(null);
  const [EnterpriseData, setEnterpriseData] = useState(null);
  const [displayJobPhoto, setDisplayJobPhoto] = useState('');
  const [relationData, setRelationData] = useState([]);
  const { loginState, setLoginState } = useContext(LoginContext);
  const [isOwner, setIsOwner] = useState(false);

  useEffect(() => {
    whoIsIt(setLoginState);
    //console.log('상세 정보', loginState.userEnterprise);
    const storedUsername = sessionStorage.getItem('username');
    if (jobData && jobData.enterpriseUser) {
      //console.log(jobData.enterpriseUser.enterprise_user_id,storedUsername,loginState.userEnterprise);
      if (
        jobData.enterpriseUser.enterprise_user_id === storedUsername &&
        loginState.userEnterprise
      ) {
        //console.log('진입');
        setIsOwner(true);
      }
    }
  }, [jobData]);

  const DeleteNotice = () => {
    if (jobData) {
      //console.log('딜리트 온클릭 까지 진입은 함', jobData.job_id);
      deleteJobNotice({ jobId: jobData.job_id, nav });
    }
  };

  useEffect(() => {
    const job = contextData.find(
      (item) => String(item.job_id) === String(jobId)
    );
    if (job) {
      setJobData(job);
      const company = companyContext.find(
        (item) =>
          String(item.enterprise_id) === String(job.enterprise.enterprise_id)
      );
      setEnterpriseData(company);

      if (job.imageName) {
        //console.log('job.imageNamejob.imageName', job.imageName);
        const checkImage = async () => {
          const url = `/api/jobs/images/${job.imageName}`;
          try {
            const response = await fetch(url);
            if (response.ok) {
              setDisplayJobPhoto(url);
            } else if (response.status === 404) {
              const fallbackUrl = `/api/jobs/images/${job.imageName}`;
              const fallbackResponse = await fetch(fallbackUrl);
              if (fallbackResponse.ok) {
                setDisplayJobPhoto(fallbackUrl);
              } else {
                setDisplayJobPhoto(
                  `https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8Gn8yBWZsQEVzdXIx-qFWrYYlphEWWnG4Og&s`
                );
              }
            }
          } catch (error) {
            // console.error('Error fetching image:', error);
            setDisplayJobPhoto(
              'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8Gn8yBWZsQEVzdXIx-qFWrYYlphEWWnG4Og&s'
            );
          }
        };

        setDisplayJobPhoto('');
        checkImage();
      } else {
        setDisplayJobPhoto(
          'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8Gn8yBWZsQEVzdXIx-qFWrYYlphEWWnG4Og&s'
        );
      }
    }
  }, [jobId, contextData, companyContext]);

  useEffect(() => {
    if (jobData) {
      const filteredData = contextData
        .filter((item) => item.jobRole === jobData.jobRole)
        .slice(0, 10);
      setRelationData(filteredData);
    }
    // console.log('jobData', jobData);
    //console.log('jobData', jobData);
  }, [jobData, contextData]);

  if (!jobData || !EnterpriseData) {
    return <div>Loading</div>;
  }

  const EnterpriseImg = EnterpriseData.imageName
    ? `api/static/images/${EnterpriseData.imageName}`
    : 'https://cdn-icons-png.flaticon.com/512/4091/4091968.png';

  const EnterpriseDescripton =
    EnterpriseData.description || '기업 설명을 작성해주세요!';

  const description = jobData.description || '';
  return (
    <BackGroundField>
      <MainTitle text={EnterpriseData.name} />
      <BasicInfo
        img={EnterpriseImg}
        name={EnterpriseData.name}
        address1={EnterpriseData.address1}
        address2={EnterpriseData.address2}
        address3={EnterpriseData.address3}
        phone_number={EnterpriseData.phone_number}
        type={EnterpriseData.type || '기업 분류를 작성해주세요!'}
        description={EnterpriseDescripton}
      />

      <MainTitle
        text={jobData.title}
        isOwner={isOwner}
        DeleteNotice={DeleteNotice}
      />

      <Container>
        <DetailsImg src={displayJobPhoto} alt={jobData.title} />
        <DetailsCondition>
          <DetailKey>
            <p>마감 기한</p> <span>{jobData.deadDate}</span>
          </DetailKey>

          <DetailKey>
            <p>연락처</p> <span>{jobData.enterpriseUser.email}</span>
          </DetailKey>
          <DetailKey>
            <p>직무 </p> <span>{jobData.jobRole}</span>
          </DetailKey>
          <DetailKey>
            <p>경력 </p>
            <span>{jobData.experience}</span>
          </DetailKey>
          <DetailKey>
            <p>고용형태 </p> <span>{jobData.employmentType}</span>
          </DetailKey>
          <DetailKey>
            <p>근무지역 </p>
            <span>
              {jobData.address1} {jobData.address2} {jobData.address3}
            </span>
          </DetailKey>
          <DetailKey>
            <p>스킬 </p>
            <span>{jobData.skills}</span>
          </DetailKey>
        </DetailsCondition>
      </Container>

      <Description>
        <Pre dangerouslySetInnerHTML={{ __html: description }} />
      </Description>
      <MainTitle text="연관된 공고" />

      <Container>
        <SwiperWrapper>
          <Swiper
            modules={[Navigation, Pagination]}
            spaceBetween={-40}
            slidesPerView={3}
            navigation
            pagination={{ clickable: true }}
            height={3000}
            width={900}
          >
            {relationData.map((item) => (
              <SwiperSlide key={item.job_id}>
                <JobNotieItem {...item} />
              </SwiperSlide>
            ))}
          </Swiper>
        </SwiperWrapper>
      </Container>
    </BackGroundField>
  );
};

export default JobNoticeDetailsCenter;

const Container = styled.div`
  display: flex;
  align-items: center;
  margin: 20px 0px;
  justify-content: start;
  gap: 170px;
  padding-left: 20px;
`;
const DetailsCondition = styled.div`
  padding: 18px 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
  transition: all 0.1s;
  max-width: 550px;
  flex: 2;
  line-height: 35px;
`;

const DetailKey = styled.div`
  display: flex;

  & > p {
    width: 90px;
    margin-right: 20px;
    font-size: 17px;
    font-weight: 550;
  }
`;

const DetailsImg = styled.img`
  max-width: 400px;
  max-height: 400px;
  flex: 1;
  border-radius: 10px;
`;
const SwiperWrapper = styled.div`
  max-width: 800px;
  display: block;
`;

const Description = styled.div`
  width: 100%;
`;

const Pre = styled.div`
  padding: 0px;
`;
