package org.zlwl.wallet;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * @author ruanzh.eth
 */
public class Infura {

    public static final String INFURA_MAINNET_URL = "https://mainnet.infura.io/v3/4d30f4bbd2dd43938b80d8418607c2e6";
    public static final String INFURA_RINKEBY_URL = "https://rinkeby.infura.io/v3/62eee2c2ab8843fd96a5ff613ef3b9a5";

    public static Web3j mainnet = Web3j.build(new HttpService(INFURA_MAINNET_URL));
    public static Web3j rinkeby = Web3j.build(new HttpService(INFURA_RINKEBY_URL));
}
