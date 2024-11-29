import instance from "../config/axios-config";

export const getMessage = async (messageId) => {
  const messageResponse = await instance.get(`/message?messageId=${messageId}`);
  return messageResponse.data
};

export const getAllMessages = async (userId) => {
  const messagesResponse = await instance.get(`/message/receiver/${userId}`);
  const mes = messagesResponse.data
  return mes
};

export const sendMessage = async (data) => {
  const messageData = {
    message: data.message,
    receiverId: data.receiverId,
    subject: data.subject
  };
  const sendResponse = await instance.post(`/message`, messageData);
  return sendResponse.data;
};

export const countUnRead = async (phoneNumber) => {
  const countResponse = await instance.get(`/message/un-read/${phoneNumber}`);
  return countResponse.data.count
};

export const markAllAsRead = async (phoneNumber) => {
  const response = await instance.put(`/notification/read-all/${phoneNumber}`, {});
  return response.data;
};

export const updateReadStatus = async (userId, messageId) => {
  const data = {
    userId: userId,
    messageId: messageId,
  };
  const response = await instance.put(`/notification/updateReadStatus`, data, {
    headers: { 'Content-Type': 'application/json' },
  });
  return response.data;
};

export const sendUserNotification = async ({
  subject,
  image,
  message,
  attachedUrl,
  sender,
  userId,
}) => {
  const payload = {
    subject,
    image,
    data: {
      message,
      attachedUrl,
      sender,
    },
    userId,
  };

  try {
    const response = await instance.post(`/notification/sendUser`, payload, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response.data;
  } catch (error) {
    console.error("Lỗi khi gửi thông báo:", error);
    throw error;
  }
};


// const subject = "Tuyen Dung 222";
// const image = "test.jpg";
// const message = "Send Tuyen Dung 2112";
// const attachedUrl = "/detail-job/1";
// const sender = "Admin";
// const userId = ["0388336472"];

// sendUserNotification({
//   token,
//   subject,
//   image,
//   message,
//   attachedUrl,
//   sender,
//   userId,
// })
//   .then((response) => {
//     console.log("Gửi thông báo thành công:", response);
//   })
//   .catch((error) => {
//     console.error("Lỗi khi gửi thông báo:", error);
//   });