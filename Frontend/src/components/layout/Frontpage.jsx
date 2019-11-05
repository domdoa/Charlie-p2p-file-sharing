import React from "react";
import { Link } from "react-router-dom";

const Frontpage = () => {
  return (
    <div className="frontpage">
      <div className="dark-overlay frontpage-inner text-light">
        <div className="container">
          <div className="row">
            <div className="col-md-12 text-right">
              <h1 className="display-1 mb-2">Dropsharesharedrop</h1>
              <p className="lead">p2p network for sharing files</p>
              <hr />
              <div className="mt-5">
                <Link to="/register" className="btn btn-lg btn-light mr-4">
                  Sign Up!
                </Link>
                <Link to="/login" className="btn btn-lg btn-primary">
                  Login
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Frontpage
