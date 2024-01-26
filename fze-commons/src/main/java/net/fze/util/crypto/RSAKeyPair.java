package net.fze.util.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * 证书生成器
 */

public class RSAKeyPair {
    /**
     * 生成RSA密钥对
     */
    public static KeyPair generate(int bits) throws NoSuchAlgorithmException {
        if (bits <= 0) {
            bits = 2048;
        }
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(bits);
        java.security.KeyPair pair = keyGen.generateKeyPair();
        KeyPair p = new KeyPair();
        p.setBits(bits);
        p.setPublicKey(Base64.getMimeEncoder().encodeToString(pair.getPrivate().getEncoded()));
        p.setPrivateKey(Base64.getMimeEncoder().encodeToString(pair.getPublic().getEncoded()));
        return p;
    }

    /**
     * Read a PEM encoded private key from the classpath
     *
     * @param pemStream - input stream of classpath file
     * @return PrivateKey
     */
    public static PrivateKey readFromStream(InputStream pemStream) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] tmp = new byte[4096];
        int length = pemStream.read(tmp);
        return decodePrivateKey(new String(tmp, 0, length, Charset.forName("UTF-8")));
    }

    /**
     * Decode a PEM encoded private key string to an RSA PrivateKey
     *
     * @param privateKey - PEM string for private key
     * @return PrivateKey
     */
    public static PrivateKey decodePrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedBytes = toEncodedBytes(privateKey);
        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    /**
     * 移出开头和结尾的内容
     */
    public static String removeBeginEnd(String pem) {
        String n = pem.replaceAll("-----BEGIN\\s*(.*)-----", "");
        n = n.replaceAll("-----END\\s*(.*)----", "");
        n = n.replaceAll("\r\n", "");
        n = n.replaceAll("\n", "");
        return n.trim();
    }

    private static byte[] toEncodedBytes(String pemEncoded) {
        String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getMimeDecoder().decode(normalizedPem);
    }
}
