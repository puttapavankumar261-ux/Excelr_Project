import { useEffect, useState } from "react";
import { getEmployees } from "../services/employeeService";

function EmployeeStep1({ formData, setFormData, nextStep }) {
  const [managers, setManagers] = useState([]);

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
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="modal-card">
      <div className="modal-header">
        <h3>Step 1 of 4 - Employee Information</h3>
      </div>

      <div className="modal-body">
        <input
          className="form-control mb-3"
          placeholder="Employee Name"
          name="employeename"
          value={formData.employeename}
          onChange={handleChange}
          required
        />

        <input
          className="form-control mb-3"
          placeholder="Employee Code"
          name="employeeCode"
          value={formData.employeeCode}
          onChange={handleChange}
        />

        <input
          className="form-control mb-3"
          placeholder="Company Email"
          name="companyemail"
          value={formData.companyemail}
          onChange={handleChange}
        />

        <input
          className="form-control mb-3"
          placeholder="Company Email"
          value={formData.companyemail}
          onChange={(e) =>
            setFormData({
              ...formData,
              companyemail: e.target.value,
            })
          }
          required
        />
        
        <input
          className="form-control mb-3"
          placeholder="Phone Number"
          name="phonenumber"
          value={formData.phonenumber}
          onChange={handleChange}
        />

        <select
          className="form-control mb-3"
          name="role"
          value={formData.role}
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
          value={formData.designation}
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
          value={formData.department}
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
          value={formData.joiningDate}
          onChange={handleChange}
        />

        <select
          className="form-control mb-3"
          name="employmentType"
          value={formData.employmentType}
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
          value={formData.employmentStatus}
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
          value={formData.workLocation}
          onChange={handleChange}
        />

        <select
          className="form-control mb-3"
          name="managerId"
          value={formData.managerId}
          onChange={handleChange}
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
        <button className="btn btn-primary" onClick={nextStep}>
          Next →
        </button>
      </div>
    </div>
  );
}

export default EmployeeStep1;
