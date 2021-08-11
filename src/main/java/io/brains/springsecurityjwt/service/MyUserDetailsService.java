package io.brains.springsecurityjwt.service;

import io.brains.springsecurityjwt.entity.UserEntity;
import io.brains.springsecurityjwt.models.AuthenticationRequest;
import io.brains.springsecurityjwt.models.AuthenticationResponse;
import io.brains.springsecurityjwt.models.CustomUserDetails;
import io.brains.springsecurityjwt.models.UserModel;
import io.brains.springsecurityjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.brains.springsecurityjwt.repository.UserRepository;

import java.util.List;


@Service
public class MyUserDetailsService implements UserDetailsService   {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        List<UserEntity> userEntityList = userRepository.findByUsername(userName);
        if (userEntityList.size() == 0) {
            throw new UsernameNotFoundException("User details not found for the user : " + userName);
        }
        return new CustomUserDetails(userEntityList.get(0));
    }

    public UserEntity createNewUser(UserModel usuario) {
        usuario.setPassword(bcryptEncoder.encode(usuario.getPassword()));
        return userRepository.save(new UserEntity(usuario));
    }

    public AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticatioRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticatioRequest.getUserName(), authenticatioRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = loadUserByUsername(authenticatioRequest.getUserName());
        final String jwt = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(jwt);
    }
}
