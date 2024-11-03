import React from 'react';
import { useParams } from 'react-router-dom';
import SearchOutput from './SearchOutput';
const Search = () => {
  const params = useParams();

  return (
    <div>
      <SearchOutput input={params.keyword} />
    </div>
  );
};

export default Search;
