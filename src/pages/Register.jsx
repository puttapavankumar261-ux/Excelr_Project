import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const [username, setUsername] = useState("");

  const [password, setPassword] = useState("");

  const [error, setError] = useState("");

  const navigate = useNavigate();

  const registerAdmin = async (e) => {
    e.preventDefault();

    try {
      await axios.post("http://localhost:8080/register-admin", {
        username,
        password,
      });

      alert("Admin Registered Successfully");

      navigate("/login");
    } catch (err) {
      setError("Admin already exists");
    }
  };

  return (
    <div className="container mt-5">
      <div className="card p-4 shadow">
        <h2 className="text-center mb-4">Register First Admin</h2>

        <form onSubmit={registerAdmin}>
          <div className="mb-3">
            <label className="form-label">Username</label>

            <input
              type="text"
              className="form-control"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Password</label>

            <input
              type="password"
              className="form-control"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="btn btn-success w-100">
            Register Admin
          </button>
        </form>

        {error && <p className="text-danger mt-3">{error}</p>}
      </div>
    </div>
  );
};

export default Register;
