import React, { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { LoginContext } from '../../App';

import { onCheckRegiNum, submitEnterprise } from '../../apis/apply';
import styled from 'styled-components';
import TextareaAutosize from 'react-textarea-autosize';

const CompanyApply = () => {
  const { loginState, setLoginState } = useContext(LoginContext);
  const [input, setInput] = useState({
    enterprise_id: '',
    name: '',
    address1: '',
    address2: '',
    address3: '',
    type: '',
    phone_number: '',
    description: '',
    imageName: '',
    enterprise_user_id: '',
  });
  const [logoFile, setLogoFile] = useState(null);
  const [page, setPage] = useState(1);
  const nav = useNavigate();

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setInput((prevInput) => ({ ...prevInput, [name]: value }));
    //console.log('inputinput', input);
  };

  const handleFileChange = (e) => {
    setLogoFile(e.target.files[0]);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    submitEnterprise(input, logoFile, nav);
  };
  useEffect(() => {
    const Username = sessionStorage.getItem('username');
    setInput((pre) => ({
      ...pre,
      enterprise_user_id: Username,
    }));
    //console.log('초기 사용자 이름 저장', input);
  }, []);

  return (
    <BaseContainer>
      <Form onSubmit={handleSubmit}>
        {page === 1 && (
          <>
            <InputWrap>
              <InputText>사업자 등록번호</InputText>
              <Input
                type="number"
                name="enterprise_id"
                placeholder="Enterprise ID"
                value={input.enterprise_id}
                onChange={handleInputChange}
              />
              <MinText>▷ &apos;-&apos;를 제외한 숫자만 입력해 주세요.</MinText>
            </InputWrap>

            <Button
              type="button"
              onClick={() => onCheckRegiNum(input.enterprise_id, setPage)}
            >
              다음
            </Button>
          </>
        )}
        {page === 2 && (
          <>
            <ButWrapper>
              <MinButton type="button" onClick={() => setPage(page - 1)}>
                &lt; 이전
              </MinButton>
            </ButWrapper>
            <InputWrap>
              <InputText>회사 이름</InputText>
              <Input
                type="text"
                name="name"
                placeholder="Name"
                value={input.name}
                onChange={handleInputChange}
              />
            </InputWrap>
            <InputWrap>
              <InputText>기업 분류</InputText>
              <Input
                type="text"
                name="type"
                placeholder="type"
                value={input.type}
                onChange={handleInputChange}
              />
            </InputWrap>
            <InputWrap>
              <InputText>전화번호</InputText>
              <Input
                type="text"
                name="phone_number"
                placeholder="phone_number"
                value={input.phone_number}
                onChange={handleInputChange}
              />
            </InputWrap>

            <Button type="button" onClick={() => setPage(page + 1)}>
              다음
            </Button>
          </>
        )}
        {page === 3 && (
          <>
            <ButWrapper>
              <MinButton type="button" onClick={() => setPage(page - 1)}>
                {' '}
                &lt; 이전
              </MinButton>
            </ButWrapper>
            <InputWrap>
              <InputText>시 / 도</InputText>
              <Input
                type="text"
                name="address1"
                placeholder="address1 "
                value={input.address1}
                onChange={handleInputChange}
              />
            </InputWrap>
            <InputWrap>
              <InputText>시/ 군 / 구</InputText>
              <Input
                type="text"
                name="address2"
                placeholder="address2"
                value={input.address2}
                onChange={handleInputChange}
              />
            </InputWrap>
            <InputWrap>
              <InputText>상세 주소</InputText>
              <Input
                type="text"
                name="address3"
                placeholder="address3"
                value={input.address3}
                onChange={handleInputChange}
              />
            </InputWrap>
            <Button type="button" onClick={() => setPage(page + 1)}>
              다음
            </Button>
          </>
        )}
        {page === 4 && (
          <>
            <ButWrapper>
              <MinButton type="button" onClick={() => setPage(page - 1)}>
                &lt; 이전
              </MinButton>
            </ButWrapper>
            <InputWrap>
              <InputText>설명</InputText>
              <TextareaStyled
                minRows={5}
                type="text"
                name="description"
                placeholder="description"
                value={input.description}
                onChange={handleInputChange}
              />
            </InputWrap>
            <InputWrap>
              <InputText>기업 로고 or 사진</InputText>
              <Input
                type="file"
                name="logo"
                accept="image/*"
                onChange={handleFileChange}
              />
            </InputWrap>
            <GuideText>
              ❗제출하신 자료는 Co Labor에서 검토 후, 기업 등록 절차를 완료해
              드리겠습니다.
            </GuideText>
            <Button type="submit">기업 등록하기</Button>
          </>
        )}
      </Form>
    </BaseContainer>
  );
};

export default CompanyApply;

const BaseContainer = styled.div`
  width: 600px;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 50px;
  height: calc(100vh - 112px);
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 30px;
  width: 100%;
`;
const InputWrap = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
`;
const InputText = styled.div`
  font-size: 20px;
  font-weight: 500;
`;
const Input = styled.input`
  height: 50px;
  width: 100%;
  border: 1px solid #b3b3b3;
  border-radius: 15px;
  padding: 5px 15px;
  font-size: 16px;
  &:focus {
    outline: none;
    transform: scale(1.05);
    border: 2px solid #157e36;
  }
  transition: all 0.3s ease-in-out;
`;
const TextareaStyled = styled(TextareaAutosize)`
  resize: none;
  max-height: 150px;
  width: 100%;
  border: 1px solid #b3b3b3;
  border-radius: 15px;
  padding: 5px 15px;
  font-size: 16px;
  &:focus {
    outline: none;
    transform: scale(1.05);
    border: 2px solid #157e36;
  }
  transition: all 0.3s ease-in-out;
`;
const MinText = styled.div`
  font-size: 18px;
  color: #929292;
`;
const ButWrapper = styled.div`
  width: 100;
  display: flex;
  justify-content: flex-start;
`;

const MinButton = styled.button`
  width: 80px;
  height: 40px;
  background-color: #e7e7e7;
  border-radius: 10px;
  color: #000000;

  border: 0;
  font-size: 20px;

  font-weight: bold;
  &:hover {
    transform: scale(1.05);
  }
  &:active {
    transform: scale(1.09);
  }
  transition: all 0.3s ease-in-out;
`;
const Button = styled.button`
  margin-top: 20px;
  width: 100%;
  height: 40px;
  background-color: white;
  border-radius: 10px;
  color: white;
  cursor: pointer;

  border: 2px solid #157e36;
  font-size: 20px;
  background-color: #157e36;

  font-weight: bold;
  &:hover {
    transform: scale(1.05);
  }
  &:active {
    transform: scale(1.09);
  }
  transition: all 0.3s ease-in-out;
`;
const GuideText = styled.div`
  font-size: 18px;
  color: #666666;
`;
