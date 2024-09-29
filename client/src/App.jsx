import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import ChatInterface from "./pages/ChatInterface";
import QuestionInterface from "./pages/QuestionInterface";
import EntriesList from "./pages/EntriesList";
import EntryDetail from "./pages/EntryDetail";

const App = () => {
  const [userId, setUserId] = useState("user_006");
  const baseUrl = "http://10.195.3.77:8080/api";

  const handleUserIdChange = (e) => {
    setUserId(e.target.value);
  };

  const [entries, setEntries] = useState([
    {
      time: "2021-09-01T08:00:00Z",
      conversation:
        "I had a great day today. I went to the park and played with my friends. I also had a picnic with my family. I'm so happy.",
      shortSummary: "Great day at the park",
    },
    {
      time: "2021-09-02T08:00:00Z",
      conversation:
        "Today was a very busy day. I had a lot of work to do at school. I also had to help my mom with the groceries. I'm very tired.",
      shortSummary: "Busy day at school",
    },
    {
      time: "2021-09-03T08:00:00Z",
      conversation:
        "I had a lot of fun today. I went to the beach with my family. We played in the sand and swam in the ocean. I'm so excited.",
      shortSummary: "Fun day at the beach",
    },
  ]);

  useEffect(() => {
    const fetchEntries = async () => {
      console.log(`${baseUrl}/journals/${userId}`);
      const response = await fetch(`${baseUrl}/journals/${userId}`);
      const data = await response.json();
      setEntries(data);
    };
    if (userId) {
      fetchEntries();
    }
  }, [userId]);

  useEffect(() => {
    console.log(entries);
  }, [entries]);

  return (
    <Router className="h-full w-screen">
      <nav className="p-4 bg-gray-800 text-white w-screen flex gap-2 fixed h-20 items-center">
        <Link className="mr-4" to="/">
          <button className="text-white">Chat</button>
        </Link>
        <Link className="mr-4" to="/question">
          <button className="text-white">Ask Question</button>
        </Link>
        <Link to="/entries">
          <button className="text-white">Entries</button>
        </Link>
        <span className="ml-auto">User ID:</span>
        <input
          className="p-2 rounded-3xl"
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
            element={<EntryDetail entries={entries} />}
          />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
