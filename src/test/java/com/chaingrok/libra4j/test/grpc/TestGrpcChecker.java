package com.chaingrok.libra4j.test.grpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.chaingrok.libra4j.grpc.GrpcChecker;
import com.chaingrok.libra4j.misc.Libra4jLog;
import com.chaingrok.libra4j.misc.Utils;
import com.chaingrok.libra4j.test.TestClass;
import com.chaingrok.libra4j.types.AccountAddress;
import com.chaingrok.libra4j.types.Hash;
import com.chaingrok.libra4j.types.Signature;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.UnknownFieldSet;
import com.google.protobuf.UnknownFieldSet.Field;

import org.libra.grpc.types.LedgerInfoOuterClass.LedgerInfo;
import org.libra.grpc.types.LedgerInfoOuterClass.ValidatorSignature;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGrpcChecker extends TestClass {
	
	
	@Test
	public void test001CheckLedgerInfoEmpty() {
		GrpcChecker grpcChecker = new GrpcChecker();
		//
		LedgerInfo ledgerInfo = LedgerInfo.newBuilder()
									.build();
		assertFalse(grpcChecker.checkLedgerInfo(ledgerInfo));
		assertTrue(Libra4jLog.hasLogs());
		assertEquals(5,Libra4jLog.getLogs().size());
		Libra4jLog.purgeLogs();
	}
	
	
	@Test
	public void test002CheckLedgerInfoOk() {
		GrpcChecker grpcChecker = new GrpcChecker();
		//
		long version = 123L;
		long timestampUsecs = 456789L;
		ByteString consensusBlockId = ByteString.copyFrom(Utils.getByteArray(Hash.BYTE_LENGTH,0x11));
		ByteString consensusDataHash = ByteString.copyFrom(Utils.getByteArray(Hash.BYTE_LENGTH,0x22));
		ByteString transactionAccumulatorHash = ByteString.copyFrom(Utils.getByteArray(Hash.BYTE_LENGTH,0x33));
		LedgerInfo ledgerInfo = LedgerInfo.newBuilder()
									.setVersion(version)
									.setTimestampUsecs(timestampUsecs)
									.setConsensusBlockId(consensusBlockId)
									.setConsensusDataHash(consensusDataHash)
									.setTransactionAccumulatorHash(transactionAccumulatorHash)
									.build();
		assertTrue(grpcChecker.checkLedgerInfo(ledgerInfo));
		assertFalse(Libra4jLog.hasLogs());
	}
	
	@Test
	public void test003CheckValidatorSignatureKo() {
		GrpcChecker grpcChecker = new GrpcChecker();
		//
		ValidatorSignature validatorSignature = ValidatorSignature.newBuilder()
									.build();
		assertFalse(grpcChecker.checkValidatorSignature(validatorSignature));
		assertTrue(Libra4jLog.hasLogs());
		assertEquals(2,Libra4jLog.getLogs().size());
		Libra4jLog.purgeLogs();
	}
	
	@Test
	public void test004CheckValidatorSignatureOk() {
		GrpcChecker grpcChecker = new GrpcChecker();
		//
		ByteString accountAddress =ByteString.copyFrom(Utils.getByteArray(AccountAddress.BYTE_LENGTH,0x33));
		ByteString signature = ByteString.copyFrom(Utils.getByteArray(Signature.BYTE_LENGTH,0x33));
		ValidatorSignature validatorSignature = ValidatorSignature.newBuilder()
									.setValidatorId(accountAddress)
									.setSignature(signature)
									.build();
		assertTrue(grpcChecker.checkValidatorSignature(validatorSignature));
	}
	
	@Test
	public void test005CheckFieldErrorsOk() {
		GrpcChecker grpcChecker = new GrpcChecker();
		//
		assertTrue(grpcChecker.checkFieldErrors(null,null));
		//
		UnknownFieldSet unknownFieldSet = UnknownFieldSet.newBuilder()
									.build();
		assertTrue(grpcChecker.checkFieldErrors(null,unknownFieldSet));
		ArrayList<String> initializationErrors = new ArrayList<String>();
		assertTrue(grpcChecker.checkFieldErrors(initializationErrors,unknownFieldSet));
		assertTrue(grpcChecker.checkFieldErrors(initializationErrors,null));
	}
	
	@Test
	public void test006CheckFieldErrorsKo() {
		GrpcChecker grpcChecker = new GrpcChecker();
		//
		assertTrue(grpcChecker.checkFieldErrors(null,null));
		//
		Field field  = Field.newBuilder()
					.build();
		UnknownFieldSet unknownFieldSet = UnknownFieldSet.newBuilder()
									.addField(1,field)
									.build();
		assertFalse(grpcChecker.checkFieldErrors(null,unknownFieldSet));
		assertTrue(Libra4jLog.hasLogs());
		assertEquals(1,Libra4jLog.getLogs().size());
		Libra4jLog.purgeLogs();
		//
		ArrayList<String> initializationErrors = new ArrayList<String>();
		initializationErrors.add("error");
		assertFalse(grpcChecker.checkFieldErrors(initializationErrors,null));
		assertTrue(Libra4jLog.hasLogs());
		assertEquals(1,Libra4jLog.getLogs().size());
		Libra4jLog.purgeLogs();
	}
	
	@Test
	public void test007CheckExpectedFieldsOkEdgeCases() {
		GrpcChecker grpcChecker = new GrpcChecker();
		assertTrue(grpcChecker.checkExpectedFields(null,0));
		assertTrue(grpcChecker.checkExpectedFields(new Object(),0));
	}
	
	@Test
	public void test008CheckExpectedFieldsOkForMessageOrBuilder() {
		GrpcChecker grpcChecker = new GrpcChecker();
		//
		int expectedSize = 5;
		long version = 123L;
		long timestampUsecs = 456789L;
		ByteString consensusBlockId = ByteString.copyFrom(Utils.getByteArray(Hash.BYTE_LENGTH,0x11));
		ByteString consensusDataHash = ByteString.copyFrom(Utils.getByteArray(Hash.BYTE_LENGTH,0x22));
		ByteString transactionAccumulatorHash = ByteString.copyFrom(Utils.getByteArray(Hash.BYTE_LENGTH,0x33));
		LedgerInfo ledgerInfo = LedgerInfo.newBuilder()
									.setVersion(version)
									.setTimestampUsecs(timestampUsecs)
									.setConsensusBlockId(consensusBlockId)
									.setConsensusDataHash(consensusDataHash)
									.setTransactionAccumulatorHash(transactionAccumulatorHash)
									.build();
		assertTrue(ledgerInfo instanceof MessageOrBuilder);
		Map<FieldDescriptor, Object> fields = ledgerInfo.getAllFields();
		assertEquals(expectedSize,fields.size());
		assertTrue(grpcChecker.checkExpectedFields(ledgerInfo,expectedSize));
	}
	
	@Test
	public void test007CheckExpectedFieldsKo() {
		GrpcChecker grpcChecker = new GrpcChecker();
		assertFalse(grpcChecker.checkExpectedFields(null,1));
		assertFalse(grpcChecker.checkExpectedFields(new Object(),1));
		//
	}
	
}
