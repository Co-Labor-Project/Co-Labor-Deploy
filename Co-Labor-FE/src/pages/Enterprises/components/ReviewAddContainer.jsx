import { useState } from 'react';
import styled from 'styled-components';
import { SetStarRate, SetBarGraph } from './ReviewAddStar';
import TextareaAutosize from 'react-textarea-autosize';
import { addReview } from '../../../apis/review';

const ReviewAddContainer = ({
  reviewAdd,
  setReviewAdd,
  setWrite,
  setComplteAdd,
}) => {
  // 특정 rating 값이 변경되었을 때 호출되는 함수
  const handleChange = (field, value) => {
    setReviewAdd((prevReviewAdd) => ({
      ...prevReviewAdd,
      [field]: value,
    }));
  };

  return (
    <ReviewContainer>
      <Title>제목</Title>
      <TextareaStyled
        minRows={3}
        value={reviewAdd.title || ''}
        onChange={(e) => handleChange('title', e.target.value)}
      />
      <Info>
        <TextTitleItem>총평</TextTitleItem>
        <SetStarRate
          rating={reviewAdd.rating}
          onRatingChange={(newRating) => handleChange('rating', newRating)}
        />
        <ListWrapper>
          <TextItem>승진 기회 및 개인 성장 가능성</TextItem>
          <SetBarGraph
            rating={reviewAdd.promotionRating}
            onRatingChange={(newRating) =>
              handleChange('promotionRating', newRating)
            }
          />
          <TextItem>복지 및 급여</TextItem>
          <SetBarGraph
            rating={reviewAdd.salaryRating}
            onRatingChange={(newRating) =>
              handleChange('salaryRating', newRating)
            }
          />
          <TextItem>업무와 삶의 균형</TextItem>
          <SetBarGraph
            rating={reviewAdd.balanceRating}
            onRatingChange={(newRating) =>
              handleChange('balanceRating', newRating)
            }
          />
          <TextItem>사내 문화 평가 점수</TextItem>
          <SetBarGraph
            rating={reviewAdd.cultureRating}
            onRatingChange={(newRating) =>
              handleChange('cultureRating', newRating)
            }
          />
          <TextItem>경영진 관련 평가 점수</TextItem>
          <SetBarGraph
            rating={reviewAdd.managementRating}
            onRatingChange={(newRating) =>
              handleChange('managementRating', newRating)
            }
          />
        </ListWrapper>
        <TextPoint>장점</TextPoint>
        <TextareaStyled
          minRows={3}
          value={reviewAdd.pros || ''}
          onChange={(e) => handleChange('pros', e.target.value)}
        />
        <TextPoint>단점</TextPoint>
        <TextareaStyled
          minRows={3}
          value={reviewAdd.cons || ''}
          onChange={(e) => handleChange('cons', e.target.value)}
        />
        <CautionWrap>
          <CautionText>
            ※ 수정 및 삭제가 불가능하므로 신중히 작성해 주세요.
          </CautionText>
          <CautionText>
            ※ 허위, 중복, 성의없는 내용을 작성할 경우, 서비스 이용이 제한될 수
            있습니다.
          </CautionText>
        </CautionWrap>
        <Submit onClick={() => addReview(reviewAdd, setWrite, setComplteAdd)}>
          작성하기
        </Submit>
      </Info>
    </ReviewContainer>
  );
};

export default ReviewAddContainer;

const ReviewContainer = styled.div`
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 13px rgba(0, 0, 0, 0.3);
  margin-bottom: 40px;
  margin-left: 60px;
  width: 500px;
  transition: all 0.3s ease;
  margin-top: 50px;
`;

const Info = styled.div`
  margin-top: 10px;
  font-size: 1em;
  color: #333;
  line-height: 35px;
`;

const Title = styled.div`
  margin-bottom: 10px;
  font-size: 20px;
  font-weight: bold;
`;
const TextareaStyled = styled(TextareaAutosize)`
  resize: none;
  border-radius: 15px;
  width: 80%;
  outline: none;
  border: 2px solid #1fbe51;
  font-size: 18px;
  padding: 10px 10px;
  font-weight: 400px;
  color: #33333;
  overflow: auto;
  max-height: 120px;

  &:focus {
    border: 3px solid #157e36;
    transform: scale(1.05);
  }
  transition: all 0.3s ease-in-out;
`;

const TextItem = styled.div`
  font-size: 16px;
  font-weight: 600;
  width: 100%;
  min-width: 100px;
`;
const TextTitleItem = styled.div`
  font-size: 18px;
  font-weight: 600;
  width: 100%;
  min-width: 100px;
`;
const TextPoint = styled.div`
  font-size: 16px;
  font-weight: 600;
  width: 100%;
  min-width: 100px;
  margin-top: 15px;
`;
const CautionWrap = styled.div`
  margin-top: 15px;
`;
const CautionText = styled.div`
  font-size: 14px;
  line-height: 1.4;
  color: #c4c4c4;
`;
const TextContent = styled.div`
  font-size: 15px;
  line-height: 28px;
`;

const ListWrapper = styled.div`
  border-bottom: 1px solid #e9e9e9;
  border-top: 1px solid #e9e9e9;
  margin-top: 15px;
`;
const Submit = styled.button`
  margin-top: 30px;
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
