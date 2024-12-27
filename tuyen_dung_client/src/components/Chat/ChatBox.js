import { Menu, Send, X } from "lucide-react";
import React, { useState } from "react";

import Picker from "@emoji-mart/react";
import data from "@emoji-mart/data";

const ChatBox = ({ user, onBack, onClose }) => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [showEmojiPicker, setShowEmojiPicker] = useState(false);

  const sendMessage = () => {
    if (input.trim()) {
      setMessages((prev) => [...prev, { sender: "me", content: input }]);
      setInput("");
    }
  };

  const addEmoji = (emoji) => {
    setInput((prev) => prev + emoji.native);
  };

  return (
    <div className="flex flex-col h-full">
      <div className="flex items-center justify-between bg-blue-500 text-white p-3">
        <button onClick={onBack} className="text-xl font-bold">
          <Menu />
        </button>

        <div className="flex items-center">
          <img
            src={user.avatar}
            alt={user.name}
            className="w-8 h-8 rounded-full mr-2"
          />
          <div>
            <h2 className="text-lg font-bold">{user.name}</h2>
          </div>
        </div>

        <button onClick={onClose} className="text-xl font-bold">
          <X />
        </button>
      </div>

      <div className="flex-1 overflow-y-auto p-3">
        {messages.map((msg, idx) => (
          <div
            key={idx}
            className={`mb-2 p-2 rounded ${
              msg.sender === "me"
                ? "bg-blue-100 text-right"
                : "bg-gray-100 text-left"
            }`}
          >
            {msg.content}
          </div>
        ))}
      </div>

      <div className="p-3 border-t relative flex items-center">
        <div className="relative flex-1">
          <input
            type="text"
            className="w-full border p-2 rounded outline-none pr-10"
            placeholder="Nhập nội dung..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
          />

          <button
            className="absolute right-2 top-1/2 transform -translate-y-1/2 text-xl"
            onClick={() => setShowEmojiPicker((prev) => !prev)}
          >
            😊
          </button>
        </div>

        <button
          className="ml-2 bg-blue-500 text-white px-3 py-2 rounded"
          onClick={() => {
            sendMessage();
            setShowEmojiPicker(false);
          }}
        >
          <Send />
        </button>

        {showEmojiPicker && (
          <div className="absolute bottom-12 left-0 z-10">
            <Picker
              data={data}
              onEmojiSelect={(emoji) => {
                setInput((prev) => prev + emoji.native);
              }}
            />
          </div>
        )}
      </div>
    </div>
  );
};

export default ChatBox;
