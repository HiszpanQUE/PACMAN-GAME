import ui.MenuPanel;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
        private static final String PACMAN = "data/logos/pacmanGameLogo.png";
    public Main() { init();}

    private void init(){
        setTitle("PAC-MAN Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setIconImage(new ImageIcon(PACMAN).getImage());
        setLocationRelativeTo(null);

        MenuPanel menuPanel = new MenuPanel();
        add(menuPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main frame = new Main();
        });
    }
}
