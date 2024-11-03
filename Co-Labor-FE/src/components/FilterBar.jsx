import { useEffect, useState } from 'react';
import styled from 'styled-components';
import {
  CityOptions,
  Classification,
  CountyOptions,
  Task,
} from './FilterOption';
import FilterIcon from '../assets/icon/filter.svg';
import FilterArrow from '../assets/icon/arrow-down-grey-filter.svg';

const FilterBar = ({ selected, setSelected, handleChange, ifJob }) => {
  useEffect(() => {
    //console.log('Updated selected:', selected);
  }, [selected]);

  const onClear = () => {
    handleChange({ target: { name: 'city', value: '지역' } });
    handleChange({ target: { name: 'county', value: '지역 (시/군)' } });
    handleChange({ target: { name: 'type', value: '기업 분류' } });
    handleChange({ target: { name: 'task', value: '' } });
  };
  return (
    <BaseContainer>
      <FilterIconStyled src={FilterIcon} alt="Filter Icon" onClick={onClear} />
      {ifJob && (
        <FilterBox
          name="task"
          value={selected.task}
          onChange={handleChange}
          options={Task}
        />
      )}
      {!ifJob && (
        <FilterBox
          name="type"
          value={selected.type}
          onChange={handleChange}
          options={Classification}
        />
      )}

      {/* 지역 필터 */}
      <FilterContainer>
        <select name="city" value={selected.city} onChange={handleChange}>
          {CityOptions.map((city) => (
            <option key={city.value} value={city.value}>
              {city.label}
            </option>
          ))}
        </select>
        <FilterArrowStyled src={FilterArrow} alt="Arrow Icon" />
      </FilterContainer>

      {selected.city !== '지역' &&
        selected.counties &&
        selected.counties.length > 0 && (
          <FilterContainer>
            <select
              name="county"
              value={selected.county}
              onChange={handleChange}
            >
              {selected.counties.map((county) => (
                <option key={county} value={county}>
                  {county}
                </option>
              ))}
            </select>
            <FilterArrowStyled src={FilterArrow} alt="Arrow Icon" />
          </FilterContainer>
        )}
    </BaseContainer>
  );
};

export default FilterBar;

const FilterBox = ({ name, value, onChange, options }) => (
  <FilterContainer>
    <select name={name} value={value} onChange={onChange}>
      {options.map((option) => (
        <option key={option.value} value={option.value}>
          {option.name}
        </option>
      ))}
    </select>
    <FilterArrowStyled src={FilterArrow} alt="Arrow Icon" />
  </FilterContainer>
);

const FilterIconStyled = styled.img`
  flex-shrink: 0;
  height: 40px;
  cursor: pointer;
`;

const FilterArrowStyled = styled.img`
  height: 16px;
  width: 16px;
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none; /* 이미지가 선택되지 않도록 */
`;

const BaseContainer = styled.div`
  height: 60px;
  gap: 10px;
  display: flex;
  overflow-x: auto;
  &::-webkit-scrollbar {
    display: none;
  }
`;

const FilterContainer = styled.div`
  position: relative;
  width: auto;
  padding: 8px 16px;
  padding-right: 34px;
  border-radius: 24px;
  height: 40px;
  border: 1px solid #bfbfbf;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;

  select {
    font-size: 16px;
    font-weight: 500;
    background-color: white;
    border: none;
    appearance: none;
    cursor: pointer;
    outline: none;
    padding-right: 24px;
    width: 100%;
  }

  &:hover {
    border: 2px solid #58c179;
    outline: none;
  }
`;
