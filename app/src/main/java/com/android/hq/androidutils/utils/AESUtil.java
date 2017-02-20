package com.android.hq.androidutils.utils;

import java.nio.charset.Charset;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by heqiang on 16-6-16.
 *
 * Java实现AES加密解密
 */
public class AESUtil {
    private static final String KEY_ALGORITHM = "AES";

    private static final Charset charset = Charset.forName( "utf-8" );

    private static String decrypt( byte[] data, byte[] key ) {
        try {
            Key k = new SecretKeySpec( key, KEY_ALGORITHM );
            Cipher cipher = Cipher.getInstance( KEY_ALGORITHM );
            cipher.init( Cipher.DECRYPT_MODE, k );
            return new String( cipher.doFinal( data ), charset );
        } catch( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    public static String decrypt( String data, String key ) {
        return decrypt( parseHexStr2Byte( data ), key.getBytes( charset ) );
    }

    public static byte[] encrypt( byte[] data, byte[] key ) {
        try {
            Key k = new SecretKeySpec( key, KEY_ALGORITHM );
            Cipher cipher = Cipher.getInstance( KEY_ALGORITHM );
            cipher.init( Cipher.ENCRYPT_MODE, k );
            return cipher.doFinal( data );
        } catch( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    public static String encrypt( String data, String key ) {
        byte[] dataBytes = data.getBytes( charset );
        byte[] keyBytes = key.getBytes( charset );
        return parseByte2HexStr( encrypt( dataBytes, keyBytes ) );
    }

    public static byte[] parseHexStr2Byte( String hexStr ) {
        byte[] result = new byte[ hexStr.length() / 2 ];
        for( int i = 0; i < hexStr.length() / 2; i++ ) {
            int high = Integer.parseInt( hexStr.substring( i * 2, i * 2 + 1 ), 16 );
            int low = Integer.parseInt( hexStr.substring( i * 2 + 1, i * 2 + 2 ), 16 );
            result[ i ] = ( byte )( high * 16 + low );
        }
        return result;
    }

    public static String parseByte2HexStr( byte buf[] ) {
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < buf.length; i++ ) {
            String hex = Integer.toHexString( buf[ i ] & 0xFF );
            if( hex.length() == 1 ) {
                hex = '0' + hex;
            }
            sb.append( hex.toUpperCase() );
        }
        return sb.toString();
    }
}
