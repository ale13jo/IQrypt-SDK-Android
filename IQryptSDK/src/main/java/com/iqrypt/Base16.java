
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

 class Base16 {
     public static byte[] decode(String input)
     {
         int len = input.length();
         byte[] data = new byte[len / 2];
         for (int i = 0; i < len; i += 2) {
             data[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                     + Character.digit(input.charAt(i+1), 16));
         }
         return data;

     }
     final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
     public static String encode(byte[] bytes)
     {
         char[] hexChars = new char[bytes.length * 2];
         for ( int j = 0; j < bytes.length; j++ ) {
             int v = bytes[j] & 0xFF;
             hexChars[j * 2] = hexArray[v >>> 4];
             hexChars[j * 2 + 1] = hexArray[v & 0x0F];
         }
         return new String(hexChars);

     }



 }

   