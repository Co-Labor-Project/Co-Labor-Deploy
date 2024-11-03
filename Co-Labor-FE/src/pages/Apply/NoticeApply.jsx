import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { submitNotice } from '../../apis/apply';
import styled from 'styled-components';
import TextareaAutosize from 'react-textarea-autosize';
const getTodayDate = () => {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};
const NoticeApply = () => {
  const [input, setInput] = useState({
    title: '',
    requirement: '',
    jobRole: '',
    experience: '',
    employmentType: '',
    address1: '',
    address2: '',
    address3: '',
    skills: '',
    views: 0,
    deadDate: getTodayDate(),
    jobDescription: '',
    applicantRequirements: '',
    preferredQualifications: '',
    applicationMethod: '',
    workingDays: '',
    workingHours: '',
    workingPeriod: '',
    salary: '',
    enterprise_id: '',
    name: '',
    type: '',
    phone_number: '',
    description: '',
  });

  const [logoFile, setLogoFile] = useState(null);
  const [page, setPage] = useState(1);
  const nav = useNavigate();

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setInput((prevInput) => ({ ...prevInput, [name]: value }));
    //console.log(input);
  };

  const handleFileChange = (e) => {
    setLogoFile(e.target.files[0]);
  };
  const handleSubmit = (e) => {
    e.preventDefault();

    const requestData = {
      ...input,
    };
    //console.log('최종 나가는 Data', requestData);
    submitNotice(requestData, logoFile, nav);
  };

  return (
    <BaseContainer>
      <Form onSubmit={handleSubmit}>
        {page === 1 && (
          <>
            <InputWrap>
              <InputText>제목</InputText>
              <Input
                type="text"
                name="title"
                placeholder="Title"
                value={input.title}
                onChange={handleInputChange}
              />
            </InputWrap>

            <InputWrap>
              <InputText>직무 역할</InputText>
              <Input
                type="text"
                name="jobRole"
                placeholder="Job Role"
                value={input.jobRole}
                onChange={handleInputChange}
              />
            </InputWrap>
            <InputWrap>
              <InputText>경력</InputText>
              <Input
                type="text"
                name="experience"
                placeholder="Experience"
                value={input.experience}
                onChange={handleInputChange}
              />
            </InputWrap>
            <Button onClick={() => setPage(page + 1)}>다음</Button>
          </>
        )}
        {page === 2 && (
          <>
            <ButWrapper>
              <MinButton onClick={() => setPage(page - 1)}>&lt; 이전</MinButton>
            </ButWrapper>
            <InputWrap>
              <InputText>시 / 도</InputText>
              <Input
                type="text"
                name="address1"
                placeholder="Address 1"
                value={input.address1}
                onChange={handleInputChange}
              />
            </InputWrap>
            <InputWrap>
              <InputText>시/ 군 / 구</InputText>
              <Input
                type="text"
                name="address2"
                placeholder="Address 2"
                value={input.address2}
                onChange={handleInputChange}
              />
            </InputWrap>
            <InputWrap>
              <InputText>상세 주소</InputText>
              <Input
                type="text"
                name="address3"
                placeholder="Address 3"
                value={input.address3}
                onChange={handleInputChange}
              />
            </InputWrap>
            <Button onClick={() => setPage(page + 1)}>다음</Button>
          </>
        )}
        {page === 3 && (
          <>
            <ButWrapper>
              <MinButton onClick={() => setPage(page - 1)}>&lt; 이전</MinButton>
            </ButWrapper>
            <InputWrap>
              <InputText>고용 형태</InputText>
              <Input
                type="text"
                name="employmentType"
                placeholder="Employment Type"
                value={input.employmentType}
                onChange={handleInputChange}
              />
            </InputWrap>
            <InputWrap>
              <InputText>기술 / 스킬</InputText>
              <Input
                type="text"
                name="skills"
                placeholder="skills"
                value={input.skills}
                onChange={handleInputChange}
              />
            </InputWrap>
            <InputWrap>
              <InputText>마감일</InputText>
              <Input
                type="date"
                name="deadDate"
                placeholder="마감일"
                value={input.deadDate}
                onChange={handleInputChange}
              />
            </InputWrap>
            <InputWrap>
              <InputText>채용공고 사진</InputText>
              <Input
                type="file"
                name="logo"
                accept="image/*"
                onChange={handleFileChange}
              />
            </InputWrap>
            <MinText>▷ 1000KB이하 크기의 사진을 넣어주세요.</MinText>

            <Button onClick={() => setPage(page + 1)}>다음</Button>
          </>
        )}
        {page === 4 && (
          <>
            <ButWrapper>
              <MinButton onClick={() => setPage(page - 1)}>&lt; 이전</MinButton>
            </ButWrapper>
            <InputWrap>
              <InputText>업무내용</InputText>
              <Input
                type="text"
                name="jobDescription"
                placeholder="Job Description"
                value={input.jobDescription}
                onChange={handleInputChange}
              />
            </InputWrap>

            <InputWrap>
              <InputText>지원 자격</InputText>
              <Input
                type="text"
                name="applicantRequirements"
                placeholder="Applicant Requirements"
                value={input.applicantRequirements}
                onChange={handleInputChange}
              />
            </InputWrap>

            <InputWrap>
              <InputText>우대 사항</InputText>
              <Input
                type="text"
                name="preferredQualifications"
                placeholder="Preferred Qualifications"
                value={input.preferredQualifications}
                onChange={handleInputChange}
              />
            </InputWrap>

            <InputWrap>
              <InputText>지원 방법</InputText>
              <Input
                type="text"
                name="applicationMethod"
                placeholder="Application Method"
                value={input.applicationMethod}
                onChange={handleInputChange}
              />
            </InputWrap>

            <InputWrap>
              <InputText>근무일</InputText>
              <Input
                type="text"
                name="workingDays"
                placeholder="Working Days"
                value={input.workingDays}
                onChange={handleInputChange}
              />
            </InputWrap>

            <InputWrap>
              <InputText>근무 시간</InputText>
              <Input
                type="text"
                name="workingHours"
                placeholder="Working Hours"
                value={input.workingHours}
                onChange={handleInputChange}
              />
            </InputWrap>

            <InputWrap>
              <InputText>근무 기간</InputText>
              <Input
                type="text"
                name="workingPeriod"
                placeholder="Working Period"
                value={input.workingPeriod}
                onChange={handleInputChange}
              />
            </InputWrap>

            <InputWrap>
              <InputText>급여</InputText>
              <Input
                type="text"
                name="salary"
                placeholder="Salary"
                value={input.salary}
                onChange={handleInputChange}
              />
            </InputWrap>

            <Button type="submit">등록</Button>
          </>
        )}
      </Form>
    </BaseContainer>
  );
};

export default NoticeApply;

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
const MinText = styled.div`
  font-size: 18px;
  color: #929292;
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

const ButWrapper = styled.div`
  width: 100%;
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
  margin-bottom: 40px;
  width: 100%;
  height: 40px;
  background-color: white;
  border-radius: 10px;
  color: white;
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
