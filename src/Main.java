import control.TetrisController;
import model.TetrisModel;
import view.TetrisView;
import view.TetrisWindow;
import view.screens.MainMenuScreen;

public class Main {

	public static void main(String[] args) {
		TetrisModel model = new TetrisModel();
		TetrisController controller = new TetrisController(model);
		MainMenuScreen menuScreen = new MainMenuScreen(controller);

		TetrisView view = new TetrisView(menuScreen);
		TetrisWindow window = new TetrisWindow(view);

		window.displayGame();
	}

}
