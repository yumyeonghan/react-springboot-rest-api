import './App.css';
import 'bootstrap/dist/css/bootstrap.css';
import React, {useEffect, useState} from 'react';
import {SeatList} from "./components/SeatList";
import {Summary} from "./components/Summary";
import axios from "axios";

function App() {
    const [seats, setSeats] = useState([{seatId: '1', category: '개방형', seatStatus: '예약 가능'}, {
        seatId: '2', category: '페쇠형', seatStatus: '예약 가능'
    }, {seatId: '3', category: '개방형', seatStatus: '예약 가능'},]);
    const [seatReservations, setSeatReservations] = useState([]);

    const handleAddClicked = (seatId) => {
        const seat = seats.find((v) => v.seatId === seatId);
        const found = seatReservations.find((v) => v.seatId === seatId);
        const updatedSeatReservations = found ? seatReservations.filter((v) => v.seatId !== seatId) : [...seatReservations, seat];
        setSeatReservations(updatedSeatReservations);
    };

    useEffect(() => {
        axios.get('/api/v1/seats')
            .then(v => setSeats(v.data));
    }, []);

    const handleOrderSubmit = (reserve) => {
        if (seatReservations.length === 0) {
            alert("좌석을 추가해 주세요!");
        } else if (seatReservations.length > 1) {
            alert("좌석을 하나만 선택해 주세요!")
        } else {
            const selectedSeatId = seatReservations[0].seatId;
            axios
                .post('/api/v1/reserves', {
                    studentId: reserve.studentId, studentName: reserve.studentName, seatId: selectedSeatId,
                })
                .then(() => {
                    alert("정상적으로 예약되었습니다.");
                }, (error) => {
                    alert("서버 장애");
                    console.error(error);
                });
        }
    };

    return (<div className="container-fluid">
        <div className="row justify-content-center m-4">
            <h1 className="text-center">Seat Reservation</h1>
        </div>
        <div className="card">
            <div className="row">
                <div className="col-md-8 mt-4 d-flex flex-column align-items-start p-3 pt-0">
                    <SeatList seats={seats} onAddClick={handleAddClicked}/>
                </div>
                <div className="col-md-4 summary p-4">
                    <Summary seatReservation={seatReservations} onReserveSubmit={handleOrderSubmit}/>
                </div>
            </div>
        </div>
    </div>);
}

export default App;
