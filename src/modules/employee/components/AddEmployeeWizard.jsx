import { useState } from "react";

import EmployeeStep1 from "./EmployeeStep1";
import EmployeeStep2 from "./EmployeeStep2";
import EmployeeStep3 from "./EmployeeStep3";

import { onboardEmployee } from "../services/employeeService";

function AddEmployeeWizard({ onClose, onSuccess }) {
  const [step, setStep] = useState(1);

  const [formData, setFormData] = useState({
    employeename: "",
    companyemail: "",
    phonenumber: "",

    role: "",
    designation: "",
    department: "",

    joiningDate: "",
    employmentType: "",
    employmentStatus: "ACTIVE",

    workLocation: "",
    managerId: "",

    basicSalary: "",
    hra: "",
    allowances: "",

    bankName: "",
    accountHolderName: "",
    accountNumber: "",
    ifscCode: "",
  });

  const handleSubmit = async () => {
    try {
      const payload = {
        employee: {
          employeename: formData.employeename,
          companyemail: formData.companyemail,
          phonenumber: formData.phonenumber,

          role: formData.role,
          designation: formData.designation,
          department: formData.department,

          joiningDate: formData.joiningDate,
          employmentType: formData.employmentType,
          employmentStatus: formData.employmentStatus,

          workLocation: formData.workLocation,

          bankName: formData.bankName,
          accountHolderName: formData.accountHolderName,
          accountNumber: formData.accountNumber,
          ifscCode: formData.ifscCode,

          basicSalary: Number(formData.basicSalary || 0),
          hra: Number(formData.hra || 0),
          allowances: Number(formData.allowances || 0),

          manager: formData.managerId
            ? {
                employeeid: Number(formData.managerId),
              }
            : null,
        },
      };

      console.log("ONBOARD PAYLOAD");
      console.log(payload);

      await onboardEmployee(payload);

      alert(
        `Employee Created Successfully

Username: ${formData.companyemail}
Password: ${formData.companyemail}`,
      );

      if (onSuccess) {
        onSuccess();
      }

      if (onClose) {
        onClose();
      }
    } catch (error) {
      console.error(error);

      alert(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to onboard employee",
      );
    }
  };

  return (
    <div className="container-fluid py-4">
      <div className="row justify-content-center">
        <div className="col-lg-10 col-xl-9">
          <div className="card shadow border-0">
            <div className="card-body p-4">
              <h2 className="mb-4">Employee Onboarding Wizard</h2>

              <div className="progress mb-4">
                <div
                  className="progress-bar"
                  role="progressbar"
                  style={{
                    width: `${(step / 3) * 100}%`,
                  }}
                >
                  Step {step} of 3
                </div>
              </div>

              <div className="d-flex justify-content-between mb-4">
                <span className={step >= 1 ? "fw-bold text-primary" : ""}>
                  1. Employee
                </span>

                <span className={step >= 2 ? "fw-bold text-primary" : ""}>
                  2. Salary
                </span>

                <span className={step >= 3 ? "fw-bold text-primary" : ""}>
                  3. Bank
                </span>
              </div>

              {step === 1 && (
                <EmployeeStep1
                  formData={formData}
                  setFormData={setFormData}
                  nextStep={() => setStep(2)}
                />
              )}

              {step === 2 && (
                <EmployeeStep2
                  formData={formData}
                  setFormData={setFormData}
                  nextStep={() => setStep(3)}
                  prevStep={() => setStep(1)}
                />
              )}

              {step === 3 && (
                <EmployeeStep3
                  formData={formData}
                  setFormData={setFormData}
                  prevStep={() => setStep(2)}
                  handleSubmit={handleSubmit}
                />
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AddEmployeeWizard;
