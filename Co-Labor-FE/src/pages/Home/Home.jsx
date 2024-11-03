import React, { useState, useEffect, useCallback } from 'react';
import MainIntroduce from './components/MainIntroduce';
import MainTitle from './components/MainTitle';
import styled from 'styled-components';
import JobInfo from './components/JobInfo';
import LegalChatInfo from './components/LegalChatInfo';
import MapInfo from './components/MapInfo';
import Contact from './components/Contact';

const Home = () => {
  const [page, setPage] = useState(0);
  const lastPage = 5; // 컨테이너 개수
  const [isThrottled, setIsThrottled] = useState(false);
  useEffect(() => {
    setPage(0);
  }, []);

  const handleScroll = useCallback(
    (e) => {
      e.preventDefault();
      if (!isThrottled) {
        if (e.deltaY > 0 && page < lastPage) {
          setPage((prevPage) => Math.min(prevPage + 1, lastPage));
        } else if (e.deltaY < 0 && page > 0) {
          setPage((prevPage) => Math.max(prevPage - 1, 0));
        }

        setIsThrottled(true);
        setTimeout(() => {
          setIsThrottled(false);
        }, 1000);
      }
    },
    [page, lastPage, isThrottled]
  );

  useEffect(() => {
    window.addEventListener('wheel', handleScroll, { passive: false });
    return () => {
      window.removeEventListener('wheel', handleScroll);
    };
  }, [handleScroll]);

  const handleClickNext = () => {
    //console.log('내려가짐');
    if (page < lastPage) {
      setPage((prevPage) => Math.min(prevPage + 1, lastPage));
    }
  };

  return (
    <Basefiled>
      <Wrap $page={page} $lastPage={lastPage}>
        <Container>
          <MainTitle onClick={handleClickNext} />
        </Container>
        <Container>
          <MainIntroduce onClick={handleClickNext} />
        </Container>
        <Container>
          <JobInfo onClick={handleClickNext} />
        </Container>
        <Container>
          <LegalChatInfo onClick={handleClickNext} />
        </Container>
        <Container>
          <MapInfo onClick={handleClickNext} />
        </Container>
        <Container>
          <Contact />
        </Container>
      </Wrap>
    </Basefiled>
  );
};

export default Home;

const Basefiled = styled.div`
  width: 100vw;
  padding: 0;
`;
const Container = styled.div`
  width: 100%;
  height: 100vh;
`;

const Wrap = styled.div`
  position: relative;
  top: ${({ $page }) => `-${$page * 100}vh`};
  transition: top 1s ease-in-out;
  display: flex;
  flex-direction: column;
  height: ${($lastPage) => `${$lastPage * 100}vh`};
`;
