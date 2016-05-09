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


 class RndEncryptor implements IEncryptor{
    CBCCipher cipher;
    IDocumentSerializer serialzer;
    
    public RndEncryptor()
    {
    	serialzer = new CryptoJsonSerializer();
        if (IQryptConfigurator.cipherType == Cipher.AES128)
        {
        	AES256Encryptor blockCipher = new AES256Encryptor();
            blockCipher.setKey(CryptoUtil.buildKey(IQryptConfigurator.encKey, 16));
            cipher =new CBCCipher( blockCipher);

        }
        else if (IQryptConfigurator.cipherType == Cipher.AES256)
        {
        	AES256Encryptor blockCipher = new AES256Encryptor();
            blockCipher.setKey(CryptoUtil.buildKey(IQryptConfigurator.encKey, 32));
            cipher =new CBCCipher( blockCipher);
        }
        else if (IQryptConfigurator.cipherType == Cipher.Camellia128)
        {
        	CamelliaEngine blockCipher = new CamelliaEngine();
            blockCipher.setKey(CryptoUtil.buildKey(IQryptConfigurator.encKey, 16));
            cipher =new CBCCipher( blockCipher);
        }
        else if (IQryptConfigurator.cipherType == Cipher.Camellia256)
        {
        	CamelliaEngine blockCipher = new CamelliaEngine();
            blockCipher.setKey(CryptoUtil.buildKey(IQryptConfigurator.encKey, 32));
            cipher =new CBCCipher( blockCipher);
        }

    }
    @Override
    public Object decrypt(String encryptedStr, Class t)
    {
        if(encryptedStr==null || encryptedStr.isEmpty())
        {
            throw new IllegalArgumentException("dvalue is empty.") ;
        }
        byte[] bytes = Base64.decode(encryptedStr,0);

        byte[] decBytes=cipher.decrypt(bytes);

        Object value = serialzer.deserialize(t, decBytes);
        return value;
    }
    @Override
    public String encrypt(Object toEncrypt)
    {
        byte[] bytes = serialzer.serialize(toEncrypt);
        if(bytes.length==0)
        {
           throw new IllegalArgumentException("evalue is empty.") ;
        }
        bytes=cipher.ensureLength(bytes);
        byte[] encBytes = cipher.encrypt(bytes);
        return Base64.encodeToString(encBytes, 0);
    }
     @Override
    public byte[] decryptBytes(byte[] encBytes)
    {

        byte[] decBytes= cipher.decrypt(encBytes);
        return cipher.removePaddPKS7( decBytes);


    }
     @Override
    public byte[] encryptBytes(byte[] bytes)
    {
        byte[] paddedBytes=cipher.paddPKS7( bytes);
        return cipher.encrypt(paddedBytes);

    }

     @Override
     public Object decrypt(String toDecrypt, Class t, boolean encodingBase16) {
         throw new RuntimeException("RND does not support this method ");
     }

     @Override
     public String encrypt(Object toEncrypt, boolean encodingBase16) {
         throw new RuntimeException("RND does not support this method ");
     }
 }
