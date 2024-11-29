import React from 'react'
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import Lightbox from 'react-image-lightbox';
import 'react-image-lightbox/style.css';
import CommonUtils from '../../utils/CommonUtils';
import { createCompanyService, getDetailCompanyByUserId, updateCompanyService } from '../../../service/userService';
import MarkdownIt from 'markdown-it'; // markdown-it
import MdEditor from 'react-markdown-editor-lite';
import 'react-markdown-editor-lite/lib/index.css';
import { Spinner, Modal } from 'reactstrap'
import '../../../components/modal/modal.css'
import { useHistory, useParams } from 'react-router-dom';
import axios from 'axios';
import LoadingPage from '../../../components/Loading';
const AddCompany = () => {
    const { id } = useParams()
    const mdParser = new MarkdownIt();
    const [user, setUser] = useState({})
    const [isLoading, setIsLoading] = useState(false)
    const [inputValues, setInputValues] = useState({
        image: '', coverImage: '', imageReview: '', coverImageReview: '', isOpen: false, name: '', phonenumber: '', address: '', website: '',
        amountEmployer: '', taxnumber: '', descriptionHTML: '', descriptionMarkdown: '', isActionADD: true, id: '', file: '', imageClick: '',
        isFileChange: false
    });
    
    
    useEffect(() => {
        const userData = JSON.parse(localStorage.getItem('userData'));
        if (userData && userData.codeRoleAccount !== "ADMIN") {
            fetchCompany(userData.id)
        }
        else if (id && userData.codeRoleAccount === 'ADMIN') {
            fetchCompany(null, id)
        }
        console.log(inputValues)
        let token = localStorage.getItem("token_user")
        setUser(userData)
    }, [])
    let fetchCompany = async (userId, companyId = null) => {
        let res = await getDetailCompanyByUserId(userId, companyId)
        if (res && res.errCode === 0) {
            const decodedData = atob(res.data.file);
            //import cái này vào tôi vưới atob
            setInputValues({
                ...inputValues,
                ["name"]: res.data.name,
                ["phonenumber"]: res.data.phonenumber,
                ["address"]: res.data.address,
                ["image"]: res.data.thumbnail,
                ["coverImage"]: res.data.coverImage,
                ["descriptionHTML"]: res.data.descriptionHTML,
                ["descriptionMarkdown"]: res.data.descriptionMarkdown,
                ["amountEmployer"]: res.data.amountEmployer,
                ["taxnumber"]: res.data.taxnumber,
                ["website"]: res.data.website,
                ["imageReview"]: res.data.thumbnail,
                ["coverImageReview"]: res.data.coverImage,
                ["isActionADD"]: false,
                ["id"]: res.data.id,
                ["file"]: res.data.file
            }) 
            setFileBase64(decodedData)
            
        } 
    }
    const handleOnChange = event => {
        const { name, value } = event.target;
        setInputValues({ ...inputValues, [name]: value });

    };

    const handleOnChangeImage = async  (event) => {
        let data = event.target.files;
        let file = data[0];
        if (file) {
            let fileURL = URL.createObjectURL(file); // path 
            // console.log(fileURL)
            console.log(file)
            setInputValues(prevState => ({
                ...prevState,
                image: file, 
                imageReview: fileURL 
            }));
        }
        
    };
    
    const handleOnChangeCoverImage = async (event) => {
        let data = event.target.files;
        let file = data[0];
        if (file) {
            let fileURL = URL.createObjectURL(file); // path 
            setInputValues(prevState => ({
                ...prevState, 
                coverImage: file, 
                coverImageReview: fileURL 
            }));
        }
    };
    console.log(inputValues.file);
    const [fileBase64, setFileBase64] = useState(null);
    
    let handleOnChangeFile = async (event) => {
        let data = event.target.files;
        
        let file = data[0];
        
        if (file) {
            if (file.size > 2097152) {
                toast.error("Hãy gửi file dưới 2MB!")
                return
            }
            let base64 = await CommonUtils.getBase64(file);
            setFileBase64(base64);
            setInputValues({ ...inputValues, file: base64, isFileChange: true })
            
        }
    }
    function base64ToBlob(base64Data, contentType) {
        const byteCharacters = atob(base64Data);
        const byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        return new Blob([byteArray], { type: contentType });
    }
    let openPreviewImage = (event) => {
        const name = event.target.getAttribute('name')
        if (!inputValues.imageReview && !inputValues.coverImageReview) return;
        setInputValues({ ...inputValues, imageClick: name === 'cover' ? inputValues.coverImageReview : inputValues.imageReview, ["isOpen"]: true })
    }
    let handleSaveCompany = async () => {
        setIsLoading(true)
        if (inputValues.isActionADD === true) {
            let formData = new FormData();
            formData.append("name", inputValues.name);
            formData.append("descriptionHTML", inputValues.descriptionHTML);
            formData.append("descriptionMarkdown", inputValues.descriptionMarkdown);
            formData.append("website", inputValues.website);
            formData.append("address", inputValues.address);
            formData.append("phonenumber", inputValues.phonenumber);
            formData.append("amountEmployer", inputValues.amountEmployer);
            formData.append("taxnumber", inputValues.taxnumber);
            formData.append("file", inputValues.file);
            formData.append("filethumb", inputValues.image);
            formData.append("fileCover", inputValues.coverImage);
            const token = localStorage.getItem("token_user");
            axios
            .post(
              "http://localhost:8080/app-tuyen-dung/api/v1/company/create-company",
              formData,
              {
                headers: {
                  Authorization: `Bearer ${token}`,
                  'Content-Type': 'multipart/form-data'
                },
              }
            ).then(res=>{
                setTimeout(() => {
                    setIsLoading(false)
                    console.log(res)
                    if (res.data && res.data.errCode === 0) {
                        toast.success("Tạo mới công ty thành công")
                        fetchCompany(user.id)
                        let userData = JSON.parse(localStorage.getItem("userData"))
                        let newUser = { ...userData, codeRoleAccount: "COMPANY", idCompany: res.companyId }
                        localStorage.setItem("userData", JSON.stringify(newUser))
                        setTimeout(() => {
                            window.location.reload()
                        }, 1000)
                    } else {
                        toast.error(res.errMessage)
                    }
                }, 1000);
            })


            
        } else {
            console.log(inputValues.file);
            // Xử lý cập nhật công ty
        let formData = new FormData();
        formData.append("name", inputValues.name);
        formData.append("phonenumber", inputValues.phonenumber);
        formData.append("address", inputValues.address);
        formData.append("descriptionHTML", inputValues.descriptionHTML);
        formData.append("descriptionMarkdown", inputValues.descriptionMarkdown);
        formData.append("amountEmployer", inputValues.amountEmployer);
        formData.append("taxnumber", inputValues.taxnumber);
        formData.append("website", inputValues.website);
        formData.append("id", inputValues.id); // ID của công ty cần cập nhật
        if (inputValues.image != null) {
            formData.append("thumbnailImage", inputValues.image); // Ảnh thumbnail mới
        }
        if (inputValues.coverImage!=null) {
            formData.append("coverimagefile", inputValues.coverImage); // Ảnh cover mới
        }

        try{
            const base64Data =(inputValues.file).split(',')[1];  
            const blob = base64ToBlob(base64Data, 'application/pdf');
            formData.append('filePDF', blob, 'file.pdf');  
        }catch(e){
            console.log(e)
        }

        const token = localStorage.getItem("token_user");
        try {
            const res = await axios.put(
                "http://localhost:8080/app-tuyen-dung/api/v1/company/update-company",
                formData,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'multipart/form-data',
                    },
                }
            );

            setIsLoading(false);
            if (res.data && res.data.errCode === 0) {
                toast.success(res.data.errMessage);
                setTimeout(() => {
                    window.location.reload()
                }, 2000)
            } else {
                toast.error(res.data.errMessage);
            }
        } catch (error) {
            setIsLoading(false);
            toast.error("Có lỗi xảy ra khi cập nhật công ty!");
            console.error(error);
        }

        }
    }
    const isDisabled = user?.codeRoleAccount === 'EMPLOYER' && user?.idCompany;
    let handleEditorChange = ({ html, text }) => {
        setInputValues({
            ...inputValues,
            ["descriptionMarkdown"]: text,
            ["descriptionHTML"]: html
        })
    }
    const history = useHistory()
    return (
        <>
            <div className=''>
                <div className="col-12 grid-margin">
                    <div className="card">
                        <div className="card-body">
                            <div onClick={() => history.goBack()} className='mb-2 hover-pointer' style={{ color: 'red' }}><i class="fa-solid fa-arrow-left mr-2"></i>Quay lại</div>
                            <h4 className="card-title">{inputValues.isActionADD === true ? 'Thêm mới công ty' : (user?.codeRoleAccount === 'ADMIN' ? 'Xem thông tin công ty' : 'Cập nhật công ty')}</h4>
                            <br></br>
                            <form className="form-sample">

                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Tên</label>
                                            <div className="col-sm-9">
                                                <input disabled={user?.codeRoleAccount === "ADMIN"||isDisabled ? true : false} value={inputValues.name} name="name" onChange={(event) => handleOnChange(event)} type="text" className="form-control" />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Số điện thoại</label>
                                            <div className="col-sm-9">
                                                <input disabled={user?.codeRoleAccount === "ADMIN" || isDisabled? true : false} value={inputValues.phonenumber} name="phonenumber" onChange={(event) => handleOnChange(event)} type="number" className="form-control" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Mã số thuế</label>
                                            <div className="col-sm-9">
                                                <input disabled={user?.codeRoleAccount === "ADMIN"||isDisabled ? true : false} value={inputValues.taxnumber} name="taxnumber" onChange={(event) => handleOnChange(event)} type="text" className="form-control" />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Số nhân viên</label>
                                            <div className="col-sm-9">
                                                <input disabled={user?.codeRoleAccount === "ADMIN"||isDisabled ? true : false} value={inputValues.amountEmployer} name="amountEmployer" onChange={(event) => handleOnChange(event)} type="number" className="form-control" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Địa chỉ</label>
                                            <div className="col-sm-9">
                                                <input disabled={user?.codeRoleAccount === "ADMIN"||isDisabled ? true : false} value={inputValues.address} name="address" onChange={(event) => handleOnChange(event)} type="text" className="form-control" />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Link website</label>
                                            <div className="col-sm-9">
                                                <input disabled={user?.codeRoleAccount === "ADMIN"||isDisabled ? true : false} value={inputValues.website} name="website" onChange={(event) => handleOnChange(event)} type="text" className="form-control" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Ảnh đại diện</label>
                                            <div className="col-sm-9">
                                                <input disabled={user?.codeRoleAccount === "ADMIN"||isDisabled ? true : false} name='image' onChange={(event) => handleOnChangeImage(event) } accept='image/*' type="file" className="form-control form-file" />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Hiển thị</label>
                                            <div className="col-sm-9">
                                                <div name='review' style={{ backgroundImage: `url(${inputValues.imageReview})` }} onClick={(event) => openPreviewImage(event)} className="box-img-preview"><img/></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Ảnh bìa</label>
                                            <div className="col-sm-9">
                                                <input disabled={user?.codeRoleAccount === "ADMIN"||isDisabled ? true : false} name='coverImage' onChange={(event) => handleOnChangeCoverImage(event)} accept='image/*' type="file" className="form-control form-file" />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Hiển thị</label>
                                            <div className="col-sm-9">
                                                <div name='cover' style={{ backgroundImage: `url(${inputValues.coverImageReview})` }} onClick={(event) => openPreviewImage(event)} className="box-img-preview"><img/></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="form-group row">
                                            <label className="col-sm-3 col-form-label">Hồ sơ chứng nhận</label>
                                            <div className="col-sm-9">
                                                <input disabled={user?.codeRoleAccount === "ADMIN"||isDisabled  ? true : false} name='coverImage' onChange={(event) => handleOnChangeFile(event)} accept='.pdf' type="file" className="form-control form-file" />
                                            </div>
                                        </div>
                                    </div>
                                    {
                                        inputValues.file &&
                                        <div className="col-md-12">
                                            <div className="form-group row">
                                                <label className="col-sm-3 col-form-label">Hiển thị</label>
                                                <iframe width={'100%'} height={'700px'} src={fileBase64} title='PDF'></iframe>
                                            </div>
                                        </div>
                                    }
                                </div>
                                <div className="row">
                                    <div className="col-md-12">
                                        <label className="form-label">Giới thiệu công ty</label>
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
    user.codeRoleAccount === "COMPANY" || (user.codeRoleAccount === "EMPLOYER" && user.idCompany === null) ? (
        <button 
            onClick={() => handleSaveCompany()} 
            type="button" 
            className="btn1 btn1-primary1 btn1-icon-text"
        >
            <i className="ti-file btn1-icon-prepend"></i>
            Lưu
        </button>
    ) : (
        <div className="no-permission-text">Không có quyền cập nhật</div>
    )
}
                            </form>
                        </div>
                    </div>
                </div>
                {
                    inputValues.isOpen === true &&
                    <Lightbox mainSrc={inputValues.imageClick}
                        onCloseRequest={() => setInputValues({ ...inputValues, ["isOpen"]: false })}
                    />
                }
            </div>
            {isLoading &&
                    <LoadingPage />
                    
                }
        </>
    )
}

export default AddCompany
