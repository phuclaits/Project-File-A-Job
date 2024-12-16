import axios from "../axios";

const getListPostService = (data) => {
  if (!data?.search) {
    data.search = "";
  }
  if (data.isHot === 1) {
    return axios.get(
      `/app-tuyen-dung/api/v1/post/get-filter-post?limit=${data.limit}&offset=${data.offset}&categoryJobCode=${data.categoryJobCode}&addressCode=${data.addressCode}&salaryJobCode=${data.salaryJobCode}&categoryJoblevelCode=${data.categoryJoblevelCode}&categoryWorktypeCode=${data.categoryWorktypeCode}&experienceJobCode=${data.experienceJobCode}&isHot=${data.isHot}&search=${data.search}`
    );
  }
  return axios.get(
    `/app-tuyen-dung/api/v1/post/get-filter-post?limit=${data.limit}&offset=${data.offset}&categoryJobCode=${data.categoryJobCode}&addressCode=${data.addressCode}&salaryJobCode=${data.salaryJobCode}&categoryJoblevelCode=${data.categoryJoblevelCode}&categoryWorktypeCode=${data.categoryWorktypeCode}&experienceJobCode=${data.experienceJobCode}&search=${data.search}`
  );
};
const getListJobTypeAndCountPost = (data) => {};

//====================POST===========================//
//createPostService, reupPostService, updatePostService, activePostService, banPostService, acceptPostService
const createPostService = (data) => {
    return axios.post(`/app-tuyen-dung/api/v1/post/create-new-post`, data)

}
const reupPostService = (data) => {
    return axios.post(`/app-tuyen-dung/api/v1/post/reup-post`, data)

}
const updatePostService = (data) => {
    return axios.put(`/app-tuyen-dung/api/v1/post/update-post`, data)

}
const activePostService = (data) => {
    return axios.put(`/app-tuyen-dung/api/v1/post/activate-post`, data)

}
const banPostService = (data) => {
    return axios.put(`/app-tuyen-dung/api/v1/post/ban-post`, data)
}
const acceptPostService = (data) => {
    return axios.put(`/app-tuyen-dung/api/v1/post/accept-post`, data)
}

const getAllPostByAdminService = (data) => {
  return axios.get(`http://localhost:8080/app-tuyen-dung/api/v1/post/get-list-post-admin?idCompany=${data.idCompany}&limit=${data.limit}&offset=${data.offset}&search=${data.search}&censorCode=${data.censorCode}`)

}
const getAllPostByRoleAdminService = (data) => {
  return axios.get(`http://localhost:8080/app-tuyen-dung/api/v1/post/get-All-Post-RoleAdmin?limit=${data.limit}&offset=${data.offset}&search=${data.search}&censorCode=${data.censorCode}`)

}
//===============ALL CODE========================//
const getAllCodeService = (type) => {
  return axios.get(`/app-tuyen-dung/api/v1/get-all-code?type=${type}`);
};

const getDetailPostByIdService = (id) => {
  return axios.get(`/app-tuyen-dung/api/v1/cv/by-post/${id.postId}`);
};
export const getDetailPostById = (id) => {
  return axios.get(`/app-tuyen-dung/api/v1/post/get-detail-post-by-id?id=${id}`);
}
export const getDetailToEditPostService = (id) => {
  return axios.get(`/app-tuyen-dung/api/v1/post/get-detail-post-by-id?id=${id}`);
}

const getListCompany = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/company/get-list-company?limit=${data.limit}&offset=${data.offset}&search=${data.search}`)

}

const getDetailCompanyById = (id) => {
  return axios.get(`/app-tuyen-dung/api/v1/company/get_company/${id}`)
};

const handleLoginService = (data) => {
  return axios.post(`http://localhost:8080/app-tuyen-dung/api/v1/auth/login`, data);
};

const createNewUser = (data) => {
  return axios.post(`http://localhost:8080/app-tuyen-dung/api/v1/auth/register`, data);
};
export const requestOtp = async (phoneNumber) => {
  try {
      const response = await axios.post(
          'http://localhost:8080/app-tuyen-dung/api/v1/auth/otp/request-otp',
          { phoneNumber }
      );
      return response;
  } catch (error) {
      console.error('Error requesting OTP:', error);
      throw error;
  }
};

export const verifyOtp = async (requestId, OTP) => {
  try {
      const response = await axios.post(
          'http://localhost:8080/app-tuyen-dung/api/v1/auth/otp/verify-otp',
          { requestId, OTP }
      );
      return response;
  } catch (error) {
      console.error('Error verifying OTP:', error);
      throw error;
  }
};
export const forgetPassword = async (phoneNumber) => {
  try {
      const response = await axios.put(
          `http://localhost:8080/app-tuyen-dung/api/v1/auth/forget-password/${phoneNumber}`
      );
      return response;
  } catch (error) {
      console.error('Error get new password:', error);
      throw error;
  }
};
const getDetailUserById = (data) => {
  return axios.get(`http://localhost:8080/app-tuyen-dung/api/v1/user/get-users/${data}`);
};

