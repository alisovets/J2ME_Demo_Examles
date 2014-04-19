import java.io.IOException;
import java.util.TimeZone;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

/**
 * performs drawing of the game field and game objects.
 * 
 * @author Alexander Lisovets 2007
 * 
 */
public class GameItemDrawer {
	private final static int CELL_BORDER_SIZE = 2;
	private final static String PATH_TO_BG_IMAGE = "imgs/ber.jpg";
	private final static String NEXT_CAPTION = "next:";
	private final static String SCORE_CAPTION = "score:";
	private final static String IT_IS_HIGHER_STRING = "It is the higher!";
	private final static String YOU_SCORE_IS_STRING = "Your score is:";
	private final static String YOU_HIGHER_SCORE_IS_STRING = "Your higher score is:";

	private final static byte BG_COLOR = 7;
	private final static int WHITE_COLOR = 0xffffff;
	private final static int GREY_COLOR = 0x505050;
	private final static int NORMAL_INTENSITY = 210;
	private final static int HIGH_INTENSITY = 255;
	private final static int LOW_INTENSITY = 120;

	// Matrixes for letters
	private static final byte[][] LETTER_C = { { 1, 1, 1 }, { 1 }, { 1 },
			{ 1 }, { 1, 1, 1 } };
	private static final byte[][] LETTER_L = { { 1 }, { 1 }, { 1 }, { 1 },
			{ 1, 1, 1 } };
	private static final byte[][] LETTER_A = { { 1, 1, 1 }, { 1, 0, 1 },
			{ 1, 1, 1 }, { 1, 0, 1 }, { 1, 0, 1 } };
	private static final byte[][] LETTER_S = { { 1, 1, 1 }, { 1 }, { 1, 1, 1 },
			{ 0, 0, 1 }, { 1, 1, 1 } };
	private static final byte[][] LETTER_I = { { 1 }, {}, { 1 }, { 1 },
			{ 1, 1, 1 } };
	private static final byte[][] LETTER_T = { { 1, 1, 1 }, { 0, 1 }, { 0, 1 },
			{ 0, 1 }, { 0, 1 } };
	private static final byte[][] LETTER_E = { { 1, 1, 1 }, { 1 }, { 1, 1 },
			{ 1 }, { 1, 1, 1 } };
	private static final byte[][] LETTER_R = { { 1, 1, 1 }, { 1, 0, 1 },
			{ 1, 1 }, { 1, 0, 1 }, { 1, 0, 1 } };
	private static final int TIMEZONE_OFFSET = TimeZone.getDefault()
			.getRawOffset();

	private int w; // width of the screen
	private int h; // height of the screen
	private int horisontalCellNumber;
	private int totalHorisontalCellNumber;
	private int verticalCellNumber;
	private int cellSize; // item size
	private int marginX; // margin
	private int marginY;
	private int nextY; // a vertical position of the Next item field
	private int scoreY; // a vertical position of the Score field
	private int clockX, clockY, clockR; // clock position
	private Image bgImg; // a background image on the game field
	private byte[][] gameField; // array to store information about the
								// placement
								// of objects on the game field

