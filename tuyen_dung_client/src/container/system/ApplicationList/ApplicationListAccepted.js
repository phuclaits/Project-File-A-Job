import React, { useEffect, useState } from 'react';
import {  scheduleInterviewService } from '../../../service/cvService'; // Import service
import ReactPaginate from 'react-paginate';
import { PAGINATION } from '../../utils/constant';
import styled from 'styled-components';
import { toast } from 'react-toastify'; // Import toast
import axios from 'axios';

import LoadingPage from '../../../components/Loading';
const Container = styled.div`
    font-family: Arial, sans-serif;
    padding: 20px;
`;

const Table = styled.table`
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
`;

const Th = styled.th`
    padding: 10px;
    border: 1px solid #ddd;
    background-color: #f2f2f2;
`;

const Td = styled.td`
    padding: 10px;
    border: 1px solid #ddd;
`;

const Pagination = styled(ReactPaginate)`
    display: flex;
    justify-content: center;
    margin-top: 20px;

    .active {
        font-weight: bold;
    }
`;

const ScheduleButton = styled.button`
    background-color: #4CAF50; /* Green */
    border: none;
    color: white;
    padding: 10px 20px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    cursor: pointer;
    &:hover {
        background-color: #45a049;
    }
`;

const Modal = styled.div`
    display: ${props => props.show ? 'block' : 'none'};
    position: fixed; /* Change to fixed */
    z-index: 1000;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgb(0,0,0);
    background-color: rgba(0,0,0,0.4);
    padding-top: 60px;
`;

const ModalContent = styled.div`
    background-color: #fefefe;
    margin: 5% auto;
    padding: 20px;
    border: 1px solid #888;
    width: 80%;
    max-width: 500px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    animation: animateModal 0.4s;

    @keyframes animateModal {
        from { transform: translateY(-50px); opacity: 0; }
        to { transform: translateY(0); opacity: 1; }
    }
`;

const CloseButton = styled.span`
    color: #aaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
    cursor: pointer;
    &:hover,
    &:focus {
        color: #000;
        text-decoration: none;
        cursor: pointer;
    }
`;

const FormGroup = styled.div`
    margin: 15px 0;
`;

const Label = styled.label`
    display: block;
    margin-bottom: 8px;
    font-weight: bold;
`;

const Input = styled.input`
    width: calc(100% - 22px);
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
`;

const ConfirmButton = styled.button`
    background-color: #4CAF50; /* Green */
    border: none;
    color: white;
    padding: 10px 20px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    cursor: pointer;
    border-radius: 4px;
    margin-top: 10px;
    &:hover {
        background-color: #45a049;
    }
`;

