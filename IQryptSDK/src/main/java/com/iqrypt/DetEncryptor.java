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

class DetEncryptor implements IEncryptor{
    CBCCipher cipher;
    final int BLOCK_SIZE;
    IDocumentSerializer serialzer;
    private Mac _hmacsha256;
    public DetEncryptor()
    {
    	serialzer = new CryptoJsonSerializer();
        IBlockCipher blockCipher = null;
        if (IQryptConfigurator.cipherType == Cipher.AES128)
        {
            blockCipher = new AES256Encryptor();
            blockCipher.setKey(CryptoUtil.buildKey(IQryptConfigurator.encKey, 16));


        }
        else if (IQryptConfigurator.cipherType == Cipher.AES256)
        {
            blockCipher = new AES256Encryptor();
            blockCipher.setKey(CryptoUtil.buildKey(IQryptConfigurator.encKey, 32));

        }
        else if (IQryptConfigurator.cipherType == Cipher.Camellia128)
        {
            blockCipher = new CamelliaEngine();
            blockCipher.setKey(CryptoUtil.buildKey(IQryptConfigurator.encKey, 16));

        }
        else if (IQryptConfigurator.cipherType == Cipher.Camellia256)
        {
            blockCipher = new CamelliaEngine();
            blockCipher.setKey(CryptoUtil.buildKey(IQryptConfigurator.encKey, 32));

        }
        cipher = new CBCCipher(blockCipher);
        BLOCK_SIZE = blockCipher.getBlockSize() / 8;
        try {
            _hmacsha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(CryptoUtil.buildKey(IQryptConfigurator.encKey, 32), "HmacSHA256");
            _hmacsha256.init(secret_key);

        } catch (NoSuchAlgorithmException |InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object decrypt(String toDecrypt, Class t)
    {
        return decrypt(toDecrypt, t, false);
    }

    @Override
    public byte[] decryptBytes(byte[] encBytes) {
        throw new RuntimeException("DET does not support this method ");
    }

    @Override
    public byte[] encryptBytes(byte[] bytes) {
        throw new RuntimeException("DET does not support this method ");
    }

    @Override
    public String encrypt(Object toEncrypt)
    {
        return encrypt(toEncrypt, false);
    }

    @Override
    public String encrypt(Object toEncrypt, boolean encodingBase16)
    {
        byte[] bytes = serialzer.serialize(toEncrypt);
        if(bytes.length==0)
        {
            throw new IllegalArgumentException("evalue is empty.") ;
        }
        bytes=CryptoUtil.ensureLength(bytes, BLOCK_SIZE);
        byte[] hash=_hmacsha256.doFinal(bytes);
        byte[] IV = new byte[BLOCK_SIZE];
        System.arraycopy( hash,0, IV,0, BLOCK_SIZE);
        byte[] encBytes = cipher.encrypt(bytes,IV);
        if (encodingBase16)
            return Base16.encode(encBytes).trim();
        else
            return Base64.encodeToString(encBytes, 0).trim();

    }
    @Override
    public Object decrypt(String toDecrypt, Class t, boolean encodingBase16)
    {
        if(toDecrypt==null || toDecrypt.isEmpty())
        {
            throw new IllegalArgumentException("dvalue is empty.") ;
        }
        byte[] bytes = null;
        if (encodingBase16)
            bytes = Base16.decode(toDecrypt);
        else
            bytes = Base64.decode(toDecrypt,0);

        byte[] decBytes= cipher.decrypt(bytes);

        Object value = serialzer.deserialize(t, decBytes);
        return value;
    }

}
