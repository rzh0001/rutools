package org.zlwl.wallet;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.zlwl.util.DecimalTool;
import org.zlwl.wallet.util.MnemonicTool;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ruanzh.eth
 */
class ERC20TokenToolTest {

    private Web3j web3j;
    private String contract;
    private String myAddress;
    private String toAddress;

    private Credentials credentials;

    private String symbol;
    private String name;
    private int decimals;
    private BigInteger totalSupply;
    private BigDecimal balance;

    private BigInteger maxPriorityFeePerGas;
    private BigInteger maxFeePerGas;
    private BigDecimal amount;


    @BeforeEach
    private void init() {
        web3j = Infura.rinkeby;
        contract = "0x01BE23585060835E02B77ef475b0Cc51aA1e0709";
        symbol = "LINK";
        name = "ChainLink Token";
        decimals = 18;
        totalSupply = new BigInteger("1000000000");

        String mnemonic = "first ceiling cupboard fresh wood situate dilemma primary october mobile family coast";
        credentials = MnemonicTool.loadEthDefaultWallet(mnemonic).getCredentials();
        toAddress = "0x3B3B43614C7E8e362A1f680d0696E054D9d850Db";
        balance = new BigDecimal("296");

        maxPriorityFeePerGas = new BigInteger("3000");
        maxFeePerGas = new BigInteger("8000");
        amount = new BigDecimal(2);
    }

    @Test
    void totalSupply() throws ExecutionException, InterruptedException {
        BigInteger s = ERC20TokenTool.totalSupply(contract, web3j);
        assertEquals(totalSupply, s.divide(DecimalTool.tenPow(decimals).toBigInteger()));
    }

    @Test
    void decimals() throws ExecutionException, InterruptedException {
        BigInteger s = ERC20TokenTool.decimals(contract, web3j);
        assertEquals(decimals, s.intValue());
    }

    @Test
    void name() throws ExecutionException, InterruptedException {
        String s = ERC20TokenTool.name(contract, web3j);
        assertEquals(name, s);
    }

    @Test
    void symbol() throws ExecutionException, InterruptedException {
        String s = ERC20TokenTool.symbol(contract, web3j);
        assertEquals(symbol, s);
    }

    @Test
    void balanceOf() throws ExecutionException, InterruptedException {
        String s = ERC20TokenTool.balanceOf(credentials.getAddress(), contract, web3j);
        assertEquals(0, balance.compareTo(new BigDecimal(s).divide(DecimalTool.tenPow(decimals))));
    }

    @Test
    void transfer() throws IOException, ExecutionException, InterruptedException {
        EthSendTransaction s = ERC20TokenTool.transfer(toAddress, amount.multiply(DecimalTool.tenPow(decimals)).toBigInteger(), contract, maxPriorityFeePerGas, maxFeePerGas, credentials, web3j);
        System.out.println(s);
    }

    @Test
    void legacyTransfer() throws IOException, ExecutionException, InterruptedException {
        String s = ERC20TokenTool.legacyTransfer(toAddress, amount.multiply(DecimalTool.tenPow(decimals)).toBigInteger(), contract, credentials, web3j);
        System.out.println(s);
    }

    @Test
    void balanceOf2() {
    }
}