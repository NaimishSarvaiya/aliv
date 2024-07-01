package com.iotsmartaliv;

/**
 * This class is used as test class for test static method.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 14/5/19 :May : 2019 on 13 : 01.
 */
public class TestCla {
    public static void main(String[] s) {
        //System.out.println(Constant.isValidPassword("aaaaaaA1"));
     /*   String text = "6d987a391d90fc2451751b2bb85890fc6e452bc057f39a85d8d1fd552f965fb2";
        String password = "Test@12345";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        // bytes to hex
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        System.out.println(sb.toString());*/
        /*TimeZone tz = TimeZone.getDefault();
        System.out.println(" Timezon id :: " + tz.getID());
        String ss = "     hello ,    hjii     ";

        System.out.println(ss.length());
        System.out.println(ss);
        System.out.println(ss.trim().length());
        System.out.println(ss.trim());
        */
        String s1 = "VF123";
        s1 = s1.replace("VF", "");
        System.out.println(s1);
        s1 = s1.replace("V", "");
        System.out.println(s1);
    }
}
