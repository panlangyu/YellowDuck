package com.duck.yellowduck.publics;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.web3j.crypto.Hash.sha256;

/**
 * 助记词工具类
 */
public class MnemonitUtitls {
    /**
     * 随机生成函数
     */
    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();

    /**
     * 2048助记词
     */
    private static List<String> WORD_LIST = null;

    /**
     * 生成注记词
     */
    public static String generateMnemonic() throws IOException {
        //1 随机生成128位字节数组
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);

        //查找随机词库 词库大小位2048
        if (WORD_LIST == null) {
            WORD_LIST = populateWordList();
        }

        //校验熵的位数是否在128-256位之间，并且是32的倍数
        validateInitialEntropy(initialEntropy);


        //2确定校验码长度 128/32 4位校验码
        int ent = initialEntropy.length * 8;
        int checksumLength = ent / 32;

        //计算校验码
        byte checksum = calculateChecksum(initialEntropy);

        //计算出数值
        boolean[] bits = convertToBits(initialEntropy, checksum);


        int iterations = (ent + checksumLength) / 11;
        StringBuilder mnemonicBuilder = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            int index = toInt(nextElevenBits(bits, i));
            mnemonicBuilder.append(WORD_LIST.get(index));

            boolean notLastIteration = i < iterations - 1;
            if (notLastIteration) {
                mnemonicBuilder.append(" ");
            }
        }

        return mnemonicBuilder.toString();
    }

    private static boolean[] nextElevenBits(boolean[] bits, int i) {
        int from = i * 11;
        int to = from + 11;
        return Arrays.copyOfRange(bits, from, to);
    }


    private static int toInt(boolean[] bits) {
        int value = 0;
        for (int i = 0; i < bits.length; i++) {
            boolean isSet = bits[i];
            if (isSet) {
                value += 1 << bits.length - i - 1;
            }
        }

        return value;
    }

    private static boolean[] convertToBits(byte[] initialEntropy, byte checksum) {
        int ent = initialEntropy.length * 8;
        int checksumLength = ent / 32;
        int totalLength = ent + checksumLength;
        boolean[] bits = new boolean[totalLength];

        for (int i = 0; i < initialEntropy.length; i++) {
            for (int j = 0; j < 8; j++) {
                byte b = initialEntropy[i];
                bits[8 * i + j] = toBit(b, j);
            }
        }

        for (int i = 0; i < checksumLength; i++) {
            bits[ent + i] = toBit(checksum, i);
        }

        return bits;
    }

    private static boolean toBit(byte value, int index) {
        return ((value >>> (7 - index)) & 1) > 0;
    }


    private static byte calculateChecksum(byte[] initialEntropy) {
        int ent = initialEntropy.length * 8;
        byte mask = (byte) (0xff << 8 - ent / 32);
        byte[] bytes = sha256(initialEntropy);
        return (byte) (bytes[0] & mask);
    }

    //校验熵的位数是否在128-256位之间，并且是32的倍数
    private static void validateInitialEntropy(byte[] initialEntropy) {
        if (initialEntropy == null) {
            throw new IllegalArgumentException("Initial entropy is required");
        }

        int ent = initialEntropy.length * 8;
        if (ent < 128 || ent > 256 || ent % 32 != 0) {
            throw new IllegalArgumentException("The allowed size of ENT is 128-256 bits of "
                    + "multiples of 32");
        }
    }




    /**
     * 生成助记词库
     *
     * @return
     */
    public static List<String> populateWordList() throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("english_worlds.txt");
        return readAllLines(inputStream);
    }

    private static List<String> readAllLines(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> data = new ArrayList<>();
        for (String line; (line = br.readLine()) != null; ) {
            data.add(line);
        }
        return data;
    }

    public static String getRandomName() throws IOException {
        if (WORD_LIST == null) {
            WORD_LIST = populateWordList();
        }
        Random random = new Random();
        int i = random.nextInt(2048);
        return WORD_LIST.get(i);
    }
}
