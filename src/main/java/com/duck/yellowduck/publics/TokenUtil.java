package com.duck.yellowduck.publics;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.PrintStream;
import java.security.Key;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class TokenUtil
{
    public static String sercetKey = "shenzhenqukuailianyouxiangongsi";
    public static final long keeptime = -1702967296L;

    public static String generToken(String id, String issuer, String subject)
    {
        long ttlMillis = -1702967296L;
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(sercetKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now);
        if (subject != null) {
            builder.setSubject(subject);
        }
        if (issuer != null) {
            builder.setIssuer(issuer);
        }
        builder.signWith(signatureAlgorithm, signingKey);
        if (ttlMillis >= 0L)
        {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public static String updateToken(String token)
    {
        try
        {
            Claims claims = verifyToken(token);
            String id = claims.getId();
            String subject = claims.getSubject();
            String issuer = claims.getIssuer();
            Date date = claims.getExpiration();
            return generToken(id, issuer, subject);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return "0";
    }

    public String updateTokenBase64Code(String token)
    {
        Base64.Encoder base64Encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        try
        {
            token = new String(decoder.decode(token), "utf-8");
            Claims claims = verifyToken(token);
            String id = claims.getId();
            String subject = claims.getSubject();
            String issuer = claims.getIssuer();
            Date date = claims.getExpiration();
            String newToken = generToken(id, issuer, subject);
            return new String(base64Encoder.encode(newToken.getBytes()), "utf-8");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return "0";
    }

    public static void main(String[] avgs)
    {
        System.out.println(generToken("che", null, null));
        System.out.println(verifyToken("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjaGUiLCJpYXQiOjE1MzI1MDI0ODl9.y5Taj0gcliptgtGJ3Pu4C2keybH-Nb57rsbAB6Xh_VMdada") + "");
    }

    public static Claims verifyToken(String token)
    {
        Claims claims = (Claims)Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(sercetKey)).parseClaimsJws(token).getBody();
        return claims;
    }
}
