import { useEffect, useState } from "react";

import { getAllEmployees } from "../../employee/services/employeeService";

import { getAllShifts, assignShift } from "../services/shiftService";

function AssignShift() {
  const [employees, setEmployees] = useState([]);

  const [shifts, setShifts] = useState([]);

  const [employeeId, setEmployeeId] = useState("");

  const [shiftId, setShiftId] = useState("");

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const empResponse = await getAllEmployees();

      const shiftResponse = await getAllShifts();

      setEmployees(empResponse.data || []);

      setShifts(shiftResponse.data || []);
    } catch (error) {
      console.error(error);
    }
  };

  const handleAssign = async () => {
    try {
      if (!employeeId || !shiftId) {
        alert("Select Employee and Shift");
        return;
      }

      await assignShift(employeeId, shiftId);

      alert("Shift Assigned Successfully");

      setEmployeeId("");
      setShiftId("");
    } catch (error) {
      console.error(error);

      alert(error.response?.data?.message || "Failed to assign shift");
    }
  };

  return (
    <div className="container-fluid">
      <h2 className="mb-4">Assign Shift</h2>

      <div className="card shadow">
        <div className="card-body">
          <div className="mb-3">
            <label className="form-label">Employee</label>

            <select
              className="form-select"
              value={employeeId}
              onChange={(e) => setEmployeeId(e.target.value)}
            >
              <option value="">Select Employee</option>

              {employees.map((emp) => (
                <option key={emp.employeeid} value={emp.employeeid}>
                  {emp.employeename}
                  {" - "}
                  {emp.employeeid}
                </option>
              ))}
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label">Shift</label>

            <select
              className="form-select"
              value={shiftId}
              onChange={(e) => setShiftId(e.target.value)}
            >
              <option value="">Select Shift</option>

              {shifts.map((shift) => (
                <option key={shift.shiftid} value={shift.shiftid}>
                  {shift.shiftName}
                  {" ("}
                  {shift.startTime}
                  {" - "}
                  {shift.endTime}
                  {")"}
                </option>
              ))}
            </select>
          </div>

          <button className="btn btn-primary" onClick={handleAssign}>
            Assign Shift
          </button>
        </div>
      </div>
    </div>
  );
}

export default AssignShift;
