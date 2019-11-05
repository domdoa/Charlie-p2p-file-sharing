import React from "react";

const Search = ({ searchString, setSearchString }) => (
  <input
    type="text"
    value={searchString}
    onChange={e => setSearchString(e.target.value)}
    placeholder="Search files..."
    className="prompt"
  />
);

export default Search;
