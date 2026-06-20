import { useEffect, useState } from "react";

import {
  getAttendanceSummary,
  getTodayAttendance,
  checkIn,
  checkOut,
} from "../../attendance/services/attendanceService";

import axiosClient from "../../../api/axiosClient";

function EmployeeAttendance() {
  const [summary, setSummary] = useState(null);

  const [todayAttendance, setTodayAttendance] = useState(null);

  const [shift, setShift] = useState(null);

  const [employeeId, setEmployeeId] = useState(null);

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user"));

    const empId = user?.id || user?.employeeId || user?.employeeid;

    setEmployeeId(empId);

    loadAttendanceSummary(empId);

    loadTodayAttendance(empId);

    loadEmployeeShift(empId);
  }, []);

  const loadAttendanceSummary = async (empId) => {
    try {
      const response = await getAttendanceSummary(empId);

      setSummary(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const loadTodayAttendance = async (empId) => {
    try {
      const response = await getTodayAttendance(empId);

      setTodayAttendance(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const loadEmployeeShift = async (empId) => {
    try {
      const response = await axiosClient.get(`/employeeshift/${empId}`);

      setShift(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const handleCheckIn = async () => {
    try {
      await checkIn(employeeId);

      alert("Checked In Successfully");

      await loadTodayAttendance(employeeId);

      await loadAttendanceSummary(employeeId);
    } catch (error) {
      alert(error.response?.data?.message || "Check In Failed");
    }
  };

  const handleCheckOut = async () => {
    try {
      await checkOut(employeeId);

      alert("Checked Out Successfully");

      await loadTodayAttendance(employeeId);

      await loadAttendanceSummary(employeeId);
    } catch (error) {
      alert(error.response?.data?.message || "Check Out Failed");
    }
  };

  return (
    <div className="container-fluid p-4">
      <h2 className="mb-4">My Attendance</h2>

      {/* Shift Details */}

      {shift && (
        <div className="card shadow-sm mb-4">
          <div className="card-header bg-primary text-white">
            Assigned Shift
          </div>

          <div className="card-body">
            <div className="row">
              <div className="col-md-2">
                <strong>Name</strong>
                <br />
                {shift.shiftName}
              </div>

              <div className="col-md-2">
                <strong>Type</strong>
                <br />
                {shift.shiftType}
              </div>

              <div className="col-md-2">
                <strong>Start Time</strong>
                <br />
                {shift.startTime}
              </div>

              <div className="col-md-2">
                <strong>End Time</strong>
                <br />
                {shift.endTime}
              </div>

              <div className="col-md-2">
                <strong>Grace Time</strong>
                <br />
                {shift.lateGraceMinutes} Min
              </div>

              <div className="col-md-2">
                <strong>Min Hours</strong>
                <br />
                {shift.minWorkHours}
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Today's Attendance */}

      <div className="card shadow-sm mb-4">
        <div className="card-header bg-success text-white">
          Today's Attendance
        </div>

        <div className="card-body">
          <div className="row mb-3">
            <div className="col-md-3">
              <strong>Status</strong>
              <br />

              <span className="badge bg-info">
                {todayAttendance?.attendanceStatus || "Not Checked In"}
              </span>
            </div>

            <div className="col-md-3">
              <strong>Punch In</strong>
              <br />
              {todayAttendance?.punchInTime || "--"}
            </div>

            <div className="col-md-3">
              <strong>Punch Out</strong>
              <br />
              {todayAttendance?.punchOutTime || "--"}
            </div>

            <div className="col-md-3">
              <strong>Total Work</strong>
              <br />
              {todayAttendance?.totalWorkMinutes || 0} Min
            </div>
          </div>

          <button
            className="btn btn-success me-2"
            onClick={handleCheckIn}
            disabled={todayAttendance?.punchInTime}
          >
            Check In
          </button>

          <button
            className="btn btn-danger"
            onClick={handleCheckOut}
            disabled={
              !todayAttendance?.punchInTime || todayAttendance?.punchOutTime
            }
          >
            Check Out
          </button>
        </div>
      </div>

      {/* Attendance Summary */}

      {summary && (
        <div className="row">
          <div className="col-md-3">
            <div className="card shadow-sm border-success">
              <div className="card-body text-center">
                <h6>Working Days</h6>

                <h2>{summary.workingDays}</h2>
              </div>
            </div>
          </div>

          <div className="col-md-3">
            <div className="card shadow-sm border-primary">
              <div className="card-body text-center">
                <h6>Present Days</h6>

                <h2>{summary.presentDays}</h2>
              </div>
            </div>
          </div>

          <div className="col-md-3">
            <div className="card shadow-sm border-warning">
              <div className="card-body text-center">
                <h6>Leave Days</h6>

                <h2>{summary.leaveDays}</h2>
              </div>
            </div>
          </div>

          <div className="col-md-3">
            <div className="card shadow-sm border-info">
              <div className="card-body text-center">
                <h6>Attendance %</h6>

                <h2>{summary.attendancePercentage}%</h2>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default EmployeeAttendance;
