import "./App.css";
import React, {useEffect, useState} from "react";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
// Components
import Navbar from "./components/layout/Navbar";
import Footer from "./components/layout/Footer";
import Frontpage from "./components/layout/Frontpage";
import Register from "./components/auth/Register";
import Login from "./components/auth/Login";
import NotFound from "./components/not-found/NotFound";
import Files from "./components/files/Files";
import {isLoggedIn} from "./components/utils/Authorization";


function App() {
    const [message, setMessage] = useState("");

    useEffect(() => {
        // const socket = socketIOClient(localhostSocket);
        // socket.on("FromAPI", data => setMessage(data));

        isLoggedIn();
    }, []);

    return (
        <Router>
            <p>{message ? message : 'Loading...'}</p>
            <Navbar/>
            <Switch>
                <Route exact path="/" component={Frontpage}/>
                <Route path="/register" component={Register}/>
                <Route path="/login" component={Login}/>
                <Route path="/files" component={Files}/>
                <Route component={NotFound}/>
            </Switch>
            <Footer/>
        </Router>
    );
}

export default App;
