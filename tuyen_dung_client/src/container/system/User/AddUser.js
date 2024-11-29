import React from 'react'
import { useEffect, useState } from 'react';

import {
    getDetailUserById,
    UpdateUserService, // 
    createNewUserByEmployeer,//
  } from "../../../service/userService";
import { useFetchDataCodeGender,useFetchRuleUser } from "../../utils/fetch";
import DatePicker from "../../../components/input/DatePicker";
import { toast } from 'react-toastify';
import { useHistory, useParams } from "react-router-dom";
import moment from 'moment';
import { Spinner, Modal } from 'reactstrap'
import '../../../components/modal/modal.css'
import LoadingPage from '../../../components/Loading';
const AddUser = () => {
    const user = JSON.parse(localStorage.getItem("userData"))
    const [birthday, setbirthday] = useState('');
    const [isChangeDate, setisChangeDate] = useState(false)
    const [isActionADD, setisActionADD] = useState(true)
    const [isLoading, setIsLoading] = useState(false)
    const { id } = useParams();
    const [inputValues, setInputValues] = useState({
       id: '', dob: '', email: '', firstName: '', lastName: '', address: '', phonenumber: '', genderCode: '', roleCode: '',  image: ''
    });
    let setStateUser = (data) => {
        setInputValues({
            ...inputValues,
            ["firstName"]: data.userAccountData.firstName,
            ["lastName"]: data.userAccountData.lastName,
            ["address"]: data.userAccountData.address,
            ["phonenumber"]: data.phonenumber,
            ["genderCode"]: data.userAccountData.genderCode,
            ["roleCode"]: data.roleData.code,
            ["id"]: data.userAccountData.id,
            ["dob"]: data.userAccountData.dob,

        })
        document.querySelector('[name="genderCode"]').value = data.userAccountData.genderCode
        document.querySelector('[name="roleCode"]').value = data.roleData.code
        setbirthday(data.userAccountData.dob ? moment.unix(+data.userAccountData.dob / 1000).locale('vi').format('DD/MM/YYYY') : null)
    }
    useEffect(() => {

        if (id) {
            let fetchUser = async () => {
                setisActionADD(false)
                let user = await getDetailUserById(id)
                if (user && user.errCode === 0) {
                    setStateUser(user.data)
                }
            }
            fetchUser()
        }
    }, [])
    const { dataGender: Genderdata } = useFetchDataCodeGender();
    const { dataRulesuser: dataRole } = useFetchRuleUser();
    let filteredDataRole = dataRole;

    if (dataRole && dataRole.length > 0) {
        console.log(dataRole)
        if (user.codeRoleAccount === 'COMPANY'){
            filteredDataRole = filteredDataRole.filter(item => item.code !== "ADMIN" && item.code !== "CANDIDATE");
        }
        else if (user.codeRoleAccount === 'ADMIN' && isActionADD=== true) 
        {
            filteredDataRole = filteredDataRole.filter(item => item.code !== "COMPANY")
        }
    }
    if (Genderdata && Genderdata.length > 0 && inputValues.genderCode === '' && dataRole && dataRole.length > 0 && inputValues.codeRoleAccount === '' && isActionADD) {
        setInputValues({ ...inputValues, ["genderCode"]: Genderdata[0].code, ["roleCode"]: dataRole[0].code })
    }
    

    const handleOnChange = event => {
        const { name, value } = event.target;
        setInputValues({ ...inputValues, [name]: value });
    };

    let handleOnChangeDatePicker = (date) => {
        setbirthday(date[0])
        setisChangeDate(true)

    }
    let handleSaveUser = async () => {
        setIsLoading(true)
        if (isActionADD === true) {
            let params = {
                email : inputValues.email,
                firstName: inputValues.firstName,
                lastName: inputValues.lastName,
                address: inputValues.address,
                roleCode: inputValues.roleCode,
                genderCode: inputValues.genderCode,
                phonenumber: inputValues.phonenumber,
                image: 'https://inkythuatso.com/uploads/thumbnails/800/2023/03/6-anh-dai-dien-trang-inkythuatso-03-15-26-36.jpg',
                dob: new Date(birthday).getTime(),
            }
            if (user.codeRoleAccount === "COMPANY")
            {
                params.companyId = user.idCompany
            }
            let res = await createNewUserByEmployeer(params)
            setTimeout(() => {
                setIsLoading(false)
                if (res && res.errCode === 0) {
                    toast.success(`Thêm mới user thành công, Chúng tôi đã gửi tài khoản mật khẩu qua ${inputValues.email} `)
                    setInputValues({
                        ...inputValues,
                        ["firstName"]: '',
                        ["lastName"]: '',
                        ["address"]: '',
                        ["phonenumber"]: '',
                        ["genderCode"]: '',
                        ["roleCode"]: '',
                        ["image"]: '',
                        ["password"]: '',
                    })
                    setbirthday('')
                } else {

                    toast.error(res.errMessage)
                }
            }, 1000);
        } else {
            let res = await UpdateUserService({
                id: inputValues.id,
                firstName: inputValues.firstName,
                lastName: inputValues.lastName,
                address: inputValues.address,
                roleCode: inputValues.roleCode,
                genderCode: inputValues.genderCode,
                dob: isChangeDate === false ? inputValues.dob : new Date(birthday).getTime()
            })
            setTimeout(() => {
                setIsLoading(false)
                if (res && res.errCode === 0) {
                    toast.success("Cập nhật người dùng thành công")

                } else {
                    toast.error(res.errMessage)
                }
            }, 1000);

        }
    }
    const history = useHistory()
    return (
        <div className=''>
            <div className="col-12 grid-margin">
                <div className="card">
                    <div className="card-body">
                    <div onClick={()=> history.goBack()} className='mb-2 hover-pointer' style={{color:'red'}}><i class="fa-solid fa-arrow-left mr-2"></i>Quay lại</div>
                        
                        <h4 className="card-title">{isActionADD === true ? 'Thêm mới người dùng' : 'Cập nhật người dùng'}</h4>
                        <br></br>
                        <form className="form-sample">

                            <div className="row">
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Họ</label>
                                        <div className="col-sm-9">
                                            <input type="text" value={inputValues.firstName} name="firstName" onChange={(event) => handleOnChange(event)} className="form-control" />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Tên</label>
                                        <div className="col-sm-9">
                                            <input type="text" value={inputValues.lastName} name="lastName" onChange={(event) => handleOnChange(event)} className="form-control" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Email</label>
                                        <div className="col-sm-9">
                                            <input type="email" value={inputValues.email} disabled={isActionADD === true ? false : true} name="email" onChange={(event) => handleOnChange(event)} className="form-control" />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Số điện thoại</label>
                                        <div className="col-sm-9">
                                            <input type="number" value={inputValues.phonenumber} disabled={isActionADD === true ? false : true} name="phonenumber" onChange={(event) => handleOnChange(event)} className="form-control" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Giới tính</label>
                                        <div className="col-sm-9">
                                            <select style={{color: "black"}} className="form-control" value={inputValues.genderCode} name="genderCode" onChange={(event) => handleOnChange(event)}>
                                                {Genderdata && Genderdata.length > 0 &&
                                                    Genderdata.map((item, index) => {
                                                        return (
                                                            <option key={index} value={item.code}>{item.value}</option>
                                                        )
                                                    })
                                                }
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Ngày sinh</label>
                                        <div className="col-sm-9">
                                            <DatePicker className="form-control" onChange={handleOnChangeDatePicker}
                                                value={birthday}

                                            />

                                        </div>
                                    </div>
                                </div>
                            </div>


                            <div className="row">
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Địa chỉ</label>
                                        <div className="col-sm-9">
                                            <input type="text" value={inputValues.address} name="address" onChange={(event) => handleOnChange(event)} className="form-control" />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Quyền</label>
                                        <div className="col-sm-9">
                                            <select style={{color: "black"}} className="form-control" value={inputValues.roleCode} name="roleCode" onChange={(event) => handleOnChange(event)}>
                                                {filteredDataRole && filteredDataRole.length > 0 &&
                                                    filteredDataRole.map((item, index) => {
                                                        return (
                                                            <option key={index} value={item.code}>{item.value}</option>
                                                        )
                                                    })
                                                }
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <button type="button" onClick={() => handleSaveUser()} className="btn1 btn1-primary1 btn1-icon-text">
                                <i class="ti-file btn1-icon-prepend"></i>
                                Lưu
                            </button>
                        </form>
                    </div>
                </div>
            </div>
            {isLoading &&
                    <LoadingPage />
                    
                }
        </div>
    )
}

export default AddUser
