package com.chaingrok.libra4j.test.types;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.libra.grpc.types.Transaction.RawTransaction;

import com.chaingrok.libra4j.grpc.GrpcField;
import com.chaingrok.libra4j.misc.Libra4jError;
import com.chaingrok.libra4j.test.TestClass;
import com.chaingrok.libra4j.test.TestData;
import com.chaingrok.libra4j.types.AccountAddress;
import com.chaingrok.libra4j.types.AccountState;
import com.chaingrok.libra4j.types.Ledger;
import com.chaingrok.libra4j.types.LedgerInfo;
import com.chaingrok.libra4j.types.Transaction;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLedger extends TestClass {
	
	
	//@Test
	public void test001GetRawTransactionsFullName() {
		assertEquals(GrpcField.RAW_TRANSACTION.getFullName(),RawTransaction.getDescriptor().getFullName());
	}
	
	
	//@Test
	public void test002GetTransactionsWithoutEvents() {
		long version = 123L;
		long count = 1;
		boolean withEvents = false;
		Ledger ledger = new Ledger(TestData.VALIDATOR_ENDPOINT);
		ArrayList<Transaction> transactions = ledger.getTransactions(version,count,withEvents);
		assertEquals(1L,ledger.getRequestCount());
		assertFalse(Libra4jError.hasLogs());
		assertNotNull(transactions);
		assertEquals(transactions.size(),count);
		for (Transaction transaction : transactions) {
			assertEquals(transaction.getVersion(),(Long)version++);
			System.out.println(transaction.toString());
		}
	}
	
	//@Test
	public void test003GetTransactionsWithEvents() {
		long version = 123L;
		long count = 10;
		boolean withEvents = true;
		Ledger ledger = new Ledger(TestData.VALIDATOR_ENDPOINT);
		ArrayList<Transaction> transactions = ledger.getTransactions(version,count,withEvents);
		assertEquals(1L,ledger.getRequestCount());
		assertFalse(Libra4jError.hasLogs());
		assertNotNull(transactions);
		assertEquals(transactions.size(),count);
		for (Transaction transaction : transactions) {
			assertEquals((Long)version++,transaction.getVersion());
			System.out.println(transaction.toString());
		}
	}
	
	//@Test
	public void test004GetTransactionWithoutEvents() {
		long version = 1L;
		boolean withEvents = false;
		Ledger ledger = new Ledger(TestData.VALIDATOR_ENDPOINT);
		Transaction transaction = ledger.getTransaction(version,withEvents);
		assertEquals(1L,ledger.getRequestCount());
		assertFalse(Libra4jError.hasLogs());
		assertNotNull(transaction);
		assertEquals(version,(long)transaction.getVersion());
	}
	
	//@Test
	public void test005GetTransactionWithEvents() {
		long version = 55L;
		//long version = 1L;
		boolean withEvents = true;
		Ledger ledger = new Ledger(TestData.VALIDATOR_ENDPOINT);
		Transaction transaction = ledger.getTransaction(version,withEvents);
		assertEquals(1L,ledger.getRequestCount());
		assertNotNull(transaction);
		assertEquals(version,(long)transaction.getVersion());
		System.out.println(transaction.toString());
		assertFalse(Libra4jError.hasLogs());
	}
	
	
	
	//@Test
	public void test006GetAccountState() {
		Ledger ledger = new Ledger(TestData.VALIDATOR_ENDPOINT);
		AccountAddress accountAddress = AccountAddress.ACCOUNT_ZERO;
		AccountState accountState = ledger.getAccountState(accountAddress);
		assertEquals(1L,ledger.getRequestCount());
		assertFalse(Libra4jError.hasLogs());
		assertNotNull(accountState);
		assertTrue(accountState.getVersion() > 0);
	}
	
	//@Test
	public void test007GetEventsbyEventAccessPath() {
		Ledger ledger = new Ledger(TestData.VALIDATOR_ENDPOINT);
		AccountAddress accountAddress = AccountAddress.ACCOUNT_ZERO;
		long count = 10L;
		ledger.getEventsbyEventAccessPath(accountAddress,count);
		assertEquals(1L,ledger.getRequestCount());
		assertFalse(Libra4jError.hasLogs());
	}
	
	//@Test
	public void test008GetAccountTransactionsBySequenceNumber() {
		Ledger ledger = new Ledger(TestData.VALIDATOR_ENDPOINT);
		AccountAddress accountAddress = AccountAddress.ACCOUNT_ZERO;
		//AccountAddress accountAddress = new AccountAddress("ab16ad163ec915ba9acd2e7c599fc04f3a70b3ee9df3780eea4cb7a9b9b07a9e");
		long sequence = 1L;
		Transaction transaction = ledger.getAccountTransactionBySequenceNumber(accountAddress,sequence);
		assertEquals(1L,ledger.getRequestCount());
		assertFalse(Libra4jError.hasLogs());
		assertNotNull(transaction);
		assertTrue(transaction.getRawTxnBytes().length > 0);
		assertNotNull(transaction.getProgram());
		assertTrue(transaction.getProgram().getArguments().size() > 0);
		//assertNotNull(transaction.getVersion());
		//assertTrue(transaction.getVersion() > 0);
	}
	
	//@Test
	public void test009GetLedgerInfo() {
		Ledger ledger = new Ledger(TestData.VALIDATOR_ENDPOINT);
		LedgerInfo ledgerInfo = ledger.getLedgerInfo();
		assertEquals(1L,ledger.getRequestCount());
		assertFalse(Libra4jError.hasLogs());
		assertNotNull(ledgerInfo);
		System.out.println(ledgerInfo.toString());
	}
}