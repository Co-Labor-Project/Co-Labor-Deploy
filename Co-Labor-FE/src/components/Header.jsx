import React, { useState, useContext, useEffect } from 'react';
import styled from 'styled-components';
import searchIcon from '../assets/icon/search_icon.png';
import LogoPhoto from '../assets/logo.jpg';
import { useNavigate } from 'react-router-dom';
import { LoginContext } from '../App';
import { logOut, whoIsIt } from '../apis/login';

const Header = () => {
  const nav = useNavigate();
  const { loginState, setLoginState } = useContext(LoginContext);
  const [searchKeyword, setSearchKeyword] = useState('');

  useEffect(() => {
    whoIsIt(setLoginState);
  }, []);

  const changeInput = (e) => {
    setSearchKeyword(e.target.value);
    //console.log(loginState);
  };

  const searchHandler = () => {
    if (searchKeyword === '') {
      alert('❌ 검색어를 입력해 주세요!');
    } else {
      nav(`/search/${searchKeyword}`);
      setSearchKeyword(''); // 검색 후 입력값 초기화
    }
    //console.log(loginState);
  };

  const keyHandler = (e) => {
    if (e.keyCode === 13) {
      searchHandler();
    }
  };

  // 다른 요소를 클릭할 때 searchKeyword 초기화
  const handleNavigation = (path) => {
    setSearchKeyword(''); // 입력값 초기화
    nav(path);
  };

  return (
    <BaseContiner>
      <Logo onClick={() => handleNavigation('/')}></Logo>
      <IndexContiner onClick={() => handleNavigation('/enterprises')}>
        기업 정보
      </IndexContiner>
      <IndexContiner onClick={() => handleNavigation('/jobNotice')}>
        채용 공고
      </IndexContiner>
      {!loginState.userEnterprise && (
        <IndexContiner onClick={() => handleNavigation('/legalChat')}>
          법률 상담
        </IndexContiner>
      )}
      <IndexContiner onClick={() => handleNavigation('/support')}>
        지원센터
      </IndexContiner>

      <SearchBox>
        <SearchBoxInner
          type="text"
          placeholder="기업 정보와 채용 공고를 검색해보세요!"
          value={searchKeyword} // 검색어 상태를 입력값으로 설정
          onChange={changeInput}
          onKeyDown={keyHandler}
        />
        <SearchBoxIcon src={searchIcon} onClick={searchHandler} />
      </SearchBox>

      <RightWrapper>
        {loginState.userEnterprise && (
          <ApplyButton onClick={() => nav('/JobNoticeApply')} $Apply={true}>
            채용공고 등록
          </ApplyButton>
        )}
        {loginState.userEnterprise && (
          <ApplyButton
            onClick={() => handleNavigation('/EnterpriseApply')}
            $Apply={false}
          >
            기업 등록
          </ApplyButton>
        )}
      </RightWrapper>

      {!loginState.userLogin ? (
        <IndexContiner onClick={() => handleNavigation('/SingIn')}>
          로그인 / 회원가입
        </IndexContiner>
      ) : (
        <IndexContiner onClick={() => logOut(setLoginState)}>
          로그아웃
        </IndexContiner>
      )}
    </BaseContiner>
  );
};
export default Header;
const BaseContiner = styled.div`
  display: flex;
  align-items: center;
  border-bottom: 1px solid rgb(226, 226, 226);
  font-size: 15px;
  padding: 5px 0px;
  z-index: 201;
  position: relative;
  background-color: white;
`;
const Logo = styled.div`
  background-image: url(${LogoPhoto});
  width: 120px;
  height: 70px;
  cursor: pointer;
  background-size: cover;
  background-position: center;
  transition: all 0.5s;
  &:hover {
    width: 140px;
    height: 70px;
  }
`;

const IndexContiner = styled.div`
  display: flex;
  padding: 0px 18px;
  cursor: pointer;
  transition: width 1s, height 1s, border 1s, font-size 0.5s;
  font-weight: bold;
  &:hover {
    font-size: 19px;
  }
`;
const SearchBox = styled.div`
  display: flex;
  padding: 0px 18px;
  transition: width 1s, height 1s, border 1s, font-size 0.5s;
  font-weight: bold;
  border: 3px solid #379c57;
  width: 500px;
  height: 40px;
  border-radius: 20px;
  margin: auto;
  &:focus-within {
    border: 4px solid #58c179;
    width: 600px;
    height: 50px;
  }
`;

const SearchBoxInner = styled.input`
  border: none;
  width: 100%;
  font-size: 18px;
  outline: none;
`;
const SearchBoxIcon = styled.img`
  height: 30px;
  width: 30px;
  margin: auto;
  z-index: 1px;
  cursor: pointer;
`;

const ApplyButton = styled.div`
  color:${({ $Apply }) => ($Apply ? 'rgb(248, 61, 61)' : '#2c88f8')};
  border: 2px solid   color:${({ $Apply }) =>
    $Apply ? 'rgb(248, 61, 61)' : '#2c88f8'};
  ;
  padding: 8px;
  border-radius: 15px;
  height: 55px;
  display: flex;
  align-items: center;
  margin-right: 10px;
  margin-left: 15px;
  font-weight: bold;
  &:hover {
    font-size: 19px;
  }
  transition: width 1s, height 1s, border 1s, font-size 0.5s;
  cursor: pointer;

`;
const RightWrapper = styled.div`
  display: flex;
`;
