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

import java.util.Arrays;


 class CBCCipher implements ICBCCipher {

	 	IBlockCipher encryptor;
      final int BLOCK_SIZE;
      MyRandom random = new MyRandom();
      public CBCCipher(IBlockCipher encryptor)
      {
          this.encryptor = encryptor;
          BLOCK_SIZE = encryptor.getBlockSize() / 8;
      }
     public byte[] encrypt(byte[] bytesIn)
     {
         byte[] IV = new byte[BLOCK_SIZE];
         random.fillRandomBuffer(IV);
        return  encrypt(bytesIn,IV);

     }
      public byte[] encrypt(byte[] bytesIn,byte[] IV)
      {
          if (bytesIn.length % BLOCK_SIZE!=0)
              throw new IllegalArgumentException("bytesIn size invalid");

          byte[] bytesOut = new byte[bytesIn.length + BLOCK_SIZE];//larger to keep IV
          System.arraycopy(IV, 0, bytesOut, 0, IV.length);//first block is IV
          byte[] cbc = new byte[BLOCK_SIZE];
          System.arraycopy(IV,0, cbc,0, BLOCK_SIZE);

          for (int i = 0; i < bytesIn.length; i += BLOCK_SIZE)
          {
              for (int j = i % BLOCK_SIZE; j < BLOCK_SIZE; j++)
              {
                  bytesIn[i+j] ^= cbc[j];
              }
              this.encryptor.encrypt(bytesIn,i, bytesIn);
              System.arraycopy(bytesIn, i, cbc, 0, BLOCK_SIZE);
          }
          System.arraycopy(bytesIn, 0, bytesOut, BLOCK_SIZE, bytesIn.length);
          return bytesOut;
      }
      public byte[] decrypt(byte[] bytesIn)
      {
          if (bytesIn.length % BLOCK_SIZE != 0)
              throw new IllegalArgumentException("bytesIn size invalid");

          byte[] cbc = new byte[BLOCK_SIZE];
          byte[] cbcNext = new byte[BLOCK_SIZE];
          byte[] bytesOut = new byte[bytesIn.length - BLOCK_SIZE];//remove IV
          System.arraycopy(bytesIn, 0, cbc, 0, cbc.length);//first block is IV
          for (int i = BLOCK_SIZE; i < bytesIn.length; i += BLOCK_SIZE)
          {
        	  System.arraycopy(bytesIn, i, cbcNext, 0, BLOCK_SIZE);

              this.encryptor.decrypt(bytesIn,i, bytesIn);
              for (int j = i % BLOCK_SIZE; j < BLOCK_SIZE; j++)
              {
                  bytesIn[i + j] ^= cbc[j];
              }
              System.arraycopy(cbcNext,0, cbc,0, BLOCK_SIZE);
              
          }
          System.arraycopy(bytesIn, BLOCK_SIZE, bytesOut, 0, bytesOut.length);
          return bytesOut;
      }
      private int paddingSize(int length)
      {
          if (length % BLOCK_SIZE == 0)
              return length;
          else
              return length + (BLOCK_SIZE - (length % BLOCK_SIZE));
      }

	@Override
	public byte[] ensureLength(byte[] bytesIn) {

		return CryptoUtil.ensureLength(bytesIn, BLOCK_SIZE);
	}
     public byte[] paddPKS7(byte[] bytesIn)
     {
         int nrBytes = paddingSize(bytesIn.length);
         if (nrBytes != bytesIn.length) {
             int padSize = nrBytes - bytesIn.length;
             int initLen = bytesIn.length;
             bytesIn = Arrays.copyOf(bytesIn, nrBytes);

             for (int i = initLen; i < nrBytes; i++) {
                 bytesIn[i] = (byte) padSize;
             }
         }
         return bytesIn;
     }
     public byte[] removePaddPKS7(byte[] bytesIn)
     {
         if (bytesIn.length > 0)
         {
             byte lastByte = bytesIn[bytesIn.length - 1];
             int till = bytesIn.length - BLOCK_SIZE < bytesIn.length - lastByte ? bytesIn.length - lastByte : bytesIn.length - BLOCK_SIZE;
             //check integrity
             for (int b = bytesIn.length - 1; b > till; b--)
             {
                 if (bytesIn[b] != lastByte)
                     return bytesIn;
             }
            return Arrays.copyOf (bytesIn, bytesIn.length-lastByte);

         }
         return bytesIn;
     }
     

}
