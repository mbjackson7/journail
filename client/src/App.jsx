import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import ChatInterface from "./pages/ChatInterface";
import QuestionInterface from "./pages/QuestionInterface";
import EntriesList from "./pages/EntriesList";
import EntryDetail from "./pages/EntryDetail";

const App = () => {
  const default_id = window.localStorage.getItem("userId") || "DefaultUser";
  const [userId, setUserId] = useState(default_id);
  const baseUrl = "http://10.195.3.77:8080/api";
  const defaultEntries = [
    {
      time: "1900-01-01T08:00:00Z",
      conversation:
        "No entries found for this user.",
      shortSummary: "No Entries for this user.",
    },
  ];

  const handleUserIdChange = (e) => {
    setUserId(e.target.value);
    window.localStorage.setItem("userId", e.target.value);
  };

  const [entries, setEntries] = useState(defaultEntries);

  const fetchEntries = async () => {
    try {
      const response = await fetch(`${baseUrl}/journals/${userId}`);
      const data = await response.json();
      setEntries(data);
    } catch (error) {
      setEntries(defaultEntries);
    }
  };

  useEffect(() => {
    if (userId) {
      fetchEntries();
    }
  }, [userId]);

  useEffect(() => {
    console.log(entries);
  }, [entries]);

  return (
    <Router className="h-full w-screen">
      <nav className="p-4 bg-back-dark text-text w-screen flex gap-2 fixed h-20 items-center">
        <Link className="mr-4" to="/">
          <button className="text-black rounded-3xl bg-custom1">Make Journal Entry</button>
        </Link>
        <Link className="mr-4" to="/question">
          <button className="text-black rounded-3xl bg-custom2">Ask Question</button>
        </Link>
        <Link to="/entries">
          <button className="text-black rounded-3xl bg-custom3">Entries</button>
        </Link>
        <span className="text-white ml-auto">User ID:</span>
        <input
          className="p-2 rounded-3xl bg-white"
          type="text"
          placeholder="Enter User ID"
          value={userId}
          onChange={handleUserIdChange}
          autoFocus={true}
        />
      </nav>
      <div className="pt-20">
        <Routes>
          <Route
            path="/"
            element={<ChatInterface userId={userId} baseUrl={baseUrl} />}
          />
          <Route
            path="/question"
            element={<QuestionInterface userId={userId} baseUrl={baseUrl} />}
          />
          <Route path="/entries" element={<EntriesList entries={entries} />} />
          <Route
            path="/entries/:id"
            element={<EntryDetail entries={entries}/>}
          />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
