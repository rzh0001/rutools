package org.zlwl.wallet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.zlwl.wallet.util.MnemonicTool;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ruanzh.eth
 */
class EthToolTest {

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
    private BigInteger gasLimit;
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

        maxPriorityFeePerGas = new BigInteger("30");
        maxFeePerGas = new BigInteger("80");
        gasLimit = BigInteger.valueOf(60000L);
        amount = new BigDecimal(2);
    }

    @Test
    void getEthBalance() throws IOException {
        BigDecimal ethBalance = EthTool.getEthBalance(credentials.getAddress(), web3j);
        System.out.println(ethBalance);
    }

    @Test
    void legacyTransfer() throws Exception {
        TransactionReceipt receipt = EthTool.legacyTransfer(credentials, toAddress, new BigDecimal("0.1"), web3j);
        System.out.println(receipt.isStatusOK());
        System.out.println(receipt.getTransactionHash());

    }

    @Test
    void transfer() throws Exception {
//        TransactionReceipt receipt = EthTool.transfer(credentials, toAddress, BigDecimal.valueOf(1), BigInteger.valueOf(8_000_000), DefaultGasProvider.GAS_LIMIT, BigInteger.valueOf(3_100_000_000L), web3j);

        TransactionReceipt receipt = Transfer.sendFundsEIP1559(
                web3j, credentials,
                toAddress, //toAddress
                BigDecimal.valueOf(1), //value
                Convert.Unit.ETHER, //unit
                BigInteger.valueOf(8_000_000),
                DefaultGasProvider.GAS_LIMIT, //maxPriorityFeePerGas (max fee per gas transaction willing to give to miners)
                BigInteger.valueOf(3_100_000_000L) //maxFeePerGas (max fee transaction willing to pay)
        ).send();
        System.out.println(receipt.isStatusOK());
        System.out.println(receipt.getTransactionHash());
    }

    @Test
    void getNonce() {
    }

    @Test
    void getGesPrice() {
    }

    @Test
    void getChainId() {
    }


}