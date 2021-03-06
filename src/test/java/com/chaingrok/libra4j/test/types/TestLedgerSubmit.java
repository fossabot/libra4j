package com.chaingrok.libra4j.test.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.chaingrok.lib.ChaingrokLog;
import com.chaingrok.lib.UInt64;
import com.chaingrok.libra4j.crypto.KeyPair;
import com.chaingrok.libra4j.misc.Minter;
import com.chaingrok.libra4j.test.TestClassLibra;
import com.chaingrok.libra4j.types.AccountAddress;
import com.chaingrok.libra4j.types.Argument;
import com.chaingrok.libra4j.types.Arguments;
import com.chaingrok.libra4j.types.Code;
import com.chaingrok.libra4j.types.Program;
import com.chaingrok.libra4j.types.Transaction;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLedgerSubmit extends TestClassLibra {
	
	
	@Test
	public void test001SubmitOkFalse() {
		ledger.submitTransaction(null);
		assertFalse(ledger.submitOk());
		assertEquals(2,(int)ledger.getStatusCaseNumber());
		assertEquals(2,(int)ledger.getAcStatusCodeValue());
		assertEquals(0L,(long)ledger.getVmStatusMajorStatus());
		assertEquals(0L,(long)ledger.getVmStatusSubStatus());
		assertEquals(0,(int)ledger.getMempoolStatusCodeValue());
		assertNull(ledger.getValidator());
	}
	
	@Test
	public void test002SubmitTransferTransaction() {
		long sequenceNumber = 1L;
		long gasUnitPrice = 1L;
		long maxGasAmount = 200_000L;
		long expirationTime = System.currentTimeMillis() / 1000 + 120; //120s until expiration
		long mintedMicroLibras = 10_000_000L;
		//
		KeyPair senderKeyPair = KeyPair.random();
		KeyPair receiverKeyPair = KeyPair.random();
		//
		AccountAddress senderAccountAddress = new AccountAddress(senderKeyPair.getLibraAddress());
		System.out.println(senderAccountAddress.toString());
		AccountAddress receiverAccountAddress = new AccountAddress(receiverKeyPair.getLibraAddress());
		System.out.println(receiverAccountAddress.toString());
		//
		new Minter()
				.mint(senderAccountAddress, mintedMicroLibras)
				.mint(receiverAccountAddress, mintedMicroLibras);
		//
		long transferredAmount = mintedMicroLibras / 10;
		Argument amountArgument = new Argument()
				.setUInt64(new UInt64(transferredAmount));
		Argument receiverArgument = new Argument()
				.setAccountAddress(receiverAccountAddress);
		Arguments arguments = new Arguments()
				.set(amountArgument)
				.set(receiverArgument);
		Program program = new Program()
				.setCode(Code.PEER_TO_PEER_TRANSFER)
				.setArguments(arguments);
		Transaction transaction = new Transaction()
				.setSenderAccountAddress(senderAccountAddress)
				.setSequenceNumber(new UInt64(sequenceNumber))
				.setProgram(program)
				.setMaxGasAmount(new UInt64(maxGasAmount))
				.setGasUnitPrice(new UInt64(gasUnitPrice))
				.setExpirationTime(new UInt64(expirationTime))
				;
		ledger.submitTransaction(transaction);
		ChaingrokLog.purgeLogs();
	}
	
	@Test
	public void test999testShutdown() throws InterruptedException {
		//assertTrue(ledger.shutdown());
	}
	
	
}
