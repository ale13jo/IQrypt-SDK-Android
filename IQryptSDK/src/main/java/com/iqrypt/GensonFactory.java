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

import com.owlike.genson.Genson;

import java.text.SimpleDateFormat;


 class GensonFactory {
    private static Genson genson;
    public static Genson Build()
    {

        if(genson==null)
        {
            SimpleDateFormat myDateFormat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            genson = new com.owlike.genson.GensonBuilder()
                    .useDateFormat(myDateFormat)
                    .create();

        }
        return genson;
    }
}
