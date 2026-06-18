import axiosClient from "../../../api/axiosClient";

export const getAllPayrolls = async () => {
  return await axiosClient.get("/getallpayrolls");
};

export const getPayrollById = async (payrollId) => {
  return await axiosClient.get(
    `/getpayroll/${payrollId}`
  );
};

export const savePayroll = async (data) => {
  return await axiosClient.post(
    "/savepayroll",
    data
  );
};

export const updatePayroll = async (
  payrollId,
  data
) => {
  return await axiosClient.put(
    `/updatepayroll/${payrollId}`,
    data
  );
};

export const approvePayroll = async (
  payrollId,
  approvedById
) => {
  return await axiosClient.put(
    `/approvepayroll/${payrollId}/${approvedById}`
  );
};

export const deletePayroll = async (
  payrollId
) => {
  return await axiosClient.delete(
    `/deletepayroll/${payrollId}`
  );
};