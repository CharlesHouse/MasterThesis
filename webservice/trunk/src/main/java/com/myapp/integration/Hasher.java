package com.myapp.integration;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hasher for AuthenticationHandler.
 *
 * @author Riku Miekk-oja
 * @version $Id: Hasher.java 13767 2017-06-26 10:24:39Z qzhong $
 */
public class Hasher
{

    public static String hash( String stringToHash, String hashFunction )
    {
        String encoding = "UTF-8";
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance( hashFunction );
            messageDigest.update( stringToHash.getBytes( encoding ) );
            byte[] hashBytes = messageDigest.digest();
            String hexEncodedHash = new String( Hex.encodeHex( hashBytes ) );
            return hexEncodedHash;
        }
        catch( NoSuchAlgorithmException nsae )
        {
            System.out.println( nsae.getMessage() );
            return "";
        }
        catch( UnsupportedEncodingException uee )
        {
            System.out.println( uee.getMessage() );
            return "";
        }
    }
}
