import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class pacman extends JPanel implements ActionListener,KeyListener {
    class block{
        int x;
        int y;
        int height;
        int width;
        Image image;

        int startx;
        int starty;

        char direction='U';
        int velocityx=0;
        int velocityy=0;

        block(Image image,int x,int y,int width,int height)
        {
            this.image=image;
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
            this.startx=x;
            this.starty=y;
        }

        void updatedirection(char direction)
        {
            char prevdirection=this.direction;
            this.direction=direction;
            updatevelocity();

            this.x+=this.velocityx;
            this.y+=this.velocityy;

            for(block wall : walls){
                if(collision(this,wall))
                {
                    this.x-=this.velocityx;
                    this.y-=this.velocityy;
                    this.direction=prevdirection;
                    updatevelocity();
                }
            }
        }
        void updatevelocity()
        {
            if(this.direction=='U')
            {
                this.velocityx=0;
                this.velocityy=-tileSize/4;
            }
            else if(this.direction == 'D')
            {
                
                this.velocityx=0;
                this.velocityy=tileSize/4;
            }
            else if(this.direction=='L')
            {
                this.velocityy=0;
                this.velocityx=-tileSize/4;
            }
            else if(this.direction=='R')
            {
                this.velocityy=0;
                this.velocityx=tileSize/4;
            }
        }
        void reset()
        {
            this.x=this.startx;
            this.y=this.starty;
        }
    }
    private int rowCount=21;
    private int columnCount=19;
    private int tileSize=32;
    private int boardwidth=columnCount*tileSize;
    private int boardHeight=rowCount*tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanRightImage;
    private Image pacmanLeftImage;

    HashSet<block> walls;
    HashSet<block> foods;
    HashSet<block> ghosts;
    block pacman;

    Timer gameloop;

    char[] dircetions = {'U','D','R','L'};
    Random random = new Random();

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    int score=0;
    int lives=3;
    boolean gameover=false;


    pacman()
    {
        setPreferredSize(new Dimension(boardwidth,boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //load images
        wallImage=new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage=new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage=new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage=new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage=new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        
        pacmanUpImage=new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanUpImage=new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage=new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage=new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
        loadmap();
        for(block ghost : ghosts){
            char newdirection= dircetions[random.nextInt(4)];
            ghost.updatedirection(newdirection);
        }
        gameloop=new Timer(50,this);
        gameloop.start();

    }

    public void loadmap(){
        walls = new HashSet<block>();
        foods = new HashSet<block>();
        ghosts = new HashSet<block>();

        for(int r=0;r<rowCount;r++)
        {
            for(int c=0;c<columnCount;c++)
            {
                String row = tileMap[r];
                char tilemapchar=row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;
                if(tilemapchar=='X')
                {
                    block wall = new block(wallImage,x,y,tileSize,tileSize);
                    walls.add(wall);
                }
                else if(tilemapchar=='b')
                {
                    block ghost = new block(blueGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tilemapchar=='p')
                {
                    block ghost = new block(pinkGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tilemapchar=='o')
                {
                    block ghost = new block(orangeGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tilemapchar=='r')
                {
                    block ghost = new block(redGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tilemapchar=='P')
                {
                    pacman= new block(pacmanRightImage,x,y,tileSize,tileSize);
                }
                else if(tilemapchar==' ')
                {
                    block food = new block(null,x+14,y+14,4,4);
                    foods.add(food);
                }

            }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g)
    {
        g.drawImage(pacman.image,pacman.x, pacman.y, pacman.width, pacman.height,null);
        for(block ghost : ghosts){
            g.drawImage(ghost.image,ghost.x,ghost.y,ghost.width,ghost.height,null);
        }

        for(block wall : walls){
            g.drawImage(wall.image,wall.x,wall.y,wall.width,wall.height,null);
        }
        g.setColor(Color.white);
        for(block food : foods){
            g.fillRect(food.x,food.y,food.width,food.height);
        }

        g.setFont(new Font("Arial",Font.PLAIN,18));
        if(gameover){
            g.drawString("Game Over: "+String.valueOf(score),tileSize/2,tileSize/2);
        }
        else
        {
            g.drawString("x: "+String.valueOf(lives),tileSize/2,tileSize/2);
        }
    }

    public void move(){
        pacman.x+=pacman.velocityx;
        pacman.y+=pacman.velocityy;

        for(block wall : walls){
            if(collision(pacman, wall) || pacman.x<=0 || pacman.x+pacman.width >=boardwidth)
            {
                pacman.x-=pacman.velocityx;
                pacman.y-=pacman.velocityy;
                break;
            }
        }

        for(block ghost : ghosts)
        {
            if(collision(ghost,pacman))
            {
                lives-=1;
                if(lives==0)
                {
                    gameover=true;
                    return;
                }
                resetpositions();
            }
            if(ghost.y==tileSize*9 || ghost.direction!='U' && ghost.direction!='D')
            {
                ghost.updatedirection('U');
            }
            ghost.x+=ghost.velocityx;
            ghost.y+=ghost.velocityy;
            for(block wall  : walls){
                if(collision(ghost, wall) || ghost.x<=0 || ghost.x+ghost.width >=boardwidth)
                {
                    ghost.x-=ghost.velocityx;
                    ghost.y-=ghost.velocityy;
                    char newdirection=dircetions[random.nextInt(4)];
                    ghost.updatedirection(newdirection);
                }
            }
        }
        block foodeaten=null;
        for(block food:foods)
        {
            if(collision(pacman, food))
            {
                foodeaten=food;
                score+=10;
            }
        }
        foods.remove(foodeaten);
        if(foods.isEmpty())
        {
            loadmap();
            resetpositions();
        }

    }

    public boolean collision(block a, block b)
    {
        return a.x<b.x+b.width && 
        a.x + a.width > b.x &&
        a.y < b.y + b.height &&
        a.y + a.height > b.y;
    }

    public void resetpositions()
    {
        pacman.reset();
        pacman.velocityx=0;
        pacman.velocityy=0;
        for(block ghost : ghosts)
        {
            ghost.reset();
            char newdirection = dircetions[random.nextInt(4)];
            ghost.updatedirection(newdirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameover)
        {
            gameloop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(gameover)
        {
            loadmap();
            resetpositions();
            lives=3;
            score=0;
            gameover=false;
            gameloop.start();
        }
        //System.out.println("KeyEvent : "+e.getKeyCode());
        if(e.getKeyCode()==KeyEvent.VK_UP)
        {
            pacman.updatedirection('U');
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN)
        {
            pacman.updatedirection('D');
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT)
        {
            pacman.updatedirection('L');
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT)
        {
            pacman.updatedirection('R');
        }

        if(pacman.direction=='U')
        {
            pacman.image=pacmanUpImage;
        }
        else if(pacman.direction=='D')
        {
            pacman.image=pacmanDownImage;
        }
        else if(pacman.direction=='L')
        {
            pacman.image=pacmanLeftImage;
        }
        else if(pacman.direction=='R')
        {
            pacman.image=pacmanRightImage;
        }
    }
}
