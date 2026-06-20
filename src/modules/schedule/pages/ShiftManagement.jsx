import { useEffect, useState } from "react";
import { getAllShifts, deleteShift } from "../services/shiftService";
import AddShiftModal from "../components/AddShiftModal";

function ShiftManagement() {
  const [shifts, setShifts] = useState([]);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    loadShifts();
  }, []);

  const loadShifts = async () => {
    try {
      const response = await getAllShifts();
      setShifts(response.data || []);
    } catch (error) {
      console.error(error);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete Shift?")) return;

    await deleteShift(id);
    loadShifts();
  };

  return (
    <div className="container-fluid">
      <div className="d-flex justify-content-between mb-3">
        <h2>Shift Management</h2>

        <button className="btn btn-success" onClick={() => setShowModal(true)}>
          Add Shift
        </button>
      </div>

      <table className="table table-bordered">
        <thead className="table-dark">
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Type</th>
            <th>Start</th>
            <th>End</th>
            <th>Grace</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {shifts.map((shift) => (
            <tr key={shift.shiftid}>
              <td>{shift.shiftid}</td>
              <td>{shift.shiftName}</td>
              <td>{shift.shiftType}</td>
              <td>{shift.startTime}</td>
              <td>{shift.endTime}</td>
              <td>{shift.lateGraceMinutes}</td>

              <td>
                <button
                  className="btn btn-danger btn-sm"
                  onClick={() => handleDelete(shift.shiftid)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {showModal && (
        <AddShiftModal
          onClose={() => setShowModal(false)}
          onSuccess={loadShifts}
        />
      )}
    </div>
  );
}

export default ShiftManagement;
