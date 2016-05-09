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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

 class CryptoUtil {

    public static byte[] buildKey(String encryptionKey, int keyLength)
    {
        byte[] key = encryptionKey.getBytes(Charset.forName("UTF-8"));
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(key);
        byte[] hash = md.digest();
        if(keyLength==32)
            return hash;
        byte[] aesKey = new byte[keyLength];
        System.arraycopy( hash,0, aesKey,0, keyLength);
        return aesKey;
    }
	  
	  public static byte[] ensureLength(byte[] bytesIn,int blockSize)
      {
          int nrBytes = paddingSize(bytesIn.length,blockSize);
          if (nrBytes != bytesIn.length)
          {
        	  bytesIn = Arrays.copyOf(bytesIn, nrBytes);
          }
          return bytesIn;
      }
      private static int paddingSize(int length,int block_size)
      {
          if (length % block_size == 0)
              return length;
          else
              return length + (block_size - (length % block_size));
      }
}
