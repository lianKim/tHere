import React, { useEffect, useMemo, useRef, useState } from 'react';
import styled from 'styled-components';
import { Map, MarkerClusterer, Polyline } from 'react-kakao-maps-sdk';
import CustomMapMarker from './CustomMapMarker';
import { useTargetPlaceInfoValue } from '../../contexts/TargetPlaceInfoContext';
import { changeKakaoMapBound } from '../../lib/hooks/useJourneyDetail';

const JourneyMapHolder = styled.div`
  width: 40%;
  height: 100%;
  background-color: grey;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export default function JourneyMap({ setFocus, hoverPlace }) {
  const [markerPosition, setMarkerPosition] = useState([]);
  const targetPlacesData = useTargetPlaceInfoValue();
  const mapRef = useRef();

  // 장소 주소들이 변경되었을 때, bounds가 변경되면 bounds를 변경해줌
  const bounds = useMemo(() => changeKakaoMapBound(targetPlacesData), [targetPlacesData]);

  // map이 생성되었을 때, map의 bound를 결정해줌
  useEffect(() => {
    const map = mapRef.current;
    if (map && targetPlacesData.length !== 0) {
      map.setBounds(bounds, 100);
    }
  }, [bounds, targetPlacesData]);

  // markerPosition을 변경해줌
  useEffect(() => {
    if (targetPlacesData.length !== 0) {
      const newMarkerPosition = [];
      targetPlacesData.forEach((place) => {
        const lat = place.latitude;
        const lng = place.longitude;
        newMarkerPosition.push({ lat, lng });
      });
      setMarkerPosition(newMarkerPosition);
    }
  }, [targetPlacesData]);

  return (
    <JourneyMapHolder>
      <Map
        center={{
          // 지도의 중심좌표
          lat: 33.450701,
          lng: 126.570667,
        }}
        style={{
          // 지도의 크기
          width: '100%',
          height: '100%',
        }}
        level={3}
        ref={mapRef}
      >
        <Polyline
          path={[markerPosition]}
          strokeWeight={2} // 선의 두께 입니다
          strokeColor="#51A863" // 선의 색깔입니다
          strokeOpacity={1} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
        />
        <MarkerClusterer
          averageCenter
          minLevel={6}
          minClusterSize={5}
        >
          {targetPlacesData?.map((place) => (
            <CustomMapMarker
              position={{ lat: place.latitude, lng: place.longitude }}
              thumbnail={place.imageUrls[0]}
              key={`${place.placeName}-${place.placeId}`}
              setFocus={setFocus}
              placeId={place.placeId}
              hoverPlace={hoverPlace}
              placeCategory={place.placeCategory}
            />
          ))}
        </MarkerClusterer>

      </Map>
    </JourneyMapHolder>
  );
}
