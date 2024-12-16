import React from 'react'
import { useEffect, useState } from 'react';
import { createNewUser, getDetailUserById, UpdateUserService } from '../../../service/userService';
// import {  } from '../../utils/fetch';
import DatePicker from '../../../components/input/DatePicker';
import { toast } from 'react-toastify';
import { useParams } from "react-router-dom";
import localization from 'moment/locale/vi';
import moment from 'moment';
import Lightbox from 'react-image-lightbox';
import 'react-image-lightbox/style.css';
import CommonUtils from '../../utils/CommonUtils';
import { useFetchDataCodeGender, useFetchDataCodeRole } from '../../utils/fetch';

import LoadingPage from '../../../components/Loading';
const UserInfo = () => {
    const [birthday, setbirthday] = useState('');
    const [isChangeDate, setisChangeDate] = useState(false)
    const [isChangeImg, setisChangeImg] = useState(false)
    const [isActionADD, setisActionADD] = useState(true)
    const { id } = useParams();
    const [isLoading, setIsLoading] = useState(false)
    const [inputValues, setInputValues] = useState({
        password: '', firstName: '', lastName: '', address: '', phonenumber: '', genderCode: '', roleCode: '', id: '', dob: '', image: '', imageReview: '', isOpen: false, email: ''
    });

    let setStateUser = (data) => {
        setInputValues({
            ...inputValues,
            ["firstName"]: data.userAccountData.firstName,
            ["lastName"]: data.userAccountData.lastName,
            ["address"]: data.userAccountData.addressUser,
            ["phonenumber"]: data.phoneNumber,
            ["genderCode"]: data.userAccountData.genderCodeValue.code,
            ["roleCode"]: data.userAccountData.roleCode,
            ["id"]: data.userAccountData.id,
            ["dob"]: data.userAccountData.dobUser,
            ["image"]: data.userAccountData.image,
            ["imageReview"]: data.userAccountData.image,
            ["email"]: data.userAccountData.email
        })
        setbirthday(moment.unix(+data.userAccountData.dobUser / 1000).locale('vi').format('DD/MM/YYYY'))
    }
    useEffect(() => {
        const userData = JSON.parse(localStorage.getItem('userData'));
        if (userData) {
            let fetchUser = async () => {
                setisActionADD(false)
                let user = await getDetailUserById(userData.id)
                if (user && user.code === 200) {
                    setStateUser(user.result)
                    // console.log(user)
                }
            }
            fetchUser()
        }
    }, [])
    const { dataGender: DataGender } = useFetchDataCodeGender();
    const { dataRulesuser : dataCodeRole } = useFetchDataCodeRole();
    

    if (DataGender && DataGender.length > 0 && inputValues.genderCode === '' && dataCodeRole && dataCodeRole.length > 0 && inputValues.roleCode === '') {
        setInputValues({ ...inputValues, ["genderCode"]: DataGender[0].code, ["roleCode"]: dataCodeRole[0].code })
    }

    const handleOnChange = event => {
        const { name, value } = event.target;
        setInputValues({ ...inputValues, [name]: value });

    };

    let handleOnChangeDatePicker = (date) => {
        setbirthday(date[0])
        setisChangeDate(true)

    }
    let handleOnChangeImage = async (event) => {
        let data = event.target.files;
        let file = data[0];
        if (file) {
            let base64 = await CommonUtils.getBase64(file);
            let objectUrl = URL.createObjectURL(file)
            setisChangeImg(true)
            setInputValues({ ...inputValues, ["image"]: base64, ["imageReview"]: objectUrl })

        }
    }
    let openPreviewImage = () => {
        if (!inputValues.imageReview) return;

        setInputValues({ ...inputValues, ["isOpen"]: true })
    }

    function base64ToBlob(base64, type = '') {
        const byteCharacters = atob(base64.split(',')[1]);
        const byteNumbers = new Array(byteCharacters.length);
        
        for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        
        const byteArray = new Uint8Array(byteNumbers);
        return new Blob([byteArray], { type: type });
    }
    let handleSaveUser = async () => {
        setIsLoading(true)
        if(inputValues.firstName === '' || inputValues.lastName === '' || inputValues.address === '' || inputValues.email === ''||inputValues.dob === '' 
            || inputValues.genderCode === '' || inputValues.genderCode === '' || inputValues.image === ''){
            setIsLoading(false)
            toast.error("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        let formData = new FormData();
        // Append the fields to the FormData object
        formData.append('id', inputValues.id);
        formData.append('firstName', inputValues.firstName);
        formData.append('lastName', inputValues.lastName);
        formData.append('address', inputValues.address);
        formData.append('roleCode', inputValues.roleCode);
        formData.append('genderCode', inputValues.genderCode);
        
        // Handle the date
        formData.append('dob', isChangeDate === false ? inputValues.dob : new Date(birthday).getTime());    
        formData.append('email', inputValues.email);

        if (inputValues.image.startsWith("data:image/jpeg;base64,") || inputValues.image.startsWith("data:image/png;base64,")) {
          const blob = base64ToBlob(inputValues.image, 'image/jpeg');
          formData.append("fileImage", blob, "image.jpg");
        }else{
          formData.append("image", inputValues.imageReview); 
        }
        let res = await UpdateUserService(formData);
         if (res && res.code === 200) {
              setIsLoading(false)
          toast.success("Cập nhật người dùng thành công");
           // Update local storage with the new user data
           let currentUser = JSON.parse(localStorage.getItem('userData'));
            console.log(currentUser)
           if (currentUser) {
               // Update only the image field
               currentUser.image = inputValues.image;
   
               // Save the updated data to local storage
               localStorage.setItem('userData', JSON.stringify(currentUser));
           }
            setTimeout(() => {
              window.location.reload();
            }, 3000);
    
        } else {
            setIsLoading(false)
          toast.error(res.errMessage);
        }
      };

    return (
        <div className=''>
            <div className="col-12 grid-margin">
                <div className="card">
                    <div className="card-body">
                        <h4 className="card-title">Thông tin cá nhân</h4>
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
                                        <label className="col-sm-3 col-form-label">Địa chỉ</label>
                                        <div className="col-sm-9">
                                            <input type="text" value={inputValues.address} name="address" onChange={(event) => handleOnChange(event)} className="form-control" />
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
                                            <select className="form-control" value={inputValues.genderCode} name="genderCode" onChange={(event) => handleOnChange(event)}>
                                                {DataGender && DataGender.length > 0 &&
                                                    DataGender.map((item, index) => {
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
                            <div className="row ">
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Hình ảnh</label>
                                        <div className="col-sm-9">
                                            <input onChange={(event) => handleOnChangeImage(event)} type="file" className="form-control form-file" />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Hình ảnh hiển thị</label>
                                        <div className="col-sm-9">
                                            <div style={{ backgroundImage: `url(${inputValues.imageReview})`,backgroundSize: 'cover', // Giữ tỷ lệ ảnh và không bị kéo dãn
                        backgroundPosition: 'center', // Căn chỉnh hình ảnh tại giữa
                        width: '150px', // Thiết lập chiều rộng hình ảnh
                        height: '150px', // Thiết lập chiều cao hình ảnh
                        borderRadius: '8px', }} onClick={() => openPreviewImage()} className="box-img-preview"></div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div className="row">
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Email</label>
                                        <div className="col-sm-9">
                                            <input type="email" value={inputValues.email} name="email" onChange={(event) => handleOnChange(event)} className="form-control" />
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
            {
                inputValues.isOpen === true &&
                <Lightbox mainSrc={inputValues.imageReview}
                    onCloseRequest={() => setInputValues({ ...inputValues, ["isOpen"]: false })}
                />
            }
            {isLoading && <LoadingPage />}
        </div>
    )
}

export default UserInfo
