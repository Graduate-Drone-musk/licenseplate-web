import React, { useState } from "react";
import { getlisenceApi } from "../api/api";
import "../css/components/Inputdate.css";
import Lisence from "./Lisence";

function Inputdate() {
  const onSubmit = (e) => {
    e.preventDefault();
    console.log(e.target.start.value.split("-").join(""));
    console.log(e.target.end.value);
    if (e.target.start.value === "" || e.target.end.value === "") {
      alert("날짜를 입력해 주세요!");
    } else {
      setVisible(true);
      
      getlisenceApi({
        startTime: e.target.start.value.split("-").join(""),
        endTime: e.target.end.value.split("-").join(""),
        showCount: "10",
        page: "1",
        allPageCount: "-1",
      }).then(data => {
        setLisence([data]);  
      });
    }
  };

  const initialLisences = [
    {
      status: "",
      message: "",
      allPageCount: "0",
      illegalList: [
        {
          phone: "",
          userName: "",
          date: "",
          licenseplate: "",
          lpUrl: "",
          originUrl: "",
        },
      ],
    },
  ];

  const [lisence, setLisence] = useState(initialLisences);
  const [visible, setVisible] = useState(false);

  return (
    <>
      <div className="shadowBox Inputdate">
        <form className="date-form" onSubmit={onSubmit}>
          날짜를 입력하세요
          <div className="top">
            <input type="date" className="date" id="start" />
            ~
            <input type="date" className="date" id="end" />
          </div>
          <button type="submit" className="submit-btn btn">
            번호판 조회
          </button>
        </form>
      </div>
      <div className={visible ? "shadowBox lisences" : "nonvisible"}>
        <div className="lisencesinfo">
          {lisence[0].illegalList.map((ls, index) => (
            <Lisence lisence={lisence[0].illegalList[index]} key={index} />
          ))}
        </div>

        <div className="page">-{lisence[0].allPageCount} page-</div>
      </div>
    </>
  );
}

export default Inputdate;