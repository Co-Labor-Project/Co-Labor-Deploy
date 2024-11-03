import JobNotieItem from './JobNotieItem';
import styled from 'styled-components';
const JobNoticeList = ({
  data,
  searchNull = { enterprises: false, jobs: false },
  JobData,
}) => {
  const jobData = Array.isArray(data) && data.length > 0 ? data : JobData;
  // console.log(jobData);
  return (
    <>
      {!searchNull.jobs && (
        <ListContainer>
          {jobData.map((item) => (
            <JobNotieItem key={item.job_id} {...item} />
          ))}
        </ListContainer>
      )}
      {searchNull.jobs && (
        <IsNullNotice>
          <Text>등록된 채용공고가 없습니다!</Text>
        </IsNullNotice>
      )}
    </>
  );
};

export default JobNoticeList;

const ListContainer = styled.div`
  padding: 20px 0px;
  width: 100%;
  max-width: 1200px;
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  justify-content: center;
  margin: 0 auto;
`;

const IsNullNotice = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 150px;
`;
const Text = styled.div`
  font-size: 20px;
  font-weight: bold;
`;
