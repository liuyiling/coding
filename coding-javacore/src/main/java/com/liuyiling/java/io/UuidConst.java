package com.liuyiling.java.io;

/**
 * Created by liuyl on 2016/10/25.
 */
public class UuidConst {
    public static final int BIT_SEQUENCE_LENGTH = 10; //10位毫秒级自增id
    public static final int BIT_BIZ_LENGTH = 6 + BIT_SEQUENCE_LENGTH; //6位业务信息
    public static final int BIT_IDC_LENGTH = 6 + BIT_BIZ_LENGTH; //6位IDC信息
    public static final int BIT_TIME_LENGTH = 41 + BIT_IDC_LENGTH; //41位时间信息

    public static final long SEQ_LIMIT = 1 << BIT_SEQUENCE_LENGTH;
}
