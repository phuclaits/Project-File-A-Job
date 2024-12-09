package com.doan.AppTuyenDung.Services;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doan.AppTuyenDung.DTO.PaymentOrderRequestCvDTO;
import com.doan.AppTuyenDung.DTO.PaymentOrderRequestDTO;
import com.doan.AppTuyenDung.Repositories.CompanyRepository;
import com.doan.AppTuyenDung.Repositories.OrderPackageCvRepository;
import com.doan.AppTuyenDung.Repositories.OrderPackageRepository;
import com.doan.AppTuyenDung.Repositories.PackageCvRepository;
import com.doan.AppTuyenDung.Repositories.PackagePostRepository;
import com.doan.AppTuyenDung.Repositories.UserRepository;
import com.doan.AppTuyenDung.entity.Company;
import com.doan.AppTuyenDung.entity.OrderPackage;
import com.doan.AppTuyenDung.entity.OrderPackageCv;
import com.doan.AppTuyenDung.entity.PackageCv;
import com.doan.AppTuyenDung.entity.PackagePost;
import com.doan.AppTuyenDung.entity.User;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
@Service
public class PaymentService {
    @Autowired
    private PackageCvRepository packageCvRepository;
    @Autowired
    private APIContext apiContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderPackageCvRepository orderPackageCvRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private PackagePostRepository packagePostRepository;
    @Autowired
    private OrderPackageRepository orderPackageRepository;

    public Map<String, Object> getPaymentLink(Integer id, Integer amount) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Kiểm tra các tham số bắt buộc
            if (id == null || amount == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Lấy thông tin sản phẩm từ cơ sở dữ liệu
            PackagePost infoItem = packagePostRepository.findById(id).orElse(null);
            if (infoItem == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Item not found!");
                return response;
            }

            // Tạo danh sách item cho PayPal
            Item item = new Item();
            item.setName(infoItem.getName())
                .setCurrency("USD")
                .setPrice(String.valueOf(infoItem.getPrice()))
                .setQuantity(String.valueOf(amount));

            ItemList itemList = new ItemList();
            itemList.setItems(Collections.singletonList(item));

            // Tạo yêu cầu thanh toán với số tiền
            Amount amountfound = new Amount();
            amountfound.setCurrency("USD");
            amountfound.setTotal(String.valueOf(amount * infoItem.getPrice()));

            Transaction transaction = new Transaction();
            transaction.setAmount(amountfound);
            transaction.setDescription("This is the payment description.");
            transaction.setItemList(itemList);

            // Cấu hình thông tin thanh toán
            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setReturnUrl("https://project-file-a-job-git-master-la-hoang-phucs-projects.vercel.app" + "/admin/payment/success");
            redirectUrls.setCancelUrl("https://project-file-a-job-git-master-la-hoang-phucs-projects.vercel.app" + "/admin/payment/cancel");

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(Collections.singletonList(transaction));
            payment.setRedirectUrls(redirectUrls);

            // Tạo thanh toán
            Payment createdPayment = payment.create(apiContext);

            // Lấy liên kết thanh toán
            String approvalLink = createdPayment.getLinks().stream()
                    .filter(link -> link.getRel().equals("approval_url"))
                    .findFirst()
                    .orElseThrow(() -> new PayPalRESTException("Approval URL not found"))
                    .getHref();

            response.put("errCode", 0);
            response.put("link", approvalLink);

        } catch (PayPalRESTException e) {
            response.put("errCode", -1);
            response.put("errMessage", e.getMessage());
        }

