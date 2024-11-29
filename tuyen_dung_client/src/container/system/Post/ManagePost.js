import React from "react";
import { useEffect, useState } from "react";
import {
  getDetailPostById,
  banPostService,
  getAllPostByAdminService,
  activePostService,
  getAllPostByRoleAdminService,
  acceptPostService,
} from "../../../service/userService";
import moment from "moment";
import { PAGINATION } from "../../utils/constant";
import ReactPaginate from "react-paginate";
import { Link, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import NoteModal from "../../../components/modal/NoteModal";
import { Col, Modal, Row, Select } from "antd";
import { ExclamationCircleOutlined } from "@ant-design/icons";
import CommonUtils from "../../utils/CommonUtils";
import { Input } from "antd";
import { getUsersByCategory } from "../../../apiCall/user";
import { sendUserNotification } from "../../../apiCall/message";
import LoadingPage from '../../../components/Loading';
const { confirm } = Modal;
const ManagePost = () => {
  const [isLoading, setIsLoading] = useState(false);
  const { id } = useParams();
  const [isSearchBy, setIsSearchBy] = useState(false);
  const [dataPost, setdataPost] = useState([]);
  const [count, setCount] = useState("");
  const [numberPage, setnumberPage] = useState("");
  const [user, setUser] = useState({});
  const [search, setSearch] = useState("");
  const [censorCode, setCensorCode] = useState("PS3");
  const [phones, setPhones] = useState([]);
  const [propsModal, setPropsModal] = useState({
    isActive: false,
    handlePost: () => {},
    postId: "",
  });
  const [total, setTotal] = useState(0);
  const censorOptions = [
    {
      value: "",
      label: "Tất cả",
    },
    {
      value: "PS1",
      label: "Đã kiểm duyệt",
    },
    {
      value: "PS2",
      label: "Đã bị từ chối",
    },
    {
      value: "PS3",
      label: "Chờ kiểm duyệt",
    },
    {
      value: "PS4",
      label: "Bài viết đã bị chặn",
    },
  ];
  const handleCategoryOnChange = async (event) => {
    const categoryCode = event.target.value;
    try {
      const fetchedUsers = await getUsersByCategory(categoryCode);
      console.log("Fetched Users:", fetchedUsers);
      setPhones(fetchedUsers);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };
  useEffect(() => {
    try {
      const userData = JSON.parse(localStorage.getItem("userData"));
      const token = localStorage.getItem("token_user");
      if (id && !isSearchBy) {
        setIsSearchBy(true);
        let fetchDataById = async () => {
          setSearch(id);
          setCensorCode("");
          let arrDataById = await getAllPostByAdminService(
            {
              limit: PAGINATION.pagerow,
              offset: 0,
              search: id,
              censorCode: "",
              idCompany: userData.idCompany,
            },
            {
              headers: {
                Authorization: `Bearer ${token}`, 
              },
            }
          );
          if (arrDataById && arrDataById.errCode === 0) {
            setdataPost(arrDataById.content);
            setnumberPage(0);
            setCount(Math.ceil(arrDataById.totalElements / PAGINATION.pagerow));
            setTotal(arrDataById.totalElements);
          }
        };
        fetchDataById();
      } else {
        if (userData) {
          let fetchData = async () => {
            let arrData = [];
            if (userData.codeRoleAccount == "ADMIN") {
              arrData = await getAllPostByRoleAdminService(
                {
                  limit: PAGINATION.pagerow,
                  offset: 0,
                  search: CommonUtils.removeSpace(search),
                  censorCode: censorCode,
                },
                {
                  headers: {
                    Authorization: `Bearer ${token}`, 
                  },
                }
              );
            } else {
              arrData = await getAllPostByAdminService(
                {
                  limit: PAGINATION.pagerow,
                  offset: 0,
                  idCompany: userData.idCompany,
                  search: CommonUtils.removeSpace(search),
                  censorCode: censorCode,
                },
                {
                  headers: {
                    Authorization: `Bearer ${token}`, 
                  },
                }
              );
            }
            if (arrData) {
              console.log(arrData);
              console.log(arrData.content);
              setdataPost(arrData.content);
              setnumberPage(0);
              setCount(Math.ceil(arrData.totalElements / PAGINATION.pagerow));
              setTotal(arrData.totalElements);
            }
          };
          fetchData();
          setUser(userData);
        }
      }
    } catch (error) {
      console.log(error);
    }
  }, [search, censorCode]);

  let handleChangePage = async (number) => {
    setnumberPage(number.selected);
    let arrData = [];
    const token = localStorage.getItem("token_user");
    if (user.codeRoleAccount == "ADMIN") {
      arrData = await getAllPostByRoleAdminService(
        {
          limit: PAGINATION.pagerow,
          offset: number.selected,
          search: CommonUtils.removeSpace(search),
          censorCode: censorCode,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`, 
          },
        }
      );
    } else {
      arrData = await getAllPostByAdminService(
        {
          limit: PAGINATION.pagerow,
          offset: number.selected,
          idCompany: user.idCompany,
          search: CommonUtils.removeSpace(search),
          censorCode: censorCode,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`, 
          },
        }
      );
    }
    if (arrData) {
      setdataPost(arrData.content);
      setTotal(arrData.totalElements);
    }
  };
  let handleOnChangeCensor = (value) => {
    setCensorCode(value);
  };
  let handleBanPost = async (id, note) => {
    const token = localStorage.getItem("token_user");
    let res = await banPostService(
      {
        postId: id,
        userId: user.id,
        note: note,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`, 
        },
      }
    );
    if (res) {
      let arrData = [];
      if (user.codeRoleAccount == "ADMIN") {
        arrData = await getAllPostByRoleAdminService(
          {
            limit: PAGINATION.pagerow,
            offset: numberPage,
            search: CommonUtils.removeSpace(search),
            censorCode: censorCode,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`, 
            },
          }
        );
      } else {
        arrData = await getAllPostByAdminService(
          {
            limit: PAGINATION.pagerow,
            offset: numberPage,
            idCompany: user.idCompany,
            search: CommonUtils.removeSpace(search),
            censorCode: censorCode,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`, 
            },
          }
        );
      }
      if (arrData) {
        setdataPost(arrData.content);
        setTotal(arrData.totalElements);
      }
      toast.success(res.errMessage);
    } else {
      toast.error(res.errMessage);
    }
  };
  let handleActivePost = async (id, note) => {
    setIsLoading(true);
    const token = localStorage.getItem("token_user");
    let res = await activePostService(
      {
        id: id,
        userId: user.id,
        note: note,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`, 
        },
      }
    );
    if (res) {
      let arrData = [];
      if (user.codeRoleAccount == "ADMIN") {
        arrData = await getAllPostByRoleAdminService(
          {
            limit: PAGINATION.pagerow,
            offset: numberPage,
            search: CommonUtils.removeSpace(search),
            censorCode: censorCode,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`, 
            },
          }
        );
      } else {
        arrData = await getAllPostByAdminService(
          {
            limit: PAGINATION.pagerow,
            offset: numberPage,
            idCompany: user.idCompany,
            search: CommonUtils.removeSpace(search),
            censorCode: censorCode,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`, 
            },
          }
        );
      }
      if (arrData) {
        setdataPost(arrData.content);
        setTotal(arrData.totalElements);
      }
      setIsLoading(false);
      toast.success(res.errMessage);
    } else {
      toast.error(res.errMessage);
    }
  };
  let handleAccecptPost = async (id, note = null, statusCode = "PS2") => {
    setIsLoading(true);
    const token = localStorage.getItem("token_user");
    let postDetail = await getDetailPostById(id, {
      headers: {
        Authorization: `Bearer ${token}`, 
      },
    });
    if (!postDetail || !postDetail.data) {
      console.error("Không thể lấy chi tiết bài đăng");
      return;
    }
    const categoryJobCode = postDetail?.data?.categoryJobCode;
    if (!categoryJobCode) {
      console.log("categoryJobCode not found in postDetail");
      return;
    }
    const users = await getUsersByCategory(categoryJobCode);
    if (users && users.length > 0) {
      console.log("Users in category:", users);
    } else {
      console.log("No users found for this category");
    }
    const payload = {
      subject: `${postDetail?.data?.nameCompanyValue} vừa đăng bài tuyển dụng với vị trí ${postDetail?.data?.name}.`,
      image: postDetail?.data?.thumbnailCompanyValue,
      message: `${postDetail?.data?.nameCompanyValue} vừa đăng bài tuyển dụng với vị trí ${postDetail?.data?.name}.`,
      attachedUrl: `/detail-job/${postDetail?.data?.id}`,
      sender: "ADMIN",
      userId: users?.result,
    };
    let res = await acceptPostService(
      {
        id: id,
        statusCode: statusCode,
        note: note,
        userId: user.id,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`, 
        },
      }
    );
    if (res) {
      const response = await sendUserNotification(payload);
      console.log("API response:", response);
      let arrData = [];
      if (user.codeRoleAccount == "ADMIN") {
        arrData = await getAllPostByRoleAdminService(
          {
            limit: PAGINATION.pagerow,
            offset: numberPage,
            search: CommonUtils.removeSpace(search),
            censorCode: censorCode,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
      } else {
        arrData = await getAllPostByAdminService(
          {
            limit: PAGINATION.pagerow,
            offset: numberPage,
            idCompany: user.idCompany,
            search: CommonUtils.removeSpace(search),
            censorCode: censorCode,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`, 
            },
          }
        );
      }
      if (arrData) {
        setdataPost(arrData.content);
        setTotal(arrData.totalElements);
      }
      setIsLoading(false);
      toast.success(res.errMessage);
    } else {
      toast.error(res.errMessage);
    }
  };
  const confirmPost = (id) => {
    console.log(id);
    confirm({
      title: "Bạn có chắc muốn duyệt bài viết này?",
      icon: <ExclamationCircleOutlined />,
      onOk() {
        handleAccecptPost(id, "", "PS1");
      },

      onCancel() {},
    });
  };
  const handleSearch = (value) => {
    setSearch(value);
  };
  return (
    <div>
      <div className="col-12 grid-margin">
        <div className="card">
          <div className="card-body">
            <h4 className="card-title">Danh sách bài đăng</h4>
            <Row justify="space-around" className="mt-5 mb-5">
              <Col xs={12} xxl={12}>
                <Input.Search
                  onSearch={handleSearch}
                  placeholder={
                    user?.codeRoleAccount === "ADMIN"
                      ? "Nhập tên hoặc mã bài đăng, tên công ty"
                      : "Nhập tên hoặc mã bài đăng"
                  }
                  allowClear
                  enterButton="Tìm kiếm"
                ></Input.Search>
              </Col>
              <Col xs={8} xxl={8}>
                <label className="mr-2">Loại trạng thái: </label>
                <Select
                  onChange={(value) => handleOnChangeCensor(value)}
                  style={{ width: "50%" }}
                  size="default"
                  defaultValue={
                    id ? censorOptions[0].value : censorOptions[3].value
                  }
                  options={censorOptions}
                ></Select>
              </Col>
            </Row>
            <div>Số lượng bài viết: {total}</div>
            <div className="table-responsive pt-2">
              <table className="table table-bordered">
                <thead>
                  <tr>
                    <th>STT</th>
                    <th>Mã bài đăng</th>
                    <th>Tên bài đăng</th>
                    {user?.codeRoleAccount === "ADMIN" && <th>Tên công ty</th>}
                    <th>Tên người đăng</th>
                    <th>Ngày kết thúc</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                  </tr>
                </thead>
                <tbody>
                  {dataPost &&
                    dataPost.length > 0 &&
                    dataPost.map((item, index) => {
                      let date = moment
                        .unix(item.timeEnd / 1000)
                        .format("DD/MM/YYYY");
                      return (
                        <tr key={index}>
                          <td>{index + 1 + numberPage * PAGINATION.pagerow}</td>
                          <td>{item.id}</td>
                          <td>{item.postName}</td>
                          {user?.codeRoleAccount === "ADMIN" && (
                            <td>{item.companyName}</td>
                          )}
                          <td>{`${item.fullName}`}</td>
                          <td>{date}</td>
                          <td>
                            <label
                              className={
                                item.statusCode == "PS1"
                                  ? "badge badge-success"
                                  : item.statusCode == "PS3"
                                  ? "badge badge-warning"
                                  : "badge badge-danger"
                              }
                            >
                              {item.statusValue}
                            </label>
                          </td>

                          <td>
                            <Link
                              style={{ color: "#4B49AC" }}
                              to={`/admin/note/${item.id}`}
                            >
                              Chú thích
                            </Link>
                            &nbsp; &nbsp;
                            {(user.codeRoleAccount == "COMPANY" ||
                              user.codeRoleAccount == "EMPLOYER") && (
                              <>
                                <Link
                                  style={{ color: "#4B49AC" }}
                                  to={`/admin/list-cv/${item.id}/`}
                                >
                                  Xem CV nộp
                                </Link>
                                &nbsp; &nbsp;
                              </>
                            )}
                            {item.statusCode !== "PS4" && (
                              <Link
                                style={{ color: "#4B49AC" }}
                                to={`/admin/edit-post/${item.id}/`}
                              >
                                {user?.codeRoleAccount === "ADMIN" ||user?.codeRoleAccount ==="EMPLOYER"
                                  ? "Xem chi tiết"
                                  : "Sửa"}
                              </Link>
                            )}
                            &nbsp; &nbsp;
                            {user.codeRoleAccount == "ADMIN" ? (
                              item.statusCode == "PS1" ? (
                                <>
                                  <a
                                    style={{
                                      color: "#4B49AC",
                                      cursor: "pointer",
                                    }}
                                    onClick={() =>
                                      setPropsModal({
                                        isActive: true,
                                        handlePost: handleBanPost,
                                        postId: item.id,
                                      })
                                    }
                                  >
                                    Chặn
                                  </a>
                                  &nbsp; &nbsp;
                                </>
                              ) : item.statusCode == "PS4" ? (
                                <>
                                  <a
                                    style={{
                                      color: "#4B49AC",
                                      cursor: "pointer",
                                    }}
                                    onClick={() =>
                                      setPropsModal({
                                        isActive: true,
                                        handlePost: handleActivePost,
                                        postId: item.id,
                                      })
                                    }
                                  >
                                    Mở lại
                                  </a>
                                </>
                              ) : (
                                <>
                                  <a
                                    style={{
                                      color: "#4B49AC",
                                      cursor: "pointer",
                                    }}
                                    onClick={() => confirmPost(item.id)}
                                  >
                                    Duyệt
                                  </a>
                                  {item.statusCode !== "PS2" && (
                                    <a
                                      style={{
                                        color: "#4B49AC",
                                        cursor: "pointer",
                                        marginLeft: "10px",
                                      }}
                                      onClick={() =>
                                        setPropsModal({
                                          isActive: true,
                                          handlePost: handleAccecptPost,
                                          postId: item.id,
                                        })
                                      }
                                    >
                                      Từ chối
                                    </a>
                                  )}
                                </>
                              )
                            ) : (
                              <></>
                            )}
                          </td>
                        </tr>
                      );
                    })}
                </tbody>
              </table>
              {dataPost && dataPost.length == 0 && (
                <div style={{ textAlign: "center" }}>Không có dữ liệu</div>
              )}
            </div>
          </div>
          
          <ReactPaginate
            forcePage={numberPage}
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
      <NoteModal
        isOpen={propsModal.isActive}
        onHide={() =>
          setPropsModal({
            isActive: false,
            handlePost: () => {},
            id: "",
          })
        }
        id={propsModal.postId}
        handleFunc={propsModal.handlePost}
      />
      {isLoading && <LoadingPage />}
    </div>
  );
};

export default ManagePost;
