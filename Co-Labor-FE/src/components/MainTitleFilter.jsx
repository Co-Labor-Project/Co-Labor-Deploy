import React from 'react';
import MainTitle from './MainTitle';
import styled from 'styled-components';
import FilterBar from './FilterBar';
const MainTitleFilter = ({
  text,
  selected,
  setSelected,
  handleChange,
  ifJob,
}) => {
  return (
    <BaseContainer>
      <MainTitle text={text} />

      <FilterBar
        selected={selected}
        setSelected={setSelected}
        handleChange={handleChange}
        ifJob={ifJob}
      />
    </BaseContainer>
  );
};

export default MainTitleFilter;
const BaseContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 20px;
`;
const Filter = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
  width: 1100px;
`;
