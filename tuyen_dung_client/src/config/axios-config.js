import axios from "axios";

const token = localStorage.token_user
const instance = axios.create({
  baseURL: "http://localhost:8080/app-tuyen-dung/api/v1",
  timeout: 10000,
  headers: {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  },
});

export default instance;