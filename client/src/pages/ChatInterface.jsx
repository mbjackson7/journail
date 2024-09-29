import React, { useState, useEffect, useRef } from "react";

const ChatInterface = ({ userId, baseUrl }) => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [allowInput, setAllowInput] = useState(false);
  const [time, setTime] = useState();
  const [ending, setEnding] = useState(false);
  const messagesEndRef = useRef(null);

  const startConversation = async () => {
    setInput("");
    setEnding(false);
    setMessages([...messages, { user: input, bot: "..." }]);
    // set time to current time in ISO format for CST timezone
    const newTime = new Date().toISOString();
    setTime(newTime);
    console.log(userId, newTime);
    console.log(JSON.stringify({ userId, newTime }));
    const response = await fetch(`${baseUrl}/journals/start-conversation`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, time: newTime }),
    });
    const data = await response.text();
    setMessages([...messages, { user: input, bot: data }]);
    setAllowInput(true);
  };

  const sendMessage = async () => {
    if (!allowInput) return;
    if (input === "") return;
    setAllowInput(false);
    setInput("");
    setMessages([...messages, { user: input, bot: "..." }]);
    console.log(JSON.stringify({ userId, time, message: input }));
    const response = await fetch(`${baseUrl}/journals/send-message`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, time, message: input }),
    });
    const data = await response.text();
    setMessages([...messages, { user: input, bot: data }]);
    setAllowInput(true);
    document.getElementById("message_input").focus();
  };

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  const initEndConversation = async () => {
    setAllowInput(false);
    setInput("");
    setMessages([...messages, { user: input, bot: "..." }]);
    const response = await fetch(`${baseUrl}/journals/initiate-end-conversation`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, time }),
    });
    setEnding(true);
    const data = await response.text();
    setMessages([...messages, { user: input, bot: data }]);
    setAllowInput(true);
    document.getElementById("message_input").focus();
  };

  const endConversation = async () => {
    await fetch(`${baseUrl}/journals/end-conversation`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, time, message: input }),
    });
    setMessages([]);
    setAllowInput(false);
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);


  return (
    <div className="flex flex-col h-full">
      <div className="p-4 pt-10 overflow-scroll">
        {messages.map((msg, index) => (
          <div key={index} className="mb-4">
            {msg.user !== "" && (
              <div className="bg-slate-800 p-2 rounded-3xl mb-2">
                <strong>You:</strong> {msg.user}
              </div>
            )}
            <div className="bg-slate-600 p-2 rounded-3xl">
              <strong>Bot:</strong> {msg.bot}
            </div>
          </div>
        ))}
        <div className="pb-20" ref={messagesEndRef} />
      </div>
      <div className="bg-[#242424] p-4 fixed bottom-0 left-0 right-0">
        {messages.length === 0 ? (
          <button
            className="text-center w-full rounded-3xl bg-yellow-600"
            onClick={startConversation}
          >
            Start A Journal Entry
          </button>
        ) : (
          <div className="max-w-3xl mx-auto flex">
            <input
              id="message_input"
              className="border p-2 flex-grow mr-2 rounded-3xl"
              value={input}
              onChange={(e) => setInput(e.target.value)}
              placeholder="Type your message..."
            />
            {ending ? (
              <>
                <button
                  className="bg-yellow-600 text-white p-2 rounded-3xl"
                  onClick={endConversation}
                >
                  Send Feedback
                </button>
              </>
            ) : (
              <>
                <button
                  className="bg-yellow-600 text-white p-2 mr-2 rounded-3xl"
                  onClick={sendMessage}
                  disabled={!allowInput}
                >
                  ➤
                </button>
                <button
                  className="bg-rose-700 text-white p-2 rounded-3xl"
                  onClick={initEndConversation}
                >
                  X
                </button>
              </>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default ChatInterface;