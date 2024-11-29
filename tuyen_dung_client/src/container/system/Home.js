import React, { useEffect, useState } from 'react';
import { Pie, Bar } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend, Title, BarElement, CategoryScale, LinearScale } from "chart.js";
import ChartDataLabels from 'chartjs-plugin-datalabels';
import { getStaticCvByStatus, getStaticCvByStatusnCompany, getStaticPostByJobType, getSumOrderCv, getSumOrderPost, getQuantityCompany, getQuantityCandidate } from "../../apiCall/statical";

ChartJS.register(ArcElement, Tooltip, Title, Legend, BarElement, CategoryScale, LinearScale, ChartDataLabels);

const Home = () => {
  const [user, setUser] = useState({});
  const [cvStatusData, setCvStatusData] = useState([]);
  const [postByJobTypeData, setPostByJobTypeData] = useState([]);
  const [sumOrderCv, setSumOrderCv] = useState(0);
  const [sumOrderPost, setSumOrderPost] = useState(0);
  const [quantityCompany, setQuantityCompany] = useState(0);
  const [quantityCandidate, setQuantityCandidate] = useState(0);

  const dataChartBarPostByCate = {
    labels: ["Công nghệ thông tin", "Kinh tế", "Giáo viên", "Bất động sản", "Khác"],
    datasets: [
      {
        label: "Biểu đồ số lượng bài post",
        backgroundColor: ["#0d6efd", "#198754", "#ffc107", "#dc3545", "#6c757d"],
        data: postByJobTypeData,
        barPercentage: 0.8,
        categoryPercentage: 0.6,
      },
    ],
  };

  const optionsBarPostByCate = {
    responsive: true,
    scales: {
      x: {
        beginAtZero: true,
      },
      y: {
        beginAtZero: true,
      },
    },
    plugins: {
      datalabels: {
        color: '#fff',
        font: {
          weight: 'bold',
          size: 12, 
        },
        align: 'center', 
        anchor: 'center',   
        formatter: (value) => value,
      },
    },
  };

  const dataChartPieStaticCv = {
    labels: ["Pending", "Accept", "Review", "Reject"],
    datasets: [
      {
        data: cvStatusData,
        label: "Số lượng bài viết",
        backgroundColor: ["#0d6efd", "#198754", "#ffc107", "#dee2e6"],
        borderColor: "transparent",
      },
    ],
  };

  const optionsPieStaticCv = {
    cutout: "70%",
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top",
      },
      datalabels: {
        color: '#fff',
        formatter: (value, context) => {
          // Nếu giá trị là 0, không hiển thị label
          if (value === 0) {
            return '';
          }
          return value;
        },
        font: {
          weight: 'bold',
          size: 14,
        },
        align: 'center',
        anchor: 'center',
      },
    },
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch user data from localStorage
        const userData = JSON.parse(localStorage.getItem('userData'));
        setUser(userData);

        // Fetch API data
        let cvStatus;
        if(userData.codeRoleAccount == "ADMIN") {
            cvStatus = await getStaticCvByStatus();
        }
        else {
            cvStatus = await getStaticCvByStatusnCompany(userData.idCompany);
        }
        const postByJobType = await getStaticPostByJobType();
        const orderCv = await getSumOrderCv();
        const orderPost = await getSumOrderPost();
        const companyCount = await getQuantityCompany();
        const candidateCount = await getQuantityCandidate();
        const cvStatusData = [
            cvStatus.result.pending,
            cvStatus.result.accepted,
            cvStatus.result['under-review'],
            cvStatus.result.rejected,
          ];
          const postByJobTypeData = [
            postByJobType.result['cong-nghe-thong-tin'] || 0, // Công nghệ
            postByJobType.result['kinh-te'] || 0, // Kinh tế
            postByJobType.result['giao-vien'] || 0, // Giáo dục
            postByJobType.result['bat-dong-san'] || 0,
            (postByJobType.result['luat'] || 0) + 
            (postByJobType.result['quan-ly-nhan-su'] || 0) + 
            (postByJobType.result['truyen-thong'] || 0), // Khác (sum of other categories)
          ];
        setCvStatusData(cvStatusData);
        setPostByJobTypeData(postByJobTypeData);
        setSumOrderCv(orderCv.result);
        setSumOrderPost(orderPost.result);
        setQuantityCompany(companyCount.result);
        setQuantityCandidate(candidateCount.result);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  }, []);

  return (
    <>
      <div className="row">
        <div className="col-md-12 m-2">
          <div className="row">
            <div className="col-12 col-xl-8 mb-2 mb-xl-0">
              <h3 className="font-weight-bold">Xin chào {user.firstName + " " + user.lastName}</h3>
            </div>
          </div>
        </div>
      </div>

      <div className='row'>
        <div className="col-xl-3 col-md-6 mb-4">
          <div className="card border-left-primary shadow h-100 py-2">
            <div className="card-body">
              <div className="row no-gutters align-items-center">
                <div className="col mr-2">
                  <div className="text-xs font-weight-bold text-primary text-uppercase mb-1">
                    Ứng Viên (Số lượng)
                  </div>
                  <div className="h5 mb-0 font-weight-bold text-gray-800">{quantityCandidate}</div>
                </div>
                <div className="col-auto">
                  <i className="fa-solid fa-users fa-2x text-gray-300"></i>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="col-xl-3 col-md-6 mb-4">
          <div className="card border-left-primary shadow h-100 py-2">
            <div className="card-body">
              <div className="row no-gutters align-items-center">
                <div className="col mr-2">
                  <div className="text-xs font-weight-bold text-info text-uppercase mb-1">
                    Công ty (Số lượng)
                  </div>
                  <div className="h5 mb-0 font-weight-bold text-gray-800">{quantityCompany}</div>
                </div>
                <div className="col-auto">
                  <i className="fa-solid fa-building fa-2x text-gray-300"></i>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="col-xl-3 col-md-6 mb-4">
          <div className="card border-left-success shadow h-100 py-2">
            <div className="card-body">
              <div className="row no-gutters align-items-center">
                <div className="col mr-2">
                  <div className="text-xs font-weight-bold text-warning text-uppercase mb-1">
                    Gói Bài Đăng (Annual)
                  </div>
                  <div className="h5 mb-0 font-weight-bold text-gray-800">${sumOrderPost}</div>
                </div>
                <div className="col-auto">
                  <i className="fas fa-dollar-sign fa-2x text-gray-300"></i>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="col-xl-3 col-md-6 mb-4">
          <div className="card border-left-success shadow h-100 py-2">
            <div className="card-body">
              <div className="row no-gutters align-items-center">
                <div className="col mr-2">
                  <div className="text-xs font-weight-bold text-success text-uppercase mb-1">
                    Gói Cv (Annual)
                  </div>
                  <div className="h5 mb-0 font-weight-bold text-gray-800">${sumOrderCv}</div>
                </div>
                <div className="col-auto">
                  <i className="fas fa-dollar-sign fa-2x text-gray-300"></i>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className='row'>
        <div className="col-xl-5 col-lg-8">
          <div className="card shadow mb-4 p-3" style={{ height: '400px' }}>
            <h5 className='text-center m-0'>Biểu đồ trạng thái cv</h5>
            <Pie className="p-3" data={dataChartPieStaticCv} options={optionsPieStaticCv} />
          </div>
        </div>
        <div className="col-xl-7">
          <div className="card shadow mb-4 p-3 d-flex justify-content-center align-items-center" style={{ height: '400px' }}>
            <Bar data={dataChartBarPostByCate} options={optionsBarPostByCate} />
          </div>
        </div>
      </div>
    </>
  );
};

export default Home;
