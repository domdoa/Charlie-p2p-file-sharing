import "./App.css";
import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import socketIOClient from "socket.io-client";

// Components
import Navbar from "./components/layout/Navbar";
import Footer from "./components/layout/Footer";
import Frontpage from "./components/layout/Frontpage";
import Register from "./components/auth/Register";
import Login from "./components/auth/Login";
import NotFound from "./components/not-found/NotFound";
import Files from "./components/files/Files";

const localhostSocket = "http://127.0.0.1:4001";
const localhostHttp = "http://127.0.0.1:8080";

function App() {
  const [message, setMessage] = useState("");

  useEffect(() => {
    const socket = socketIOClient(localhostSocket);
    socket.on("FromAPI", data => setMessage(data));
  }, []);

  return (
    <Router>
      <p>{message?message:'Loading...'}</p>
      <Navbar />
      <Switch>
        <Route exact path="/" component={Frontpage} />
        <Route path="/register" component={Register} />
        <Route path="/login" component={Login} />
        <Route path="/files" component={Files} />
        <Route component={NotFound} />
      </Switch>
      <Footer />
    </Router>
  );
}

export default App;