const UpdateUserService = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/user/update`, data);
};

const UpdateUserSettingService = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/user/set-user-setting`, data);
};

const getAllSkillByJobCode = (categoryJobCode) => {
  return axios.get(
    `/app-tuyen-dung/api/v1/skill/get-all-skill-by-job-code?categoryJobCode=${categoryJobCode}`
  );
};

const getDetailCompanyByUserId = (userId, companyId) => {
  return axios.get(
    `/app-tuyen-dung/api/v1/company/get-detail-company-by-userId?userId=${userId}&companyId=${companyId}`
  );
};
const createCompanyService = async (formData) => {
  try {
    let response = await axios.post(
      "http://localhost:8080/app-tuyen-dung/api/v1/company/create-company",
      formData
    );
    console.log(response);
    return response.data;
  } catch (error) {
    console.error("Error creating company:", error);
    return error.response.data;
  }
};

const updateCompanyService = (data) => {
  return axios.put(`http://localhost:8080/app-tuyen-dung/api/v1/company/update-company`, data);
};

const getAllCompanyByAdmin = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/company/getAllCompanyByAdmin?limit=${data.limit}&offset=${data.offset}&search=${data.search}&censorCode=${data.censorCode}`)
}
const banCompanyByAdmin = (companyId) => {
  return axios.put(`/app-tuyen-dung/api/v1/company/Ban-company?companyId=${companyId}`)
}

const unbanCompanyByAdmin = (companyId) => {
  return axios.put(`/app-tuyen-dung/api/v1/company/UnBan-company?companyId=${companyId}`)

}

const acceptCompanyByAdmin = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/company/Accept-company`, data)
}

const RecruitmentServiceByEmployer = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/company/AddUser-ToCompany`, data)

}
const createNewUserByEmployeer = (data) => {
  return axios.post(`http://localhost:8080/app-tuyen-dung/api/v1/company/CreateEmployee-FromCompany`, data);
};

const getAllUserByCompanyIdService = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/company/getAllUserByCompanyId?companyId=${data.idCompany}&limit=${data.limit}&offset=${data.offset}`)

}
const QuitCompanyService = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/company/CancelCompanyByEmployer?userId=${data}`)

}

const checkUserPhoneService = (phonenumber) => {
  return axios.get(`/app-tuyen-dung/api/v1/user/check-phonenumber-user?phonenumber=${phonenumber}`);
};
const changePasswordByphone = (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/account/change-password`, data);
};
export const changePasswordByphoneForgotPass = (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/account/change-forgot-password`, data);
};
const handleChangePassword = (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/account/change-password`, data);
};

const getAllUsers = (data) => {
  return axios.get(
    `/app-tuyen-dung/api/v1/user/users?limit=${data.limit}&offset=${data.offset}&search=${data.search}`
  );
};

const createJobType = (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/admin/code/create-jobType`, data);
};
const getAllJobType = (data) => {
  return axios.get(
    `app-tuyen-dung/api/v1/admin/code/get-all-jobtype?limit=${data.limit}&offset=${data.offset}&search=${data.search}`
  );
};

const DeleteJobtype = (codeId) => {
  return axios.delete(`/app-tuyen-dung/api/v1/admin/code/delete-jobtype?code=${codeId}`);
};
const UpdateJobtype = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/admin/code/update-jobtype`, data);
};
const getDetailJobTypeByCode = (code) => {
  return axios.get(`/app-tuyen-dung/api/v1/admin/code/get-detail-JobType-by-code?code=${code}`);
};
//, getDetailJobLevel, UpdateJobLevel

const createJobLevel = (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/admin/code/create-jobLevel`, data);
};
const getAllJobLevel = (data) => {
  return axios.get(
    `/app-tuyen-dung/api/v1/admin/code/get-all-joblevel?limit=${data.limit}&offset=${data.offset}&search=${data.search}`
  );
};

const DeleteJobLevel = (codeId) => {
  return axios.delete(`/app-tuyen-dung/api/v1/admin/code/delete-joblevel?code=${codeId}`);
};
const UpdateJobLevel = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/admin/code/update-joblevel`, data);
};
const getDetailJobLevelByCode = (code) => {
  return axios.get(`/app-tuyen-dung/api/v1/admin/code/get-detail-JobLevel-by-code?code=${code}`);
};

// createWorkType, getDetailWorkTypeByCode, UpdateWorkType, DeleteWorkType,getAllWorkType
const createWorkType = (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/admin/code/create-worktype`, data);
};
const getAllWorkType = (data) => {
  return axios.get(
    `/app-tuyen-dung/api/v1/admin/code/get-all-worktype?limit=${data.limit}&offset=${data.offset}&search=${data.search}`
  );
};

