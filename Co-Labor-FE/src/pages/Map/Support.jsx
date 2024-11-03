import SupportCenterMap from './SupportCenterMap';
import { Container as MapDiv } from 'react-naver-maps';
import styled from 'styled-components';
const Support = () => {
  return (
    <BackGroundField>
      <MapDiv
        style={{
          width: '100%',
          height: '100%',
        }}
      >
        <SupportCenterMap />
      </MapDiv>
    </BackGroundField>
  );
};

export default Support;
const BackGroundField = styled.div`
  width: 100%;
  height: calc(100vh - 80px);
`;
