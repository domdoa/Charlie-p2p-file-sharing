import axios from "axios";

let config = {
  headers: { Authorization: localStorage.Authorization }
};

export const getAllPeersWithAFileFromAllServers = (server, file, email) => {
  axios.post(`${server}/getAllPeersWithAFileFromAllServers?email=${email}`,file,config).catch(() => alert('Please start desktop client to start your download'));
};