const DeleteWorkType = (codeId) => {
  return axios.delete(`/app-tuyen-dung/api/v1/admin/code/delete-worktype?code=${codeId}`);
};

const UpdateWorkType = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/admin/code/update-worktype`, data);
};

const getDetailWorkTypeByCode = (code) => {
  return axios.get(`/app-tuyen-dung/api/v1/admin/code/get-detail-WorkType-by-code?code=${code}`);
};

// createSalaryType, getDetailSalaryTypeByCode, UpdateSalaryType, DeleteSalaryType, getAllSalaryType
const createSalaryType = (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/admin/code/create-salarytype`, data);
};
const getAllSalaryType = (data) => {
  return axios.get(
    `/app-tuyen-dung/api/v1/admin/code/get-all-salarytype?limit=${data.limit}&offset=${data.offset}&search=${data.search}`
  );
};

const DeleteSalaryType = (codeId) => {
  return axios.delete(`/app-tuyen-dung/api/v1/admin/code/delete-salarytype?code=${codeId}`);
};

const UpdateSalaryType = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/admin/code/update-salarytype`, data);
};

const getDetailSalaryTypeByCode = (code) => {
  return axios.get(`/app-tuyen-dung/api/v1/admin/code/get-detail-SalaryType-by-code?code=${code}`);
};

//createExpType, getAllExpType, DeleteExpType, UpdateExpType, getDetailExpTypeByCode
const createExpType = (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/admin/code/create-exptype`, data);
};
const getAllExpType = (data) => {
  return axios.get(
    `/app-tuyen-dung/api/v1/admin/code/get-all-exptype?limit=${data.limit}&offset=${data.offset}&search=${data.search}`
  );
};

const DeleteExpType = (codeId) => {
  return axios.delete(`/app-tuyen-dung/api/v1/admin/code/delete-exptype?code=${codeId}`);
};

const UpdateExpType = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/admin/code/update-exptype`, data);
};

const getDetailExpTypeByCode = (code) => {
  return axios.get(`/app-tuyen-dung/api/v1/admin/code/get-detail-ExpType-by-code?code=${code}`);
};


const BanUserService = (userId) => {
  return axios.post(`http://localhost:8080/app-tuyen-dung/api/v1/user/ban-user`, {
    userId: userId
  });
};

const UnbanUserService = (userId) => {
  return axios.post(`http://localhost:8080/app-tuyen-dung/api/v1/user/unban-user`, {
    userId: userId
  });
};

const AddNewUser = (data) => {
  return axios.post(`/api/create-new-user`, data);
};
const getListSkill = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/skill/get-list-skill?categoryJobCode=${data.categoryJobCode}&limit=${data.limit}&offset=${data.offset}&search=${data.search}`)
}
const getDetailSkillById = (id) => {
  return axios.get(`/app-tuyen-dung/api/v1/skill/getdetailsskillbyid?id=${id}`)
}

const DeleteSkillService = (skillId) => {
  return axios.delete(`/app-tuyen-dung/api/v1/skill/delete-skill-id?id=${skillId}`);
};
const createSkill = (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/skill/createnewskill`, data)

}
const UpdateSkill = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/skill/updateskill`, data)

}

//======================== PACKAGE ====================================//
const getPackageByType = (isHot) => {
  return axios.get(`/app-tuyen-dung/api/v1/package/get-package-by-Type?isHot=${isHot}`)
}

const getPackageById = (id) => {
  return axios.get(`/app-tuyen-dung/api/v1/package/get-package-by-Id?id=${id}`)
}
// payment
const getPaymentLink = (id,amount) => {
  return axios.get(`/app-tuyen-dung/api/v1/payment/get-payment-link?id=${id}&amount=${amount}`)
}

const paymentOrderSuccessService = (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/payment/execute`, data)
}

const getAllPackage = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/api/get-all-package?limit=${data.limit}&offset=${data.offset}&search=${data.search}`)
}
// getPackageByType, getPackageById, paymentOrderSuccessService, getAllPackage

// company
const getAllPackageCV = () => {
  return axios.get(`/app-tuyen-dung/api/v1/package-cv/get-all-package-cv`)
}
const getPaymentLinkCv = (id,amount) => {
  return axios.get(`/app-tuyen-dung/api/v1/payment/get-payment-link-cv?id=${id}&amount=${amount}`)
}

const executePaymentCV = (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/payment/execute-cv`, data)
}

