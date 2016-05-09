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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


class HmacEncryptor implements IEncryptor {

    private Mac _hmacsha256;
    IDocumentSerializer serialzer;
   public HmacEncryptor()
   {
       serialzer = new CryptoJsonSerializer();
       try {
           _hmacsha256 = Mac.getInstance("HmacSHA256");
           SecretKeySpec secret_key = new SecretKeySpec(CryptoUtil.buildKey(IQryptConfigurator.encKey, 32), "HmacSHA256");
           _hmacsha256.init(secret_key);

       } catch (NoSuchAlgorithmException |InvalidKeyException e) {
           e.printStackTrace();
       }
   }
    @Override
    public String encrypt(Object obj) {
        return this.encrypt(obj, false);
    }

    @Override
    public Object decrypt(String encryptedStr, Class t) {
        throw new RuntimeException("You cannot decrypt with HMAC");
    }

    @Override
    public byte[] decryptBytes(byte[] encBytes) {
        throw new RuntimeException("You cannot decrypt with HMAC");
    }

    @Override
    public byte[] encryptBytes(byte[] bytes) {
        return _hmacsha256.doFinal(bytes);
    }

    @Override
    public Object decrypt(String toDecrypt, Class t, boolean encodingBase16) {
        throw new RuntimeException("You cannot decrypt with HMAC");
    }

    @Override
    public String encrypt(Object toEncrypt, boolean encodingBase16) {
        byte[] bytes = serialzer.serialize(toEncrypt);
        if(bytes.length==0)
        {
            throw new IllegalArgumentException("evalue is empty.") ;
        }
        byte[] encBytes=_hmacsha256.doFinal(bytes);
        if (encodingBase16)
            return Base16.encode(encBytes).trim();
        else
            return Base64.encodeToString(encBytes, 0).trim();
    }
}
