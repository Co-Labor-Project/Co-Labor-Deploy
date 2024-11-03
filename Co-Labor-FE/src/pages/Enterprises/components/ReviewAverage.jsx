import { useContext } from 'react';
import StarRate from '../../../components/StarRate';
import BarGraph from '../../../components/BarGraph';
import RadarChart from '../../../components/RadarChart';
import { useParams } from 'react-router-dom';
import { ReviewContext } from '../../../App';
import styled from 'styled-components';

const ReviewAverage = () => {
  const params = useParams();

  const reviewContext = useContext(ReviewContext);
  const reviewData = reviewContext.filter(
    (review) =>
      String(review.enterprise.enterprise_id) === String(params.enterprise_id)
  );

  const calculateAverageRatings = (reviews) => {
    const totalRatings = reviews.length;
    if (totalRatings === 0) return {};

    const sumRatings = reviews.reduce(
      (acc, review) => {
        acc.total += review.rating;
        acc.promotion += review.promotion_rating;
        acc.salary += review.salary_rating;
        acc.balance += review.balance_rating;
        acc.culture += review.culture_rating;
        acc.management += review.management_rating;
        return acc;
      },
      {
        total: 0,
        promotion: 0,
        salary: 0,
        balance: 0,
        culture: 0,
        management: 0,
      }
    );

    return {
      averageTotal: (sumRatings.total / totalRatings).toFixed(1),
      averagePromotion: (sumRatings.promotion / totalRatings).toFixed(1),
      averageSalary: (sumRatings.salary / totalRatings).toFixed(1),
      averageBalance: (sumRatings.balance / totalRatings).toFixed(1),
      averageCulture: (sumRatings.culture / totalRatings).toFixed(1),
      averageManagement: (sumRatings.management / totalRatings).toFixed(1),
    };
  };

  const averageRatings = calculateAverageRatings(reviewData);

  return (
    <BaseContainer>
      <ItemContainer>
        <div style={{ marginTop: '10px' }}>
          <TextItem>{reviewData.length}개의 리뷰</TextItem>
          <StarRate rating={averageRatings.averageTotal} />
          <br />
        </div>
        <div>
          <TextItem>승진 기회 및 개인 성장 가능성: </TextItem>
          <BarGraph rating={averageRatings.averagePromotion} />
          <br />
        </div>
        <div>
          <TextItem>복지 및 급여: </TextItem>
          <BarGraph rating={averageRatings.averageSalary} />
          <br />
        </div>
        <div>
          <TextItem>업무와 삶의 균형: </TextItem>
          <BarGraph rating={averageRatings.averageBalance} />
          <br />
        </div>
        <div>
          <TextItem>사내 문화 평가 점수: </TextItem>
          <BarGraph rating={averageRatings.averageCulture} />
          <br />
        </div>
        <div>
          <TextItem>경영진 관련 평가 점수: </TextItem>
          <BarGraph rating={averageRatings.averageManagement} />
          <br />
        </div>
      </ItemContainer>
      <ItemContainer>
        <RadarChart data={averageRatings} />
      </ItemContainer>
    </BaseContainer>
  );
};

export default ReviewAverage;

const BaseContainer = styled.div`
  padding: 20px 0px;
  width: 1200px;
  max-width: 1200px;
  margin: 0 auto;
  align-items: center;
  display: flex;
  margin: 40px 0px;
`;
const ItemContainer = styled.div`
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 500px;
  height: 500px;

  align-items: center;
  margin-top: 10px;
  margin: 0 auto;
  justify-content: center;
  border: 0px solid var(--primary-color);
  transition: all 0.5s smooth;

  &:hover {
    border: 3px solid var(--primary-color);
  }
`;
const TextItem = styled.div`
  font-size: 16px;
  font-weight: 600;
  width: 100%;
  min-width: 100px;
`;
