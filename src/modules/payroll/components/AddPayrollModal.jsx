import { useEffect, useMemo, useState } from "react";
import { FiX } from "react-icons/fi";

import { ErrorBanner } from "../../../components/ui/EnterpriseUI";
import { formatCurrency } from "../../../components/ui/formatters";
import { getApiErrorMessage } from "../../../api/errorUtils";
import { getEmployees } from "../../employee/services/employeeService";
import { getAllTaxSlabs } from "../../tax/services/taxService";
import { savePayroll } from "../services/payrollService";

const moneyFields = [
  ["basicSalary", "Basic Salary"],
  ["hra", "HRA"],
  ["allowances", "Allowances"],
  ["bonus", "Bonus"],
  ["deductions", "Deductions"],
  ["pf", "PF"],
  ["esi", "ESI"],
  ["professionalTax", "Professional Tax"],
  ["incomeTax", "Income Tax"],
];

function AddPayrollModal({ onClose, onSuccess }) {
  const [employees, setEmployees] = useState([]);
  const [taxSlabs, setTaxSlabs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [payroll, setPayroll] = useState({
    employeeId: "",
    taxId: "",
    basicSalary: "",
    hra: "",
    allowances: "",
    bonus: "",
    deductions: "",
    pf: "",
    esi: "",
    professionalTax: "",
    incomeTax: "",
    payrollMonth: "",
  });

  const loadData = async () => {
    try {
      setLoading(true);
      setError("");
      const [empResponse, taxResponse] = await Promise.all([
        getEmployees(),
        getAllTaxSlabs(),
      ]);
      setEmployees(empResponse.data || []);
      setTaxSlabs(taxResponse.data || []);
    } catch (loadError) {
      console.error(loadError);
      setError(getApiErrorMessage(loadError, "Unable to load payroll form data."));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const totals = useMemo(() => {
    const gross =
      Number(payroll.basicSalary || 0) +
      Number(payroll.hra || 0) +
      Number(payroll.allowances || 0) +
      Number(payroll.bonus || 0);
    const deductions =
      Number(payroll.deductions || 0) +
      Number(payroll.pf || 0) +
      Number(payroll.esi || 0) +
      Number(payroll.professionalTax || 0) +
      Number(payroll.incomeTax || 0);

    return { gross, deductions, net: Math.max(gross - deductions, 0) };
  }, [payroll]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setPayroll((current) => ({
      ...current,
      [name]: value,
    }));
  };

  const validateForm = () => {
    if (!payroll.employeeId) return "Please select an employee.";
    if (!payroll.taxId) return "Please select a tax slab.";
    if (!payroll.payrollMonth) return "Please select a payroll month.";
    if (totals.gross <= 0) return "Gross salary must be greater than zero.";
    return "";
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const validationError = validateForm();

    if (validationError) {
      setError(validationError);
      return;
    }

    const payload = {
      employee: {
        employeeid: Number(payroll.employeeId),
      },
      taxSlab: {
        taxid: Number(payroll.taxId),
      },
      basicSalary: Number(payroll.basicSalary || 0),
      hra: Number(payroll.hra || 0),
      allowances: Number(payroll.allowances || 0),
      bonus: Number(payroll.bonus || 0),
      deductions: Number(payroll.deductions || 0),
      pf: Number(payroll.pf || 0),
      esi: Number(payroll.esi || 0),
      professionalTax: Number(payroll.professionalTax || 0),
      incomeTax: Number(payroll.incomeTax || 0),
      grossSalary: totals.gross,
      netSalary: totals.net,
      payrollMonth: payroll.payrollMonth,
    };

    try {
      setSaving(true);
      setError("");
      await savePayroll(payload);
      await onSuccess();
      onClose();
    } catch (saveError) {
      console.error(saveError);
      setError(getApiErrorMessage(saveError, "Payroll save failed."));
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="enterprise-modal-overlay" role="presentation">
      <div className="enterprise-modal" role="dialog" aria-modal="true">
        <div className="enterprise-modal-header">
          <div>
            <h3>Create Payroll</h3>
            <p className="mb-0 text-muted">Generate a payroll run from employee and tax data.</p>
          </div>
          <button
            type="button"
            className="enterprise-close"
            onClick={onClose}
            aria-label="Close payroll dialog"
          >
            <FiX />
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="enterprise-modal-body">
            <ErrorBanner message={error} onRetry={loadData} />

            <div className="enterprise-form-grid">
              <label className="enterprise-form-field">
                <span>Employee</span>
                <select
                  className="form-select"
                  name="employeeId"
                  value={payroll.employeeId}
                  onChange={handleChange}
                  disabled={loading}
                  required
                >
                  <option value="">Select Employee</option>
                  {employees.map((emp) => (
                    <option key={emp.employeeid} value={emp.employeeid}>
                      {emp.employeename}
                    </option>
                  ))}
                </select>
              </label>

              <label className="enterprise-form-field">
                <span>Tax Slab</span>
                <select
                  className="form-select"
                  name="taxId"
                  value={payroll.taxId}
                  onChange={handleChange}
                  disabled={loading}
                  required
                >
                  <option value="">Select Tax Slab</option>
                  {taxSlabs.map((tax) => (
                    <option key={tax.taxid} value={tax.taxid}>
                      {tax.slabName}
                    </option>
                  ))}
                </select>
              </label>

              {moneyFields.map(([name, label]) => (
                <label className="enterprise-form-field" key={name}>
                  <span>{label}</span>
                  <input
                    className="form-control"
                    type="number"
                    min="0"
                    step="0.01"
                    name={name}
                    value={payroll[name]}
                    onChange={handleChange}
                    placeholder="0"
                  />
                </label>
              ))}

              <label className="enterprise-form-field">
                <span>Payroll Month</span>
                <input
                  type="date"
                  className="form-control"
                  name="payrollMonth"
                  value={payroll.payrollMonth}
                  onChange={handleChange}
                  required
                />
              </label>
            </div>

            <section className="enterprise-grid mt-4">
              <article className="enterprise-metric metric-blue">
                <span className="enterprise-metric-icon">G</span>
                <div>
                  <span>Gross</span>
                  <strong>{formatCurrency(totals.gross)}</strong>
                  <small>Salary components</small>
                </div>
              </article>
              <article className="enterprise-metric metric-red">
                <span className="enterprise-metric-icon">D</span>
                <div>
                  <span>Deductions</span>
                  <strong>{formatCurrency(totals.deductions)}</strong>
                  <small>Tax and statutory deductions</small>
                </div>
              </article>
              <article className="enterprise-metric metric-green">
                <span className="enterprise-metric-icon">N</span>
                <div>
                  <span>Net Pay</span>
                  <strong>{formatCurrency(totals.net)}</strong>
                  <small>Projected payable amount</small>
                </div>
              </article>
            </section>
          </div>

          <div className="enterprise-modal-footer">
            <button
              type="button"
              className="btn btn-outline-secondary"
              onClick={onClose}
              disabled={saving}
            >
              Cancel
            </button>
            <button type="submit" className="btn btn-success" disabled={saving}>
              {saving ? "Saving..." : "Save Payroll"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AddPayrollModal;
