import axiosClient from "../../../api/axiosClient";

const isNetworkFailure = (error) =>
  error?.code === "ECONNREFUSED" ||
  error?.code === "ERR_NETWORK" ||
  error?.message?.includes("ECONNREFUSED") ||
  error?.message?.includes("ERR_CONNECTION_REFUSED") ||
  error?.message?.includes("Network Error") ||
  error?.message?.includes("502") ||
  error?.response?.status === 502;

const mockLeaves = [
  {
    leaveId: 101,
    employeeId: 1,
    employeeName: "Mock Employee",
    department: "SOFTWARE",
    leaveType: "CASUAL",
    leaveStartDate: "2026-06-24",
    leaveEndDate: "2026-06-25",
    leaveDays: 2,
    approvalStatus: "PENDING_MANAGER",
  },
  {
    leaveId: 102,
    employeeId: 2,
    employeeName: "Priya Sharma",
    department: "HR",
    leaveType: "SICK",
    leaveStartDate: "2026-06-18",
    leaveEndDate: "2026-06-18",
    leaveDays: 1,
    approvalStatus: "APPROVED",
  },
];

export const getAllLeaves = async () => {
  try {
    return await axiosClient.get("/getallleaves");
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: mockLeaves };
    }
    throw error;
  }
};

export const getEmployeeLeaves = async (employeeId) => {
  try {
    return await axiosClient.get(`/getemployeeleaves/${employeeId}`);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return {
        data: mockLeaves.filter(
          (leave) => leave.employeeId === Number(employeeId),
        ),
      };
    }
    throw error;
  }
};

export const applyLeave = async (data) => {
  try {
    return await axiosClient.post("/saveleave", data);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { success: true, ...data } };
    }
    throw error;
  }
};

export const cancelLeave = async (leaveId) => {
  try {
    return await axiosClient.put(`/cancelleave/${leaveId}`);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { success: true, leaveId, approvalStatus: "CANCELLED" } };
    }
    throw error;
  }
};

export const finalApproveLeave = async (leaveId) => {
  try {
    return await axiosClient.put(`/approveleave/${leaveId}`);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { success: true, leaveId, approvalStatus: "APPROVED" } };
    }
    throw error;
  }
};

export const rejectLeave = async (
  leaveId,
  rejectedById,
  reason
) => {
  try {
    return await axiosClient.put(
      `/rejectleave/${leaveId}/${rejectedById}`,
      reason,
    );
  } catch (error) {
    if (isNetworkFailure(error)) {
      return {
        data: { success: true, leaveId, rejectedById, reason, approvalStatus: "REJECTED" },
      };
    }
    throw error;
  }
};

export const sendToManager = async (leaveId) => {
  try {
    return await axiosClient.put(`/sendleavetomanager/${leaveId}`);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { success: true, leaveId } };
    }
    throw error;
  }
};

export const sendToHr = async (leaveId) => {
  try {
    return await axiosClient.put(`/sendleavetohr/${leaveId}`);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { success: true, leaveId } };
    }
    throw error;
  }
};

export const approveByTeamLead = async (
  leaveId,
  approverId
) => {
  try {
    return await axiosClient.put(
      `/teamleadreviewleave/${leaveId}/${approverId}`,
    );
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { success: true, leaveId, approverId } };
    }
    throw error;
  }
};

export const approveByManager = async (
  leaveId,
  approverId
) => {
  try {
    return await axiosClient.put(
      `/managerreviewleave/${leaveId}/${approverId}`,
    );
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { success: true, leaveId, approverId } };
    }
    throw error;
  }
};

export const approveByHr = async (
  leaveId,
  approverId
) => {
  try {
    return await axiosClient.put(`/hrreviewleave/${leaveId}/${approverId}`);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { success: true, leaveId, approverId } };
    }
    throw error;
  }
};

export const hrApproveLeave = async (
  leaveId,
  approverId
) => {
  return approveByHr(leaveId, approverId);
};

export const teamLeadApproveLeave = async (
  leaveId,
  approverId
) => {
  return approveByTeamLead(leaveId, approverId);
};

export const managerApproveLeave = async (
  leaveId,
  approverId
) => {
  return approveByManager(leaveId, approverId);
};


