import { useState } from 'react';
import styled from 'styled-components';

const MessageSend = ({ onSendMessage, isSending }) => {
  const [message, setMessage] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    if (!isSending) {
      // 메시지 전송 중에는 중복 전송 방지
      onSendMessage(message);
      setMessage('');
    }
  };

  return (
    <BaseContainer onSubmit={handleSubmit}>
      <InputPrompt
        type="text"
        value={message}
        onChange={(event) => setMessage(event.target.value)}
        disabled={isSending}
      ></InputPrompt>
      <SendButton type="submit" disabled={isSending}>
        {isSending ? 'Sending...' : '→'}
      </SendButton>
    </BaseContainer>
  );
};

export default MessageSend;

const BaseContainer = styled.form`
  border-top: 1px solid #f0f0f0;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 20px;
`;
const InputPrompt = styled.input`
  flex-grow: 1;
  padding: 10px;
  border-radius: 16px;
  border: 0px;

  background-color: #f7f7f5;
  position: relative;
  height: 60px;
  font-size: 18px;
  &:focus {
    outline: none;
    border: 1px solid #ccc;
  }
`;
const SendButton = styled.button`
  padding: 10px 30px;
  border-radius: 16px;
  border: none;
  background-color: var(--primary-color);
  color: #fff;
  cursor: pointer;
  font-size: 22px;
`;
