function EmployeeStep3({
  formData,
  setFormData,
  handleSubmit,
  prevStep,
  errors = {},
  submitting = false,
}) {
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="modal-card">
      <h3>Step 3 - Bank Information</h3>

      <input
        className="form-control mb-2"
        placeholder="Bank Name"
        name="bankName"
        value={formData.bankName || ""}
        onChange={handleChange}
      />

      <input
        className="form-control mb-2"
        placeholder="Account Holder Name"
        name="accountHolderName"
        value={formData.accountHolderName || ""}
        onChange={handleChange}
      />

      <input
        className="form-control mb-2"
        placeholder="Account Number"
        name="accountNumber"
        value={formData.accountNumber || ""}
        onChange={handleChange}
      />

      <input
        className={`form-control mb-1 ${errors.ifscCode ? "is-invalid" : ""}`}
        placeholder="IFSC Code"
        name="ifscCode"
        value={formData.ifscCode || ""}
        onChange={handleChange}
      />
      {errors.ifscCode && (
        <div className="invalid-feedback d-block mb-2">{errors.ifscCode}</div>
      )}

      <div className="d-flex justify-content-between">
        <button
          className="btn btn-secondary"
          onClick={prevStep}
          disabled={submitting}
        >
          Back
        </button>

        <button
          className="btn btn-success"
          onClick={handleSubmit}
          disabled={submitting}
        >
          {submitting ? "Creating Employee..." : "Complete Onboarding"}
        </button>
      </div>
    </div>
  );
}

export default EmployeeStep3;
