package com.example.backend.infrastructure.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final String secret = "TXkgU3VwZXIgU2VjcmV0IEtleSBGb3IgSldUIEF1dGhlbnRpY2F0aW9uIDI1NiBCaXRz";
    private final long expiration = 86400000;

    public String generateToken(Authentication authentication) {
        try {
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(authentication.getName())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expiration))
                    .claim("scope", String.join(" ", roles))
                    .build();

            JWSSigner signer = new MACSigner(secret.getBytes());
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Error generando token JWT", e);
        }
    }
}
