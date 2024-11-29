import axios from "axios";

const token = localStorage.token_user;
const getAllListCvByUserIdService = (data) => {
  return axios.get(
    `http://localhost:8080/app-tuyen-dung/api/v1/cv/get-all-cv-by-userId?limit=${data.limit}&offset=${data.offset}&userId=${data.userId}`
  );
};

const getDetailCvService = (id) => {
  return axios.get(
    `http://localhost:8080/app-tuyen-dung/api/v1/cv/user-detail/${id}`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    }
  );
};

export const getDetailCvServicebyAdmin = (id) => {
    return axios.get(
      `http://localhost:8080/app-tuyen-dung/api/v1/cv/detail/${id}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );
  };


const getStatisticalCv = (data) => {
  return axios.get(
    `/app-tuyen-dung/api/v1/package/get-statistical-cv?limit=${data.limit}&offset=${data.offset}&fromDate=${data.fromDate}&toDate=${data.toDate}&companyId=${data.companyId}`
  );
};
// when send cv new => send and create new
const createNewCv = async (data, token) => {
  try {
    // Thực hiện request POST với headers Authorization
    let response = await axios.post(
      "http://localhost:8080/app-tuyen-dung/api/v1/cv/createCVnew",
      data,
      {
        headers: {
          Authorization: `Bearer ${token}`, // Thêm token vào header
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error creating CV:", error);
    throw error;
  }
};
const getPendingCVsService = (data) => {
  return axios.get(
    `http://localhost:8080/app-tuyen-dung/api/v1/cv/status?status=pending&limit=${data.limit}&offset=${data.offset}`
  );
};

export const acceptCVService = (cvId) => {
  console.log(cvId);
  return axios.put(`http://localhost:8080/app-tuyen-dung/api/v1/cv/accept`, {
    id: cvId,
  });
};
export const rejectCVService = (cvId) => {
  return axios.put(`http://localhost:8080/app-tuyen-dung/api/v1/cv/reject`, {
    id: cvId,
  });
};
export const reviewCVService = (cvId) => {
  return axios.put(`http://localhost:8080/app-tuyen-dung/api/v1/cv/review`, {
    id: cvId,
  });
};

export const getAcceptedCVsService = async () => {
  return axios.get(
    "http://localhost:8080/app-tuyen-dung/api/v1/cv/list-accepted"
  );
};

export const scheduleInterviewService = (data) => {
  return axios.put(
    "http://localhost:8080/app-tuyen-dung/api/v1/cv/schedule-interview",
    data
  );
};

export const getUnderReviewCVsService = async (params) => {
  return axios.get("/api/cv/under-review");
};

const checkSeeCandiate = (data) => {
  return axios.post(
    `http://localhost:8080/app-tuyen-dung/api/v1/cv/checkSeeCandidate?userId=${data.userId}&companyId=${data.companyId}`
  );
};
const getFilterCvUV = (data) => {
  return axios.post(
    `http://localhost:8080/app-tuyen-dung/api/v1/cv/filter`,
    data
  );
};
const getFilterCv = (data) => {
  return axios.get(
    `http://localhost:8080/app-tuyen-dung/api/v1/user/search-users?limit=${data.limit}&offset=${data.offset}&experienceJobCode=${data.experienceJobCode}&categoryJobCode=${data.categoryJobCode}&listSkills=${data.listSkills}&otherSkills=${data.otherSkills}&salaryCode=${data.salaryCode}&provinceCode=${data.provinceCode}`
  );
};

const getAllListCvByPostService = (data) => {
  return axios.get(
    `http://localhost:8080/app-tuyen-dung/api/v1/cv/detail/${data.postId}?limit=${data.limit}&offset=${data.offset}`
  );
};
const getDetailPostByIdService = (id) => {
  return axios.get(`/app-tuyen-dung/api/v1/cv/by-post/${id}`);
};
export {
  getAllListCvByPostService,
  getDetailPostByIdService,
  getAllListCvByUserIdService,
  getDetailCvService,
  getStatisticalCv,
  createNewCv,
  checkSeeCandiate,
  getFilterCv,
  getFilterCvUV,
  getPendingCVsService,
};
