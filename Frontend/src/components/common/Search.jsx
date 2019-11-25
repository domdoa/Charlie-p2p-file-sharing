import React from "react";

const Search = ({ searchString, setSearchString }) => (
  <form className="form-inline">
    <i className="fas fa-search" aria-hidden="true"></i>
    <input
      className="form-control form-cntrol-sm ml-3 w-75"
      value={searchString}
      onChange={e => setSearchString(e.target.value)}
      type="text"
      placeholder="Search files..."
      aria-label="Search"
    />
  </form>
);

export default Search;
