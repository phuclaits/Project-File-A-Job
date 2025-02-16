import React from 'react'
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import DatePicker from '../../../components/input/DatePicker';
import { createPostService, updatePostService, getDetailToEditPostService, reupPostService, getDetailCompanyByUserId } from '../../../service/userService';
import MarkdownIt from 'markdown-it';
import MdEditor from 'react-markdown-editor-lite';
import 'react-markdown-editor-lite/lib/index.css';

import { useHistory, useParams } from "react-router-dom";
import { Spinner, Modal } from 'reactstrap'
import localization from 'moment/locale/vi';
import moment from 'moment';
import '../../../components/modal/modal.css'
import ReupPostModal from '../../../components/modal/ReupPostModal';
import { useFetchDataExpType, useFetchDataGenderPost, useFetchDataJobLevel, useFetchDataJobLocation, useFetchDataJobType, useFetchDataSalaryType, useFetchDataWorkType } from '../../utils/fetch';
import LoadingPage from '../../../components/Loading';
const AddPost = () => {
    const today = new Date();
    const mdParser = new MarkdownIt();
    const [user, setUser] = useState({})
    const [timeEnd, settimeEnd] = useState('');
    const [isChangeDate, setisChangeDate] = useState(false)
    const [isLoading, setIsLoading] = useState(false)
    const id = useParams();
    const params = useParams();

    const [companyPostAllow, setCompanyPostAllow] = useState({
        hot: 0,
        nonHot: 0
    })
    const [inputValues, setInputValues] = useState({
        name: '', categoryJobCode: '', addressCode: '', salaryJobCode: '', amount: '', timeEnd: '', categoryJoblevelCode: '', categoryWorktypeCode: '', experienceJobCode: '',
        genderCode: '', descriptionHTML: '', descriptionMarkdown: '', isActionADD: true, id: '', isHot: 0
    });
    const [propsModal, setPropsModal] = useState({
        isActive: false,
        handlePost: () => { },
    })
    let fetchCompany = async (userId, companyId = null) => {
        let res = await getDetailCompanyByUserId(userId, companyId)
        if (res && res.errCode === 0) {
            setCompanyPostAllow({
                ...companyPostAllow,
                hot: res.data.allowHotPost,
                nonHot: res.data.allowPost
            })
        }
    }
    useEffect(() => {

        const userData = JSON.parse(localStorage.getItem('userData'));
        if (userData !== "ADMIN") {
            fetchCompany(userData.id)
        }
        
        if (params.id) {
            fetchPost(params.id)
        }
        setUser(userData)
    }, [])
    let fetchPost = async (id) => {
        console.log(id)
        let res = await getDetailToEditPostService(params.id)
        console.log(res)
        if (res.errCode === 0) {
            setStatePost(res.data)
        }
    }
    let setStatePost = (data) => {
        setInputValues({
            ...inputValues,
            ["name"]: data.name,
            ["categoryJobCode"]: data.categoryJobCode,
            ["addressCode"]: data.adressCode,
            ["salaryJobCode"]: data.salaryCode,
            ["amount"]: data.amount, 
            ["timeEnd"]: data.timeEndValue,
            ["categoryJoblevelCode"]: data.categoryJobLevelCode,
            ["categoryWorktypeCode"]: data.categoryWorktypeCode,
            ["experienceJobCode"]: data.experienceJobCode,
            ["genderCode"]: data.genderPostCode, 
            ["descriptionHTML"]: data.descriptionHTMLValue,
            ["descriptionMarkdown"]: data.descriptionMarkdownValue,
            ["isActionADD"]: false,
            ["id"]: data.id

        })
        document.querySelector('[name="categoryJobCode"]').value = data.categoryJobCode
        document.querySelector('[name="addressCode"]').value = data.adressCode
        document.querySelector('[name="salaryJobCode"]').value = data.salaryCode
        document.querySelector('[name="categoryJoblevelCode"]').value = data.categoryJobLevelCode
        document.querySelector('[name="categoryWorktypeCode"]').value = data.categoryWorktypeCode
        document.querySelector('[name="experienceJobCode"]').value = data.experienceJobCode
        document.querySelector('[name="genderCode"]').value = data.genderPostCode
        settimeEnd(moment.unix(+data.timeEndValue / 1000).locale('vi').format('DD/MM/YYYY'))
    }
    let { dataJobLevel : dataJobLevels } = useFetchDataJobLevel();
    let { dataGenderPost: dataGenderPosts } = useFetchDataGenderPost();
    let { dataWorkType: dataWorkTypes }  = useFetchDataWorkType();
    let { dataJobLocation: dataJobLocations} = useFetchDataJobLocation();
    let { dataExpType: dataExpTypes } = useFetchDataExpType();
    let { dataSalaryType: dataSalaryTypes} = useFetchDataSalaryType();
    let { dataJobType: dataJobTypes } = useFetchDataJobType();



    if (dataGenderPosts && dataGenderPosts.length > 0 && inputValues.genderCode === '' && dataJobTypes && dataJobTypes.length > 0 && inputValues.categoryJobCode === '' && dataJobLevels && dataJobLevels.length > 0 && inputValues.categoryJoblevelCode === '' &&
        dataSalaryTypes && dataSalaryTypes.length > 0 && inputValues.salaryJobCode === '' && dataExpTypes && dataExpTypes.length > 0 && inputValues.experienceJobCode === '' &&
        dataWorkTypes && dataWorkTypes.length > 0 && inputValues.categoryWorktypeCode === '' && dataJobLocations && dataJobLocations.length > 0 && inputValues.addressCode === ''
    ) {

        setInputValues({
            ...inputValues, ["genderCode"]: dataGenderPosts[0].code, ["categoryJobCode"]: dataJobTypes[0].code,
            ["categoryJoblevelCode"]: dataJobLevels[0].code, ["salaryJobCode"]: dataSalaryTypes[0].code, ["experienceJobCode"]: dataExpTypes[0].code,
            ["categoryWorktypeCode"]: dataWorkTypes[0].code, ["addressCode"]: dataJobLocations[0].code
        })
    }
    const handleOnChange = event => {
        const { name, value } = event.target;
        setInputValues({ ...inputValues, [name]: value });

    };
    let handleIsHot = (e) => {
        setInputValues({
            ...inputValues,
            isHot: e.target.checked ? 1 : 0
        })
    }
    let handleEditorChange = ({ html, text }) => {
        setInputValues({
            ...inputValues,
            ["descriptionMarkdown"]: text,
            ["descriptionHTML"]: html
        })
    }
    let handleOnChangeDatePicker = (date) => {
        settimeEnd(date[0])
        setisChangeDate(true)

    }
    let handleSavePost = async () => {
        setIsLoading(true)
        if (inputValues.isActionADD === true) {

            if (new Date().getTime() > new Date(timeEnd).getTime()) {
                toast.error("Ngày kết thúc phải hơn ngày hiện tại")
            }
            else {
                if(inputValues.name === ""||inputValues.descriptionHTML === ""||inputValues.descriptionMarkdown === "" || 
                    inputValues.categoryJobCode === "" || inputValues.addressCode === "" || inputValues.salaryJobCode === "" ||
                    inputValues.categoryJoblevelCode === "" || inputValues.categoryWorktypeCode === "" ||
                    inputValues.experienceJobCode === "" || inputValues.genderCode === "" || inputValues.amount === ""){
                        setIsLoading(false)
                        toast.error("Vui lòng nhập đầy đủ thông tin")
                        return;
                    }
                let res = await createPostService({
                    name: inputValues.name,
                    descriptionHTML: inputValues.descriptionHTML,
                    descriptionMarkdown: inputValues.descriptionMarkdown,
                    categoryJobCode: inputValues.categoryJobCode,
                    addressCode: inputValues.addressCode,
                    salaryJobCode: inputValues.salaryJobCode,
                    amount: inputValues.amount,
                    timeEnd: new Date(timeEnd).getTime(),
                    categoryJoblevelCode: inputValues.categoryJoblevelCode,
                    categoryWorktypeCode: inputValues.categoryWorktypeCode,
                    experienceJobCode: inputValues.experienceJobCode,
                    genderPostCode: inputValues.genderCode,
                    userId: user.id,
                    isHot: inputValues.isHot
                })
                setTimeout(() => { 
                    setIsLoading(false)
                    if (res && res.errCode === 0) {
                        fetchCompany(user.id)
                        toast.success(res.errMessage)
                        setInputValues({
                            ...inputValues,
                            ["name"]: '',
                            ["descriptionHTML"]: '',
                            ["descriptionMarkdown"]: '',
                            ["categoryJobCode"]: '',
                            ["addressCode"]: '',
                            ["salaryJobCode"]: '',
                            ["amount"]: '',
                            ["timeEnd"]: '',
                            ["categoryJoblevelCode"]: '',
                            ["categoryWorktypeCode"]: '',
                            ["experienceJobCode"]: '',
                            ["genderCode"]: '',
                            ["isHot"]: 0
                        })
                        settimeEnd('')
                    } else {
                        toast.error(res.errMessage)
                    }
                }, 1000);
            }
        } else {
            if(inputValues.name === ""||inputValues.descriptionHTML === ""||inputValues.descriptionMarkdown === "" || 
                inputValues.categoryJobCode === "" || inputValues.addressCode === "" || inputValues.salaryJobCode === "" ||
                inputValues.categoryJoblevelCode === "" || inputValues.categoryWorktypeCode === "" ||
                inputValues.experienceJobCode === "" || inputValues.genderCode === "" || inputValues.amount === ""){
                    setIsLoading(false)
                    toast.error("Vui lòng nhập đầy đủ thông tin")
                    return;
                }
            let res = await updatePostService({
                name: inputValues.name,
                descriptionHTML: inputValues.descriptionHTML,
                descriptionMarkdown: inputValues.descriptionMarkdown,
                categoryJobCode: inputValues.categoryJobCode,
                addressCode: inputValues.addressCode,
                salaryJobCode: inputValues.salaryJobCode,
                amount: inputValues.amount,
                timeEnd: isChangeDate === false ? inputValues.timeEnd : new Date(timeEnd).getTime(),
                categoryJoblevelCode: inputValues.categoryJoblevelCode,
                categoryWorktypeCode: inputValues.categoryWorktypeCode,
                experienceJobCode: inputValues.experienceJobCode,
                genderPostCode: inputValues.genderCode,
                id: inputValues.id,
                userId: user.id
            })
            setTimeout(() => {
                setIsLoading(false)
                if (res && res.errCode === 0) {
                    toast.success(res.errMessage)

                } else {
                    toast.error(res.errMessage)
                }
            }, 1000);
        }
    }
    let handleReupPost = async (timeEnd) => {
        let res = await reupPostService({
            userId: user.id,
            postId: id,
            timeEnd: timeEnd
        })
        if (res && res.errCode === 0) {
            toast.success(res.errMessage)

        } else {
            toast.error(res.errMessage)
        }
    }
    
    const history = useHistory()
    return (
        <>
            <div className=''>
                <div className="col-12 grid-margin">
                    <div className="card">
                        <div className="card-body">
                            <div onClick={() => history.goBack()} className='mb-2 hover-pointer' style={{ color: 'red' }}><i class="fa-solid fa-arrow-left mr-2"></i>Quay lại</div>
                            <h4 className="card-title">{inputValues.isActionADD === true ? 'Thêm mới bài đăng' :(user?.codeRoleAccount === 'ADMIN' ? 'Xem thông tin bài đăng' : 'Cập nhật bài đăng')}</h4>
                            <br></br>
                            {inputValues.isActionADD === true && user.codeRoleAccount !== "ADMIN" &&
                                <div className='mb-5'>
                                    <h4>Công ty còn:</h4>
                                    <p>{companyPostAllow.nonHot} bài bình thường</p>
                                    <p>{companyPostAllow.hot} bài nổi bật</p>
                                </div>
                            }
                            <form className="form-sample">

                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Tên bài đăng</label>
                                            <div className="col-sm-9">
                                                <input disabled={user?.codeRoleAccount === "ADMIN" ? true : false} value={inputValues.name} name="name" onChange={(event) => handleOnChange(event)} type="text" className="form-control" />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Địa chỉ</label>
                                            <div className="col-sm-9">
                                                <select disabled={user?.codeRoleAccount === "ADMIN"  ?  true : false} style={{ color: "black" }} className="form-control" value={inputValues.addressCode} name="addressCode" onChange={(event) => handleOnChange(event)}>
                                                    {dataJobLocations && dataJobLocations.length > 0 &&
                                                        dataJobLocations.map((item, index) => {
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
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">SL nhân viên</label>
                                            <div className="col-sm-9">
                                                <input disabled={user?.codeRoleAccount === "ADMIN"  ? true : false} value={inputValues.amount} name="amount" onChange={(event) => handleOnChange(event)} type="number" className="form-control" />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Thời gian KT</label>
                                            <div className="col-sm-9">
                                                <DatePicker disabled={!inputValues.isActionADD} dis className="form-control" onChange={handleOnChangeDatePicker}
                                                    value={timeEnd}
                                                />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Giới tính</label>
                                            <div className="col-sm-9">
                                                <select disabled={user?.codeRoleAccount === "ADMIN"  ? true : false} style={{ color: "black" }} className="form-control" value={inputValues.genderCode} name="genderCode" onChange={(event) => handleOnChange(event)}>
                                                    {dataGenderPosts && dataGenderPosts.length > 0 &&
                                                        dataGenderPosts.map((item, index) => {
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
                                            <label className="col-sm-3 col-form-label">Kinh nghiệm</label>
                                            <div className="col-sm-9">
                                                <select disabled={user?.codeRoleAccount === "ADMIN"  ? true : false} style={{ color: "black" }} className="form-control" value={inputValues.experienceJobCode} name="experienceJobCode" onChange={(event) => handleOnChange(event)}>
                                                    {dataExpTypes && dataExpTypes.length > 0 &&
                                                        dataExpTypes.map((item, index) => {
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
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Ngành</label>
                                            <div className="col-sm-9">
                                                <select disabled={user?.codeRoleAccount === "ADMIN"  ? true : false} style={{ color: "black" }} className="form-control" value={inputValues.categoryJobCode} name="categoryJobCode" onChange={(event) => handleOnChange(event)}>
                                                    {dataJobTypes && dataJobTypes.length > 0 &&
                                                        dataJobTypes.map((item, index) => {
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
                                            <label className="col-sm-3 col-form-label">Chức vụ</label>
                                            <div className="col-sm-9">
                                                <select disabled={user?.codeRoleAccount === "ADMIN"  ? true : false} style={{ color: "black" }} className="form-control" value={inputValues.categoryJoblevelCode} name="categoryJoblevelCode" onChange={(event) => handleOnChange(event)}>
                                                    {dataJobLevels && dataJobLevels.length > 0 &&
                                                        dataJobLevels.map((item, index) => {
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
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Lương</label>
                                            <div className="col-sm-9">
                                                <select disabled={user?.codeRoleAccount === "ADMIN"  ? true : false} style={{ color: "black" }} className="form-control" value={inputValues.salaryJobCode} name="salaryJobCode" onChange={(event) => handleOnChange(event)}>
                                                    {dataSalaryTypes && dataSalaryTypes.length > 0 &&
                                                        dataSalaryTypes.map((item, index) => {
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
                                            <label className="col-sm-3 col-form-label">Hình thức LV</label>
                                            <div className="col-sm-9">
                                                <select disabled={user?.codeRoleAccount === "ADMIN"  ? true : false} style={{ color: "black" }} className="form-control" value={inputValues.categoryWorktypeCode} name="categoryWorktypeCode" onChange={(event) => handleOnChange(event)}>
                                                    {dataWorkTypes && dataWorkTypes.length > 0 &&
                                                        dataWorkTypes.map((item, index) => {
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
                                {inputValues.isActionADD && (<>
                                    <div className='row'>
                                        <div className="col-md-6">
                                            <div className="form-group row">
                                                <label className="col-sm-3 col-form-label">Bài viết nổi bật</label>
                                                <div className="col-sm-9">
                                                    <input disabled={user?.codeRoleAccount === "ADMIN"  ? true : false} onChange={handleIsHot} checked={inputValues.isHot} style={{ marginTop: '20px' }} type={'checkbox'}></input>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </>)}
                                <div className="row">
                                    <div className="col-md-12">
                                        <label className="form-label">Mô tả công việc</label>
                                        <div className="form-group">

                                            <MdEditor
                                                style={{ height: '500px' }}
                                                renderHTML={text => mdParser.render(text)}
                                                onChange={handleEditorChange}
                                                value={inputValues.descriptionMarkdown}
                                            />
                                        </div>
                                    </div>

                                </div>
                                {
                                        user.codeRoleAccount !== "ADMIN"  &&
                                        <>
                                            <button onClick={() => handleSavePost()} type="button" className="btn1 btn1-primary1 btn1-icon-text">
                                                <i class="ti-file btn1-icon-prepend"></i>
                                                Lưu
                                            </button>
                                        </>
                                }
                                {
                                    id && user.codeRoleAccount !== "ADMIN"&& user.codeRoleAccount !== "EMPLOYER"  && new Date().getTime() > new Date(timeEnd).getTime() &&
                                    <>
                                        <button onClick={() => setPropsModal({
                                            ...propsModal,
                                            isActive: true,
                                            handlePost: handleReupPost
                                        })} type="button" className="ml-2 btn1 btn1-primary1 btn1-icon-text">
                                            <i class="ti-file btn1-icon-prepend"></i>
                                            Đăng lại
                                        </button>
                                    </>
                                }
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            {isLoading &&
                    <LoadingPage />
                    
                }
            <ReupPostModal isOpen={propsModal.isActive} onHide={() => setPropsModal({
                ...propsModal,
                isActive: false,
            })} id={propsModal.postId} handleFunc={propsModal.handlePost} />
        </>
    )
}

export default AddPost
