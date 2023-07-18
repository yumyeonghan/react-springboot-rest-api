import React from "react";
import chair from '../chair.png';

export function Seat(props) {
    const seatId = props.seatId;
    const category = props.category;
    const seatStatus = props.seatStatus;

    const handleAddBtnClicked = () => {
        props.onAddClick(seatId);
    };

    return <>
        <div className="col-2">
            <img className="img-fluid" src={chair} alt=""/>
        </div>
        <div className="col text-center price">{seatId}번</div>
        <div className="col text-center price">{category}</div>
        <div className="col text-center price">{seatStatus}</div>
        <div className="col text-center price">
            <button onClick={handleAddBtnClicked} className="btn btn-small btn-outline-dark">예약</button>
        </div>
    </>
}
