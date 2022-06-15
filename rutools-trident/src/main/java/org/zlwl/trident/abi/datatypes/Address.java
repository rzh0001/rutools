/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.zlwl.trident.abi.datatypes;

import java.math.BigInteger;
import java.util.Arrays;

import org.zlwl.trident.utils.Base58Check;
import org.zlwl.trident.utils.Numeric;

/**
 * Address type, which by default is equivalent to uint160 which follows the Ethereum specification.
 */
public class Address implements Type<String> {
    /**
     * 类型名称
     */
    public static final String TYPE_NAME = "address";
    /**
     * 默认长度
     */
    public static final int DEFAULT_LENGTH = 160;
    /**
     * 默认
     */
    public static final Address DEFAULT = new Address(BigInteger.ZERO);

    private final Uint value;

    /**
     * 地址
     *
     * @param value 价值
     */
    public Address(Uint value) {
        this.value = value;
    }

    /**
     * 地址
     *
     * @param value 价值
     */
    public Address(BigInteger value) {
        this(DEFAULT_LENGTH, value);
    }

    /**
     * 地址
     *
     * @param bitSize 位大小
     * @param value   价值
     */
    public Address(int bitSize, BigInteger value) {
        this(new Uint(bitSize, value));
    }

    /**
     * 地址
     *
     * @param value 价值
     */
    public Address(String value) {
        if (value.startsWith("T")) {
            byte[] rawValue = Base58Check.base58ToBytes(value);
            this.value = new Uint(DEFAULT_LENGTH, Numeric.toBigInt(Arrays.copyOfRange(rawValue, 1, 21)));
        } else if (value.startsWith("41")) {
            this.value = new Uint(DEFAULT_LENGTH, Numeric.toBigInt(value.substring(2)));
        } else {
            // ETH compatible
            this.value = new Uint(DEFAULT_LENGTH, Numeric.toBigInt(value));
        }
    }

    /**
     * 地址
     *
     * @param bitSize  位大小
     * @param hexValue 十六进制值
     */
    public Address(int bitSize, String hexValue) {
        this(bitSize, Numeric.toBigInt(hexValue));
    }

    /**
     * 转换使用uint
     *
     * @return {@link Uint}
     */
    public Uint toUint() {
        return value;
    }

    @Override
    public String getTypeAsString() {
        return TYPE_NAME;
    }

    @Override
    public String toString() {
        byte[] rawAddr = Numeric.toBytesPadded(value.getValue(), 21);
        rawAddr[0] = 0x41;
        return Base58Check.bytesToBase58(rawAddr);
    }

    @Override
    public String getValue() {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;

        return value != null ? value.value.equals(address.value.value) : address.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
