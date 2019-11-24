import React, {useState} from "react";
import TextFieldGroup from "../common/TextFieldGroup";
import {registerUser} from "../utils/Authorization";
import ReactSnackBar from "react-js-snackbar";

const Checkbox = props => (
    <input type="checkbox" {...props} />
);

const Register = (props) => {
    const [newUser, setNewUser] = useState({
        email: '',
        password: '',
        group: '',
        inviteString: ''
    });
    const [inviteCheckbox, setInviteCheckbox] = useState({
        checked: false
    });
    const [snackBar, setSnackBar] = useState({
        Show: false,
        Showing: false,
        Text: ""
    });

    const checkboxOnChange = (e) => {
        setInviteCheckbox({
            checked: e.target.checked
        });
        setNewUser({
            ...newUser,
            group: "",
            inviteString: ""
        });
    };

    const onChange = (e) => {
        setNewUser({
            ...newUser,
            [e.target.name]: e.target.value
        });
    };

    const registerNewUser = (e) => {
        e.preventDefault();
        let data = {
            email: newUser.email,
            password: newUser.password,
            group: newUser.group,
            inviteString: newUser.inviteString,
        };

        let promise = new Promise((resolve, reject) => registerUser(data, resolve, reject));
        promise.then((response) => {
            if(response && response.InviteString){
                props.show("Invite code: " + response.InviteString, 20000)
            }
            props.history.push("/")
        })
        promise.catch((reject) => {
            props.show("An error occured. Please try again", 5000)
        })
    };

    return (
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
                            <TextFieldGroup
                                placeholder="Create new group"
                                name="group"
                                type="text"
                                disabled={inviteCheckbox.checked}
                                value={newUser.group}
                                onChange={onChange}
                            />
                            <label>
                                <Checkbox
                                    checked={inviteCheckbox.checked}
                                    onChange={checkboxOnChange}
                                />
                                <span>Join an existing group</span>
                            </label>
                            <TextFieldGroup
                                placeholder="Invite code"
                                name="inviteString"
                                type="text"
                                disabled={inviteCheckbox.checked === false}
                                value={newUser.inviteString}
                                onChange={onChange}
                            />
                            <input
                                type="submit"
                                className="btn btn-info btn-block mt-4"
                                value="Register"
                            />
                            <ReactSnackBar Icon={<span>🦄</span>} Show={snackBar.Show}>
                                {snackBar.Text}
                            </ReactSnackBar>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Register
