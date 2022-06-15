package org.zlwl.trident.abi.datatypes.generated;

import org.zlwl.trident.abi.datatypes.Bytes;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.zlwl.trident.codegen.AbiTypesGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Bytes4 extends Bytes {
    /**
     * 默认
     */
    public static final Bytes4 DEFAULT = new Bytes4(new byte[4]);

    /**
     * bytes4
     *
     * @param value 价值
     */
    public Bytes4(byte[] value) {
        super(4, value);
    }
}
