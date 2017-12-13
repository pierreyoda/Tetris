import control.TetrisController;
import model.TetrisModel;
import view.TetrisView;
import view.TetrisWindow;
import view.screens.GameScreen;

public class Main {

	public static void main(String[] args) {
		TetrisModel model = new TetrisModel();
		TetrisController controller = new TetrisController(model);
		GameScreen gameScreen = new GameScreen(controller);

		TetrisView view = new TetrisView(gameScreen);
		TetrisWindow window = new TetrisWindow(view);

		window.displayGame();
	}

}
