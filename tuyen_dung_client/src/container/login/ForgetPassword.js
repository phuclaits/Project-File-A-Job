import React from "react";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import handleValidate from "../utils/Validation";
import {
  checkUserPhoneService,
  changePasswordByphoneForgotPass,
  handleLoginService,
} from "../../service/userService";
import { Link } from "react-router-dom";
import OtpForgetPassword from "./OtpForgetPassword";
import axios from "axios";
const ForgetPassword = () => {
  const [inputValidates, setValidates] = useState({
    phonenumber: true,
    newPassword: true,
    confirmPassword: true,
  });
  const [inputValues, setInputValues] = useState({
    phonenumber: "",
    isOpen: false,
    isSuccess: false,
    newPassword: "",
    confirmPassword: "",
  });

  const handleOnChange = (event) => {
    const { name, value } = event.target;
    setInputValues({ ...inputValues, [name]: value });
  };

  let handleOpenVerifyOTP = async () => {
    let checkPhone = handleValidate(inputValues.phonenumber, "phone");
    if (!(checkPhone === true)) {
      setValidates({
        ...inputValidates,
        phonenumber: checkPhone,
      });
      return;
    }

    let res = await checkUserPhoneService(inputValues.phonenumber);

    if (res === true) {
      setInputValues({ ...inputValues, ["isOpen"]: true });
    } else {
      setValidates({
        ...inputValidates,
        phonenumber: true,
      });
      toast.error("Số điện thoại không tồn tại!");
    }
  };
  const recieveVerify = (success) => {
    setInputValues({ ...inputValues, ["isOpen"]: false, ["isSuccess"]: true });
  };
  let handleLogin = async (phonenumber, password) => {
    let paramsLogin = {
      phonenumber: phonenumber,
      password: password,
    };
    axios
      .post(
        "http://localhost:8080/app-tuyen-dung/api/v1/auth/login",
        paramsLogin
      )
      .then((res) => {
        if (res.data.statusCode === 200) {
          console.log(res.data);
          localStorage.setItem("token_user", res.data.token);
          const token = localStorage.getItem("token_user");
          console.log(`token:  ${token}`);
          axios
            .post(
              "http://localhost:8080/app-tuyen-dung/api/v1/user/get-info",
              {},
              {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              }
            )
            .then((response) => {
              if (response.data && response.data.length > 0) {
                const userData = response.data[0]; // Lấy object đầu tiên trong mảng
                localStorage.setItem("userData", JSON.stringify(userData));
                // console.log("User data saved to localStorage:", userData.codeRoleAccount);
                // Chuyển hướng sau khi lưu
                if (
                  res.data.roleCode === "ADMIN" ||
                  res.data.roleCode === "EMPLOYER" ||
                  res.data.roleCode === "COMPANY"
                ) {
                  toast.success("Đăng nhập thành công ");
                  window.location.href = "/admin/";
                } else {
                  const lastUrl = localStorage.getItem("lastUrl");
                  if (lastUrl) {
                    localStorage.removeItem("lastUrl");
                    window.location.href = lastUrl;
                  } else {
                    window.location.href = "/";
                  }
                }
              } else {
                console.log("No user data found.");
              }
            });
        } else {
          toast.error("Đăng nhập thất bại");
        }
      });
  };
  let handleForgetPassword = async () => {
    let checkNewPass = handleValidate(inputValues.newPassword, "password");
    if (!(checkNewPass === true)) {
      setValidates({
        ...inputValidates,
        newPassword: checkNewPass,
      });
      if (!inputValues.confirmPassword) {
        toast.error("Vui lòng nhập mật khẩu xác nhận");
        return;
      }
      if (inputValues.confirmPassword !== inputValues.newPassword) {
        setValidates({
          ...inputValidates,
          confirmPassword: "Mật khẩu nhập lại không trùng",
        });
        return;
      }
      return;
    }
    let res = await changePasswordByphoneForgotPass({
      phonenumber: inputValues.phonenumber,
      newPassword: inputValues.newPassword,
    });
    if (res && res.errCode === 0) {
      toast.success("Đổi mật khẩu thành công");
      handleLogin(inputValues.phonenumber, inputValues.newPassword);
    } else {
      toast.error(res.errMessage);
    }
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
                    <h4>Quên mật khẩu?</h4>
                    <h6 className="font-weight-light">
                      Đừng lo! Khôi phục trong vài giây
                    </h6>
                    <form className="pt-3">
                      {inputValues.isSuccess === true && (
                        <>
                          <div className="form-group">
                            <input
                              type="password"
                              value={inputValues.newPassword}
                              name="newPassword"
                              onChange={(event) => handleOnChange(event)}
                              className="form-control form-control-lg"
                              id="exampleInputPassword1"
                              placeholder="Mật khẩu mới"
                            />
                            {inputValidates.newPassword && (
                              <p style={{ color: "red" }}>
                                {inputValidates.newPassword}
                              </p>
                            )}
                          </div>
                          <div className="form-group">
                            <input
                              type="password"
                              value={inputValues.confirmPassword}
                              name="confirmPassword"
                              onChange={(event) => handleOnChange(event)}
                              className="form-control form-control-lg"
                              id="exampleInputPassword1"
                              placeholder="Xác nhận mật khẩu"
                            />
                            {inputValidates.confirmPassword && (
                              <p style={{ color: "red" }}>
                                {inputValidates.confirmPassword}
                              </p>
                            )}
                          </div>
                          <div className="mt-3">
                            <a
                              onClick={() => handleForgetPassword()}
                              className="btn1 btn1-block btn1-primary1 btn1-lg font-weight-medium auth-form-btn1"
                            >
                              Xác nhận
                            </a>
                          </div>
                        </>
                      )}
                      {inputValues.isSuccess === false && (
                        <>
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
                          <div className="mt-3">
                            <a
                              onClick={() => handleOpenVerifyOTP()}
                              className="btn1 btn1-block btn1-primary1 btn1-lg font-weight-medium auth-form-btn1"
                            >
                              Xác nhận
                            </a>
                          </div>
                        </>
                      )}
                      <div className="text-center mt-4 font-weight-light">
                        Chưa có tài khoản?{" "}
                        <Link to="/register" className="text-primary">
                          Đăng ký
                        </Link>
                        <br></br>
                        <br></br>
                        Đã có tài khoản?{" "}
                        <Link to="/login" className="text-primary">
                          Đăng nhập
                        </Link>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
      {inputValues.isOpen === true && (
        <OtpForgetPassword
          dataUser={inputValues.phonenumber}
          recieveVerify={recieveVerify}
        />
      )}
    </>
  );
};

export default ForgetPassword;
