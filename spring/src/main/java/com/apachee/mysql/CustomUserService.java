package com.apachee.mysql;

import com.apachee.spring.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.
        UserDetailsService;
import org.springframework.security.core.userdetails
        .UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomUserService implements UserDetailsService {
    @Autowired
    SysUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s)
            throws UsernameNotFoundException {
        System.out.println("用户名：" + s);
        SysUser user = userRepository.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return user;
    }
}
