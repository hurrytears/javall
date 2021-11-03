package com.apachee.mysql;

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

    BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String s)
            throws UsernameNotFoundException {
        SysUser user = userRepository.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        System.out.println("s:" + s);
        System.out.println("username:" + user.getUsername()
                + ";password:" + encoder.encode(user.getPassword()));
        return user;
    }
}
