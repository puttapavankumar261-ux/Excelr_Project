import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import EmployeeTable from "../components/EmployeeTable";

import { getEmployees, deleteEmployee } from "../services/employeeService";

import "../styles/Employee.css";

function EmployeeList() {
  const navigate = useNavigate();

  const [employees, setEmployees] = useState([]);
  const [search, setSearch] = useState("");

  const fetchEmployees = async () => {
    try {
      const response = await getEmployees();

      setEmployees(response.data || []);
    } catch (error) {
      console.error("Employee Load Error:", error);
    }
  };

  useEffect(() => {
    fetchEmployees();
  }, []);

  const handleDelete = async (id) => {
    try {
      const confirmDelete = window.confirm(
        "Are you sure you want to delete this employee?",
      );

      if (!confirmDelete) return;

      await deleteEmployee(id);

      alert("Employee deleted successfully");

      fetchEmployees();
    } catch (error) {
      console.error(error);

      alert(error.response?.data?.message || "Failed to delete employee");
    }
  };

  const filteredEmployees = employees.filter((emp) =>
    (emp.employeename || "").toLowerCase().includes(search.toLowerCase()),
  );

  return (
    <div className="container-fluid py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">Employee Management</h2>

        <button
          className="btn btn-primary"
          onClick={() => navigate("/admin/employees/add")}
        >
          Add Employee
        </button>
      </div>

      <div className="card shadow-sm border-0">
        <div className="card-body">
          <input
            type="text"
            className="form-control mb-4"
            placeholder="Search Employee..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />

          <EmployeeTable
            employees={filteredEmployees}
            onView={(id) => navigate(`/admin/employees/view/${id}`)}
            onEdit={(id) => navigate(`/admin/employees/edit/${id}`)}
            onDelete={handleDelete}
          />
        </div>
      </div>
    </div>
  );
}

export default EmployeeList;
