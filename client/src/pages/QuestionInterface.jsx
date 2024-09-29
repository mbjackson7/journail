import React, { useState, useEffect, useRef } from "react";
import Conversation from "../components/Conversation";

const ChatInterface = ({ userId, baseUrl }) => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [allowInput, setAllowInput] = useState(true);
  const messagesEndRef = useRef(null);

  const sendQuery = async () => {
    setAllowInput(false);
    setInput("");
    setMessages([...messages, { user: input, bot: "..." }]);
    console.log(JSON.stringify({ userId, query: input }));
    const response = await fetch(`${baseUrl}/journals/query`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, query: input }),
    });
    const data = await response.json();
    // returns { response: 'response', sources: ['source1', 'source2'] }
    setMessages([...messages, { user: input, bot: data }]);
    setAllowInput(true);
  };

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  return (
    <div className="flex flex-col h-full">
      <div className="p-4 pt-10 overflow-scroll">
        <Conversation messages={messages}/>
        <div className="pb-20" ref={messagesEndRef} />
      </div>
      <div className="h-20 bottom-0 left-0 right-0"/>
      <div className="bg-back-dark p-4 fixed bottom-0 left-0 right-0">
        <div className="max-w-3xl mx-auto flex">
          <input
            className="border p-2 flex-grow mr-2 rounded-3xl bg-white"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Type your question..."
          />
          <button
            className="bg-custom3 p-2 rounded-3xl"
            onClick={sendQuery}
            disabled={!allowInput}
          >
            ➤
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChatInterface;
