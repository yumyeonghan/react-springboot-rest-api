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

    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const pageSize = 3;

    const fetchPageData = (pageNumber) => {
        axios.get(`/api/v1/page-seats?page=${pageNumber}&size=${pageSize}`)
            .then(response => {
                const { seats, totalPages } = response.data;
                setSeats(seats);
                setTotalPages(totalPages);
            })
            .catch(error => {
                console.error("Error fetching data:", error);
            });
    };

    useEffect(() => {
        fetchPageData(page);
    }, [page]);


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
                    alert("좌석 예약이 되었습니다.");
                })
                .catch((error) => {
                    alert(error.response.data.errorMessage);
                });
        }
    };

    const handlePrevPage = () => {
        setPage(prevPage => Math.max(prevPage - 1, 1));
    };

    const handleNextPage = () => {
        setPage(prevPage => Math.min(prevPage + 1, totalPages));
    };

    return (<div className="container-fluid">
        <div className="row justify-content-center m-4">
            <h1 className="text-center">Seat Reservation</h1>
        </div>
        <div className="card">
            <div className="row">
                <div className="col-md-8 mt-4 d-flex flex-column align-items-start p-3 pt-0">
                    <SeatList seats={seats} onAddClick={handleAddClicked}/>
                    <div className="d-flex justify-content-center">
                        <button className="btn btn-secondary m-2" onClick={handlePrevPage} disabled={page === 1}>이전</button>
                        <button className="btn btn-secondary m-2" onClick={handleNextPage} disabled={page === totalPages}>다음</button>
                    </div>
                </div>
                <div className="col-md-4 summary p-4">
                    <Summary seatReservation={seatReservations} onReserveSubmit={handleOrderSubmit}/>
                </div>
            </div>
        </div>
    </div>);
}

export default App;
