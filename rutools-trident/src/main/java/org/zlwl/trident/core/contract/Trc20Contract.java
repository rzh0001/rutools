package org.zlwl.trident.core.contract;

import org.zlwl.trident.abi.datatypes.Address;
import org.zlwl.trident.abi.datatypes.Bool;
import org.zlwl.trident.abi.datatypes.Function;
import org.zlwl.trident.abi.datatypes.generated.Uint8;
import org.zlwl.trident.abi.datatypes.generated.Uint256;
import org.zlwl.trident.abi.datatypes.Utf8String;
import org.zlwl.trident.abi.FunctionReturnDecoder;
import org.zlwl.trident.abi.TypeReference;
import org.zlwl.trident.core.ApiWrapper;
import org.zlwl.trident.core.transaction.TransactionBuilder;
import org.zlwl.trident.proto.Chain.Transaction;
import org.zlwl.trident.proto.Response.TransactionExtention;
import org.zlwl.trident.proto.Response.TransactionReturn;
import org.zlwl.trident.utils.Base58Check;
import org.zlwl.trident.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * The {@code Trc20Contract} is a wrapper class of a standard TRC-20 smart contract.
 * 
 * <p>A {@code Trc20Contract} object includes standard TRC-20 functions defined 
 * in TIP-20. Each {@code Trc20Contract} binds a {@link ApiWrapper} with specific 
 * caller's private key and address.</p>
 * 
 * @since jdk 1.8.0_231
 * @see org.zlwl.trident.core.ApiWrapper
 * @see org.zlwl.trident.abi.datatypes.Function
 */
public class Trc20Contract extends Contract {

    public Trc20Contract(Contract cntr, String ownerAddr, ApiWrapper wrapper) {
        super(cntr, ownerAddr, wrapper);
    }

    /**
      * Call function name() public view returns (string).
      *
      * Returns the name of the token - e.g. "MyToken".
      *
      * @return the name of the token
      */
      public String name() {
        Function name = new Function("name",
                Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {}));

        TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()), 
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), name);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (String)FunctionReturnDecoder.decode(result, name.getOutputParameters()).get(0).getValue();
     }

     /**
      * Call function symbol() public view returns (string).
      *
      * Returns the symbol of the token. E.g. "HIX".
      *
      * @return the symbol of the token
      */
      public String symbol() {
        Function symbol = new Function("symbol",
                Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {}));

        TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()), 
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), symbol);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (String)FunctionReturnDecoder.decode(result, symbol.getOutputParameters()).get(0).getValue();
      }

      /**
       * Call function decimals() public view returns (uint8).
       * 
       * Returns the number of decimals the token uses - e.g. 8, 
       * means to divide the token amount by 100000000 to get its user representation
       * 
       * @return the number of decimals the token uses
       */
      public BigInteger decimals() {
        Function decimals = new Function("decimals",
                Collections.emptyList(), Arrays.asList(new TypeReference<Uint8>() {}));
        
        TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()), 
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), decimals);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, decimals.getOutputParameters()).get(0).getValue();
      }

      /**
       * Call function totalSupply() public view returns (uint256).
       * 
       * Returns the total token supply.
       * 
       * @return the total token supply
       */
      public BigInteger totalSupply() {
        Function totalSupply = new Function("totalSupply",
                Collections.emptyList(), Arrays.asList(new TypeReference<Uint256>() {}));

        TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()), 
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), totalSupply);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, totalSupply.getOutputParameters()).get(0).getValue();
      }

      /**
       * Call function balanceOf(address _owner) public view returns (uint256 balance).
       * 
       * Returns the account balance of another account with address _owner.
       * 
       * @param accountAddr The token owner's address
       * @return the account balance of another account with address _owner
       */
      public BigInteger balanceOf(String accountAddr) {
        Function balanceOf = new Function("balanceOf",
                Arrays.asList(new Address(accountAddr)), Arrays.asList(new TypeReference<Uint256>() {}));

        TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()), 
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), balanceOf);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, balanceOf.getOutputParameters()).get(0).getValue();
      }

      /**
       * Call function transfer(address _to, uint256 _value) public returns (bool success).
       * 
       * Transfers _value amount of tokens to address _to.
       * 
       * @param destAddr The address to receive the token
       * @param amount The transfer amount
       * @param power The power number of 10 that the transfer amount multiplied by
       * @param memo The transaction memo
       * @param feeLimit The energy fee limit
       * @return Transaction hash
       */
      public String transfer(String destAddr, long amount, int power,
             String memo, long feeLimit) {
        Function transfer = new Function("transfer",
                Arrays.asList(new Address(destAddr),
                        new Uint256(BigInteger.valueOf(amount).multiply(BigInteger.valueOf(10).pow(power)))),
                Arrays.asList(new TypeReference<Bool>() {}));

        TransactionBuilder builder = wrapper.triggerCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()), 
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), transfer);
        builder.setFeeLimit(feeLimit);
        builder.setMemo(memo);

        Transaction signedTxn = wrapper.signTransaction(builder.build());
        return wrapper.broadcastTransaction(signedTxn);
      }

      /**
       * call function transferFrom(address _from, address _to, uint256 _value) public returns (bool success)
       * 
       * The transferFrom method is used for a withdraw workflow, 
       * allowing contracts to transfer tokens on your behalf. This can only be called
       * when someone has allowed you some amount.
       * 
       * @param fromAddr The address who sends tokens (or the address to withdraw from)
       * @param destAddr The address to receive the token
       * @param amount The transfer amount
       * @param power The power number of 10 that the transfer amount multiplied by
       * @param memo The transaction memo
       * @param feeLimit The energy fee limit
       * @return Transaction hash
       */
      public String transferFrom(String fromAddr, String destAddr, long amount, int power,
             String memo, long feeLimit) {
        Function transferFrom = new Function("transferFrom",
                Arrays.asList(new Address(fromAddr) ,new Address(destAddr),
                        new Uint256(BigInteger.valueOf(amount).multiply(BigInteger.valueOf(10).pow(power)))),
                Arrays.asList(new TypeReference<Bool>() {}));

        TransactionBuilder builder = wrapper.triggerCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()), 
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), transferFrom);
        builder.setFeeLimit(feeLimit);
        builder.setMemo(memo);

        Transaction signedTxn = wrapper.signTransaction(builder.build());
        return wrapper.broadcastTransaction(signedTxn);
      }

      /**
       * Call function approve(address _spender, uint256 _value) public returns (bool success)
       * 
       * Allows _spender to withdraw from your account multiple times, up to the _value amount. 
       * If this function is called again it overwrites the current allowance with _value.
       * 
       * @param spender The address who is allowed to withdraw.
       * @param amount The amount allowed to withdraw.
       * @param power The power number of 10 that the transfer amount multiplied by
       * @param memo The transaction memo
       * @param feeLimit The energy fee limit
       * @return Transaction hash
       */
      public String approve(String spender ,long amount, int power,
             String memo, long feeLimit) {
        Function approve = new Function("approve",
                Arrays.asList(new Address(spender) ,
                        new Uint256(BigInteger.valueOf(amount).multiply(BigInteger.valueOf(10).pow(power)))),
                Arrays.asList(new TypeReference<Bool>() {}));

                TransactionBuilder builder = wrapper.triggerCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()), 
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), approve);
        builder.setFeeLimit(feeLimit);
        builder.setMemo(memo);

        Transaction signedTxn = wrapper.signTransaction(builder.build());
        return wrapper.broadcastTransaction(signedTxn);
      }

      /**
       * Call function allowance(address _owner, address _spender) public view returns (uint256 remaining).
       * 
       * Returns the amount which _spender is still allowed to withdraw from _owner.
       * 
       * @param owner The address to be withdrew from.
       * @param spender The address of the withdrawer.
       * @return the amount which _spender is still allowed to withdraw from _owner
       */
      public BigInteger allowance(String owner, String spender) {
        Function allowance = new Function("allowance",
                Arrays.asList(new Address(owner), new Address(spender)),
                Arrays.asList(new TypeReference<Uint256>() {}));
        
        TransactionExtention txnExt = wrapper.constantCall(Base58Check.bytesToBase58(ownerAddr.toByteArray()), 
                Base58Check.bytesToBase58(cntrAddr.toByteArray()), allowance);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        return (BigInteger)FunctionReturnDecoder.decode(result, allowance.getOutputParameters()).get(0).getValue();
      }

}