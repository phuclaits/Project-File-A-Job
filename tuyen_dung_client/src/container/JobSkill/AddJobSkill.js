import React from 'react'
import { Col, Row, Select } from 'antd';
import { useEffect, useState } from 'react';
import { createSkill, getDetailSkillById, UpdateSkill } from '../../service/userService';
import { useFetchDataJobType } from '../utils/fetch';
import { toast } from 'react-toastify';
import { useHistory, useParams } from "react-router-dom";
import 'react-image-lightbox/style.css';
import { Spinner, Modal } from 'reactstrap'
import '../../components/modal/modal.css'
import './AddJobSkill.scss';
import LoadingPage from '../../components/Loading';

const AddJobSkill = () => {
    const [isActionADD, setisActionADD] = useState(true)

    const [isLoading, setIsLoading] = useState(false)

    const { code } = useParams();

    const [inputValues, setInputValues] = useState({
        name: '', categoryJobCode: '', id: ''
    });

    let fetchDetailJobType = async (code) => {
        setisActionADD(false)
        let skill = await getDetailSkillById(code)
        if (skill && skill.errCode === 0) {
            
            setInputValues({ ...inputValues, ["name"]: skill.data.name, ["id"]: skill.data.id, ["categoryJobCode"]: skill.data.categoryJobCode  })
        }
    }
    console.log(inputValues)
    let { dataJobType: listCategoryJobCode } = useFetchDataJobType();
    listCategoryJobCode = listCategoryJobCode.map(item=> ({
        value: item.code,
        label: item.value
    }))

    useEffect(() => {

        if (code) {
            fetchDetailJobType(code)
        }
    }, [])

    const handleOnChange = event => {
        const { name, value } = event.target;
        setInputValues({ ...inputValues, [name]: value });
    };

    let handleOnChangeCategoryJobCode = async(value) => {
        setInputValues({
            ...inputValues,
            categoryJobCode: value
        })
    }

    let handleSaveJobSkill = async () => {
        setIsLoading(true)
        if (isActionADD === true) {
            let res = await createSkill({
                name: inputValues.name,
                categoryJobCode: inputValues.categoryJobCode
            })
            setTimeout(() => {
                setIsLoading(false)
                if (res && res.errCode === 0) {
                    toast.success("Thêm kỹ năng thành công")
                    setInputValues({
                        ...inputValues,
                        ["name"]: '',
                        ["categoryJobCode"]: '',
                    })
                }
                else if (res && res.errCode === 2) {
                    toast.error(res.errMessage)
                }
                else toast.error("Thêm kĩ năng thất bại")
            }, 50);
        } else {
            let res = await UpdateSkill({
                name: inputValues.name,
                id: code,
                categoryJobCode: inputValues.categoryJobCode
            })
            setTimeout(() => {
                setIsLoading(false)
                if (res && res.errCode === 0) {
                    toast.success("Cập nhật loại công việc thành công")

                }
                else if (res && res.errCode === 2) {
                    toast.error(res.errMessage)
                }
                else toast.error("Cập nhật loại công việc thất bại")
            }, 50);
        }
    }
    const history = useHistory()
    return (
        <div className=''>
            <div className="col-12 grid-margin">
                <div className="card">
                    <div className="card-body">
                        <div onClick={() => history.goBack()} className='mb-2 hover-pointer' style={{ color: 'red' }}><i class="fa-solid fa-arrow-left mr-2"></i>Quay lại</div>

                        <h4 className="card-title">{isActionADD === true ? 'Thêm mới loại công việc' : 'Cập nhật loại công việc'}</h4>
                        <br></br>
                        <form className="form-sample">
                            <div className="row">
                                <div className="col-md-8">
                                    <div className="form-group row">
                                        <label className="col-sm-3 col-form-label">Tên kĩ năng</label>
                                        <div className="col-sm-8">
                                            <input type="text" value={inputValues.name} name="name" onChange={(event) => handleOnChange(event)} className="form-control" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <Row>
                            <Col xs={4} xxl={4}>
                                <label className='mr-2'>Lĩnh vực</label>
                            </Col>
                            <Col xs={12} xxl={12}>
                                <Select style={{width:'86%'}} onChange={(value)=> handleOnChangeCategoryJobCode(value)} value={inputValues.categoryJobCode}  size='default' options={listCategoryJobCode ? listCategoryJobCode : []}>
                                </Select>
                            </Col>
                            </Row>


                            <button type="button" className="btn1 btn1-primary1 btn1-icon-text" onClick={() => handleSaveJobSkill()}>
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

export default AddJobSkill
