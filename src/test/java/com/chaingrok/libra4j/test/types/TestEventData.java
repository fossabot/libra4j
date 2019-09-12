package com.chaingrok.libra4j.test.types;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.chaingrok.libra4j.misc.Utils;
import com.chaingrok.libra4j.test.TestClass;
import com.chaingrok.libra4j.types.AccountAddress;
import com.chaingrok.libra4j.types.EventData;
import com.google.protobuf.ByteString;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestEventData extends TestClass {
	
	@Test
	public void test001NewInstance() {
		ByteString byteString  = ByteString.copyFrom(Utils.getByteArray(AccountAddress.BYTE_LENGTH * 2)); //length must be > Account address length to deserialize access path;
		EventData eventData = new EventData(byteString);
		assertEquals(byteString,eventData.getByteString());
	}

}
