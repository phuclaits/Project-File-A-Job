import React, { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import {executePaymentCV} from '../../../service/userService'
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link,
    Redirect,
    useParams,
    useLocation,
    useHistory
} from "react-router-dom";


function useQuery() {
    const { search } = useLocation();
    
    return React.useMemo(() => new URLSearchParams(search), [search]);
  }

function PaymentSuccessCv(props) {
    let query = useQuery();
    const [message, setMessage] = useState("Đang xử lý")
    useEffect(() => {
        let orderData =  JSON.parse(localStorage.getItem("orderCvData"))
        if(orderData){
          const paymentId = query.get("paymentId");
          const token = query.get("token");
          const PayerID = query.get("PayerID");
      
          console.log("paymentId:", paymentId);
          console.log("token:", token);
          console.log("PayerID:", PayerID);
          if (paymentId && token && PayerID) {
            orderData.paymentId = paymentId;
            orderData.token = token;
            orderData.payerID = PayerID;
            createNewOrder(orderData);
          }
        }
        else {
            setMessage("Thông tin đơn hàng không hợp lệ")
        }
    }, [])
    let createNewOrder = async (data) =>{
        let res = await executePaymentCV(data)
        if(res && res.errCode == 0){
            toast.success(res.errMessage)
            localStorage.removeItem("orderCvData")
            setMessage("Chúc mừng bạn đã mua lượt tìm ứng viên thành công")
        }else{
            toast.error(res.errMessage)
        }
    }
    const history = useHistory()
    return (

        <div style={{height:'50vh',textAlign:'center'}}> 
           {message}
           {message === 'Chúc mừng bạn đã mua lượt tìm ứng viên thành công' && <div className='mt-5'><button onClick={() => history.push("/admin/list-candiate") } style={{backgroundColor: "green"}}>Tìm ứng viên ngay</button></div>}
        </div>

    );
}

export default PaymentSuccessCv;
