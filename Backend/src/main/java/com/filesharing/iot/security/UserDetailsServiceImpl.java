package com.filesharing.iot.security;

import com.filesharing.iot.models.UserModel;
import com.filesharing.iot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import org.springframework.security.core.userdetails.User;
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
        UserModel applicationUserModel = userRepository.findByEmail(email);
        if (applicationUserModel == null || applicationUserModel.getEmail() == null) {
            throw new UsernameNotFoundException("Username with email: " + email + " not found");
        }
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_" + "USER");

        return new User(applicationUserModel.getEmail(), applicationUserModel.getPassword(), grantedAuthorities);
    }
}
