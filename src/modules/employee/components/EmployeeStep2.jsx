function EmployeeStep2({ formData, setFormData, nextStep, prevStep }) {
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="modal-card">
      <h3>Step 2 - Salary Package Details</h3>

      <input
        className="form-control mb-3"
        placeholder="Basic Salary"
        name="basicSalary"
        value={formData.basicSalary || ""}
        onChange={handleChange}
      />

      <input
        className="form-control mb-3"
        placeholder="HRA"
        name="hra"
        value={formData.hra || ""}
        onChange={handleChange}
      />

      <input
        className="form-control mb-3"
        placeholder="Special Allowance"
        name="allowances"
        value={formData.allowances || ""}
        onChange={handleChange}
      />

      <input
        className="form-control mb-3"
        placeholder="Bonus"
        name="bonus"
        value={formData.bonus || ""}
        onChange={handleChange}
      />

      <input
        className="form-control mb-3"
        placeholder="PF Contribution"
        name="pf"
        value={formData.pf || ""}
        onChange={handleChange}
      />

      <input
        className="form-control mb-3"
        placeholder="Professional Tax"
        name="professionalTax"
        value={formData.professionalTax || ""}
        onChange={handleChange}
      />

      <input
        className="form-control mb-3"
        placeholder="Medical Allowance"
        name="medicalAllowance"
        value={formData.medicalAllowance || ""}
        onChange={handleChange}
      />

      <input
        className="form-control mb-3"
        placeholder="Annual CTC"
        name="annualCtc"
        value={formData.annualCtc || ""}
        onChange={handleChange}
      />

      <div className="d-flex justify-content-between">
        <button className="btn btn-secondary" onClick={prevStep}>
          Back
        </button>

        <button className="btn btn-primary" onClick={nextStep}>
          Next
        </button>
      </div>
    </div>
  );
}

export default EmployeeStep2;
