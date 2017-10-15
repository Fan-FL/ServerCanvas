package Server.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	private static String sKey = "distributed sym!";
	public static String getsKey() {
		return sKey;
	}

	/**
     * Encrypt using following parameters
     * @param plaintext 
     * @param key 
     * @return ciphertext
     */
    public static String Encrypt(String sSrc, String sKey) {
        try{
            if (sKey == null) {
                return null;
            }
            // �ж�Key�Ƿ�Ϊ16λ
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

            return Base64.getEncoder().encodeToString(encrypted);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static String Encrypt(String sSrc) {
    	return AES.Encrypt(sSrc, AES.sKey);
    }
    
    /**
     * Decrypt using following parameters
     * @param ciphertext 
     * @param key
     * @return plaintext
     */
    public static String Decrypt(String sSrc, String sKey) {
        try {
            // �ж�Key�Ƿ���ȷ
            if (sKey == null) {
                System.out.print("Key is null��");
                return null;
            }
            // �ж�Key�Ƿ�Ϊ16λ
            if (sKey.length() != 16) {
                System.out.print("The lenght of key must be 16.");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);//decrypt using base64 first
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
    
    public static String Decrypt(String sSrc) {
    	return AES.Decrypt(sSrc, AES.sKey);
    }
}