const ApplicationListAccepted = () => {
    const [dataCv, setDataCv] = useState([]);
    const [count, setCount] = useState(0);
    const [page, setPage] = useState(0);
    const [showModal, setShowModal] = useState(false);
    const [selectedCV, setSelectedCV] = useState(null);
    const [interviewTime, setInterviewTime] = useState('');
    const [isLoading, setIsLoading] = useState(false)
    useEffect(() => {
        fetchAcceptedCVs(page);
    }, [page]);

    const getAcceptedCVsService = (data) => {
        return axios.get("http://localhost:8080/app-tuyen-dung/api/v1/cv/status", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token_user")}`,
          },
          params: {
            idCompany: JSON.parse(localStorage.getItem("userData")).idCompany,
            status: "accepted",
            limit: data.limit,
            offset: data.offset,
          },
        });
      };

    const fetchAcceptedCVs = async (page) => {
        try {
          let res = await getAcceptedCVsService({
            limit: PAGINATION.pagerow,
            offset: page * PAGINATION.pagerow,
          });
          console.log(res)
          if (res) {
            setDataCv(res.data.result.content);
            setPage(0);
            setCount(Math.ceil(res.data.result.totalElements / PAGINATION.pagerow));
          }
        } catch (error) {
          console.log(error);
        }
      };

    // Thêm toast khi đăng ký thành công
    const notifySuccess = (message) => toast.success(message);

    // Thêm toast khi đăng ký thất bại
    const notifyError = (message) => toast.error(message);
    const handlePageClick = (event) => {
        setPage(event.selected);
    }

    const handleScheduleClick = (cv) => {
        setSelectedCV(cv);
        setShowModal(true);
    }

    const handleCloseModal = () => {
        setShowModal(false);
        setSelectedCV(null);
        setInterviewTime('');
    }

    const handleScheduleInterview = async () => {
        if (selectedCV && interviewTime) {
            setIsLoading(true);
            try {
                let res = await scheduleInterviewService({
                    cvId: selectedCV.id,
                    interviewTime
                });
                
                if (res) {
                    setIsLoading(false);
                    handleCloseModal();
                    fetchAcceptedCVs(page); // Refresh the list
                    notifySuccess("Lên lịch phỏng vấn thành công!"); // Thông báo toast thành công
                } else {
                    setIsLoading(false);
                    console.log(res.errMessage);
                    notifyError("Lên lịch phỏng vấn thất bại!"); // Thông báo toast thất bại
                }
            } catch (error) {
                console.log(error);
                notifyError("Lên lịch phỏng vấn thất bại!"); // Thông báo toast thất bại
            }
        } else {
            notifyError("Vui lòng nhập đầy đủ thông tin!"); // Thông báo toast khi thông tin không hợp lệ
        }
    }

    return (
        <Container>
            <h1>Đã chấp nhận</h1>
            <Table>
                <thead>
                    <tr>
                    <Th>ID Người dùng</Th>
                    <Th>Họ và tên</Th>
                    <Th>Email</Th>
                    <Th>ID Bài đăng</Th>
                    <Th>Trạng thái</Th>
                    <Th>Thời gian phỏng vấn</Th> 
                    <Th>Hành động</Th> 

                    </tr>
                </thead>
                <tbody>
                {dataCv.filter((cv) => cv !== null).length > 0 ? (
                    dataCv.filter((cv) => cv !== null).map((cv, index) => (
                    <tr key={index}>
                        <Td>{cv.userId || ""}</Td>
                        <Td>{`${cv.firstName || ""} ${cv.lastName || ""}`}</Td>
                        <Td>{cv.email || ""}</Td>
                        <Td>{cv.postId || ""}</Td>
                        <Td>
                        <label className="badge badge-success">{cv.status || ""}</label>
                        </Td>
                        <td>
                        {cv.interviewTime
                            ? new Date(cv.interviewTime).toLocaleString("en-GB", {
                                day: "2-digit",
                                month: "2-digit",
                                year: "numeric",
                                hour: "2-digit",
                                minute: "2-digit",
                                second: "2-digit",
                                hour12: false, // Định dạng 24h
                            })
                            : "N/A"}
                        </td>
                        <Td>
                        <ScheduleButton onClick={() => handleScheduleClick(cv)}>
                            Lên lịch phỏng vấn
                        </ScheduleButton>
                        </Td>
                    </tr>
                    ))
                ) : (
                    <tr>
                    <td colSpan="7" style={{ textAlign: "center" }}>
                        <p>Không có ứng viên nào được chấp nhận. Tìm kiếm ứng viên <a href='https://project-file-a-job-git-master-la-hoang-phucs-projects.vercel.app/admin/list-candiate/'>Tại đây!</a></p>
                    </td>
                    </tr>
                )}
                </tbody>
                            </Table>
                            <ReactPaginate
                                        forcePage={page}
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
                                        onPageChange={handlePageClick}
                                    />

                            <Modal show={showModal}>
                                <ModalContent>
                                    <CloseButton onClick={handleCloseModal}>&times;</CloseButton>
                                    <h2>Lên lịch phỏng vấn</h2>
                                    <FormGroup>
                                        <Label>Mã ứng viên:</Label>
                                        <p>{selectedCV && selectedCV.idUser ? selectedCV.idUser : ''}</p>
                                    </FormGroup>
                                    <FormGroup>
                                        <Label>Thời gian phỏng vấn:</Label>
                                        <Input 
                                            type="datetime-local" 
                                            value={interviewTime}
                                            onChange={(e) => setInterviewTime(e.target.value)}
                                        />
                                    </FormGroup>
                                    <ConfirmButton onClick={handleScheduleInterview}>Xác nhận</ConfirmButton>
                                </ModalContent>
                            </Modal>
                            {isLoading && <LoadingPage />}
        </Container>
    );
};

export default ApplicationListAccepted;
