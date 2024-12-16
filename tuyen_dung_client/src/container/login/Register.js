import React from "react";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import Otp from "./Otp";
import { Link } from "react-router-dom";
import handleValidate from "../utils/Validation";
import { useHistory } from "react-router";
// import { useFetchDataCodeRole,useFetchDataCodeGender } from "../utils/fetch";
import axios from "axios";
const Register = () => {
  let history = useHistory();
  const [inputValidates, setValidates] = useState({
    phonenumber: true,
    password: true,
    firstName: true,
    lastName: true,
    email: true,
    againPass: true,
  });
  const [inputValues, setInputValues] = useState({
    phonenumber: "",
    firstName: "",
    lastName: "",
    password: "",
    isOpen: false,
    dataUser: {},
    roleCode: "CANDIDATE",
    email: "",
    againPass: "",
    genderCode: "FE",
    images: "",
  });
  console.log(inputValues);
  const [dataCodeRole, setDataCodeRole] = useState([]);
  const [dataCodeGender, setDataCodeGender] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem("token_user");
    if (token) {
      const userData = JSON.parse(localStorage.getItem("userData"));
      if (
        userData &&
        (userData.codeRoleAccount === "ADMIN" ||
          userData.codeRoleAccount === "EMPLOYER" ||
          userData.codeRoleAccount === "COMPANY")
      ) {
        history.push("/admin");
      } else {
        history.push("/");
      }
    }

    const fetchData = async () => {
      try {
        // Gọi API để lấy dữ liệu
        const roleResponse = await axios.get(
          "http://localhost:8080/app-tuyen-dung/api/v1/get-all-code/rules-user"
        );
        const genderResponse = await axios.get(
          "http://localhost:8080/app-tuyen-dung/api/v1/get-all-code/genders-user"
        );
        setDataCodeGender(genderResponse.data);
        // Cập nhật trạng thái với dữ liệu lấy được
        setDataCodeRole(roleResponse.data);
      } catch (error) {
        console.error("Error fetching data", error);
      }
    };
    fetchData();
  }, []);
  const filteredDataCodeRole = dataCodeRole.filter(
    (item) => item.code !== "ADMIN" && item.code !== "COMPANY"
  );

  if (
    dataCodeGender &&
    dataCodeGender.length > 0 &&
    inputValues.genderCode === "" &&
    dataCodeRole &&
    dataCodeRole.length > 0 &&
    inputValues.roleCode === ""
  ) {
    setInputValues({
      ...inputValues,
      ["genderCode"]: dataCodeGender[0].code,
      ["roleCode"]: dataCodeRole[0].code,
    });
  }

  const handleOnChange = (event) => {
    const { name, value } = event.target;
    setInputValues({ ...inputValues, ...{ [name]: value } });
  };

  let handleOpenVerifyOTP = async () => {
    if(inputValues.password ==='' || inputValues.phonenumber ==='' || inputValues.firstName ==='' || inputValues.lastName ==='' || inputValues.email ===''){
      toast.error("Vui lòng nhập đầy đủ thông tin !")
      return;
    }
    let checkPhonenumber = handleValidate(inputValues.phonenumber, "phone");
    let checkPassword = handleValidate(inputValues.password, "password");
    let checkFirstName = handleValidate(inputValues.firstName, "isEmpty");
    let checkLastName = handleValidate(inputValues.lastName, "isEmpty");
    let checkEmail = handleValidate(inputValues.email, "email");
    if (
      !(
        checkPhonenumber === true &&
        checkPassword === true &&
        checkFirstName === true &&
        checkLastName === true &&
        checkEmail === true
      )
    ) {
      setValidates({
        phonenumber: checkPhonenumber,
        password: checkPassword,
        firstName: checkFirstName,
        lastName: checkLastName,
        email: checkEmail,
      });
      return;
    }
    
    if (!inputValues.againPass) {
      toast.error("Mật khẩu nhập lại không được bỏ trống!");
      return;
    }
    if(inputValues.password.length <= 6){
      toast.error("Mật khẩu phải lớn hơn 5 ký tự !");
      return;
    }
    if (inputValues.againPass !== inputValues.password) {
      toast.error("Mật khẩu nhập lại không trùng khớp!");
      return;
    }

    axios
      .get(
        "http://localhost:8080/app-tuyen-dung/api/v1/user/check-phonenumber-user",
        {
          params: { phonenumber: inputValues.phonenumber },
        }
      )
      .then((res) => {

        if (res.data === true) {
          toast.error("Số điện thoại đã tồn tại !");
        } else {
          setInputValues({
            ...inputValues,
            ["dataUser"]: {
              phonenumber: inputValues.phonenumber,
              firstName: inputValues.firstName,
              lastName: inputValues.lastName,
              password: inputValues.password,
              roleCode: inputValues.roleCode,
              email: inputValues.email,
              genderCode: inputValues.genderCode,
              image:
                "https://inkythuatso.com/uploads/thumbnails/800/2023/03/6-anh-dai-dien-trang-inkythuatso-03-15-26-36.jpg",
            },
            ["isOpen"]: true,
          });
        }
      })
      .catch((error) => {
        console.error("Error fetching feature data", error);
      });
  };
  return (
    <>
      {inputValues.isOpen === false && (
        <div className="container-scroller">
          <div className="container-fluid page-body-wrapper full-page-wrapper">
            <div className="content-wrapper d-flex align-items-center auth px-0">
              <div className="row w-100 mx-0">
                <div className="col-lg-4 mx-auto">
                  <div className="auth-form-light text-left py-5 px-4 px-sm-5">
                    <div className="brand-logo">
                      <img src="/assets/img/logo.png" alt="logo" />
                    </div>
                    <h4>Người mới?</h4>
                    <h6 className="font-weight-light">
                      Đăng ký dễ dàng chỉ vài bước đơn giản
                    </h6>
                    <form className="pt-3">
                      <div className="form-group">
                        <input
                          type="text"
                          value={inputValues.firstName}
                          name="firstName"
                          onChange={(event) => handleOnChange(event)}
                          className="form-control form-control-lg"
                          id="exampleInputUsername1"
                          placeholder="Họ"
                        />
                        {inputValidates.firstName && (
                          <p style={{ color: "red" }}>
                            {inputValidates.firstName}
                          </p>
                        )}
                      </div>
                      <div className="form-group">
                        <input
                          type="text"
                          value={inputValues.lastName}
                          name="lastName"
                          onChange={(event) => handleOnChange(event)}
                          className="form-control form-control-lg"
                          id="exampleInputUsername1"
                          placeholder="Tên"
                        />
                        {inputValidates.lastName && (
                          <p style={{ color: "red" }}>
                            {inputValidates.lastName}
                          </p>
                        )}
                      </div>
                      <div className="form-group">
                        <input
                          type="number"
                          value={inputValues.phonenumber}
                          name="phonenumber"
                          onChange={(event) => handleOnChange(event)}
                          className="form-control form-control-lg"
                          id="exampleInputEmail1"
                          placeholder="Số điện thoại"
                        />
                        {inputValidates.phonenumber && (
                          <p style={{ color: "red" }}>
                            {inputValidates.phonenumber}
                          </p>
                        )}
                      </div>
                      <div className="form-group">
                        <input
                          type="text"
                          value={inputValues.email}
                          name="email"
                          onChange={(event) => handleOnChange(event)}
                          className="form-control form-control-lg"
                          placeholder="Email"
                        />
                        {inputValidates.email && (
                          <p style={{ color: "red" }}>{inputValidates.email}</p>
                        )}
                      </div>
                      <div className="form-group">
                        <input
                          type="password"
                          value={inputValues.password}
                          name="password"
                          onChange={(event) => handleOnChange(event)}
                          className="form-control form-control-lg"
                          id="exampleInputPassword1"
                          placeholder="Mật khẩu"
                        />
                        {inputValidates.password && (
                          <p style={{ color: "red" }}>
                            {inputValidates.password}
                          </p>
                        )}
                      </div>
                      <div className="form-group">
                        <input
                          type="password"
                          value={inputValues.againPass}
                          name="againPass"
                          onChange={(event) => handleOnChange(event)}
                          className="form-control form-control-lg"
                          placeholder="Nhập lại mật khẩu"
                        />
                        {inputValidates.againPass && (
                          <p style={{ color: "red" }}>
                            {inputValidates.againPass}
                          </p>
                        )}
                      </div>
                      <div className="form-group">
                        <select
                          style={{ color: "black" }}
                          className="form-control"
                          value={inputValues.roleCode}
                          name="roleCode"
                          onChange={(event) => handleOnChange(event)}
                        >
                          {dataCodeRole &&
                            dataCodeRole.length > 0 &&
                            dataCodeRole.map((item, index) => {
                              if (
                                item.code !== "ADMIN" &&
                                item.code !== "COMPANY"
                              ) {
                                return (
                                  <option key={index} value={item.code}>
                                    {item.value}
                                  </option>
                                );
                              }
                            })}
                        </select>
                      </div>
                      <div className="form-group">
                        <select
                          style={{ color: "black" }}
                          className="form-control"
                          value={inputValues.genderCode}
                          name="genderCode"
                          onChange={(event) => handleOnChange(event)}
                        >
                          {dataCodeGender &&
                            dataCodeGender.length > 0 &&
                            dataCodeGender.map((item, index) => {
                              return (
                                <option key={index} value={item.code}>
                                  {item.value}
                                </option>
                              );
                            })}
                        </select>
                      </div>
                      <div className="mt-3">
                        <a
                          onClick={() => handleOpenVerifyOTP()}
                          className="btn1 btn1-block btn1-primary1 btn1-lg font-weight-medium auth-form-btn1"
                        >
                          Đăng ký
                        </a>
                      </div>
                      <div className="text-center mt-4 font-weight-light">
                        Bạn đã có tài khoản rồi?{" "}
                        <Link to="/login" className="text-primary">
                          Đăng nhập ngay
                        </Link>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
            {/* content-wrapper ends */}
          </div>
          {/* page-body-wrapper ends */}
        </div>
      )}

      {inputValues.isOpen === true && <Otp dataUser={inputValues.dataUser} />}
    </>
  );
};

export default Register;
