package org.zlwl.wallet;

import org.web3j.protocol.Web3j;
import org.zlwl.util.DecimalTool;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

/**
 * @author ruanzh.eth
 */
public class ERC20Token {
    private Web3j client;
    private String address;
    private String name;
    private String symbol;
    private Integer decimals;
    private BigDecimal totalSupply;

    public String getAddress() {
        return this.address;
    }

    public ERC20Token(String address, Web3j web3j) {
        this.address = address;
        this.client = web3j;
    }

    public BigDecimal getTotalSupply() throws ExecutionException, InterruptedException {
        if (this.totalSupply == null) {
            String totalSupply = ERC20TokenTool.totalSupply(this.address, this.client);
            Integer decimals = getDecimals();
            this.totalSupply = new BigDecimal(totalSupply).divide(DecimalTool.tenPow(decimals));
        }

        return this.totalSupply;
    }

    public Integer getDecimals() throws ExecutionException, InterruptedException {
        if (this.decimals == null) {
            String decimals = ERC20TokenTool.decimals(this.address, this.client);
            this.decimals = Integer.valueOf(decimals);
        }
        return this.decimals;
    }

}
