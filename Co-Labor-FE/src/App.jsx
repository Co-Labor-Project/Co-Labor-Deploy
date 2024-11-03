import { BrowserRouter, Routes, Route } from 'react-router-dom';

import React, { useEffect, useState, createContext } from 'react';
import styled from 'styled-components';
import { NavermapsProvider } from 'react-naver-maps';

import Header from './components/Header';
import TranslateList from './components/TranslateList';
import Enterprises from './pages/Enterprises/Enterprises';
import Home from './pages/Home/Home';
import Notfound from './pages/Notfound';
import JobNotice from './pages/Jobs/JobNotice';
import LegalChat from './pages/LegalChat/LegalChat';
import Support from './pages/Map/Support';
import Search from './pages/Search/Search';
import EnterpriseParticular from './pages/Enterprises/EnterpriseParticular';
import JobNoticeParticular from './pages/Jobs/JobNoticeParticular';
import SingIn from './pages/Login/SingIn';
import EnterpriseApply from './pages/Apply/EnterpriseApply';
import NoticeApply from './pages/Apply/NoticeApply';

export const JobContext = createContext();
export const CompanyContext = createContext();
export const LoginContext = createContext();
export const ReviewContext = createContext();
export const IsEnrollContext = createContext();

function App() {
  const [jobs, setJobs] = useState([]);
  const [companies, setCompanies] = useState([]);
  const [reviews, setReviews] = useState([]);
  const [loginState, setLoginState] = useState({
    userLogin: false,
    userEnterprise: false,
  });
  const [isEnroll, setIsEnroll] = useState(false);

  useEffect(() => {
    fetch('/api/enterprises')
      .then((response) => response.json())
      .then((data) => {
        setCompanies(data);
      })
      .catch((error) => console.error('Error fetching companies:', error));
  }, []);

  useEffect(() => {
    fetch(`/api/jobs`)
      .then((response) => response.json())
      .then((data) => {
        setJobs(data);
        setIsEnroll(false);
      })
      .catch((error) => console.error('Error fetching jobs:', error));
  }, [isEnroll]);

  useEffect(() => {
    fetch('/api/reviews/all')
      .then((response) => response.json())
      .then((data) => setReviews(data))
      .catch((error) => console.error('Error fetching reviews:', error));
  }, []);

  useEffect(() => {
    const savedUsername = sessionStorage.getItem('username');
    const userType = sessionStorage.getItem('userType');
    if (savedUsername) {
      if (userType === 'enterprise') {
        setLoginState({ userEnterprise: true, userLogin: false });
      } else {
        setLoginState({ userLogin: true, userEnterprise: false });
      }
    }
  }, []);

  return (
    <>
      <NavermapsProvider ncpClientId={`${import.meta.env.VITE_MAP_ID}`}>
        <LoginContext.Provider value={{ loginState, setLoginState }}>
          <JobContext.Provider value={jobs}>
            <IsEnrollContext.Provider value={{ isEnroll, setIsEnroll }}>
              <CompanyContext.Provider value={companies}>
                <ReviewContext.Provider value={reviews}>
                  <BrowserRouter>
                    <Header />
                    <Routes>
                      <Route path="/" element={<Home />} />
                      <Route path="/enterprises" element={<Enterprises />} />
                      <Route path="/jobNotice" element={<JobNotice />} />
                      <Route path="/legalChat" element={<LegalChat />} />
                      <Route path="/support" element={<Support />} />
                      <Route path="/SingIn" element={<SingIn />} />

                      <Route path="/Search/:keyword" element={<Search />} />
                      <Route
                        path="/Search/:keyword/:enterprise_id"
                        element={<EnterpriseParticular />}
                      />
                      <Route
                        path="/Company/:enterprise_id"
                        element={<EnterpriseParticular />}
                      />
                      <Route
                        path="/Search/:keyword/jobNotice/:job_id"
                        element={<JobNoticeParticular />}
                      />
                      <Route
                        path="/JobNotice/:job_id"
                        element={<JobNoticeParticular />}
                      />

                      <Route
                        path="/EnterpriseApply"
                        element={<EnterpriseApply />}
                      />
                      <Route path="/JobNoticeApply" element={<NoticeApply />} />
                      <Route path="*" element={<Notfound />} />
                    </Routes>
                    <TraslateButton>
                      <TranslateList />
                    </TraslateButton>
                  </BrowserRouter>
                </ReviewContext.Provider>
              </CompanyContext.Provider>
            </IsEnrollContext.Provider>
          </JobContext.Provider>
        </LoginContext.Provider>
      </NavermapsProvider>
    </>
  );
}

export default App;

const TraslateButton = styled.div`
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 200px;
`;
