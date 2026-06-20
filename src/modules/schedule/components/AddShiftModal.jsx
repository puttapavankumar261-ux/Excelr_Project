import { useState } from "react";
import { saveShift } from "../services/shiftService";

function AddShiftModal({ onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    shiftName: "",
    shiftType: "MORNING",
    startTime: "",
    endTime: "",
    lateGraceMinutes: 15,
    earlyExitGraceMinutes: 15,
    minWorkHours: 8,
    active: true,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await saveShift(formData);

      alert("Shift Created Successfully");

      onSuccess();

      onClose();
    } catch (error) {
      console.error(error);

      alert(error.response?.data?.message || "Failed to create shift");
    }
  };

  return (
    <div
      className="modal d-block"
      style={{
        backgroundColor: "rgba(0,0,0,0.5)",
      }}
    >
      <div className="modal-dialog modal-lg">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">Add New Shift</h5>

            <button className="btn-close" onClick={onClose}></button>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="modal-body">
              <div className="row">
                <div className="col-md-6 mb-3">
                  <label className="form-label">Shift Name</label>

                  <input
                    type="text"
                    className="form-control"
                    name="shiftName"
                    value={formData.shiftName}
                    onChange={handleChange}
                    required
                  />
                </div>

                <div className="col-md-6 mb-3">
                  <label className="form-label">Shift Type</label>

                  <select
                    className="form-select"
                    name="shiftType"
                    value={formData.shiftType}
                    onChange={handleChange}
                  >
                    <option value="MORNING">MORNING</option>

                    <option value="NIGHT">NIGHT</option>

                    <option value="US">US</option>

                    <option value="ROTATIONAL">ROTATIONAL</option>
                  </select>
                </div>

                <div className="col-md-6 mb-3">
                  <label className="form-label">Start Time</label>

                  <input
                    type="time"
                    className="form-control"
                    name="startTime"
                    value={formData.startTime}
                    onChange={handleChange}
                    required
                  />
                </div>

                <div className="col-md-6 mb-3">
                  <label className="form-label">End Time</label>

                  <input
                    type="time"
                    className="form-control"
                    name="endTime"
                    value={formData.endTime}
                    onChange={handleChange}
                    required
                  />
                </div>

                <div className="col-md-4 mb-3">
                  <label className="form-label">Late Grace Minutes</label>

                  <input
                    type="number"
                    className="form-control"
                    name="lateGraceMinutes"
                    value={formData.lateGraceMinutes}
                    onChange={handleChange}
                  />
                </div>

                <div className="col-md-4 mb-3">
                  <label className="form-label">Early Exit Grace</label>

                  <input
                    type="number"
                    className="form-control"
                    name="earlyExitGraceMinutes"
                    value={formData.earlyExitGraceMinutes}
                    onChange={handleChange}
                  />
                </div>

                <div className="col-md-4 mb-3">
                  <label className="form-label">Minimum Work Hours</label>

                  <input
                    type="number"
                    step="0.5"
                    className="form-control"
                    name="minWorkHours"
                    value={formData.minWorkHours}
                    onChange={handleChange}
                  />
                </div>

                <div className="col-md-12">
                  <div className="form-check">
                    <input
                      type="checkbox"
                      className="form-check-input"
                      name="active"
                      checked={formData.active}
                      onChange={handleChange}
                    />

                    <label className="form-check-label">Active Shift</label>
                  </div>
                </div>
              </div>
            </div>

            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-secondary"
                onClick={onClose}
              >
                Cancel
              </button>

              <button type="submit" className="btn btn-primary">
                Save Shift
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default AddShiftModal;
