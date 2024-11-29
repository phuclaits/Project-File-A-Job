import instance from "../config/axios-config";

export const getStaticCvByStatus = async () => {
    try {
      const response = await instance.get(`/cv/statical-by-status`);
      return response.data;
    } catch (error) {
      console.error("Error fetching static by status:", error);
      throw error;
    }
  };
  export const getStaticCvByStatusnCompany = async (idCompany) => {
    try {
      const response = await instance.get(`/cv/statical-by-status-company/${idCompany}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching static by status and company:", error);
      throw error;
    }
  };
  export const getStaticPostByJobType = async () => {
    try {
      const response = await instance.get(`/post/job-type-count`);
      return response.data;
    } catch (error) {
      console.error("Error fetching static by JobType:", error);
      throw error;
    }
  };

  export const getSumOrderCv = async () => {
    try {
      const response = await instance.get(`/order-package-cv/sum-price`);
      return response.data;
    } catch (error) {
      console.error("Error fetching sum order cv:", error);
      throw error;
    }
  };

  export const getSumOrderPost= async () => {
    try {
      const response = await instance.get(`/order-package-post/sum-price`);
      return response.data;
    } catch (error) {
      console.error("Error fetching sum order post:", error);
      throw error;
    }
  };

  export const getQuantityCompany = async () => {
    try {
      const response = await instance.get(`/company/quantity-company`);
      return response.data;
    } catch (error) {
      console.error("Error fetching quantity company:", error);
      throw error;
    }
  };

  export const getQuantityCandidate = async () => {
    try {
      const response = await instance.get(`/user/quantity_candidate`);
      return response.data;
    } catch (error) {
      console.error("Error fetching quantity candidate:", error);
      throw error;
    }
  };