package control;

import Element.BackgroundElement;
import Element.Pieces;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import stage.Stage;

/**Classe que controla a criação e desenho das peças e todo o menu ( prox. peça,
 * obstáculos, linhas, pontuação, etc ) Contendo todas váriaveis e métodos para
 * fazer o controle.
 *  
 * @author Grupo 1
 */
public class GameScreen extends JPanel implements KeyListener, Serializable {

    private BufferedImage blocks, background;
    BackgroundElement obstacle = new BackgroundElement();
    private int[][] matrixObstacles;
    private final int panelHeight = 21, panelWidth = 11;
    private int[][] panel = new int[panelHeight][panelWidth];
    private int blockSize = 30;
    private final Timer looper;
    private int FPS = 60;
    private int delay = 1000 / FPS;
    private int normalUp;
    private Pieces[] piece = new Pieces[7];
    private Pieces obstaculos;
    private int totalLines = 0; //total de linhas do jogo todo
    private int linesCompleted = 0; //linhas que foram completadas de uma vez
    private int score;
    private int auxScore = 0;
    private int level;
    private Font customFont;
    private boolean gamePaused = false;
    private static Pieces currentShape, nextShape;
    private boolean gameOver = false;
    private JFrame frame;
    private Stage stage;
    private Clip musicStage;
    private Clip gameOverSound;
    private boolean saveGame;

