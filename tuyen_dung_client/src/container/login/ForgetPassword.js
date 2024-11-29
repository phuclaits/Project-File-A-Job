import React from 'react'
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import handleValidate from '../utils/Validation';
import { checkUserPhoneService, changePasswordByphone, handleLoginService } from '../../service/userService';
import { Link } from 'react-router-dom';
import OtpForgetPassword from './OtpForgetPassword';
import axios from 'axios';
const ForgetPassword = () => {
    const [inputValidates, setValidates] = useState({
        phonenumber: true, newPassword: true, confirmPassword: true
    })
    const [inputValues, setInputValues] = useState({
        phonenumber: '', isOpen: false, isSuccess: false, newPassword: '', confirmPassword: '',
    });

    const handleOnChange = event => {
        const { name, value } = event.target;
        setInputValues({ ...inputValues, [name]: value });

    };

    let handleOpenVerifyOTP = async () => {
        let checkPhone = handleValidate(inputValues.phonenumber, "phone")
        if (!(checkPhone === true)) {
            setValidates({
                ...inputValidates,
                phonenumber: checkPhone
            })
            return
        }
        let res = await checkUserPhoneService(inputValues.phonenumber)
        if (res === true) {
            setInputValues({ ...inputValues, ["isOpen"]: true })
        } else {
            setValidates({
                ...inputValidates,
                phonenumber: true
            })
            toast.error("Số điện thoại không tồn tại!")
        }

    }


    return (
        <>
            {inputValues.isOpen === false &&
                <div className="container-scroller">
                    <div className="container-fluid page-body-wrapper full-page-wrapper">
                        <div className="content-wrapper d-flex align-items-center auth px-0">
                            <div className="row w-100 mx-0">
                                <div className="col-lg-4 mx-auto">
                                    <div className="auth-form-light text-left py-5 px-4 px-sm-5">
                                        <div className="brand-logo">
                                            <img src="/assets/img/logo/logo.png" alt="logo" />
                                        </div>
                                        <h4>Quên mật khẩu?</h4>
                                        <h6 className="font-weight-light">Đừng lo! Khôi phục trong vài giây</h6>
                                        <form className="pt-3">
                                            <div className="form-group">
                                                <input type="text" value={inputValues.phonenumber} name="phonenumber" onChange={(event) => handleOnChange(event)} className="form-control form-control-lg" id="exampleInputEmail1" placeholder="Số điện thoại" />
                                                {inputValidates.phonenumber && <p style={{ color: 'red' }}>{inputValidates.phonenumber}</p>}
                                            </div>
                                            <div className="mt-3">
                                                <a onClick={() => handleOpenVerifyOTP() } className="btn1 btn1-block btn1-primary1 btn1-lg font-weight-medium auth-form-btn1" >Xác nhận</a>
                                            </div>
                                            <div className="text-center mt-4 font-weight-light">
                                                Chưa có tài khoản? <Link to="/register" className="text-primary">Đăng ký</Link>
                                                <br></br>
                                                <br></br>
                                                Đã có tài khoản? <Link to="/login" className="text-primary">Đăng nhập</Link>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            }
            {inputValues.isOpen === true &&
                <OtpForgetPassword dataUser={inputValues.phonenumber} />
            }
        </>
    )
}

export default ForgetPassword
