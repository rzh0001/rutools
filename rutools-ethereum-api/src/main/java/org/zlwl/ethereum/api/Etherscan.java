package org.zlwl.ethereum.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.google.gson.Gson;
import lombok.Data;

import java.util.List;

/**
 * 区块浏览器API
 * (国内网络需使用代理）
 *
 * @author ruanzh.eth
 */
@Data
public class Etherscan {
    public static final String URL_MAINNET = "https://api.etherscan.io/";
    public static final String URL_GOERLI = "https://api-goerli.etherscan.io/";
    public static final String URL_KOVAN = "https://api-kovan.etherscan.io/";
    public static final String URL_RINKEBY = "https://api-rinkeby.etherscan.io/";
    public static final String URL_ROPSTEN = "https://api-ropsten.etherscan.io/";

    private static final String default_api_key = "TXYWZWJHZGECS7U4TJCN1CPAXTXKSZFR74";

    private static final String STATUS_OK = "1";
    private static final String MESSAGE_NOT_FOUND = "No transactions found";

    private String baseUrl;
    private String apiKey;

    Log log = LogFactory.get();


    public Etherscan(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl == null ? URL_MAINNET : baseUrl;
        this.apiKey = apiKey == null ? default_api_key : apiKey;
    }


    /**
     * 获取代理转账数据
     *
     * @param address 以太坊地址
     * @return
     */
    public List<ERC20TokenTxData> getERC20Transfers(String address) {
        String url = "api?module=account&action=tokentx&address={}&page=1&offset=10000&sort=asc";
        url = StrUtil.format(url, address);
        String response = httpGet(url);
        ERC20TokenTxDTO data = new Gson().fromJson(response, ERC20TokenTxDTO.class);
        if (!data.getStatus().equals(STATUS_OK) && !data.getMessage().equals(MESSAGE_NOT_FOUND)) {
            log.error(new IllegalStateException(data.getMessage()));
        }
        return data.getResult();
    }

    /**
     * 获取NFT转账数据
     *
     * @param address 以太坊地址
     * @return
     */
    public List<ERC721TokenTxData> getERC721Transfers(String address) {
        String url = "api?module=account&action=tokennfttx&address={}&page=1&offset=10000&sort=asc";
        url = StrUtil.format(url, address);
        String response = httpGet(url);
        ERC721TokenTxDTO data = new Gson().fromJson(response, ERC721TokenTxDTO.class);
        if (!data.getStatus().equals(STATUS_OK) && !data.getMessage().equals(MESSAGE_NOT_FOUND)) {
            log.error(new IllegalStateException(data.getMessage()));
        }
        return data.getResult();
    }

    public String httpGet(String url) {
        url = StrUtil.format("{}{}&apikey={}", this.baseUrl, url, this.apiKey);
        log.info("url={}", url);
        return HttpUtil.get(url);
    }

    public static void main(String[] args) {

        System.setProperty("https.proxySet", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7890");


        Etherscan etherscan = new Etherscan(URL_RINKEBY, null);

        List<ERC20TokenTxData> erc20Transfers = etherscan.getERC20Transfers("0x4a7D597365982a941F00e11638994c17385122C9");
        List<ERC721TokenTxData> erc721Transfers = etherscan.getERC721Transfers("0x4a7D597365982a941F00e11638994c17385122C9");

    }

}
