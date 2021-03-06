package com.chaingrok.lib.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.chaingrok.lib.ChaingrokLog;
import com.chaingrok.lib.UInt8;
import com.chaingrok.lib.Utils;
import com.chaingrok.lib.ChaingrokLog.Type;
import com.google.protobuf.ByteString;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUInt8 extends TestClass {
	
	@Test
	public void test001WrongFormat() {
		//null with byte[]
		new UInt8((byte[])null);
		assertEquals(1,ChaingrokLog.getLogs().size());
		assertEquals(Type.INVALID_LENGTH,ChaingrokLog.getLogs().get(0).getType());
		ChaingrokLog.purgeLogs();
		//null with ByteString
		new UInt8((ByteString)null);
		assertEquals(1,ChaingrokLog.getLogs().size());
		assertEquals(Type.INVALID_LENGTH,ChaingrokLog.getLogs().get(0).getType());
		ChaingrokLog.purgeLogs();
		//no bytes
		byte[] bytes = {};
		new UInt8(bytes);
		assertEquals(1,ChaingrokLog.getLogs().size());
		assertEquals(Type.INVALID_LENGTH,ChaingrokLog.getLogs().get(0).getType());
		ChaingrokLog.purgeLogs();
		//too many bytes
		byte[] bytes3 = {0x00,0x01};
		new UInt8(bytes3);
		assertEquals(1,ChaingrokLog.getLogs().size());
		assertEquals(Type.INVALID_LENGTH,ChaingrokLog.getLogs().get(0).getType());
		ChaingrokLog.purgeLogs();
	}
	
	@Test
	public void test002LongValues() {
		Long value = 0L;
		byte[] bytes = Utils.longToByteArray(value,UInt8.BYTE_LENGTH);
		ByteString byteString = ByteString.copyFrom(bytes);
		assertEquals(value,new UInt8(byteString).getAsLong());
		//
		value = 1L;
		bytes = Utils.longToByteArray(value,UInt8.BYTE_LENGTH);
		byteString = ByteString.copyFrom(bytes);
		assertEquals(value,new UInt8(byteString).getAsLong());
		//
		value = (long)Byte.MAX_VALUE;
		bytes = Utils.longToByteArray(value,UInt8.BYTE_LENGTH);
		byteString = ByteString.copyFrom(bytes);
		assertEquals(value,new UInt8(byteString).getAsLong());
	}
	
	@Test
	public void test003BiggerThanIntegerValues() {
		Long value = -1L;
		byte[] bytes = Utils.longToByteArray(value,UInt8.BYTE_LENGTH);
		ByteString byteString = ByteString.copyFrom(bytes);
		assertEquals((Long)UInt8.MAX_VALUE.longValue(),new UInt8(byteString).getAsLong());
		//
		value = new Long(Byte.MAX_VALUE) + 1L;
		assertTrue(value > 0);
		UInt8 uint8 = new UInt8(value);
		assertEquals(value,uint8.getAsLong());
	}
	
	@Test
	public void test004ContructFromLong() {
		Long value = 0L;
		UInt8 u8 = new UInt8(value);
		assertEquals(value,u8.getAsLong());
		//
		value = 1L;
		u8 = new UInt8(value);
		assertEquals(value,u8.getAsLong());
		//
		value = 100L;
		u8 = new UInt8(value);
		assertEquals(value,u8.getAsLong());
		//
		value = 123L;
		u8 = new UInt8(value);
		assertEquals(value,u8.getAsLong());
		//
		value = new Long(Byte.MAX_VALUE);
		u8 = new UInt8(value);
		assertEquals(value,u8.getAsLong());
		//
		value = UInt8.MAX_VALUE.longValue();
		u8 = new UInt8(value);
		assertEquals(value,u8.getAsLong());
		//
		value = -1L;
		assertFalse(ChaingrokLog.hasLogs());
		new UInt8(value);
		assertEquals(1,ChaingrokLog.getLogs().size());
		assertEquals(Type.INVALID_VALUE,ChaingrokLog.getLogs().get(0).getType());
		ChaingrokLog.purgeLogs();
	}
	
	@Test
	public void test005GetBytes() {
		Long value = 0L;
		UInt8 u8 = new UInt8(value);
		byte[] bytes = u8.getBytes();
		assertEquals(UInt8.BYTE_LENGTH,bytes.length);
		byte[] expected =  {0x00};
		assertArrayEquals(expected,bytes);
		assertEquals(value,u8.getAsLong());
		//
		value = 1L;
		u8 = new UInt8(value);
		bytes = u8.getBytes();
		assertEquals(UInt8.BYTE_LENGTH,bytes.length);
		byte[]  expected2 =  {0x01};
		assertArrayEquals(expected2,bytes);
		assertEquals(value,u8.getAsLong());
		//
		value = 100L;
		u8 = new UInt8(value);
		bytes = u8.getBytes();
		assertEquals(UInt8.BYTE_LENGTH,bytes.length);
		byte[]  expected3 =  {0x64};
		assertArrayEquals(expected3,bytes);
		assertEquals(value,u8.getAsLong());
		//
		value = UInt8.MAX_VALUE.longValue();
		u8 = new UInt8(value);
		bytes = u8.getBytes();
		assertEquals(UInt8.BYTE_LENGTH,bytes.length);
		byte[]  expected4 =  {(byte)0xff};
		assertArrayEquals(expected4,bytes);
		assertEquals(value,u8.getAsLong());
	}
	
	@Test
	public void test005BytesVsLongValues() {
		Long number = 123L;
		UInt8 uint8 = new UInt8(number);
		assertEquals(number,(Long)uint8.getAsLong());
		byte[] bytes = uint8.getBytes();
		uint8 = new UInt8(bytes);
		assertEquals(number,(Long)uint8.getAsLong());
	}
	
	@Test
	public void test006Equals() {
		long value = 99L;
		UInt8 uint_1 = new UInt8(value);
		UInt8 uint_2 = new UInt8(value);
		assertNotSame(uint_1,uint_2);
		assertEquals(uint_1,uint_2);
		//
		uint_2 = new UInt8(value + 1L);
		assertNotEquals(uint_1,uint_2);
		//
		Object object = null;
		assertFalse(uint_1.equals(object)); //test the overriden equals against null value.
		//
		uint_1.equals(new Object());
		assertEquals(1,ChaingrokLog.getLogs().size());
		assertEquals(Type.INVALID_CLASS,ChaingrokLog.getLogs().get(0).getType());
		assertTrue(((String)ChaingrokLog.getLogs().get(0).getObject()).contains("cannot compare objects of different classes"));
		ChaingrokLog.purgeLogs();
	}
	
	@Test
	public void test007hashCode() {
		UInt8 uint8 = new UInt8(0L);
		assertEquals(0,uint8.hashCode());
		uint8 = new UInt8(1L);
		assertEquals(16777216,uint8.hashCode());
		uint8 = new UInt8(UInt8.MAX_VALUE.longValue());
		assertEquals(-16777216,uint8.hashCode());
	}
}
