import React, { useState, useEffect, useRef, useContext } from 'react';
import './SingInCenter.css';
import { useNavigate } from 'react-router-dom';
import { LoginContext } from '../../../App';
import axios from 'axios';
import { onLogin, signUp } from '../../../apis/login';

const SingInCenter = () => {
  const { loginState, setLoginState } = useContext(LoginContext);
  const nav = useNavigate();
  const containerRef = useRef(null);
  const [input, setInput] = useState({
    username: '',
    password: '',
    passwordConfirm: '',
    email: '',
    name: '',
    isEnterprise: false,
  });
  const [Loginusername, setUsername] = useState('');
  const [Loginpassword, setPassword] = useState('');
  const [errors, setErrors] = useState({
    username: '',
    email: '',
    password: '',
    passwordConfirm: '',
    name: '',
  });

  const [isValid, setIsValid] = useState({
    username: true,
    email: true,
    password: true,
    passwordConfirm: true,
    name: true,
  });
  useEffect(() => {
    if (loginState.userLogin || loginState.userEnterprise) {
      nav('/');
    }
  }, [loginState, nav]);

  const validateUsername = () => {
    const usernameRegex = /^[a-zA-Z0-9]{4,}$/;

    if (input.username.trim() === '') {
      setErrors((prev) => ({ ...prev, username: 'userName을 입력해주세요.' }));
      setIsValid((prev) => ({ ...prev, username: false }));
    } else if (!usernameRegex.test(input.username)) {
      setErrors((prev) => ({
        ...prev,
        username: 'userName는 영어와 숫자로 4글자 이상 입력 가능합니다.',
      }));
      setIsValid((prev) => ({ ...prev, username: false }));
    } else {
      setErrors((prev) => ({ ...prev, username: '' }));
      setIsValid((prev) => ({ ...prev, username: true }));
    }
  };

  const validateEmail = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(input.email)) {
      setErrors((prev) => ({
        ...prev,
        email: '올바른 이메일 형식이 아닙니다.',
      }));
      setIsValid((prev) => ({ ...prev, email: false }));
    } else {
      setErrors((prev) => ({ ...prev, email: '' }));
      setIsValid((prev) => ({ ...prev, email: true }));
    }
  };

  const validatePassword = () => {
    const passwordRegex = /^[a-zA-Z0-9@$#!%*?&]{8,}$/;
    if (!passwordRegex.test(input.password)) {
      setErrors((prev) => ({
        ...prev,
        password:
          '비밀번호는 8자 이상, 영어, 숫자, 특수문자를 포함해야 합니다.',
      }));
      setIsValid((prev) => ({ ...prev, password: false }));
    } else {
      setErrors((prev) => ({ ...prev, password: '' }));
      setIsValid((prev) => ({ ...prev, password: true }));
    }
  };

  const validatePasswordConfirm = () => {
    if (input.password !== input.passwordConfirm) {
      setErrors((prev) => ({
        ...prev,
        passwordConfirm: '비밀번호가 일치하지 않습니다.',
      }));
      setIsValid((prev) => ({ ...prev, passwordConfirm: false }));
    } else {
      setErrors((prev) => ({ ...prev, passwordConfirm: '' }));
      setIsValid((prev) => ({ ...prev, passwordConfirm: true }));
    }
  };

  const onChangeInput = (e) => {
    const { name, value } = e.target;
    setInput((prevInput) => ({
      ...prevInput,
      [name]: name === 'isEnterprise' ? e.target.checked : value,
    }));
  };

  const onChangeUsername = (e) => {
    setUsername(e.target.value);
  };

  const onChangePassword = (e) => {
    setPassword(e.target.value);
  };

  const onsubmit = () => {
    if (input.password !== input.passwordConfirm) {
      alert('패스워드가 일치하지 않습니다.');
      return;
    }
    const url = input.isEnterprise
      ? '/api/auth/signup-enterprise'
      : '/api/auth/signup-labor';
    const json = JSON.stringify(input);
    signUp(json, url, nav);
  };

  const toggle = () => {
    if (containerRef.current) {
      containerRef.current.classList.toggle('sign-in');
      containerRef.current.classList.toggle('sign-up');
    }
  };
  const keyHandler = (e) => {
    if (e.keyCode === 13) {
      onLogin(Loginusername, Loginpassword, setLoginState);
    }
  };
  useEffect(() => {
    if (containerRef.current) {
      setTimeout(() => {
        containerRef.current.classList.add('sign-in');
      }, 200);
    }
  }, []);

  return (
    //  This code was originally created by TechNext on CodePen
    //  Original Pen: https://codepen.io/technext/pen/WNpWeWw
    <div className="SingInCenter">
      <div id="container" className="container" ref={containerRef}>
        <div className="row">
          <div className="col align-items-center flex-col sign-up">
            <div className="form-wrapper align-items-center">
              <div id="signup-form" className="form sign-up">
                <div className="error-messages">
                  {errors.username && (
                    <div style={{ color: 'red' }}>{errors.username}</div>
                  )}
                  {errors.email && (
                    <div style={{ color: 'red' }}>{errors.email}</div>
                  )}

                  {errors.password && (
                    <div style={{ color: 'red' }}>{errors.password}</div>
                  )}
                  {errors.passwordConfirm && (
                    <div style={{ color: 'red' }}>{errors.passwordConfirm}</div>
                  )}
                </div>
                <div className="input-group">
                  <i className="bx bxs-user"></i>
                  <input
                    type="text"
                    name="username"
                    placeholder="Username"
                    value={input.username}
                    onChange={onChangeInput}
                    onBlur={validateUsername}
                    className={isValid.username ? '' : 'invalid-input'}
                  />
                </div>
                <div className="input-group">
                  <i className="bx bx-mail-send"></i>
                  <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={input.email}
                    onChange={onChangeInput}
                    onBlur={validateEmail}
                    className={isValid.email ? '' : 'invalid-input'}
                  />
                </div>
                <div className="input-group">
                  <i className="bx bxs-lock-alt"></i>
                  <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={input.password}
                    onChange={onChangeInput}
                    onBlur={validatePassword}
                    className={isValid.password ? '' : 'invalid-input'}
                  />
                </div>
                <div className="input-group">
                  <i className="bx bxs-lock-alt"></i>
                  <input
                    type="password"
                    name="passwordConfirm"
                    placeholder="Confirm password"
                    value={input.passwordConfirm}
                    onChange={onChangeInput}
                    onBlur={validatePasswordConfirm}
                    className={isValid.passwordConfirm ? '' : 'invalid-input'}
                  />
                </div>
                <div className="input-group">
                  <i className="bx bxs-lock-alt"></i>
                  <input
                    type="text"
                    name="name"
                    placeholder="name"
                    value={input.name}
                    onChange={onChangeInput}
                  />
                </div>
                <div className="input-group">
                  <div className="enterpriseMem">
                    <span>기업회원이신가요?</span>
                    <input
                      type="checkbox"
                      name="isEnterprise"
                      checked={input.isEnterprise}
                      onChange={onChangeInput}
                    />
                  </div>
                </div>
                <button
                  onClick={() => {
                    onsubmit();
                  }}
                >
                  Sign up
                </button>
                <p>
                  <span>Already have an account? &nbsp;&nbsp;&nbsp;&nbsp;</span>
                  <b onClick={toggle} className="pointer">
                    Sign in here
                  </b>
                </p>
              </div>
            </div>
          </div>
          <div className="col align-items-center flex-col sign-in">
            <div className="form-wrapper align-items-center">
              <div id="signin-form" className="form sign-in">
                <div className="input-group">
                  <i className="bx bxs-user"></i>
                  <input
                    type="text"
                    name="username"
                    placeholder="Username"
                    onChange={onChangeUsername}
                  />
                </div>
                <div className="input-group">
                  <i className="bx bxs-lock-alt"></i>
                  <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    onChange={onChangePassword}
                    onKeyDown={keyHandler}
                  />
                </div>
                <button
                  type="submit"
                  onClick={() =>
                    onLogin(Loginusername, Loginpassword, setLoginState)
                  }
                >
                  Sign in
                </button>

                <p>
                  <span>
                    Don&apos;t have an account? &nbsp;&nbsp;&nbsp;&nbsp;
                  </span>
                  <b onClick={toggle} className="pointer">
                    Sign up here
                  </b>
                </p>
              </div>
            </div>
          </div>
        </div>
        <div className="row content-row">
          <div className="col align-items-center flex-col">
            <div className="text sign-in">
              <h2 className="notranslate">Co Labor</h2>
            </div>
            <div className="img sign-in"></div>
          </div>
          <div className="col align-items-center flex-col">
            <div className="img sign-up"></div>
            <div className="text sign-up">
              <h2>Join with us</h2>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SingInCenter;
