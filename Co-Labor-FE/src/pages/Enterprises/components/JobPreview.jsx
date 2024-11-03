import { useContext } from 'react';
import { JobContext } from '../../../App';
import { Navigation, Pagination } from 'swiper/modules';
import { Swiper, SwiperSlide } from 'swiper/react';
import { useParams, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
const JobPreview = () => {
  const jobContext = useContext(JobContext);
  const params = useParams();

  const jobData = jobContext.filter(
    (job) =>
      String(job.enterprise.enterprise_id) === String(params.enterprise_id)
  );
  const clickHandler = (jobId) => {
    nav(`/JobNotice/${jobId}`);
  };
  const nav = useNavigate();
  const JobImgName = (job) =>
    job.imageName
      ? `/api/jobs/images/${job.imageName}`
      : 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8Gn8yBWZsQEVzdXIx-qFWrYYlphEWWnG4Og&s';

  return (
    <JobList>
      {jobData.length > 0 ? (
        <Swiper
          modules={[Navigation, Pagination]}
          spaceBetween={-110}
          slidesPerView={3}
          navigation
          pagination={{ clickable: true }}
          width="1000"
        >
          {jobData.map((job) => (
            <SwiperSlide key={job.job_id}>
              <JobItem onClick={() => clickHandler(job.job_id)}>
                <Title>{job.title}</Title>
                <JobImg src={JobImgName(job)} width="100px" alt={job.title} />
                <Content>마감일 : {job.deadDate}</Content>
                <Content>제약 조건 : {job.requirement}</Content>
                <Content>직무 : {job.jobRole}</Content>
                <Content>경력 : {job.experience}</Content>
                <Content>고용형태 : {job.employmentType}</Content>
                <Content>근무지역 : {job.location}</Content>
                <Content>스킬 : {job.skills}</Content>
              </JobItem>
            </SwiperSlide>
          ))}
        </Swiper>
      ) : (
        <Title>등록된 채용 공고가 없습니다.</Title>
      )}
    </JobList>
  );
};

export default JobPreview;

const JobList = styled.div`
  padding: 20px 0px;
  width: 1100px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin: 40px 0px;
`;
const JobItem = styled.div`
  margin: 30px 8px;
  padding: 18px 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
  transition: all 0.1s smooth;
  border: 0px solid #58c179;
  max-width: 250px;
  cursor: pointer;
  border-radius: 8px;
  margin-bottom: 70px;
  height: 440px;
`;
const JobImg = styled.img`
  border-radius: 15px;
  width: 172px;
  height: 124px;
`;
const Title = styled.div`
  margin-bottom: 10px;
  font-size: 20px;
  font-weight: bold;
`;
const Content = styled.div`
  margin-top: 5px;
  font-size: 15px;
  font-weight: 400;
  white-space: normal;
  width: 200px;
`;
