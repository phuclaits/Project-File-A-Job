import React from 'react'
import { useEffect, useState } from 'react';
import { getDetailUserById, UpdateUserSettingService, getAllSkillByJobCode } from '../../../service/userService';
import { checkSeeCandiate } from '../../../service/cvService';
import { toast } from 'react-toastify';
import 'react-image-lightbox/style.css';
import { Select } from 'antd'
import { useHistory, useParams } from "react-router-dom";
import { useFetchDataExpType, useFetchDataJobLocation, useFetchDataJobType, useFetchDataSalaryType } from '../../utils/fetch';

const DetailFilterUser = () => {
    const [listSkills,setListSkills] = useState([])
    const [inputValues, setInputValues] = useState({
        jobType: '', salary: '', skills: [], jobProvince: '', exp: '', file: ''
    });
    const { id } = useParams();
    let getListSkill = async(jobType) => {
        let res = await getAllSkillByJobCode(jobType)
        let listSkills =  res.data.map(item=>({
            value: item.id,
            label: item.name
        }))
        setListSkills(listSkills)
    }

    let setStateUser = (data) => {
        console.log(data.userAccountData.userSettingData.categoryJobCode.code);
        getListSkill(data.userAccountData.userSettingData.categoryJobCode.code)
        let listSkills = []
        if (Array.isArray(data.listSkills) && data.listSkills.length > 0) {
            listSkills = data.listSkills.map(item=>item.skill.name)
        }
        setInputValues({
            ...inputValues,
            jobType: data.userAccountData.userSettingData.categoryJobCode,
            salary: data.userAccountData.userSettingData.salaryJobCode,
            skills: listSkills,
            jobProvince: data.userAccountData.userSettingData.addressCode,
            exp: data.userAccountData.userSettingData.experienceJobCode,
            isFindJob: data.userAccountData.userSettingData.isFindJob,
            isTakeMail: data.userAccountData.userSettingData.isTakeMail,
            file: data.userAccountData.userSettingData.file
        })
    }
    useEffect(() => {
        
        if (id) {
            console.log(id);
            let fetchUser = async () => {
                let userData = JSON.parse(localStorage.getItem("userData"))
                let check = await checkSeeCandiate({
                    userId: userData.id,
                    companyId: userData.idCompany
                })
                if (check.data.errCode ===0 ) {
                    let user = await getDetailUserById(id)
                    console.log(user);
                    
                    if (user && user.code === 200) {
                        console.log(user);
                        
                        setStateUser(user.result)
                    }
                } else {
                    toast.error(check.errMessage)
                    setTimeout(()=> {
                        history.push('/admin/list-candiate/')
                    },1000)
                }
            }
            fetchUser()
        }
    }, [])
    
    let { dataJobLocation: dataJobLocationF} = useFetchDataJobLocation();
    let { dataExpType: dataExpTypeF } = useFetchDataExpType();
    let { dataSalaryType: dataSalaryTypeF} = useFetchDataSalaryType();
    let { dataJobType: dataJobTypeF } = useFetchDataJobType();

    dataJobLocationF = dataJobLocationF.map(item=>({
        value: item.code,
        label: item.value,
    }))

    dataExpTypeF = dataExpTypeF.map(item=>({
        value: item.code,
        label: item.value,
    }))

    dataSalaryTypeF = dataSalaryTypeF.map(item=>({
        value: item.code,
        label: item.value,
    }))

    dataJobTypeF = dataJobTypeF.map(item=>({
        value: item.code,
        label: item.value,
    }))
    const history = useHistory()
    return (
        <div className=''>
            <div className="col-12 grid-margin">
                <div className="card">
                    <div className="card-body">
                    <div onClick={() => history.goBack()} className='mb-2 hover-pointer' style={{ color: 'red' }}><i class="fa-solid fa-arrow-left mr-2"></i>Quay lại</div>

                        <h4 className="card-title">Thông tin chi tiết ứng viên</h4>
                        <br></br>
                        <form className="form-sample">

                            <div className="row">
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Lĩnh vực</label>
                                        <div className="col-sm-9 mt-3">
                                        <Select
                                                allowClear
                                                style={{
                                                    width: '100%',
                                                }}
                                                placeholder="Chọn lĩnh vực"
                                                disabled
                                                options={dataJobTypeF}
                                                value={inputValues.jobType}
                                            >
                                            </Select>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Mức lương</label>
                                        <div className="col-sm-9 mt-3">
                                        <Select
                                                allowClear
                                                style={{
                                                    width: '100%',
                                                }}
                                                placeholder="Chọn mức lương"
                                                disabled
                                                options={dataSalaryTypeF}
                                                value={inputValues.salary}
                                            >
                                            </Select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className='row'>
                                <div className='col-md-12'>
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Kĩ năng</label>
                                        <div className="col-sm-9 mt-3" style={{ marginLeft: '-115px' }}>
                                            <Select
                                                disabled
                                                mode="multiple"
                                                allowClear
                                                style={{
                                                    width: 'calc(100% + 115px)',
                                                }}
                                                placeholder="Chọn kĩ năng của bạn"
                                                options={listSkills}
                                                value={inputValues.skills}
                                                
                                            >
                                            </Select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Khu vực làm việc</label>
                                        <div className="col-sm-9 mt-3">
                                            <Select
                                                allowClear
                                                style={{
                                                    width: '100%',
                                                }}
                                                placeholder="Chọn nơi làm việc"
                                                disabled
                                                options={dataJobLocationF}
                                                value={inputValues.jobProvince}
                                            >
                                            </Select>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-6">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Kinh nghiệm làm việc</label>
                                        <div className="col-sm-9 mt-3">
                                        <Select
                                                allowClear
                                                style={{
                                                    width: '100%',
                                                }}
                                                placeholder="Chọn khoảng kinh nghiệm"
                                                disabled
                                                options={dataExpTypeF}
                                                value={inputValues.exp}
                                            >
                                            </Select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            {
                                inputValues.file &&
                                <div className="col-md-12">
                                    <div className="form-group row">
                                        
                                        <iframe width={'100%'} height={'700px'} src={inputValues.file}></iframe>
                                    </div>
                                </div>
                            }
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default DetailFilterUser
