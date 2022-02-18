package org.zlwl.wallet;


import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;

import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.zlwl.util.DecimalTool;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static org.zlwl.wallet.Infura.INFURA_MAINNET_URL;


public class EthTool {


    private static final Admin admin = Admin.build(new HttpService(INFURA_MAINNET_URL));


    private static final BigDecimal decimal = new BigDecimal("1000000000000000000");


    public static BigDecimal getEthBalance(String address, int decimals) throws IOException {
        BigInteger balance = admin.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        return new BigDecimal(balance).divide(DecimalTool.tenPow(decimals));
    }

    public static TransactionReceipt ethSend(Credentials credentials, Web3j client, String toAddress, BigDecimal amount) throws Exception {
        return Transfer.sendFunds(client, credentials, toAddress, amount, Convert.Unit.ETHER).send();
    }

    public static BigInteger getNonce(String address, Web3j web3j) throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
        if (ethGetTransactionCount == null) {
            throw new RuntimeException("failed to get nonce!");
        }
        return ethGetTransactionCount.getTransactionCount();
    }

    public static BigInteger getGesPrice(Web3j web3j) throws ExecutionException, InterruptedException {
        EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
        if (ethGasPrice == null) {
            throw new RuntimeException("failed to get gas price!");
        }
        return ethGasPrice.getGasPrice();
    }


    public String toDecimal(int decimal, BigInteger integer) {
        StringBuffer sbf = new StringBuffer("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        String balance = new BigDecimal(integer).divide(new BigDecimal(sbf.toString())).toPlainString();
        return balance;
    }


    public static void main(String[] args) {
        String add = "0x7339917906EcB9e5f7F6A2009e70bBEa7B0C1d43";


        try {
            BigInteger gasPrice1 = admin.ethGasPrice().send().getGasPrice();
            System.out.println(gasPrice1);

            BigInteger balance = admin.ethGetBalance(add, DefaultBlockParameterName.LATEST).send().getBalance();
            System.out.println(balance);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BigDecimal ethBalance = null;
        try {
            ethBalance = getEthBalance(add, 18);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(ethBalance);

    }
}
