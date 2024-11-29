package com.doan.AppTuyenDung.Services;

import com.doan.AppTuyenDung.Repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailService implements UserDetailsService {
    @Autowired
    private AccountRepository accountReoisitory;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        return accountReoisitory.findByPhonenumber(phone);
    }
}
