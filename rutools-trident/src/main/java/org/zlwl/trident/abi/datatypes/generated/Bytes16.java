package org.zlwl.trident.abi.datatypes.generated;

import org.zlwl.trident.abi.datatypes.Bytes;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.zlwl.trident.codegen.AbiTypesGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Bytes16 extends Bytes {
    /**
     * 默认
     */
    public static final Bytes16 DEFAULT = new Bytes16(new byte[16]);

    /**
     * bytes16
     *
     * @param value 价值
     */
    public Bytes16(byte[] value) {
        super(16, value);
    }
}
