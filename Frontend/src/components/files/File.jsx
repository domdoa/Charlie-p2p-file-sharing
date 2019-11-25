import React from "react";
import peerService from '../../services/Peer';
import { getEmail } from "../utils/Authorization";

const File = ({file}, key) => {
  const downloadFile = (file) => {
    //Check if current user is a peer
    console.log(getEmail());

    peerService
      .getAllPeersWithAFileFromAllServers(file)
      .then(request => {
        console.log(request.data);
      })
      .catch(err => {
        console.log(err);
      });
  };
  return (
    <tr key={key}>
      <td>{file.name}</td>
      <td>{file.size}</td>
      <td>{file.ext}</td>
      <td>{file.seeders}</td>
      <td>Test upload date</td>
      <td><button onClick={() => downloadFile(file)}>Download</button></td>
    </tr>
  );
};
export default File;
