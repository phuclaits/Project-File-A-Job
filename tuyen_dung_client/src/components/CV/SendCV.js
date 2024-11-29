import '../modal/modal.css'
import React, { useEffect, useState } from 'react';
import CommonUtils from '../../container/utils/CommonUtils';

import { toast } from 'react-toastify';
import { Modal, ModalHeader, ModalFooter, ModalBody, Button, Spinner, Input } from 'reactstrap';
import { getDetailUserById} from '../../service/userService'
import axios from 'axios';
import LoadingPage from '../Loading';
function SendCV(props){
    const userData = JSON.parse(localStorage.getItem('userData'));
    const [isLoading, setIsLoading] = useState(false)
    const [inputValue, setInputValue] = useState({
        userId: '', postId: '', file: '', linkFileUser: '', fileUser: '', linkFile: '',description: '',userEmail: '',companyEmail: '',jobTitle: '',userName: ''
    })

    const [typeCv,setTypeCv] = useState('pcCv')
    useEffect(() => {
        console.log(props)
        if (userData)
        getFileCv(userData.id)
    }, [props.postId, userData.id])

    let getFileCv= async(id) => {
        let res = await getDetailUserById(id)
        setInputValue({
            ...inputValue,
            ["jobTitle"]: props.jobTitle,
            ["companyEmail"]: props.emailCompany,
            ["userName"]: res.result.userAccountData.firstName + ' ' + res.result.userAccountData.lastName,
            ["userEmail"]: res.result.userAccountData.email,
            ["userId"]: id,
            ["postId"]: props.postId,
            ['linkFileUser']: res.result.userAccountData.userSettingData.file ? URL.createObjectURL(dataURLtoFile(res.result.userAccountData.userSettingData.file,'yourCV')) : '',
            ['fileUser'] : res.result.userAccountData.userSettingData.file ? res.result.userAccountData.userSettingData.file : ''
        })
        
    }
    console.log(inputValue)
    // console.log(inputValue)
    const handleChange = (event) => {
        const { name, value } = event.target
        setInputValue({
            ...inputValue,
            [name]: value
        })
    }

    const radioOnChange = (e) => {
        const {value} = e.target
        if (value==='userCv' && !inputValue.linkFileUser) {
            toast.error('Hiện chưa đăng CV online cho chúng tôi')
        }
        else {
            setTypeCv(value)
        }
    }
    let dataURLtoFile = (dataurl, filename) => {
 
        var arr = dataurl.split(','),
            mime = arr[0].match(/:(.*?);/)[1],
            bstr = atob(arr[1]), 
            n = bstr.length, 
            u8arr = new Uint8Array(n);
            
        while(n--){
            u8arr[n] = bstr.charCodeAt(n);
        }
        
        return new File([u8arr], filename, {type:mime});
    }

    const handleOnChangeFile = async (event) => {
        let data = event.target.files;
        let file = data[0];
        if (file) {
            if (file.size > 2097152)
            {
                toast.error("File của bạn quá lớn. Chỉ gửi file dưới 2MB")
                return
            }
            let base64 = await CommonUtils.getBase64(file);
            setInputValue({
                ...inputValue,
                ["file"]: base64,
                ["linkFile"]: URL.createObjectURL(file)
            })
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

    const handleSendCV = async () => {
        setIsLoading(true)
        let cvSend = ''
        if (typeCv === 'userCv') {
            cvSend = inputValue.fileUser
        }
        else {
            cvSend = inputValue.file
        }

        const token = localStorage.getItem("token_user");
        let formData = new FormData();
        formData.append('userId', inputValue.userId);
        formData.append('filePDF', cvSend);
        formData.append('postId', inputValue.postId);
        formData.append('description', inputValue.description);
        if(!cvSend){
            toast.error("File CV không bỏ trống");
            setIsLoading(false);
            return 
        }
        if (cvSend) {
            const base64Data =cvSend.split(',')[1];  
            const blob = base64ToBlob(base64Data, 'application/pdf');
            formData.append('filePDF', blob, 'file.pdf');  
        }
        try{
            const res = await axios.post("http://localhost:8080/app-tuyen-dung/api/v1/cv/createCVnew", formData, {
                headers: {
                    Authorization: `Bearer ${token}`
                },
            });
            if (res.data.errCode === 0) {
                const emailPayload = {
                    userEmail: inputValue.userEmail,  
                    companyEmail: inputValue.companyEmail, 
                    jobTitle: inputValue.jobTitle,  
                    userName: inputValue.userName  
                };
                // Gửi email sau khi nộp CV thành công
            const emailRes = await axios.post("http://localhost:8080/app-tuyen-dung/api/v1/email/send-mail/send-application-email", emailPayload, {
                headers: {
                    Authorization: `Bearer ${token}`
                },
            });
            if(emailRes.data.code === 200)
            {
                setTimeout(() => {
                    setIsLoading(false);
                    setInputValue({
                        ...inputValue,
                        file: '',
                        description: '',
                        linkFile: ''
                    });
                    toast.success("Đã nộp CV và gửi email thành công!");
                    props.onHide();
                }, 1000);
                props.handleSendNotification();
            }
        } else {
            toast.error("Gửi CV thất bại");
        }
    } catch (error) {
        console.log(error)
        setIsLoading(false)
        toast.error("Gửi CV thất bại")    
    }
}
    return (
        <div>
            <Modal isOpen={props.isOpen} className={'booking-modal-container'}
                size="md" centered
            >
                <p className='text-center' style={{color:'red'}}>NỘP CV CỦA BẠN CHO NHÀ TUYỂN DỤNG</p>
                <ModalBody>
                    Nhập lời giới thiệu gửi đến nhà tuyển dụng
                    <div>
                    <textarea placeholder='Giới thiệu hoặc để lại đoạn nhỏ mô tả về bản thân đến nhà tuyển dụng' 
                    name='description' className='mt-2' style={{ width: "100%" }} rows='5' onChange={(event) => handleChange(event)}></textarea>
                    <div className='d-flex' style={{justifyContent:'space-between'}}>
                        <div>
                        <input onChange={radioOnChange} type="radio" checked={typeCv === 'pcCv'} value="pcCv" name="typeCV"></input>
                        <label className='ml-2'>Tự chọn CV</label>
                        </div>
                        <div>
                        <input onChange={radioOnChange} type="radio" checked={typeCv === 'userCv'} value="userCv" name="typeCV"></input>
                        <label className='ml-2'>CV online</label>
                        </div>
                    </div>
                    {
                        typeCv === 'pcCv' &&
                        <input type="file" className='mt-2' accept='.pdf'
                        onChange={(event) => handleOnChangeFile(event)}></input>
                    }
                    {
                        typeCv === 'pcCv' && inputValue.linkFile && <div><a href={inputValue.linkFile} style={{ color: 'blue' }} target='_blank'>Nhấn vào đây để xem lại CV của bạn </a></div>
                    }
                                        {
                        typeCv === 'userCv' && inputValue.linkFileUser && <div><a href={inputValue.linkFileUser} style={{ color: 'blue' }} target='_blank'>Nhấn vào đây để xem lại CV của bạn </a></div>
                    }
                    </div>
                </ModalBody>
                <ModalFooter style={{ justifyContent: 'space-between' }}>
                    <Button className='me-5' onClick={() => handleSendCV()}>
                        Gửi hồ sơ
                    </Button>

                    <Button onClick={() => {
                        setInputValue({
                            ...inputValue,
                            ["file"]: '', ["description"]: '', ["linkFile"] : ''
                        })
                        props.onHide()
                    }}>
                        Hủy
                    </Button>
                </ModalFooter>

                {isLoading &&
                    <LoadingPage />
                    
                }
            </Modal>
        </div>
    );


}
export default SendCV;
