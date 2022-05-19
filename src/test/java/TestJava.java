import java.text.SimpleDateFormat;

/**
 * @author: Rrow
 * @date: 2022/5/19 0:25
 */
public class TestJava {
    public static void main(String[] args) {
        String format = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss").format(System.currentTimeMillis());
        System.out.println(format);
    }
}
