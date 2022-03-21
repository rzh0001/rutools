package org.zlwl.wallet.util;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * @author ruanzh.eth
 */
public class ClientUtil {

    /**
     * web3j
     *
     * @param rpcUrl 节点地址
     * @return {@link Web3j}
     */
    public static Web3j web3j(String rpcUrl) {
        return Web3j.build(new HttpService(rpcUrl));
    }
}
