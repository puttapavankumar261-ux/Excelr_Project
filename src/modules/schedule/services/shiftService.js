import axiosClient from "../../../api/axiosClient";

export const saveShift = async (data) => {
  return await axiosClient.post(
    "/saveshift",
    data
  );
};

export const getAllShifts = async () => {
  return await axiosClient.get(
    "/getallshifts"
  );
};

export const getShiftById = async (
  shiftId
) => {
  return await axiosClient.get(
    `/getshift/${shiftId}`
  );
};

export const updateShift = async (
  shiftId,
  data
) => {
  return await axiosClient.put(
    `/updateshift/${shiftId}`,
    data
  );
};

export const deleteShift = async (
  shiftId
) => {
  return await axiosClient.delete(
    `/deleteshift/${shiftId}`
  );
};

export const assignShift = async (
  employeeId,
  shiftId
) => {
  return await axiosClient.put(
    `/assignshift/${employeeId}/${shiftId}`
  );
};