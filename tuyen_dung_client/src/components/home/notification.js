import React from "react";
import { markAllAsRead, updateReadStatus } from "../../apiCall/message";
import { Link } from "react-router-dom/cjs/react-router-dom.min";
const Notification = ({
  toggleNotification,
  unreadCount,
  isNotificationOpen,
  // markAllAsRead, 
  user,
  messages
}) => {
  function formatTime(sendTime) {
    const now = new Date();

    // Xử lý định dạng ngày
    let messageTime;
    if (typeof sendTime === 'string') {
      // Kiểm tra định dạng "Thu Nov 21 18:29:19 ICT 2024"
      if (isNaN(Date.parse(sendTime))) {
        const parsedTime = sendTime.match(/(\w{3}) (\w{3}) (\d{2}) (\d{2}:\d{2}:\d{2}) (\w{3}) (\d{4})/);
        if (parsedTime) {
          const [_, dayOfWeek, month, date, time, timezone, year] = parsedTime;
          if (timezone === "ICT") {
            // Chuyển ICT sang UTC (giảm 7 giờ)
            messageTime = new Date(`${month} ${date} ${year} ${time} UTC`);
            messageTime.setHours(messageTime.getHours() - 7);
          } else {
            throw new Error('Định dạng múi giờ không được hỗ trợ');
          }
        } else {
          throw new Error('Định dạng ngày không hợp lệ');
        }
      } else {
        messageTime = new Date(sendTime); // Định dạng ISO (2024-11-21T11:14:48.814+00:00)
      }
    } else {
      messageTime = new Date(sendTime); // Nếu `sendTime` là Date hoặc timestamp
    }

    const diffInSeconds = Math.floor((now - messageTime) / 1000);
    if (diffInSeconds < 0) {
      return `Thời gian gửi là trong tương lai`;
    } else if (diffInSeconds < 60) {
      return `${diffInSeconds} giây trước`;
    } else if (diffInSeconds < 3600) {
      const diffInMinutes = Math.floor(diffInSeconds / 60);
      return `${diffInMinutes} phút trước`;
    } else if (diffInSeconds < 86400) {
      const diffInHours = Math.floor(diffInSeconds / 3600);
      return `${diffInHours} giờ trước`;
    } else {
      const diffInDays = Math.floor(diffInSeconds / 86400);
      return `${diffInDays} ngày trước`;
    }
  }



  const handleMarkAllAsRead = async () => {
    try {
      await markAllAsRead(user.phoneNumber);
    } catch (error) {
      console.error("Lỗi khi đánh dấu tất cả là đã đọc:", error);
    }
  };
  const handleClick = async (userId, messageId, attachedUrl, e) => {
    e.preventDefault();

    try {
      await updateReadStatus(userId, messageId);

      window.location.assign(attachedUrl);
    } catch (error) {
      console.error("Lỗi khi cập nhật trạng thái đã đọc:", error);
    }
  };
  const handleCompanyClick = async (userId, messageId) => {
    try {
      await updateReadStatus(userId, messageId);
    } catch (error) {
      console.error("Lỗi khi cập nhật trạng thái đã đọc:", error);
    }
  };
  return (
    <li className="nav-item position-relative" style={{ top: '15px', right: '35px' }}>
      <button className="icon-notifi" onClick={toggleNotification}>
        <i className="far fa-bell"></i>
        {unreadCount > 0 && <span className="count bg-danger">{unreadCount}</span>}
      </button>
      {isNotificationOpen && (
        <div className="notification-dropdown">
          <div className="triangle-up-wrapper"></div>
          <div className="triangle-up"></div>
          <div className="dropdown-header d-flex justify-content-between align-items-center">
            <span>Thông báo</span>
            <button
              className="text-success text-notifi"
              onClick={handleMarkAllAsRead}
            >
              Đánh dấu đã đọc ({unreadCount})
            </button>
          </div>
          <div className="notification-list">
            {messages.map((message, index) => {
              // Kiểm tra nếu không phải CANDIDATE
              if (user.codeRoleAccount === "CANDIDATE") {
                return (
                  <a
                    key={index} // key cần được đặt ở phần tử gốc
                    href={message.attachedUrl}
                    onClick={(e) => {
                      e.preventDefault();
                      handleClick(
                        user.phoneNumber,
                        message.messageID,
                        message.attachedUrl,
                        e
                      );
                    }}
                  >
                    <div
                      className={`notification-item ${message.status === "sent" ? "unread" : ""
                        }`}
                    >
                      <p>{message.message}</p>
                      <small>{formatTime(message.sendTime)}</small>
                      {message.status === "sent" && <span className="status-dot"></span>}
                    </div>
                  </a>
                );
              }
              return (
                <Link className="nav-link p-0"
                  to="/admin/application-list/new"
                  onClick={(e) => {
                    handleCompanyClick(
                      user.phoneNumber,
                      message.messageID
                    )
                  }}
                >
                  <div
                    className={`notification-item ${message.status === "sent" ? "unread" : ""
                      }`}
                  >
                    <p>{message.subject}</p>
                    <small>{formatTime(message.sendTime)}</small>
                    {message.status === "sent" && <span className="status-dot"></span>}
                  </div>
                </Link>
              );
            })}

          </div>
        </div>
      )}
    </li>
  )
}

export default Notification