import React, { useState, useEffect } from "react";
import Search from "../common/Search";
import File from "./File";
import peerService from "../../services/Peer";

var stompClient = null;

const Files = () => {
  const [files, setFiles] = useState([]);
  const [search, setSearch] = useState("");

  useEffect(() => {
    connect();
  }, []);

  const connect = () => {
    const Stomp = require("stompjs");
    var SockJS = require("sockjs-client");
    SockJS = new SockJS("http://localhost:8080/socket");
    stompClient = Stomp.over(SockJS);
    stompClient.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    // Subscribing to the topic
    console.log("connected");
    stompClient.subscribe("/topic/files", onMessageReceived);
  };

  const sendMessage = event => {
    event.preventDefault();
    if (stompClient) {
      // send public message
      stompClient.send("/app/sendMessage", {}, "test message from client");
    }
  };

  const onMessageReceived = payload => {
    if (payload.body !== "No files") {
      let files = JSON.parse(payload.body);
      files.forEach(file => {
        peerService.getAllPeersWithAFileFromAllServers(file).then(res => {
          file.seeders = res.peers.length;
          setFiles([...files]);
        });
      });
    }
  };

  const onError = error => {
    console.log(error);
  };

  return (
    <div className="container">
      <div className="row">
        <div className="col-md-8 m-auto">
          <h1 className="display-4 text-center">Files</h1>
          <div className="d-flex justify-content-center">
            <Search searchString={search} setSearchString={setSearch} />
          </div>
          <div className="row mt-3">
            <div className="col-12">
              <table className="table">
                <thead className="thead-light">
                  <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Size</th>
                    <th scope="col">Type</th>
                    <th scope="col">Seeders</th>
                    <th scope="col">Upload date</th>
                    <th scope="col">Download</th>
                  </tr>
                </thead>
                <tbody>
                  {files
                    .filter(file =>
                      file.name.toLowerCase().includes(search.toLowerCase())
                    )
                    .map(file => (
                      <File file={file} key={file.md5Sign} />
                    ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Files;
