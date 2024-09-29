import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const EntryDetail = ({ entries }) => {
  const { id } = useParams();
  const [entry, setEntry] = useState();

  const getEntry = (entries, id) => {
    return entries.find((entry) => entry.time === id);
  };

  const renderConversation = (conversation) => {
    return conversation.split("\n").map((line, index) => (
      <p key={index}>{line}</p>
    ));
  };

  useEffect(() => {
    setEntry(getEntry(entries, id));
  }, [entries, id]);

  return (
    <div className="p-4">
      <button className="bg-blue-500 text-white p-2 mb-4" onClick={() => window.history.back()}>Back</button>
      {entry && (
        <>
          <h2 className="text-2xl font-bold mb-4">
            {new Date(entry.time).toLocaleString()}
          </h2>
          <h3 className="text-xl font-bold">Summary</h3>
          <p>{entry.shortSummary}</p>
          <h3 className="text-xl font-bold">Full Conversation</h3>
          <p>{renderConversation(entry.conversation)}</p>
        </>
      )}
    </div>
  );
};

export default EntryDetail;
