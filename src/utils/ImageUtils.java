package utils;

import javax.swing.*;
import java.awt.*;

public class ImageUtils {
    public static ImageIcon loadScaledIcon(Icon icon, int size) {
        ImageIcon imageIcon = (ImageIcon) icon;
        Image img = imageIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
