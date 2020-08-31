package control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import Element.BackgroundElement;
import Element.Pieces;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import stage.Stage;

/**Classe do menu principal do jogo, tendo um KeyListener para registrar a opção
 * selecionada no menu do jogo.
 * 
 * @author Grupo 1
 */
public class PrincipalScreen implements KeyListener {

    public static final int SCREEN_HEIGHT = 736;
    public static final int SCREEN_WIDTH = 960;

    private final JFrame mainScreen;
    private final String title;
    private final String path;
    private final Clip openingMusic;

    private boolean visited_N;

    protected ImageIcon screenImage;
    protected JLabel label;

    /**Construtor da classe PrincipalScreen. Não Recebe nenhum parâmetro, já que
     * as imagens e a música do menu são fixas e sempre a mesma.
     * 
     * O menu contém uma openingMusic que fica em loop, um gif para a imagem de
     * fundo que é adcionado no jframe, e os atributos da janela.
     *
     */
    public PrincipalScreen() {
        openingMusic = BackgroundElement.LoadSound("music\\arcade_music.wav");
        openingMusic.start();
        openingMusic.loop(Clip.LOOP_CONTINUOUSLY);
        this.visited_N = false;
        this.title = "Tetris Legends - Home";
        this.path = "imgs/TelaInicialPisca.gif";
        mainScreen = new JFrame(title);
        mainScreen.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        mainScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainScreen.setLocationRelativeTo(null);
        mainScreen.setResizable(false);
        addImage(path);
    }

    public void changeScreen(String path, String title) {

        mainScreen.remove(this.label);
        addImage(path);
        mainScreen.setTitle(title);

    }

    public void showMainScreen() {
        mainScreen.pack();
        mainScreen.setVisible(true);
        mainScreen.setFocusable(true);
        mainScreen.requestFocusInWindow();
        mainScreen.addKeyListener(this);
    }

    public final void addImage(String path) {

        this.screenImage = new ImageIcon(path);
        label = new JLabel(this.screenImage);
        mainScreen.getContentPane().add(label);
    }

    /**Método responsável pelos eventos de tecla, sendo eles N para novo jogo,
     * mudando para a tela com os botões das 2 fases disponíveis, ou L para 
     * carregar um jogo salvo a partir de um arquivo "save.dat".
     * 
     * @param e - O evento de tecla pressionada para as escolhas do usuário.
    */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_N: //Novo Game  
                visited_N = true;
                changeScreen("imgs/TelaFasesPisca.gif", "Tetris Legends - Choose the Stage");
                mainScreen.setVisible(true);
                break;

            case KeyEvent.VK_L: //Load Game

                try {
                    File arquivo = new File("save.dat");
                    if (arquivo.exists()) {
                        FileInputStream fs = new FileInputStream("save.dat");
                        ObjectInputStream os = new ObjectInputStream(fs);
                        int score = (int) os.readObject();
                        int totalLines = (int) os.readObject();
                        String stage = (String) os.readObject();
                        int level = (int) os.readObject();
                        int[][] panel = (int[][]) os.readObject();

                        int[][] obstacles = (int[][]) os.readObject();
                        int[][] coordCurrent = (int[][]) os.readObject();
                        int colorCurrent = (int) os.readObject();
                        int[][] coordNext = (int[][]) os.readObject();
                        int colorNext = (int) os.readObject();
                        fs.close();

                        os.close();
                        if (stage.equals("ZEN  TEEMO")) {
                            openingMusic.stop();
                            mainScreen.removeKeyListener(this);
                            Stage zenTeemo = new Stage("ZEN  TEEMO", "Tetris Legends - Zen Teemo Stage", false);
                            zenTeemo.setGameOverImagePath("imgs/GameOverTeemo.gif");
                            zenTeemo.setMusicStagePath("music\\astro_teemo_music.wav");
                            zenTeemo.setGameOverMusicPath("music\\gameOver_ZenTeemo.wav");
                            zenTeemo.setTilePatch("imgs/tiles_Teemo.png");
                            GameScreen gamePanelZ = new GameScreen(BackgroundElement.TEEMO_PATH, zenTeemo, mainScreen, panel, obstacles, totalLines,
                                    score, level, coordCurrent, colorCurrent, coordNext, colorNext);
                            gamePanelZ.loadGame(label);
                        } else {
                            openingMusic.stop();
                            mainScreen.removeKeyListener(this);
                            Stage bossVeigar = new Stage("BOSS VEIGAR", "Tetris Legends - Boss Veigar Stage", true);
                            bossVeigar.setGameOverImagePath("imgs/GameOverVeigar.gif");
                            bossVeigar.setMusicStagePath("music\\final_boss_veigar_music.wav");
                            bossVeigar.setGameOverMusicPath("music\\gameOver_BossVeigar.wav");
                            bossVeigar.setTilePatch("imgs/tiles_Veigar.png");
                            GameScreen gamePanelB = new GameScreen(BackgroundElement.VEIGAR_PATH, bossVeigar, mainScreen, panel, obstacles, totalLines,
                                    score, level, coordCurrent, colorCurrent, coordNext, colorNext);
                            gamePanelB.loadGame(label);
                        }
                    } else {
                        changeScreen("imgs/TelaLoadErro.gif", "Tetris Legends - No saved game found");
                        mainScreen.setVisible(true);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                break;

            case KeyEvent.VK_C: //Come back
                visited_N = false;
                changeScreen("imgs/TelaInicialPisca.gif", "Tetris Legends - Home");
                mainScreen.setVisible(true);
                break;

            case KeyEvent.VK_Z: // Fase Zen Teemo

                if (visited_N) {
                    openingMusic.stop();
                    mainScreen.removeKeyListener(this);
                    Stage zenTeemo = new Stage("ZEN  TEEMO", "Tetris Legends - Zen Teemo Stage", false);
                    zenTeemo.setGameOverImagePath("imgs/GameOverTeemo.gif");
                    zenTeemo.setMusicStagePath("music\\astro_teemo_music.wav");
                    zenTeemo.setGameOverMusicPath("music\\gameOver_ZenTeemo.wav");
                    zenTeemo.setTilePatch("imgs/tiles_Teemo.png");
                    GameScreen gamePanelZ = new GameScreen(BackgroundElement.TEEMO_PATH, zenTeemo, mainScreen);
                    gamePanelZ.startTetris(label);
                }

                break;

            case KeyEvent.VK_B: // Fase Boss Veigar

                if (visited_N) {
                    openingMusic.stop();
                    mainScreen.removeKeyListener(this);
                    Stage bossVeigar = new Stage("BOSS VEIGAR", "Tetris Legends - Boss Veigar Stage", true);
                    bossVeigar.setGameOverImagePath("imgs/GameOverVeigar.gif");
                    bossVeigar.setMusicStagePath("music\\final_boss_veigar_music.wav");
                    bossVeigar.setGameOverMusicPath("music\\gameOver_BossVeigar.wav");
                    bossVeigar.setTilePatch("imgs/tiles_Veigar.png");
                    GameScreen gamePanelB = new GameScreen(BackgroundElement.VEIGAR_PATH, bossVeigar, mainScreen);
                    gamePanelB.startTetris(label);
                }

                break;

        }

    }

    @SuppressWarnings("unchecked")

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
