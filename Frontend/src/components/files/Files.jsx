import React, { useState } from "react";
import Search from "../common/Search";
import File from "./File";

var stompClient = null;

const Files = () => {
  const [files, setFiles] = useState({});
  const [search, setSearch] = useState("");

  const connect = () => {
    const Stomp = require("stompjs");
    var SockJS = require("sockjs-client");
    SockJS = new SockJS("http://localhost:8080/socket");
    stompClient = Stomp.over(SockJS);
    stompClient.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    // Subscribing to the test topic
    console.log('connected');
    stompClient.subscribe("/topic/messages", onMessageReceived);
  };

  const sendMessage = event => {
    event.preventDefault(); 
    if (stompClient) {
      // send public message
      stompClient.send("/app/sendMessage", {}, "test message from client");
    }
  };

  const onMessageReceived = payload => {
    console.log(payload.body);
  };

  const onError = error => {
    console.log(error);
  };

  return (
    <div className="container">
      <div className="row">
        <div className="col-md-8 m-auto">
          <h1 className="display-4 text-center">Files</h1>
          <div>
            <Search searchString={search} setSearchString={setSearch} />
          </div>
          <table className="table">
            <thead className="thead-light">
              <tr>
                <th scope="col">Name</th>
                <th scope="col">Size</th>
                <th scope="col">Type</th>
                <th scope="col">Seeders</th>
                <th scope="col">Upload date</th>
              </tr>
            </thead>
            <tbody>
              <File />
            </tbody>
          </table>
          <button onClick={connect}>Connect</button>
          <button onClick={sendMessage}>Send message</button>
        </div>
      </div>
    </div>
  );
};

export default Files;
