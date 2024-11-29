import React from "react";
import { useEffect, useState } from "react";
import { useHistory, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import moment from "moment";
import SendCV from "../../components/CV/SendCV";
import axios from "axios";
import CommonUtils from "../utils/CommonUtils";
import { getUsersByCompanyId, getIdCompany } from "../../apiCall/user"
import { sendUserNotification  } from "../../apiCall/message"
import './JobDetail.css';
const JobDetail = () => {
  const history = useHistory();
  const { id } = useParams();
  const [isActiveModal, setAcitveModal] = useState(false);
  const [dataPost, setDataPost] = useState({});
  const [phoneNumbers, setPhoneNumbers] = useState([]);
  const [companyId, setCompanyId] = useState(null);
  
  useEffect(() => {
    const fetchData = async () => {
      if (id) {
        try {
          const response = await axios.get("http://localhost:8080/app-tuyen-dung/api/v1/post/get-detail-post-by-id", {
            params: { id },
          });
          console.log("Fetched data:", response.data); 
          setDataPost(response.data);
          if (response.data?.data?.phoneCompanyValue) {
            const idResponse = await getIdCompany(response.data.data.phoneCompanyValue);
            console.log(idResponse)
            if (idResponse?.result) {
              setCompanyId(idResponse.result);  

              const phones = await getUsersByCompanyId(idResponse.result);
              const phonesList = phones?.result || []
              setPhoneNumbers(phones?.result || []);
              console.log(phonesList)
            }
          }
        } catch (error) {
          console.error("Error fetching feature data", error);
        }
      }
    };
  
    fetchData();
  }, [id]);
  const handleSendNotification = async () => {
    try {
      if (phoneNumbers.length === 0) {
        console.error("Không có số điện thoại người dùng để gửi thông báo!");
        return;
      }
  
      const payload = {
        subject: `Ứng viên mới ứng tuyển vị trí ${dataPost.data?.categoryJobLevelCodeValue}`,
        image: dataPost.data?.thumbnailCompanyValue,
        message: `Kính gửi Quý công ty,\n\nChúng tôi xin thông báo rằng một ứng viên đã nộp hồ sơ ứng tuyển cho vị trí ${dataPost.data?.categoryJobLevelCodeValue}. Vui lòng kiểm tra CV của ứng viên và đưa ra quyết định tuyển dụng.\n\nTrân trọng,`,
        attachedUrl: "/admin/application-list/new",
        sender: "ADMIN",
        userId: phoneNumbers,
      };
  
      const response = await sendUserNotification(payload);
      console.log("API response:", response); 
    } catch (error) {
      console.error("Error sending notification:", error);
    }
  };
  // Kiểm tra thời gian hết hạn
  const isDeadlinePassed = moment().isAfter(moment.unix(dataPost.data?.timeEndValue / 1000));

  const handleOpenModal =  () => {
    if (dataPost.data?.timeEndValue && CommonUtils.formatDate(dataPost.data?.timeEndValue) > 0) {
      const userData = JSON.parse(localStorage.getItem("userData"));
      if (userData) {
        setAcitveModal(true);
      }
      else {
        toast.error("Vui lòng đăng nhập trước để có thể thực hiện nộp CV");
        setTimeout(() => {
          localStorage.setItem("lastUrl", window.location.href);
          history.push("/login");
        }, 1000);
      }
    } else toast.error("Hạn ứng tuyển đã hết");
  };
  return (
    <>
      <main>
        <div className="slider-area ">
          <div
            className="single-slider slider-height2 d-flex align-items-center"
            style={{
              backgroundImage: `url(${dataPost.data?.coverImageCompanyValue})`,
            }}
          >
            <div className="container">
              <div className="row">
                <div className="col-xl-12">
                  <div className="hero-cap text-center"></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="job-post-company pt-120 pb-120">
          <div className="container">
            <div className="row justify-content-between">
              <div className="col-xl-7 col-lg-8">
                <div className="single-job-items mb-30">
                  <div className="job-items">
                    <div className="company-img company-img-details">
                      <img
                        src={dataPost.data?.thumbnailCompanyValue}
                        alt="Ảnh bị lỗi"
                        width={100}
                        height={100}
                      />
                    </div>
                    <div className="job-tittle">
                      <h4>{dataPost.data?.name}</h4>

                      <ul>
                        <li>{dataPost.data?.categoryWorktypeCodeValue}</li>
                        <li>
                          <i className="fas fa-map-marker-alt"></i>
                          {dataPost.data?.adressCodeValue}
                        </li>
                        <li>
                          <i class="fas fa-money-bill-wave"></i>
                          {dataPost.data?.salaryCodeValue}
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>

                <div className="job-post-details">
                  <div className="post-details1 mb-50">
                    <div className="small-section-tittle">
                      <h4>Mô tả công việc</h4>
                    </div>
                  </div>
                  <div
                    dangerouslySetInnerHTML={{
                      __html: dataPost.data?.descriptionHTMLValue,
                    }}
                  />
                </div>
              </div>

              <div className="col-xl-4 col-lg-4">
                <div className="post-details3  mb-50">
                  <div className="small-section-tittle">
                    <h4>Thông tin công việc</h4>
                  </div>
                  <ul>
                    <li>
                      Lĩnh vực :{" "}
                      <span>{dataPost.data?.categoryWorktypeCodeValue}</span>
                    </li>
                    <li>
                      Nơi làm việc :{" "}
                      <span>{dataPost.data?.addressCompanyValue}</span>
                    </li>
                    <li>
                      Hình thức làm việc :{" "}
                      <span>{dataPost.data?.categoryWorktypeCodeValue}</span>
                    </li>
                    <li>
                      Kinh nghiệm:{" "}
                      <span>{dataPost.data?.experienceJobCodeValue}</span>
                    </li>
                    <li>
                      Lương : <span>{dataPost.data?.salaryCodeValue}</span>
                    </li>
                    <li>
                      Hạn nộp :{" "}
                      <span>
                        {moment
                          .unix(dataPost.data?.timeEndValue / 1000)
                          .format("DD/MM/YYYY")}
                      </span>
                    </li>
                  </ul>
                  {isDeadlinePassed ? (
                  <div className="disabled-button">
                    Đã hết thời gian ứng tuyển
                  </div>
                ) : (
                  <div className="btn" onClick={handleOpenModal}>
                    Ứng tuyển ngay
                  </div>
                )}
                </div>
                <div
                  className="post-details4 mb-50 "
                  style={{ border: "0.1rem solid", padding: "20px" }}
                >
                  <div className="small-section-tittle">
                    <h4 style={{ textAlign: "center" }}>Thông tin công ty</h4>
                  </div>
                  <span>Tên công ty : {dataPost.data?.nameCompanyValue}</span>
                  <ul>
                    <li>
                      Website :{" "}
                      <span>{dataPost.data?.websiteCompanyValue}</span>
                    </li>
                    <li>
                      Địa chỉ :{" "}
                      <span>{dataPost.data?.addressCompanyValue}</span>
                    </li>
                    <li>
                      Điện thoại :{" "}
                      <span>{dataPost.data?.phoneCompanyValue}</span>
                    </li>
                    <li>
                      Mã số thuế : <span>{dataPost.data?.taxNumberValue}</span>
                    </li>
                    <li>
                      Số nhân viên:{" "}
                      <span>{dataPost.data?.employerCompanyValue}</span>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* <!-- job post company End --> */}
      {
        isActiveModal && <SendCV
          isOpen={isActiveModal}
          onHide={() => setAcitveModal(false)}
          postId={id}
          emailCompany={dataPost.data?.emailCompany}  
          jobTitle={dataPost.data?.name} 
          handleSendNotification={handleSendNotification}
        />
      }
      </main>
    </>
  );
};

export default JobDetail;
