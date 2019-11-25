import React from "react";
import peerService from "../../services/Peer";
import { getEmail } from "../utils/Authorization";

const File = ({ file }, key) => {
  const downloadFile = file => {
    //Check if current user is a peer
    peerService
      .findPeerByEmail(getEmail())
      .then(response => {
        if(typeof(response) === "object"){
          console.log("Start download");
        }
      })
      .catch(error => {
        console.log(error);
        alert('Please start desktop client to start your download')
      });
  };
  return (
    <tr key={key}>
      <td>{file.name}</td>
      <td>{file.size}</td>
      <td>{file.ext}</td>
      <td>{file.seeders}</td>
      <td>Test upload date</td>
      <td>
        <button onClick={() => downloadFile(file)}>Download</button>
      </td>
    </tr>
  );
};
export default File;
