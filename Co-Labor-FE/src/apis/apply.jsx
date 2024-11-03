import axios from 'axios';
export const onCheckRegiNum = (regiNum, setPage) => {
  //console.log(regiNum, setPage);
  axios
    .get(`/api/enterprises/status?enterpriseId=${regiNum}`, {
      withCredentials: true,
    })
    .then((res) => {
      //console.log(res);
      if (res.data.status === 1) {
        onMapRegiNum(regiNum, setPage);
      } else {
        alert('🧑‍⚖️ 실제로 존재하지 않는 사업자 번호입니다.');
      }
    })
    .catch((err) => {
      //console.log(err);
      alert('사업자 번호 확인에 실패했습니다.');
    });
};

const onMapRegiNum = (regiNum, setPage) => {
  axios
    .post(`/api/enterprises/map?enterpriseId=${regiNum}`, null, {
      withCredentials: true,
    })
    .then((res) => {
      //console.log(res);
      if (res.data.status === 1) {
        setPage(2);
      } else if (res.data.status === 0) {
        alert('❌ 일반 회원은 이용하실 수 없습니다.');
      } else if (res.data.status === 2) {
        alert('📌 기업 등록이 이미 완료되어 있습니다.');
        window.location.href = '/';
      } else if (res.data.status === 3) {
        alert('📌 이미 기업에 포함되어 있습니다.');
        window.location.href = '/';
      }
    })
    .catch((error) => {
      //console.log('2번째 map error:\n', error.response || error);
    });
};
export const submitEnterprise = async (input, logoFile, nav) => {
  const formData = new FormData();

  // enterpriseQueue 정보 추가
  formData.append('enterpriseQueue', JSON.stringify(input));

  // 이미지 파일 추가
  formData.append('logo', logoFile);

  try {
    const response = await axios.post('/api/enterprises/queue', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      withCredentials: true,
    });

    //console.log('기업등록결과: ', response.data);
    if (response.status === 200) {
      alert('기업등록 성공!');
      window.location.href = '/';
    }
  } catch (error) {
    console.error('Error:', error.response);
    alert('기업등록 실패!');
  }
};
export const submitNotice = async (input, logoFile, nav) => {
  const formData = new FormData();

  // job과 jobPostingDTO 정보 추가
  formData.append(
    'job',
    JSON.stringify({
      title: input.title,
      requirement: input.requirement,
      jobRole: input.jobRole,
      experience: input.experience,
      employmentType: input.employmentType,
      address1: input.address1,
      address2: input.address2,
      address3: input.address3,
      skills: input.skills,
      deadDate: input.deadDate,
    })
  );

  formData.append(
    'jobPostingDTO',
    JSON.stringify({
      jobDescription: input.jobDescription,
      applicantRequirements: input.applicantRequirements,
      preferredQualifications: input.preferredQualifications,
      applicationMethod: input.applicationMethod,
      workingDays: input.workingDays,
      workingHours: input.workingHours,
      workingPeriod: input.workingPeriod,
      salary: input.salary,
    })
  );

  // 이미지 파일 추가
  formData.append('image', logoFile);

  try {
    const response = await axios.post('/api/jobs', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      withCredentials: true,
    });

    //console.log('채용공고 등록결과: ', response.data);
    if (response.status === 200) {
      alert('채용 공고 등록 성공!');
      window.location.href = '/';
      // nav('/'); // 등록 성공 시 메인 페이지로 이동
    }
  } catch (error) {
    // console.error('Error:', error.response);
    alert('기업등록을 먼저 해주세요!');
  }
};
