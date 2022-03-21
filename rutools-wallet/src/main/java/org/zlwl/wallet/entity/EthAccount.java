package org.zlwl.wallet.entity;

import lombok.Data;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

/**
 * 以太坊账户
 *
 * @author ruanzh.eth
 */
@Data
public class EthAccount {
    private String address;
    private ECKeyPair keyPair;

    public EthAccount(ECKeyPair keyPair) {
        this.keyPair = keyPair;
        this.address = Keys.getAddress(keyPair);
    }

    public String getPrivateKey() {
        return keyPair.getPrivateKey().toString(16);
    }

    public String getPublicKey() {
        return keyPair.getPublicKey().toString(16);
    }

    public String get0xAddress() {
        return "0x" + this.address;
    }

    public Credentials getCredentials() {
        return Credentials.create(this.keyPair);
    }
}
