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

import java.util.ArrayList;

class NumberEncoder
    {
        private final String PozitiveSign = "@";
        public long decode(String encoded)
        {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(encoded);
            int index = 0;
            int length = 1;
            int signCount = 0;
            String destination = "";
            while (strBuild.length() > 0)
            {
                long value;
                destination = strBuild.substring(index, index + length);
                index+=length;
                try{
                    value = Long.parseLong(destination);
                    signCount--;
                    if (signCount > 0)
                    {
                        length = (int)value;
                    }
                    else
                    {
                        return value;
                    }
                }
                catch(NumberFormatException ex)
                {
                    signCount++;
                }
            }
            return 0;
        }
        public String encode(long number)
        {
            int signPadding = 1;
            ArrayList<String> lengthPadding = new  ArrayList<String>();
            StringBuilder strB = new StringBuilder();

            int n = EncodingUtil.length(number);
            lengthPadding.add(number + "");
            while (n > 1)
            {
                signPadding++;
                lengthPadding.add(n + "");
                n = EncodingUtil.length((long) n);
            }

            for (int i = 0; i < signPadding; i++)
            {
                strB.append(PozitiveSign);
            }
            int size =  lengthPadding.size();
            for (int i = size - 1; i >= 0; i--)
            {
                strB.append(lengthPadding.get(i));
            }
            if (size == 0)
            {
                strB.append("0");
            }
            return strB.toString();
        }
    }

