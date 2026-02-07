import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;


public class MyFrame extends JFrame {

	private JLabel imageLabel;
	private Clip clip;

	private int width = 800;
	private int height = 800;
	private int x = 0, y = 0;
	private int xStep = 4, yStep = 4;

	public MyFrame() {
		setLayout(null);
		setResizable(false);
		setUndecorated(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.width;
		height = screenSize.height;

		setSize(width, height);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(null);
		contentPanel.setBackground(Color.BLACK);
		contentPanel.setBounds(0, 0, width, height);
		add(contentPanel);

		BufferedImage image;

		try {
			image = ImageIO.read(new File("image.png"));
		} catch (IOException e) {
			System.out.println("Image");
			System.out.println(e.getMessage());
			return;
		}

		AudioInputStream audioIn;
		try {
			BufferedInputStream bufferedIn = new BufferedInputStream(getClass().getResourceAsStream("/sound.wav"));
			audioIn = AudioSystem.getAudioInputStream(bufferedIn);
		} catch (Exception e) {
			System.out.println("AudioInputStream");
			System.out.println(e.getMessage());
			return;
		}

		try {
			clip = AudioSystem.getClip();
			clip.addLineListener(event -> {
				if (event.getType() == LineEvent.Type.STOP) {
					clip.stop();
					clip.setFramePosition(0);
				}
			});
			clip.open(audioIn);
		} catch (LineUnavailableException | IOException e) {
			System.out.println("Clip");
			System.out.println(e.getMessage());
			return;
		}

		try {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log(0.25f) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
		} catch (Exception e) {
			System.out.println("Volume");
			System.out.println(e.getMessage());
			return;
		}

		Image scaled = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		imageLabel = new JLabel(new ImageIcon(scaled));
		imageLabel.setBounds(x, y, 200, 200);
		contentPanel.add(imageLabel);

		Timer timer = new Timer(1000 / 60, e -> update());
		timer.start();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void update() {
		x += xStep;
		y += yStep;

		if ((x == 0 && y == 0) || (x + imageLabel.getWidth() == width && y + imageLabel.getHeight() == height) || (
				x + imageLabel.getWidth() == width && y == 0) || (x == 0 && y + imageLabel.getHeight() == height)) {
			clip.start();
		}

		int xOver = x + imageLabel.getWidth() - width;
		if (xOver > 0) {
			x = width - imageLabel.getWidth();
			x -= xOver;
			xStep = -xStep;
		}
		int yOver = y + imageLabel.getHeight() - height;
		if (yOver > 0) {
			y = height - imageLabel.getHeight();
			y -= yOver;
			yStep = -yStep;
		}
		if (x < 0) {
			x = -x;
			xStep = -xStep;
		}
		if (y < 0) {
			y = -y;
			yStep = -yStep;
		}

		imageLabel.setLocation(x, y);
	}

}