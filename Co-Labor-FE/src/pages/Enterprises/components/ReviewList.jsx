import React from 'react';
import { useContext, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import { ReviewContext } from '../../../App';

import styled from 'styled-components';
import ReviewItem from './ReviewItem';

const ReviewList = ({ complteAdd }) => {
  const params = useParams();
  const [reviewData, setReviewData] = useState([]);
  useEffect(() => {
    fetch('/api/reviews/all')
      .then((response) => response.json())
      .then((data) =>
        setReviewData(
          data.filter(
            (review) =>
              String(review.enterprise.enterprise_id) ===
              String(params.enterprise_id)
          )
        )
      )
      .catch((error) => console.error('Error fetching reviews:', error));
  }, [complteAdd, params.enterprise_id]);

  return (
    <BaseContiner>
      {reviewData.length > 0 ? (
        reviewData.map((review) => (
          <ReviewItem key={review.review_id} review={review} />
        ))
      ) : (
        <Title>등록된 리뷰가 없습니다.</Title>
      )}
    </BaseContiner>
  );
};

export default ReviewList;

const BaseContiner = styled.div`
  padding: 20px 0px;
  width: 1400px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin: 40px 0px;
`;

const Title = styled.div`
  margin-bottom: 10px;
  font-size: 20px;
  font-weight: bold;
`;
