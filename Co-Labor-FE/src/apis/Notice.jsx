import axios from 'axios';
export const deleteJobNotice = ({ jobId, nav }) => {
  axios
    .delete(`/api/jobs/${jobId}`, {
      withCredentials: true,
    })
    .then((res) => {
      //console.log('삭제 성공:', res.data);
      nav('/jobNotice');
    })
    .catch((err) => {
      console.error('삭제 실패:', err);
    });
};
