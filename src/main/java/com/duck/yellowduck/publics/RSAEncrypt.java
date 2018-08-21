package com.duck.yellowduck.publics;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

public class RSAEncrypt
{
    public static HashMap<String, String> getKeys()
    {
        HashMap<String, String> map = new HashMap();
        KeyPairGenerator keyPairGen = null;
        try
        {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        keyPairGen.initialize(512, new SecureRandom());

        KeyPair keyPair = keyPairGen.generateKeyPair();

        String publicKey = base64ToStr(keyPair.getPublic().getEncoded());

        String privateKey = base64ToStr(keyPair.getPrivate().getEncoded());
        map.put("publicKey", publicKey);
        map.put("privateKey", privateKey);
        return map;
    }

    public static RSAPublicKey loadPublicKey(String publicKeyStr)
            throws Exception
    {
        try
        {
            byte[] buffer = DatatypeConverter.parseBase64Binary(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey)keyFactory.generatePublic(keySpec);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Exception("无此算法");
        }
        catch (InvalidKeySpecException e)
        {
            throw new Exception("公钥非法");
        }
        catch (NullPointerException e)
        {
            throw new Exception("公钥数据为空");
        }
    }

    public static RSAPrivateKey loadPrivateKey(String privateKeyStr)
            throws Exception
    {
        try
        {
            byte[] buffer = DatatypeConverter.parseBase64Binary(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Exception("无此算法");
        }
        catch (InvalidKeySpecException e)
        {
            throw new Exception("私钥非法");
        }
        catch (NullPointerException e)
        {
            throw new Exception("私钥数据为空");
        }
    }

    public static String encrypt(RSAPublicKey publicKey, byte[] plainTextData)
            throws Exception
    {
        if (publicKey == null) {
            throw new Exception("加密公钥为空,请设置");
        }
        Cipher cipher = null;
        try
        {
            cipher = Cipher.getInstance("RSA");
            cipher.init(1, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return base64ToStr(output);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Exception("无此加密算法");
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (InvalidKeyException e)
        {
            throw new Exception("加密公钥非法,请检查");
        }
        catch (IllegalBlockSizeException e)
        {
            throw new Exception("明文长度非法");
        }
        catch (BadPaddingException e)
        {
            throw new Exception("明文数据已损坏");
        }
    }

    public static String decrypt(RSAPrivateKey privateKey, byte[] cipherData)
            throws Exception
    {
        if (privateKey == null) {
            throw new Exception("解密私钥为空,请设置");
        }
        Cipher cipher = null;
        try
        {
            cipher = Cipher.getInstance("RSA");
            cipher.init(2, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return new String(output);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Exception("无此解密算法");
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (InvalidKeyException e)
        {
            throw new Exception("解密私钥非法,请检查");
        }
        catch (IllegalBlockSizeException e)
        {
            throw new Exception("密码长度非法");
        }
        catch (BadPaddingException e)
        {
            throw new Exception("密码数据已损坏");
        }
    }

    public static String base64ToStr(byte[] b)
    {
        return DatatypeConverter.printBase64Binary(b);
    }

    public static byte[] strToBase64(String str)
    {
        return DatatypeConverter.parseBase64Binary(str);
    }

    public static void main(String[] args)
    {
        String pubkey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMhLsyvfrTvw9W7dgWXpUdSlWLqhTrTNYBBMeuNGFji1dcOGiUHSh2Gp1F9WIAh6+br07CF/MSqa3hPrCianRtcCAwEAAQ==";
        String prikey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAyEuzK9+tO/D1bt2BZelR1KVYuqFOtM1gEEx640YWOLV1w4aJQdKHYanUX1YgCHr5uvTsIX8xKpreE+sKJqdG1wIDAQABAkEAkql7hMDAJIgH7yoaufjihoacNewvqYxIryaRRSHo25QWlUVjrzwF+uioyIcW2qtoWAyVhLcR/0U+EruHXPVdQQIhAPjD/L72lxpPX0JXG46ML/HhMapaAMVYrlblWWTCN6r5AiEAzh7dEecysSs43GmTkz9LjE16NNHcfSfmx+GBpvgCpE8CIEyr4y9cQzAZvItu25BwxqovNSaC/O/WKE2h/I5gidXJAiEAqqmlU2iBcW4fI2iLFzUmIWIZGFjW3g3GqKMPzaa10UMCIQCYQmU6LHbBhABrmtsEdt5qdwwzvYlmfHnFjhnG0xyc+A==";

        System.out.println("pubkey:" + pubkey);
        System.out.println("prikey:" + prikey);
        try
        {
            RSAPublicKey publicKey = loadPublicKey(pubkey);
            RSAPrivateKey privateKey = loadPrivateKey(prikey);
            String str = "hello world";
            String s = encrypt(publicKey, str.getBytes());
            System.out.println("����" + s);
            String s1 = decrypt(privateKey, strToBase64(s));
            System.out.println(s1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
