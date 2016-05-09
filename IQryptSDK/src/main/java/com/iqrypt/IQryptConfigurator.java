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
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class IQryptConfigurator {

	 public static Cipher cipherType;
     public static String encKey;
   
     public static void setEncryptionChiper(Cipher cipher, String encryptionKey)
     {
         encKey = encryptionKey;
         cipherType = cipher;
         //warm-up
         EncryptorFactory.getEncryptor(EncryptionType.DET);
         EncryptorFactory.getEncryptor(EncryptionType.RND);
         EncryptorFactory.getEncryptor(EncryptionType.OPE);
         GensonFactory.Build();
     }
    public static void resetSettings()
    {

        encKey = null;

        cipherType = Cipher.AES128;
        EncryptorFactory.reset();

    }

 }
