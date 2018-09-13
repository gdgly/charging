import java.io.IOException;
import java.util.Calendar;

public class JustTest {

    public static void main(String[] args) {

        int i = 100;

        // 发送前置消息
        /*
         * MQMsgProduct mqMsgProduct = new MQMsgProduct(); mqMsgProduct.init(); while (i-- > 0) { Calendar now =
         * Calendar.getInstance(); MsgAppointmentReq msg = new MsgAppointmentReq(Integer.valueOf(1), Integer.valueOf(1),
         * "123456", now.getTime(), "123456"); com.alibaba.rocketmq.client.producer.SendResult result =
         * mqMsgProduct.sendMQMsg(msg); System.out.println(result); }
         */
        // Date d = new Date();
        // d.setTime(184774955L);
        // Calendar now = Calendar.getInstance();
        // now.setTime(d);
        // System.out.println("Finded Zombie channel!  ID=" + d.toString());

        String carNo = "ABF389";

        String ascii = "";
        for (i = 0; i < carNo.getBytes().length; i++) {
            ascii += Integer.toHexString(carNo.getBytes()[i]);
        }
        System.out.println("Finded Zombie channel!  ID=" + ascii);
        byte carByte[] = { 0x39, 0x38, 0x33, 0x46, 0x42, 0x41 };
        carNo = "";
        for (i = 0; i < carByte.length; i++) {
            carNo += (char) carByte[i];
        }

        System.out.println("Finded Zombie channel!  ID=" + carNo);
        //
        /*
         * int i = Integer.MAX_VALUE - 1; for (int k = 0; k < 10; k++) { i++; if (i < 0) i = 0; System.out.println(i); }
         */
    }

    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }

    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    static byte[] toByte(Calendar date) {
        short msec = 1234;
        byte min = 35;
        byte hour = 5;
        byte day_m = (byte) date.get(Calendar.DAY_OF_MONTH);
        byte day_w = (byte) (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? 7 : date.get(Calendar.DAY_OF_WEEK) - 1);
        byte month = (byte) (date.get(Calendar.MONTH) + 1);
        byte year = (byte) (date.get(Calendar.YEAR) - 2000);
        byte[] time = new byte[7];
        time[0] = (byte) (msec & 0xFF);
        time[1] = (byte) ((msec >> 8) & 0xFF);
        time[2] = (byte) (min & 0x3F);
        time[3] = (byte) (hour & 0x1F);
        time[4] = (byte) ((day_m & 0x1F) | (day_w << 5));
        time[5] = month;
        time[6] = year;
        return time;
    }

    public float aMethod(float a, float b) throws IOException {
        return b;
    }

}
