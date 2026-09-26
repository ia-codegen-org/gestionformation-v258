package com.gestionformation.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.gestionformation.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        try {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "Utilisateur non trouvé : " + username));
        } catch (DataAccessException e) {
            // ✅ Convertir toute exception SQL (ex: table absente
            //    après reset de la base H2 en mémoire) en échec
            //    d'authentification propre (401), pas un crash 500
            throw new UsernameNotFoundException(
                    "Authentification impossible : " + username, e);
        }
    }
}
