import axios from 'axios';

export const onLogin = (Loginusername, Loginpassword, setLoginState) => {
  axios
    .post(
      '/api/auth/login',
      {
        username: Loginusername,
        password: Loginpassword,
      },
      { withCredentials: true }
    )
    .then((response) => {
      const result = response.data;
      //console.log('로그인 결과: ', result);

      if (result.message === 'Login successful') {
        whoIsIt(setLoginState);

        alert('로그인 성공!');
      } else {
        throw new Error('로그인 실패');
      }
    })
    .catch((error) => {
      console.error(
        '로그인 오류:',
        error.response ? error.response.data : error.message
      );
      alert('로그인 실패!');
    });
};

export const signUp = (json, url, nav) => {
  axios
    .post(url, json, {
      headers: {
        'Content-Type': 'application/json', // 명시적으로 Content-Type을 지정
      },
      withCredentials: true, // 쿠키를 포함하여 요청 보냄
    })
    .then((response) => {
      alert('회원가입 성공!');
      nav('/');
    })
    .catch((error) => {
      console.error(
        '회원가입 실패:',
        error.response ? error.response.data : error.message
      );
      alert('회원가입 실패!');
    });
};

export const whoIsIt = (setLoginState) => {
  axios
    .get('/api/auth/current-user', {
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      withCredentials: true,
    })
    .then((res) => {
      if (res.data.message === 'No JSESSIONID found') {
        setLoginState({ userEnterprise: false, userLogin: false });
        return;
      }

      // //console.log('resres', res.data);
      sessionStorage.setItem('username', res.data.username);
      sessionStorage.setItem('userType', res.data.userType);
      if (res.data.userType === 'enterprise_user') {
        setLoginState({ userEnterprise: true, userLogin: true });
      } else {
        setLoginState({ userLogin: true, userEnterprise: false });
      }
    })
    .catch((error) => {});
};

export const logOut = (setLoginState) => {
  axios
    .post('/api/auth/logout', {
      headers: {
        'Content-Type': 'application/json',
      },
      withCredentials: true,
    })
    .then((response) => {
      setLoginState({ userLogin: false, userEnterprise: false });
      sessionStorage.clear();
      alert('로그아웃 완료!');
      const cookies = document.cookie.split(';');
      cookies.forEach((cookie) => {
        const eqPos = cookie.indexOf('=');
        const name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie =
          name + '=;expires=Thu, 01 Jan 2024 00:00:00 GMT;path=/';
      });
    })
    .catch((error) => {
      console.error(
        '로그아웃 실패:',
        error.response ? error.response.data : error.message
      );
      alert('로그아웃 실패!');
    });
};
