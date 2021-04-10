import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

class StudentIDServer {

    public static String getId(String name) {

        String id = "XXXXXXXX";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(name.getBytes());
            byte[] digest = md.digest();
            //convertir le tableau de bits en une format hexadécimal
            StringBuffer sb = new StringBuffer();
            sb.append(Integer.toString((digest[0] & 0xff) + 0x100, 16).substring(1));
            sb.append(Integer.toString((digest[1] & 0xff) + 0x100, 16).substring(1));
            sb.append(Integer.toString((digest[2] & 0xff) + 0x100, 16).substring(1));
            sb.append(Integer.toString((digest[3] & 0xff) + 0x100, 16).substring(1));
            /*digest.length*/
            id = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return id;
    }

}
