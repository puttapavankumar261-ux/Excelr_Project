import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  FiBriefcase,
  FiMapPin,
  FiRefreshCw,
  FiUserCheck,
  FiUserPlus,
  FiUsers,
} from "react-icons/fi";

import {
  EmptyState,
  EnterprisePage,
  ErrorBanner,
  LoadingState,
  MetricCard,
  MiniBarChart,
  PageHero,
  SearchField,
} from "../../../components/ui/EnterpriseUI";
import EmployeeTable from "../components/EmployeeTable";
import { deleteEmployee, getEmployees } from "../services/employeeService";

function EmployeeList() {
  const navigate = useNavigate();

  const [employees, setEmployees] = useState([]);
  const [search, setSearch] = useState("");
  const [departmentFilter, setDepartmentFilter] = useState("ALL");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [loading, setLoading] = useState(true);
  const [actionLoadingId, setActionLoadingId] = useState(null);
  const [error, setError] = useState("");

  const fetchEmployees = async () => {
    try {
      setLoading(true);
      setError("");
      const response = await getEmployees();
      setEmployees(response.data || []);
    } catch (loadError) {
      console.error("Employee Load Error:", loadError);
      setError("Unable to load employees. Please retry or check the backend.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEmployees();
  }, []);

  const departments = useMemo(
    () =>
      [...new Set(employees.map((employee) => employee.department).filter(Boolean))].sort(),
    [employees],
  );

  const departmentSummary = useMemo(() => {
    const counts = employees.reduce((acc, employee) => {
      const department = employee.department || "Unknown";
      acc[department] = (acc[department] || 0) + 1;
      return acc;
    }, {});

    return Object.entries(counts)
      .map(([label, value]) => ({ label, value }))
      .sort((a, b) => b.value - a.value)
      .slice(0, 5);
  }, [employees]);

  const filteredEmployees = useMemo(() => {
    const query = search.trim().toLowerCase();

    return employees.filter((employee) => {
      const searchable = [
        employee.employeename,
        employee.employeeName,
        employee.companyemail,
        employee.department,
        employee.designation,
        employee.role,
        employee.workLocation,
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase();

      const searchMatch = !query || searchable.includes(query);
      const departmentMatch =
        departmentFilter === "ALL" || employee.department === departmentFilter;
      const statusMatch =
        statusFilter === "ALL" ||
        (employee.employmentStatus || "ACTIVE") === statusFilter;

      return searchMatch && departmentMatch && statusMatch;
    });
  }, [departmentFilter, employees, search, statusFilter]);

  const activeCount = employees.filter(
    (employee) => (employee.employmentStatus || "ACTIVE") === "ACTIVE",
  ).length;

  const handleDelete = async (id) => {
    const employee = employees.find((item) => item.employeeid === id);
    const name = employee?.employeename || "this employee";

    if (!window.confirm(`Delete ${name}? This action cannot be undone.`)) {
      return;
    }

    try {
      setActionLoadingId(id);
      setError("");
      await deleteEmployee(id);
      setEmployees((current) => current.filter((item) => item.employeeid !== id));
    } catch (deleteError) {
      console.error(deleteError);
      setError(
        deleteError.response?.data?.message ||
          "Failed to delete employee. Please try again.",
      );
    } finally {
      setActionLoadingId(null);
    }
  };

  return (
    <EnterprisePage>
      <PageHero
        eyebrow="Employee Profile"
        title="Employee Management"
        description="Search the workforce, review employee health, and manage profile records from a single responsive workspace."
        icon={FiUsers}
        meta={
          <>
            <span>{filteredEmployees.length} visible records</span>
            <span>{departments.length} departments</span>
          </>
        }
        actions={
          <>
            <button
              type="button"
              className="btn btn-light"
              onClick={fetchEmployees}
              disabled={loading}
            >
              <FiRefreshCw /> Refresh
            </button>
            <button
              type="button"
              className="btn btn-warning"
              onClick={() => navigate("/admin/employees/add")}
            >
              <FiUserPlus /> Add Employee
            </button>
          </>
        }
      />

      <ErrorBanner message={error} onRetry={fetchEmployees} />

      <section className="enterprise-grid">
        <MetricCard
          label="Total Employees"
          value={employees.length}
          helper="All workforce records"
          icon={FiUsers}
          tone="blue"
        />
        <MetricCard
          label="Active Employees"
          value={activeCount}
          helper={`${employees.length - activeCount} inactive or notice`}
          icon={FiUserCheck}
          tone="green"
        />
        <MetricCard
          label="Departments"
          value={departments.length}
          helper="Active team groups"
          icon={FiBriefcase}
          tone="gold"
        />
        <MetricCard
          label="Locations"
          value={
            new Set(employees.map((employee) => employee.workLocation).filter(Boolean))
              .size
          }
          helper="Work locations tracked"
          icon={FiMapPin}
          tone="teal"
        />
      </section>

      <section className="enterprise-panel">
        <div className="enterprise-panel-header">
          <div>
            <h2>Employee Directory</h2>
            <p>Filter by name, department, designation, role, status, or location.</p>
          </div>
        </div>

        <div className="enterprise-toolbar">
          <SearchField
            value={search}
            onChange={setSearch}
            placeholder="Search employees, emails, roles..."
          />

          <select
            className="form-select"
            value={departmentFilter}
            onChange={(event) => setDepartmentFilter(event.target.value)}
            aria-label="Filter employees by department"
          >
            <option value="ALL">All Departments</option>
            {departments.map((department) => (
              <option key={department} value={department}>
                {department}
              </option>
            ))}
          </select>

          <select
            className="form-select"
            value={statusFilter}
            onChange={(event) => setStatusFilter(event.target.value)}
            aria-label="Filter employees by status"
          >
            <option value="ALL">All Statuses</option>
            <option value="ACTIVE">Active</option>
            <option value="NOTICE_PERIOD">Notice Period</option>
            <option value="RESIGNED">Resigned</option>
            <option value="TERMINATED">Terminated</option>
          </select>
        </div>

        {loading ? (
          <LoadingState label="Loading employees..." />
        ) : employees.length === 0 ? (
          <EmptyState
            title="No employee records yet"
            message="Create the first employee to begin building the workforce directory."
          />
        ) : (
          <div className="enterprise-table-wrap" aria-busy={!!actionLoadingId}>
            <EmployeeTable
              employees={filteredEmployees}
              onView={(id) => navigate(`/admin/employees/view/${id}`)}
              onEdit={(id) => navigate(`/admin/employees/edit/${id}`)}
              onDelete={handleDelete}
            />
          </div>
        )}
      </section>

      {departmentSummary.length > 0 && (
        <section className="enterprise-panel">
          <div className="enterprise-panel-header">
            <div>
              <h3>Department Distribution</h3>
              <p>Live chart based on the currently loaded employee records.</p>
            </div>
          </div>
          <MiniBarChart items={departmentSummary} valueLabel="employees" />
        </section>
      )}
    </EnterprisePage>
  );
}

export default EmployeeList;
