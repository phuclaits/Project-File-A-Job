import { X } from "lucide-react";
import React from "react";

const UserList = ({ onUserSelect, onClose }) => {
  const users = [
    {
      id: 1,
      name: "Nu",
      avatar: "https://via.placeholder.com/50",
      lastMessage: "hay aaaaaa",
      time: "1 giờ",
      isOnline: true,
    },
    {
      id: 2,
      name: "Gà",
      avatar: "https://via.placeholder.com/50",
      lastMessage: "may hỏi a đấy e",
      time: "1 giờ",
      isOnline: false,
    },
    {
      id: 3,
      name: "La Hoàng Phúc",
      avatar: "https://via.placeholder.com/50",
      lastMessage: "tùy à",
      time: "1 giờ",
      isOnline: true,
    },
  ];

  return (
    <div className="flex flex-col h-full">
      <div className="flex items-center justify-between bg-blue-500 text-white p-3">
        <h2 className="text-lg font-bold">Danh sách người dùng</h2>
        <button onClick={onClose} className="text-xl font-bold">
          <X size={24} />
        </button>
      </div>

      <div className="flex-1 overflow-y-auto">
        {users.map((user) => (
          <div
            key={user.id}
            className="flex items-center p-3 border-b border-gray-200 cursor-pointer hover:bg-gray-200"
            onClick={() => onUserSelect(user)}
          >
            <img
              src={user.avatar}
              alt={user.name}
              className="w-12 h-12 rounded-full"
            />

            <div className="ml-3 flex-1">
              <div className="flex justify-between items-center">
                <span className="font-semibold">{user.name}</span>
                <span className="text-sm text-gray-500">{user.time}</span>
              </div>
              <div className="text-sm text-gray-600">{user.lastMessage}</div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default UserList;
