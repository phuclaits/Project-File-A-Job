import React from 'react'
import { Link, NavLink } from "react-router-dom";
const Footer = () => {
    return (
        <>
            <footer>
                {/* <!-- Footer Start--> */}

                {/* <!-- footer-bottom area --> */}
                <div className="footer-bottom-area footer-bg">
                    <div className="container">
                        <div className="footer-border">
                            <div className="row d-flex justify-content-between align-items-center">
                                <div className="col-xl-10 col-lg-10 ">
                                    <div className="footer-copy-right">
                                        <p> 
                                          TuyenDungViecLam.com  
                                        </p>
                                    </div>
                                </div>
                                <div className="col-xl-2 col-lg-2">
                                    <div className="footer-social f-right">
                                        <a href="https://www.facebook.com/lahoangphuc1312"><i className="fab fa-facebook-f"></i></a>
                                        <a href="#"><i className="fab fa-twitter"></i></a>
                                        <a href="#"><i className="fas fa-globe"></i></a>
                                        
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </footer>
        </>
    )
}

export default Footer
