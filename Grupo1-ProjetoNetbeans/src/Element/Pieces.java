package Element;

import control.GameScreen;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.JOptionPane;

/**Classe de peças, com as váriaveis das características das mesmas.
 *
 * @author Grupo 1
 */
public class Pieces extends Element implements Serializable{

    private int color;
    private int x, y;
    private long time, lastTime;
    private int normal = 600;
    private static int delay, auxDelay;
    private BufferedImage block;
    private int[][] coords;
    private int[][] reference;
    private int deltaX;
    private GameScreen board;
    private boolean collision = false, moveX = false;

    /**Construtor sem parametros*/
    public Pieces() {
        super();
        this.setTransposable(false);
    }

    /**Construtor da classe peça.
     *
     * @param coords - Coordenadas da peça.
     * @param block - Imagem do bloco que monta a peça.
     * @param board - GameScreen do jogo.
     * @param color - Cor da peça sendo construida.
     */
    public Pieces(int[][] coords, BufferedImage block, GameScreen board, int color) {
        this.coords = coords;
        this.block = block;
        this.board = board;
        this.color = color;
        deltaX = 0;
        x = 4;
        y = 0;
        delay = normal;
        time = 0;
        lastTime = System.currentTimeMillis();
        reference = new int[coords.length][coords[0].length];

        System.arraycopy(coords, 0, reference, 0, coords.length);

    }

    /**Outro construtor da Peça, usado quando o jogador avança de nível.
     *
     * @param coords - Coordenadas da peça.
     * @param block - Imagem do bloco que monta as peças.
     * @param board - GameScreen do jogo.
     * @param color - Cor da peça.
     * @param normalUp - Aumento da velocidade.
     */
    public Pieces(int[][] coords, BufferedImage block, GameScreen board, int color, int normalUp) {
        this.coords = coords;
        this.block = block;
        this.board = board;
        this.color = color;
        deltaX = 0;
        x = 4;
        y = 0;
        normal = normalUp;
        delay = normal;
        time = 0;
        lastTime = System.currentTimeMillis();
        reference = new int[coords.length][coords[0].length];

        System.arraycopy(coords, 0, reference, 0, coords.length);

    }

