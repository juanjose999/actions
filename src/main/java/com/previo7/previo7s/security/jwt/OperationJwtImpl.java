package com.previo7.previo7s.security.jwt;

import com.previo7.previo7s.dto.TokenDto;
import com.previo7.previo7s.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

import static com.previo7.previo7s.utils.Constants.CLAIMS_ROLES_KEY;
import static com.previo7.previo7s.utils.Constants.TOKEN_DURATION_MINUTES;

@Component
public class OperationJwtImpl implements OperationJwt{

    @Value("${jwt.secret}")
    String secret;

    private String generateToken(User user, Date expirationDate){
        return Jwts.builder()
                .setSubject(user.getId()).claim(CLAIMS_ROLES_KEY, user.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public TokenDto generateTokenDto(User user){
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.MINUTE, 30);
        String token = generateToken(user, expirationDate.getTime());
        return new TokenDto(token, expirationDate.getTime());
    }
}