	/**
	 * Creates GameItemDrawer object
	 * 
	 * @param gameCanvas
	 * @param graphics
	 * @param pGameFld
	 *            - array to store information about the placement of objects on
	 *            the game field
	 */
	GameItemDrawer(GameCanvas gameCanvas, Graphics graphics, byte[][] pGameFld) {
		w = gameCanvas.getWidth();
		h = gameCanvas.getHeight();
		gameField = pGameFld;
		horisontalCellNumber = gameField.length;
		verticalCellNumber = gameField[0].length;
		totalHorisontalCellNumber = horisontalCellNumber + TetrisCanvas.ITEM_SIZE;

		cellSize = (h + CELL_BORDER_SIZE * 2) / verticalCellNumber;
		if (cellSize > w / (totalHorisontalCellNumber + 1))
			cellSize = w / (totalHorisontalCellNumber + 1);

		marginX = (w - cellSize * totalHorisontalCellNumber) / 3;
		marginY = (h - cellSize * verticalCellNumber) / 2;

		Font font = graphics.getFont();
		int fontHeight = font.getHeight();
		nextY = fontHeight + CELL_BORDER_SIZE;
		scoreY = nextY + cellSize * TetrisCanvas.ITEM_SIZE + fontHeight;
		clockInit(fontHeight);

		try {
			bgImg = reSizeImg(
					Image.createImage(getClass().getResourceAsStream(
							PATH_TO_BG_IMAGE)),
					cellSize * horisontalCellNumber, cellSize
							* verticalCellNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bgImg = reSizeImg(bgImg, cellSize * horisontalCellNumber, cellSize
				* verticalCellNumber);

	}

	/*
	 * draws a square block with a volume effect x, y - position of the top left
	 * corner c - conventional color should be in the range 1..8
	 */
	private void showBlock(Graphics g, int x, int y, int color) {
		g.setColor(getRealColor(color, NORMAL_INTENSITY));
		g.fillRect(x + 1, y + 1, cellSize - CELL_BORDER_SIZE, cellSize
				- CELL_BORDER_SIZE);
		g.setColor(getRealColor(color, HIGH_INTENSITY));
		g.drawLine(x, y, x + cellSize - 1, y);
		g.drawLine(x, y + 1, x, y + cellSize - 1);

		g.setColor(WHITE_COLOR);
		g.drawLine(x + 1, y + 1, x + cellSize - 2, y + 1);
		g.drawLine(x + 1, y + 2, x + 1, y + cellSize - 1);

		g.setColor(GREY_COLOR);
		g.drawLine(x, y + cellSize - 1, x + cellSize - 2, y + cellSize - 1);
		g.drawLine(x + cellSize - 1, y, x + cellSize - 1, y + cellSize - 1);
	}

	/*
	 * draws or clears a square block in relative position on the game field
	 * with a volume effect xPosition, yPosition - relative position of the top
	 * left corner c - conventional color should be in the range 0..8 when c = 0
	 * clears the block
	 */
	private void showGameBloc(Graphics g, int xPosition, int yPosition,
			int color) {
		if (color == 0) {
			clearGameBloc(g, xPosition, yPosition);
		} else
			showBlock(g, marginX + xPosition * cellSize, marginY + yPosition
					* cellSize, color);

	}

	/*
	 * clears a square block in relative position on the game field restores
	 * background
	 */
	private void clearGameBloc(Graphics g, int xPos, int yPos) {
		if ((xPos < 0) || (yPos < 0) || (xPos > 9) || (yPos > 19))
			return;
		try {
			g.drawRegion(bgImg, xPos * cellSize, yPos * cellSize, cellSize,
					cellSize, Sprite.TRANS_NONE, marginX + xPos * cellSize,
					marginY + yPos * cellSize,
					javax.microedition.lcdui.Graphics.TOP
							| javax.microedition.lcdui.Graphics.LEFT);
		} catch (Exception e) {
			g.setColor(0);
			g.fillRect(marginX + xPos * cellSize, marginY + yPos * cellSize,
					cellSize, cellSize);
		}
	}

	/**
	 * draws a next item in the next item field
	 * 
	 * @param g
	 *            -Graphics
	 * @param nextItem
	 *            - a byte array with a map of the item
	 */
	void showNextItem(Graphics g, byte[][] nextItem) {
		g.setColor(getRealColor(BG_COLOR, 200));
		g.fillRect(w - marginX - cellSize * TetrisCanvas.ITEM_SIZE - 1, nextY
				+ CELL_BORDER_SIZE + 1, cellSize * TetrisCanvas.ITEM_SIZE, cellSize
				* (TetrisCanvas.ITEM_SIZE - 1));
		for (int i = 0; i < TetrisCanvas.ITEM_SIZE; i++)
			for (int j = 0; j < TetrisCanvas.ITEM_SIZE; j++)
				if (nextItem[i][j] != 0)
					showBlock(g, w - marginX - cellSize * (TetrisCanvas.ITEM_SIZE - i) - 1,
							nextY + cellSize * j + CELL_BORDER_SIZE + 1,
							BG_COLOR);
	}

	/**
	 * Draws a game item in the game field
	 * 
	 * @param g
	 * @param gameItem
	 *            - a byte array with a map of the item
	 * @param itemPositionX
	 *            a relative horizontal position of the item in the game field
	 * @param itemPositionY
	 *            a relative vertical position of the item in the game field
	 */
	void showGameItem(Graphics g, byte[][] gameItem, int itemPositionX,
			int itemPositionY) {
		// redraw nearby area to clean artifacts after the item was moved
		for (int i = itemPositionX - 1; i <= itemPositionX + TetrisCanvas.ITEM_SIZE; i++)
			for (int j = itemPositionY - 2; j <= itemPositionY + TetrisCanvas.ITEM_SIZE; j++) {
				if ((i < 0) || i >= horisontalCellNumber)
					continue;
				if ((j < 0) || (j >= verticalCellNumber))
					continue;
				showGameBloc(g, i, j, gameField[i][j]);
			}
		// draw the item
		for (int i = 0; i < TetrisCanvas.ITEM_SIZE; i++)
			for (int j = 0; j < TetrisCanvas.ITEM_SIZE; j++) {
				if (itemPositionY + j < 0)
					continue;
				if (gameItem[i][j] != 0) {
					showGameBloc(g, itemPositionX + i, itemPositionY + j,
							gameItem[i][j]);
				}
			}
	}

	/**
	 * returns a real rgb color from the conventional color and intensity color
	 * 
	 * @param color
	 *            - conventional color should be in the range 0..8 intensity -
	 *            should be in
	 * @param intensity
	 *            - should be in the range 0..255
	 * @return real rgb color
	 */
	int getRealColor(int color, int intensity) {
		intensity &= 255;
		return (color & 1) * intensity + (color & 2) * (intensity << 7)
				+ (color & 4) * (intensity << 14);
	}

	/**
	 * Scales an image to the specified size
	 * 
	 * @param img
	 *            - image
	 * @param width
	 * @param height
	 * @return the scaled image
	 */
	Image reSizeImg(Image img, int width, int height) {
		int imgWidth = img.getWidth();
		int imgHeight = img.getHeight();

		int[] rgbBuffer1 = new int[imgWidth * imgHeight];
		int[] rgbBuffer2 = new int[width * height];

		float factorX = (float) imgWidth / width;
		float factorY = (float) imgHeight / height;
		img.getRGB(rgbBuffer1, 0, imgWidth, 0, 0, imgWidth, imgHeight);
		for (int row = 0; row < height; row++)
			for (int i = 0; i < width; i++) {
				int k = 0;
				int rColor = 0;
				int gColor = 0;
				int bColor = 0;
				for (int x = (int) (factorX * i); ((x < factorX * (i + 1)) && (x < imgWidth)); x++) {
					for (int y = (int) (factorY * row); (y < factorY
							* (row + 1))
							&& (y < imgHeight); y++) {
						k++;
						rColor += (rgbBuffer1[y * imgWidth + x] & 0xff0000) >> 16;
						gColor += (rgbBuffer1[y * imgWidth + x] & 0xff00) >> 8;
						bColor += rgbBuffer1[y * imgWidth + x] & 0xff;
					}
				}
				rgbBuffer2[row * width + i] = ((rColor / k) << 16)
						+ ((gColor / k) << 8) + bColor / k;
			}
		return Image.createRGBImage(rgbBuffer2, width, height, false);
	}

	/**
	 * Draws the game screen
	 * 
	 * @param g
	 *            Graphics
	 */
	void showGameScreen(Graphics g) {
		g.setColor(getRealColor(BG_COLOR, 200));
		g.fillRect(0, 0, w, h);

		g.setColor(0);
		// draw the game field
		g.fillRect(marginX, marginY, cellSize * TetrisCanvas.GAME_FIELD_H_SIZE, cellSize * TetrisCanvas.GAME_FIELD_V_SIZE);
		showBorder(g, marginX, marginY, cellSize * TetrisCanvas.GAME_FIELD_H_SIZE, cellSize * TetrisCanvas.GAME_FIELD_V_SIZE, BG_COLOR);
		showBorder(g, w - marginX - cellSize * TetrisCanvas.ITEM_SIZE - 1, nextY
				+ CELL_BORDER_SIZE + 1, cellSize * TetrisCanvas.ITEM_SIZE, cellSize
				* (TetrisCanvas.ITEM_SIZE - 1), BG_COLOR);

		g.setColor(0xff);
		g.drawString(NEXT_CAPTION, w - marginX - cellSize * TetrisCanvas.ITEM_SIZE
				- CELL_BORDER_SIZE - 1, CELL_BORDER_SIZE, Graphics.TOP
				| Graphics.LEFT);
		g.drawString(SCORE_CAPTION, w - marginX - cellSize * TetrisCanvas.ITEM_SIZE
				- CELL_BORDER_SIZE - 1, nextY + cellSize * TetrisCanvas.ITEM_SIZE,
				Graphics.TOP | Graphics.LEFT);

		showGameField(g);
	}

	/**
	 * Draws the game field
	 * 
	 * @param g Graphics
	 */
	void showGameField(Graphics g) {

		g.drawImage(bgImg, marginX, marginY,
				javax.microedition.lcdui.Graphics.TOP
						| javax.microedition.lcdui.Graphics.LEFT);
		for (int i = 0; i < horisontalCellNumber; i++)
			for (int j = 0; j < verticalCellNumber; j++) {
				showGameBloc(g, i, j, gameField[i][j]);
			}
	}

	/**
	 * Shows a border of game fields
	 * 
	 * @param g
	 * @param x
	 *            the left position of the game field
	 * @param y
	 *            the top position of the game field
	 * @param width
	 * @param height
	 * @param color
	 *            conventional color should be in the range 0..8
	 */
	void showBorder(Graphics g, int x, int y, int width, int height, int color) {

		if (color > 0)
			g.setColor(getRealColor(color, LOW_INTENSITY));
		else
			g.setColor(getRealColor(-color, HIGH_INTENSITY));
		g.drawLine(x - 1, y - 1, x + width - 1, y - 1);
		g.drawLine(x - 1, y, x - 1, y + height);
		g.drawLine(x - 2, y - 2, x + width, y - 2);
		g.drawLine(x - 2, y - 1, x - 2, y + height + 1);
		if (color > 0)
			g.setColor(getRealColor(color, HIGH_INTENSITY));
		else
			g.setColor(getRealColor(-color, LOW_INTENSITY));
		g.drawLine(x - 1, y + height, x + width, y + height);
		g.drawLine(x + width, y - 1, x + width, y + height - 1);
		g.drawLine(x - 2, y + height + 1, x + width + 1, y + height + 1);
		g.drawLine(x + width + 1, y - 2, x + width + 1, y + height);

	}

	/**
	 * Shows the title screen
	 * 
	 * @param g
	 */
	void title(Graphics g) {
		int mainMarginX = marginX;
		int mainMarginY = marginY;
		int mainCellSize = cellSize;

		cellSize = w / 29;
		if (cellSize > h / 13)
			cellSize = h / 13;
		marginX = (int) ((w - cellSize * 29) / 2.0);
		marginY = (h - cellSize * 16) / 2;
		g.setColor(0);
		g.fillRect(0, 0, w, h);

		showLetter(LETTER_C, g, 1, 1, 6);

		showLetter(LETTER_L, g, 5, 1, 6);
		showLetter(LETTER_A, g, 9, 1, 6);
		showLetter(LETTER_S, g, 13, 1, 6);
		showLetter(LETTER_S, g, 17, 1, 6);
		showLetter(LETTER_I, g, 21, 1, 6);
		showLetter(LETTER_C, g, 25, 1, 6);
		marginX += cellSize / 2;
		showLetter(LETTER_T, g, 2, 7, 6);
		showLetter(LETTER_E, g, 6, 7, 6);
		showLetter(LETTER_T, g, 10, 7, 6);
		showLetter(LETTER_R, g, 14, 7, 6);
		showLetter(LETTER_I, g, 18, 7, 6);
		showLetter(LETTER_S, g, 22, 7, 6);

		marginX = mainMarginX;
		marginY = mainMarginY;
		cellSize = mainCellSize;
	}

	/*
	 * Show letter pX, pY - start position in blocs
	 */
	private void showLetter(byte[][] letter, Graphics g, int pX, int pY, int c) {
		for (int y = 0; y < letter.length; y++) {
			for (int x = 0; x < letter[y].length; x++) {
				if (letter[y][x] != 0) {
					showGameBloc(g, pX + x, pY + y, c);
				}
			}
		}
	}

	/*
	 * inits a clock position
	 */
	private void clockInit(int fontHeight) {
		clockX = marginX + cellSize * TetrisCanvas.GAME_FIELD_H_SIZE + 4;
		int clockX2 = w - 3;
		clockY = scoreY + 2 * fontHeight;
		int clockY2 = marginY + TetrisCanvas.GAME_FIELD_V_SIZE * cellSize;
		if (clockY2 > h - 1)
			clockY2 = h - 1;
		clockR = clockX2 - clockX;
		if (clockR > clockX2 - clockX)
			clockR = clockX2 - clockX;
		clockR /= 2;
		clockX = clockX + clockR;
		clockY = clockY2 - clockR;
	}

	/**
	 * Draws clock hands
	 * 
	 * @param g
	 * @param timeStamp
	 */
	void showTime(Graphics g, long timeStamp) {
		int th = (int) (((timeStamp + TIMEZONE_OFFSET) / 1000) % 86400);
		int s = th % 60;
		th /= 60;
		int m = th % 60;

		g.setColor(getRealColor(BG_COLOR, 200));
		g.fillArc(clockX - clockR + 6, clockY - clockR + 6, 2 * clockR - 12,
				2 * clockR - 12, 0, 360);

		float al = (float) (8.727E-3 * th);
		int khx = (int) (Math.sin(al) * clockR * 0.4);
		int khy = (int) (Math.cos(al) * clockR * 0.4);
		int c = 1;
		g.setColor(getRealColor(c, 255));
		g.fillTriangle(clockX + khx, clockY - khy, clockX - khy / 4, clockY
				- khx / 4, clockX + khy / 4, clockY + khx / 4);
		g.fillTriangle(clockX - khx / 3, clockY + khy / 3, clockX - khy / 4,
				clockY - khx / 4, clockX + khy / 4, clockY + khx / 4);
		g.setColor(0);
		g.drawLine(clockX + khx, clockY - khy, clockX - khy / 5, clockY - khx
				/ 5);
		g.drawLine(clockX + khx, clockY - khy, clockX + khy / 5, clockY + khx
				/ 5);
		g.drawLine(clockX - khx / 3, clockY + khy / 3, clockX - khy / 5, clockY
				- khx / 5);
		g.drawLine(clockX - khx / 3, clockY + khy / 3, clockX + khy / 5, clockY
				+ khx / 5);
		al = (float) (0.1047 * m);
		khx = (int) (Math.sin(al) * clockR * 0.7);
		khy = (int) (Math.cos(al) * clockR * 0.7);
		g.setColor(getRealColor(c, 255));
		g.fillTriangle(clockX + khx, clockY - khy, clockX - khy / 8, clockY
				- khx / 8, clockX + khy / 8, clockY + khx / 8);
		g.fillTriangle(clockX - khx / 4, clockY + khy / 4, clockX - khy / 8,
				clockY - khx / 8, clockX + khy / 8, clockY + khx / 8);
		g.setColor(0);
		g.drawLine(clockX + khx, clockY - khy, clockX - khy / 8, clockY - khx
				/ 8);
		g.drawLine(clockX + khx, clockY - khy, clockX + khy / 8, clockY + khx
				/ 8);
		g.drawLine(clockX - khx / 4, clockY + khy / 4, clockX - khy / 8, clockY
				- khx / 8);
		g.drawLine(clockX - khx / 4, clockY + khy / 4, clockX + khy / 8, clockY
				+ khx / 8);
		al = (float) (0.1047 * s);
		khx = (int) (Math.sin(al) * (clockR - 7));
		khy = (int) (Math.cos(al) * (clockR - 7));
		g.drawLine(clockX, clockY, clockX + khx, clockY - khy);
	}

	/**
	 * Draws the clock face
	 * 
	 * @param g
	 */
	void showClock(Graphics g) {
		int c = 1;
		int k1 = (int) ((clockR - 3) * 0.87);
		int k2 = (int) ((clockR - 3) * 0.5);
		float alfa = 0;
		for (float i = 0; i <= 3; i++) {
			k1 = (int) ((clockR - 3) * Math.sin(alfa));
			k2 = (int) ((clockR - 3) * Math.cos(alfa));
			showBorder(g, clockX + k1, clockY + k2, 0, 0, -c);
			showBorder(g, clockX - k1, clockY - k2, 0, 0, -c);
			showBorder(g, clockX + k1, clockY - k2, 0, 0, -c);
			showBorder(g, clockX - k1, clockY + k2, 0, 0, -c);

			g.setColor(getRealColor(5, 255));
			g.fillRect(clockX, clockY, 1, 1);
			alfa += 0.524;

		}

	}

	/**
	 * draws score
	 * 
	 * @param g
	 * @param score
	 */
	void showScore(Graphics g, int score) {
		Font f = g.getFont();
		int flen = w - 2 * marginX - cellSize * TetrisCanvas.GAME_FIELD_H_SIZE - 2;
		int slen = f.stringWidth("" + score);
		int shight = f.getHeight();

		g.setColor(getRealColor(BG_COLOR, 200)); // 200
		g.fillRect(marginX + cellSize * TetrisCanvas.GAME_FIELD_H_SIZE + 2, scoreY + 1, flen + marginX,
				shight);
		g.setColor(0xff);
		if (slen > flen) {
			g.drawString("" + score, w - flen - marginX, scoreY + 2,
					Graphics.TOP | Graphics.LEFT);
		} else {
			g.drawString("" + score, w - slen - marginX, scoreY + 2,
					Graphics.TOP | Graphics.LEFT);
		}

		g.setColor(getRealColor(BG_COLOR, 160));
		g.drawLine(w - flen - marginX / 2, scoreY, w - marginX / 2, scoreY);
		g.drawLine(w - flen - marginX / 2, scoreY + shight, w - marginX / 2,
				scoreY + shight);
		g.setColor(getRealColor(BG_COLOR, 255));
		g.drawLine(w - flen - marginX / 2, scoreY + 1, w - marginX / 2,
				scoreY + 1);
		g.drawLine(w - flen - marginX / 2, scoreY + shight + 1,
				w - marginX / 2, scoreY + shight + 1);
	}

	// shows the score after game, isHigh - true if it is high score
	void showGameScore(Graphics g, int score, boolean isHigh) {
		g.setColor(0);
		g.fillRect(0, 0, w, h);

		Font f = g.getFont();
		int slen1 = f.stringWidth(YOU_SCORE_IS_STRING);
		int slen2 = f.stringWidth("" + score);
		int w1 = (w - slen1) / 2;
		int w2 = (w - slen2) / 2;
		int shight = f.getHeight();
		int h1 = (h - shight / 5) / 3;

		g.setColor(getRealColor(BG_COLOR, 200)); // 200
		if (isHigh) {
			g.fillRect(w1 - 10, h1 - 10, slen1 + 20, 20 + shight * 5);
			showBorder(g, w1 - 10, h1 - 10, slen1 + 20, 20 + shight * 5,
					-BG_COLOR);
		} else {
			g.fillRect(w1 - 10, h1 - 10, slen1 + 20, 20 + shight * 3);
			showBorder(g, w1 - 10, h1 - 10, slen1 + 20, 20 + shight * 3,
					-BG_COLOR);
		}

		g.setColor(0xff);
		g.drawString(YOU_SCORE_IS_STRING, w1, h1, Graphics.TOP | Graphics.LEFT);
		g.drawString("" + score, w2, h1 + 2 * shight, Graphics.TOP
				| Graphics.LEFT);
		if (isHigh) {
			slen2 = f.stringWidth(IT_IS_HIGHER_STRING);
			w2 = (w - slen2) / 2;
			g.drawString(IT_IS_HIGHER_STRING, w2, h1 + 4 * shight, Graphics.TOP
					| Graphics.LEFT);
		}

	}

	// shows a high score
	void showHighScore(Graphics g, String scoreString, String dateString) {
		g.setColor(0);
		g.fillRect(0, 0, w, h);
		Font f = g.getFont();
		int slen = f.stringWidth(YOU_HIGHER_SCORE_IS_STRING);
		int w1 = (w - slen) / 2;
		int shight = f.getHeight();
		int h1 = (h - shight / 5) / 3;

		g.setColor(getRealColor(BG_COLOR, 200)); // 200
		g.fillRect(w1 - 10, h1 - 10, slen + 20, 20 + shight * 5);
		showBorder(g, w1 - 10, h1 - 10, slen + 20, 20 + shight * 5, -BG_COLOR);
		g.setColor(0xff);
		g.drawString(YOU_HIGHER_SCORE_IS_STRING, w1, h1, Graphics.TOP
				| Graphics.LEFT);

		slen = f.stringWidth(scoreString);
		w1 = (w - slen) / 2;
		g.drawString(scoreString, w1, h1 + 2 * shight, Graphics.TOP
				| Graphics.LEFT);
		slen = f.stringWidth(dateString);
		w1 = (w - slen) / 2;
		g.drawString(dateString, w1, h1 + 4 * shight, Graphics.TOP
				| Graphics.LEFT);

	}
}
