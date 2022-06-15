package org.zlwl.trident.abi.datatypes.generated;

import org.zlwl.trident.abi.datatypes.Bytes;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.zlwl.trident.codegen.AbiTypesGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Bytes32 extends Bytes {
    /**
     * 默认
     */
    public static final Bytes32 DEFAULT = new Bytes32(new byte[32]);

    /**
     * bytes32
     *
     * @param value 价值
     */
    public Bytes32(byte[] value) {
        super(32, value);
    }
}
