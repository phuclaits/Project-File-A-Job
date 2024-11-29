import React from 'react'
import { useEffect, useState } from 'react';
import { DeleteJobLevel, getAllJobLevel } from '../../../service/userService';
import moment from 'moment';
import { PAGINATION } from '../../utils/constant';
import ReactPaginate from 'react-paginate';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import CommonUtils from '../../utils/CommonUtils';
import {Input, Modal} from 'antd'
import { ExclamationCircleOutlined } from '@ant-design/icons';
const {confirm} = Modal

const ManageJobLevel = () => {
    const [dataJobLevel, setdataJobLevel] = useState([])
    const [count, setCount] = useState('')
    const [numberPage, setnumberPage] = useState('')
    const [search,setSearch] = useState('')

    useEffect(() => {
        try {
            let fetchData = async () => {
                let arrData = await getAllJobLevel({

                    type: 'JOBLEVEL',
                    limit: PAGINATION.pagerow,
                    offset: 0,
                    search: CommonUtils.removeSpace(search)

                })
                if (arrData && arrData.errCode === 0) {
                    setnumberPage(0)
                    setdataJobLevel(arrData.data.content)
                    setCount(Math.ceil(arrData.data.totalElements / PAGINATION.pagerow))
                }
            }
            fetchData();
        } catch (error) {
            console.log(error)
        }

    }, [search])
    let handleDeleteJobLevel = async (code) => {
    let res = await DeleteJobLevel(code);
    if (res && res.errCode === 0) {
        toast.success(res.errMessage);

        // Thêm setTimeout để reset trang sau 2 giây
        setTimeout(async () => {
            let arrData = await getAllJobLevel({
                type: 'JOBLEVEL',
                limit: PAGINATION.pagerow,
                offset: numberPage,
                search: CommonUtils.removeSpace(search),
            });

            if (arrData && arrData.errCode === 0) {
                setdataJobLevel(arrData.data.content);
                setCount(Math.ceil(arrData.data.totalElements / PAGINATION.pagerow));
            }
        }, 2000);  // Chờ 2 giây trước khi tải lại danh sách

    } else {
        toast.error(res.errMessage);
    }
};
    let handleChangePage = async (number) => {
        setnumberPage(number.selected)
        let arrData = await getAllJobLevel({

            type: 'JOBLEVEL',
            limit: PAGINATION.pagerow,
            offset: number.selected,
            search: CommonUtils.removeSpace(search)

        })
        if (arrData && arrData.errCode === 0) {
            setdataJobLevel(arrData.data.content)

        }
    }
    const handleSearch = (value) => {
        setSearch(value)
    }
    const confirmDelete = (id) => {
        confirm({
            title: 'Bạn có chắc muốn xóa trình độ này này?',
            icon: <ExclamationCircleOutlined />,    
            onOk() {
                handleDeleteJobLevel(id)
            },
        
            onCancel() {
            },
          });
    }
    return (
        <div>
            <div className="col-12 grid-margin">
                <div className="card">
                    <div className="card-body">
                        <h4 className="card-title">Danh sách cấp bậc</h4>
                        <Input.Search onSearch={handleSearch} className='mt-5 mb-5' placeholder="Nhập tên cấp bậc" allowClear enterButton="Tìm kiếm">
                                    
                                    </Input.Search>
                        <div className="table-responsive pt-2">
                            <table className="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>
                                            STT
                                        </th>
                                        <th>
                                            Tên cấp bậc
                                        </th>
                                        <th>
                                            Mã code
                                        </th>

                                        <th>
                                            Thao tác
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {dataJobLevel && dataJobLevel.length > 0 &&
                                        dataJobLevel.map((item, index) => {

                                            return (
                                                <tr key={index}>
                                                    <td>{index + 1 + numberPage * PAGINATION.pagerow}</td>
                                                    <td>{item.value}</td>
                                                    <td>{item.code}</td>
                                                    <td>
                                                        <Link style={{ color: '#4B49AC' }} to={`/admin/edit-job-level/${item.code}/`}>Sửa</Link>
                                                        &nbsp; &nbsp;
                                                        <a style={{ color: '#4B49AC' }} href="#" onClick={(event) => confirmDelete(item.code)} >Xóa</a>
                                                    </td>
                                                </tr>
                                            )
                                        })
                                    }

                                </tbody>
                            </table>
                            {
                                            dataJobLevel && dataJobLevel.length == 0 && (
                                                <div style={{ textAlign: 'center' }}>

                                                    Không có dữ liệu

                                                </div>
                                            )
                                        }
                        </div>
                    </div>
                    <ReactPaginate
                        forcePage={numberPage}
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

export default ManageJobLevel
