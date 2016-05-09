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
import java.util.Calendar;

public class EncryptorFactory
{
    static OpeEncryptor ope;
    static RopeEncryptor rope;
    static DetEncryptor det;
    static RndEncryptor rnd;
    static BitmapEncryptor bit;
    static HmacEncryptor hmac;
    public static IEncryptor getEncryptor(EncryptionType encType)
    {

        if (encType == EncryptionType.OPE)
        {
            if (ope == null)
            {
                ope = new OpeEncryptor();
            }
            return ope;
        }
        else if (encType == EncryptionType.ROPE)
        {
            if (rope == null)
            {
                rope = new RopeEncryptor();
            }
            return rope;
        }
        else if (encType == EncryptionType.DET)
        {
            if (det == null)
            {
                det = new DetEncryptor();
            }
            return det;
        }
        else if (encType == EncryptionType.BITMAP)
        {
            if (bit == null)
            {
                bit = new BitmapEncryptor(new DetEncryptor());
            }
            return bit;
        }
        else if (encType == EncryptionType.HMAC)
        {
            if (hmac == null)
            {
                hmac = new HmacEncryptor();
            }
            return hmac;
        }
        else
        {
            if (rnd == null)
            {
                rnd = new RndEncryptor();
            }
            return rnd;
        }
    }
    public static void reset() {

        ope = null;
        rnd = null;
        det = null;
        bit = null;
        rope = null;
        hmac = null;


    }


}

