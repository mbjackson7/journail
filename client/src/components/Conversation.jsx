const Conversation = ({ messages }) => (
  <>
    {messages.map((msg, index) => (
      <div key={index} className="mb-4">
        {msg.user !== "" && (
          <div className="text-black bg-custom1 p-2 rounded-3xl mb-2">
            <strong>You:</strong> {msg.user}
          </div>
        )}
        <div className="text-black bg-custom4 p-2 rounded-3xl">
          <strong>Clarity:</strong> {msg.bot}
        </div>
      </div>
    ))}
  </>
);

export default Conversation;
