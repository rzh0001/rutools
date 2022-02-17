package org.zlwl.wallet.util;

import com.google.common.collect.ImmutableList;
import org.bitcoinj.crypto.*;
import org.web3j.crypto.*;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * 助記詞工具類
 *
 * @author ruanzh.eth
 */
public class MnemonicTool {

    private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
            ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
                    ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);

    /**
     * 生成助记词
     */
    public static String generateMnemonic() {
        byte[] initialEntropy = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(initialEntropy);
        return MnemonicUtils.generateMnemonic(initialEntropy);
    }

    public static byte[] toSeed(String mnemonic, String password) {
        List<String> words = Arrays.stream(mnemonic.split(" ")).toList();
        return MnemonicCode.toSeed(words, password);
    }

    /**
     * 从助记词推导出主账户钱包
     *
     * @param mnemonic 助记词
     * @param password 密码
     * @param index    address_index
     * @return 以太坊账户
     */
    public static EthAccount loadEthWalletByMnemonic(String mnemonic, String password, int index) {
        // 生成种子密钥
        byte[] seed = toSeed(mnemonic, password);
        // 生成父密钥
        DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        /**
         * BIP-44预定义的树形层级
         * m / purpose' / coin_type' / account' / change / address_index
         * Eth主账号 默认 m/44'60'/0'/0/0
         */
        // 生成主账号第{index}个钱包密钥
        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
        DeterministicKey deterministicKey = deterministicHierarchy
                .deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(index));

        return new EthAccount(ECKeyPair.create(deterministicKey.getPrivKeyBytes()));
    }

    /**
     * 加载以太坊默认账户
     *
     * @param mnemonic 助记词
     * @return 以太坊账户
     */
    public static EthAccount loadEthDefaultWallet(String mnemonic) {
        return loadEthWalletByMnemonic(mnemonic, "", 0);
    }

    public static void main(String[] args) {
        String mn = "first ceiling cupboard fresh wood situate dilemma primary october mobile family coast";
        String address = "0x4a7D597365982a941F00e11638994c17385122C9";
        String pk = "c3a74bb901a1c57ea13891040a6ab4d4955bc9b2996b84d1901e2d87e72e0223";

        EthAccount ethAccount = loadEthDefaultWallet(mn);
        System.out.println(ethAccount.getAddress());
        System.out.println(ethAccount.getPrivateKey());
        System.out.println(ethAccount.getPublicKey());
        System.out.println(ethAccount.get0xAddress().equals(address.toLowerCase()));
        System.out.println(ethAccount.getPrivateKey().equals(pk.toLowerCase()));

    }
}
