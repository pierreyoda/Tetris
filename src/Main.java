import control.TetrisController;
import model.TetrisModel;
import view.TetrisView;
import view.TetrisWindow;

public class Main {

	public static void main(String[] args) {
		TetrisModel model = new TetrisModel();
		TetrisController controller = new TetrisController(model);
		TetrisView view = new TetrisView(model, controller);
		TetrisWindow window = new TetrisWindow(view);

		model.setView(view);
		model.initGame();
		window.startGame();
	}

}
