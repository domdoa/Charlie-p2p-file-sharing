import React, {useState} from "react";
import TextFieldGroup from "../common/TextFieldGroup";
import {logUser} from "../utils/Authorization";

const Login = (props) => {
    const [user, setUser] = useState({
        email: '',
        password: ''
    });

    const onChange = (e) => {
        setUser({
            ...user
            , [e.target.name]: e.target.value
        });
    };

    const loginUser = (e) => {
        e.preventDefault();
        //loginUser(newUser);

        let data = {
            email: user.email,
            password: user.password
        };

        let promise = new Promise((resolve, reject) => logUser(data, resolve, reject));
        promise.then(() => props.history.push("/"))
    }

    return (
        <div className="login">
            <div className="container">
                <div className="row">
                    <div className="col-md-8 m-auto">
                        <h1 className="display-4 text-center">Log In</h1>
                        <p className="lead text-center">Sign in to your account</p>
                        <form onSubmit={loginUser}>
                            <TextFieldGroup
                                placeholder="Email address"
                                name="email"
                                type="email"
                                value={user.email}
                                onChange={onChange}
                            />
                            <TextFieldGroup
                                placeholder="Password"
                                name="password"
                                type="password"
                                value={user.password}
                                onChange={onChange}
                            />
                            <input
                                type="submit"
                                className="btn btn-info btn-block mt-4"
                                value="Login"
                            />
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Login;