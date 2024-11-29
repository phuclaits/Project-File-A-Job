import React from "react";
import { useEffect, useState } from "react";
import { getAllListCvByPostService,getDetailCvServicebyAdmin } from "../../../service/cvService";
import { getDetailPostByIdService } from "../../../service/userService";

import { PAGINATION } from "../../utils/constant";
import ReactPaginate from "react-paginate";
import { Link, useHistory } from "react-router-dom";
import { useParams } from "react-router-dom";

const ManageCv = () => {
  const [dataCv, setdataCv] = useState([]);
  const [count, setCount] = useState("");
  const [numberPage, setnumberPage] = useState("");
  const { id } = useParams();
  const [post, setPost] = useState("");
  let fetchPost = async (id) => {
    let res = await getDetailCvServicebyAdmin(id);
    
    if (res && res.data.code === 200) {
      setPost(res.data.result);
    }
  };
  useEffect(() => {
    if (id) {
      const token = localStorage.getItem("token_user");
      const fetchData = async () => {
        try {
          let arrData = await getDetailPostByIdService(
            {
              limit: PAGINATION.pagerow,
              offset: 0,
              postId: id,
            },
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
  
          if (arrData) {
            setdataCv(arrData.result.content);
            setCount(Math.ceil(arrData.result.totalElements / PAGINATION.pagerow));
            return arrData.result.content; // Trả về dataCv từ đây
          }
        } catch (error) {
          console.log(error);
        }
      };
  
      const handleData = async () => {
        const fetchedData = await fetchData(); // Đợi fetchData hoàn thành
        if (fetchedData && fetchedData.length > 0) {
          const firstCv = fetchedData[0]; // Lấy item đầu tiên hoặc item bạn cần
          await fetchPost(firstCv.idCv); // Gọi fetchPost với idCv từ fetchedData
        }
      };
  
      handleData(); // Gọi chuỗi xử lý
    }
  }, [id]);

  let handleChangePage = async (number) => {
    const token = localStorage.getItem("token_user");
    setnumberPage(number.selected);
    let arrData = await getAllListCvByPostService({
      limit: PAGINATION.pagerow,
      offset: number.selected * PAGINATION.pagerow,
      postId: id,
    });

    if (arrData) {
      const combinedData = arrData.data.map((cv, index) => ({
        ...cv,
        matchPercentage:
          arrData.matchPercentage[index]?.matchPercentage || "0%",
        phoneNumber: arrData.matchPercentage[index]?.phoneNumber || "N/A",
      }));

      setdataCv(combinedData);
    }
  };
  const history = useHistory();
  return (
    <div>
      <div className="col-12 grid-margin">
        <div className="card">
          <div className="card-body">
            <h4 className="card-title">Danh sách CV</h4>
            <div
              onClick={() => history.goBack()}
              className="mb-2 hover-pointer"
              style={{ color: "red" }}
            >
              <i class="fa-solid fa-arrow-left mr-2"></i>Quay lại
            </div>
            <div className="text-center">
              <h3>{post && post.name}</h3>
            </div>
            <div className="table-responsive pt-2">
              <table className="table table-bordered">
                <thead>
                  <tr>
                    <th>STT</th>
                    <th>Tên người nộp</th>
                    <th>Số điện thoại</th>
                    {/* <th>Tỉ lệ phù hợp</th> */}
                    {/* <th>Đánh giá</th> */}
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                  </tr>
                </thead>
                <tbody>
                  {dataCv.map((item, index) => (
                      <tr key={index}>
                        <td>{index + 1 + numberPage * PAGINATION.pagerow}</td>
                        <td>
                          {item.firstName + " " + item.lastName}
                        </td>
                        <td>{item.phoneNumber}</td>
                        <td>{item.status}</td>
                        <td>
                          <Link
                            style={{ color: "#4B49AC", cursor: "pointer" }}
                            to={`/admin/admin-cv/${item.idCv}/`}
                          >
                            Xem CV
                          </Link>
                        </td>
                      </tr>
                    ))
                  }
                </tbody>
              </table>
              {dataCv && dataCv.length == 0 && (
                <div style={{ textAlign: "center" }}>Không có dữ liệu</div>
              )}
            </div>
          </div>
          <ReactPaginate
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
            onPageChange={handleChangePage}
          />
        </div>
      </div>
    </div>
  );
};

export default ManageCv;
