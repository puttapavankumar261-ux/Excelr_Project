import axiosClient from "../../../api/axiosClient";

export const employeeLogin = async (data) => {
  return await axiosClient.post("/employee/login", data);
};

export const userLogin = async (data) => {
  return await axiosClient.post("/user/login", data);
};

const isMissingEndpoint = (error) =>
  error?.response?.status === 404 || error?.response?.status === 405;

export const loginWithFallback = async (data) => {
  try {
    return await employeeLogin(data);
  } catch (employeeError) {
    try {
      return await userLogin(data);
    } catch (userError) {
      if (isMissingEndpoint(employeeError)) {
        throw userError;
      }

      throw employeeError;
    }
  }
};

export const getLoginRole = (loginResponseData) => {
  return (
    loginResponseData?.role ||
    loginResponseData?.user?.role ||
    loginResponseData?.employee?.role ||
    ""
  ).toUpperCase();
};

export const normalizeLoggedInUser = (loginResponseData) => {
  const employee = loginResponseData?.employee;
  const user = loginResponseData?.user;
  const id =
    loginResponseData?.id ||
    loginResponseData?.employeeid ||
    employee?.employeeid ||
    user?.id;

  return {
    ...loginResponseData,
    id,
    role: getLoginRole(loginResponseData),
  };
};

export const changePassword = async (data) => {
  return await axiosClient.post(
    "/change-password",
    data
  );
};
