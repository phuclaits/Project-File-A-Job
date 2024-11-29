import React from 'react'
import Header from './Header';
import Menu from './Menu';

import Footer from './Footer';

import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link
} from "react-router-dom";
import ManageCv from './Cv/ManageCv';
import FilterCv from './Cv/FilterCv';
import AddCompany from './Company/AddCompany';
import Home from './Home';
import AddUser from './User/AddUser';
import ManageUser from './User/ManageUser';
import ManageJobType from '../JobType/ManageJobType';
import AddJobType from '../JobType/AddJobType';
import ManageJobSkill from '../JobSkill/ManageJobSkill';
import AddJobSkill from '../JobSkill/AddJobSkill';
import DetailCompany from '../Company/DetailCompany';
import AddJobLevel from './JobLevel/AddJobLevel';
import ManageJobLevel from './JobLevel/ManageJobLevel';
import AddWorkType from './WorkType/AddWorkType';
import ManageWorkType from './WorkType/ManageWorkType';
import AddSalaryType from './SalaryType/AddSalaryType';
import ManageSalaryType from './SalaryType/ManageSalaryType';
import AddExpType from './ExpType/AddExpType';
import ManageExpType from './ExpType/ManageExpType';
import ProfilePage from './User/ProfileUser';
import SearchPage from './Cannidate/Search';
import DetailFilterUser from './Cv/DetailFilterUser';
import AddPost from './Post/AddPost';
import UserInfo from './User/UserInfo';
import ChangePassword from './User/ChangePassword';
import ManagePost from './Post/ManagePost';
import BuyPost from './Post/BuyPost';
import ManageCompany from '../Company/ManageCompany';
import Recruitment from './Company/Recruitment';
import ManageEmployer from './Company/ManageEmployer';
import PaymentSuccess from './Payment/BuySuccess';
import PaymentSuccessCv from './Payment/BuySuccesCv';
import BuyCv from './Payment/BuyCv';
import ApplicationListNew from './ApplicationList/ApplicationListNew';
import ApplicationListAccepted from './ApplicationList/ApplicationListAccepted';
import ApplicationListUnderReview from './ApplicationList/ApplicationListUnderReview';
import ApplicationListRejected from './ApplicationList/ApplicationListRejected';
import NotePost from './Post/NotePost';
import HistoryTradePost from './HistoryTransaction/HistoryTradePost';
import HistoryTradeCv from './HistoryTransaction/HistoryTradeCv';
import UserCv from './Cv/UserCv';
import AdminCv from './Cv/AdminCv';
import ManagePackageCv from './Package/ManagePackageCv';
import AddpackageCv from './Package/AddPackageMatchCv';
import AddPackageMatchCv from './Package/AddPackageMatchCv';
import ManagePackagePost from './Package/ManagePackagePost';
import AddpackagePost from './Package/AddpackagePost';
// import PaymentSuccessCv from './Payment/BuySuccesCv';
const HomeAdmin = () => {
    return (
        <Router>
            <Switch >
                <div className="container-scroller">
                    {/* partial:partials/_navbar.html */}
                    <Header />
                    {/* partial */}
                    <div className="container-fluid page-body-wrapper">
                        {/* partial:partials/_settings-panel.html */}
                        <div className="theme-setting-wrapper">
                            <div id="settings-trigger"><i className="ti-settings" /></div>
                            <div id="theme-settings" className="settings-panel">
                                <i className="settings-close ti-close" />
                                <p className="settings-heading">Slidebar Project</p>
                                <div className="sidebar-bg-options selected" id="sidebar-light-theme"><div className="img-ss rounded-circle bg-light border mr-3" />Light</div>
                                <div className="sidebar-bg-options" id="sidebar-dark-theme"><div className="img-ss rounded-circle bg-dark border mr-3" />Dark</div>
                                <p className="settings-heading mt-2">Header Project</p>
                                <div className="color-tiles mx-0 px-4">
                                    <div className="tiles success" />
                                    <div className="tiles warning" />
                                    <div className="tiles danger" />
                                    <div className="tiles info" />
                                    <div className="tiles dark" />
                                    <div className="tiles default" />
                                </div>
                            </div>
                        </div>
                        <div id="right-sidebar" className="settings-panel">
                            <i className="settings-close ti-close" />
                            <ul className="nav nav-tabs border-top" id="setting-panel" role="tablist">
                                <li className="nav-item">
                                    <a className="nav-link active" id="todo-tab" data-toggle="tab" href="#todo-section" role="tab" aria-controls="todo-section" aria-expanded="true">TO DO LIST</a>
                                </li>
                                <li className="nav-item">
                                    <a className="nav-link" id="chats-tab" data-toggle="tab" href="#chats-section" role="tab" aria-controls="chats-section">CHATS</a>
                                </li>
                            </ul>
                            <div className="tab-content" id="setting-content">
                                <div className="tab-pane fade show active scroll-wrapper" id="todo-section" role="tabpanel" aria-labelledby="todo-section">
                                    <div className="add-items d-flex px-3 mb-0">
                                        <form className="form w-100">
                                            <div className="form-group d-flex">
                                                <input type="text" className="form-control todo-list-input" placeholder="Add To-do" />
                                                <button type="submit" className="add btn btn-primary todo-list-add-btn" id="add-task">Add</button>
                                            </div>
                                        </form>
                                    </div>
                                    <div className="list-wrapper px-3">
                                        <ul className="d-flex flex-column-reverse todo-list">
                                            <li>
                                                <div className="form-check">
                                                    <label className="form-check-label">
                                                        <input className="checkbox" type="checkbox" />
                                                        Team review meeting at 3.00 PM
                                                    </label>
                                                </div>
                                                <i className="remove ti-close" />
                                            </li>
                                            <li>
                                                <div className="form-check">
                                                    <label className="form-check-label">
                                                        <input className="checkbox" type="checkbox" />
                                                        Prepare for presentation
                                                    </label>
                                                </div>
                                                <i className="remove ti-close" />
                                            </li>
                                            <li>
                                                <div className="form-check">
                                                    <label className="form-check-label">
                                                        <input className="checkbox" type="checkbox" />
                                                        Resolve all the low priority tickets due today
                                                    </label>
                                                </div>
                                                <i className="remove ti-close" />
                                            </li>
                                            <li className="completed">
                                                <div className="form-check">
                                                    <label className="form-check-label">
                                                        <input className="checkbox" type="checkbox" defaultChecked />
                                                        Schedule meeting for next week
                                                    </label>
                                                </div>
                                                <i className="remove ti-close" />
                                            </li>
                                            <li className="completed">
                                                <div className="form-check">
                                                    <label className="form-check-label">
                                                        <input className="checkbox" type="checkbox" defaultChecked />
                                                        Project review
                                                    </label>
                                                </div>
                                                <i className="remove ti-close" />
                                            </li>
                                        </ul>
                                    </div>
                                    <h4 className="px-3 text-muted mt-5 font-weight-light mb-0">Events</h4>
                                    <div className="events pt-4 px-3">
                                        <div className="wrapper d-flex mb-2">
                                            <i className="ti-control-record text-primary mr-2" />
                                            <span>Feb 11 2018</span>
                                        </div>
                                        <p className="mb-0 font-weight-thin text-gray">Creating component page build a js</p>
                                        <p className="text-gray mb-0">The total number of sessions</p>
                                    </div>
                                    <div className="events pt-4 px-3">
                                        <div className="wrapper d-flex mb-2">
                                            <i className="ti-control-record text-primary mr-2" />
                                            <span>Feb 7 2018</span>
                                        </div>
                                        <p className="mb-0 font-weight-thin text-gray">Meeting with Alisa</p>
                                        <p className="text-gray mb-0 ">Call Sarah Graves</p>
                                    </div>
                                </div>
                                {/* To do section tab ends */}
                                <div className="tab-pane fade" id="chats-section" role="tabpanel" aria-labelledby="chats-section">
                                    <div className="d-flex align-items-center justify-content-between border-bottom">
                                        <p className="settings-heading border-top-0 mb-3 pl-3 pt-0 border-bottom-0 pb-0">Friends</p>
                                        <small className="settings-heading border-top-0 mb-3 pt-0 border-bottom-0 pb-0 pr-3 font-weight-normal">See All</small>
                                    </div>
                                    <ul className="chat-list">
                                        <li className="list active">
                                            <div className="profile"><img src="assetsAdmin/images/faces/face1.jpg" alt="image" /><span className="online" /></div>
                                            <div className="info">
                                                <p>Thomas Douglas</p>
                                                <p>Available</p>
                                            </div>
                                            <small className="text-muted my-auto">19 min</small>
                                        </li>
                                        <li className="list">
                                            <div className="profile"><img src="assetsAdmin/images/faces/face2.jpg" alt="image" /><span className="offline" /></div>
                                            <div className="info">
                                                <div className="wrapper d-flex">
                                                    <p>Catherine</p>
                                                </div>
                                                <p>Away</p>
                                            </div>
                                            <div className="badge badge-success badge-pill my-auto mx-2">4</div>
                                            <small className="text-muted my-auto">23 min</small>
                                        </li>
                                        <li className="list">
                                            <div className="profile"><img src="assetsAdmin/images/faces/face3.jpg" alt="image" /><span className="online" /></div>
                                            <div className="info">
                                                <p>Daniel Russell</p>
                                                <p>Available</p>
                                            </div>
                                            <small className="text-muted my-auto">14 min</small>
                                        </li>
                                        <li className="list">
                                            <div className="profile"><img src="assetsAdmin/images/faces/face4.jpg" alt="image" /><span className="offline" /></div>
                                            <div className="info">
                                                <p>James Richardson</p>
                                                <p>Away</p>
                                            </div>
                                            <small className="text-muted my-auto">2 min</small>
                                        </li>
                                        <li className="list">
                                            <div className="profile"><img src="assetsAdmin/images/faces/face5.jpg" alt="image" /><span className="online" /></div>
                                            <div className="info">
                                                <p>Madeline Kennedy</p>
                                                <p>Available</p>
                                            </div>
                                            <small className="text-muted my-auto">5 min</small>
                                        </li>
                                        <li className="list">
                                            <div className="profile"><img src="assetsAdmin/images/faces/face6.jpg" alt="image" /><span className="online" /></div>
                                            <div className="info">
                                                <p>Sarah Graves</p>
                                                <p>Available</p>
                                            </div>
                                            <small className="text-muted my-auto">47 min</small>
                                        </li>
                                    </ul>
                                </div>
                                {/* chat tab ends */}
                            </div>
                        </div>
                        {/* partial */}
                        {/* partial:partials/_sidebar.html */}
                        <Menu />
                        {/* partial */}
                        <div className="main-panel">
                            <div className="content-wrapper">
                                <Route exact path="/admin/">
                                    <Home />
                                </Route>
                                <Route exact path="/admin/user-info/">
                                    <UserInfo />
                                </Route>
                                <Route exact path="/admin/changepassword/">
                                    <ChangePassword />
                                </Route>
                                <Route exact path="/admin/add-company">
                                    <AddCompany />
                                </Route>
                                <Route exact path="/admin/recruitment">
                                    <Recruitment />
                                </Route>
                                <Route exact path="/admin/list-employer">
                                    <ManageEmployer />
                                </Route>
                                <Route exact path="/admin/list-user">
                                    <ManageUser />
                                </Route>
                                <Route exact path="/admin/add-user">
                                    <AddUser />
                                </Route>
                                <Route exact path="/admin/edit-user/:id">
                                    <AddUser />
                                </Route>
                                <Route exact path="/admin/detail-user/:id">
                                    <ProfilePage />
                                </Route>
                                <Route exact path="/admin/list-job-type">
                                    <ManageJobType />
                                </Route>
                                <Route exact path="/admin/add-job-type">
                                    <AddJobType />
                                </Route>
                                <Route exact path="/admin/edit-job-type/:code">
                                    <AddJobType />
                                </Route>
                                <Route exact path="/admin/add-job-skill">
                                    <AddJobSkill />
                                </Route>
                                <Route exact path="/admin/list-job-skill">
                                    <ManageJobSkill />
                                </Route>
                                <Route exact path="/admin/edit-job-skill/:code">
                                    <AddJobSkill />
                                </Route>

                                <Route exact path="/admin/list-company-admin/">
                                    <ManageCompany />
                                </Route>
                                <Route exact path="/admin/edit-company">
                                    <AddCompany />
                                </Route>
                                <Route exact path="/admin/edit-company-admin/:id">
                                    <AddCompany />
                                </Route>

                                <Route exact path="/admin/add-job-level">
                                    <AddJobLevel />
                                </Route>
                                <Route exact path="/admin/list-job-level">
                                    <ManageJobLevel />
                                </Route>
                                <Route exact path="/admin/edit-job-level/:id">
                                    <AddJobLevel />
                                </Route>

                                <Route exact path="/admin/add-work-type">
                                    <AddWorkType />
                                </Route>
                                <Route exact path="/admin/list-work-type">
                                    <ManageWorkType />
                                </Route>
                                <Route exact path="/admin/edit-work-type/:id">
                                    <AddWorkType />
                                </Route>

                                <Route exact path="/admin/add-salary-type">
                                    <AddSalaryType />
                                </Route>
                                <Route exact path="/admin/list-salary-type">
                                    <ManageSalaryType />
                                </Route>
                                <Route exact path="/admin/edit-salary-type/:id">
                                    <AddSalaryType />
                                </Route>

                                <Route exact path="/admin/add-exp-type">
                                    <AddExpType />
                                </Route>
                                <Route exact path="/admin/list-exp-type">
                                    <ManageExpType />
                                </Route>
                                <Route exact path="/admin/edit-exp-type/:id">
                                    <AddExpType />
                                </Route>
                                <Route exact path="/admin/search-cannidate">
                                    <SearchPage />
                                </Route>
                                <Route exact path="/admin/note/:id">
                                    <NotePost />
                                </Route>
                                {/* candicate */}
                                <Route exact path="/admin/list-cv/:id">
                                    <ManageCv />
                                </Route>
                                <Route exact path="/admin/list-candiate/">
                                    <FilterCv />
                                </Route>
                                <Route exact path="/admin/candiate/:id">
                                    <DetailFilterUser />
                                </Route>
                                
                                <Route exact path="/admin/add-post">
                                    <AddPost />
                                </Route>
                                <Route exact path="/admin/edit-post/:id">
                                    <AddPost />
                                </Route>
                                <Route exact path="/admin/list-post/">
                                    <ManagePost />
                                </Route>
                                <Route exact path="/admin/list-post/:id">
                                    <ManagePost />
                                </Route>
                                <Route exact path="/admin/buy-post/">
                                    <BuyPost />
                                </Route>
                                <Route exact path="/admin/paymentCv/success">
                                    <PaymentSuccessCv />
                                </Route>
                                
                                <Route exact path="/admin/payment/success">
                                    <PaymentSuccess />
                                </Route>
                                <Route exact path="/admin/buy-cv/">
                                    <BuyCv />
                                </Route>
                                <Route exact path="/admin/history-post/">
                                    {/* <HistoryTradePost /> */}
                                </Route>
                                <Route exact path="/admin/history-cv/">
                                    {/* <HistoryTradeCv /> */}
                                </Route>
                                
                            {/*admin*/}    <Route exact path="/admin/list-post-admin/">
                                    <ManagePost />
                                </Route>{/*admin*/}

                                <Route path="/admin/application-list/new">
                                <Header />
                                <ApplicationListNew />
                                
                                </Route>

                                <Route path="/admin/application-list/under-review">
                                <Header />
                                <ApplicationListUnderReview />
                                
                                </Route>

                                <Route path="/admin/application-list/accepted">
                                <Header />
                                <ApplicationListAccepted />
                                
                                </Route>

                                <Route path="/admin/application-list/rejected">
                                <Header />
                                <ApplicationListRejected />
                                
                                </Route>
                                <Route exact path="/admin/history-post/">
                                    <HistoryTradePost />
                                </Route>
                                <Route exact path="/admin/history-cv/">
                                    <HistoryTradeCv />
                                    </Route>
                                <Route exact path="/admin/admin-cv/:id">
                                    <AdminCv />
                                </Route>
                                <Route exact path="/admin/list-package-cv">
                                    <ManagePackageCv />
                                </Route>
                                <Route exact path="/admin/add-package-cv">
                                    <AddPackageMatchCv />
                                </Route>
                                <Route exact path="/admin/edit-package-cv/:id">
                                    <AddPackageMatchCv />
                                </Route>
                                <Route exact path="/admin/add-package-post">
                                    <AddpackagePost />
                                </Route>
                                <Route exact path="/admin/list-package-post">
                                    <ManagePackagePost />
                                </Route>
                                <Route exact path="/admin/edit-package-post/:id">
                                    <AddpackagePost />
                                </Route>

                            </div>
                            {/* content-wrapper ends */}
                            {/* partial:partials/_footer.html */}
                            <Footer />
                            {/* partial */}
                        </div>
                        {/* main-panel ends */}
                    </div>
                    {/* page-body-wrapper ends */}
                </div>
            </Switch >
        </Router>
    )
}

export default HomeAdmin
