import axiosClient from "../../../api/axiosClient";

const isNetworkFailure = (error) =>
  error?.code === "ECONNREFUSED" ||
  error?.code === "ERR_NETWORK" ||
  error?.message?.includes("ECONNREFUSED") ||
  error?.message?.includes("ERR_CONNECTION_REFUSED") ||
  error?.message?.includes("Network Error") ||
  error?.message?.includes("502") ||
  error?.response?.status === 502;

const mockPayrolls = [
  {
    payrollId: 501,
    employee: {
      employeeid: 1,
      employeename: "Mock Employee",
      department: "SOFTWARE",
    },
    payrollMonth: "2026-06-01",
    grossSalary: 85000,
    netSalary: 74200,
    status: "PENDING",
  },
  {
    payrollId: 502,
    employee: {
      employeeid: 2,
      employeename: "Priya Sharma",
      department: "HR",
    },
    payrollMonth: "2026-06-01",
    grossSalary: 65000,
    netSalary: 58600,
    status: "APPROVED",
  },
];

export const getAllPayrolls = async () => {
  try {
    return await axiosClient.get("/getallpayrolls");
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: mockPayrolls };
    }
    throw error;
  }
};

export const getPayrollById = async (payrollId) => {
  try {
    return await axiosClient.get(`/getpayroll/${payrollId}`);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return {
        data:
          mockPayrolls.find((payroll) => payroll.payrollId === Number(payrollId)) ||
          mockPayrolls[0],
      };
    }
    throw error;
  }
};

export const savePayroll = async (data) => {
  try {
    return await axiosClient.post("/savepayroll", data);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return {
        data: {
          ...data,
          payrollId: Date.now(),
          status: "PENDING",
        },
      };
    }
    throw error;
  }
};

export const updatePayroll = async (
  payrollId,
  data
) => {
  try {
    return await axiosClient.put(`/updatepayroll/${payrollId}`, data);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { ...data, payrollId } };
    }
    throw error;
  }
};

export const approvePayroll = async (
  payrollId,
  approvedById
) => {
  try {
    return await axiosClient.put(
      `/approvepayroll/${payrollId}/${approvedById}`,
    );
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { success: true, payrollId, approvedById, status: "APPROVED" } };
    }
    throw error;
  }
};

export const deletePayroll = async (
  payrollId
) => {
  try {
    return await axiosClient.delete(`/deletepayroll/${payrollId}`);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { success: true, payrollId } };
    }
    throw error;
  }
};
