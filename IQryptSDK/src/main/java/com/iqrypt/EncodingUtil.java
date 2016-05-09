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

 class EncodingUtil {

      public static String reverseStr(String str)
      {
          StringBuilder strBuild = new StringBuilder();
          int size = str.length();
          for (int i = 0;i< size;i++)
          {
        	  char character = str.charAt(i);
              switch (character)
              {
                  case '0':
                      {
                          strBuild.append("9");
                          break;
                      }
                  case '1':
                      {
                          strBuild.append("8");
                          break;
                      }
                  case '2':
                      {
                          strBuild.append("7");
                          break;
                      }
                  case '3':
                      {
                          strBuild.append("6");
                          break;
                      }
                  case '4':
                      {
                          strBuild.append("5");
                          break;
                      }
                  case '5':
                      {
                          strBuild.append("4");
                          break;
                      }
                  case '6':
                      {
                          strBuild.append("3");
                          break;
                      }
                  case '7':
                      {
                          strBuild.append("2");
                          break;
                      }
                  case '8':
                      {
                          strBuild.append("1");
                          break;
                      }
                  case '9':
                      {
                          strBuild.append("0");
                          break;
                      }
                  default:
                      {
                          strBuild.append(character);
                          break;
                      }
              }
          }
          return strBuild.toString();
      }
      
	  public static int length(long n)
	  {
	      int result = 0;
	      while (n > 0)
	      {
	          if (n > 1000)
	          {
	              result += 3;
	              n /= 1000;
	          }
	          else
	          {
	              result += 1;
	              n /= 10;
	          }
	      }
	      return result;
	  }
}
