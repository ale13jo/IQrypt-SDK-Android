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

class BitmapEncryptor implements IEncryptor{

    DetEncryptor detEnc;
    public BitmapEncryptor(DetEncryptor detEnc)
    {
        this.detEnc=detEnc;
    }

    @Override
    public Object decrypt(String toDecrypt, Class t)
    {
        return detEnc.decrypt(toDecrypt, t);
    }

    @Override
    public byte[] decryptBytes(byte[] encBytes) {
        throw new RuntimeException("BITMAP does not support this method ");
    }

    @Override
    public byte[] encryptBytes(byte[] bytes) {
        throw new RuntimeException("BITMAP does not support this method ");
    }

    @Override
    public String encrypt(Object toEncrypt)
    {
        return detEnc.encrypt(toEncrypt);
    }

    @Override
    public String encrypt(Object toEncrypt, boolean encodingBase16)
    {
        throw new RuntimeException("BITMAP does not support this method ");

    }
    @Override
    public Object decrypt(String toDecrypt, Class t, boolean encodingBase16)
    {
        throw new RuntimeException("BITMAP does not support this method ");
    }

}
