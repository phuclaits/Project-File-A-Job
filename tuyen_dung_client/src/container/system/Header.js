import React, { useEffect, useReducer, useState } from "react";
import { useHistory, Link, NavLink } from "react-router-dom";

import { getAllMessages, countUnRead } from "../../apiCall/message";
import { addToken, removeToken } from "../../apiCall/user";
import { getFcmToken } from "../../customhook/CustomHook";
import { getMessaging, onMessage } from "firebase/messaging";
import firebaseApp from "../../container/utils/firebase";
import Notification from "../../components/home/notification";
const Header = () => {
  const [isNotificationOpen, setIsNotificationOpen] = useState(false);
  const [messages, setMessages] = useState([]);
  const [update, setUpdate] = useState();
  const [token, setToken] = useState("");
  const [countUnread, setCountUnread] = useState(0);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isServiceWorkerRegistered, setIsServiceWorkerRegistered] = React.useState(false);
  const [, forceUpdate] = useReducer((x) => x + 1, 0);
  let history = useHistory();
  const [user, setUser] = useState({})
  let handleLogout = () => {
    console.log("hello")
    localStorage.clear();
    outClicked()
    window.location.href = "/login"
  }
  const handleRerender = () => {
    forceUpdate();
  };
  let messageProcessed = false;
  const handleMessageReceived = (payload) => {
    console.log("Foreground push notification received:", payload);
    const data = payload.data;
    processMessage(data);
  };

  const processMessage = (data) => {
    if (!messageProcessed) {
      console.log(messageProcessed)
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
      console.log(messageProcessed)
      messageProcessed = false;
      console.log(messageProcessed)
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
  useEffect(() => {
    if (!token && user) {
      getFcmToken().then(async (data) => {
        setToken(data.token);
        if (typeof window !== "undefined" && "serviceWorker" in navigator) {
          if (data.notificationPermissionStatus === "granted") {
            navigator.serviceWorker
              .register("/firebase-messaging-sw.js")
              .then(() => {
                setIsServiceWorkerRegistered(true);
                const messaging = getMessaging(firebaseApp);
                onMessage(messaging, handleMessageReceived);
                const channel = new BroadcastChannel("sw-messages");
                channel.addEventListener("message", (event) => {
                  handleMessageReceived(event.data);
                });
              });
          }
        }
        const userData = JSON.parse(localStorage.getItem("userData"));

        const phone = user.phoneNumber
        if (userData) {
          addToken({
            phone: phone,
            registrationToken: data.token,
          }).then(() => {
          });
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
  useEffect(() => {
    const userData = JSON.parse(localStorage.getItem('userData'));
    setUser(userData)
  }, [])
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
    <nav className="navbar col-lg-12 col-12 p-0 fixed-top d-flex flex-row">
      <div className="text-center navbar-brand-wrapper d-flex align-items-center justify-content-center">
        <Link className="navbar-brand brand-logo mr-5" to={"/admin/"}><img src="/assets/img/logo.png" className="mr-2" alt="logo" /></Link>
        <a className="navbar-brand brand-logo-mini" href="index.html"><img src="/assetsAdmin/images/logo-mini.svg" alt="logo" /></a>
      </div>
      <div className="navbar-menu-wrapper d-flex align-items-center justify-content-end">
        <button className="navbar-toggler navbar-toggler align-self-center" type="button" data-toggle="minimize">
          <span className="icon-menu" />
        </button>
        <ul className="navbar-nav navbar-nav-right">
          {user.codeRoleAccount !== "ADMIN" && (
            <li>
              <Notification
                toggleNotification={toggleNotification}
                unreadCount={countUnread}
                isNotificationOpen={isNotificationOpen}
                // markAllAsRead={markAllAsRead}
                user={user}
                messages={messages}
              />
            </li>
          )}
          <li className="nav-item nav-profile dropdown">
            <a className="nav-link dropdown-toggle" href="#" data-toggle="dropdown" id="profileDropdown">
              <img style={{ objectFit: 'cover' }} src={user.image} alt="profile" />
            </a>
            <div className="dropdown-menu dropdown-menu-right navbar-dropdown" aria-labelledby="profileDropdown">
              <Link to={'/admin/user-info/'} className="dropdown-item">
                <i className="far fa-user text-primary"></i>
                Thông tin

              </Link>
              <Link to={'/admin/changepassword/'} className="dropdown-item">
                <i className="ti-settings text-primary" />
                Đổi mật khẩu
              </Link>
              <a onClick={() => handleLogout()} className="dropdown-item">
                <i className="ti-power-off text-primary" />
                Đăng xuất
              </a>
            </div>
          </li>

        </ul>
        <button className="navbar-toggler navbar-toggler-right d-lg-none align-self-center" type="button" data-toggle="offcanvas">
          <span className="icon-menu" />
        </button>
      </div>
    </nav>
  )
}

export default Header
