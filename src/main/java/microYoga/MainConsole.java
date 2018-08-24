package microYoga;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MainConsole {
    public static void main(String[] args) {

        try {
            File fileOne = new File("C:\\temp\\yoga\\photos\\e1f1bdce-b66c-4eb4-9877-4f26f1c10000\\20180824142113-Chrysanthemum.jpg");
            BufferedImage imageFirst = ImageIO.read(fileOne);
            int width = imageFirst.getWidth();
            int height = imageFirst.getHeight();

            if(width <= 90)
                return;

            imageFirst = zoomInImage(imageFirst, 90, 90);
            File outFile = new File("c:/temp/4.jpg");
            ImageIO.write(imageFirst, "jpg", outFile);
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