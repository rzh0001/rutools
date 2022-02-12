package org.zlwl.wallet;


import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.contracts.token.ERC20BasicInterface;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Ethereum;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGasPrice;

import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.events.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;



public class Eth {

    private static final String DATA_PREFIX = "0x70a08231000000000000000000000000";

    private static final String INFURA_URL = "https://mainnet.infura.io/v3/4d30f4bbd2dd43938b80d8418607c2e6";

    private static final Admin admin = Admin.build(new HttpService(INFURA_URL));;

    private static final BigDecimal decimal = new BigDecimal("1000000000000000000");



    public static BigDecimal getEthBalance(String address) throws IOException {
            BigInteger balance = admin.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
            return new BigDecimal(balance).divide(decimal);
    }


    public static BigDecimal getERC20Balance(String address, String contractAddress) throws IOException {
        /**
         * data数据格式：最前边的“0x70a08231000000000000000000000000”是固定的，后边的是钱包地址（不带“0x”前缀）
         */
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(address,
                contractAddress, DATA_PREFIX + address.substring(2));
        EthCall ethCall = admin.ethCall(ethCallTransaction, DefaultBlockParameterName.PENDING).send();

        String s = new BigInteger(ethCall.getValue().substring(2), 16).toString();
//        BigDecimal balance = new BigDecimal(s).divide(WEI, 6, RoundingMode.HALF_DOWN);
        BigDecimal balance = new BigDecimal(s);
        return balance;
    }
    public static String getERC20Balance1(String address, String contractAddress) throws ExecutionException, InterruptedException {
        Function function = new Function("balanceOf",
                Arrays.asList(new Address(address)),
                Arrays.asList(new TypeReference<Address>() {
                }));

        String encode = FunctionEncoder.encode(function);
//        Log.i(TAG, "getERC20Balance encode : " + encode);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(address, contractAddress, encode);

        EthCall ethCall = admin.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        String value = ethCall.getResult();
//        Log.i(TAG, "getERC20Balance balance : " + value);
        return value;
    }

//    public String getERC20Balance2(String address, String contractAddress) throws Exception {
//        //加载合约
//        TokenERC20 contract = TokenERC20.load(
//                contractAddress, web3j, credentials, Contract.GAS_PRICE, Contract.GAS_LIMIT);
//        BigInteger balanceOf = contract.balanceOf(address).sendAsync().get();
//        String balance = toDecimal(18, balanceOf);
//
//        return balance;
//    }

    public String toDecimal(int decimal, BigInteger integer) {
        StringBuffer sbf = new StringBuffer("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        String balance = new BigDecimal(integer).divide(new BigDecimal(sbf.toString())).toPlainString();
        return balance;
    }

//    public static BigDecimal get

    public static final String USDT_CONTRACT = "0xdac17f958d2ee523a2206206994597c13d831ec7";

    public static void main(String[] args) {
        String add = "0x7339917906EcB9e5f7F6A2009e70bBEa7B0C1d43";
        try {
            BigDecimal balance = getERC20Balance(add, USDT_CONTRACT);
            System.out.println(balance);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            ethBalance = getEthBalance(add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(ethBalance);

        try {
            String erc20Balance1 = getERC20Balance1(add, USDT_CONTRACT);
            System.out.println(erc20Balance1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
