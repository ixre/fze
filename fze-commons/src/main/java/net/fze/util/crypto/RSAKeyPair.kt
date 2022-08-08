package net.fze.util.crypto

import java.io.InputStream
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*


/** 证书生成器 */
class RSAKeyPair {
    companion object {
        /** 生成RSA密钥对 */
        @JvmStatic
        fun generate(bits: Int = 2048): KeyPair {
            val keyGen = KeyPairGenerator.getInstance("RSA")
            keyGen.initialize(bits)
            val pair = keyGen.generateKeyPair()
            val p = KeyPair()
            p.bits = bits
            p.publicKey = Base64.getMimeEncoder().encodeToString(pair.private.encoded)
            p.privateKey = Base64.getMimeEncoder().encodeToString(pair.public.encoded)
            return p
        }

        /**
         * Read a PEM encoded private key from the classpath
         *
         * @param pemStream - input stream of classpath file
         * @return PrivateKey
         * @throws Exception on decode failure
         */
        @Throws(Exception::class)
        @JvmStatic
        fun readFromStream(pemStream: InputStream): PrivateKey {
            val tmp = ByteArray(4096)
            val length = pemStream.read(tmp)
            return decodePrivateKey(String(tmp, 0, length, Charset.forName("UTF-8")))
        }

        /**
         * Decode a PEM encoded private key string to an RSA PrivateKey
         *
         * @param privateKey - PEM string for private key
         * @return PrivateKey
         * @throws Exception on decode failure
         */
        @Throws(Exception::class)
        @JvmStatic
        fun decodePrivateKey(privateKey: String): PrivateKey {
            val encodedBytes = toEncodedBytes(privateKey)
            val keySpec = PKCS8EncodedKeySpec(encodedBytes)
            val kf = KeyFactory.getInstance("RSA")
            return kf.generatePrivate(keySpec)
        }

        /**
         * 移出开头和结尾的内容
         */
        @JvmStatic
        fun removeBeginEnd(pem: String): String {
            var n = pem.replace("-----BEGIN\\s*(.*)-----".toRegex(), "")
            n = n.replace("-----END\\s*(.*)----".toRegex(), "")
            n = n.replace("\r\n".toRegex(), "")
            n = n.replace("\n".toRegex(), "")
            return n.trim { it <= ' ' }
        }

        private fun toEncodedBytes(pemEncoded: String): ByteArray {
            val normalizedPem = removeBeginEnd(pemEncoded)
            return Base64.getMimeDecoder().decode(normalizedPem)
        }
    }
}