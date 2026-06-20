
import { useEffect, useState } from "react";

import { getEmployees } from "../../employee/services/employeeService";
import { getAllTaxSlabs } from "../../tax/services/taxService";

import { saveSalaryStructure } from "../services/salaryStructureService";

function AddSalaryStructureModal({ onClose, onSuccess }) {
  const [employees, setEmployees] = useState([]);
  const [taxSlabs, setTaxSlabs] = useState([]);

  const [formData, setFormData] = useState({
    employeeId: "",
    taxId: "",
    basicSalary: "",
    hra: "",
    allowances: "",
    pf: "",
    esi: "",
    professionalTax: "",
  });

  useEffect(() => {
    const loadData = async () => {
      try {
        const empRes = await getEmployees();
        setEmployees(empRes.data || []);

        const taxRes = await getAllTaxSlabs();
        setTaxSlabs(taxRes.data || []);
      } catch (error) {
        console.error(error);
      }
    };

    loadData();
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const payload = {
        employee: {
          employeeid: Number(formData.employeeId),
        },

        taxSlab: {
          taxid: Number(formData.taxId),
        },

        basicSalary: Number(formData.basicSalary),

        hra: Number(formData.hra),

        allowances: Number(formData.allowances),

        pf: Number(formData.pf),

        esi: Number(formData.esi),

        professionalTax: Number(formData.professionalTax),

        active: true,
      };

      await saveSalaryStructure(payload);

      alert("Salary Structure Saved Successfully");

      onSuccess();
      onClose();
    } catch (error) {
      console.error(error);

      alert(error?.response?.data?.message || "Error saving salary structure");
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <div className="modal-header">
          <h3>Add Salary Structure</h3>

          <button className="btn-close" onClick={onClose}>
            &times;
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <select
            className="form-control mb-2"
            name="employeeId"
            onChange={handleChange}
            required
          >
            <option value="">Select Employee</option>

            {employees.map((emp) => (
              <option key={emp.employeeid} value={emp.employeeid}>
                {emp.employeename}
              </option>
            ))}
          </select>

          <select
            className="form-control mb-2"
            name="taxId"
            onChange={handleChange}
            required
          >
            <option value="">Select Tax Slab</option>

            {taxSlabs.map((tax) => (
              <option key={tax.taxid} value={tax.taxid}>
                {tax.slabName}
              </option>
            ))}
          </select>

          <input
            className="form-control mb-2"
            placeholder="Basic Salary"
            name="basicSalary"
            onChange={handleChange}
            required
          />

          <input
            className="form-control mb-2"
            placeholder="HRA"
            name="hra"
            onChange={handleChange}
            required
          />

          <input
            className="form-control mb-2"
            placeholder="Allowances"
            name="allowances"
            onChange={handleChange}
            required
          />

          <input
            className="form-control mb-2"
            placeholder="PF"
            name="pf"
            onChange={handleChange}
            required
          />

          <input
            className="form-control mb-2"
            placeholder="ESI"
            name="esi"
            onChange={handleChange}
            required
          />

          <input
            className="form-control mb-3"
            placeholder="Professional Tax"
            name="professionalTax"
            onChange={handleChange}
            required
          />

          <button className="btn btn-success w-100" type="submit">
            Save Salary Structure
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddSalaryStructureModal;
