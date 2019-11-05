import React, { useState } from "react";
import {Link} from "react-router-dom";

const Navbar = () => {
  const [user, setUser] = useState({});

  const onLogout = () => {
    setUser({});
    // Logout
  };

  let isAuthenticated = false;
  if(user){
    isAuthenticated = true;
  }

  const authLinks = (
    <ul className="navbar-nav ml-auto">
      <li className="nav-item">
        <a href="" onClick={onLogout} className="nav-link">
          Logout
          <i className="fas fa-sign-out-alt ml-2" />
        </a>
      </li>
    </ul>
  );
  const guestLinks = (
    <ul className="navbar-nav ml-auto">
      <li className="nav-item">
        <Link className="nav-link" to="/register">
          Sign Up
        </Link>
      </li>
      <li className="nav-item">
        <Link className="nav-link" to="/login">
          Login
        </Link>
      </li>
    </ul>
  );

  return (
    <nav className="navbar navbar-expand-sm navbar-dark bg-dark mb-4">
      <div className="container">
        <Link className="navbar-brand" to="/">
          Dropsharesharedrop
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-toggle="collapse"
          data-target="#mobile-nav"
        >
          <span className="navbar-toggler-icon" />
        </button>
        <div className="collapse navbar-collapse" id="mobile-nav">
          {isAuthenticated ? authLinks : guestLinks}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
