package microYoga.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PhotoUtils {

    public static void generateThumbPicture(String originalPath, String thumbPath, int targetW, int targetH) throws IOException {

        File originalFile = new File(originalPath);
        BufferedImage originalImage = ImageIO.read(originalFile);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        if(width <= targetW){
            File outFile = new File(thumbPath);
            ImageIO.write(originalImage, originalFile.getName().substring(originalFile.getName().lastIndexOf(".") + 1), outFile);
            return;
        }

        BufferedImage newImage = new BufferedImage(targetW, targetH, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();


        File outFile = new File(thumbPath);
        ImageIO.write(newImage, originalFile.getName().substring(originalFile.getName().lastIndexOf(".") + 1), outFile);

    }
}
