package com.spring.authentication.config;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
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
	 
	 
	 
	 public String refreshAccessToken(String refreshToken) {
	        String username = validateRefreshToken(refreshToken);
	        return generateAccessToken(new User(username, null, null));
	    }
	 
	
	 
	 public String generateRefreshToken(User user) {
	        try {
	            Algorithm algorithm = Algorithm.HMAC256(secretKey);
	            return JWT.create()
	                .withSubject(user.getUsername())
	                .withClaim("username", user.getUsername())
	                .withExpiresAt(genRefreshExpirationDate())
	                .sign(algorithm);
	        } catch (JWTCreationException exception) {
	            throw new JWTCreationException("Error while generating refresh token", exception);
	        }
	    }
	 
	 
	 
	 public String validateRefreshToken(String token) {
	        try {
	            Algorithm algorithm = Algorithm.HMAC256(secretKey);
	            return JWT.require(algorithm).build().verify(token).getSubject();
	        } catch (JWTVerificationException exception) {
	            throw new JWTVerificationException("Error while validating refresh token", exception);
	        }
	    }
	 
	 


	private Instant genAccessExpirationDate() {
		    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC);
		  }
	
	private Instant genRefreshExpirationDate() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.UTC);
    }
	
}





//public String generateRefreshAccessToken(HashMap<String,Object> claims,User user) {
//try {
//  Algorithm algorithm = Algorithm.HMAC256(secretKey);
//  return JWT.create()
//      .withSubject(user.getUsername())
//      .withClaim("username", user.getUsername())
//      .withExpiresAt(genRefreshExpirationDate())
//      .sign(algorithm);
//} catch (JWTCreationException exception) {
//  throw new JWTCreationException("Error while generating refreshtoken", exception);
//}
//}




//private DecodedJWT extractAllClaims(String token) throws JWTVerificationException {
//Algorithm algorithm = Algorithm.HMAC256(secretKey);
//return JWT.require(algorithm).build().verify(token);
//}
//
//
//private <T> T extractClaim(String token, ClaimResolver<T> claimResolver) throws JWTVerificationException {
//DecodedJWT decodedJWT = extractAllClaims(token);
//Map<String, Claim> claimsMap = decodedJWT.getClaims();
//return claimResolver.apply(claimsMap);
//}
//
//
//public String extractUserName(String token) throws JWTVerificationException {
//return extractClaim(token, claims -> claims.get("sub").asString());
//}
//
//
//public boolean isTokenValid(String token, UserDetails user) {
//try {
//    final String username = extractUserName(token);
//    return username.equals(user.getUsername()) && !isTokenExpired(token);
//} catch (JWTVerificationException e) {
//    return false;
//}
//}
//
//
//private boolean isTokenExpired(String token) throws JWTVerificationException {
//Object expirationDate = extractClaim(token, claims -> claims.get("exp").asDate());
//return expirationDate != null && ((java.util.Date) expirationDate).before(new Date(0));
//}
//
//
//@FunctionalInterface
//interface ClaimResolver<T> {
//T apply(Map<String, Claim> claims);
//}
