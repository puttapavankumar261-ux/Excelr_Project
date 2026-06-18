import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axiosClient from "../../../api/axiosClient";

function EditPayslip() {
  const { id } = useParams();

  const [payslip, setPayslip] = useState(null);

  useEffect(() => {
    loadPayslip();
  }, []);

  const loadPayslip = async () => {
    const response = await axiosClient.get(`/getpayslip/${id}`);

    setPayslip(response.data);
  };

  if (!payslip) {
    return <h4>Loading...</h4>;
  }

  return (
    <div className="container p-4">
      <h2>Edit Payslip</h2>

      <form>
        <div className="mb-3">
          <label>Bonus</label>

          <input
            type="number"
            className="form-control"
            value={payslip.bonus}
            onChange={(e) =>
              setPayslip({
                ...payslip,
                bonus: e.target.value,
              })
            }
          />
        </div>

        <div className="mb-3">
          <label>Other Deductions</label>

          <input
            type="number"
            className="form-control"
            value={payslip.otherDeductions}
            onChange={(e) =>
              setPayslip({
                ...payslip,
                otherDeductions: e.target.value,
              })
            }
          />
        </div>

        <button className="btn btn-primary" type="button">
          Update Payslip
        </button>
      </form>
    </div>
  );
}

export default EditPayslip;
