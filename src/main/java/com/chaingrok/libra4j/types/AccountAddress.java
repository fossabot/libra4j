package com.chaingrok.libra4j.types;

import com.chaingrok.lib.ChaingrokError;
import com.chaingrok.lib.Utils;
import com.chaingrok.lib.ChaingrokLog.Type;

//Soource : types/src/account_address.rs

public class AccountAddress extends ByteArray {
	
	public static final String ADDRESS_ZERO = "0000000000000000000000000000000000000000000000000000000000000000";
	public static final AccountAddress ACCOUNT_ZERO = new AccountAddress(ADDRESS_ZERO);
	public static final String ADDRESS_TXN2 = "f7d5da0c23abd8a7d33fbf200ed9f5a25329020cfecb7d2375f7447b22940354";
	public static final AccountAddress ACCOUNT_TXN2 = new AccountAddress(ADDRESS_TXN2);
	
	public static final int BYTE_LENGTH = 32;
	public static final String LIBRA_NETWORK_ID_SHORT = "lb";
	
	public AccountAddress(String hex) {
		this(Utils.hexStringToByteArray(hex));  // NullException fine if bytes is null
	}
	
	public AccountAddress(byte[] bytes) {
		super(bytes);
		if (bytes == null) {
			new ChaingrokError(Type.NULL_DATA,"input byte array cannot be null for account address");
		} else {
			if (bytes.length != BYTE_LENGTH) {
				new ChaingrokError(Type.INVALID_LENGTH,"invalid account address size: " + bytes.length +  " <> " + BYTE_LENGTH);
			}
		}
	}
}
