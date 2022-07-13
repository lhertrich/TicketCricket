package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.*;
import de.hohenheim.ticketcricket.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByID(int id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Sucht nach einem User mit einem bestimmten Usernamen.
     *
     * @param username der username.
     * @return User-Objekt.
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Spring Security Authentication Methoden
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Gibt den aktuell eingeloggten User zurück.
     *
     * @return User.
     */
    public User getCurrentUser() {
        return getUserByUsername(((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername());
    }


    public List<User> findAllUserByUsername(String searchString) {
        List<User> roleUserList = new ArrayList<>();
        List<User> userList = findAllUsers();
        for (User user : userList) {
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role.getRolename().equals("ROLE_USER")) {
                    roleUserList.add(user);
                }
            }
        }
        List<User> allUsersSearch;
        if (searchString != null) {
            allUsersSearch = roleUserList.stream().filter(x -> x.getUsername().toLowerCase(Locale.ROOT).replaceAll("\s", "").contains(searchString)).collect(Collectors.toList());
        } else {
            allUsersSearch = roleUserList;
        }
        return allUsersSearch;
    }


    /**
     * Gibt das UserDetails Objekt des aktuell eingeloggten Users zurück. Wird u.U. benötigt um
     * Rollen-Authentifizierungschecks durchzuführen.
     *
     * @return UserDetails Objekt der Spring Security.
     */
    public org.springframework.security.core.userdetails.User getCurrentUserDetails() {
        return (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    /**
     * Überschreibt die Methode, welche für den Login der Spring Security benötigt wird..
     *
     * @param username the username des Benutzers.
     * @return UserDetails Objekt des Spring Security Frameworks.
     * @throws UsernameNotFoundException exception.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Could not find the user for username " + username);
        }
        List<GrantedAuthority> grantedAuthorities = getUserAuthorities(user.getRoles());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true, user.isEnabled(), grantedAuthorities);
    }

    private List<GrantedAuthority> getUserAuthorities(Set<Role> roleSet) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role : roleSet) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRolename()));
        }
        return grantedAuthorities;
    }

    public Set<User> getAdmins() {
        HashSet<User> admins = new HashSet<>();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            for (Role role : user.getRoles()) {
                if (role.getRolename() == "ROLE_ADMIN") {
                    admins.add(user);
                }
            }
        }
        return admins.stream().sorted(Comparator.comparing(User::getUsername)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<User> getAdminsByCategory(Category category) {
        List<User> allAdmins = getAdmins().stream().toList();
        List<User> adminsByRole = new ArrayList<>();
        for (User admin : allAdmins) {
            Set<String> roleNames = admin.getRoles().stream().map(Role::getRolename).collect(Collectors.toSet());
            System.out.println("called: "+category.toString());
            if (roleNames.contains("ROLE_"+category)) {
                adminsByRole.add(admin);
            }
        }
        if (adminsByRole.isEmpty()) {
            return allAdmins;
        } else {
            return adminsByRole;
        }
    }
}