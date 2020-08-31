package stage;
//import Element.BackgroundElement;

/**Classe feita para controlar os estágios, tendo controle sobre a música, game
 * over, background, obstáculos e outros detalhes das duas fases.
 * 
 * @author Grupo 1
 */

public class Stage {

    protected boolean haveObstacles;
    protected String stageName;
    protected String frameTitle;

    protected String gameOverImagePath;
    protected String tilesPath;
    protected String musicStagePath;
    protected String gameOverMusicPath;
    
    /**Construtor da classe Stage.
     *
     * @param stageName - Recebe uma string sendo o nome do nível.
     * @param frameTitle - Recebe uma string sendo o nome da janela.
     * @param haveObstacles - Recebe um boolena se o nível tem ou não obstáculos.
     */
    public Stage(String stageName, String frameTitle, boolean haveObstacles) {
        this.stageName = stageName;
        this.frameTitle = frameTitle;
        this.haveObstacles = haveObstacles;

    }

    public void setStageName(String name) {
        this.stageName = name;
    }

    public String getStageName() {
        return this.stageName;
    }

    public void setMusicStagePath(String musicPath) {
        this.musicStagePath = musicPath;
    }

    public String getMusicStagePath() {
        return this.musicStagePath;
    }

    public String getFrameTitle() {
        return this.frameTitle;
    }

    public void setFrameTitle(String frameTitle) {
        this.frameTitle = frameTitle;
    }

    public final void sethaveObstacles(boolean haveObstacles) {
        this.haveObstacles = haveObstacles;
    }

    public final boolean gethaveObstacles() {
        return this.haveObstacles;
    }

    public String getGameOverImagePath() {
        return this.gameOverImagePath;
    }

    public void setGameOverImagePath(String path) {
        this.gameOverImagePath = path;
    }

    public String getTilePath() {
        return this.tilesPath;
    }
    
    public void setTilePatch(String path)
    {
        this.tilesPath = path;
    }
    
    public String getGameOverMusicPath() {
        return this.gameOverMusicPath;
    }
    
    public void setGameOverMusicPath(String musicPath) {
        this.gameOverMusicPath = musicPath;
    }


}
