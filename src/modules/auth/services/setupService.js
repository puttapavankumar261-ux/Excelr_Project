import axiosClient from "../../../api/axiosClient";

export const setupAdmin = async (data) => {
  return await axiosClient.post("/setup/admin", data);
};

export const saveEmployee = async (data) => {
  return await axiosClient.post("/Saveemp", data);
};

export const saveLogin = async (data) => {
  return await axiosClient.post("/savelogin", data);
};
