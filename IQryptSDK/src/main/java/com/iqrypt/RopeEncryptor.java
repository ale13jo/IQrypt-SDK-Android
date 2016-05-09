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


import java.util.Calendar;
import java.util.Date;


class RopeEncryptor implements IEncryptor {
    ArrayEncoder encoder;
    Ope ope;
    public RopeEncryptor()
    {
        encoder = new ArrayEncoder();
        ope= Ope.getInstance(IQryptConfigurator.encKey);
    }
    public Object decrypt(String encryptedStr, Class t)
    {
        Class type = t;
        if (type == int.class || type == long.class
                || type == Integer.class || type == Long.class )
        {
            long[] decoded = encoder.decode(encryptedStr);
            IntEncryptor encryptor = new IntEncryptor(ope);
            return encryptor.decrypt(decoded,true);
        }
        else if (type == float.class || type == double.class ||
                type == Double.class || type == Float.class)
        {
            long[] decoded = encoder.decode(encryptedStr);
            FloatEncryptor encryptor = new FloatEncryptor(ope);
            return encryptor.decrypt(decoded,true);
        }
        else if (type == Date.class)
        {
            long[] decoded = encoder.decode(encryptedStr);
            DateEncryptor encryptor = new DateEncryptor(ope);
            return encryptor.decrypt(decoded,true);
        }
        else throw new IQryptException("Type not supported");
    }

    @Override
    public byte[] decryptBytes(byte[] encBytes) {
        throw new RuntimeException("ROPE does not support this method ");
    }

    @Override
    public byte[] encryptBytes(byte[] bytes) {
        throw new RuntimeException("ROPE does not support this method ");
    }

    @Override
    public Object decrypt(String toDecrypt, Class t, boolean encodingBase16) {
        throw new RuntimeException("ROPE does not support this method ");
    }

    @Override
    public String encrypt(Object toEncrypt, boolean encodingBase16) {
        throw new RuntimeException("ROPE does not support this method ");
    }

    public String encrypt(Object obj)
    {
        Class type = obj.getClass();
        if (type == int.class || type == Integer.class )
        {
            Long val=new Long((int)obj);
            if(val < 0 || val > Ope.MAX)
            {
                throw new IllegalArgumentException("Range should be 0-" + Ope.MAX);
            }
            IntEncryptor encryptor = new IntEncryptor(ope);
            long[] encVal = encryptor.encrypt(val,true);
            return encoder.encode(encVal);
        }
        else if (type == long.class || type == Long.class )
        {
            Long val=(Long)obj;
            if(val < 0 || val > Ope.MAX)
            {
                throw new IllegalArgumentException("Range should be 0-" + Ope.MAX);
            }
            IntEncryptor encryptor = new IntEncryptor(ope);
            long[] encVal = encryptor.encrypt(val,true);
            return encoder.encode(encVal);
        }
        else if (type == float.class || type == Float.class)
        {
            float val=(float)obj;
            if(val < 0 || val > Ope.MAX)
            {
                throw new IllegalArgumentException("Range should be 0-" + Ope.MAX);
            }
            FloatEncryptor encryptor = new FloatEncryptor(ope);
            long[] encVal = encryptor.encrypt(val,true);
            return encoder.encode(encVal);
        }
        else if (type == double.class|| type == Double.class  )
        {
            float val= (float)((double)obj);
            if(val < 0 || val > Ope.MAX)
            {
                throw new IllegalArgumentException("Range should be 0-" + Ope.MAX);
            }
            FloatEncryptor encryptor = new FloatEncryptor(ope);
            long[] encVal = encryptor.encrypt(val,true);
            return encoder.encode(encVal);
        }
        else if (type == Date.class)
        {
            Date date=(Date)obj;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if(cal.YEAR > 2500 )
            {
                throw new IllegalArgumentException("Year should be less than 2500");
            }
            DateEncryptor encryptor = new DateEncryptor(ope);
            long[] encVal = encryptor.encrypt(date,true);
            return encoder.encode(encVal);
        }
        else throw new IQryptException("Type not supported");
    }
}
