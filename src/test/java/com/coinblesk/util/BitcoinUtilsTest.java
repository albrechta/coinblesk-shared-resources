package com.coinblesk.util;

import java.util.ArrayList;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bitcoinj.core.Address;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.params.UnitTestParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.RedeemData;
import org.junit.Assert;
import org.junit.Test;

import com.coinblesk.bitcoin.TimeLockedAddress;

public class BitcoinUtilsTest {
    
    @Test
    public void sortTestOutputs() {
        Transaction tx = FakeTxBuilder.createFakeTx(UnitTestParams.get(), Coin.COIN, new ECKey());
        TransactionOutput to1 = tx.addOutput(Coin.valueOf(1), new ECKey());
        TransactionOutput to2 = tx.addOutput(Coin.valueOf(2), new ECKey());
        TransactionOutput to3 = tx.addOutput(Coin.valueOf(3), new ECKey());
        
        TransactionInput ti1 = tx.addInput(to1);
        TransactionInput ti2 = tx.addInput(to2);
        TransactionInput ti3 = tx.addInput(to3);
        
        List<TransactionOutput> txs1 = Arrays.asList(to1, to2, to3);
        List<TransactionOutput> txs2 = Arrays.asList(to2, to1, to3);
        List<TransactionOutput> txs3 = Arrays.asList(to3, to2, to1);
        List<TransactionOutput> txs4 = Arrays.asList(to3, to1, to2);
        List<TransactionOutput> txs5 = Arrays.asList(to1, to3, to2);
        List<TransactionOutput> txs6 = Arrays.asList(to2, to3, to1);
        
        List<TransactionOutput> reference = BitcoinUtils.sortOutputs(txs1);
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs1));
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs2));
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs3));
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs4));
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs5));
        Assert.assertEquals(reference, BitcoinUtils.sortOutputs(txs6));
        Assert.assertNotEquals(txs1, txs2);
        
    }
    
    @Test
    public void sortTestInputs() {
        Transaction tx = FakeTxBuilder.createFakeTx(UnitTestParams.get(), Coin.COIN, new ECKey());
        TransactionOutput to1 = tx.addOutput(Coin.valueOf(1), new ECKey());
        TransactionOutput to2 = tx.addOutput(Coin.valueOf(2), new ECKey());
        TransactionOutput to3 = tx.addOutput(Coin.valueOf(3), new ECKey());
        
        TransactionInput ti1 = tx.addInput(to1);
        TransactionInput ti2 = tx.addInput(to2);
        TransactionInput ti3 = tx.addInput(to3);
        
        List<TransactionInput> txs1 = Arrays.asList(ti1, ti2, ti3);
        List<TransactionInput> txs2 = Arrays.asList(ti2, ti1, ti3);
        List<TransactionInput> txs3 = Arrays.asList(ti3, ti2, ti1);
        List<TransactionInput> txs4 = Arrays.asList(ti3, ti1, ti2);
        List<TransactionInput> txs5 = Arrays.asList(ti1, ti3, ti2);
        List<TransactionInput> txs6 = Arrays.asList(ti2, ti3, ti1);
        List<TransactionInput> reference = BitcoinUtils.sortInputs(txs1);
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs1));
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs2));
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs3));
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs4));
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs5));
        Assert.assertEquals(reference, BitcoinUtils.sortInputs(txs6));
        Assert.assertNotEquals(txs1, txs2);
        
 
        
    }
    
    
    @Test
    public void locktimeTest() {
    	assertTrue(BitcoinUtils.isLockTimeByBlock(0)); // lock time disabled, i.e. block 0
    	assertTrue(BitcoinUtils.isLockTimeByBlock(1));
    	assertTrue(BitcoinUtils.isLockTimeByBlock(499999999));
    	assertFalse(BitcoinUtils.isLockTimeByBlock(500000000));
    	
    	assertFalse(BitcoinUtils.isLockTimeByTime(0));
    	assertFalse(BitcoinUtils.isLockTimeByTime(1));
    	assertFalse(BitcoinUtils.isLockTimeByTime(499999999));
    	assertTrue(BitcoinUtils.isLockTimeByTime(500000000));
    }
    
    @Test
    public void testIsBeforeLockTime() {
    	assertTrue( BitcoinUtils.isBeforeLockTime(100, 101) );
    	assertFalse( BitcoinUtils.isBeforeLockTime(101, 100) );
    	
    	assertTrue( BitcoinUtils.isBeforeLockTime(500000100, 500000101) );
    	assertFalse( BitcoinUtils.isBeforeLockTime(500000101, 500000100) );
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIsBeforeLockTime_DifferentTypes_1() {
    	BitcoinUtils.isBeforeLockTime(499999999, 500000000);
    	fail();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIsBeforeLockTime_DifferentTypes_2() {
    	BitcoinUtils.isBeforeLockTime(500000000, 499999999);
    	fail();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIsBeforeLockTime_NegativeCurrent() {
    	BitcoinUtils.isBeforeLockTime(-1, 500000000);
    	fail();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIsBeforeLockTime_NegativeToTest() {
    	BitcoinUtils.isBeforeLockTime(500000000, -1);
    	fail();
    }
    
    @Test
    public void testIsAfterLockTime() {
    	assertFalse( BitcoinUtils.isAfterLockTime(100, 101) );
    	assertTrue( BitcoinUtils.isAfterLockTime(101, 100) );
    	
    	assertFalse( BitcoinUtils.isAfterLockTime(500000100, 500000101) );
    	assertTrue( BitcoinUtils.isAfterLockTime(500000101, 500000100) );
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIsAfterLockTime_DifferentTypes_1() {
    	BitcoinUtils.isAfterLockTime(499999999, 500000000);
    	fail();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIsAfterLockTime_DifferentTypes_2() {
    	BitcoinUtils.isAfterLockTime(500000000, 499999999);
    	fail();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIsAfterLockTime_NegativeCurrent() {
    	BitcoinUtils.isAfterLockTime(-1, 500000000);
    	fail();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIsAfterLockTime_NegativeToTest() {
    	BitcoinUtils.isAfterLockTime(500000000, -1);
    	fail();
    }
    
    @Test(expected=InsufficientFunds.class)
    public void testCreateTxInsufficientFunds() throws InsufficientFunds, CoinbleskException {
        NetworkParameters params = UnitTestParams.get();      
        ECKey changeAddress = new ECKey();
        ECKey addressTo = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, addressTo);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), changeAddress.toAddress(params), 
                addressTo.toAddress(params), Coin.FIFTY_COINS.value + 1, true);
        tx.verify();
        tx = BitcoinUtils.sign(params, tx, addressTo);
        BitcoinUtils.verifyTxFull(tx);
        
    }
    
    @Test(expected=CoinbleskException.class)
    public void testCreateTxNoFundsForFee() throws InsufficientFunds, CoinbleskException {
        NetworkParameters params = UnitTestParams.get();
        ECKey changeAddress = new ECKey();
        ECKey addressTo = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, addressTo);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), changeAddress.toAddress(params), 
                addressTo.toAddress(params), Coin.FIFTY_COINS.value - 1, true);
        tx.verify();
        tx = BitcoinUtils.sign(params, tx, addressTo);
        BitcoinUtils.verifyTxFull(tx);
    }
    
    @Test
    public void testCreateTxFundsForFee() throws InsufficientFunds, CoinbleskException {
        NetworkParameters params = UnitTestParams.get();
        ECKey changeAddress = new ECKey();
        ECKey addressTo = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, addressTo);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), changeAddress.toAddress(params), 
                addressTo.toAddress(params), Coin.FIFTY_COINS.value, true);
        tx.verify();
        tx = BitcoinUtils.sign(params, tx, addressTo);
        BitcoinUtils.verifyTxFull(tx);
        Assert.assertEquals(1, tx.getOutputs().size());
        Assert.assertEquals(5760, tx.getFee().value);
        Assert.assertEquals(Coin.FIFTY_COINS.value - 5760, tx.getOutput(0).getValue().value);
    }
    
    @Test
    public void testCreateTxNoChange() throws InsufficientFunds, CoinbleskException {
        NetworkParameters params = UnitTestParams.get();
        ECKey changeAddress = new ECKey();
        ECKey addressTo = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, addressTo);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), changeAddress.toAddress(params), 
                addressTo.toAddress(params), Coin.FIFTY_COINS.value - 6000, true);
        tx.verify();
        tx = BitcoinUtils.sign(params, tx, addressTo);
        BitcoinUtils.verifyTxFull(tx);
        Assert.assertEquals(1, tx.getOutputs().size());
        Assert.assertEquals(5760, tx.getFee().value);
        Assert.assertEquals(Coin.FIFTY_COINS.value - 5760, tx.getOutput(0).getValue().value);
    }
    
    @Test
    public void testCreateTxNoChange2() throws InsufficientFunds, CoinbleskException {
        NetworkParameters params = UnitTestParams.get();
        ECKey changeAddress = new ECKey();
        ECKey addressTo = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, addressTo);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), changeAddress.toAddress(params), 
                addressTo.toAddress(params), Coin.FIFTY_COINS.value - 2000, false);
        tx.verify();
        tx = BitcoinUtils.sign(params, tx, addressTo);
        BitcoinUtils.verifyTxFull(tx);
        Assert.assertEquals(1, tx.getOutputs().size());
        Assert.assertEquals(5760, tx.getFee().value);
        Assert.assertEquals(Coin.FIFTY_COINS.value - 5760, tx.getOutput(0).getValue().value);
    }
    
    @Test
    public void testCreateTxChange() throws InsufficientFunds, CoinbleskException {
        NetworkParameters params = UnitTestParams.get();
        ECKey changeAddress = new ECKey();
        ECKey addressTo = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, addressTo);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), changeAddress.toAddress(params), 
                addressTo.toAddress(params), Coin.FIFTY_COINS.value - 20000, true);
        tx.verify();
        tx = BitcoinUtils.sign(params, tx, addressTo);
        BitcoinUtils.verifyTxFull(tx);
        Assert.assertEquals(2, tx.getOutputs().size());
        Assert.assertEquals(6780, tx.getFee().value);
        Assert.assertEquals(20000 - 6780, tx.getOutput(0).getValue().value);
        Assert.assertEquals(Coin.FIFTY_COINS.value - 20000, tx.getOutput(1).getValue().value);
    }
    
    @Test
    public void testCreateTxNoChangeRecv() throws InsufficientFunds, CoinbleskException {
        NetworkParameters params = UnitTestParams.get();
        ECKey changeAddress = new ECKey();
        ECKey addressTo = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, addressTo);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), changeAddress.toAddress(params), 
                addressTo.toAddress(params), Coin.FIFTY_COINS.value, false);
        tx.verify();
        tx = BitcoinUtils.sign(params, tx, addressTo);
        BitcoinUtils.verifyTxFull(tx);
        Assert.assertEquals(1, tx.getOutputs().size());
        Assert.assertEquals(5760, tx.getFee().value);
        Assert.assertEquals(Coin.FIFTY_COINS.value - 5760, tx.getOutput(0).getValue().value);
    }
    
    @Test
    public void testCreateTxNoChangeRecv2() throws InsufficientFunds, CoinbleskException {
        NetworkParameters params = UnitTestParams.get();
        ECKey changeAddress = new ECKey();
        ECKey addressTo = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, addressTo);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), changeAddress.toAddress(params), 
                addressTo.toAddress(params), Coin.FIFTY_COINS.value - 100, false);
        tx.verify();
        tx = BitcoinUtils.sign(params, tx, addressTo);
        BitcoinUtils.verifyTxFull(tx);
        Assert.assertEquals(1, tx.getOutputs().size());
        Assert.assertEquals(5760, tx.getFee().value);
        Assert.assertEquals(Coin.FIFTY_COINS.value - 5760, tx.getOutput(0).getValue().value);
    }
    
    @Test
    public void testCreateTxNoChangeRecv3() throws InsufficientFunds, CoinbleskException {
        NetworkParameters params = UnitTestParams.get();
        ECKey changeAddress = new ECKey();
        ECKey addressTo = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, addressTo);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), changeAddress.toAddress(params), 
                addressTo.toAddress(params), Coin.FIFTY_COINS.value - 20000, false);
        tx.verify();
        tx = BitcoinUtils.sign(params, tx, addressTo);
        BitcoinUtils.verifyTxFull(tx);
        Assert.assertEquals(2, tx.getOutputs().size());
        Assert.assertEquals(6780, tx.getFee().value);
        Assert.assertEquals(20000, tx.getOutput(0).getValue().value);
        Assert.assertEquals(Coin.FIFTY_COINS.value - 20000 - 6780, tx.getOutput(1).getValue().value);
    }
    
    @Test
    public void testFeeRegular() throws CoinbleskException, InsufficientFunds {
        NetworkParameters params = UnitTestParams.get();
        ECKey changeAddress = new ECKey();
        ECKey addressTo = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, addressTo);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), changeAddress.toAddress(params), 
                addressTo.toAddress(params), Coin.COIN.value, false);
        tx = BitcoinUtils.sign(params, tx, addressTo);
        
        System.out.println("tx len:"+tx.unsafeBitcoinSerialize().length);
        System.out.println("estimate:" + BitcoinUtils.estimateSize(2, 0, 1, 0));
        System.out.println("input len:"+tx.getInput(0).unsafeBitcoinSerialize().length);
        System.out.println("output len 1:"+tx.getOutput(0).unsafeBitcoinSerialize().length);
        System.out.println("output len 2:"+tx.getOutput(1).unsafeBitcoinSerialize().length);
        
        Assert.assertTrue(Math.abs(tx.unsafeBitcoinSerialize().length - BitcoinUtils.estimateSize(2, 0, 1, 0)) < 2);
    }
    
    @Test
    public void testFeeMultiSigOutput() throws CoinbleskException, InsufficientFunds {
        NetworkParameters params = UnitTestParams.get();
        ECKey changeAddress = new ECKey();
        ECKey ecKey = new ECKey();
        ECKey ecKeyServer = new ECKey();
        List<ECKey> list = new ArrayList<ECKey>();
        list.add(ecKey);
        list.add(ecKeyServer);
        Script p2shScript = BitcoinUtils.createP2SHOutputScript(2, list);
        Address p2shAddress = p2shScript.getToAddress(params);
        
        ECKey funding = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, funding);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), changeAddress.toAddress(params), 
                p2shAddress, Coin.COIN.value, false);
        tx = BitcoinUtils.sign(params, tx, funding);
        
        System.out.println("tx len:"+tx.unsafeBitcoinSerialize().length);
        System.out.println("estimate:" + BitcoinUtils.estimateSize(1, 1, 1, 0));
        System.out.println("input len:"+tx.getInput(0).unsafeBitcoinSerialize().length);
        System.out.println("output len 1:"+tx.getOutput(0).unsafeBitcoinSerialize().length);
        System.out.println("output len 2:"+tx.getOutput(1).unsafeBitcoinSerialize().length);
        
        Assert.assertTrue(Math.abs(tx.unsafeBitcoinSerialize().length - BitcoinUtils.estimateSize(1, 1, 1, 0)) < 2);
    }
    
    @Test
    public void testFeeTwoMultiSigOutput() throws CoinbleskException, InsufficientFunds {
        NetworkParameters params = UnitTestParams.get();
        
        ECKey ecKeychangeAddress = new ECKey();
        ECKey ecKeyServerchangeAddress = new ECKey();
        List<ECKey> listchangeAddress = new ArrayList<ECKey>();
        listchangeAddress.add(ecKeychangeAddress);
        listchangeAddress.add(ecKeyServerchangeAddress);
        Script p2shScriptchangeAddress = BitcoinUtils.createP2SHOutputScript(2, listchangeAddress);
        Address p2shAddresschangeAddress = p2shScriptchangeAddress.getToAddress(params);
        
        ECKey ecKey = new ECKey();
        ECKey ecKeyServer = new ECKey();
        List<ECKey> list = new ArrayList<ECKey>();
        list.add(ecKey);
        list.add(ecKeyServer);
        Script p2shScript = BitcoinUtils.createP2SHOutputScript(2, list);
        Address p2shAddress = p2shScript.getToAddress(params);
        
        ECKey funding = new ECKey();
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, funding);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), p2shAddresschangeAddress, 
                p2shAddress, Coin.COIN.value, false);
        tx = BitcoinUtils.sign(params, tx, funding);
        
        System.out.println("tx len:"+tx.unsafeBitcoinSerialize().length);
        System.out.println("estimate:" + BitcoinUtils.estimateSize(0, 2, 1, 0));
        System.out.println("input len:"+tx.getInput(0).unsafeBitcoinSerialize().length);
        System.out.println("output len 1:"+tx.getOutput(0).unsafeBitcoinSerialize().length);
        System.out.println("output len 2:"+tx.getOutput(1).unsafeBitcoinSerialize().length);
        
        Assert.assertTrue(Math.abs(tx.unsafeBitcoinSerialize().length - BitcoinUtils.estimateSize(0, 2, 1, 0)) < 2);
    }
    
    @Test
    public void testFeeEverythingMultiSig() throws CoinbleskException, InsufficientFunds {
        NetworkParameters params = UnitTestParams.get();
        
        ECKey ecKeychangeAddress = new ECKey();
        ECKey ecKeyServerchangeAddress = new ECKey();
        List<ECKey> listchangeAddress = new ArrayList<ECKey>();
        listchangeAddress.add(ecKeychangeAddress);
        listchangeAddress.add(ecKeyServerchangeAddress);
        Script p2shScriptchangeAddress = BitcoinUtils.createP2SHOutputScript(2, listchangeAddress);
        Address p2shAddresschangeAddress = p2shScriptchangeAddress.getToAddress(params);
        
        ECKey ecKey = new ECKey();
        ECKey ecKeyServer = new ECKey();
        List<ECKey> list = new ArrayList<ECKey>();
        list.add(ecKey);
        list.add(ecKeyServer);
        Script p2shScript = BitcoinUtils.createP2SHOutputScript(2, list);
        Address p2shAddress = p2shScript.getToAddress(params);
        
        ECKey ecKeyfunding = new ECKey();
        ECKey ecKeyServerfunding = new ECKey();
        List<ECKey> listfunding = new ArrayList<ECKey>();
        listfunding.add(ecKeyfunding);
        listfunding.add(ecKeyServerfunding);
        Script p2shScriptfunding = BitcoinUtils.createP2SHOutputScript(2, listfunding);
        Script redeem = BitcoinUtils.createRedeemScript(2, listfunding);
        Address p2shAddressfunding = p2shScriptfunding.getToAddress(params);
        System.out.println("we have a p2sh: "+p2shAddressfunding.isP2SHAddress());
        
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, p2shAddressfunding);
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), p2shAddresschangeAddress, 
                p2shAddress, Coin.COIN.value, false);
        List<TransactionSignature> sigs1 = BitcoinUtils.partiallySign(tx, redeem, ecKeyServerfunding);
        List<TransactionSignature> sigs2 = BitcoinUtils.partiallySign(tx, redeem, ecKeyfunding);
        
        Assert.assertTrue(BitcoinUtils.applySignatures(tx, redeem, sigs1, sigs2, true));
        
        System.out.println("tx len:"+tx.unsafeBitcoinSerialize().length);
        System.out.println("estimate:" + BitcoinUtils.estimateSize(0, 2, 0, 1));
        System.out.println("input len:"+tx.getInput(0).unsafeBitcoinSerialize().length);
        System.out.println("output len 1:"+tx.getOutput(0).unsafeBitcoinSerialize().length);
        System.out.println("output len 2:"+tx.getOutput(1).unsafeBitcoinSerialize().length);
        
        Assert.assertTrue(Math.abs(tx.unsafeBitcoinSerialize().length - BitcoinUtils.estimateSize(0, 2, 0, 1)) < 2);
    }
    
    @Test
    public void testSpendAll() throws CoinbleskException, InsufficientFunds {
        NetworkParameters params = UnitTestParams.get();
        
        ECKey ecKeychangeAddress = new ECKey();
        ECKey ecKeyServerchangeAddress = new ECKey();
        List<ECKey> listchangeAddress = new ArrayList<ECKey>();
        listchangeAddress.add(ecKeychangeAddress);
        listchangeAddress.add(ecKeyServerchangeAddress);
        Script p2shScriptchangeAddress = BitcoinUtils.createP2SHOutputScript(2, listchangeAddress);
        Address p2shAddresschangeAddress = p2shScriptchangeAddress.getToAddress(params);
        
        ECKey ecKey = new ECKey();
        
        Address destAddress = ecKey.toAddress(params);
        
        ECKey ecKeyfunding = new ECKey();
        ECKey ecKeyServerfunding = new ECKey();
        List<ECKey> listfunding = new ArrayList<ECKey>();
        listfunding.add(ecKeyfunding);
        listfunding.add(ecKeyServerfunding);
        Script p2shScriptfunding = BitcoinUtils.createP2SHOutputScript(2, listfunding);
        Script redeem = BitcoinUtils.createRedeemScript(2, listfunding);
        Address p2shAddressfunding = p2shScriptfunding.getToAddress(params);
        
        Transaction coinBase = FakeTxBuilder.createFakeCoinbaseTx(params, p2shAddressfunding);
        
        Transaction txAll = BitcoinUtils.createSpendAllTx(params, coinBase.getOutputs(), destAddress);
        
        Coin value = txAll.getInputSum();
        System.out.println("we can spend "+ value);
        
        Transaction tx = BitcoinUtils.createTx(params, coinBase.getOutputs(), p2shAddresschangeAddress, 
                destAddress, value.value, false);
        List<TransactionSignature> sigs1 = BitcoinUtils.partiallySign(tx, redeem, ecKeyServerfunding);
        List<TransactionSignature> sigs2 = BitcoinUtils.partiallySign(tx, redeem, ecKeyfunding);
        
        Assert.assertTrue(BitcoinUtils.applySignatures(tx, redeem, sigs1, sigs2, true));
        
        System.out.println("tx:"+tx.getOutputs().size());
        
        System.out.println("tx len:"+tx.unsafeBitcoinSerialize().length);
        System.out.println("tx out1:"+tx.getOutputs().get(0).unsafeBitcoinSerialize().length);
        System.out.println("tx in1:"+tx.getInputs().get(0).unsafeBitcoinSerialize().length);
        System.out.println("estimate:" + BitcoinUtils.estimateSize(1, 0, 0, 1));
        Assert.assertTrue(Math.abs(tx.unsafeBitcoinSerialize().length - BitcoinUtils.estimateSize(1, 0, 0, 1)) < 2);
    }
    
    @Test
    public void testSetFlagsOfCLTVInputs_AfterLocktime() {
    	NetworkParameters params = UnitTestParams.get();
    	
    	long nowSeconds = System.currentTimeMillis()/1000;
    	TimeLockedAddress tla1 = new TimeLockedAddress(new ECKey().getPubKey(), new ECKey().getPubKey(), nowSeconds-100);
    	TimeLockedAddress tla2 = new TimeLockedAddress(new ECKey().getPubKey(), new ECKey().getPubKey(), nowSeconds-10);
    	
    	Transaction fundingTx = FakeTxBuilder.createFakeTxWithChangeAddress(params, Coin.COIN, tla1.getAddress(params), tla2.getAddress(params));
        
        Transaction txAll = null;
		try {
			txAll = BitcoinUtils.createSpendAllTx(params, fundingTx.getOutputs(), new ECKey().toAddress(params));
		} catch (CoinbleskException e) {
			fail(e.getMessage());
		}
		
		Map<String, Long> outputTimeLocks = new HashMap<>();
		outputTimeLocks.put(SerializeUtils.bytesToHex(fundingTx.getOutput(0).getScriptPubKey().getPubKeyHash()).toLowerCase(), tla1.getLockTime());
		outputTimeLocks.put(SerializeUtils.bytesToHex(fundingTx.getOutput(1).getScriptPubKey().getPubKeyHash()).toLowerCase(), tla2.getLockTime());
		
		BitcoinUtils.setFlagsOfCLTVInputs(txAll, outputTimeLocks, nowSeconds);
    	
    	assertTrue(txAll.getLockTime() >= 1234567);
    	
    	// assert that inputs have max seqNo not set
    	for (TransactionInput txIn : txAll.getInputs()) {
    		assertTrue(txIn.getSequenceNumber() < TransactionInput.NO_SEQUENCE);
    	}
    }
    
    
        @Test
    public void testSetFlagsOfCLTVInputs_BeforeLocktime() {
    	NetworkParameters params = UnitTestParams.get();
    	
    	long nowSeconds = System.currentTimeMillis()/1000;
    	TimeLockedAddress tla1 = new TimeLockedAddress(new ECKey().getPubKey(), new ECKey().getPubKey(), nowSeconds+1000);
    	TimeLockedAddress tla2 = new TimeLockedAddress(new ECKey().getPubKey(), new ECKey().getPubKey(), nowSeconds+10000);
    	
    	Transaction fundingTx = FakeTxBuilder.createFakeTxWithChangeAddress(params, Coin.COIN, tla1.getAddress(params), tla2.getAddress(params));
        
        Transaction txAll = null;
		try {
			txAll = BitcoinUtils.createSpendAllTx(params, fundingTx.getOutputs(), new ECKey().toAddress(params));
		} catch (CoinbleskException e) {
			fail(e.getMessage());
		}
		
		Map<String, Long> outputTimeLocks = new HashMap<>();
		outputTimeLocks.put(SerializeUtils.bytesToHex(fundingTx.getOutput(0).getScriptPubKey().getPubKeyHash()).toLowerCase(), tla1.getLockTime());
		outputTimeLocks.put(SerializeUtils.bytesToHex(fundingTx.getOutput(1).getScriptPubKey().getPubKeyHash()).toLowerCase(), tla2.getLockTime());
		
		BitcoinUtils.setFlagsOfCLTVInputs(txAll, outputTimeLocks, nowSeconds);
    	
    	assertTrue(txAll.getLockTime() == 0);
    	
    	// assert that inputs have max seqNo set
    	for (TransactionInput txIn : txAll.getInputs()) {
    		assertTrue(txIn.getSequenceNumber() == TransactionInput.NO_SEQUENCE);
    	}
    }
}
