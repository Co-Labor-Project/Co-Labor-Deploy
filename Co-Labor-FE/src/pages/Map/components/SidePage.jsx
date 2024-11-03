import React from 'react';
import SupportCenterItem from './SupportCenterItem';
import styled from 'styled-components';
import ChooseImg from '../../../assets/Building.jpg'; //fanjianhua- FREEPIK
const SidePage = ({
  selectedCenter,
  optionCenter,
  sortedCenters,
  handleCenterClick,
}) => {
  return (
    <BaseContainer>
      <ChooseCenter>
        <Selected>
          {/* IMG 출처 - fanjianhua */}
          <ObjectImg src={ChooseImg} alt="image" />
          {selectedCenter && (
            <TextContainer>
              <TextWrapper>
                <TextItem>📌 이름</TextItem>
                <TextContent>{selectedCenter.name}</TextContent>
              </TextWrapper>
              <TextWrapper>
                <TextItem> 🏢 주소</TextItem>
                <TextContent> {selectedCenter.address}</TextContent>
              </TextWrapper>
              <TextWrapper>
                <TextItem>📞 전화번호</TextItem>
                <TextContent> {selectedCenter.phone}</TextContent>
              </TextWrapper>
              {optionCenter && selectedCenter.hospitalInfo && (
                <TextWrapper>
                  <TextItem>🏥 병원 정보</TextItem>{' '}
                  <TextContent>{selectedCenter.hospitalInfo}</TextContent>
                </TextWrapper>
              )}
            </TextContainer>
          )}
        </Selected>
      </ChooseCenter>
      <NearList>
        {sortedCenters.map((center, index) => (
          <div key={index} onClick={() => handleCenterClick(center)}>
            <SupportCenterItem
              name={center.name}
              address={center.address}
              phone={center.phone}
              hospitalInfo={optionCenter ? center.hospitalInfo : null}
            />
          </div>
        ))}
      </NearList>
    </BaseContainer>
  );
};

export default SidePage;

const BaseContainer = styled.div`
  position: absolute;
  right: 10%;
  top: 50%;
  transform: translate(+10%, -50%);

  height: 90%;
  width: 20%;
  z-index: 10px;
  background: #f7f7f7;
  opacity: 0.8;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  transition: all 0.5s ease-in-out;
  &:hover {
    opacity: 1;
    width: 25%;
  }
`;

const NearList = styled.div`
  overflow-y: auto;

  height: 100%;
`;

const Selected = styled.div`
  width: 100%;
  height: 0.1px;
  transition: all 1.5s;
  display: flex;
  border-radius: 20px;

  flex-direction: column;
  gap: 10px;
  align-items: center;
  background-color: white;
  border-bottom: 1px solid #9f9f9f;
  margin-bottom: 15px;
  height: 260px;
  opacity: 1;
  padding: 10px 25px;
`;
const ObjectImg = styled.img`
  height: 150px;
  width: 100%;
  border-radius: 15px;
`;

const ChooseCenter = styled.div`
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
`;

const TextContainer = styled.div`
  font-size: 20px;
  line-height: 25px;
  padding: 5px 10px;
  border-radius: 15px;
  width: 100%;
`;
const TextWrapper = styled.div`
  display: flex;
  gap: 5px;
`;
const TextItem = styled.div`
  font-size: 16px;
  font-weight: 600;
  width: 100px;
  min-width: 100px;
`;
const TextContent = styled.div`
  font-size: 15px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;
