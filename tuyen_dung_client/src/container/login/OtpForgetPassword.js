import React, { useEffect, useState } from "react";
import "./Otp.scss";
import { toast } from "react-toastify";
import { forgetPassword, requestOtp, verifyOtp } from "../../service/userService";
import { Modal, ModalHeader, ModalFooter, ModalBody, Button } from "reactstrap";
import { useHistory } from "react-router";
const OtpForgetPassword = (props) => {
    let history = useHistory();
    const [dataUser, setdataUser] = useState({});
    const [requestID, setRequestID] = useState("");
    const [inputValues, setInputValues] = useState({
        so1: "",
        so2: "",
        so3: "",
        so4: "",
    });

    const [showDialog, setShowDialog] = useState(false);
    const [newPassword, setNewPassword] = useState("");

    useEffect(() => {
        const token = localStorage.getItem("token_user");
        if (token) {
        const userData = JSON.parse(localStorage.getItem("userData"));
        if (userData && (userData.codeRoleAccount === "ADMIN" || userData.codeRoleAccount === "EMPLOYER" || userData.codeRoleAccount === "COMPANY")) {
            history.push("/admin");
        } else {
            history.push("/");
        }
        }
        if (props.dataUser) {
            setdataUser(props.dataUser);
        }
        sendOtpToUser();
    }, [props.dataUser]);

    const handleOnChange = (event) => {
        const { name, value } = event.target;
        setInputValues({ ...inputValues, [name]: value });
    };

    const sendOtpToUser = async () => {
        try {
            const phoneNumber = props.dataUser;
            const response = await requestOtp(phoneNumber);
            if (response) {
                setRequestID(response.result.requestID);
                toast.success("Đã gửi OTP thành công!");
            }
        } catch (error) {
            toast.error("Gửi OTP thất bại!");
        }
    };

    const submitOTP = async () => {
        try {
            const otpCode = Object.values(inputValues).join("");

            if (otpCode.length !== 4) {
                toast.error("Vui lòng nhập đầy đủ 4 số OTP!");
                return;
            }
            // if(otpCode == "1234") {
            //     toast.success("Xác minh OTP thành công!");

            //     const forgetPasswordResponse = await forgetPassword(
            //         props.dataUser
            //     );
            //     if (forgetPasswordResponse && forgetPasswordResponse.code === 200) {
            //         setNewPassword(forgetPasswordResponse.result); // Lưu mật khẩu mới
            //         setShowDialog(true); // Hiển thị modal
            //     } else {
            //         toast.error("Không thể lấy lại mật khẩu. Vui lòng thử lại.");
            //     }
            // } else {
            //     toast.error("Mã OTP không đúng!");
            // }
            const response = await verifyOtp(requestID, otpCode);
            if (response.result) {
                toast.success("Xác minh OTP thành công!");

                const forgetPasswordResponse = await forgetPassword(
                    props.dataUser
                );
                if (forgetPasswordResponse && forgetPasswordResponse.code === 200) {
                    setNewPassword(forgetPasswordResponse.result); // Lưu mật khẩu mới
                    setShowDialog(true); // Hiển thị modal
                } else {
                    toast.error("Không thể lấy lại mật khẩu. Vui lòng thử lại.");
                }
            } else {
                toast.error("Mã OTP không đúng!");
            }
        } catch (error) {
            toast.error("Xác minh OTP hoặc lấy lại mật khẩu thất bại!");
        }
    };

    return (
        <>
            <div className="container d-flex justify-content-center align-items-center container_Otp">
                <div className="card text-center">
                    <div className="card-header p-5">
                        <img src="https://raw.githubusercontent.com/Rustcodeweb/OTP-Verification-Card-Design/main/mobile.png" />
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
                    </div>
                    <div>
                        <small>
                            bạn không nhận được Otp ?
                            <a
                                onClick={() => sendOtpToUser()}
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

            {/* Modal hiển thị mật khẩu mới */}
            <Modal isOpen={showDialog} toggle={() => setShowDialog(false)}>
                <ModalHeader toggle={() => setShowDialog(false)}>
                    Khôi phục mật khẩu thành công
                    <button
                        className="btn btn-link p-0"
                        style={{ fontSize: '2rem', position: 'absolute', top: '10px', right: '15px' }}
                        onClick={() => setShowDialog(false)}
                    >
                        <i className="fa-regular fa-circle-xmark"></i>
                    </button>
                </ModalHeader>
                <ModalBody>
                    Mật khẩu mới của bạn là: <strong>{newPassword}</strong>
                </ModalBody>
                <ModalFooter>
                    <Button color="primary" href="/login">
                        Đi tới trang đăng nhập
                    </Button>
                </ModalFooter>
            </Modal>
        </>
    );
};

export default OtpForgetPassword;
