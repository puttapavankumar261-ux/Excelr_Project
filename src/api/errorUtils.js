export const isConnectionError = (error) =>
  error?.code === "ECONNREFUSED" ||
  error?.code === "ERR_NETWORK" ||
  error?.message?.includes("ECONNREFUSED") ||
  error?.message?.includes("ERR_CONNECTION_REFUSED") ||
  error?.message?.includes("Network Error") ||
  error?.response?.status === 502;

export const getApiErrorMessage = (
  error,
  fallbackMessage = "Request failed. Please try again.",
) => {
  const data = error?.response?.data;

  if (typeof data === "string" && data.trim()) {
    return data;
  }

  if (data?.message) {
    return data.message;
  }

  if (data?.error) {
    return data.error;
  }

  if (data?.errors && typeof data.errors === "object") {
    return Object.entries(data.errors)
      .map(([field, message]) => `${field}: ${message}`)
      .join(", ");
  }

  if (isConnectionError(error)) {
    return "Backend server is not reachable at http://127.0.0.1:8080. Start it with .\\mvnw.cmd spring-boot:run, then try again.";
  }

  return error?.message || fallbackMessage;
};
