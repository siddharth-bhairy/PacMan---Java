import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
    int rowCount=21;
    int columnCount=19;
    int tileSize=16;
    int boardwidth=columnCount*tileSize;
    int boardHeight=rowCount*tileSize;

    JFrame frame= new JFrame("Pac-Man");
    //frame.setVisible(true);
    frame.setSize(boardwidth,boardHeight);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pacman pacmangame = new pacman();
    frame.add(pacmangame);
    frame.pack();
    pacmangame.requestFocus();
    frame.setVisible(true);

    }
}
