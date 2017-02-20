package com.android.hq.androidutils.utils;

/**
 * Created by heqiang on 17-2-6.
 *
 * 对字符串异或加密
 */

public class StringUtil {

    /**
     * 对字符串异或加密
     * @param para
     * @return
     */
    public String getXorEncryption(String para){
        int key = 0x10;
        char[] charArray = para.toCharArray();
        for(int i =0;i<charArray.length;i++){
            charArray[i]=(char)(charArray[i]^key);
        }
//		byte key = 0x10;
//		byte[] bts =  para.getBytes();
//		for(int i = 0; i< bts.length;i++){
//			bts[i]^=key;
//		}
//		String s = new String(bts);

        return String.valueOf(charArray);
    }
}
