import React, { useEffect, useState } from "react";
import "./Otp.scss";
import { toast } from "react-toastify";
import firebaseApp from "../utils/firebase";
import {createNewUser, handleLoginService} from "../../service/userService";
import firebase from "firebase/app";
import "firebase/auth";
import { getAuth } from "firebase/auth";
import { RecaptchaVerifier, signInWithPhoneNumber } from "firebase/auth";
import axios from "axios";
const Otp = (props) => {
  const [dataUser, setDataUser] = useState({});
  const [requestID, setRequestID] = useState(""); 
  const [inputValues, setInputValues] = useState({
    so1: "",
    so2: "",
    so3: "",
    so4: ""
  });
  const auth = getAuth(firebaseApp);
  console.log(props);
  useEffect(() => {
    if (props.dataUser) {
      let fetchOtp = async () => {
        await onSignInSubmit(false);
      };
      fetchOtp();
    }
  }, [props.dataUser]);
  const handleOnChange = (event) => {
    const { name, value } = event.target;
    setInputValues({ ...inputValues, [name]: value });
  };
  const configureCaptcha = () => {
    if (window.recaptchaVerifier) {
      window.recaptchaVerifier.clear();
    }
    window.recaptchaVerifier = new RecaptchaVerifier(
      "sign-in-button",
      {
        size: "invisible",
        // theme: "dark", // Chủ đề của ReCAPTCHA
        defaultCountry: "VN",
      },
      auth 
    );
  };
  let onSignInSubmit = async (isResend) => {
    if (!isResend) {
      configureCaptcha();
    }
    let phoneNumber = props.dataUser.phonenumber;
    if (phoneNumber) {
      phoneNumber = "+84" + phoneNumber.slice(1);
    }

    console.log("check phonenumber", phoneNumber);
    const appVerifier = window.recaptchaVerifier;
    
    try {
      const confirmationResult = await signInWithPhoneNumber(auth,phoneNumber, appVerifier);
      window.confirmationResult = confirmationResult;
      toast.success("Đã gửi mã OTP vào điện thoại");
    } catch (error) {
      toast.error("Gửi mã thất bại !");
    }
  };
  let submitOTP = async () => {

    if(inputValues.so1 ===''||inputValues.so2 ===''||inputValues.so3 ===''||inputValues.so4 ===''||inputValues.so5 ===''||inputValues.so6 ===''){
      toast.error("Vui lòng nhập đầy đủ mã OTP !");
      return;
    }
    const code = +(
      inputValues.so1 +
      inputValues.so2 +
      inputValues.so3 +
      inputValues.so4 +
      inputValues.so5 +
      inputValues.so6
    );

    await window.confirmationResult
      .confirm(code)
      .then((result) => {
        // User signed in successfully.
        const user = result.user;
        toast.success("Đã xác minh số điện thoại !");
        let createUser = async () => {
          let res = await createNewUser({
            password: props.dataUser.password,
            firstName: props.dataUser.firstName,
            lastName: props.dataUser.lastName,
            phonenumber: props.dataUser.phonenumber,
            roleCode: props.dataUser.roleCode,
            email: props.dataUser.email,
            genderCode: props.dataUser.genderCode,
            
            // image:
            //   "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSH-bmqm7mCI2OBNsFo6PDo9QD3NPzXnpn9vA&s",
          });
          if (res && res.statusCode === 200) {
            toast.success("Tạo tài khoản thành công");
            handleLogin(props.dataUser.phonenumber, props.dataUser.password);
          } else {
            toast.error(res.errMessage);
          }
        };
        createUser();

        // ...
      })
      .catch((error) => {
        // User couldn't sign in (bad verification code?)
        // ...
        toast.error("Mã OTP không đúng !");
      });
  };
  let resendOTP = async () => {
    await onSignInSubmit(true);
  };
  let handleLogin = async (phonenumber, password) => {
    // let res = await handleLoginService({
    //   phonenumber: phonenumber,
    //   password: password,
    // });
    let paramsLogin = {
      phonenumber: phonenumber,
      password: password,
    };
    axios
      .post("http://localhost:8080/app-tuyen-dung/api/v1/auth/login", paramsLogin)
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
              res.data.roleCode === "COMPANY")
              {
                toast.success("Đăng nhập thành công ");
                window.location.href = "/admin/";
                
              }
            else {
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
      // toast.error(res.data.error)
      toast.error("Đăng nhập thất bại");
    }
  });
  };
  

  return (
    <>
      <div className="container d-flex justify-content-center align-items-center container_Otp">
        <div className="card text-center">
          <div className="card-header p-5">
            <img src="/assets/img/logo.png" />
            <h5 style={{ color: "#fff" }} className="mb-2">
              XÁC THỰC OTP
            </h5>
            <div>
              <small>
                mã đã được gửi tới sdt{" "}
                {props.dataUser && props.dataUser.phonenumber}
              </small>
            </div>
          </div>
          <div className="input-container d-flex flex-row justify-content-center mt-2">
            <input
              value={inputValues.so1}
              name="so1"
              onChange={(event) => handleOnChange(event)}
              type="text"
              className="m-1 text-center form-control rounded"
              maxLength={1}
            />
            <input
              value={inputValues.so2}
              name="so2"
              onChange={(event) => handleOnChange(event)}
              type="text"
              className="m-1 text-center form-control rounded"
              maxLength={1}
            />
            <input
              value={inputValues.so3}
              name="so3"
              onChange={(event) => handleOnChange(event)}
              type="text"
              className="m-1 text-center form-control rounded"
              maxLength={1}
            />
            <input
              value={inputValues.so4}
              name="so4"
              onChange={(event) => handleOnChange(event)}
              type="text"
              className="m-1 text-center form-control rounded"
              maxLength={1}
            />
            <input
              value={inputValues.so5}
              name="so5"
              onChange={(event) => handleOnChange(event)}
              type="text"
              className="m-1 text-center form-control rounded"
              maxLength={1}
            />
            <input
              value={inputValues.so6}
              name="so6"
              onChange={(event) => handleOnChange(event)}
              type="text"
              className="m-1 text-center form-control rounded"
              maxLength={1}
            />
          </div>
          <div>
            <small>
              bạn không nhận được Otp ?
              <a
                onClick={() => resendOTP()}
                style={{ color: "#3366FF" }}
                className="text-decoration-none ml-2"
              >
                Gửi lại
              </a>
            </small>
          </div>
          <div className="mt-3 mb-5">
          <div id="sign-in-button"></div>
            <button
              onClick={() => submitOTP()}
              className="btn btn-success px-4 verify-btn"
            >
              Xác thực
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default Otp;