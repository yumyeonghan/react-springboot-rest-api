import React from "react";

export function SummarySeat({seatId}) {
    return (<div className="row">
        <h6 className="p-0"><span className="badge bg-dark text-">{seatId}번 좌석</span></h6>
    </div>)
}
