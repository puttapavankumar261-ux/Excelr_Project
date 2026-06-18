import { useState, useEffect } from "react";

import { addEmployee, getEmployees } from "../services/employeeService";

function AddEmployeeModal({ onClose, onSuccess }) {
  const [managers, setManagers] = useState([]);

  const [employee, setEmployee] = useState({
    employeename: "",
    role: "",
    designation: "",
    department: "",
    joiningDate: "",
    employmentType: "",
    employmentStatus: "ACTIVE",
    workLocation: "",
    managerId: "",

    bankName: "",
    accountHolderName: "",
    accountNumber: "",
    ifscCode: "",
  });

  useEffect(() => {
    loadManagers();
  }, []);

  const loadManagers = async () => {
    try {
      const response = await getEmployees();
      setManagers(response.data || []);
    } catch (error) {
      console.error(error);
    }
  };

  const handleChange = (e) => {
    setEmployee({
      ...employee,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const payload = {
        employeename: employee.employeename,
        role: employee.role,
        designation: employee.designation,
        department: employee.department,
        joiningDate: employee.joiningDate,
        employmentType: employee.employmentType,
        employmentStatus: employee.employmentStatus,
        workLocation: employee.workLocation,

        bankName: employee.bankName,
        accountHolderName: employee.accountHolderName,
        accountNumber: employee.accountNumber,
        ifscCode: employee.ifscCode,

        manager: employee.managerId
          ? {
              employeeid: parseInt(employee.managerId),
            }
          : null,
      };

      await addEmployee(payload);

      onSuccess();
      onClose();
    } catch (error) {
      console.error("Employee Save Error:", error);

      console.log("Status:", error.response?.status);
      console.log("Response:", error.response?.data);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <div className="modal-header">
          <h3>Add Employee</h3>

          <button className="btn-close" onClick={onClose}>
            ✕
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <input
            className="form-control mb-3"
            placeholder="Employee Name"
            name="employeename"
            value={employee.employeename}
            onChange={handleChange}
            required
          />

          <select
            className="form-control mb-3"
            name="role"
            value={employee.role}
            onChange={handleChange}
            required
          >
            <option value="">Select Role</option>
            <option value="ADMIN">Admin</option>
            <option value="HR">HR</option>
            <option value="MANAGER">Manager</option>
            <option value="TEAM_LEAD">Team Lead</option>
            <option value="EMPLOYEE">Employee</option>
            <option value="PROJECT_MANAGER">Project Manager</option>
            <option value="RECRUITMENT_LEAD">Recruitment Lead</option>
            <option value="TEACHER_TRAINER">Teacher Trainer</option>
            <option value="FINANCE">Finance</option>
            <option value="PAYROLL_ADMIN">Payroll Admin</option>
          </select>

          <select
            className="form-control mb-3"
            name="designation"
            value={employee.designation}
            onChange={handleChange}
            required
          >
            <option value="">Select Designation</option>
            <option value="INTERN">Intern</option>
            <option value="TRAINEE">Trainee</option>
            <option value="ASSOCIATE">Associate</option>
            <option value="SENIOR_ASSOCIATE">Senior Associate</option>
            <option value="SME">SME</option>
            <option value="TRAINER">Trainer</option>
            <option value="TEAM_LEAD">Team Lead</option>
            <option value="ASSISTANT_MANAGER">Assistant Manager</option>
            <option value="MANAGER">Manager</option>
            <option value="SENIOR_MANAGER">Senior Manager</option>
            <option value="DIRECTOR">Director</option>
            <option value="VICE_PRESIDENT">Vice President</option>
            <option value="C_LEVEL_EXECUTIVE">C Level Executive</option>
            <option value="CEO">CEO</option>
            <option value="MANAGING_DIRECTOR">Managing Director</option>
            <option value="CHAIRMAN">Chairman</option>
          </select>

          <select
            className="form-control mb-3"
            name="department"
            value={employee.department}
            onChange={handleChange}
            required
          >
            <option value="">Select Department</option>
            <option value="HR">HR</option>
            <option value="SALES">Sales</option>
            <option value="CUSTOMER_SERVICE">Customer Service</option>
            <option value="SOFTWARE">Software</option>
            <option value="FINANCE">Finance</option>
            <option value="OPERATIONS">Operations</option>
            <option value="RECRUITMENT">Recruitment</option>
            <option value="TRAINING">Training</option>
            <option value="ADMIN">Admin</option>
            <option value="MANAGEMENT">Management</option>
          </select>

          <input
            type="date"
            className="form-control mb-3"
            name="joiningDate"
            value={employee.joiningDate}
            onChange={handleChange}
          />

          <select
            className="form-control mb-3"
            name="employmentType"
            value={employee.employmentType}
            onChange={handleChange}
            required
          >
            <option value="">Select Employment Type</option>
            <option value="FULL_TIME">Full Time</option>
            <option value="PART_TIME">Part Time</option>
            <option value="CONTRACT">Contract</option>
            <option value="INTERN">Intern</option>
            <option value="FREELANCER">Freelancer</option>
          </select>

          <select
            className="form-control mb-3"
            name="employmentStatus"
            value={employee.employmentStatus}
            onChange={handleChange}
          >
            <option value="ACTIVE">Active</option>
            <option value="NOTICE_PERIOD">Notice Period</option>
            <option value="RESIGNED">Resigned</option>
            <option value="TERMINATED">Terminated</option>
            <option value="ABSCONDED">Absconded</option>
          </select>

          <input
            className="form-control mb-3"
            placeholder="Work Location"
            name="workLocation"
            value={employee.workLocation}
            onChange={handleChange}
          />

          <div className="border rounded p-3 mb-3 bg-light">
            <h5 className="mb-3">Bank Information</h5>

            <input
              className="form-control mb-3"
              placeholder="Bank Name"
              name="bankName"
              value={employee.bankName}
              onChange={handleChange}
            />

            <input
              className="form-control mb-3"
              placeholder="Account Holder Name"
              name="accountHolderName"
              value={employee.accountHolderName}
              onChange={handleChange}
            />

            <input
              className="form-control mb-3"
              placeholder="Account Number"
              name="accountNumber"
              value={employee.accountNumber}
              onChange={handleChange}
            />

            <input
              className="form-control mb-3"
              placeholder="IFSC Code"
              name="ifscCode"
              value={employee.ifscCode}
              onChange={handleChange}
            />
          </div>

          <select
            className="form-control mb-3"
            name="managerId"
            value={employee.managerId}
            onChange={handleChange}
          >
            <option value="">Select Manager</option>

            {managers.map((manager) => (
              <option key={manager.employeeid} value={manager.employeeid}>
                {manager.employeename}
              </option>
            ))}
          </select>

          <button className="btn btn-success w-100" type="submit">
            Save Employee
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddEmployeeModal;
