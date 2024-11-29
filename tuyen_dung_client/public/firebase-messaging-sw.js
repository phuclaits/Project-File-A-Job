/* eslint-env serviceworker */
/* global firebase */

importScripts(
  "https://www.gstatic.com/firebasejs/10.5.0/firebase-app-compat.js"
);
importScripts(
  "https://www.gstatic.com/firebasejs/10.5.0/firebase-messaging-compat.js"
);

const firebaseConfig = {
  apiKey: "AIzaSyC6DALmsnNatl73YBAOLFaVsem1lpK28xE",
  authDomain: "notituyendung.firebaseapp.com",
  projectId: "notituyendung",
  storageBucket: "notituyendung.appspot.com",
  messagingSenderId: "527643207465",
  appId: "1:527643207465:web:b62d2f5f784e21eac9f938",
  measurementId: "G-L3N3PZ1MP0",
};
//firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

class CustomPushEvent extends Event {
  constructor(data) {
    super("push");

    Object.assign(this, data);
    this.custom = true;
  }
}
/*
 * Overrides push notification data, to avoid having 'notification' key and firebase blocking
 * the message handler from being called
 */

const processedMessages = new Set(); // Theo dõi các thông báo đã xử lý
let lastNotificationId = null; // Lưu ID thông báo cuối cùng

/* eslint-disable no-restricted-globals */
self.addEventListener("push", (e) => {
  const oldData = e.data.json();

  // Kiểm tra xem loại thông báo là "new" và chưa được xử lý
  if (oldData.data?.type === "new" && !processedMessages.has(oldData.data.id)) {
    processedMessages.add(oldData.data.id); // Đánh dấu thông báo đã xử lý
    lastNotificationId = oldData.data.id; // Cập nhật ID thông báo cuối

    const newEvent = new CustomPushEvent({
      data: {
        ...oldData.data,
        json() {
          return oldData;
        },
      },
      waitUntil: e.waitUntil.bind(e),
    });

    e.stopImmediatePropagation();
    dispatchEvent(newEvent);

    // Hiển thị thông báo
    self.registration.showNotification(oldData.data.subject, {
      body: oldData.data.message,
      icon: "./assets/img/logo.png",
      data: oldData.data,
    });
  }
});

messaging.onBackgroundMessage((payload) => {
  console.log("Received payload in background:", payload);

  // Nếu thông báo đã được xử lý bởi sự kiện "push", bỏ qua
  if (payload.data.id === lastNotificationId) {
    console.log("Bỏ qua thông báo đã xử lý:", payload.data.id);
    return;
  }

  const channel = new BroadcastChannel("sw-messages");
  channel.postMessage(payload);

  const { subject, message } = payload.data;

  // Kiểm tra thông báo nền, chỉ xử lý nếu chưa được hiển thị
  if (payload.data.type === "new" && !processedMessages.has(payload.data.id)) {
    processedMessages.add(payload.data.id); // Đánh dấu thông báo đã xử lý

    self.registration.showNotification(subject, {
      body: message,
      icon: "./assets/img/logo.png",
      data: payload.data,
    });
  } else {
    console.log("Thông báo đã xử lý, bỏ qua:", payload.data.id);
  }
});

self.addEventListener("notificationclick", (event) => {
  if (event?.notification?.data && event?.notification?.data?.attachedUrl) {
    self.clients.openWindow(event?.notification?.data?.attachedUrl);
  }
  event.notification.close();
});
