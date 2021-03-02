package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.security;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Player;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.PlayerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.hibernate.Hibernate;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.persistence.EntityNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private PlayerRepository playerRepository;

    public JWTAuthorizationFilter(ApplicationContext applicationContext) {
        this.playerRepository = applicationContext.getBean(PlayerRepository.class);
    }

    /*
            Este test simple sólo comprueba que exista el campo Authorization en el encabezado http
            y que contenga "OK"
     */
    protected void simpleDemoFilter(HttpServletRequest request) {
        String encabezado = request.getHeader("Authorization");
        if(encabezado != null && encabezado.equals("OK"))
            simpleSpringAuthentication();
        else
            SecurityContextHolder.clearContext();
    }

    /*******************************************************************************************
     Autenticación simple sin recuperar información del token para demostrar mecanismo
     */
    private void simpleSpringAuthentication() {
        List<String> authoritiesText = new ArrayList<>(Arrays.asList("ROLE_ADMIN", "ROLE_GOD"));

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("lalalala", null, sinLambdas(authoritiesText));
    }

    // Lista de permisos sin 'lambdas'
    private List<SimpleGrantedAuthority> sinLambdas(List<String> textList) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for( String text: textList ) {
            authorities.add(new SimpleGrantedAuthority(text));
        }
        return authorities;
    }

    // Lista de permisos usando 'lambdas'
    private List<SimpleGrantedAuthority> conLambdas(List<String> textList) {
        return textList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    /******************************************************************************************/

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // TODO: No keys hardcoded!!!
        if(httpServletRequest.getHeader("Authorization")!=null) {
            if (!httpServletRequest.getHeader("Authorization").startsWith("Bearer ")) {
                SecurityContextHolder.clearContext();
            } else {
                String jwtToken = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
                try {
                    Claims claims = Jwts.parser().setSigningKey("pestillo".getBytes()).parseClaimsJws(jwtToken).getBody();
                    String username = claims.getSubject();
                    Player player = playerRepository.findPlayerByUsername(username)
                            .orElseThrow(() -> new EntityNotFoundException());
                    if(!player.getToken().equals(jwtToken))
                        throw new Exception();
                    Hibernate.initialize(player.getRoles());
                    setUpSpringAuthentication(player);
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                }
            }
        } else {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void setUpSpringAuthentication(Player player) {

        Hibernate.initialize(player.getRoles());

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(player.getRoles());


        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(player, null,
                        roles);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
