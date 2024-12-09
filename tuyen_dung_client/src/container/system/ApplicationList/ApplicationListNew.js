import React, { useEffect, useState } from "react";
import {
  acceptCVService,
  rejectCVService,
  reviewCVService,
} from "../../../service/cvService";
import { getDetailCompanyById } from "../../../service/userService";
import ReactPaginate from "react-paginate";
import { PAGINATION } from "../../utils/constant";
import { toast } from "react-toastify";
import "./ApplicationListNew.css";
import { getPhoneByUserId } from "../../../apiCall/user";
import { sendUserNotification } from "../../../apiCall/message";
import axios from "axios";
import { Link } from "react-router-dom/cjs/react-router-dom.min";
import { useHistory } from "react-router-dom";
const ApplicationListNew = () => {
  const [dataCv, setDataCv] = useState([]);
  const [count, setCount] = useState(0);
  const [page, setPage] = useState(0);
  const history = useHistory();
  useEffect(() => {
    fetchPendingCVs(page);
  }, [page]);

  const fetchPendingCVs = async (page) => {
    try {
      let res = await getPendingCVsService({
        limit: PAGINATION.pagerow,
        offset: page * PAGINATION.pagerow,
      });

      if (res && res.data.code === 200) {
        console.log(res);
        setDataCv(res.data.result.content);
        setPage(0);
        setCount(Math.ceil(res.data.result.totalElements / PAGINATION.pagerow));
      }
    } catch (error) {
      console.log(error);
    }
  };
  const getPendingCVsService = (data) => {
    const userData = JSON.parse(localStorage.getItem("userData"));
    return axios.get("http://localhost:8080/app-tuyen-dung/api/v1/cv/status", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token_user")}`,
      },
      params: {
        idCompany: userData.idCompany,
        status: "pending",
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

  const handlePageClick = (event) => {
    setPage(event.selected);
  };

  const notifyReviewSuccess = () => toast.success("Đã duyệt hồ sơ thành công!");

  const notifyReviewError = () => toast.error("Duyệt hồ sơ thất bại!");

  const handleReview = async (cvId) => {
    try {
      let res = await reviewCVService(cvId.id);
      if (res) {
        handleSendNotification(cvId.userId, "reviewed");
        fetchPendingCVs(page);
        notifyReviewSuccess();
      } else {
        console.log(res.errMessage);
        notifyReviewError();
      }
    } catch (error) {
      console.log(error);
      notifyReviewError();
    }
  };

  // Thêm toast khi accept thành công
  const notifyAcceptSuccess = () => toast.success("Duyệt hồ sơ thành công!");

  // Thêm toast khi accept thất bại
  const notifyAcceptError = () => toast.error("Duyệt hồ sơ thất bại!");

  const handleAccept = async (cvId) => {
    try {
      let res = await acceptCVService(cvId.id);
      if (res) {
        handleSendNotification(cvId.userId, "accepted");
        fetchPendingCVs(page); // Refresh the list
        notifyAcceptSuccess(); // Thông báo toast accept thành công
      } else {
        console.log(res.errMessage);
        notifyAcceptError(); // Thông báo toast accept thất bại
      }
    } catch (error) {
      console.log(error);
      notifyAcceptError(); // Thông báo toast accept thất bại
    }
  };

  // Thêm toast khi reject thành công
  const notifyRejectSuccess = () => toast.success("Từ chối hồ sơ thành công!");

  // Thêm toast khi reject thất bại
  const notifyRejectError = () => toast.error("Từ chối hồ sơ thất bại!");

  const handleReject = async (cvId) => {
    try {
      let res = await rejectCVService(cvId.id);
      if (res) {
        handleSendNotification(cvId.userId, "rejected");
        fetchPendingCVs(page); // Refresh the list
        notifyRejectSuccess(); // Thông báo toast reject thành công
      } else {
        console.log(res.errMessage);
        notifyRejectError(); // Thông báo toast reject thất bại
      }
    } catch (error) {
      console.log(error);
      notifyRejectError(); // Thông báo toast reject thất bại
    }
  };

  return (
    <div>
      <h1>Danh sách hồ sơ mới</h1>
      <table>
        <thead>
          <tr>
            <th>ID Người dùng</th>
            <th>Họ và tên</th>
            <th>Email</th>
            <th>ID Bài đăng</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {dataCv.filter((cv) => cv !== null).length > 0 ? (
            dataCv
              .filter((cv) => cv !== null)
              .map((cv, index) => (
                <tr key={index}>
                  <td>{cv.userId || ""}</td>
                  <td>{`${cv.firstName || ""} ${cv.lastName || ""}`}</td>
                  <td>{cv.email || ""}</td>
                  <td>{cv.postId || ""}</td>
                  <td className="middle-buttonstatus">
                    <label className="badge badge-warning">
                      {cv.status || ""}
                    </label>
                  </td>
                  <td>
                    <button
                      className="btn-view"
                      style={{ color: "#4B49AC" }}
                      onClick={() =>
                        history.push(`/admin/list-cv/${cv.postId}/`)
                      }
                    >
                      Xem CV nộp
                    </button>
                    &nbsp; &nbsp;
                    <button
                      className="btn-review"
                      onClick={() => handleReview(cv)}
                    >
                      Duyệt
                    </button>
                    <button
                      className="btn-accept"
                      onClick={() => handleAccept(cv)}
                    >
                      Chấp nhận
                    </button>
                    <button
                      className="btn-reject"
                      onClick={() => handleReject(cv)}
                    >
                      Từ chối
                    </button>
                  </td>
                </tr>
              ))
          ) : (
            <tr>
              <td colSpan="7" style={{ textAlign: "center" }}>
                <p>
                  Không có ứng viên nào được chấp nhận. Tìm kiếm ứng viên{" "}
                  <a href="https://project-file-a-job-git-master-la-hoang-phucs-projects.vercel.app/admin/list-candiate/">
                    Tại đây!
                  </a>
                </p>
              </td>
            </tr>
          )}
        </tbody>
      </table>
      <ReactPaginate
        forcePage={page}
        previousLabel={"Quay lại"}
        nextLabel={"Tiếp"}
        breakLabel={"..."}
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
    </div>
  );
};

export default ApplicationListNew;
