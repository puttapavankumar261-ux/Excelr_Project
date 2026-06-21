import { useEffect, useState } from "react";
import { getEmployees } from "../services/employeeService";

function EmployeeStep1({ formData, setFormData, errors = {}, nextStep }) {
  const [managers, setManagers] = useState([]);

  useEffect(() => {
    const loadManagers = async () => {
      try {
        const response = await getEmployees();
        setManagers(response.data || []);
      } catch (error) {
        console.error(error);
      }
    };

    loadManagers();
  }, []);

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const fieldClass = (field) =>
    `form-control mb-1 ${errors[field] ? "is-invalid" : ""}`;

  const renderError = (field) =>
    errors[field] ? (
      <div className="invalid-feedback d-block mb-2">{errors[field]}</div>
    ) : null;

  return (
    <div className="modal-card">
      <div className="modal-header">
        <h3>Step 1 of 3 - Employee Information</h3>
      </div>

      <div className="modal-body">
        <label className="form-label fw-semibold" htmlFor="employeename">
          Employee Name
        </label>
        <input
          id="employeename"
          className={fieldClass("employeename")}
          placeholder="Employee Name"
          name="employeename"
          value={formData.employeename}
          onChange={handleChange}
          required
        />
        {renderError("employeename")}

        <label className="form-label fw-semibold" htmlFor="employeeCode">
          Employee Code
        </label>
        <input
          id="employeeCode"
          className="form-control mb-3"
          placeholder="Employee Code"
          name="employeeCode"
          value={formData.employeeCode || ""}
          onChange={handleChange}
        />

        <label className="form-label fw-semibold" htmlFor="companyemail">
          Company Email
        </label>
        <input
          id="companyemail"
          type="email"
          className={fieldClass("companyemail")}
          placeholder="name@company.com"
          name="companyemail"
          value={formData.companyemail}
          onChange={handleChange}
          required
        />
        {renderError("companyemail")}

        <label className="form-label fw-semibold" htmlFor="phonenumber">
          Phone Number
        </label>
        <input
          id="phonenumber"
          className="form-control mb-3"
          placeholder="Phone Number"
          name="phonenumber"
          value={formData.phonenumber}
          onChange={handleChange}
        />

        <select
          className={fieldClass("role")}
          name="role"
          value={formData.role}
          onChange={handleChange}
          required
          aria-label="Select role"
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
        {renderError("role")}

        <select
          className={fieldClass("designation")}
          name="designation"
          value={formData.designation}
          onChange={handleChange}
          required
          aria-label="Select designation"
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
        {renderError("designation")}

        <select
          className={fieldClass("department")}
          name="department"
          value={formData.department}
          onChange={handleChange}
          required
          aria-label="Select department"
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
        {renderError("department")}

        <label className="form-label fw-semibold" htmlFor="joiningDate">
          Joining Date
        </label>
        <input
          id="joiningDate"
          type="date"
          className="form-control mb-3"
          name="joiningDate"
          value={formData.joiningDate}
          onChange={handleChange}
        />

        <select
          className={fieldClass("employmentType")}
          name="employmentType"
          value={formData.employmentType}
          onChange={handleChange}
          required
          aria-label="Select employment type"
        >
          <option value="">Select Employment Type</option>
          <option value="FULL_TIME">Full Time</option>
          <option value="PART_TIME">Part Time</option>
          <option value="CONTRACT">Contract</option>
          <option value="INTERN">Intern</option>
          <option value="FREELANCER">Freelancer</option>
        </select>
        {renderError("employmentType")}

        <select
          className="form-control mb-3"
          name="employmentStatus"
          value={formData.employmentStatus}
          onChange={handleChange}
          aria-label="Select employment status"
        >
          <option value="ACTIVE">Active</option>
          <option value="NOTICE_PERIOD">Notice Period</option>
          <option value="RESIGNED">Resigned</option>
          <option value="TERMINATED">Terminated</option>
          <option value="ABSCONDED">Absconded</option>
        </select>

        <label className="form-label fw-semibold" htmlFor="workLocation">
          Work Location
        </label>
        <input
          id="workLocation"
          className="form-control mb-3"
          placeholder="Work Location"
          name="workLocation"
          value={formData.workLocation}
          onChange={handleChange}
        />

        <select
          className="form-control mb-3"
          name="managerId"
          value={formData.managerId}
          onChange={handleChange}
          aria-label="Select manager"
        >
          <option value="">Select Manager</option>
          {managers.map((manager) => (
            <option key={manager.employeeid} value={manager.employeeid}>
              {manager.employeename}
            </option>
          ))}
        </select>
      </div>

      <div className="d-flex justify-content-end">
        <button type="button" className="btn btn-primary" onClick={nextStep}>
          Next
        </button>
      </div>
    </div>
  );
}

export default EmployeeStep1;
