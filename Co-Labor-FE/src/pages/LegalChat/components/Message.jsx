import {
  LoadingText,
  LoadingSpinner,
  LoadingWrapper,
} from '../../../components/CommonStyled';
import styled from 'styled-components';
export const MessageList = ({ messages, loading, isSending }) => (
  <BaseContainer>
    {loading ? (
      <LoadingWrapper>
        <LoadingSpinner />
        <LoadingText>🤖 이전 채팅 기록을 불러오는 중 입니다...</LoadingText>
      </LoadingWrapper>
    ) : (
      <>
        {messages.map((message, index) => (
          <Message key={index} {...message} />
        ))}
        {isSending && (
          <LoadingWrapper>
            <LoadingSpinner />
            <LoadingText>🤖 답변을 생성 중 입니다...</LoadingText>
          </LoadingWrapper>
        )}
      </>
    )}
  </BaseContainer>
);

// 메시지 컴포넌트
export const Message = ({ text, isUser }) => {
  return isUser ? (
    <UserMsg>
      <AIMsgWrapper dangerouslySetInnerHTML={{ __html: text }} />
    </UserMsg>
  ) : (
    <AiMsg>
      <UserMsgWrapper className="notranslate">{'Co Labor :'}</UserMsgWrapper>
      <AIMsgWrapper dangerouslySetInnerHTML={{ __html: text }} />
    </AiMsg>
  );
};

const BaseContainer = styled.div`
  padding: 20px;
  flex-grow: 1;
  overflow-y: auto;
`;
const UserMsgWrapper = styled.div`
  flex: 1;
`;
const AIMsgWrapper = styled.div`
  flex: 5;
`;

const UserMsg = styled.div`
  margin-bottom: 20px;
  display: flex;
  gap: 4px;
  align-self: flex-end;
  max-width: 40%;
  background-color: var(--primary-color);
  color: #fff;
  padding: 10px 15px;
  border-radius: 16px 16px 0 16px;
  margin-left: auto;
`;

const AiMsg = styled.div`
  align-self: flex-start;
  background: #f0f0f0;
  color: #333;
  padding: 10px 15px;
  border-radius: 16px 16px 16px 0;
  margin-bottom: 20px;
  display: flex;
  gap: 4px;
  max-width: 70%;
`;
