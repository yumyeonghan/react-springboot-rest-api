import React from "react";
import {Seat} from "./Seat";

export function SeatList({seats = [], onAddClick}) {
    return (<React.Fragment>
        <h5 className="flex-grow-0"><b>좌석 목록</b></h5>
        <ul className="list-group products">
            {seats.map(v => <li key={v.seatId} className="list-group-item d-flex mt-3">
                <Seat {...v} onAddClick={onAddClick}/>
            </li>)}
        </ul>
    </React.Fragment>)
}
