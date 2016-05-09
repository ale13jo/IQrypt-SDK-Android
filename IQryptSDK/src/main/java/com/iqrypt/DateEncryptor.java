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


 class DateEncryptor
    {
        Ope cipher;
        

        public DateEncryptor(Ope ope)
        {
            cipher = ope;
        }


        private Calendar setDateComponent(long dateComponent)
        {
            Calendar cal = Calendar.getInstance();
            int days=(int)dateComponent%365;
            int year=(int)(dateComponent-days)/365;
            cal.set(Calendar.YEAR,year);
            cal.set(Calendar.DAY_OF_YEAR,days);
            cal.set(Calendar.HOUR_OF_DAY,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            return cal;
        }

        private void setTimeComponent(Calendar date, long milliseconds)
        {
            date.setTimeInMillis(date.getTimeInMillis()+milliseconds*100);
        }

        private long getTimeComponent(Date date)
        {

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),0,0,0);
            cal2.set(Calendar.HOUR_OF_DAY, 0);
            cal2.set(Calendar.MILLISECOND, 0);
            long milliseconds1 = cal.getTimeInMillis();
            long milliseconds2 = cal2.getTimeInMillis();
            long diff = milliseconds1 - milliseconds2;
            return (long)Math.round((float)diff/100);

        }
    

        private long getDateComponent(Date date)
        {
        	   Calendar cal = Calendar.getInstance();
        	   cal.setTime(date);
        	   return cal.get(Calendar.YEAR)*365+cal.get(Calendar.DAY_OF_YEAR);

        }

       

        public long[] encrypt(Date toEncrypt,boolean randomized)
        {
            
            long dateComponent = getDateComponent(toEncrypt);
            long encryptedDateComponent =randomized?cipher.encryptRandomized(dateComponent): cipher.encrypt(dateComponent);
                  
            long timeComponent = getTimeComponent(toEncrypt);
            if (timeComponent != 0)
            {
                long encryptedTimeComponent =randomized?cipher.encryptRandomized(timeComponent): cipher.encrypt(timeComponent);
                return new long[] { encryptedDateComponent, encryptedTimeComponent };
            }
            else
            {
                return new long[] { encryptedDateComponent };
            }

        }

        public Date decrypt(long[] encryptedData,boolean randomized)
        {

            long dateComponent = 0, timeComponent = 0;
            if (encryptedData.length>= 1)
            {
                dateComponent =randomized?cipher.decryptRandomized(encryptedData[0]): cipher.decrypt(encryptedData[0]);
            }
            Calendar date= setDateComponent( dateComponent);
            Date dt=date.getTime();
            if (encryptedData.length == 2)
            {
                timeComponent =randomized?cipher.decryptRandomized(encryptedData[1]):cipher.decrypt(encryptedData[1]);
            }
            setTimeComponent( date, timeComponent);
            return date.getTime();
        }
    }

