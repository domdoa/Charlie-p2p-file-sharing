import React, {useState} from "react";
import TextFieldGroup from "../common/TextFieldGroup";
import {registerUser} from "../utils/Authorization";

const Register = (props) => {
  const [newUser, setNewUser] = useState({
    email: '',
    password: ''
  });

  const onChange = (e) => {
      setNewUser({
          ...newUser,
          [e.target.name]: e.target.value
      });
  }

  const registerNewUser = (e) => {
    e.preventDefault();
    //registerUser(newUser);
      let data = {
          email: newUser.email,
          password: newUser.password
      };

      let promise = new Promise((resolve, reject) => registerUser(data, resolve, reject));
      promise.then(() => props.history.push("/"))
  }

  return(
    <div className="register">
    <div className="container">
      <div className="row">
        <div className="col-md-8 m-auto">
          <h1 className="display-4 text-center">Sign Up</h1>
          <p className="lead text-center">
            Create your IoT dropsharesharedrop account
          </p>
          <form onSubmit={registerNewUser}>
            <TextFieldGroup
              placeholder="Email address"
              name="email"
              type="email"
              value={newUser.email}
              onChange={onChange}
            />
            <TextFieldGroup
              placeholder="Password"
              name="password"
              type="password"
              value={newUser.password}
              onChange={onChange}
            />
            <input
              type="submit"
              className="btn btn-info btn-block mt-4"
              value="Register"
            />
          </form>
        </div>
      </div>
    </div>
  </div>
  )
}

export default Register
