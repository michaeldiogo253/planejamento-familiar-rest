package alura.orcamentofamiliar.auth.application;

import alura.orcamentofamiliar.usuario.domain.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${planejamento.jwt.expiration}") private String expiration;

    @Value("${planejamento.jwt.secret}") private String secret;

    public String gerarToken(Authentication authentication) {

        Usuario logado = (Usuario) authentication.getPrincipal();

        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                   .setIssuer("API de Planejamento Familiar")
                   .setSubject(logado.getId()
                                     .toString())
                   .setIssuedAt(hoje)
                   .setExpiration(dataExpiracao)
                   .signWith(SignatureAlgorithm.HS256, secret)
                   .compact();
    }

    public boolean isTokenvalido(String token) {

        try {
            Jwts.parser()
                .setSigningKey(this.secret)
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Long getIdUsuario(String token) {

        Claims claims = Jwts.parser()
                            .setSigningKey(this.secret)
                            .parseClaimsJws(token)
                            .getBody();
        return Long.parseLong(claims.getSubject());
    }

}
