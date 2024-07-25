package com.spring.authentication.config;


import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.spring.authentication.entity.User;



@Service
public class TokenProvider {
	
	 @Value("${security.jwt.secret-key}")	
	  String secretKey ;
	 
	 public String generateAccessToken(User user) {
		    try {
		      Algorithm algorithm = Algorithm.HMAC256(secretKey);
		      return JWT.create()
		          .withSubject(user.getUsername())
		          .withClaim("username", user.getUsername())
		          .withExpiresAt(genAccessExpirationDate())
		          .sign(algorithm);
		    } catch (JWTCreationException exception) {
		      throw new JWTCreationException("Error while generating token", exception);
		    }
		  }
	 
	 
	 public String validateToken(String token) {
		    try {
		        Algorithm algorithm = Algorithm.HMAC256(secretKey);
		        return JWT.require(algorithm)
		                  .build()
		                  .verify(token)
		                  .getSubject();
		    } catch (TokenExpiredException e) {
		        System.err.println("Token has expired: " + e.getMessage());
		        throw new JWTVerificationException("Token has expired", e);
		    } catch (JWTVerificationException e) {
		        System.err.println("Token validation error: " + e.getMessage());
		        throw new JWTVerificationException("Error while validating token", e);
		    }
		}
	 
	 
	 public String generateRefreshAccessToken(HashMap<String,Object> claims,User user) {
		    try {
		      Algorithm algorithm = Algorithm.HMAC256(secretKey);
		      return JWT.create()
		          .withSubject(user.getUsername())
		          .withClaim("username", user.getUsername())
		          .withExpiresAt(genAccessExpirationDate())
		          .sign(algorithm);
		    } catch (JWTCreationException exception) {
		      throw new JWTCreationException("Error while generating refreshtoken", exception);
		    }
		  }
	 
	 
	 
	// Extract all claims from the token
	    private DecodedJWT extractAllClaims(String token) throws JWTVerificationException {
	        Algorithm algorithm = Algorithm.HMAC256(secretKey);
	        return JWT.require(algorithm).build().verify(token);
	    }

	    // Extract a specific claim using the claimsResolver function
	    private <T> T extractClaim(String token, ClaimResolver<T> claimResolver) throws JWTVerificationException {
	        DecodedJWT decodedJWT = extractAllClaims(token);
	        Map<String, Claim> claimsMap = decodedJWT.getClaims();
	        return claimResolver.apply(claimsMap);
	    }

	    // Extract username claim from the token
	    public String extractUserName(String token) throws JWTVerificationException {
	        return extractClaim(token, claims -> claims.get("sub").asString());
	    }

	    // Check if the token is valid for the given user
	    public boolean isTokenValid(String token, UserDetails user) {
	        try {
	            final String username = extractUserName(token);
	            return username.equals(user.getUsername()) && !isTokenExpired(token);
	        } catch (JWTVerificationException e) {
	            return false;
	        }
	    }

	    // Check if the token has expired
	    private boolean isTokenExpired(String token) throws JWTVerificationException {
	        Object expirationDate = extractClaim(token, claims -> claims.get("exp").asDate());
	        return expirationDate != null && ((java.util.Date) expirationDate).before(new Date(0));
	    }

	    // Functional interface for resolving claims
	    @FunctionalInterface
	    interface ClaimResolver<T> {
	        T apply(Map<String, Claim> claims);
	    }

	private Instant genAccessExpirationDate() {
		    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
		  }


}
