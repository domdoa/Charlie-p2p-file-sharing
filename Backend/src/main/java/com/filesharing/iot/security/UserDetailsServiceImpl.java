package com.filesharing.iot.security;

import com.filesharing.iot.models.User;
import com.filesharing.iot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User applicationUser = userRepository.findByEmail(email);
        if (applicationUser == null || applicationUser.getEmail() == null) {
            throw new UsernameNotFoundException("Username with email: " + email + " not found");
        }
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_" + "USER");

        return new org.springframework.security.core.userdetails.User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);
    }
}
