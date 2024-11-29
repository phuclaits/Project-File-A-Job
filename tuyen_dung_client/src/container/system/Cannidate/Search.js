import React from "react";
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import axios from 'axios';

function SearchPage() {
  const [searchParams, setSearchParams] = useState({
    categoryJobCode: '',
    salaryJobCode: '',
    skillName: '',
    firstName: '',
    lastName: '',
  });
  const [candidates, setCandidates] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setSearchParams(prev => ({ ...prev, [name]: value }));
  };

  const searchCandidates = async (page = 0) => {
    try {
      const response = await axios.get('http://localhost:8080/app-tuyen-dung/api/v1/user/search-users', {
        params: { ...searchParams, page },
      });
      setCandidates(response.data.result.content);
      setTotalPages(response.data.result.totalPages);
      setCurrentPage(page);
    } catch (error) {
      console.error('Error fetching candidates:', error);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    searchCandidates();
  };

  return (
    <div className="container mt-5">
      <h1 className="mb-4">Tìm kiếm ứng viên</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <div className="form-group col-md-4">
            <label htmlFor="categoryJobCode">Ngành nghề</label>
            <input
              type="text"
              className="form-control"
              id="categoryJobCode"
              name="categoryJobCode"
              value={searchParams.categoryJobCode}
              onChange={handleInputChange}
              placeholder="Nhập ngành nghề"
            />
          </div>
          <div className="form-group col-md-4">
            <label htmlFor="salaryJobCode">Mức lương</label>
            <input
              type="text"
              className="form-control"
              id="salaryJobCode"
              name="salaryJobCode"
              value={searchParams.salaryJobCode}
              onChange={handleInputChange}
              placeholder="Nhập mức lương"
            />
          </div>
          <div className="form-group col-md-4">
            <label htmlFor="skillName">Kỹ năng</label>
            <input
              type="text"
              className="form-control"
              id="skillName"
              name="skillName"
              value={searchParams.skillName}
              onChange={handleInputChange}
              placeholder="Nhập kỹ năng"
            />
          </div>
        </div>
        <div className="form-row">
          <div className="form-group col-md-6">
            <label htmlFor="firstName">Tên</label>
            <input
              type="text"
              className="form-control"
              id="firstName"
              name="firstName"
              value={searchParams.firstName}
              onChange={handleInputChange}
              placeholder="Nhập tên"
            />
          </div>
          <div className="form-group col-md-6">
            <label htmlFor="lastName">Họ</label>
            <input
              type="text"
              className="form-control"
              id="lastName"
              name="lastName"
              value={searchParams.lastName}
              onChange={handleInputChange}
              placeholder="Nhập họ"
            />
          </div>
        </div>
        <button type="submit" className="btn btn-primary">Tìm kiếm</button>
      </form>

      <div className="mt-5">
        <h2>Kết quả tìm kiếm</h2>
        {candidates.map(candidate => (
          <div key={candidate.id} className="card mb-3">
            <div className="card-body">
              <h5 className="card-title">
                {candidate.userAccountData.firstName} {candidate.userAccountData.lastName}
              </h5>
              <p className="card-text">{candidate.userAccountData.email}</p>
              <p className="card-text">
                Kỹ năng: {candidate.listSkills.map(skill => skill.skill.name).join(', ')}
              </p>
              <Link to={`/admin/detail-user/${candidate.id}`} className="btn btn-info">Xem hồ sơ</Link>
            </div>
          </div>
        ))}
      </div>

      <nav>
        <ul className="pagination">
          {Array.from({ length: totalPages }, (_, i) => (
            <li key={i} className={`page-item ${currentPage === i ? 'active' : ''}`}>
              <button className="page-link" onClick={() => searchCandidates(i)}>{i + 1}</button>
            </li>
          ))}
        </ul>
      </nav>
    </div>
  );
}

export default SearchPage;
