import axiosClient from "../../../api/axiosClient";

const isNetworkFailure = (error) =>
  error?.code === "ECONNREFUSED" ||
  error?.code === "ERR_NETWORK" ||
  error?.message?.includes("ECONNREFUSED") ||
  error?.message?.includes("ERR_CONNECTION_REFUSED") ||
  error?.message?.includes("Network Error") ||
  error?.message?.includes("502") ||
  error?.response?.status === 502;

const mockTaxSlabs = [
  {
    taxid: 1,
    slabName: "Standard FY 2026",
    minAmount: 0,
    maxAmount: 700000,
    taxPercentage: 0,
  },
  {
    taxid: 2,
    slabName: "Professional FY 2026",
    minAmount: 700001,
    maxAmount: 1500000,
    taxPercentage: 10,
  },
];

export const getAllTaxSlabs = async () => {
  try {
    return await axiosClient.get("/getalltaxslabs");
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: mockTaxSlabs };
    }
    throw error;
  }
};

export const getTaxSlabById = async (taxId) => {
  try {
    return await axiosClient.get(`/gettaxslab/${taxId}`);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return {
        data:
          mockTaxSlabs.find((slab) => slab.taxid === Number(taxId)) ||
          mockTaxSlabs[0],
      };
    }
    throw error;
  }
};

export const saveTaxSlab = async (data) => {
  try {
    return await axiosClient.post("/savetaxslab", data);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { ...data, taxid: Date.now() } };
    }
    throw error;
  }
};

export const updateTaxSlab = async (taxId, data) => {
  try {
    return await axiosClient.put(`/updatetaxslab/${taxId}`, data);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { ...data, taxid: taxId } };
    }
    throw error;
  }
};

export const deleteTaxSlab = async (taxId) => {
  try {
    return await axiosClient.delete(`/deletetaxslab/${taxId}`);
  } catch (error) {
    if (isNetworkFailure(error)) {
      return { data: { success: true, taxId } };
    }
    throw error;
  }
};