    /**Método de update da tela, checa se alguma linha foi completa,e checa também se a peça pode se mover no eixo x ou y. 
     *
     * @param matrixObstacles - Matriz com os obstaculos.
     * @return Retorna o número de linhas completas.
     */
    public int update(int[][] matrixObstacles) {
        int linesCompleted = 0;
        moveX = true;
        time += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        if (collision) {
            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[0].length; col++) {
                    if (coords[row][col] != 0) {
                        board.getPanel()[y + row][x + col] = color;
                    }
                }
            }
            linesCompleted = checkLine(matrixObstacles);
            board.addScore(1);
            board.setCurrentShape();
        }

        if (!(x + deltaX + coords[0].length > 11) && !(x + deltaX < 0)) {

            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[row].length; col++) {
                    if (coords[row][col] != 0) {
                        if (board.getPanel()[y + row][x + deltaX + col] != 0 || matrixObstacles[y + row][x + deltaX + col] == 1) {
                            //    JOptionPane.showMessageDialog(null, "chegou", "Erro", JOptionPane.INFORMATION_MESSAGE);
                            moveX = false;
                        }

                    }
                }
            }
            if (moveX) {
                x += deltaX;
            }
        }
        if (!(y + 1 + coords.length > 21)) {

            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[row].length; col++) {
                    if (coords[row][col] != 0) {
                        if (board.getPanel()[y + 1 + row][x + col] != 0 || matrixObstacles[y + 1 + row][x + col] == 1) {
                            //JOptionPane.showMessageDialog(null, "chegou", "Erro", JOptionPane.INFORMATION_MESSAGE);
                            collision = true;
                        }
                    }
                }
            }
            if (time > delay) {
                y++;
                time = 0;
            }
        } else {
            collision = true;
        }
        deltaX = 0;
        return linesCompleted;
    }
    
    /**Método que checa se as linhas estão completas, descendo toda a linha superior caso
     * a inferior esteja completa.
     * 
     * @param matrixObstacles - Matriz com as peças fixas no tabuleiro.
     * @return retorna o número de linhas removidas para pontuação.
    */
    public int checkLine(int[][] matrixObstacles) {
        int size = board.getPanel().length - 1;
        int lines = 0;
        int removedLines = 0;
        int flag = 0;
        int flag2 = 0;
        int sentinel = 0;
        for (int i = board.getPanel().length - 1; i > 0; i--) {
            int count = 0;
            for (int j = 0; j < board.getPanel()[0].length; j++) {
                for (int k = size; k < board.getPanel().length; k++) {
                    if (matrixObstacles[k][j] == 1) {
                        flag = 1;
                    }
                }
                if (board.getPanel()[i][j] != 0 || matrixObstacles[i][j] != 0) {
                    count++;
                    if (matrixObstacles[i][j] == 1) {
                        flag2 = 1;
                    }
                }
                if (flag2 == 0 && count == 11) {
                    sentinel = 1;
                } else if (count==11) {
                    sentinel = 0;
                }
                if (matrixObstacles[size][j] == 0 && flag == 0 || matrixObstacles[size][j] == 0 && sentinel == 1) {
                    board.getPanel()[size][j] = board.getPanel()[i][j];
                }
                flag = 0;

            }

            //    JOptionPane.showMessageDialog(null, "count = " + count + "linha : " + i, "Erro", JOptionPane.INFORMATION_MESSAGE);
            flag2 = 0;
            if (count < board.getPanel()[0].length) {

                size--;
                lines++;
            }

        }
        removedLines = 20 - lines;
        //  JOptionPane.showMessageDialog(null, "Delay : " + delay, "Erro", JOptionPane.INFORMATION_MESSAGE);
        return removedLines;
    }

    /**Desenha a peça.*/
    public void render(Graphics g) {

        for (int row = 0; row < coords.length; row++) {
            for (int col = 0; col < coords[0].length; col++) {
                if (coords[row][col] != 0) {
                    g.drawImage(block, col * 30 + (x + 1) * 30 - 6, row * 30 + (y + 2) * 30 + 1, null);
                }
            }
        }

    }

    /**Realiza a rotação da peça, certificando que ela é possível.*/
    public void rotateShape() {

        int[][] rotatedShape;

        rotatedShape = RotatedMatrix(coords);

        if ((x + rotatedShape[0].length > 11) || (y + rotatedShape.length > 21)||(x + rotatedShape[0].length < 3)) {
            return;
        }

        for (int row = 0; row < rotatedShape.length; row++) {
            for (int col = 0; col < rotatedShape[row].length; col++) {
                if (rotatedShape[row][col] != 0) {
                    if (board.getPanel()[y + row][x + col] != 0) {
                        return;
                    }
                }
            }
        }
        coords = rotatedShape;
    }

    /**Cria a matriz que serve como parâmetro para ver se a peça pode ser rodada ou não.
     * 
     * @param matrix - Recebe a peça que se deseja rodar.
     * @return Retorna a peça rodada, para ser comparada se pode ser rodada ou não.
     */
    private int[][] RotatedMatrix(int[][] matrix) {
        int l = matrix.length;
        int h = matrix[0].length;
        int[][] ret = new int[h][l];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < l; j++) {
                ret[i][j] = matrix[l - j - 1][i];
            }
        }
        return ret;
    }

    public int[][] getCoords() {
        return coords;
    }

    public int getColor() {
        return color;
    }

    public BufferedImage getBlock() {
        return block;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public void speedUp() {
        delay = 35;
    }

    public void speedDown() {
        delay = normal;
    }

    public int levelUp() {
        normal = normal - (3 * normal / 10);
        delay = normal;
        return normal;
    }
}
