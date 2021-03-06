package com.chaingrok.libra4j.types;

import org.bouncycastle.jcajce.provider.digest.SHA3;

import com.chaingrok.lib.ChaingrokError;
import com.chaingrok.lib.ChaingrokLog.Type;
import com.google.protobuf.ByteString;


//source : crypto/legacy_crypto/src/hash.rs

public class Hash extends ByteArray {
	
	public static final int BYTE_LENGTH = 32;
	
	public Hash(ByteString byteString) {
		this(byteString.toByteArray());
	}
	
	public Hash(byte[] bytes) {
		super(bytes);
		if (bytes.length != BYTE_LENGTH) {
			new ChaingrokError(Type.INVALID_LENGTH,"invalid hash size: " + bytes.length +  " <> " + BYTE_LENGTH);
		}
	}
	
	public static Hash hash(String str) {
		return hash(str.getBytes());
	}
	
	public static Hash hash(byte[] bytes) {
		SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest256();
        Hash result = new Hash(digestSHA3.digest(bytes));
        return result;
	}
	
	public static Hash hashWithSalt(String str, HashSalt salt) {
		return hash(str.getBytes(),salt);
	}
	
	public static Hash hash(byte[] bytes,HashSalt salt) {
		SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest256();
		byte[] saltBytes = salt.getSalt().getBytes();
		byte[] allBytes = new byte[bytes.length + saltBytes.length];
        System.arraycopy(bytes, 0, allBytes, 0, bytes.length);
        System.arraycopy(saltBytes, 0, allBytes, bytes.length, saltBytes.length);
        Hash result = new Hash(digestSHA3.digest(allBytes));
        return result;
	}
	
	public enum HashSalt {  //as per crypto/crypto/src/hash.rs - see define_hasher functions()
		
		ACCOUNT_ADDRESS("AccountAddress"),
		ACCOUNT_STATE_BLOB("AccountStateBlob"),
		RAW_TRANSACTION("RawTransaction"),
		SIGNED_TRANSACTION("SignedTransaction"),
		TRANSACTION_INFO("TransactionInfo"),
		
		;
		
		public static final String LIBRA_HASH_SUFFIX = "@@$$LIBRA$$@@";  //as per crypto/crypto/src/hash.rs
		
		private String salt = null;
		
		private HashSalt(String salt)  {
			this.salt = salt;
		}
		
		public String getSalt() {
			return salt + LIBRA_HASH_SUFFIX ;
		}

	}

}
