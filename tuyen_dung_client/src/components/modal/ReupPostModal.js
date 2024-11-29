import React, { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { Modal, ModalHeader, ModalFooter, ModalBody, Button, Spinner } from 'reactstrap';
import {banPostService} from '../../service/userService'
import DatePicker from '../input/DatePicker';
import './modal.css'
import LoadingPage from '../Loading';
function ReupPostModal(props) {
    const [isLoading, setIsLoading] = useState(false)
    const [inputValue, setInputValue] = useState({
        timeEnd: '',
    })
    let handleOnChangeDatePicker = (date) => {
        setInputValue({
            ...inputValue,
            timeEnd: new Date(date[0]).getTime()
        })
    }
    const handlePost = () => {
        setIsLoading(true)
        props.handleFunc(inputValue.timeEnd)
        setIsLoading(false)
        props.onHide()
    }
    return (
        <div>
            <Modal isOpen={props.isOpen} className={'booking-modal-container'}
                size="md" centered
            >
                <p className='text-center'>Hãy chọn thời gian kết thúc tuyển dụng</p>
                <ModalBody>
                    Chọn thời gian kết thúc
                    <div>
                    <DatePicker className="form-control" onChange={handleOnChangeDatePicker}
                                                    value={inputValue.timeEnd}
                                                />
                    </div>
                </ModalBody>
                <ModalFooter style={{ justifyContent: 'space-between' }}>
                    <Button className='me-5' onClick={() => handlePost()}>
                        Hoàn thành
                    </Button>

                    <Button onClick={() => {
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

export default ReupPostModal;