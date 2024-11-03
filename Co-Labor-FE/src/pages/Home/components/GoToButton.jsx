import React from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import useScrollFadeIn from '../../../hooks/fade_in';

const GoToButton = ({ text, url }) => {
  const fadeInProps = useScrollFadeIn('up', 1);
  const nav = useNavigate();

  return (
    <div {...fadeInProps}>
      <Button onClick={() => nav(`${url}`)}>
        {text}
        <Direction>↗</Direction>
      </Button>
    </div>
  );
};

export default GoToButton;
const Direction = styled.p`
  font-size: 20px;
  font-weight: 400;
`;
const Button = styled.button`
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  font-size: 16px;
  font-weight: 600;
  color: #000;

  color: black;
  background: transparent;
  box-shadow: -1px 4px 6px rgba(0, 0, 0, 0.2);
  border: 0px;
  position: relative;
  transition: 0.6s cubic-bezier(0.77, 0, 0.175, 1); /* ease-in-out-quartic */
  border-radius: 20px;
  width: 250px;
  height: 40px;

  &::before {
    border: 0px;

    position: absolute;
    content: '';
    top: 0;
    left: 0;
    z-index: -1;
    width: 100%;
    border-radius: 20px;
    height: 100%;
    background-color: #148d7f;
    transform: scaleX(0);
    transform-origin: right;
    transition: transform 0.6s cubic-bezier(0.77, 0, 0.175, 1);
  }

  &:hover {
    color: white;
    background: transparent;
    outline: none;
    &::before {
      transform: scaleX(1);
      transform-origin: left;
    }
  }
`;
