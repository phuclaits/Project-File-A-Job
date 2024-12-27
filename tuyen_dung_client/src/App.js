import React, { useEffect, useState } from "react";
import Header from "./container/header/header";
import Footer from "./container/footer/Footer";
import Home from "./container/home/home";
import About from "./container/About/About";
import Contact from "./container/Contact/Contact";
import "react-toastify/dist/ReactToastify.css";
import JobPage from "./container/JobPage/JobPage";
import HomeAdmin from "./container/system/HomeAdmin";
import HomeCandidate from "./container/Candidate/HomeCandidate";
import { ToastContainer } from "react-toastify";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";
import Otp from "./container/login/Otp";
import Login from "./container/login/Login";
import Register from "./container/login/Register";
import ForgetPassword from "./container/login/ForgetPassword";
import JobDetail from "./container/JobDetail/JobDetail";
import ListCompany from "./container/Company/ListCompany";
import DetailCompany from "./container/Company/DetailCompany";
import UserList from "./components/Chat/UserList";
import ChatBox from "./components/Chat/ChatBox";
import { MessageCircleMore } from "lucide-react";
// import HomeCandidate from "./container/Candidate/HomeCandidate";
function App() {
  const [isChatOpen, setIsChatOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

  const handleUserSelect = (user) => {
    setSelectedUser(user);
  };

  return (
    <Router>
      <Switch>
        <div className="App">
          <Route exact path="/">
            <Redirect to="/home" />
          </Route>
          <Route exact path="/home">
            <Header />
            <Home />
            <Footer />
          </Route>
          <Route path="/about">
            <Header />
            <About />
            <Footer />
          </Route>
          <Route path="/contact">
            <Header />
            <Contact />
            <Footer />
          </Route>
          <Route path="/job">
            <Header />
            <JobPage />
            <Footer />
          </Route>

          <Route path="/company">
            <Header />
            <ListCompany />
            <Footer />
          </Route>
          <Route path="/detail-company/:id">
            <Header />
            <DetailCompany />
            <Footer />
          </Route>
          <Route
            path="/admin/"
            render={() => {
              if (
                JSON.parse(localStorage.getItem("userData")) &&
                (JSON.parse(localStorage.getItem("userData"))
                  .codeRoleAccount === "ADMIN" ||
                  JSON.parse(localStorage.getItem("userData"))
                    .codeRoleAccount === "EMPLOYER" ||
                  JSON.parse(localStorage.getItem("userData"))
                    .codeRoleAccount === "COMPANY")
              ) {
                return <HomeAdmin />;
              } else {
                return <Redirect to={"/login"} />;
              }
            }}
          ></Route>
          <Route
            path="/candidate/"
            render={() => {
              if (
                JSON.parse(localStorage.getItem("userData")) &&
                JSON.parse(localStorage.getItem("userData")).codeRoleAccount ===
                  "CANDIDATE"
              ) {
                return (
                  <>
                    <Header />
                    <HomeCandidate />
                    <Footer />
                  </>
                );
              } else {
                return <Redirect to={"/login"} />;
              }
            }}
          ></Route>

          <Route path="/login">
            <Header />
            <Login />
            <Footer />
          </Route>
          <Route path="/register">
            <Header />
            <Register />
            <Footer />
          </Route>
          <Route path="/forget-password">
            <Header />
            <ForgetPassword />
            <Footer />
          </Route>

          {/* <DetailPage /> */}
          <Route path="/detail-job/:id">
            <Header />
            <JobDetail />
            <Footer />
          </Route>

          <ToastContainer
            position="top-right"
            autoClose={4000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
          />
          <div className="relative h-screen">
            {!isChatOpen && (
              <button
                onClick={() => setIsChatOpen(true)}
                className="fixed bottom-5 right-5 bg-blue-500 text-white p-3 rounded-full shadow-lg"
              >
                <MessageCircleMore size={24} />
              </button>
            )}
            {isChatOpen && (
              <div className="fixed bottom-5 right-5 w-96 h-50 bg-white">
                {!selectedUser ? (
                  <UserList
                    onUserSelect={handleUserSelect}
                    onClose={() => setIsChatOpen(false)}
                  />
                ) : (
                  <ChatBox
                    user={selectedUser}
                    onBack={() => setSelectedUser(null)}
                    onClose={() => {
                      setSelectedUser(null);
                      setIsChatOpen(false);
                    }}
                  />
                )}
              </div>
            )}
          </div>
        </div>
      </Switch>
    </Router>
  );
}

export default App;
