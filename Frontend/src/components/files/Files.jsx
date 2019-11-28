import React, {useState, useEffect} from "react";
import Search from "../common/Search";
import File from "./File";
import {PEERS, socketList} from "../utils/constants";
import {getEmail} from "../utils/Authorization";
import {getAllPeersWithAFileFromAllServers} from "../../services/Peer";

let stompClients = [];
let peerFilesServers = new Map();


const Files = () => {
    const [files, setFiles] = useState([]);
    const [search, setSearch] = useState("");

    useEffect(() => {
        connect();
    }, []);

    const connect = () => {
        const Stomp = require("stompjs");
        let SockJS = require("sockjs-client");
        let sockets = [];
        for (let socket of socketList) {
            let x = new SockJS(socket);
            sockets.push(x);
            stompClients.push(Stomp.over(x))
        }
        for (let stompClient of stompClients) {
            stompClient.connect({}, onConnected, onError);
        }
    };

    const onConnected = () => {
        // Subscribing to the topic
        console.log("connected");
        getFiles();
        for (let stompClient of stompClients) {
            stompClient.subscribe("/topic/files", onMessageReceivedFiles);
            stompClient.subscribe("/topic/peers", (payload) => onMessageReceivedPeers(payload, stompClient.ws._transUrl.split("/socket")[0]));
        }
        setInterval(getFiles, 3000);
    };

    const getFiles = () => {
        for (let stompClient of stompClients) {
            if (stompClient) {
                // send public message
                stompClient.send("/app/getFiles", {}, getEmail());
            }
        }
    };

    const onMessageReceivedFiles = payload => {
        if (payload.body !== "No files") {
            let filesFromServer = JSON.parse(JSON.parse(payload.body).message);
            setFiles(...files, [...filesFromServer])
        }
    };

    const onMessageReceivedPeers = (payload, serverUrl) => {
        if (payload.body !== "No peers") {
            let peersFromServer = JSON.parse(payload.body);
            peerFilesServers.set(serverUrl, peersFromServer);
        }
    };

    const downloadFunc = (file) => {
        let server = "";
        for (var [key, value] of peerFilesServers) {
            console.log(key + ' = ' + value);
            for (let peer of value) {
                if (peer.email === getEmail()) {
                    server = key;
                    break;
                }
            }
        }

        if (server === "")
            alert('Please start desktop client to start your download');
        else
            getAllPeersWithAFileFromAllServers(server + PEERS, file, getEmail());
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
                        <Search searchString={search} setSearchString={setSearch}/>
                    </div>
                    <div className="row mt-3">
                        <div className="col-12">
                            <table className="table">
                                <thead className="thead-light">
                                <tr>
                                    <th scope="col">Name</th>
                                    <th scope="col">Size</th>
                                    <th scope="col">Type</th>
                                    <th scope="col">MD5</th>
                                    <th scope="col">Seeders</th>
                                    <th scope="col">Download</th>
                                </tr>
                                </thead>
                                <tbody>
                                {files
                                    .filter(file =>
                                        file.name.toLowerCase().includes(search.toLowerCase())
                                    )
                                    .map(file => (
                                        <File file={file} key={file.md5Sign + file.name} download={downloadFunc}/>
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
