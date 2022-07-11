import React from "react";
import "../css/components/Lisence.css";
function Lisence({ lisence }) {
  return (
    <>
      <div className="lisence">
        <img src={lisence.originUrl} alt="" />

        <div className="description">
          <div className="plate">{lisence.licenseplate}</div>
          <div>차주 : {lisence.userName}</div>
          <div>찍힌 날짜 : {lisence.date}</div>
          <div>핸드폰 번호 : {lisence.phone}</div>
        </div>
      </div>
    </>
  );
}

export default Lisence;
