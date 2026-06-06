package br.com.ticketcore.api.config;

import br.com.ticketcore.api.dao.PerfilDAO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtUtil jwtUtil;
    private final PerfilDAO perfilDAO;

    public JwtAuthFilter(JwtUtil jwtUtil, PerfilDAO perfilDAO) {
        this.jwtUtil = jwtUtil;
        this.perfilDAO = perfilDAO;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.tokenValido(token)) {
                Long idUsuario = jwtUtil.extrairIdUsuario(token);
                List<GrantedAuthority> authorities = carregarAuthorities(idUsuario);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(idUsuario, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    private List<GrantedAuthority> carregarAuthorities(Long idUsuario) {
        try {
            List<String> nomes = perfilDAO.buscarNomesPorUsuario(idUsuario);
            if (nomes == null || nomes.isEmpty()) {
                return Collections.emptyList();
            }
            return nomes.stream()
                    .map(nome -> new SimpleGrantedAuthority("ROLE_" + nome))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Não foi possível carregar os perfis do usuário {}: {}", idUsuario, e.getMessage());
            return Collections.emptyList();
        }
    }
}
