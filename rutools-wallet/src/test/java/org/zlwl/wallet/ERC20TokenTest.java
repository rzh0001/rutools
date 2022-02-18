package org.zlwl.wallet;

import org.junit.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ruanzh.eth
 */
class ERC20TokenTest {
    public static final String LINK_CONTRACT_RINKEBY = "0x01BE23585060835E02B77ef475b0Cc51aA1e0709";

    private ERC20Token LINK = new ERC20Token(LINK_CONTRACT_RINKEBY, Infura.rinkeby);

    @org.junit.jupiter.api.Test
    void getTotalSupply() throws ExecutionException, InterruptedException {
        assertEquals(0, BigDecimal.valueOf(1000000000).compareTo(LINK.getTotalSupply()));
    }
}