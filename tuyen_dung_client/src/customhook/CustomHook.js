import { getMessaging, getToken, deleteToken } from "firebase/messaging";
import firebaseApp from "../container/utils/firebase";

const getFcmToken = async () => {
  try {
    if (typeof window !== "undefined") {
      const messaging = getMessaging(firebaseApp);
      const permission = await Notification.requestPermission();
      if (permission === "granted" && "serviceWorker" in navigator) {
        const currentToken = await getToken(messaging, {
          vapidKey:
            "BHZLF2O5bTzNItCBLsV3X6CYntoC7uXiL6RGX8EWKqD9f11DtdtVNuWRTnDVjBITep309Y3SJOUF6P6xE-dJHNo",
        });
        if (currentToken) {
          return {
            token: currentToken,
            notificationPermissionStatus: permission,
          };
        } else {
          console.log(
            "No registration token available. Request permission to generate one."
          );
        }
      }
    }
    else {
      console.log("Quyền denied");
      return { token: "", notificationPermissionStatus: "denied" };
    }
  } catch (error) {
    console.log("Error retrieving token:", error);
    return { token: "", notificationPermissionStatus: "denied" };
  }
};

const deleteRegistrationToken = async () => {
  const messaging = getMessaging(firebaseApp);
  return await deleteToken(messaging);
};

export { getFcmToken, deleteRegistrationToken };
