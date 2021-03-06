package com.chaingrok.libra4j.types;

import com.chaingrok.lib.ChaingrokException;
import com.chaingrok.lib.Utils;
import com.google.protobuf.ByteString;

public class AccessPath {
	
	// Source : types/src/access_path.rs
	// Source : types/src/unit_tests/access_path_test.rs
	
	public static final byte CODE_TAG = 0x00;
	public static final byte RESOURCE_TAG = 0x01;
	public static final String SEPARATOR = "/";
	
	private byte tag;
	private AccountAddress accountAddress;
	private Path path;
	
	public AccessPath() {
	}
	
	public AccessPath(String hex) {
		this(Utils.hexStringToByteArray(hex));
	}
	
	public AccessPath(ByteString byteString) {
		this(byteString.toByteArray());
	}
	
	public AccessPath(byte[] bytes) {
		deserialize(bytes);
	}
	
	public AccessPath(byte tag, String addressHex, String pathHex) {
		this(tag, pathHex.getBytes(),addressHex.getBytes());
	}
	
	public AccessPath(byte tag, byte[] address, byte[] path) {
		this(tag, new AccountAddress(address), new Path(path));
	}
	
	public AccessPath(byte tag, AccountAddress accountAddress, Path path) {
		this.setTag(tag);
		this.path = path;
		this.accountAddress = accountAddress;
	}
	
	private void setTag(byte tag) {
		if ((tag != CODE_TAG) 
				&& (tag != RESOURCE_TAG)) {
			throw new ChaingrokException("invalid tag for access path:" + tag);
		}
		this.tag = tag;
	}
	
	public byte getTag() {
		return tag;
	}
	
	public Path getPath() {
		return path;
	}
	
	public void setPath(Path path) {
		this.path = path;
	}

	public AccountAddress getAccountAddress() {
		return accountAddress;
	}
	
	public void setAccountAddress(AccountAddress accountAddress) {
		this.accountAddress = accountAddress;
	}
	
	public void deserialize(byte[] bytes) {
		this.setTag(bytes[0]);
		byte[] addressBytes = new byte[AccountAddress.BYTE_LENGTH];
		System.arraycopy(bytes, 1, addressBytes, 0, addressBytes.length);
		byte[] pathBytes = new byte[bytes.length - 1 - AccountAddress.BYTE_LENGTH];
		System.arraycopy(bytes, 1+addressBytes.length, pathBytes, 0, pathBytes.length);
		accountAddress = new AccountAddress(addressBytes);
		path = new Path(pathBytes);
	}
	
	@Override
	public String toString() {
		String result = "";
		String accountAddress = "";
		String path = "";
		if (getAccountAddress() != null) {
			accountAddress = getAccountAddress().toString();
		}
		if (getPath() != null) {
			path = getPath().toString();
		}
		result += "access path - tag: " + tag + " - address: " + accountAddress + " - path: " + path;
		return result;
	}
	
	public static AccessPath create(byte tag,AccountAddress accountAddress,Path path) {
		byte[] tagArray = new byte[1];
		tagArray[0] = tag;
		return new AccessPath(Utils.byteArrayToHexString(tagArray) + Utils.byteArrayToHexString(accountAddress.getBytes()) + Utils.byteArrayToHexString(path.getBytes()));
	}

}
