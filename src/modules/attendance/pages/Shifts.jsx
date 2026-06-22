import { useEffect, useState } from "react";
import { getAllShifts, deleteShift } from "../services/shiftService";

import {
  getEmployees,
  assignShift,
} from "../../employee/services/employeeService";

function Shifts() {
  const [shifts, setShifts] = useState([]);
  const [employees, setEmployees] = useState([]);

  const [selectedShift, setSelectedShift] = useState(null);
  const [selectedEmployee, setSelectedEmployee] = useState("");

  const [showAssignModal, setShowAssignModal] = useState(false);

  useEffect(() => {
    loadShifts();
    loadEmployees();
  }, []);

  const loadShifts = async () => {
    try {
      const response = await getAllShifts();

      setShifts(response.data || []);
    } catch (error) {
      console.error(error);
    }
  };

  const loadEmployees = async () => {
    try {
      const response = await getEmployees();

      setEmployees(response.data || []);
    } catch (error) {
      console.error(error);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this shift?")) return;

    try {
      await deleteShift(id);

      loadShifts();
    } catch (error) {
      console.error(error);
    }
  };

  const handleAssign = async () => {
    try {
      if (!selectedEmployee) {
        alert("Please select employee");
        return;
      }

      await assignShift(selectedEmployee, selectedShift.shiftid);

      alert("Shift Assigned Successfully");

      setShowAssignModal(false);

      setSelectedEmployee("");
    } catch (error) {
      console.error(error);

      alert(error.response?.data?.message || "Failed to assign shift");
    }
  };

  return (
    <div className="container-fluid">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Shift Management</h2>
      </div>

      <table className="table table-bordered table-hover">
        <thead className="table-dark">
          <tr>
            <th>ID</th>
            <th>Shift Name</th>
            <th>Type</th>
            <th>Start</th>
            <th>End</th>
            <th>Work Hours</th>
            <th>Late Grace</th>
            <th>Active</th>
            <th width="250">Actions</th>
          </tr>
        </thead>

        <tbody>
          {shifts.length > 0 ? (
            shifts.map((shift) => (
              <tr key={shift.shiftid}>
                <td>{shift.shiftid}</td>
                <td>{shift.shiftName}</td>
                <td>{shift.shiftType}</td>
                <td>{shift.startTime}</td>
                <td>{shift.endTime}</td>
                <td>{shift.minWorkHours}</td>
                <td>{shift.lateGraceMinutes} min</td>
                <td>{shift.active ? "Yes" : "No"}</td>

                <td>
                  <button className="btn btn-sm btn-primary me-2">Edit</button>

                  <button
                    className="btn btn-sm btn-success me-2"
                    onClick={() => {
                      setSelectedShift(shift);
                      setShowAssignModal(true);
                    }}
                  >
                    Assign Employee
                  </button>

                  <button
                    className="btn btn-sm btn-danger"
                    onClick={() => handleDelete(shift.shiftid)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="9" className="text-center">
                No Shifts Found
              </td>
            </tr>
          )}
        </tbody>
      </table>

      {/* Assign Shift Modal */}

      {showAssignModal && (
        <div
          className="modal d-block"
          style={{
            backgroundColor: "rgba(0,0,0,0.5)",
          }}
        >
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Assign Shift</h5>

                <button
                  className="btn-close"
                  onClick={() => setShowAssignModal(false)}
                />
              </div>

              <div className="modal-body">
                <div className="mb-3">
                  <label className="form-label">Shift</label>

                  <input
                    type="text"
                    className="form-control"
                    value={selectedShift?.shiftName || ""}
                    disabled
                  />
                </div>

                <div className="mb-3">
                  <label className="form-label">Employee</label>

                  <select
                    className="form-select"
                    value={selectedEmployee}
                    onChange={(e) => setSelectedEmployee(e.target.value)}
                  >
                    <option value="">Select Employee</option>

                    {employees.map((emp) => (
                      <option key={emp.employeeid} value={emp.employeeid}>
                        {emp.employeename}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="modal-footer">
                <button
                  className="btn btn-secondary"
                  onClick={() => setShowAssignModal(false)}
                >
                  Cancel
                </button>

                <button className="btn btn-success" onClick={handleAssign}>
                  Assign
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Shifts;
