package io.github.xiewuzhiying.vs_addition.compats.computercraft.apis;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class CryptoAPI implements ILuaAPI {

    private final String ALGO = "AES_256/GCM/NoPadding";
    public final int AES_KEY_SIZE = 256;
    public final int GCM_IV_LENGTH = 12;
    public final int TLEN = 128;

    @Override
    public String[] getNames() {
        return new String[] {"cpt", "crypto"};
    }

    private Key generateKey(String key_string) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        sr.setSeed(key_string.getBytes(StandardCharsets.UTF_8));
        kg.init(AES_KEY_SIZE, sr);
        return kg.generateKey();
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();
        if (src == null || src.length == 0) {
            return null;
        }
        for (int j = 0; j < src.length; j++) {
            int v = src[j] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append(0);
            }
            sb.append(hv);
        }
        return sb.toString().toUpperCase();
    }

    private byte[] hexStringToBytes(String src) {
        if (src == null || src.equals("")) {
            return null;
        }
        src = src.toUpperCase();
        int len = src.length() / 2;
        char[] hexChars = src.toCharArray();
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            b[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return b;
    }

    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    @LuaFunction
    public final Object SHA512HASH(IArguments raw_data) {
        MessageDigest sha512 = null;
        try {
            sha512 = MessageDigest.getInstance("SHA3-512");
        } catch (NoSuchAlgorithmException ignored) {

        }
        try {
            sha512.update(raw_data.getString(0).getBytes(StandardCharsets.UTF_8));
        } catch (LuaException ignored) {

        }
        byte[] hash = sha512.digest();
        return bytesToHexString(hash);
    }

    @LuaFunction
    public final Object SHA256HASH(IArguments raw_data) throws LuaException {
        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException ignored) {

        }
        sha256.update(raw_data.getString(0).getBytes(StandardCharsets.UTF_8));
        byte[] hash = sha256.digest();
        return bytesToHexString(hash);
    }

    @LuaFunction
    public final Object AESGCMEncrypt(IArguments args) throws LuaException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGO);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ignored) {

        }
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(iv);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(args.getString(1)), new GCMParameterSpec(TLEN, iv));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException ignored) {

        }
        byte[] textBytes = args.getString(0).getBytes(StandardCharsets.UTF_8);
        byte[] encryptBytes = new byte[0];
        try {
            encryptBytes = cipher.doFinal(textBytes);
        } catch (IllegalBlockSizeException | BadPaddingException ignored) {

        }
        byte[] msg = new byte[GCM_IV_LENGTH + encryptBytes.length];
        System.arraycopy(iv, 0, msg, 0, GCM_IV_LENGTH);
        System.arraycopy(encryptBytes, 0, msg, GCM_IV_LENGTH, encryptBytes.length);
        return bytesToHexString(msg);
    }

    @LuaFunction
    public final Object AESGCMDecrypt(IArguments args) throws LuaException {
        byte[] bytes = hexStringToBytes(args.getString(0));
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] content = new byte[bytes.length - GCM_IV_LENGTH];
        System.arraycopy(bytes, 0, iv, 0, GCM_IV_LENGTH);
        System.arraycopy(bytes, GCM_IV_LENGTH, content, 0, content.length);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGO);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ignored) {

        }
        GCMParameterSpec params = new GCMParameterSpec(TLEN, iv);
        try {
            cipher.init(Cipher.DECRYPT_MODE, generateKey(args.getString(1)), params);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException ignored) {

        }
        byte[] finals = new byte[0];
        try {
            finals = cipher.doFinal(content);
        } catch (IllegalBlockSizeException | BadPaddingException ignored) {

        }
        return new String(finals, StandardCharsets.UTF_8);
    }

    @LuaFunction
    public final Map<String, String> GenECDHKeypair() {
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("EC");
        } catch (NoSuchAlgorithmException ignored) {

        }
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        try {
            kpg.initialize(ecSpec);
        } catch (InvalidAlgorithmParameterException ignored) {

        }
        KeyPair kp = kpg.generateKeyPair();
        String pri = bytesToHexString(kp.getPrivate().getEncoded());
        String pub = bytesToHexString(kp.getPublic().getEncoded());
        Map<String, String> map = new HashMap<>();
        map.put("pri", pri);
        map.put("pub", pub);
        return map;
    }

    @LuaFunction
    public final Object GetECDHFinalKey(IArguments args) throws LuaException {
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(hexStringToBytes(args.getString(0)));
        PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(hexStringToBytes(args.getString(1)));
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("EC");
        } catch (NoSuchAlgorithmException ignored) {

        }
        PublicKey pk = null;
        try {
            pk = kf.generatePublic(x509);
        } catch (InvalidKeySpecException ignored) {

        }
        PrivateKey priK = null;
        try {
            priK = kf.generatePrivate(pkcs);
        } catch (InvalidKeySpecException ignored) {

        }
        KeyAgreement kam = null;
        try {
            kam = KeyAgreement.getInstance("ECDH");
        } catch (NoSuchAlgorithmException ignored) {

        }
        try {
            kam.init(priK);
        } catch (InvalidKeyException ignored) {

        }
        try {
            kam.doPhase(pk, true);
        } catch (InvalidKeyException ignored) {

        }
        byte[] sSecret = kam.generateSecret();
        return bytesToHexString(sSecret);
    }

    @LuaFunction
    public final String MakeECDSASign(IArguments args) throws LuaException {
        String input = args.getString(0);
        String pri = args.getString(1);
        String result = "";
        byte[] signedBytes;
        try {
            Signature sign = Signature.getInstance("SHA256withECDSA");
            PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(hexStringToBytes(pri));
            KeyFactory kf = KeyFactory.getInstance("EC");
            PrivateKey key = kf.generatePrivate(pkcs);
            sign.initSign(key);
            sign.update(input.getBytes());
            signedBytes = sign.sign();
            result = bytesToHexString(signedBytes);
        } catch (Exception ignored) {

        }
        return result;
    }

    @LuaFunction
    public final boolean CheckECDSASign(IArguments args) throws LuaException {
        String input = args.getString(0);
        String pub = args.getString(1);
        String data = args.getString(2);
        boolean result = false;
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(hexStringToBytes(pub));
            KeyFactory kf = KeyFactory.getInstance("EC");
            PublicKey key = kf.generatePublic(x509);
            signature.initVerify(key);
            signature.update(data.getBytes());
            result = signature.verify(hexStringToBytes(input));
            } catch (Exception ignored) {
        }
        return result;
    }

    @LuaFunction
    public final String Salt() {
        byte[] b = new byte[0];
        try {
            SecureRandom sr = SecureRandom.getInstanceStrong();
            b = new byte[128];
            sr.nextBytes(b);
        } catch (NoSuchAlgorithmException ignored) {
        }
        return bytesToHexString(b);
    }
}
