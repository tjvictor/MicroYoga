package microYoga;

import org.jasypt.util.text.BasicTextEncryptor;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainConsole {
    public static void main(String[] args) {

        try {
//            Date today=new Date();
//            for(int day = 0; day < 7; day++){
//                SimpleDateFormat dateFm = new SimpleDateFormat("EEE");
//                switch (dateFm.format(CommonUtils.dateAddDay(today, day)).toLowerCase()){
//                    case "mon": System.out.println("周一"); break;
//                    case "tue": System.out.println("周二"); break;
//                    case "wed": System.out.println("周三"); break;
//                    case "thu": System.out.println("周四"); break;
//                    case "fri": System.out.println("周五"); break;
//                    case "sat": System.out.println("周六"); break;
//                    case "sun": System.out.println("周日"); break;
//                    default: System.out.println(""); break;
//                }
//            }
            BasicTextEncryptor encryptor = new BasicTextEncryptor();
            encryptor.setPassword("eas");
            String encrypted = encryptor.encrypt("Abc@12345");
            System.out.println(encrypted);


        }catch(Exception e){

        }
    }

    public static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
        int width = source.getWidth();
        int height = source.getHeight();
        return zoomInImage(source, targetW, targetH);
    }

    public static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }
}