import javax.swing.*;
import java.awt.*;


public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			SwingUtilities.invokeLater(MyFrame::new);
		} else {
			for (String arg : args) {
				switch (arg) {
					case "--test", "-t":
						Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
						int W = screenSize.width - 200;
						int H = screenSize.height - 200;
						int x = 0;
						int y = 0;
						int xs = 4;
						int ys = 4;
						int i = 0;
						boolean touched = false;
						while (!touched) {
							x += xs;
							y += ys;

							if (x == 0 && y == 0) {
								touched = true;
							}
							if (x == W && y == H) {
								touched = true;
							}
							if (x == 0 && y == H) {
								touched = true;
							}
							if (x == W && y == 0) {
								touched = true;
							}
							if (x == W || x == 0) {
								xs = -xs;
								i++;
							}
							if (y == H || y == 0) {
								ys = -ys;
								i++;
							}
						}
						System.out.printf("Calculating for resolution: %sx%s\n", W + 200, H + 200);
						System.out.printf("Number of hits: %s\n", i);
						break;
				}
			}
		}
	}

}