        return response;
    }
     public Map<String, Object> paymentOrderSuccess(PaymentOrderRequestDTO data) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Kiểm tra các tham số bắt buộc
            if (data.getPayerID() == null || data.getPaymentId() == null || data.getToken() == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameter!");
                return response;
            }

            // Lấy thông tin sản phẩm từ cơ sở dữ liệu
            PackagePost infoItem = packagePostRepository.findById(data.getPackageId()).orElse(null);
            if (infoItem == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Package not found!");
                return response;
            }

            // Cấu hình thanh toán
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(data.getPayerID());

            Payment payment = new Payment();
            payment.setId(data.getPaymentId());

            // Thiết lập số tiền giao dịch
            Transaction transaction = new Transaction();
            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(String.valueOf(data.getAmount() * infoItem.getPrice()));
            transaction.setAmount(amount);
            payment.setTransactions(Collections.singletonList(transaction));

            // Thực hiện thanh toán
            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            // Nếu thanh toán thành công, lưu vào cơ sở dữ liệu
            if ("approved".equals(executedPayment.getState())) {
                OrderPackage orderPackageSave = new OrderPackage();
                // orderPackage.setPackagePostId(data.getPackageId());
                PackagePost foundPackagePost = packagePostRepository.findById(data.getPackageId()).orElse(null);
                if (foundPackagePost == null) {
                    response.put("errCode", -1);
                    response.put("errMessage", "Không tìm thấy packagePost");
                    return response;
                }
                orderPackageSave.setPackagePost(foundPackagePost);
                User foundUser = userRepository.findById(data.getUserId()).orElse(null);
                if (foundUser == null) {
                    response.put("errCode", -1);
                    response.put("errMessage", "Không tìm thấy user");
                    return response;
                    
                }
                orderPackageSave.setUser(foundUser);
                orderPackageSave.setCurrentPrice(infoItem.getPrice());
                orderPackageSave.setAmount(data.getAmount());
                orderPackageSave.setCreatedAt(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                orderPackageSave.setUpdatedAt(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                orderPackageRepository.save(orderPackageSave);

                // Cập nhật số lượng post hoặc hot post của công ty
                User user = userRepository.findById(data.getUserId()).orElse(null);
                if (user != null) {
                    Company company = companyRepository.findById(user.getCompanyId()).orElse(null);
                    if (company != null) {
                        if (!infoItem.getIsHot()) {
                            company.setAllowPost(company.getAllowPost() + Integer.parseInt(infoItem.getValue()) * data.getAmount());
                        } else {
                            company.setAllowHotPost(company.getAllowHotPost() + Integer.parseInt(infoItem.getValue()) * data.getAmount());
                        }
                        companyRepository.save(company);
                    }
                }

                response.put("errCode", 0);
                response.put("errMessage", "Hệ thống đã ghi nhận lịch sử mua của bạn");
            } else {
                response.put("errCode", -1);
                response.put("errMessage", "Thanh toán không thành công");
            }

        } catch (PayPalRESTException e) {
            response.put("errCode", -1);
            response.put("errMessage", e.getMessage());
        }

        return response;
    }


    public Map<String, Object> getPaymentLinkCV(Integer id, Integer amount) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Kiểm tra các tham số bắt buộc
            if (id== null || amount == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Lấy thông tin gói CV từ cơ sở dữ liệu
            PackageCv infoItem = packageCvRepository.findById(id).orElse(null);
            if (infoItem == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Item not found!");
                return response;
            }

            // Tạo danh sách item cho PayPal
            Item item = new Item();
            item.setName(infoItem.getName())
                .setCurrency("USD")
                .setPrice(String.valueOf(infoItem.getPrice()))
                .setQuantity(String.valueOf(amount));

            ItemList itemList = new ItemList();
            itemList.setItems(Collections.singletonList(item));

            // Tạo yêu cầu thanh toán với số tiền
            Amount amountfound = new Amount();
            amountfound.setCurrency("USD");
            amountfound.setTotal(String.valueOf(amount * infoItem.getPrice()));

            Transaction transaction = new Transaction();
            transaction.setAmount(amountfound);
            transaction.setDescription("This is the payment description.");
            transaction.setItemList(itemList);

            // Cấu hình thông tin thanh toán
            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setReturnUrl("https://project-file-a-job-git-master-la-hoang-phucs-projects.vercel.app" + "/admin/paymentCv/success");
            redirectUrls.setCancelUrl("https://project-file-a-job-git-master-la-hoang-phucs-projects.vercel.app" + "/admin/paymentCv/cancel");

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(Collections.singletonList(transaction));
            payment.setRedirectUrls(redirectUrls);

            // Tạo thanh toán
            Payment createdPayment = payment.create(apiContext);

            // Lấy liên kết thanh toán
            String approvalLink = createdPayment.getLinks().stream()
                    .filter(link -> link.getRel().equals("approval_url"))
                    .findFirst()
                    .orElseThrow(() -> new PayPalRESTException("Approval URL not found"))
                    .getHref();

            response.put("errCode", 0);
            response.put("link", approvalLink);

        } catch (PayPalRESTException e) {
            response.put("errCode", -1);
            response.put("errMessage", e.getMessage());
        }

        return response;
    }
    

    public Map<String, Object> paymentOrderSuccessCV(PaymentOrderRequestCvDTO data) {
        Map<String, Object> response = new HashMap<>();

        try {

            if (data.getPayerID() == null || data.getPaymentId() == null || 
                data.getToken() == null || data.getPackageCvId() == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameter!");
                return response;
            }


            PackageCv infoItem = packageCvRepository.findById(data.getPackageCvId()).orElse(null);
            if (infoItem == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Package not found!");
                return response;
            }


            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(data.getPayerID());

            Payment payment = new Payment();
            payment.setId(data.getPaymentId());

            Transaction transaction = new Transaction();
            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(String.valueOf(data.getAmount() * infoItem.getPrice()));
            transaction.setAmount(amount);
            payment.setTransactions(Collections.singletonList(transaction));


            Payment executedPayment = payment.execute(apiContext, paymentExecution);


            if ("approved".equals(executedPayment.getState())) {
                OrderPackageCv orderPackageCV = new OrderPackageCv();
                orderPackageCV.setPackageCv(infoItem);
                User foundUser = userRepository.findById(data.getUserId()).orElse(null);
                if (foundUser == null) {
                    response.put("errCode", -1);
                    response.put("errMessage", "Không tìm thấy user");
                    return response;
                    
                }
                orderPackageCV.setUser(foundUser);
                orderPackageCV.setCurrentPrice(infoItem.getPrice().intValue());
                orderPackageCV.setAmount(data.getAmount());
                orderPackageCV.setCreatedAt(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                orderPackageCV.setUpdatedAt(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                orderPackageCvRepository.save(orderPackageCV);

                User user = userRepository.findById(data.getUserId()).orElse(null);
                if (user != null) {
                    Company company = companyRepository.findById(user.getCompanyId()).orElse(null);
                    if (company != null) {
                        company.setAllowCV(company.getAllowCV() + Integer.parseInt(infoItem.getValue()) * data.getAmount());
                        companyRepository.save(company);
                    }
                }

                response.put("errCode", 0);
                response.put("errMessage", "Hệ thống đã ghi nhận lịch sử mua của bạn");
            } else {
                response.put("errCode", -1);
                response.put("errMessage", "Thanh toán không thành công");
            }

        } catch (PayPalRESTException e) {
            response.put("errCode", -1);
            response.put("errMessage", e.getMessage());
        }

        return response;
    }
}
