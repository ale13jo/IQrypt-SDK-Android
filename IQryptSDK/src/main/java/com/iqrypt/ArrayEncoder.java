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


class ArrayEncoder
    {
        private NumberEncoder numberEncoder;
        char separator = ' ';
        public ArrayEncoder()
        {
            this.numberEncoder = new NumberEncoder();
        }
        public String encode(long[] array)
        {
            StringBuilder builder = new StringBuilder();
            if (array.length == 0)
            {
                return "0";
            }

            for (int i = 0; i < array.length; i++)
            {
                String encoded = numberEncoder.encode(array[i]);
                if (i == 0)
                {
                    builder.append(encoded);
                }
                else
                {
                    builder.append(separator);
                    builder.append(encoded);
                }

            }

            return builder.toString();
        }

        public long[] decode(String str)
        {

            String[] values = str.split(separator+"");
            long[] results = new long[values.length];
            for (int i = 0; i < values.length; i++)
            {
                results[i] = numberEncoder.decode(values[i]);
            }

            return results;

        }

    }

