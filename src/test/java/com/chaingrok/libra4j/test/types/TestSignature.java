package com.chaingrok.libra4j.test.types;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.chaingrok.libra4j.misc.Libra4jException;
import com.chaingrok.libra4j.misc.Utils;
import com.chaingrok.libra4j.test.TestClass;
import com.chaingrok.libra4j.types.Signature;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSignature extends TestClass {
	
	@Test
	public void test001_newInstanceOk() {
		byte[] bytes = Utils.getByteArray(Signature.BYTE_LENGTH,0x00);
		Signature signature = new Signature(bytes);
		assertSame(bytes,signature.getBytes());
	}
	
	@Test
	public void test001_newInstanceKo() {
		byte[] bytes = Utils.getByteArray(Signature.BYTE_LENGTH-1,0x00);
		try {
			new Signature(bytes);
			fail("invalid length shhould throw exception");
		} catch (Libra4jException e) {
			assertTrue(e.getMessage().startsWith("invalid length for signature"));
		}
		
	}

}