package com.liuyiling.guava.string;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liuyl on 16/1/14.
 * guava中String类的使用方法
 */
public class StringUseTest {


    public static void main(String[] agrs) {

        StringUseTest stringUseTest = new StringUseTest();
        System.out.println(stringUseTest.joinerTest1());
        System.out.println(stringUseTest.joinerTest2());
        System.out.println(stringUseTest.joinerTest3());


        System.out.println(stringUseTest.stringSplitter());


        /**
         * CharMatcher可以有以下功能：
         * trim,collapse,remove,retain
         * 获取字符匹配器
         * 其他获取字符匹配器的常见方法包括：
         * 方法	描述
         anyOf(CharSequence)	枚举匹配字符。如CharMatcher.anyOf(“aeiou”)匹配小写英语元音
         is(char)	给定单一字符匹配。
         inRange(char, char)	给定字符范围匹配，如CharMatcher.inRange(‘a’, ‘z’)
         此外，CharMatcher还有negate()、and(CharMatcher)和or(CharMatcher)方法。
         使用字符匹配器
         CharMatcher提供了多种多样的方法操作CharSequence中的特定字符。其中最常用的罗列如下：
         方法	描述
         collapseFrom(CharSequence,   char)	把每组连续的匹配字符替换为特定字符。如WHITESPACE.collapseFrom(string, ‘ ‘)把字符串中的连续空白字符替换为单个空格。
         matchesAllOf(CharSequence)	测试是否字符序列中的所有字符都匹配。
         removeFrom(CharSequence)	从字符序列中移除所有匹配字符。
         retainFrom(CharSequence)	在字符序列中保留匹配字符，移除其他字符。
         trimFrom(CharSequence)	移除字符序列的前导匹配字符和尾部匹配字符。
         replaceFrom(CharSequence,   CharSequence)	用特定字符序列替代匹配字符。
         所有这些方法返回String，除了matchesAllOf返回的是boolean。
         */
        String string = " a b 23 A  ";
        String noControl = CharMatcher.JAVA_ISO_CONTROL.removeFrom(string); //移除control字符
        System.out.println(noControl);
        String theDigits = CharMatcher.DIGIT.retainFrom(string); //只保留数字字符
        System.out.println(theDigits);
        String spaced = CharMatcher.WHITESPACE.trimAndCollapseFrom(string, ' ');
        System.out.println(spaced);
        //去除两端的空格，并把中间的连续空格替换成单个空格
        String noDigits = CharMatcher.JAVA_DIGIT.replaceFrom(string, "*"); //用*号替换所有数字
        System.out.println(noDigits);
        String lowerAndDigit = CharMatcher.JAVA_DIGIT.or(CharMatcher.JAVA_LOWER_CASE).retainFrom(string);
        // 只保留数字和小写字母
        System.out.println(lowerAndDigit);


        /**
         * 字符集替换
         */
        byte[] bytes = string.getBytes(Charsets.UTF_8);
        //try {
        //    bytes = string.getBytes("UTF-8");
        //} catch (UnsupportedEncodingException e) {
        //    throw new AssertionError(e);
        //}


    }

    /**
     * 更优秀的字符连接
     *
     * @return
     */
    public String joinerTest1() {
        Joiner joiner = Joiner.on(";").skipNulls();
        return joiner.join("a", null, "b");
    }

    public String joinerTest2() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        return joiner.join("a", null, "b");
    }

    public String joinerTest3() {
        Joiner joiner = Joiner.on(",").skipNulls();
        return joiner.join(Arrays.asList(1, 5, 7));
    }


    /**
     * 更优秀的字符拆分
     * 拆分器工厂
     * <p>
     * 方法	描述	范例
     * Splitter.on(char)	按单个字符拆分	Splitter.on(‘;’)
     * Splitter.on(CharMatcher)	按字符匹配器拆分	Splitter.on(CharMatcher.BREAKING_WHITESPACE)
     * Splitter.on(String)	按字符串拆分	Splitter.on(“,   “)
     * Splitter.on(Pattern) Splitter.onPattern(String)	按正则表达式拆分	Splitter.onPattern(“\r?\n”)
     * Splitter.fixedLength(int)	按固定长度拆分；最后一段可能比给定长度短，但不会为空。	Splitter.fixedLength(3)
     * 拆分器修饰符
     * <p>
     * 方法	描述
     * omitEmptyStrings()	从结果中自动忽略空字符串
     * trimResults()	移除结果字符串的前导空白和尾部空白
     * trimResults(CharMatcher)	给定匹配器，移除结果字符串的前导匹配字符和尾部匹配字符
     * limit(int)	限制拆分出的字符串数量
     */
    public List stringSplitter() {
        Splitter splitter = Splitter.on(",")
                .trimResults()
                .omitEmptyStrings();

        return Lists.newArrayList(splitter.split("foo,bar,,  qux"));


    }


}
