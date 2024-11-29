package com.doan.AppTuyenDung.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("PKL.TD@gmail.com");
        
        mailSender.send(message);
    }

    public void sendApplicationEmails(String userEmail, String companyEmail, String jobTitle, String userName) {
        // Email cho user
        String userSubject = "Xác nhận ứng tuyển thành công - " + jobTitle;
        String userBody = "Kính gửi " + userName + ",\n\n" +
                          "Cảm ơn bạn đã ứng tuyển vị trí " + jobTitle + " tại công ty. Hồ sơ của bạn đã được gửi đi thành công. " +
                          "Chúng tôi sẽ thông báo kết quả sớm nhất sau khi công ty xem xét.\n\n" +
                          "Trân trọng,\nĐội ngũ tuyển dụng";

        sendSimpleEmail(userEmail, userSubject, userBody);

        // Email cho công ty
        String companySubject = "Ứng viên mới cho vị trí " + jobTitle;
        String companyBody = "Kính gửi bộ phận nhân sự,\n\n" +
                             "Ứng viên " + userName + " đã nộp đơn ứng tuyển cho vị trí " + jobTitle + ". " +
                             "Vui lòng đăng nhập vào hệ thống để xem xét hồ sơ và phản hồi cho ứng viên.\n\n" +
                             "Trân trọng,\nĐội ngũ quản lý ứng tuyển";
        
        sendSimpleEmail(companyEmail, companySubject, companyBody);
    }

    public void sendResponseEmails(String userEmail, String companyEmail, String jobTitle, String userName, String statusApproved) {
        // Email cho user
        String userSubject = "Kết quả ứng tuyển vị trí " + jobTitle;
        String userBody = "Kính gửi " + userName + ",\n\n" +
                          "Công ty đã xem xét hồ sơ của bạn cho vị trí " + jobTitle + ". " +
                          (statusApproved.equals("yes") ? "Chúc mừng! Bạn đã vượt qua vòng xét duyệt hồ sơ và tiến tới vòng tiếp theo." : 
                          "Rất tiếc, bạn chưa phù hợp với yêu cầu của công ty cho vị trí này. Mong bạn sẽ có cơ hội trong tương lai.") + "\n\n" +
                          "Trân trọng,\nĐội ngũ tuyển dụng";

        sendSimpleEmail(userEmail, userSubject, userBody);

        // Email cho công ty
        String companySubject = "Xác nhận phản hồi ứng viên - " + userName;
        String companyBody = "Kính gửi bộ phận nhân sự,\n\n" +
                             "Phản hồi của quý công ty về ứng viên " + userName + " cho vị trí " + jobTitle + " đã được gửi thành công.\n\n" +
                             "Trân trọng,\nĐội ngũ quản lý ứng tuyển";
        
        sendSimpleEmail(companyEmail, companySubject, companyBody);
    }
    public void sendCompanyApprovalEmail(String userEmail, String companyName, boolean isApproved, String additionalNote) {
        String subject = isApproved ? "Công ty của bạn đã được kiểm duyệt thành công" : "Công ty của bạn đã bị từ chối";
        String body;
    
        if (isApproved) {
            body = "Kính gửi Quý công ty " + companyName + ",\n\n" +
                   "Chúc mừng! Công ty của bạn đã được kiểm duyệt thành công. " +
                   "Bạn có thể xem chi tiết tại trang của công ty.\n\n" +
                   "Trân trọng,\nBan quản lý hệ thống.";
        } else {
            body = "Kính gửi Quý công ty " + companyName + ",\n\n" +
                   "Rất tiếc, công ty của bạn đã bị từ chối kiểm duyệt vì: " + additionalNote + ".\n\n" +
                   "Trân trọng,\nBan quản lý hệ thống.";
        }
    
        sendSimpleEmail(userEmail, subject, body);
    }
}
