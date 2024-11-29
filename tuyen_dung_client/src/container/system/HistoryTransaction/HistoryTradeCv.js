import React from 'react'
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { PAGINATION } from '../../utils/constant';
import ReactPaginate from 'react-paginate';
import moment from 'moment';
import { DatePicker } from 'antd';
import CommonUtils from '../../utils/CommonUtils';
import { getHistoryTradeCv } from '../../../service/userService';
const HistoryTradeCv = () => {
    const { RangePicker } = DatePicker;
    const [user, setUser] = useState({})
    const [dataSum,setDataSum] = useState(0)
    const [fromDatePost,setFromDatePost] = useState('')
    const [toDatePost,setToDatePost] = useState('')



    const [data, setData] = useState([])
    const [count, setCount] = useState('')

    const [numberPage, setnumberPage] = useState('')

    let sendParams = {
        limit: PAGINATION.pagerow,
        offset: 0,
        fromDate: '',
        toDate: '',
        companyId: user.companyId
    }

    let getData = async (params) => {

        let arrData = await getHistoryTradeCv(params)
        if (arrData && arrData.code === 200) {
            setData(arrData.result.content)
            setCount(Math.ceil(arrData.result.totalElements / PAGINATION.pagerow))
        }
    }

    let onDatePicker = async (values) => {
        const userData = localStorage.getItem('userData'); // Lấy chuỗi JSON từ localStorage
        const parsedUserData = JSON.parse(userData);
        let fromDate =''
        let toDate = ''
        if (values) {
            fromDate = values[0].format('YYYY-MM-DD')
            toDate = values[1].format('YYYY-MM-DD')
        }
        getData({
            ...sendParams,
            fromDate,
            toDate,
            offset: 0,
            companyId: parsedUserData.idCompany
        })
        setFromDatePost(fromDate)
        setToDatePost(toDate)
    }

    let handleChangePage = async (number) => {
        const userData = localStorage.getItem('userData'); 
        const parsedUserData = JSON.parse(userData);
        setnumberPage(number.selected)
        getData({
            ...sendParams,
            offset: number.selected,
            companyId: parsedUserData.idCompany
        })
    }
    let handleOnClickExport =async ()=>{
        const userData = localStorage.getItem('userData'); 
        const parsedUserData = JSON.parse(userData);
        let res = await getHistoryTradeCv({
            ...sendParams,
            limit: '',
            offset: '',
            fromDate: fromDatePost,
            toDate: toDatePost,
            companyId: parsedUserData.idCompany
        })
        if(res.code === 200){
            let formatData = res.result.content.map((item)=> {
                let obj = {
                    'Tên gói': item.packageCvData.name,
                    'Mã giao dịch': item.id,
                    'Số lượng mua': item.amount,
                    'Đơn giá': item.packageCvData.price + " USD",
                    'Tên người mua': item.userData.firstName + ' ' + item.userData.lastName,
                    'Thời gian mua': moment(item.createdAt).format('DD-MM-YYYY HH:mm:ss')
                }
                return obj
            })
            await CommonUtils.exportExcel(formatData,"History Trade Cv","History Trade Cv")
        }
        
    }

    useEffect(async () => {
        const userData = JSON.parse(localStorage.getItem('userData'));
        setUser(userData)

        getData({...sendParams,companyId: userData.idCompany})
    }, [])

    return (
                
                <div className="col-12 grid-margin">
                    <div className="card">
                        <div className="card-body">
                            <h4 className="card-title">Lịch sử thanh toán các gói bài đăng</h4>
                            <button  style={{float:'right'}} onClick={() => handleOnClickExport()} >Xuất excel <i class="fa-solid fa-file-excel"></i></button>
                            <RangePicker onChange={onDatePicker}
                                format={'DD/MM/YYYY'}
                            ></RangePicker>


                            <div className="table-responsive pt-2">
                                <table className="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>
                                                STT
                                            </th>
                                            <th>
                                                Tên gói
                                            </th>
                                            <th>
                                                Mã giao dịch
                                            </th>
                                            <th>
                                                Số lượng đã mua
                                            </th>
                                            <th>
                                                Đơn giá
                                            </th>
                                            <th>
                                                Người mua
                                            </th>
                                            <th>
                                                Thời gian mua
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {data && data.length > 0 &&
                                            data.map((item, index) => {

                                                return (
                                                    <tr key={index}>
                                                        <td>{index + 1 + numberPage * PAGINATION.pagerow}</td>
                                                        <td>{item.packageCvData.name}</td>
                                                        <td>{item.id}</td>
                                                        <td>{item.amount}</td>
                                                        <td style={{ textAlign: 'right' }}>{item.packageCvData.price} USD</td>
                                                        <td>{item.userData.firstName + ' ' + item.userData.lastName }</td>
                                                        <td>{moment(item.createdAt).format('DD-MM-YYYY HH:mm:ss')}</td>
                                                    </tr>
                                                )
                                            })
                                        }
                                    </tbody>
                                </table>
                                        {
                                            data && data.length == 0 && (
                                                <div style={{ textAlign: 'center'}}>

                                                    Không có dữ liệu

                                                </div>
                                            )
                                        }
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

    )
}

export default HistoryTradeCv
