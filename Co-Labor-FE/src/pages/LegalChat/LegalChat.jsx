import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import MessageSend from './components/MessageSend';
import { MessageList } from './components/Message';
import styled from 'styled-components';
import InitalMessage from './components/InitalMessage';
const LegalChat = () => {
  const [messages, setMessages] = useState([]);
  const [username, setUsername] = useState('');
  const [loading, setLoading] = useState(false);
  const [isSending, setIsSending] = useState(false);
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUsername = sessionStorage.getItem('username');
    if (storedUsername) {
      setUsername(storedUsername);
    } else {
      navigate('/SingIn');
      alert('이 기능을 사용하려면 로그인이 필요합니다.');
    }
    console.log(username);
    console.log(username);
    console.log(username);
  }, [navigate]);

  // 메시지 목록 불러오기
  const fetchMessages = (userId) => {
    setLoading(true);
    fetch(`/api/chatting/all?userId=${userId}`, {
      credentials: 'include',
    })
      .then((response) => {
        if (response.status === 401) {
          throw new Error('Unauthorized');
        }
        return response.json();
      })
      .then((data) => {
        setMessages(
          data.map((msg) => ({
            text: msg.content,
            isUser: msg.my_message,
          }))
        );
      })
      .catch((error) => {
        console.error('Error fetching messages:', error);
        alert('메시지를 불러오지 못했습니다. 다시 시도해 주세요.');
      })
      .finally(() => {
        setLoading(false);
      });
  };

  // 메시지 전송 핸들러
  const handleSendMessage = (message) => {
    if (!username) {
      console.error('User not logged in');
      return;
    }

    setIsSending(true);

    setMessages((prevMessages) => [
      ...prevMessages,
      { text: message, isUser: true },
    ]);

    fetch(
      `/api/chatting/send?userId=${username}&message=${encodeURIComponent(
        message
      )}`,
      {
        method: 'POST',
        credentials: 'include',
      }
    )
      .then((response) => {
        if (response.status === 401) {
          throw new Error('Unauthorized');
        }
        fetchMessages(username);
      })
      .then(() => {
        setRefreshTrigger((prev) => prev + 1); // 트리거 증가
      })
      .catch((error) => {
        console.error('Error sending message:', error);
        alert('메시지를 전송하지 못했습니다. 다시 시도해 주세요.');
      })
      .finally(() => {
        setIsSending(false);
      });
  };

  useEffect(() => {
    if (username) {
      fetchMessages(username);
    }
  }, [username, refreshTrigger]);

  return (
    <Field>
      <BaseContainer>
        {messages.length === 0 ? (
          //메시지가 없을 때 초기화면
          <InitalMessage
            messages={messages}
            loading={loading}
            isSending={isSending}
            onSendMessage={handleSendMessage}
            username={username}
          />
        ) : (
          <>
            {/* 메시지가 있을 때 */}
            <MessageList
              messages={messages}
              loading={loading}
              isSending={isSending}
            />

            <MessageSend
              onSendMessage={handleSendMessage}
              isSending={isSending}
            />
          </>
        )}
      </BaseContainer>
    </Field>
  );
};

export default LegalChat;

const Field = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
  background: #fff;
`;
const BaseContainer = styled.div`
  width: 1000px;
  height: 100%;
  background: #fff;
  overflow: hidden;
  display: flex;
  flex-direction: column;
`;
