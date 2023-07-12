import {SummarySeat} from "./SummarySeat";
import React, {useState} from "react";

export function Summary({seatReservation = [], onReserveSubmit}) {
    const seatId = seatReservation.map(value => value.seatId)
    const [reserve, setReserve] = useState({
        studentId: "", studentName: ""
    });
    const handleStudentIdInputChanged = (e) => setReserve({...reserve, studentId: e.target.value})
    const handleStudentNameInputChanged = (e) => setReserve({...reserve, studentName: e.target.value})
    const handleSubmit = (e) => {
        if (reserve.studentId === "" || reserve.studentName === "") {
            alert("학번과 이름을 입력해 주세요!")
        } else {
            onReserveSubmit(reserve);
        }
    }
    return (<>
        <div>
            <h5 className="m-0 p-0"><b>Summary</b></h5>
        </div>
        <hr/>
        {seatReservation.map(v => <SummarySeat key={v.seatId} seatId={v.seatId}/>)}
        <form>
            <div className="mb-3">
                <label htmlFor="studentId" className="form-label">학번</label>
                <input type="text" className="form-control mb-1" value={reserve.studentId}
                       onChange={handleStudentIdInputChanged}
                       id="studentId"/>
            </div>
            <div className="mb-3">
                <label htmlFor="studentName" className="form-label">이름</label>
                <input type="text" className="form-control mb-1" value={reserve.studentName}
                       onChange={handleStudentNameInputChanged}
                       id="studentName"/>
            </div>
            <div>00:00분에 예약이 초기화 됩니다.</div>
        </form>
        <div className="row pt-2 pb-2 border-top">
            <h5 className="col">좌석 번호</h5>
            <h5 className="col text-end">{seatId}번</h5>
        </div>
        <button className="btn btn-dark col-12" onClick={handleSubmit}>예약하기</button>
    </>)
}
