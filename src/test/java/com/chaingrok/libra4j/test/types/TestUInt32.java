package com.chaingrok.libra4j.test.types;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.chaingrok.libra4j.misc.Libra4jException;
import com.chaingrok.libra4j.misc.Libra4jLog;
import com.chaingrok.libra4j.misc.Libra4jLog.Type;
import com.chaingrok.libra4j.misc.Utils;
import com.chaingrok.libra4j.test.TestClass;
import com.chaingrok.libra4j.types.UInt32;
import com.google.protobuf.ByteString;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUInt32 extends TestClass {
	
	@Test
	public void test001WrongFormat() {
		//null with byte[]
		new UInt32((byte[])null);
		assertEquals(1,Libra4jLog.getLogs().size());
		assertEquals(Type.INVALID_LENGTH,Libra4jLog.getLogs().get(0).getType());
		Libra4jLog.purgeLogs();
		//null with ByteString
		new UInt32((ByteString)null);
		assertEquals(1,Libra4jLog.getLogs().size());
		assertEquals(Type.INVALID_LENGTH,Libra4jLog.getLogs().get(0).getType());
		Libra4jLog.purgeLogs();
		//no bytes
		byte[] bytes = {};
		new UInt32(bytes);
		assertEquals(1,Libra4jLog.getLogs().size());
		assertEquals(Type.INVALID_LENGTH,Libra4jLog.getLogs().get(0).getType());
		Libra4jLog.purgeLogs();
		//not enough bytes
		byte[] bytes2 = {0x00,0x01};
		new UInt32(bytes2);
		assertEquals(1,Libra4jLog.getLogs().size());
		assertEquals(Type.INVALID_LENGTH,Libra4jLog.getLogs().get(0).getType());
		Libra4jLog.purgeLogs();
		//too many bytes
		byte[] bytes3 = {0x00,0x01,0x02,0x03,0x04};
		new UInt32(bytes3);
		assertEquals(1,Libra4jLog.getLogs().size());
		assertEquals(Type.INVALID_LENGTH,Libra4jLog.getLogs().get(0).getType());
		Libra4jLog.purgeLogs();
	}
	
	@Test
	public void test002LongValues() {
		Long value = 0L;
		byte[] bytes = Utils.longToByteArray(value,UInt32.BYTE_LENGTH);
		ByteString byteString = ByteString.copyFrom(bytes);
		assertEquals(value,new UInt32(byteString).getAsLong());
		//
		value = 1L;
		bytes = Utils.longToByteArray(value,UInt32.BYTE_LENGTH);
		byteString = ByteString.copyFrom(bytes);
		assertEquals(value,new UInt32(byteString).getAsLong());
		//
		value = (long)Integer.MAX_VALUE;
		bytes = Utils.longToByteArray(value,UInt32.BYTE_LENGTH);
		byteString = ByteString.copyFrom(bytes);
		assertEquals(value,new UInt32(byteString).getAsLong());
	}
	
	@Test
	public void test003BiggerThanIntegerValues() {
		Long value = -1L;
		byte[] bytes = Utils.longToByteArray(value,UInt32.BYTE_LENGTH);
		ByteString byteString = ByteString.copyFrom(bytes);
		assertEquals((Long)UInt32.MAX_VALUE.longValue(),new UInt32(byteString).getAsLong());
		//
		value = new Long(Integer.MAX_VALUE) + 1L;
		assertTrue(value > 0);
		UInt32 uint32 = new UInt32(value);
		assertEquals(value,uint32.getAsLong());
	}
	
	@Test
	public void test004ContructFromLong() {
		Long value = 0L;
		UInt32 u32 = new UInt32(value);
		assertEquals(value,u32.getAsLong());
		//
		value = 1L;
		u32 = new UInt32(value);
		assertEquals(value,u32.getAsLong());
		//
		value = 100L;
		u32 = new UInt32(value);
		assertEquals(value,u32.getAsLong());
		//
		value = 123456L;
		u32 = new UInt32(value);
		assertEquals(value,u32.getAsLong());
		//
		value = (long)Integer.MAX_VALUE;
		u32 = new UInt32(value);
		assertEquals(value,u32.getAsLong());
		//
		value = UInt32.MAX_VALUE.longValue();
		u32 = new UInt32(value);
		assertEquals(value,u32.getAsLong());
		//
		value = -1L;
		try {
			new UInt32(value);
			fail("should fail with negative value");
		} catch (Libra4jException e) {
			assertEquals("UInt cannot be constructed from negative long value: " + value,e.getMessage());
		}
	}
	
	@Test
	public void test005GetBytes() {
		long value = 0L;
		UInt32 u32 = new UInt32(value);
		byte[] bytes = u32.getBytes();
		assertEquals(UInt32.BYTE_LENGTH,bytes.length);
		byte[] expected =  {0x00,0x00,0x00,0x00};
		assertArrayEquals(expected,bytes);
		//
		value = 1L;
		u32 = new UInt32(value);
		bytes = u32.getBytes();
		assertEquals(UInt32.BYTE_LENGTH,bytes.length);
		byte[]  expected2 =  {0x00,0x00,0x00,0x01};
		assertArrayEquals(expected2,bytes);
		//
		value = 100L;
		u32 = new UInt32(value);
		bytes = u32.getBytes();
		assertEquals(UInt32.BYTE_LENGTH,bytes.length);
		byte[]  expected3 =  {0x00,0x00,0x00,0x64};
		assertArrayEquals(expected3,bytes);
	}
	
	@Test
	public void test005BytesVsLongValues() {
		Long number = 123456L;
		UInt32 uint32 = new UInt32(number);
		assertEquals(number,(Long)uint32.getAsLong());
		byte[] bytes = uint32.getBytes();
		uint32 = new UInt32(bytes);
		assertEquals(number,(Long)uint32.getAsLong());
		
	}
	
	@Test
	public void test006Equals() {
		long value = 9999L;
		UInt32 uint_1 = new UInt32(value);
		UInt32 uint_2 = new UInt32(value);
		assertNotSame(uint_1,uint_2);
		assertEquals(uint_1,uint_2);
		//
		uint_2 = new UInt32(value + 1L);
		assertNotEquals(uint_1,uint_2);
		//
		assertFalse(uint_1.equals(null));
		//
		uint_1.equals(new Object());
		assertEquals(1,Libra4jLog.getLogs().size());
		assertEquals(Type.INVALID_CLASS,Libra4jLog.getLogs().get(0).getType());
		assertTrue(((String)Libra4jLog.getLogs().get(0).getObject()).contains("cannot compare objects of different classes"));
		Libra4jLog.purgeLogs();
	}
}
