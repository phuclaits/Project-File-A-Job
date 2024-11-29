import React, { useEffect, useReducer, useState } from "react";
import { useHistory, Link, NavLink } from "react-router-dom";
import "./header.css";
import "./header.scss";
import { getAllMessages, countUnRead } from "../../apiCall/message";
import { addToken, removeToken } from "../../apiCall/user";
import { getFcmToken } from "../../customhook/CustomHook";
import { getMessaging, onMessage } from "firebase/messaging";
import firebaseApp from "../../container/utils/firebase";
import Notification from "../../components/home/notification";


const Header = () => {
  const [user, setUser] = useState(null);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isMobile, setIsMobile] = useState(false);
  const [isNotificationOpen, setIsNotificationOpen] = useState(false);
  const [messages, setMessages] = useState([]);
  const [update, setUpdate] = useState();
  const [token, setToken] = useState("");
  const [countUnread, setCountUnread] = useState(0);
  const [isServiceWorkerRegistered, setIsServiceWorkerRegistered] = React.useState(false);
  const [, forceUpdate] = useReducer((x) => x + 1, 0);
  const history = useHistory();
  const handleRerender = () => {
    forceUpdate();
  };
  let messageProcessed = false;
  const handleMessageReceived = (payload) => {
    //console.log("Foreground push notification received:", payload);
    const data = payload.data;
    processMessage(data);
  };

  const processMessage = (data) => {
    if(!messageProcessed) {
      //console.log(messageProcessed)
    messageProcessed = true;
    if (data?.type === "new") {
      setMessages((prev) => [
        {
          messageID: data.messageID,
          message: data.message,
          sendTime: data.sendTime,
          attachedUrl: data.attachedUrl,
          status: data.status,
          readAt: data.readAt,
          receiver: data.receiverId,
          sender: data.sender,
          subject: data.subject,
        },
        ...prev,
      ]);
      setCountUnread((prevCount) => prevCount + 1);
    } else if (data?.type === "readAll") {
      updateAllMessages(data);
      setCountUnread(0);
    } else {
      const update = data;
      if (update != null) {
        setUpdate(update);
        forceUpdate();
        setCountUnread((prevCount) => Math.max(prevCount - 1, 0));
      }
    }
    }
    else {
      messageProcessed = false;
    }

  };
  const updateAllMessages = (data) => {
    setMessages((prevMessages) =>
      prevMessages.map((message) => ({
        ...message,
        readAt: data.readAt,
        status: "read",
      }))
    );
    handleRerender();
  };

  const handleResize = () => {
    setIsMobile(window.innerWidth < 991);
  };

  useEffect(() => {
    const handleUserData = () => {
      const userData = JSON.parse(localStorage.getItem("userData"));
      setUser(userData);
    };
    handleUserData();
    window.addEventListener("resize", handleResize);
    handleResize();
    return () => {
      window.removeEventListener("resize", handleResize);
      setUser(null);
    };
  }, []);

  useEffect(() => {
    if (!token && user) {
      getFcmToken().then(async (data) => {
        setToken(data.token);
  
        if (typeof window !== "undefined" && "serviceWorker" in navigator) {
          if (data.notificationPermissionStatus === "granted") {
            navigator.serviceWorker.register("/firebase-messaging-sw.js").then(() => {
              setIsServiceWorkerRegistered(true);
  
              const messaging = getMessaging(firebaseApp);
              onMessage(messaging, (payload) => {
                payload.data.fromServiceWorker = false;
                handleMessageReceived(payload.data);
              });
  
              const channel = new BroadcastChannel("sw-messages");
              channel.addEventListener("message", (event) => {
                if (event.data.fromServiceWorker) {
                  handleMessageReceived(event.data);
                }
              });
            });
          }
        }
  
        // Lưu token vào server và lấy tin nhắn hiện có
        const userData = JSON.parse(localStorage.getItem("userData"));
        const phone = user.phoneNumber;
        if (userData) {
          addToken({ phone, registrationToken: data.token }).then(() => {});
          getAllMessages(phone).then((data) => {
            setMessages(data);
          });
  
          countUnRead(phone).then((unreadCount) => {
            setCountUnread(unreadCount);
          });
        }
      });
    }
  }, [token, user]);  
  const handleLogout = () => {
    localStorage.clear();
    setUser(null);
    outClicked()
    window.location.href = "/login";
  };

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };
  const toggleNotification = () => {
    setIsNotificationOpen(!isNotificationOpen);
  };
  // const markAllAsRead = () => {
  //   setNotifications(notifications.map(n => ({ ...n, isRead: true })));
  // };
  const outClicked = async () => {
    await removeToken(user.phoneNumber, token);
  };
  useEffect(() => {
    if (update != null) {
      const message = messages.find((mess) => mess.id === update.id);
      if (message) {
        message.readAt = update.readAt;
        message.status = "read";
        handleRerender();
      }
    }
  }, [update, messages]);
  return (
    <header className="header-area bg-light shadow-sm">
      <div className="container-fluid">
        <nav className="navbar navbar-expand-lg navbar-light bg-transparent">
          <NavLink className="navbar-brand" to="/home">
            <img
              src="/assets/img/logo.png"
              style={{ maxHeight: "50px", maxWidth: "250px" }}
              alt="Logo"
            />
          </NavLink>
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded={isMenuOpen ? "true" : "false"}
            aria-label="Toggle navigation"
            onClick={() => setIsMenuOpen(!isMenuOpen)}
          >
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className={`collapse navbar-collapse ${isMenuOpen ? "show" : ""}`} id="navbarNav">
            <ul className="navbar-nav mx-auto">
              <li className="nav-item mx-3">
                <NavLink className="nav-link" to="/home">Trang chủ</NavLink>
              </li>
              <li className="nav-item mx-3">
                <NavLink className="nav-link" to="/job">Việc làm</NavLink>
              </li>
              <li className="nav-item mx-3">
                <NavLink className="nav-link" to="/company">Công ty</NavLink>
              </li>
              <li className="nav-item mx-3">
                <NavLink className="nav-link" to="/about">Giới thiệu</NavLink>
              </li>
            </ul>

            <ul className="navbar-nav ms-auto auth-buttons">
              {user ? (
                <>
                  <Notification
                    toggleNotification={toggleNotification}
                    unreadCount={countUnread}
                    isNotificationOpen={isNotificationOpen}
                    // markAllAsRead={markAllAsRead}
                    user={user}
                    messages={messages}
                  />

                  <li className="nav-item dropdown">
                    <a className="nav-link dropdown-toggle" href="#" id="userDropdown" onClick={toggleDropdown}>
                      <img src={user.image} alt="User" className="rounded-circle" style={{ width: "30px", height: "30px" }} />
                      {user.firstName + " " + user.lastName + " "}
                      <i className="fas fa-chevron-down ms-2"></i>
                    </a>
                    <ul className={`dropdown-menu dropdown-menu-end ${isDropdownOpen ? "show" : ""}`} aria-labelledby="userDropdown">
                      <li><Link className="dropdown-item" to="/candidate/info"><i className="far fa-user text-primary"></i> Thông tin</Link></li>
                      <li><Link className="dropdown-item" to="/candidate/usersetting"><i className="fas fa-cogs text-primary"></i> Cài đặt</Link></li>
                      <li><Link className="dropdown-item" to="/candidate/cv-post"><i className="fas fa-file-alt text-primary"></i> CV đã nộp</Link></li>
                      <li><Link className="dropdown-item" to="/candidate/changepassword"><i className="fas fa-key text-primary"></i> Đổi mật khẩu</Link></li>
                      <li><button className="dropdown-item" onClick={handleLogout}><i className="fas fa-sign-out-alt text-primary"></i> Đăng xuất</button></li>
                    </ul>
                  </li>
                </>
              ) : (
                <>
                  <li className="nav-item mx-3">
                    <Link to="/register" className={`nav-link ${isMobile ? 'nav-link text-center' : 'nav-link btn btn-primary text-center'}`}>Đăng ký</Link>
                  </li>
                  <li className="nav-item mx-3">
                    <Link to="/login" className={`nav-link ${isMobile ? 'nav-link text-center' : 'nav-link btn btn-secondary text-center'}`}>Đăng nhập</Link>
                  </li>
                </>
              )}
            </ul>
          </div>
        </nav>
      </div>
    </header>
  );
};

export default Header;