import axios from "axios";
const baseUrl = "http://localhost:8080/peers";

var config = {
  headers: { Authorization: localStorage.Authorization }
};

const getAllPeersWithAFileFromAllServers = file => {
  const promise = axios.post(`${baseUrl}/getAllPeersWithAFileFromAllServers`,file,config);
  return promise.then(response => response.data);
};

const findPeerByEmail = email => {
  const promise = axios.get(`${baseUrl}/findPeerByEmail?email=${email}`,config);
  return promise.then(response => response.data);
};

export default { getAllPeersWithAFileFromAllServers, findPeerByEmail };
