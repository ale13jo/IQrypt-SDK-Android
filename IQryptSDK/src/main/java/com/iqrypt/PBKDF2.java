/*  IQrypt - encrypt and query your database

    Copyright(C) 2016 Dotissi Development SRL

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.If not, see http://www.gnu.org/licenses/gpl.txt
    */
package com.iqrypt;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


//http://www.ietf.org/rfc/rfc2898.txt
class PBKDF2
{

    private Mac _hmacsha256;
    private  int hLen;
    private  byte[] P;
    private  byte[] S;
    private  int c;
    private int dkLen;
    public PBKDF2(byte[] password, byte[] salt, int iterations)
    {
        try {
            _hmacsha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(password, "HmacSHA256");
            _hmacsha256.init(secret_key);

        } catch (NoSuchAlgorithmException|InvalidKeyException e) {
            e.printStackTrace();
        }
        hLen = _hmacsha256.getMacLength() / 8;

        P = password;
        S = salt;
        c = iterations;
    }
    public byte[] getDerivedBytes(int keyLength)
    {
        dkLen = keyLength;

        double l = Math.ceil((double) dkLen / hLen);

        byte[] finalBlock = new byte[0];

        for (int i = 1; i <= l; i++)
        {
            finalBlock = mergeArrays(finalBlock, F(P, S, c, i));
        }

        byte[] ret = new byte[keyLength];
        System.arraycopy(finalBlock, 0, ret, 0, keyLength);
        return ret;

    }

    public static boolean verifyHashedPassword(String hashedPassword, String password)
    {
        if (hashedPassword == null)
        {
            return false;
        }
        if (password == null)
        {
            throw new IllegalArgumentException("password");
        }
        byte[] src = Base64.decode(hashedPassword, 0);
        byte[] salt = new byte[src[0]];
        System.arraycopy(src, 1, salt, 0, salt.length);
        byte[] pwdBytes = password.getBytes(Charset.forName("UTF-8"));
        PBKDF2 pbkdf = new PBKDF2(pwdBytes, salt, 3000);
        byte[] bytes = pbkdf.getDerivedBytes(32);
        byte[] dst = new byte[salt.length + 1 + bytes.length];
        dst[0] = (byte)salt.length;
        System.arraycopy(salt, 0, dst, 1, salt.length);
        System.arraycopy(bytes, 0, dst, salt.length + 1, bytes.length);
        return Base64.encodeToString(dst,0).trim().equals( hashedPassword);

    }
    private byte[] F(byte[] P, byte[] S, int c, int i)
    {

        byte[] Si = mergeArrays(S, INT(i));
        byte[] temp = PRF(Si);
        byte[] U_c = temp;

        for (int C = 1; C < c; C++)
        {
            temp = PRF(temp);
            for (int j = 0; j < temp.length; j++)
            {
                U_c[j] ^= temp[j];
            }
        }
        return U_c;
    }
    private byte[] INT(int i)
    {
        byte[] dest =new byte[4];
        dest[ 0] = (byte) (i / (256 * 256 * 256));
        dest[ 1] = (byte) (i / (256 * 256));
        dest[ 2] = (byte) (i / (256));
        dest[3] = (byte) (i);

        return dest;
    }
    private byte[] PRF(byte[] S)
    {
        return _hmacsha256.doFinal(S);
    }

    private byte[] mergeArrays(byte[] a, byte[] b)
    {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}