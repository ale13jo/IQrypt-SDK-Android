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

 class FloatEncryptor  {
        Ope cipher;

        public FloatEncryptor(Ope ope) {
            cipher = ope;
        }


        public long[] encrypt(float toEncrypt,boolean randomized) {
            long intPart = getIntPart(toEncrypt);
            long encryptedintPart =randomized?cipher.encryptRandomized(intPart): cipher.encrypt(intPart);

            long fractionalPart = fractionalPart(toEncrypt);
            if (fractionalPart != 0) {
                long encryptedfractionalPart =randomized?cipher.encryptRandomized(fractionalPart): cipher.encrypt(fractionalPart);
                return new long[]{encryptedintPart, encryptedfractionalPart};
            } else {
                return new long[]{encryptedintPart};
            }

        }

        public float decrypt(long[] encryptedData,boolean randomized) {

            long intComponent = 0, fractionalComponent = 0;
            if (encryptedData.length >= 1) {
                intComponent =randomized?cipher.decryptRandomized(encryptedData[0]): cipher.decrypt(encryptedData[0]);
            }
            float f = intComponent;
            if (encryptedData.length == 2) {
                fractionalComponent =randomized?cipher.decryptRandomized(encryptedData[1]): cipher.decrypt(encryptedData[1]);
                float a = (float) fractionalComponent / 100000;
                f += a;
            }
            return f;
        }

        private long fractionalPart(float n) {
            String s = Float.toString(n);
            if (s.indexOf(".") >= 0) {
                String ss = "0." + s.substring(s.indexOf(".") + 1);
                float f = Float.parseFloat(ss);
                f *= 100000;
                return getIntPart(f);
            } else return 0;

        }

        private long getIntPart(float n) {

            String s = Float.toString(n);
            if (s.indexOf(".") > 0) {
                long l=Long.parseLong(s.substring(0, s.indexOf(".")));
                return l;
            } else if (s == null || s.isEmpty() || s.indexOf(".") == 0) {
                return 0;
            } else {
                return Long.parseLong(s);
            }


        }
    }

