import React from "react";

const File = ( {file, key, download}) => {
  return (
    <tr key={key}>
      <td>{file.name}</td>
      <td>{file.size}</td>
      <td>{file.ext}</td>
      <td>{file.md5Sign}</td>
      <td>{file.seeders ? file.seeders : 1}</td>
      <td>
        <button onClick={() => download(file)}>Download</button>
      </td>
    </tr>
  );
};
export default File;
