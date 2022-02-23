package org.zlwl.wallet;


import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;

import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
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

    public static BigDecimal getEthBalance(String address, Web3j web3j) throws IOException {
        BigInteger balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        return Convert.fromWei(String.valueOf(balance), Convert.Unit.ETHER);
    }

    public static TransactionReceipt legacyTransfer(Credentials credentials, String toAddress, BigDecimal amount, Web3j web3j) throws Exception {
        return Transfer.sendFunds(web3j, credentials, toAddress, amount, Convert.Unit.ETHER).send();
    }

    public static TransactionReceipt transfer(Credentials credentials, String toAddress, BigDecimal amount, BigInteger gasLimit, BigInteger maxPriorityFeePerGas, BigInteger maxFeePerGas, Web3j web3j) throws Exception {
        return Transfer.sendFundsEIP1559(web3j, credentials, toAddress, amount, Convert.Unit.ETHER, gasLimit, maxPriorityFeePerGas, maxFeePerGas).send();
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

    public static long getChainId(Web3j web3j) throws IOException {
        return web3j.ethChainId().send().getChainId().longValue();
    }

    /**
     * 获取普通交易的gas上限
     *
     * @param transaction 交易对象
     * @return gas 上限
     */
    private static BigInteger getTransactionGasLimit(Transaction transaction, Web3j web3j) {
        BigInteger gasLimit = BigInteger.ZERO;
        try {
            EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
            gasLimit = ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gasLimit;
    }

}
