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

import java.nio.charset.Charset;

import com.owlike.genson.Genson;

 class CryptoJsonSerializer implements IDocumentSerializer
{

    public CryptoJsonSerializer()
    {


    }

	@Override
    public Object deserialize(Class<? extends Object> type, byte[] objectBytes) {

        String jsonStr = new String(objectBytes, Charset.forName("UTF-8"));

        String str = jsonStr.replaceAll("\0", "");
        Genson genson= GensonFactory.Build();

        return genson.deserialize(str, type);

    }
	@Override
    public byte[] serialize(Object obj) {

        Genson genson= GensonFactory.Build();

        String jsonStr = genson.serialize(obj);
        return jsonStr.getBytes(Charset.forName("UTF-8"));

    }
}
