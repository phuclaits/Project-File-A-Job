import React from 'react'
import { useEffect, useState } from 'react';
import { getAllListCvByUserIdService } from '../../service/cvService';

import { PAGINATION } from '../utils/constant';
import ReactPaginate from 'react-paginate';
import { Link } from 'react-router-dom';
import { useParams } from "react-router-dom";
import moment from 'moment';


const ManageCvCandidate = (props) => {
    const [dataCv, setdataCv] = useState([])
    const [count, setCount] = useState('')
    const [numberPage, setnumberPage] = useState('')
    const [user, setUser] = useState({})
    useEffect(() => {
        const userData = JSON.parse(localStorage.getItem('userData'));
        setUser(userData)
        if (userData) {
            try {
                let fetchData = async () => {
                    let arrData = await getAllListCvByUserIdService({
                        limit: PAGINATION.pagerow,
                        offset: 0,
                        userId: userData.id
                    })
                    console.log(arrData)
                    if (arrData && arrData.data.code === 200) {
                        setdataCv(arrData.data.result.content)
                        console.log(arrData.data.result.content)

                        setCount(Math.ceil(arrData.data.result.totalElements / PAGINATION.pagerow))
                    }
                }
                fetchData();
            } catch (error) {
                console.log(error)
            }
        }


    }, [])

    let handleChangePage = async (number) => {
        setnumberPage(number.selected)
        let arrData = await getAllListCvByUserIdService({
            limit: PAGINATION.pagerow,
            offset: number.selected,
            userId: user.id

        })
        if (arrData && arrData.data.code === 200) {
            setdataCv(arrData.data.content)
        
        }
    }

    console.log("dataCv", dataCv);
    
    return (

        <div>

            <div className="col-12 grid-margin">
                <div className="card">
                    <div className="card-body">
                        <h4 className="card-title">Danh sách Công Việc Đã Nộp</h4>
                        <div className="table-responsive pt-2">
                            <table className="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>
                                            STT
                                        </th>
                                        <th>
                                            Tên công việc
                                        </th>
                                        <th>
                                            Ngành
                                        </th>
                                        <th>
                                            Chức vụ
                                        </th>
                                        <th>
                                            Địa chỉ
                                        </th>
                                        <th>
                                            Thời gian nộp
                                        </th>
                                        <th>
                                            Trạng thái
                                        </th>
                                        <th>
                                            Thời gian phỏng vấn
                                        </th>
                                        <th>
                                            Thao tác
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {dataCv && dataCv.length > 0 &&
                                        dataCv.map((item, index) => {

                                            return (
                                                <tr key={index}>
                                                    <td>{index + 1 + numberPage * PAGINATION.pagerow}</td>
                                                    <td>{item.name}</td>
                                                    <td>{item.valueJobType}</td>
                                                    <td>{item.valueJobLevel}</td>
                                                    <td>{item.valueProvince}</td>
                                                    <td>{moment(item.createdAtCv).format('DD-MM-YYYY HH:mm:ss')}</td>
                                                    <td>
    {(() => {
        if (item.status === 'accepted') {
            return 'Chấp nhận';
        } else if (item.status === 'rejected') {
            return 'Từ chối';
        } else if (item.status === 'pending') {
            return 'Chờ xử lý';
        } else if (item.status === 'interview') {
            return 'Phỏng vấn';
        } else {
            return 'Trạng thái không xác định';
        }
    })()}
</td>
<td>
        {item.interviewTime ? moment(item.interviewTime).format('DD-MM-YYYY HH:mm:ss') : 'N/A'}
    </td>
                                                    <td>
                                                        <Link style={{ color: '#4B49AC', cursor: 'pointer' }} to={`/detail-job/${item.idPost}/`}>Xem công việc</Link>
                                                        &nbsp; &nbsp;
                                                        <Link style={{ color: '#4B49AC', cursor: 'pointer' }} to={`/candidate/cv-detail/${item.idCv}`}>Xem CV đã nộp</Link>
                                                        &nbsp; &nbsp;
                                                    </td>
                                                </tr>
                                            )
                                        })
                                    }

                                </tbody>
                            </table>
                        </div>
                    </div>
                    <ReactPaginate
                        previousLabel={'Quay lại'}
                        nextLabel={'Tiếp'}
                        breakLabel={'...'}
                        pageCount={count}
                        marginPagesDisplayed={3}
                        containerClassName={"pagination justify-content-center pb-3"}
                        pageClassName={"page-item"}
                        pageLinkClassName={"page-link"}
                        previousLinkClassName={"page-link"}
                        previousClassName={"page-item"}
                        nextClassName={"page-item"}
                        nextLinkClassName={"page-link"}
                        breakLinkClassName={"page-link"}
                        breakClassName={"page-item"}
                        activeClassName={"active"}
                        onPageChange={handleChangePage}
                    />
                </div>

            </div>



        </div>
    )
}

export default ManageCvCandidate
