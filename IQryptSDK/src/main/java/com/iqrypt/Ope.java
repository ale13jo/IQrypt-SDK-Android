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

import java.nio.ByteBuffer;
import java.util.Arrays;

 class Ope
    {

        private byte[] key;
        public static int MAX  = (int)Math.pow(2, 20);
        private int salt;
        private int[] a;
        private static Ope instance;
        public static Ope getInstance(String key)
        {
            if (instance == null)
            {
                instance = new Ope(key);
            }
            return instance;
        }
        private Ope(String key)
        {
            this.key = CryptoUtil.buildKey(key, 32);
            a= new int[MAX];
            preEncrypt();
            
        }
       
        private void preEncrypt() {


            int[] seed = new int[256];

            seed[0] = key[0];
            for (int i = 1; i < 256; i++) {
                if (i % 32 == 0)
                {
                    key = CryptoUtil.buildKey(Base64.encodeToString(this.key, 0).trim(), 32);
                }
                seed[i] = key[i % 32];
            }
            ISAACRand x = new ISAACRand(seed);
            while (salt<=0)
            {
                salt = x.nextInt();
            }
            x = new ISAACRand(seed);
            for (int i = 0; i < MAX; i++) {
                a[i] = x.nextInt();
            }
            Arrays.sort(a);

            boolean existsDuplicates = true;
            while (existsDuplicates) {
                int jj = 0;
                int ii = 1;
                while (ii < a.length) {
                    if (a[ii] == a[jj]) {
                        ii++;
                    } else {
                        jj++;
                        a[jj] = a[ii];
                        ii++;
                    }
                }
                existsDuplicates = jj + 1 < a.length;
                if (existsDuplicates) {
                    int[] a2 = new int[a.length - jj];
                    for (int p = 0; p < a2.length; p++) {
                        a2[p] = x.nextInt();
                    }

                    System.arraycopy(a2, 0, a, jj, a2.length);
                    Arrays.sort(a);
                }

            }
           // long estimatedTime = System.currentTimeMillis() - startTime;
            //Log.d("elapsed millis:", ""+estimatedTime);

        }
        public long encrypt(long data) {

            return a[(int) data] - (long) Integer.MIN_VALUE + salt;
        }

        public long decrypt(long data) {

            data-= -(long)Integer.MIN_VALUE + salt;

            long decrypted= (long) Arrays.binarySearch(a, (int) data);
            if(decrypted<0)
                return 0;
            return decrypted;

        }
        public long encryptRandomized(long data) {

            long start = a[(int) data] - (long) Integer.MIN_VALUE + salt;
            long end = a[(int) data + 1] - 1 - (long) Integer.MIN_VALUE + salt;
            MyRandom rnd = new MyRandom();
            byte[] rndBytes = new byte[8];
            rnd.fillRandomBuffer(rndBytes);
            long rndLong =bytesToLong(rndBytes);
            if (rndLong < 0)
                rndLong = -rndLong;
            return start + (rndLong % (end - start));

        }
        public long decryptRandomized(long data) {

            data -= -(long) Integer.MIN_VALUE + salt;
            if (data > a[MAX - 1])
                return 0;
            long decrypted = Arrays.binarySearch(a, (int) data);
            if (decrypted >= 0)
                return decrypted;
            else
                return ~decrypted - 1;


        }
        private long bytesToLong(byte[] bytes) {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.put(bytes);
            buffer.flip();
            return buffer.getLong();
        }

       

    }
   

