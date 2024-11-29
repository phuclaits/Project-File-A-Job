import React, { useEffect, useState } from "react";
// import { getRejectedCVsService } from '../../../service/cvService';
import ReactPaginate from "react-paginate";
import { PAGINATION } from "../../utils/constant";
import styled from "styled-components";
import axios from "axios";

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

const ApplicationListRejected = () => {
  const [dataCv, setDataCv] = useState([]);
  const [count, setCount] = useState(0);
  const [page, setPage] = useState(0);

  useEffect(() => {
    fetchRejectedCVs(page);
  }, [page]);

  const fetchRejectedCVs = async (page) => {
    try {
        let res = await getRejectedCVsService({
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
  };

  const getRejectedCVsService = (data) => {
    const userData = JSON.parse(localStorage.getItem("userData"));
    return axios.get("http://localhost:8080/app-tuyen-dung/api/v1/cv/status", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token_user")}`,
      },
      params: {
        idCompany: userData.idCompany,
        status: "rejected",
        limit: data.limit,
        offset: data.offset,
      },
    });
  };

  const handlePageClick = (event) => {
    setPage(event.selected);
  };

  return (
    <Container>
      <h1>Đã từ chối</h1>
      <Table>
        <thead>
          <tr>
            <Th>ID Người dùng</Th>
            <Th>Họ và tên đầy đủ</Th>
            <Th>Email</Th>
            <Th>ID Bài đăng</Th>
            <Th>Trạng thái</Th>
          </tr>
        </thead>
        <tbody>
          { dataCv.filter((cv) => cv !== null).length > 0 ? (
            dataCv
            .filter((cv) => cv !== null)
            .map((cv, index) => (
              <tr key={index}>
                <Td>{cv.userId || ""}</Td>
                <Td>{`${cv.firstName || ""} ${cv.lastName || ""}`}</Td>
                <Td>{cv.email || ""}</Td>
                <Td>{cv.postId || ""}</Td>
                <Td>{cv.status}</Td>
              </tr>
            ))) : (
    <tr>
      <td colSpan="7" style={{ textAlign: "center" }}>
        <p>Không có ứng viên nào được chấp nhận. Hãy kiểm tra Danh sách ứng tuyển mới <a href='http://localhost:3000/admin/list-candiate/'>Tại đây!</a></p>
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

export default ApplicationListRejected;
