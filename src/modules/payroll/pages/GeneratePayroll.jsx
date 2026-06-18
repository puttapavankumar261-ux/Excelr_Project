import { useState } from "react";
import { generatePayroll } from "../services/payrollService";

const GeneratePayroll = () => {
  const [employeeId, setEmployeeId] = useState("");
  const [year, setYear] = useState("2026");
  const [month, setMonth] = useState("");

  const handleGenerate = async () => {
    try {
      const response = await generatePayroll(employeeId, year, month);

      alert("Payroll Generated");

      console.log(response.data);
    } catch (error) {
      alert(error.response?.data?.message);
    }
  };

  return (
    <div className="container mt-4">
      <h3>Generate Payroll</h3>

      <input
        className="form-control mb-2"
        placeholder="Employee ID"
        value={employeeId}
        onChange={(e) => setEmployeeId(e.target.value)}
      />

      <input
        className="form-control mb-2"
        placeholder="Year"
        value={year}
        onChange={(e) => setYear(e.target.value)}
      />

      <input
        className="form-control mb-2"
        placeholder="Month"
        value={month}
        onChange={(e) => setMonth(e.target.value)}
      />

      <button className="btn btn-primary" onClick={handleGenerate}>
        Generate Payroll
      </button>
    </div>
  );
};

export default GeneratePayroll;
