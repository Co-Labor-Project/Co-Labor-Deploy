import { useState, useContext, useEffect } from 'react';
import EnterprisesList from './components/EnterprisesList';
import { BackGroundField } from '../../components/CommonStyled';
import MainTitleFilter from '../../components/MainTitleFilter';
import { CountyOptions } from '../../components/FilterOption';
import { CompanyContext } from '../../App';

function Company() {
  const EnterprisesData = useContext(CompanyContext);
  //console.log(EnterprisesData);
  const [filteredData, setFilteredData] = useState(EnterprisesData);
  const [selected, setSelected] = useState({
    city: '지역',
    county: '지역 (시/군)',
    counties: CountyOptions['지역'],
    type: '기업 분류',
    task: '',
  });

  useEffect(() => {
    filterData(selected);
  }, [selected, EnterprisesData]);

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

    const filtered = EnterprisesData.filter((item) => {
      return (
        (city === '지역' || item.address1 === city) &&
        (county === '지역 (시/군)' || item.address2 === county) &&
        (type === '기업 분류' || item.type === type) &&
        (task === '' || item.task === task)
      );
    });

    setFilteredData(filtered);
  };

  return (
    <BackGroundField>
      <MainTitleFilter
        text="🏢 기업 정보"
        selected={selected}
        setSelected={setSelected}
        handleChange={handleChange}
        ifJob={false}
      />
      <EnterprisesList EnterpriseData={filteredData} />
    </BackGroundField>
  );
}

export default Company;
