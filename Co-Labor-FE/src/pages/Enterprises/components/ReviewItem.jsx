import React from 'react';
import styled from 'styled-components';
import StarRate from '../../../components/StarRate';
import BarGraph from '../../../components/BarGraph';

const ReviewItem = ({ review }) => {
  return (
    <ReviewContainer>
      <Title>{review.title}</Title>
      <Info>
        <TextContent>
          {review.created_at} 작성자 : {review.laborUser.name}
        </TextContent>
        <StarRate rating={review.rating} />
        <ListWrapper>
          <TextItem>승진 기회 및 개인 성장 가능성</TextItem>
          <BarGraph rating={review.promotion_rating} />
          <TextItem>복지 및 급여</TextItem>
          <BarGraph rating={review.salary_rating} />
          <TextItem>업무와 삶의 균형</TextItem>
          <BarGraph rating={review.balance_rating} />
          <TextItem>사내 문화 평가 점수</TextItem>
          <BarGraph rating={review.culture_rating} />
          <TextItem>경영진 관련 평가 점수</TextItem>
          <BarGraph rating={review.management_rating} />
        </ListWrapper>
        <TextPoint>장점</TextPoint>
        <TextContent>{review.pros}</TextContent>
        <TextPoint>단점</TextPoint>
        <TextContent>{review.cons}</TextContent>
      </Info>
    </ReviewContainer>
  );
};

export default ReviewItem;

const ReviewContainer = styled.div`
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 40px;
  margin-left: 60px;

  width: 500px;
  transition: all 0.3s smooth;
  border: 0px solid #58c179;
  &:hover {
    border: 3px solid #58c179;
    transform: scale(1.05);
  }
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
const TextItem = styled.div`
  font-size: 16px;
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

const TextContent = styled.div`
  font-size: 15px;
  line-height: 28px;
`;

const ListWrapper = styled.div`
  border-bottom: 1px solid #e9e9e9;
  border-top: 1px solid #e9e9e9;
  margin-top: 15px;
`;
