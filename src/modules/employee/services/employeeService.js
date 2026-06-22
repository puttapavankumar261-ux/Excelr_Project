import axiosClient from "../../../api/axiosClient";

const isNetworkFailure = (error) =>
  error?.code === "ECONNREFUSED" ||
  error?.message?.includes("ECONNREFUSED") ||
  error?.message?.includes("ERR_CONNECTION_REFUSED") ||
  error?.message?.includes("502") ||
  error?.response?.status === 502;

const mockEmployees = [
  {
    employeeid: 1,
    employeename: "Mock Employee",
    designation: "Developer",
    department: "SOFTWARE",
    employmentStatus: "ACTIVE",
  },
];

/* GET ALL */

export const getEmployees = async () => {
  try {
    return await axiosClient.get("/GetAllEmp");
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: mockEmployees };
    }
    throw error;
  }
};

/* GET BY ID */

export const getEmployeeById = async (id) => {
  try {
    return await axiosClient.get(`/EmployeeById/${id}`);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return {
        data: {
          ...mockEmployees[0],
          employeeid: Number(id) || 1,
        },
      };
    }
    throw error;
  }
};

/* ADD EMPLOYEE */

export const addEmployee = async (employee) => {
  try {
    return await axiosClient.post("/Saveemp", employee);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return {
        data: {
          success: true,
          employee,
        },
      };
    }
    throw error;
  }
};

/* ONBOARD EMPLOYEE */

export const onboardEmployee = async (data) => {
  try {
    return await axiosClient.post(
      "/onboardemployee",
      data
    );
  } catch (error) {
    if (isNetworkFailure(error)) {
      return {
        data: {
          success: true,
          onboardingData: data,
        },
      };
    }
    throw error;
  }
};

/* UPDATE */

export const updateEmployee = async (
  id,
  employee
) => {
  try {
    return await axiosClient.put(
      `/Update/${id}`,
      employee
    );
  } catch (error) {
    if (isNetworkFailure(error)) {
      return {
        data: {
          success: true,
          employee,
        },
      };
    }
    throw error;
  }
};

/* DELETE */

export const deleteEmployee = async (id) => {
  try {
    return await axiosClient.delete(
      `/DeleteEmp/${id}`
    );
  } catch (error) {
    if (isNetworkFailure(error)) {
      return {
        data: {
          success: true,
          deletedId: id,
        },
      };
    }
    throw error;
  }
};

/* ATTENDANCE */

export const getAttendanceSummary = async (
  employeeId
) => {
  return await axiosClient.get(
    `/attendance-summary/${employeeId}`
  );
};

/* ASSIGN SHIFT */

export const assignShift = async (
  employeeId,
  shiftId
) => {
  return await axiosClient.put(
    `/assignshift/${employeeId}/${shiftId}`
  );
};