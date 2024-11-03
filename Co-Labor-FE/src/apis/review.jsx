import axios from 'axios';

export const addReview = (reviewAdd, setWrite, setComplteAdd) => {
  //console.log('reviewAdd 요청 전', reviewAdd);
  axios
    .post(`/api/reviews/${reviewAdd.enterpriseId}`, reviewAdd, {
      headers: {
        'Content-Type': 'application/json', // 명시적으로 Content-Type을 지정
      },
      withCredentials: true, // 쿠키를 포함하여 요청 보냄
    })
    .then((response) => {
      alert('리뷰 등록완료!');
      //console.log('리뷰 등록 response:', response);
      setWrite(false);

      setComplteAdd(true);
      setTimeout(() => setComplteAdd(false), 1000);
      //console.log('리뷰 등록 response:', response);
    })

    .catch((error) => {
      console.error('리뷰 등록 실패', error);
      alert('리뷰 등록 실패');
    });
};

export const canAddReview = ({ type, name }) => {
  if (type && name) {
    if (type === 'labor_user') {
      return true;
    } else return false;
  }
  return false;
};
