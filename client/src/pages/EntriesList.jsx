import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

const EntriesList = ({ entries }) => {
  const [search, setSearch] = useState('');
  const [filteredEntries, setFilteredEntries] = useState(entries);

  useEffect(() => {
    const filtEntry = entries.filter((entry) =>
      entry.shortSummary && entry.shortSummary.toLowerCase().includes(search.toLowerCase()) ||
      entry.conversation && entry.conversation.toLowerCase().includes(search.toLowerCase()) ||
      !entry.conversation && !entry.shortSummary
    );
    const sortEntry = filtEntry.sort((a, b) => new Date(b.time) - new Date(a.time));
    setFilteredEntries(sortEntry);
  }, [search, entries]);

  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold mb-4">Entries List</h2>
      <input
        className="border p-2 mb-4 w-full bg-white"
        type="text"
        placeholder="Search"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />
      <ul className="space-y-4">
        {filteredEntries.map((entry, index) => (
          <li key={index} className="border p-4 rounded-lg shadow-md">
            <div className="flex justify-between items-center">
              <div>
                <h3 className="text-xl font-semibold">
                  {new Date(entry.time).toLocaleString()}
                </h3>
                <p className="text-gray-600">{entry.shortSummary}</p>
              </div>
              <Link
                to={`/entries/${entry.time}`}
              >
                <button className="bg-custom1 text-black p-2 rounded-3xl">
                  View
                </button>
              </Link>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default EntriesList;