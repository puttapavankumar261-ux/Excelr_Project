import axiosClient from "../../../api/axiosClient";

export const getAllPayslips = () =>
  axiosClient.get("/getallpayslips");

export const generatePayslip = (
  employeeId,
  year,
  month
) =>
  axiosClient.post(
    `/generatepayslip/${employeeId}/${year}/${month}`
  );

export const approvePayslip = (
  payslipId,
  approvedById
) =>
  axiosClient.put(
    `/approvepayslip/${payslipId}/${approvedById}`
  );

export const markPayslipPaid = (
  payslipId
) =>
  axiosClient.put(
    `/markpayslippaid/${payslipId}`
  );

export const deletePayslip = (
  payslipId
) =>
  axiosClient.delete(
    `/deletepayslip/${payslipId}`
  );

export const getEmployeePayslips = (
  employeeId
) =>
  axiosClient.get(
    `/getemployeepayslips/${employeeId}`
  );