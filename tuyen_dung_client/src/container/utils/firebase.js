import { firebaseConfig } from "../../config/firebase-config";
import { initializeApp } from "firebase/app";
// Initialize Firebase
// console.log(firebaseConfig);
// const firebaseApp = initializeApp(firebaseConfig_sub);
const firebaseApp = initializeApp(firebaseConfig);
export default firebaseApp;



// // Import the functions you need from the SDKs you need
// import { initializeApp } from "firebase/app";
// import { getAnalytics } from "firebase/analytics";
// // TODO: Add SDKs for Firebase products that you want to use
// // https://firebase.google.com/docs/web/setup#available-libraries

// // Your web app's Firebase configuration
// // For Firebase JS SDK v7.20.0 and later, measurementId is optional
// const firebaseConfig = {
//   apiKey: "AIzaSyC6DALmsnNatl73YBAOLFaVsem1lpK28xE",
//   authDomain: "notituyendung.firebaseapp.com",
//   projectId: "notituyendung",
//   storageBucket: "notituyendung.appspot.com",
//   messagingSenderId: "527643207465",
//   appId: "1:527643207465:web:b62d2f5f784e21eac9f938",
//   measurementId: "G-L3N3PZ1MP0"
// };

// // Initialize Firebase
// const app = initializeApp(firebaseConfig);
// const analytics = getAnalytics(app);