import React, { useEffect, useState } from "react";
import "./Otp.scss";
import { toast } from "react-toastify";
import {createNewUser, requestOtp, verifyOtp} from "../../service/userService";

const Otp = (props) => {
  const [dataUser, setDataUser] = useState({});
  const [requestID, setRequestID] = useState(""); 
  const [inputValues, setInputValues] = useState({
    so1: "",
    so2: "",
    so3: "",
    so4: ""
  });

  useEffect(() => {
    if (props.dataUser) {
      sendOtpToUser();
    }
  }, [props.dataUser]);

  const handleOnChange = (event) => {
    const { name, value } = event.target;
    setInputValues({ ...inputValues, [name]: value });
  };

  const sendOtpToUser = async () => {
    try {
      const phoneNumber = props.dataUser.phonenumber;
      console.log(phoneNumber)
      const response = await requestOtp(phoneNumber);
      if (response) {
        setRequestID(response.result.requestID);
        toast.success("Đã gửi OTP thành công!");
      }
    } catch (error) {
      toast.error("Gửi OTP thất bại!");
    }
  };

  // Hàm xác nhận OTP
  const submitOTP = async () => {
    try {
      const otpCode = Object.values(inputValues).join("");
  
      if (otpCode.length !== 4) {
        toast.error("Vui lòng nhập đầy đủ 4 số OTP!");
        return;
      }
      // if(otpCode === "1234") {
      //   toast.success("Xác minh OTP thành công!");
  
      //   const res = await createNewUser({
      //     password: props.dataUser.password,
      //     firstName: props.dataUser.firstName,
      //     lastName: props.dataUser.lastName,
      //     phonenumber: props.dataUser.phonenumber,
      //     roleCode: props.dataUser.roleCode,
      //     email: props.dataUser.email,
      //     genderCode: props.dataUser.genderCode,
      //     image: props.dataUser.image,
      //   });
  
      //   if (res && res.statusCode === 200) {
      //     toast.success("Tạo tài khoản thành công!");
      //   } else {
      //     toast.error(res.errMessage || "Tạo tài khoản thất bại!");
      //   }
      // } else {
      //   toast.error("Mã OTP không đúng!");
      // }
      const response = await verifyOtp(requestID, otpCode); 
      if (response.result) {
        toast.success("Xác minh OTP thành công!");
  
        const res = await createNewUser({
          password: props.dataUser.password,
          firstName: props.dataUser.firstName,
          lastName: props.dataUser.lastName,
          phonenumber: props.dataUser.phonenumber,
          roleCode: props.dataUser.roleCode,
          email: props.dataUser.email,
          genderCode: props.dataUser.genderCode,
          image: props.dataUser.image,
        });
  
        if (res && res.statusCode === 200) {
          toast.success("Tạo tài khoản thành công!");
        } else {
          toast.error(res.errMessage || "Tạo tài khoản thất bại!");
        }
      } else {
        toast.error("Mã OTP không đúng!");
      }
    } catch (error) {
      toast.error("Xác minh OTP thất bại!");
    }
  };
  

  return (
    <div className="container d-flex justify-content-center align-items-center container_Otp">
      <div className="card text-center">
        <div className="card-header p-5">
          <img src="https://raw.githubusercontent.com/Rustcodeweb/OTP-Verification-Card-Design/main/mobile.png" />
          <h5 style={{ color: "#fff" }} className="mb-2">XÁC THỰC OTP</h5>
          <div>
            <small>Mã đã được gửi tới số điện thoại {props.dataUser?.phonenumber}</small>
          </div>
        </div>
        <div className="input-container d-flex flex-row justify-content-center mt-2">
          {["so1", "so2", "so3", "so4"].map((name, index) => (
            <input
              key={index}
              value={inputValues[name]}
              name={name}
              onChange={handleOnChange}
              type="text"
              className="m-1 text-center form-control rounded"
              maxLength={1}
            />
          ))}
        </div>
        <div>
          <small>
            Bạn không nhận được OTP?{" "}
            <a onClick={sendOtpToUser} style={{ color: "#3366FF" }} className="text-decoration-none ml-2">
              Gửi lại
            </a>
          </small>
        </div>
        <div className="mt-3 mb-5">
          <button onClick={submitOTP} className="btn btn-success px-4 verify-btn">Xác thực</button>
        </div>
      </div>
    </div>
  );
};

export default Otp;