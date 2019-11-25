import "./App.css";
import React, {useEffect, useState} from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
// Components
import Navbar from "./components/layout/Navbar";
import Frontpage from "./components/layout/Frontpage";
import Register from "./components/auth/Register";
import Login from "./components/auth/Login";
import NotFound from "./components/not-found/NotFound";
import Files from "./components/files/Files";
import {isLoggedIn} from "./components/utils/Authorization";
import ReactSnackBar from "react-js-snackbar";


function App() {
    const [message, setMessage] = useState("");
    const [snackBar, setSnackBar] = useState({
        Show: false,
        Showing: false,
        Text: ""
    });
    useEffect(() => {


        isLoggedIn();
    }, []);

    const show = (text, time) => {
        if (snackBar.Showing) return;

        setSnackBar({ Show: true, Showing: true, Text: text});
        setTimeout(() => {
            setSnackBar({ Show: false, Showing: false, Text: '' });
        }, time);
    }

    return (
        <Router>
            <Navbar/>
            <Switch>
                <Route exact path="/" component={Frontpage}/>
                <Route path="/register" component={(props) => <Register {...props} show={show}/>}/>
                <Route path="/register" component={Register}/>
                <Route path="/login" component={Login}/>
                <Route path="/files" component={Files}/>
                <Route component={NotFound}/>
            </Switch>
            <ReactSnackBar Icon={<span>🦄</span>} Show={snackBar.Show}>
                {snackBar.Text}
            </ReactSnackBar>
        </Router>
    );
}

export default App;
