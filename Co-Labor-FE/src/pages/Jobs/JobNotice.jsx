import JobNoticeList from './components/JobNoticeList';
import MainTitleFilter from '../../components/MainTitleFilter';
import { BackGroundField } from '../../components/CommonStyled';
import { CountyOptions } from '../../components/FilterOption';
import { useEffect, useState } from 'react';
function JobNotice() {
  const [JobData, setJobData] = useState([]);
  const [filteredData, setFilteredData] = useState(JobData);
  const [selected, setSelected] = useState({
    city: '지역',
    county: '지역 (시/군)',
    counties: CountyOptions['지역'],
    type: '기업 분류',
    task: '',
  });
  useEffect(() => {
    fetch(`/api/jobs`)
      .then((response) => response.json())
      .then((data) => {
        setJobData(data);
      })
      .catch((error) => console.error('Error fetching jobs:', error));
  }, []);
  useEffect(() => {
    filterData(selected);
  }, [selected, JobData]);

  const handleChange = (event) => {
    const { name, value } = event.target;

    setSelected((prevState) => {
      let newSelected = {
        ...prevState,
        [name]: value,
      };

      if (name === 'city') {
        newSelected = {
          ...newSelected,
          counties: CountyOptions[value] || ['지역 (시/군)'],
          county: '지역 (시/군)',
        };
      }

      return newSelected;
    });
  };

  const filterData = (newSelected) => {
    const { city, county, type, task } = newSelected;

    const filtered = JobData.filter((item) => {
      return (
        (city === '지역' || item.address1 === city) &&
        (county === '지역 (시/군)' || item.address2 === county) &&
        (type === '기업 분류' || item.type === type) &&
        (task === '' || item.jobRole === task)
      );
    });

    setFilteredData(filtered);
  };
  return (
    <BackGroundField>
      <MainTitleFilter
        text="📢 채용 공고"
        selected={selected}
        setSelected={setSelected}
        handleChange={handleChange}
        ifJob={true}
      />
      <JobNoticeList JobData={filteredData} />
    </BackGroundField>
  );
}

export default JobNotice;
