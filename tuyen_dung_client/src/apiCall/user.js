import instance from "../config/axios-config";

export const addToken = async ({ phone, registrationToken }) => {
  const add = await instance.post(
    `/user-firebase/token`,
    null,
      {
        params: {
          token: registrationToken,
          phoneNumber: phone
        },
      }
    );
    return add.data;
  };

  export const removeToken = async (phone, registrationToken) => {
    try {
      const deletedUser = await instance.delete('/user-firebase/token', {
        params: {
          phoneNumber: phone,
          token: registrationToken,
        },
      });
      return "OK";
    } catch (error) {
      console.error("Error deleting token:", error);
      throw error;
    }
  };

  export const addUser = async ({ phoneNumber, role, registrationToken }) => {
    try {
      const response = await instance.post(
        `/user-firebase`,
        {
          phoneNumber: phoneNumber,
          role: role,
          registrationToken: registrationToken,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error adding user:", error);
      throw error;
    }
  };

  
  export const getUsersByCategory = async (categoryJobCode) => {
    try {
      const response = await instance.get(`/user/phone`, {
        params: {
          categoryJobCode: categoryJobCode,
        },
      });
      return response.data;
    } catch (error) {
      console.error("Error fetching users by category:", error);
      throw error;
    }
  };
  export const getUsersByCompanyId = async (companyId) => {
    try {
      const response = await instance.get(`/user/phone-of-company/${companyId}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching phone numbers by company ID:", error);
      throw error;
    }
  };
  export const getIdCompany = async (phone) => {
    try {
      const response = await instance.get(`user/companyId/${phone}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching id company by phone number:", error);
      throw error;
    }
  };

  export const getPhoneByUserId = async (userId) => {
    try {
      const response = await instance.get(`/user/phone/${userId}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching phone numbers by company ID:", error);
      throw error;
    }
  };

