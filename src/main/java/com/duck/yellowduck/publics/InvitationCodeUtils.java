package com.duck.yellowduck.publics;

import java.io.PrintStream;

public class InvitationCodeUtils
{
    static String[] digit = { "E", "5", "F", "C", "D", "G", "3", "H", "Q", "A", "4", "B", "1", "N", "O", "P", "I", "J", "2", "R", "S", "T", "U", "V", "6", "7", "M", "W", "X", "8", "9", "K", "L", "Y", "Z" };

    public static String getInvitaionCode(int userId)
    {
        int num = userId;
        String code = "";
        while (num > 0)
        {
            int mod = num % 35;
            num = (num - mod) / 35;
            code = digit[mod] + code;
        }
        while (code.length() < 4) {
            code = add(code);
        }
        return code;
    }

    private static String add(String str)
    {
        if (str.length() < 4) {
            str = "0" + str;
        }
        return str;
    }

    public static void main(String[] avgs)
    {
        System.out.println(getInvitaionCode(777777777));
        System.out.println(getInvitaionCode(2));
        System.out.println(getInvitaionCode(3));
        System.out.println(getInvitaionCode(7));
    }
}
