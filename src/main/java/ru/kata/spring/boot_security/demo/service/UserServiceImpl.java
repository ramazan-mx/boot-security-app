package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepo;
import ru.kata.spring.boot_security.demo.repository.UserRepo;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.CustomUserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(RoleRepo roleRepo, UserRepo userRepo) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> person = userRepo.findByUsername(username);
        if (person.isPresent()) {
            return new CustomUserDetails(person.get());
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

    @Transactional
    @Override
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    @Transactional
    @Override
    public void add(User user, List<Integer> roles) {
        HashSet<Role> rolesOfUser = new HashSet<>();
        for (Integer roleId : roles) {
            rolesOfUser.add(roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role 'ROLE_USER' not found")));
        }
        user.setRoles(rolesOfUser);
        userRepo.save(user);
    }

    @Transactional
    @Override
    public void update(int id, User user, List<Integer> roles) {
        User u = getUserById(id);
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setEmail(user.getEmail());

        HashSet<Role> rolesOfUser = new HashSet<>();
        for (Integer roleId : roles) {
            rolesOfUser.add(roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role 'ROLE_USER' not found")));
        }

        u.setRoles(rolesOfUser);
    }

    @Transactional
    @Override
    public void delete(int id) {
        userRepo.deleteById(id);
    }

    @Transactional
    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Transactional
    @Override
    public User getUserById(int id) {
        return userRepo.findById(id).orElse(null);
    }
}