    /**Construtor da classe GameScreen no caso de um novo jogo.
     *
     * @param backgroundPath - String que contém o caminho para a imagem do background.
     * @param stage - Instância da classe Stage que determina qual é a fase e as características.
     * @param frame - JFrame que contém o listener para movimentação das peças.
     */
    public GameScreen(String backgroundPath, Stage stage, JFrame frame) {

        this.totalLines = 0;
        this.score = 0;
        this.level = 1;
        this.linesCompleted = 0;
        this.stage = stage;
        this.frame = frame;
         this.saveGame = false;

        try {
            this.blocks = ImageIO.read(new File(stage.getTilePath()));
            background = ImageIO.read(new File(backgroundPath));

        } catch (IOException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        looper = new Timer(delay, new GameLooper());

        piece[0] = new Pieces(new int[][]{
            {1, 1, 1, 1} // peça I;
        }, blocks.getSubimage(0, 0, blockSize, blockSize), this, 1);

        piece[1] = new Pieces(new int[][]{
            {1, 1, 1},
            {0, 1, 0}, // peça T;
        }, blocks.getSubimage(blockSize, 0, blockSize, blockSize), this, 2);

        piece[2] = new Pieces(new int[][]{
            {1, 1, 1},
            {1, 0, 0}, // peça L;
        }, blocks.getSubimage(blockSize * 2, 0, blockSize, blockSize), this, 3);

        piece[3] = new Pieces(new int[][]{
            {1, 1, 1},
            {0, 0, 1}, // peça J;
        }, blocks.getSubimage(blockSize * 3, 0, blockSize, blockSize), this, 4);

        piece[4] = new Pieces(new int[][]{
            {0, 1, 1},
            {1, 1, 0}, // peça S;
        }, blocks.getSubimage(blockSize * 4, 0, blockSize, blockSize), this, 5);

        piece[5] = new Pieces(new int[][]{
            {1, 1, 0},
            {0, 1, 1}, // peça Z;
        }, blocks.getSubimage(blockSize * 5, 0, blockSize, blockSize), this, 6);

        piece[6] = new Pieces(new int[][]{
            {1, 1},
            {1, 1}, // peça O;
        }, blocks.getSubimage(blockSize * 6, 0, blockSize, blockSize), this, 7);
    }

    /**Construtor da GameScreen no caso de um jogo sendo carregado.
     *
     * @param backgroundPath - String tendo o caminho para a imagem de background.
     * @param loadStage - Stage sendo carregado, com todas características.
     * @param frame - JFrame com o listener da funçao.
     * @param loadPanel - Matriz das peças que contém as posições com peças fixas.
     * @param loadObstacles - Vetor com as posições dos obstaculos.
     * @param loadTotalLines - Número de linhas limpadas.
     * @param loadScore - Score do jogo salvo.
     * @param loadLevel - Número do nível atual.
     * @param loadCurrentCoord - Matriz que representa a peça que está caindo.
     * @param loadCurrentColor - Cor da peça atual.
     * @param loadNextCoord - Matriz que representa a próxima peça.
     * @param loadNextColor - A cor da próxima peça.
     */
    public GameScreen(String backgroundPath, Stage loadStage, JFrame frame, int loadPanel[][], int loadObstacles[][], int loadTotalLines,
            int loadScore, int loadLevel,int[][] loadCurrentCoord, int loadCurrentColor, int[][] loadNextCoord, int loadNextColor) {
        this.panel = loadPanel;
        this.matrixObstacles = loadObstacles;
        this.totalLines = loadTotalLines;
        this.score = loadScore;
        this.level = loadLevel;
        this.linesCompleted = 0;
        this.stage = loadStage;
        this.frame = frame;
        this.saveGame = false;
        
        try {
            this.blocks = ImageIO.read(new File(stage.getTilePath()));
            background = ImageIO.read(new File(backgroundPath));

        } catch (IOException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentShape = new Pieces(loadCurrentCoord,blocks.getSubimage(blockSize*(loadCurrentColor-1), 0, blockSize, blockSize),this,loadCurrentColor);
        nextShape = new Pieces(loadNextCoord,blocks.getSubimage(blockSize*(loadNextColor-1), 0, blockSize, blockSize),this,loadNextColor);
        looper = new Timer(delay, new GameLooper());

        piece[0] = new Pieces(new int[][]{
            {1, 1, 1, 1} // peça I;
        }, blocks.getSubimage(0, 0, blockSize, blockSize), this, 1);

        piece[1] = new Pieces(new int[][]{
            {1, 1, 1},
            {0, 1, 0}, // peça T;
        }, blocks.getSubimage(blockSize, 0, blockSize, blockSize), this, 2);

        piece[2] = new Pieces(new int[][]{
            {1, 1, 1},
            {1, 0, 0}, // peça L;
        }, blocks.getSubimage(blockSize * 2, 0, blockSize, blockSize), this, 3);

        piece[3] = new Pieces(new int[][]{
            {1, 1, 1},
            {0, 0, 1}, // peça J;
        }, blocks.getSubimage(blockSize * 3, 0, blockSize, blockSize), this, 4);

        piece[4] = new Pieces(new int[][]{
            {0, 1, 1},
            {1, 1, 0}, // peça S;
        }, blocks.getSubimage(blockSize * 4, 0, blockSize, blockSize), this, 5);

        piece[5] = new Pieces(new int[][]{
            {1, 1, 0},
            {0, 1, 1}, // peça Z;
        }, blocks.getSubimage(blockSize * 5, 0, blockSize, blockSize), this, 6);

        piece[6] = new Pieces(new int[][]{
            {1, 1},
            {1, 1}, // peça O;
        }, blocks.getSubimage(blockSize * 6, 0, blockSize, blockSize), this, 7);

    }


    /**Método que roda toda hora, desenha o cenário, o grid, os obstáculos, as peças
     * que cairam e estão fixas, prox. peça, todo o side panel e a peça atual.
     * Também é responsável por desenhar a tela de game over.
     * 
     * @param g - Objeto da classe Graphics do próprio java, usado como parâmetro nas funções
     * de desenho.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        BackgroundElement scenario = new BackgroundElement();
        scenario.drawStaticImage(g, background);

        BackgroundElement grid = new BackgroundElement();
        grid.drawGrid(g, panelHeight, panelWidth, blockSize);

        matrixObstacles = obstacle.drawObstacles(g, panelHeight, panelWidth, stage);

        obstacle.setTransposable(false);
        //JOptionPane.showMessageDialog(null, "chegou", "Erro", JOptionPane.INFORMATION_MESSAGE);

        //matrixObstacles = obstacle.drawObstacles(g, panelHeight, panelWidth, stage);
        for (int row = 0; row < panel.length; row++) {
            for (int col = 0; col < panel[row].length; col++) {

                if (panel[row][col] != 0) {

                    g.drawImage(blocks.getSubimage((panel[row][col] - 1) * blockSize,
                            0, blockSize, blockSize), (col + 1) * blockSize - 5, (row + 2) * blockSize, null);
                }

            }
        }
        for (int row = 0; row < nextShape.getCoords().length; row++) {
            for (int col = 0; col < nextShape.getCoords()[0].length; col++) {
                if (nextShape.getCoords()[row][col] != 0) {
                    g.drawImage(nextShape.getBlock(), col * 30 + 400, row * 30 + 130, null);
                }
            }
        }
        currentShape.render(g);

        drawSidePanel(g, stage.getStageName());

        if (gameOver) {

            String path = stage.getGameOverImagePath();
            Image screenImage;
            screenImage = new ImageIcon(path).getImage();
            scenario.drawAnimation(g, screenImage);

        }

    }

    /**Método que prepara o frame para um start game.
     *
     * @param label - O label seria a imagem da tela anterior, sendo removida para
     * iniciar o jogo.
     */
    public void startTetris(JLabel label) {

        startGame();
        this.frame.addKeyListener(this);
        this.frame.remove(label);
        this.frame.add(this);
        this.frame.revalidate();

    }

    /**Método que autualiza o número de linhas e checa o gameOver.
     * Também chama o método da classe currentShape, que autualiza os obstáculos.
     */
    private void update() {
        if (gameOver | saveGame) {
            return;
        }
        linesCompleted = currentShape.update(matrixObstacles);

        if (linesCompleted != 0) {
            addScore(2);
            this.totalLines += linesCompleted;
            linesCompleted = 0;
        }
    }

    /**Método que prepara o jogo para ser carregado, junção do startGame e startTetris.
     *
     * @param label - Contém a imagem da tela anterior, sendo removida para
     * iniciar o jogo.
     */
    public void loadGame(JLabel label) {

        this.frame.addKeyListener(this);
        this.frame.remove(label);
        this.frame.add(this);
        this.frame.revalidate();
       
        musicStage = BackgroundElement.LoadSound(stage.getMusicStagePath());
        musicStage.start();
        musicStage.loop(Clip.LOOP_CONTINUOUSLY);
        gameOver = false;
        looper.start();
    }

    /**Reseta todas funções, decide a próxima peça e a peça atual, e por fim começa 
     * o loop do jogo.
     */
    public void startGame() {
        musicStage = BackgroundElement.LoadSound(stage.getMusicStagePath());
        musicStage.start();
        musicStage.loop(Clip.LOOP_CONTINUOUSLY);
        stopGame();
        setNextShape();
        setCurrentShape();
        gameOver = false;
        looper.start();

    }

    public void playGameOverSound() {
        musicStage.stop();
        gameOverSound = BackgroundElement.LoadSound(stage.getGameOverMusicPath());
        gameOverSound.start();
    }

    public int[][] getPanel() {
        return panel;
    }

    /**Sorteia uma peça para ser a próxima.
     */
    public void setNextShape() {
        int index = (int) (Math.random() * piece.length);
        if (this.level < 2) {
            nextShape = new Pieces(piece[index].getCoords(), piece[index].getBlock(), this, piece[index].getColor());
        } else {
            nextShape = new Pieces(piece[index].getCoords(), piece[index].getBlock(), this, piece[index].getColor(), normalUp);
        }
    }

    /**Solta a peça atual no topo da tela, e certifica se ela está no presa no topo (gameOver)
     */
    public void setCurrentShape() {

        currentShape = nextShape;
        setNextShape();

        int flag = 0;
        for (int row = 0; row < currentShape.getCoords().length; row++) {
            for (int col = 0; col < currentShape.getCoords()[0].length; col++) {
                if (currentShape.getCoords()[row][col] != 0) {
                    if (panel[currentShape.getY() + row][currentShape.getX() + col] != 0) {
                        gameOver = true;
                        playGameOverSound();

                        //JOptionPane.showMessageDialog(null, "col=" + col + "\n row=" + row, "seila", JOptionPane.INFORMATION_MESSAGE);
                        flag = 1;

                    }
                }
                if (flag == 1) {
                    break;
                }
            }
            if (flag == 1) {
                break;
            }
        }

    }

    /**Fonte personalizada para o texto.
     *  
     * @param fontPath - String com o caminho para a fonte. 
     */
    public void addFont(String fontPath) {
        try {
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(65f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

        } catch (FontFormatException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro com o arquivo da nova fonte.", "Erro", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**Desenha o painel lateral.
     *
     * @param g - Próprio do java, para os métodos de desenho.
     * @param stageName - String com o nome da fase.
     */
    public void drawSidePanel(Graphics g, String stageName) {
        addFont("font\\game_over.ttf");
        g.setFont(customFont);
        g.setColor(Color.WHITE);
        g.drawString(totalLines + "", 438, 328);
        g.drawString(level + "", 438, 445);
        g.drawString(score + "", 438, 562);

        if ("ZEN  TEEMO".equals(stageName)) {
            g.drawString(stageName + "", 403, 672);
        } else {
            g.drawString(stageName + "", 398, 672);
        }
    }

    /**Reseta o painel lateral.
     */
    public void resetSidePanel() {
        this.totalLines = 0;
        this.score = 0;
        auxScore = score;
        this.level = 1;
        this.linesCompleted = 0;
    }

    /**Aumenta o score a partir do número de linhas.
     *
     * @param type - Número de linhas que foram limpas.
     */
    public void addScore(int type) {
        switch (type) {
            //empilhar uma peca
            case 1:
                auxScore++;
                this.score++;
                break;
            //completar linhas    
            case 2:
                //Progressão geométrica
                int a1 = 50;
                int q = 4;
                int lineScore;
                lineScore = (int) (a1 * Math.pow(q, linesCompleted - 1));
                this.score += lineScore;
                auxScore += lineScore;
                break;

        }
        while (auxScore >= 1000) {
            normalUp = currentShape.levelUp();
            this.level++;
            auxScore -= 1000;
        }

    }
    
    /**Para o loop do jogo.
     */
    public void stopGame() {
        resetSidePanel();

        for (int[] panel1 : panel) {
            for (int col = 0; col < panel1.length; col++) {
                panel1[col] = 0;
            }
        }
        looper.stop();
    }

    /**Classe aninhada que fica repetindo um loop. Realiza um update constante,
    * checa linhas completas para pontuação, e fica redesenhando todos objetos.
    */
    class GameLooper implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            update();

            linesCompleted = currentShape.checkLine(matrixObstacles);
            if (linesCompleted != 0) {
                addScore(2);
                totalLines += linesCompleted;
                linesCompleted = 0;
            }
            repaint();
        }

    }

    @SuppressWarnings("unchecked")

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**Eventos de tecla e tratamento dos mesmos; dentro de um dos eventos ocorre 
     * a serialização do projeto.
     */   
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            currentShape.rotateShape();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentShape.setDeltaX(1);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            currentShape.setDeltaX(-1);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.speedUp();
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) //jogar novamente a mesma fase
        {

            if (gameOver) {
                BackgroundElement bg = new BackgroundElement();
                bg.drawStaticImage(this.getGraphics(), background);
                startGame();
            }

        }
        if (e.getKeyCode() == KeyEvent.VK_Q) //Quit and save (falta a parte do save)
        {

            musicStage.stop();
            frame.dispose();
            frame.setVisible(false);

            PrincipalScreen mainScreen = new PrincipalScreen();
            mainScreen.showMainScreen();

            if (!gameOver) { //se for durante o jogo
                //salva o jogo
                saveGame = true;
                try {

                    FileOutputStream fs = new FileOutputStream("save.dat");
                    ObjectOutputStream os = new ObjectOutputStream(fs);
                    os.writeObject(this.score);
                    os.writeObject(this.totalLines);
                    os.writeObject(this.stage.getStageName());
                    os.writeObject(this.level);
                    os.writeObject(this.panel);
                    os.writeObject(this.matrixObstacles);
                    os.writeObject(currentShape.getCoords());
                    os.writeObject(currentShape.getColor());
                    os.writeObject(nextShape.getCoords());
                    os.writeObject(nextShape.getColor());
                    fs.flush();

                    fs.close();

                    os.flush();

                    os.close();
                    //JOptionPane.showMessageDialog(null, "gravou é tetra", "Erro", JOptionPane.INFORMATION_MESSAGE);
                    // os.writeInt();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                for (int a = 0; a < panelHeight; a++) {
                    for (int b = 0; b < panelWidth; b++) {
                        matrixObstacles[a][b] = 0;
                    }
                }
            } else {
                gameOverSound.stop();
                for (int a = 0; a < panelHeight; a++) {
                    for (int b = 0; b < panelWidth; b++) {
                        matrixObstacles[a][b] = 0;
                    }
                }

            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentShape.speedDown();
        }
    }

}
