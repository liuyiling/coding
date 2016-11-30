package com.liuyiling.proto;

/**
 * Created by liuyl on 2016/11/17.
 */
public class ProtocolTest {

    public static void main(String[] agrs) {

        AddressBookProtos.Person person = AddressBookProtos.Person.newBuilder()
                .setId(123)
                .setName("aaa")
                .setEmail("aaa")
                .addPhone(
                        AddressBookProtos.Person.PhoneNumber.newBuilder()
                                .setNumber("123")
                                .setType(AddressBookProtos.Person.PhoneType.HOME)
                )
                .build();


    }

}
