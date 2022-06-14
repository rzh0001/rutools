package org.zlwl.wallet;

import org.jetbrains.annotations.NotNull;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Numeric;
import org.zlwl.util.DecimalTool;
import org.zlwl.wallet.entity.EthAccount;
import org.zlwl.wallet.util.MnemonicTool;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.zlwl.wallet.EthTool.getChainId;

/**
 * @author ruanzh.eth
 */
public class ERC20TokenTool {

    public static BigInteger totalSupply(String contract, Web3j web3j) throws ExecutionException, InterruptedException {
        String name = "totalSupply";
        List<TypeReference<?>> outputParameters = List.of(new TypeReference<Uint256>() {
        });
        Function function = new Function(name, new ArrayList<>(), outputParameters);
        EthCall response = query(function, Ox00, contract, web3j);
        List<Type> results = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        return (BigInteger) results.get(0).getValue();

    }

    public static BigInteger decimals(String contract, Web3j web3j) throws ExecutionException, InterruptedException {
        String name = "decimals";
        List<TypeReference<?>> outputParameters = List.of(new TypeReference<Uint256>() {
        });
        Function function = new Function(name, new ArrayList<>(), outputParameters);
        EthCall response = query(function, Ox00, contract, web3j);
        List<Type> results = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        return (BigInteger) results.get(0).getValue();
    }

    public static String name(String contract, Web3j web3j) throws ExecutionException, InterruptedException {
        String name = "name";
        List<TypeReference<?>> outputParameters = List.of(new TypeReference<Utf8String>() {
        });
        Function function = new Function(name, new ArrayList<>(), outputParameters);
        EthCall response = query(function, Ox00, contract, web3j);
        List<Type> results = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        return results.get(0).getValue().toString();
    }

    public static String symbol(String contract, Web3j web3j) throws ExecutionException, InterruptedException {
        String name = "symbol";
        List<TypeReference<?>> outputParameters = List.of(new TypeReference<Utf8String>() {
        });
        Function function = new Function(name, new ArrayList<>(), outputParameters);
        EthCall response = query(function, Ox00, contract, web3j);
        List<Type> results = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
        return results.get(0).getValue().toString();
    }

    public static String balanceOf(String address, String contract, Web3j web3j) throws ExecutionException, InterruptedException {
        Function function = new Function("balanceOf",
                List.of(new Address(address)),
                List.of(new TypeReference<Address>() {
                }));

        EthCall response = query(function, address, contract, web3j);
        return hex2dec(response.getValue());
    }

    /**
     * ERC20代币转账
     *
     * @param to          收款地址
     * @param amount      转账金额
     * @param contract    代币合约地址
     * @param credentials 密钥
     * @param web3j       client
     * @return 交易哈希
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    public static String legacyTransfer(String to, BigInteger amount, String contract, Credentials credentials, Web3j web3j) throws ExecutionException, InterruptedException, IOException {
        Function function = new Function("transfer",
                Arrays.asList(new Address(to), new Uint256(amount)),
                List.of(new TypeReference<Address>() {
                }));

        //获取交易笔数
        BigInteger nonce = EthTool.getNonce(credentials.getAddress(), web3j);

        //手续费
        BigInteger gasPrice = EthTool.getGesPrice(web3j);

        //注意手续费的设置，这块很容易遇到问题
        BigInteger gasLimit = BigInteger.valueOf(600000L);


        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contract, encodedFunction);

        //进行签名操作
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signMessage);
        //发起交易
        return web3j.ethSendRawTransaction(hexValue).sendAsync().get().getTransactionHash();
    }

    public static EthSendTransaction transfer(String to, BigInteger amount, String contract, BigInteger maxPriorityFeePerGas, BigInteger maxFeePerGas, Credentials credentials, Web3j web3j) throws ExecutionException, InterruptedException, IOException {
        Function function = new Function("transfer",
                Arrays.asList(new Address(to), new Uint256(amount)),
                List.of(new TypeReference<Address>() {
                }));

        //获取交易笔数
        BigInteger nonce = EthTool.getNonce(credentials.getAddress(), web3j);

        //手续费
        BigInteger gasPrice = EthTool.getGesPrice(web3j);

        //注意手续费的设置，这块很容易遇到问题
        BigInteger gasLimit = BigInteger.valueOf(600000L);

        long chainId = getChainId(web3j);


        String rawData = FunctionEncoder.encode(function);

        TransactionManager manager = new RawTransactionManager(web3j, credentials);
        EthSendTransaction transaction = manager.sendEIP1559Transaction(chainId, maxPriorityFeePerGas, maxFeePerGas, gasLimit, contract, rawData, amount);

        //发起交易
        return transaction;
    }

    private static EthCall query(Function function, String address, String contract, Web3j web3j) throws InterruptedException, ExecutionException {
        String rawData = FunctionEncoder.encode(function);
        return call(address, contract, rawData, web3j);
    }

    private static EthCall call(String address, String contract, String encode, Web3j web3j) throws InterruptedException, ExecutionException {
        Transaction transaction = Transaction.createEthCallTransaction(address, contract, encode);
        return web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
    }

    public static EthCall balanceOf2(String address, String contract, Web3j web3j) throws IOException {
        Transaction transaction = Transaction.createEthCallTransaction(address, contract, prepareData(address));
        return web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
    }

    private static final String DATA_PREFIX = "0x70a08231000000000000000000000000";
    public static final String Ox00 = "0x0000000000000000000000000000000000000000";

    /**
     * data数据格式：最前边的“0x70a08231000000000000000000000000”是固定的，后边的是钱包地址（不带“0x”前缀）
     */
    @NotNull
    private static String prepareData(String address) {

        return DATA_PREFIX + address.substring(2);
    }

    /**
     * 以太坊返回报文转十进制
     *
     * @param value 以太坊返回报文
     * @return
     */
    private static String hex2dec(String value) {
        return new BigInteger(value.substring(2), 16).toString();
    }


    public static final String USDT_CONTRACT_MAINNET = "0xdac17f958d2ee523a2206206994597c13d831ec7";
    public static final String LINK_CONTRACT_RINKEBY = "0x01BE23585060835E02B77ef475b0Cc51aA1e0709";

    public static void main(String[] args) {
        String address = "0x4a7D597365982a941F00e11638994c17385122C9";
        String ox0 = "0x0000000000000000000000000000000000000000";
        String mn = "first ceiling cupboard fresh wood situate dilemma primary october mobile family coast";

        String to = "0x3B3B43614C7E8e362A1f680d0696E054D9d850Db";

        Function function = new Function(
                "totalSupply",
                new ArrayList<>(),  // Solidity Types in smart contract functions
                new ArrayList<>());

        Web3j web3j = Web3j.build(new HttpService(Infura.INFURA_RINKEBY_URL));

        EthAccount ethAccount = MnemonicTool.loadEthDefaultWallet(mn);


        BigInteger amount = BigDecimal.valueOf(2).multiply(DecimalTool.tenPow(18)).toBigInteger();


    }


}
