import React, { useEffect, useState } from 'react';
import { getUnderReviewCVsService, acceptCVService, rejectCVService } from '../../../service/cvService';
import { getDetailCompanyById } from "../../../service/userService";
import ReactPaginate from 'react-paginate';
import { PAGINATION } from '../../utils/constant';
import styled from 'styled-components';
import { toast } from 'react-toastify';
import { getPhoneByUserId } from "../../../apiCall/user";
import { sendUserNotification } from "../../../apiCall/message";
import axios from 'axios';

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

const Button = styled.button`
    padding: 8px 12px;
    border: none;
    cursor: pointer;
    border-radius: 4px;
    margin-right: 4px;
`;

const Pagination = styled(ReactPaginate)`
    display: flex;
    justify-content: center;
    margin-top: 20px;

    .active {
        font-weight: bold;
    }
`;

const ApplicationListUnderReview = () => {
    const [dataCv, setDataCv] = useState([]);
    const [count, setCount] = useState(0);
    const [page, setPage] = useState(0);

    useEffect(() => {
        fetchUnderReviewCVs(page);
    }, [page]);

    const getUnderReviewCVsService = (data) => {
        const userData = JSON.parse(localStorage.getItem("userData"));
        return axios.get("http://localhost:8080/app-tuyen-dung/api/v1/cv/status", {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token_user")}`,
            },
            params: {
                idCompany: userData.idCompany,
                status: "under-review",
                limit: data.limit,
                offset: data.offset,
            },
        });
    };

    const handleSendNotification = async (userId, status) => {
        try {
            const phoneNumber = await getPhoneByUserId(userId);
            const userData = localStorage.getItem("userData");
            const userObject = JSON.parse(userData);
            const response = await getDetailCompanyById(userObject.idCompany);
            let subject = "";
            let message = "";

            switch (status) {
                case "accepted":
                    subject = `${response.result?.name} đã chấp nhận CV của bạn!`;
                    message = `Chúc mừng! ${response.result?.name} đã đồng ý với hồ sơ của bạn.`;
                    break;
                case "rejected":
                    subject = `${response.result?.name} đã từ chối CV của bạn.`;
                    message = `Rất tiếc! Hồ sơ của bạn đã bị ${response.result?.name} từ chối.`;
                    break;
                case "reviewed":
                    subject = `${response.result?.name} đang xem xét CV của bạn.`;
                    message = `Hồ sơ của bạn đang được ${response.result?.name} xem xét. Vui lòng chờ thông báo tiếp theo.`;
                    break;
                default:
                    subject = "Trạng thái không xác định.";
                    message = "Vui lòng kiểm tra lại trạng thái hồ sơ.";
            }

            const payload = {
                subject: subject,
                image: response.result?.thumbnail,
                message: message,
                attachedUrl: "/home",
                sender: "ADMIN",
                userId: [phoneNumber.result],
            };

            console.log("payload:", payload);
            const notificationResponse = await sendUserNotification(payload);
            console.log("API response:", notificationResponse);
        } catch (error) {
            console.error("Error sending notification:", error);
        }
    };
    const fetchUnderReviewCVs = async (page) => {
        try {
            let res = await getUnderReviewCVsService({
                limit: PAGINATION.pagerow,
                offset: page * PAGINATION.pagerow,
            });
            console.log(res.data.result.content)
            if (res) {
                setDataCv(res.data.result.content);
                setPage(0);
                setCount(Math.ceil(res.data.result.totalElements / PAGINATION.pagerow));
            }
        } catch (error) {
            console.log(error);
        }
    }

    const handlePageClick = (event) => {
        setPage(event.selected);
    }

    const notifyAcceptSuccess = () => toast.success("Chấp nhận hồ sơ thành công!");

    const notifyAcceptError = () => toast.error("Chấp nhận hồ sơ thất bại!");

    const handleAccept = async (cvId) => {
        try {
            let res = await acceptCVService(cvId.id);
            if (res) {
                handleSendNotification(cvId.userId, "accepted")
                fetchUnderReviewCVs(page);
                notifyAcceptSuccess();
            } else {
                console.log(res.errMessage);
                notifyAcceptError();
            }
        } catch (error) {
            console.log(error);
            notifyAcceptError();
        }
    }

    const notifyRejectSuccess = () => toast.success("Từ chối hồ sơ thành công!");

    const notifyRejectError = () => toast.error("Từ chối hồ sơ thất bại!");

    const handleReject = async (cvId) => {
        try {
            let res = await rejectCVService(cvId.id);
            if (res) {
                handleSendNotification(cvId.userId, "rejected")
                fetchUnderReviewCVs(page);
                notifyRejectSuccess();
            } else {
                console.log(res.errMessage);
                notifyRejectError();
            }
        } catch (error) {
            console.log(error);
            notifyRejectError();
        }
    }

    return (
        <Container>
            <h1>Danh sách đang xem xét</h1>
            <Table>
                <thead>
                    <tr>
                        <Th>ID Người dùng</Th>
                        <Th>Họ và tên</Th>
                        <Th>Email</Th>
                        <Th>ID Bài đăng</Th>
                        <Th>Trạng thái</Th>
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
                                <Td>{cv.status}</Td>
                                <Td>
                                    <Button className="btn-accept" onClick={() => handleAccept(cv)}>Chấp nhận</Button>
                                    <Button className="btn-reject" onClick={() => handleReject(cv)}>Từ chối</Button>
                                </Td>
                            </tr>
                        ))) : (
                        <tr>
                            <td colSpan="7" style={{ textAlign: "center" }}>
                                <p>Không có ứng viên nào được xem xét. Tìm kiếm ứng viên <a href='http://localhost:3000/admin/list-candiate/'>Tại đây!</a></p>
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
        </Container>
    );
};

export default ApplicationListUnderReview;
