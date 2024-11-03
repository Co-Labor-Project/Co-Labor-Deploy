import React, { useEffect, useState, useRef } from 'react';
import { NaverMap, Marker, useNavermaps } from 'react-naver-maps';
import axios from 'axios';
import SupportCenterItem from './components/SupportCenterItem';
import styled from 'styled-components';
import ChooseMode from './components/ChooseMode';
import SidePage from './components/SidePage';
import HospitalImg from '../../assets/icon/HospitalLocation.png'; //병원 아이콘 제작자: mavadee - Flaticon
import HospitalChooseImg from '../../assets/icon/HospitalChoose.png'; //병원 아이콘 제작자: mavadee - Flaticon
import BuildingImg from '../../assets/icon/BuildingLocation.png'; //회사 아이콘 제작자: nawicon - Flaticon
import BuildingChooseImg from '../../assets/icon/BuildingChoose.png'; //회사 아이콘 제작자: nawicon - Flaticon
import CurrentLocation from '../../assets/icon/currentLocation.png'; // 내 위치 아이콘 제작자: Creative Stall Premium - Flaticon
function SupportCenterMap() {
  const navermaps = useNavermaps();
  const mapRef = useRef(null);
  const [centers, setCenters] = useState([]);
  const [currentPosition, setCurrentPosition] = useState(null);
  const [mapCenter, setMapCenter] = useState(null);
  const [sortedCenters, setSortedCenters] = useState([]);
  const [selectedCenter, setSelectedCenter] = useState(null);
  const [optionCenter, setOptionCenter] = useState(false);
  const [click, setClick] = useState(false);
  const [currentAddress, setCurrentAddress] = useState({
    latitude: '',
    longitude: '',
  });

  useEffect(() => {
    if (!navermaps) {
      //console.log('navermaps 객체가 아직 로드되지 않았습니다.');
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        const userPosition = { latitude: latitude, longitude: longitude };
        setCurrentAddress([latitude, longitude]);
        setCurrentPosition(userPosition);
        setMapCenter(new navermaps.LatLng(latitude, longitude));
      },
      (error) => {
        console.error('Error fetching current position:', error);
      }
    );
  }, [navermaps]);

  useEffect(() => {
    if (!currentPosition) return;

    const fetchData = () => {
      if (optionCenter) {
        axios
          .get('/api/hospitals/nearby', {
            params: {
              latitude: currentPosition.latitude,
              longitude: currentPosition.longitude,
            },
          })
          .then((response) => {
            setCenters(response.data);
            //console.log('병원 데이터:', response.data);

            const sorted = [...response.data]
              .sort((a, b) => {
                return (
                  getDistance(currentPosition, a) -
                  getDistance(currentPosition, b)
                );
              })
              .slice(0, 50);
            setSelectedCenter(sorted[0]);
            setSortedCenters(sorted);

            if (sorted[0]) {
              setMapCenter(
                new navermaps.LatLng(sorted[0].latitude, sorted[0].longitude)
              );
            }
          })
          .catch((error) => {
            console.error('병원 데이터를 가져오는 중 에러 발생:', error);
          });
      } else {
        axios
          .get('/api/support-centers/all')
          .then((response) => {
            setCenters(response.data);
            //console.log('지원센터 데이터:', response.data);

            const sorted = [...response.data]
              .sort((a, b) => {
                return (
                  getDistance(currentPosition, a) -
                  getDistance(currentPosition, b)
                );
              })
              .slice(0, 50);
            setSelectedCenter(sorted[0]);
            setSortedCenters(sorted);

            if (sorted[0]) {
              setMapCenter(
                new navermaps.LatLng(sorted[0].latitude, sorted[0].longitude)
              );
            }
          })
          .catch((error) => {
            console.error('지원센터 데이터를 가져오는 중 에러 발생:', error);
          });
      }
    };

    fetchData();
  }, [navermaps, optionCenter, currentPosition, click]);

  const moveToCurrentPosition = () => {
    if (currentPosition) {
      setMapCenter(
        new navermaps.LatLng(
          currentPosition.latitude,
          currentPosition.longitude
        )
      );
    } else {
      console.error('현재 위치를 가져올 수 없습니다.');
    }
  };

  // 거리 구하기
  const getDistance = (pos1, pos2) => {
    const toRad = (value) => (value * Math.PI) / 180;
    const R = 6371;
    const dLat = toRad(pos2.latitude - pos1.latitude);
    const dLon = toRad(pos2.longitude - pos1.longitude);
    const lat1 = toRad(pos1.latitude);
    const lat2 = toRad(pos2.latitude);

    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const distance = R * c;
    return distance;
  };

  const handleCenterClick = (center) => {
    setSelectedCenter(center);
    setMapCenter(new navermaps.LatLng(center.latitude, center.longitude));
  };

  const selectedIconImage = optionCenter
    ? {
        content: `<img src=${HospitalChooseImg} alt="" style="margin: 0px; padding: 0px; border: 0px solid transparent; display: block; max-width: none; max-height: none; -webkit-user-select: none; position: relative; width: 60px; height: 60px; left: 0px; top: 0px; z-index: 2;">`,
        size: new navermaps.Size(48, 48),
        origin: new navermaps.Point(0, 0),
        anchor: new navermaps.Point(24, 24),
      }
    : {
        content: `<img src=${BuildingChooseImg} alt="" style="margin: 0px; padding: 0px; border: 0px solid transparent; display: block; max-width: none; max-height: none; -webkit-user-select: none; position: relative; width: 60px; height: 60px; left: 0px; top: 0px; z-index: 2;">`,
        size: new navermaps.Size(48, 48),
        origin: new navermaps.Point(0, 0),
        anchor: new navermaps.Point(24, 24),
      };

  const defaultIconImage = optionCenter
    ? {
        content: `<img src=${HospitalImg} alt="" style="margin: 0px; padding: 0px; border: 0px solid transparent; display: block; max-width: none; max-height: none; -webkit-user-select: none; position: relative; width: 40px; height: 40px; left: 0px; top: 0px;">`,
        size: new navermaps.Size(24, 24),
        origin: new navermaps.Point(0, 0),
        anchor: new navermaps.Point(12, 12),
      }
    : {
        content: `<img src=${BuildingImg} alt="" style="margin: 0px; padding: 0px; border: 0px solid transparent; display: block; max-width: none; max-height: none; -webkit-user-select: none; position: relative; width: 40px; height: 40px; left: 0px; top: 0px;">`,
        size: new navermaps.Size(24, 24),
        origin: new navermaps.Point(0, 0),
        anchor: new navermaps.Point(12, 12),
      };
  return (
    <>
      {mapCenter && (
        <NaverMap
          ref={mapRef}
          center={mapCenter}
          defaultZoom={15}
          style={{ width: '100%', height: '100%' }}
        >
          {currentPosition && (
            <Marker
              position={
                new navermaps.LatLng(
                  currentPosition.latitude,
                  currentPosition.longitude
                )
              }
              title="현재 위치"
              clickable={true}
              icon={{
                content: `<img src=${CurrentLocation} alt="" style="margin: 0px; padding: 0px; border: 0px solid transparent; display: block; max-width: none; max-height: none; -webkit-user-select: none; position: absolute; width: 40px; height: 40px; left: 0px; top: 0px;">`,
                size: new navermaps.Size(48, 48),
                origin: new navermaps.Point(0, 0),
                anchor: new navermaps.Point(24, 24),
              }}
            />
          )}

          {centers.map((center) => {
            const isSelected = optionCenter
              ? selectedCenter && selectedCenter.id === center.id
              : selectedCenter &&
                selectedCenter.support_center_id === center.support_center_id;

            return (
              <Marker
                key={optionCenter ? center.id : center.support_center_id}
                position={
                  new navermaps.LatLng(center.latitude, center.longitude)
                }
                title={center.name}
                clickable={true}
                icon={isSelected ? selectedIconImage : defaultIconImage} // 선택 여부에 따른 아이콘 설정
                onClick={() => {
                  handleCenterClick(center);
                }}
              />
            );
          })}
        </NaverMap>
      )}
      <SidePage
        selectedCenter={selectedCenter}
        optionCenter={optionCenter}
        sortedCenters={sortedCenters}
        handleCenterClick={handleCenterClick}
      />

      <ChooseMode
        setOptionCenter={setOptionCenter}
        moveToCurrentPosition={moveToCurrentPosition}
        click={click}
        setClick={setClick}
      />
    </>
  );
}

export default SupportCenterMap;
