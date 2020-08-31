package Element;


import control.GameScreen;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import stage.Stage;

/**Classe que controla os elementos de fundo de todo jogo.
 *
 * @author Grupo 1
 */
public class BackgroundElement extends Element {

    public static final String TEEMO_PATH = "imgs/ZenTeemo.jpg";
    public static final String VEIGAR_PATH = "imgs/BossVeigar.jpg";
    public static final String OBSTACLE_PATH = "imgs/obstacle.jpg";
   
    public BackgroundElement() {
        super();

    }

    /**Método que desenha a grid onde ocorre o jogo.
     *
     * @param g - Próprio do java para funções.
     * @param height - Altura da grid do jogo.
     * @param width - Largura da grid do jogo.
     * @param blockSize - Tamanho de cada quadrado do painel.
     */
    public void drawGrid(Graphics g, int height, int width, int blockSize) {
        Graphics2D grid = (Graphics2D) g;

        //espessura da linha
        grid.setStroke(new BasicStroke(1));

        //cor da linha
        grid.setColor(new Color(0, 150, 0, 100));

        //desenha linhas horizontais
        for (int i = 0; i <= height; i++) {
            grid.drawLine(25, blockSize * (i + 2), width * blockSize + 23, blockSize * (i + 2));
        }

        //desenha linhas verticais
        for (int j = 0; j <= width + 1; j++) {
            grid.drawLine(j * blockSize - 6, 60, j * blockSize - 6, height * 30 + 60);

        }
    }

    /**Desenha uma imagem no fundo.
     * 
     * @param g - Próprio do java.
     * @param background - Imagem do fundo.
     */
    public void drawStaticImage(Graphics g, BufferedImage background) {
        g.drawImage(background, 0, 0, null);

    }

    public void drawAnimation(Graphics g, Image background) {
        g.drawImage(background, 0, 0, null);

    }
    
  
    
    /**Função que desenha os obstáculos (caso exista).
     *
     * @param g - Próprio do java para funções.
     * @param height - Altura da grid do jogo.
     * @param width - Largura da grid do jogo. 
     * @param stage - Objeto do nível atual.
     * @return retorna a matriz com os obstáculos para serem desenhados.
     */
    public final int[][] drawObstacles(Graphics g, int height, int width, Stage stage) {
        BufferedImage blockImage;
        int[][] obstacles;
        if(stage.getStageName().equals("BOSS VEIGAR"))
            obstacles = new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,1,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {1,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,1,1},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
            };
       else
            obstacles = new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
            };
        
        try {
          //  JOptionPane.showMessageDialog(null, "chegou", "debug", JOptionPane.INFORMATION_MESSAGE);
            blockImage = ImageIO.read(new File(BackgroundElement.OBSTACLE_PATH));
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                if (obstacles[row][col] != 0) {
                    g.drawImage(blockImage, (col+1)*30-5, (row+2)*30, null);
                }
            }
        }
            
         
            

        } catch (IOException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obstacles;
    }

    /**Classe que carrega os arquivos de áudio do jogo.
     *
     * @param path - Caminho para os arquivos de áudio.
     * @return retorna o clip de áudio aberto.
     */
    public static Clip LoadSound(String path){
        try {
            File file = new File(path);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            return clip;
        } catch (UnsupportedAudioFileException | IOException| LineUnavailableException e) {
            JOptionPane.showMessageDialog (null, "Ocorreu um erro no momento de abrir o arquivo de audio.", "Erro", JOptionPane.INFORMATION_MESSAGE);
        }
        return null;
    }
    

}
