import React, { useState } from "react";
import Search from '../common/Search';
import File from './File';

const Files = () => {
  const [files, setFiles] = useState({});
  const [search, setSearch] = useState("");

  return (
    <div className="container">
      <div className="row">
        <div className="col-md-8 m-auto">
          <h1 className="display-4 text-center">Files</h1>
          <div>
            <Search searchString={search} setSearchString={setSearch} />
          </div>
          <table class="table">
            <thead class="thead-light">
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
        </div>
      </div>
    </div>
  );
};

export default Files;