// thống kê 
const getStatisticalPackagePost = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/api/get-statistical-package?limit=${data.limit}&offset=${data.offset}&fromDate=${data.fromDate}&toDate=${data.toDate}`)
}

const getStatisticalTypePost = (limit) => {
  return axios.get(`/app-tuyen-dung/api/v1/post/get-statistical-post?limit=${limit}`)
}

const getStatisticalPackageCv = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/api/get-statistical-package-cv?limit=${data.limit}&offset=${data.offset}&fromDate=${data.fromDate}&toDate=${data.toDate}`)
}

const getListNoteByPost = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/post/get-note-by-post?limit=${data.limit}&offset=${data.offset}&id=${data.id}`)
}


const getHistoryTradeCv = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/order-package-cv/get-history-trade-cv?limit=${data.limit}&offset=${data.offset}&fromDate=${data.fromDate}&toDate=${data.toDate}&companyId=${data.companyId}`)
}
const getHistoryTradePost = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/order-package-post/get-history-trade-post?limit=${data.limit}&offset=${data.offset}&fromDate=${data.fromDate}&toDate=${data.toDate}&companyId=${data.companyId}`)
}

export const getAllPackageCv = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/package-cv/get-all-package-cv-filter?limit=${data.limit}&offset=${data.offset}&search=${data.search}`)
}
export const setActivePackageCv= (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/package-cv/set-active`, data)
}
export const getPackageByIdCv = (id) => {
  return axios.get(`/app-tuyen-dung/api/v1/package-cv/get-by-id?id=${id}`)
}
export const createPackageCv= (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/package-cv/create-package-cv`, data)
}

export const updatePackageCv = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/package-cv/update-package-cv`, data)
}

export const getAllPackagePost = (data) => {
  return axios.get(`/app-tuyen-dung/api/v1/package-post/get-all-package?limit=${data.limit}&offset=${data.offset}&search=${data.search}`)
}
export const setActivePackagePost= (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/package-post/set-active-package-post`, data)
}
export const getPackagePostById = (id) => {
  return axios.get(`/app-tuyen-dung/api/v1/package-post/get-packagePost-by-id?id=${id}`)
}
export const createPackagePost= (data) => {
  return axios.post(`/app-tuyen-dung/api/v1/package-post/create-package-post`, data)
}

export const updatePackagePost = (data) => {
  return axios.put(`/app-tuyen-dung/api/v1/package-post/update-package-post`, data)
}
export {
  getHistoryTradeCv,
  getListNoteByPost,
  getListCompany,
  getDetailCompanyById,
  getListPostService,
  getListJobTypeAndCountPost,
  getAllCodeService,
  getDetailPostByIdService,
  handleLoginService,
  createNewUser,
  UpdateUserService,
  getDetailUserById,
  getAllSkillByJobCode,
  UpdateUserSettingService,
  getDetailCompanyByUserId,
  updateCompanyService,
  checkUserPhoneService,
  changePasswordByphone,
  createCompanyService,
  getAllCompanyByAdmin,
  banCompanyByAdmin,unbanCompanyByAdmin,acceptCompanyByAdmin,
  RecruitmentServiceByEmployer,createNewUserByEmployeer,
  getAllUserByCompanyIdService, QuitCompanyService,
  handleChangePassword,
  getAllUsers,
  BanUserService,
  UnbanUserService,
  AddNewUser,
  createJobType,
  getAllJobType,
  DeleteJobtype,
  UpdateJobtype,
  getDetailJobTypeByCode,
  getListSkill,
  DeleteSkillService,
  getDetailSkillById,

  UpdateSkill,
  createSkill,
  createJobLevel,
  getAllJobLevel,
  DeleteJobLevel,
  UpdateJobLevel,
  getDetailJobLevelByCode,

  createWorkType,
  getDetailWorkTypeByCode,
  UpdateWorkType,
  DeleteWorkType,
  getAllWorkType,

  createSalaryType,
  getDetailSalaryTypeByCode,
  UpdateSalaryType,
  DeleteSalaryType,
  getAllSalaryType,

  createExpType,
  getAllExpType,
  DeleteExpType,
  UpdateExpType,
  getDetailExpTypeByCode,

  getStatisticalPackagePost,
  getStatisticalTypePost,
  getStatisticalPackageCv,
  //POST//
  createPostService,
  reupPostService,
  updatePostService,
  activePostService,
  banPostService,
  acceptPostService,
  getAllPostByAdminService,
  getAllPostByRoleAdminService,

  //PACKAGE
  getPackageByType,
  getPaymentLink,
  getPackageById,
  paymentOrderSuccessService,
  getAllPackage,
  getAllPackageCV,
  getPaymentLinkCv,
  executePaymentCV,
  getHistoryTradePost,

};
