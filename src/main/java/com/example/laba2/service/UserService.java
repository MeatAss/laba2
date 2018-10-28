package com.example.laba2.service;

import com.example.laba2.domain.User;
import com.example.laba2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean lockUsers(List<Long> usersId) {
        boolean lockAuthentication = false;
        String nameAuthentication = SecurityContextHolder.getContext().getAuthentication().getName();

        for (Long id: usersId) {
            User user = userRepository.getOne(id);
            user.lock();
            userRepository.save(user);

            if (nameAuthentication.equals(user.getUsername()))
                lockAuthentication = true;
        }

        return lockAuthentication;
    }

    public void unlockUsers(List<Long> usersId) {
        for (Long id: usersId) {
            userRepository.getOne(id).unlock();
            userRepository.save(userRepository.getOne(id));
        }
    }

    public boolean deleteUsers(List<Long> usersId) {
        boolean lockAuthentication = false;
        String nameAuthentication = SecurityContextHolder.getContext().getAuthentication().getName();

        for (Long id: usersId) {
            User user = userRepository.getOne(id);
            userRepository.delete(user);

            if (nameAuthentication.equals(user.getUsername()))
                lockAuthentication = true;
        }

        return lockAuthentication;
    }

}
