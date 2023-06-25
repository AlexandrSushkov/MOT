package dev.nelson.mot.main.manager

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * This manager uses:
 *
 * * default Android [KeyGenerator] to crate a key.
 * * [KeyStore] to score the key,
 * * "AES/GCM/NoPadding" transformation to encrypt/decrypt data.
 */
class CryptoManager {

    private val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
        load(null)
    }

    /**
     * Encrypted data must be stored as encoded [Base64] string.
     *
     * Before decryption it must be decoded back to byteArray.
     *
     * @param text regular UTF-8 encoded string.
     * @return encrypted data as [Base64] encoded string.
     */
    fun encrypt(text: String): String {
        val bytesOfText = text.toByteArray()
        val encryptedText = encryptWithIv(bytesOfText)
        return base64Encode(encryptedText)
    }

    /**
     * @param base64EncryptedText [Base64] encoded string that contains encrypted data.
     * @return decrypted data as UTF-8 encoded string.
     */
    fun decrypt(base64EncryptedText: String): String {
        val decodedText = base64Decode(base64EncryptedText)
        val decryptedText = decryptWithIv(decodedText)
        return decryptedText.decodeToString()
    }


    /**
     * combinedData byteArray looks like this: [[ivSize],[iv],[encryptedData]]
     */
    private fun encryptWithIv(input: ByteArray): ByteArray {
        val cipher = getEncryptCipher()
        val ivSize = cipher.iv.size
        val iv = cipher.iv

        val encryptedData = cipher.doFinal(input)

        // create a new byte array of ivSize + size iv + encrypted bytes
        // the first byte in the array is reserved to store the ivSize
        val combinedData = ByteArray(1 + ivSize + encryptedData.size)

        // add ivSize
        combinedData[0] = ivSize.toByte()

        // add iv to output bytes after ivSize. it will be needed during decryption.
        System.arraycopy(iv, 0, combinedData, 1, iv.size)

        // add encrypted bytes to output bytes after iv.
        System.arraycopy(encryptedData, 0, combinedData, 1 + iv.size, encryptedData.size)
        return combinedData
    }

    /**
     * @param combinedData byteArray contains information about viSize, iv itself and encrypted data.
     * Looks like this: [[ivSize],[iv],[encryptedData]]
     *
     */
    fun decryptWithIv(combinedData: ByteArray): ByteArray {

        // retrieve ivSize from the first byte of the combined data
        val ivSize = combinedData[0].toInt()

        val iv = ByteArray(ivSize)

        // retrieve iv from the combined data
        System.arraycopy(combinedData, 1, iv, 0, ivSize)

        val cipher = getDecryptCipher(iv)

        val encryptedDataSize = combinedData.size - ivSize - 1
        val encryptedData = ByteArray(encryptedDataSize)

        // retrieve encrypted bytes from the combined data
        System.arraycopy(combinedData, ivSize + 1, encryptedData, 0, encryptedDataSize)

        return cipher.doFinal(encryptedData)
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(KEY_ALIAS, null) as? SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    /**
     * @param iv initialization vector. It is used as an additional input along with
     * the encryption key to ensure that the same plaintext message encrypted
     * with the same key results in different ciphertexts.
     */
    private fun getDecryptCipher(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), GCMParameterSpec(GCM_DEFAULT_TAG_LENGTH, iv))
        }
    }

    private fun getEncryptCipher() = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.ENCRYPT_MODE, getKey())
    }

    private fun base64Encode(input: ByteArray): String {
        return Base64.encodeToString(input, Base64.NO_PADDING or Base64.NO_WRAP)
    }

    private fun base64Decode(input: String?): ByteArray {
        return Base64.decode(input, Base64.NO_PADDING or Base64.NO_WRAP)
    }


    companion object {
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val KEY_ALIAS = "shared_preferences_key"
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val GCM_DEFAULT_TAG_LENGTH = 128
    }
}
