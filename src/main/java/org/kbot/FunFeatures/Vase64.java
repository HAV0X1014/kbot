package org.kbot.FunFeatures;

import java.io.UnsupportedEncodingException;

public class Vase64 {
    public static String vase64(String m) {
        String returnString = "Decode/Encode Error.";
        String eod = m.replace("[base64 ", "");                  //eod stands for "encode or decode"
        String dt = null;                                                            //stands for decoded text

        if (eod.startsWith("decode")) {
            byte[] base = null;
            String ttd = eod.replace("decode ", "");                //assign text to decode (ttd)
            try {
                base = java.util.Base64.getDecoder().decode(ttd);
            } catch(IllegalArgumentException iae) {
                returnString = "Decoding Failed";
            }
            try {
                dt = new String(base, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (dt.contains("@")) {
                dt = "```" + dt + "```";
            }
            returnString = dt;
        }else

        if (eod.startsWith("encode")) {
            String tte = eod.replace("encode ", "");
            String et = null;
            byte[] base = null;
            byte[] tte2 = tte.getBytes();
            try {
                base = java.util.Base64.getEncoder().encode(tte2);
            } catch(IllegalArgumentException iae) {
                returnString = "Encoding Failed";
            }
            try {
                et = new String(base, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            returnString = et;
        }
        return returnString;
    }
}
