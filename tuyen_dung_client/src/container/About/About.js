import React from 'react';
import './style.css';
import { Link } from 'react-router-dom/cjs/react-router-dom.min';
const About = () => {
    const members = [
        {
            name: 'La Hoàng Phúc',
            role: 'Full Stack Engineer',
            major: 'Data Scientist',
            image: '/assets/avtphuc.jpg', 
        },
        {
            name: 'Nguyễn Thành Luân',
            role: 'Backend Developer',
            major: 'Software Engineer',
            image: '/assets/avtluan.jpg', 
        },
        {
            name: 'Nguyễn Anh Kiệt',
            role: 'Frontend Developer',
            major: 'Mobile Developer',
            image: '/assets/avtkiet.jpg', 
        },
    ];

    return (
        <main>
            {/* Hero Section */}
            <div className="slider-area">
                <div
                    className="single-slider section-overly slider-height2 d-flex align-items-center"
                    style={{ backgroundImage: `url("assets/img/hero/about.jpg")` }}
                >
                    <div className="container">
                        <div className="row">
                            <div className="col-xl-12">
                                <div className="hero-cap text-center">
                                    <h2>Giới thiệu đội nhóm</h2>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Team Members Section */}
            <div className="team-area section-padding">
                <div className="container">
                    <div className="row">
                        {members.map((member, index) => (
                            <div className="col-lg-4 col-md-6" key={index}>
                                <div className="single-team text-center">
                                    <div className="team-img">
                                        <img src={member.image} alt={member.name} style={{width:350 }}/>
                                    </div>
                                    <div className="team-caption">
                                        <h4>{member.name}</h4>
                                        <p>{member.role}</p>
                                        <p>{member.major}</p>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
            <div className="personal-info text-center">
                <div className="section-tittle section-tittle2">
                    <span>Thông tin về chúng tôi</span>
                    <h2>Project Website Tuyển Dụng Việc Làm</h2>
                </div>
                <div className="support-caption">
                    <p className="pera-top">
                        Tôi là La Hoàng Phúc với tư cách là nhóm trưởng, hiện đang làm việc với vai trò Full Stack Engineer. Tôi có
                        niềm đam mê đặc biệt với Data Science và luôn tìm kiếm những cơ hội để ứng dụng
                        dữ liệu vào việc giải quyết các bài toán thực tế. Với nền tảng kỹ thuật vững chắc và kinh nghiệm đa dạng, tôi mong muốn mang lại
                        giá trị cho cộng đồng với đồ án này giúp các doanh nghiệp và ứng viên có thể kết nối với nhau.
                    </p>
                    <p>
                    Với sự hợp tác chung giữa 3 thành viên tạo nên một thương hiệu website Tuyển Dụng Việc Làm
                    </p>
                    <Link to={'/login'} className="btn post-btn">
                        Tham gia ngay
                    </Link>
                </div>
            </div>
        </main>
    );
};

export default About;