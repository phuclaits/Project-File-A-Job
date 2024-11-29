import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';

function ProfilePage() {
  const { id } = useParams();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/app-tuyen-dung/api/v1/user/get-users/${id}`);
        setProfile(response.data.result);
      } catch (error) {
        console.error('Error fetching profile:', error);
        setError('Có lỗi xảy ra khi tải thông tin hồ sơ.');
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, [id]);

  if (loading) {
    return <div className="container mt-5">Đang tải...</div>;
  }

  if (error) {
    return <div className="container mt-5 text-danger">{error}</div>;
  }

  if (!profile) {
    return <div className="container mt-5">Không tìm thấy thông tin hồ sơ.</div>;
  }

  const getValue = (obj, path) => {
    return path.split('.').reduce((acc, part) => acc && acc[part], obj) ?? "Chưa có dữ liệu";
  };

  // Hàm chuyển Base64 thành Blob
  const base64ToBlob = (base64) => {
    const byteCharacters = atob(base64.split(',')[1]);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    return new Blob([byteArray], { type: 'application/pdf' });
  };

  // Hàm tạo URL từ Base64
  const createPdfUrl = (base64) => {
    if (!base64) return null;
    const blob = base64ToBlob(base64);
    return URL.createObjectURL(blob);
  };

  return (
    <div className="container mt-5">
      <div className="card">
        <div className="card-body">
          <div className="row">
            <div className="col-md-4 border-right">
              <div className="d-flex flex-column align-items-center text-center p-3 py-5">
                <img 
                  className="rounded-circle mt-5" 
                  width="150px" 
                  src={getValue(profile, 'userAccountData.image') || 'https://st3.depositphotos.com/15648834/17930/v/600/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg'}
                  alt="Profile"
                />
                <span className="font-weight-bold mt-3">
                  {getValue(profile, 'userAccountData.firstName')} {getValue(profile, 'userAccountData.lastName')}
                </span>
                <span className="text-black-50">{getValue(profile, 'userAccountData.email')}</span>
                <span>{getValue(profile, 'phoneNumber')}</span>
              </div>
            </div>
            <div className="col-md-8">
              <div className="p-3 py-5">
                <div className="d-flex justify-content-between align-items-center mb-3">
                  <h4 className="text-right">Thông tin cá nhân</h4>
                </div>
                <div className="row mt-3">
                  <div className="col-md-6"><label className="labels">Họ</label><input type="text" className="form-control" placeholder={getValue(profile, 'userAccountData.firstName')} readOnly /></div>
                  <div className="col-md-6"><label className="labels">Tên</label><input type="text" className="form-control" placeholder={getValue(profile, 'userAccountData.lastName')} readOnly /></div>
                </div>
                <div className="row mt-3">
                  <div className="col-md-12"><label className="labels">Số điện thoại</label><input type="text" className="form-control" placeholder={getValue(profile, 'phoneNumber')} readOnly /></div>
                  <div className="col-md-12"><label className="labels">Địa chỉ</label><input type="text" className="form-control" placeholder={getValue(profile, 'userAccountData.addressUser')} readOnly /></div>
                  <div className="col-md-12"><label className="labels">Email</label><input type="text" className="form-control" placeholder={getValue(profile, 'userAccountData.email')} readOnly /></div>
                  <div className="col-md-12"><label className="labels">Giới tính</label><input type="text" className="form-control" placeholder={getValue(profile, 'userAccountData.genderCodeValue.value')} readOnly /></div>
                </div>
                <div className="mt-5">
                  <h4>Thông tin nghề nghiệp</h4>
                  <div className="col-md-12"><label className="labels">Ngành nghề</label><input type="text" className="form-control" placeholder={getValue(profile, 'userAccountData.userSettingData.categoryJobCode.value')} readOnly /></div>
                  <div className="col-md-12"><label className="labels">Địa điểm làm việc</label><input type="text" className="form-control" placeholder={getValue(profile, 'userAccountData.userSettingData.addressCode.value')} readOnly /></div>
                  <div className="col-md-12"><label className="labels">Mức lương mong muốn</label><input type="text" className="form-control" placeholder={getValue(profile, 'userAccountData.userSettingData.salaryJobCode.value')} readOnly /></div>
                  <div className="col-md-12"><label className="labels">Kinh nghiệm</label><input type="text" className="form-control" placeholder={getValue(profile, 'userAccountData.userSettingData.experienceJobCode.value')} readOnly /></div>
                </div>
                <div className="mt-5">
                  <h4>Kỹ năng</h4>
                  <ul className="list-group">
                    {profile.listSkills && profile.listSkills.length > 0 ? (
                      profile.listSkills.map((skill, index) => (
                        <li key={index} className="list-group-item">{skill.skill ? skill.skill.name : "Chưa có dữ liệu"}</li>
                      ))
                    ) : (
                      <li className="list-group-item">Chưa có dữ liệu</li>
                    )}
                  </ul>
                </div>
                
              </div>
            </div>
          </div>
        </div>
        <div className="mt-5">
                  <h4 className=''>CV của Người Dùng</h4>
                  {profile.userAccountData.userSettingData && profile.userAccountData.userSettingData.file ? (
                    <div>
                      <a 
                        href={createPdfUrl(profile.userAccountData.userSettingData.file)}
                        download="CV.pdf" 
                        className="btn btn-primary mb-3"
                      >
                        Tải CV
                      </a>
                      <div className="embed-responsive embed-responsive-4by3">
                        <iframe 
                          className="embed-responsive-item"
                          src={createPdfUrl(profile.userAccountData.userSettingData.file)}
                          title="CV Preview"
                        />
                      </div>
                    </div>
                  ) : (
                    <p>Chưa có dữ liệu</p>
                  )}
                </div>
      </div>
      
    </div>
  );
}

export default ProfilePage;
