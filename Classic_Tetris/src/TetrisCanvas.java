import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * 
 * @author Alexander Lisovets 2007
 */
public class TetrisCanvas extends GameCanvas {
	private final static String PATH_TO_DOWN_MIDI = "midi/1a.mid";
	private final static String PATH_TO_ROWS_DELETE_MIDI = "midi/3a.mid";
	public final static int GAME_FIELD_H_SIZE = 10; // horizontal game field
													// size in elements
	public final static int GAME_FIELD_V_SIZE = 20; // vertical game field size
	public final static int ITEM_SIZE = 4;
	public final static int MENU_BG_COLOR = 7;
	private final static int MENU_BG_INTENSITY = 200;
	private final static int SELECTED_MENU_TEXT_COLOR = 0xff;
	private final static int UNSELECTED_MENU_TEXT_COLOR = 0;
	private final static int ROTATION_IMPOSSIBLE_RESULT = -10;

	private TetrisMidlet midlet;
	private Display myDisplay;
	private boolean initialized = false;
	private int w, h;
	private boolean key2 = false;
	private boolean key5 = false;
	private boolean key8 = false;
	private boolean key4 = false;
	private boolean key6 = false;
	private boolean keyFire = false;
	private boolean keyDown = false;
	private boolean keyUp = false;
	private boolean keyRight = false;
	private boolean keyLeft = false;
	private boolean keyStop = false;
	private int loopCount = 1; // loop conter;
	private int loopCount2 = 0;
	private byte itemNum = 0;
	private byte nextNum = 0;
	private byte[][] gameFld;
	private byte[][] gameItem;
	private byte[][] turnItem;
	private byte[][] nextItem;
	private byte[][][] items;
	private byte currentItemColor = 1; // curent item color
	private Random rand;
	private Player player1;
	private Player player2;

	private int itmX, itemPositionY;
	private int score = 0;
	private int cntrScore = 1000;
	private int delay = 50; 
	private int menuFlags = 0x21;
	private String rsGameName = "tetris_game";
	private String rsScoreName = "tetris_score";
	private GameItemDrawer itemDrawer;

	public TetrisCanvas(TetrisMidlet midlet) {
		super(false);
		this.midlet = midlet;
		myDisplay = Display.getDisplay(midlet);
		myDisplay.setCurrent(this);
		setFullScreenMode(true);
		rand = new Random();
		w = getWidth();
		h = getHeight();
	}

	/**
	 * initials the game
	 */
	public void startInit() {
		gameFld = new byte[GAME_FIELD_H_SIZE][GAME_FIELD_V_SIZE];
		Graphics graphics = getGraphics();
		itemDrawer = new GameItemDrawer(this, graphics, gameFld);
		itemDrawer.title(getGraphics());
		flushGraphics();

		gameItem = new byte[ITEM_SIZE][ITEM_SIZE];
		turnItem = new byte[ITEM_SIZE][ITEM_SIZE];
		nextItem = new byte[ITEM_SIZE][ITEM_SIZE];

		for (int i = 0; i < ITEM_SIZE; i++)
			for (int j = 0; j < ITEM_SIZE; j++) {
				gameItem[i][j] = 0;
				turnItem[i][j] = 0;
				nextItem[i][j] = 0;
			}
		initItems();
		try {
			player1 = Manager.createPlayer(
					getClass().getResourceAsStream(PATH_TO_DOWN_MIDI),
					"audio/midi");
			player2 = Manager.createPlayer(
					getClass().getResourceAsStream(PATH_TO_ROWS_DELETE_MIDI),
					"audio/midi");
		} catch (Exception e) {
			// do nothing , mute
		}
		if (getNumRecords(rsGameName) != 0) {
			menuFlags = 0x25;
		}
		if (getNumRecords(rsScoreName) != 0) {
			menuFlags |= 0x10;
		}

		while (!anyKey(true)) {
			pause(100);
		}

		pause(200);
		initialized = true;

	}

	/*
	 * creates and draws the menu return a selected item number
	 */
	private int menu(Graphics g, int flags) {

		// TODO: It must be completely redesigned. It is awkward. :(
		// Who is the idiot who did it? Oh, it's me.

		String[] items = { "new game", "return to game", "load game",
				"save game", "high score", "exit" };

		int selectItemPosition = 0; // selection item
		Font font = g.getFont();
		int fontHeight = font.getHeight();
		int menuColor = itemDrawer.getRealColor(MENU_BG_COLOR,
				MENU_BG_INTENSITY); // menu color

		// determine the number of involved menu items
		int involvedNumber = 0;
		for (int i = 0; i < items.length; i++) {
			if ((flags & (1 << i)) != 0)
				involvedNumber++;
		}

		// involved to create menu items
		String[] activeMenuItems = new String[involvedNumber];
		for (int i = 0, j = 0; j < involvedNumber; i++) {
			if ((flags & (1 << i)) != 0) {
				activeMenuItems[j++] = items[i];
			}
		}

		while (true) {
			if (loopCount++ % 16 == 0) {
				if (keyDown || key8) {
					if (selectItemPosition < activeMenuItems.length - 1)
						selectItemPosition++;
				}
				if (keyUp || key2) {
					if (selectItemPosition > 0)
						selectItemPosition--;
				}

				int strt = 0;
				if ((2 * fontHeight + 4) * (selectItemPosition + 1) - 2 > h) {
					strt = 1 + selectItemPosition - (h + 2)
							/ (2 * fontHeight + 4);
				}
				g.setColor(menuColor);
				g.fillRect(0, 0, w, h);

				// draw menu buttons
				for (int i = strt; i < activeMenuItems.length; i++) {
					if (5 + (2 * fontHeight + 4) * (i - strt) + 3 * fontHeight
							/ 2 < h) {
						if (i != selectItemPosition) {
							g.setColor(UNSELECTED_MENU_TEXT_COLOR);
							g.drawString(activeMenuItems[i], 10, 5
									+ (2 * fontHeight + 4) * (i - strt)
									+ fontHeight / 2, Graphics.TOP
									| Graphics.LEFT);
						} else {
							if (keyFire || key5) {
								itemDrawer.showBorder(g, 7, 7
										+ (2 * fontHeight + 4) * (i - strt),
										w - 14, 2 * fontHeight - 4, -7);
								g.setColor(SELECTED_MENU_TEXT_COLOR);
								g.drawString(activeMenuItems[i], 15, 6
										+ (2 * fontHeight + 4) * (i - strt)
										+ fontHeight / 2, Graphics.TOP
										| Graphics.LEFT);
								flushGraphics();
								pause(100);
								for (int j = 0, n = -1; j < items.length; j++) {
									if ((flags & (1 << j)) != 0) {
										if (++n == i)
											return j;
									}
								}
							} else {
								itemDrawer.showBorder(g, 7, 7
										+ (2 * fontHeight + 4) * (i - strt),
										w - 14, 2 * fontHeight - 4, -7);
								g.setColor(SELECTED_MENU_TEXT_COLOR);
								g.drawString(activeMenuItems[i], 13, 4
										+ (2 * fontHeight + 4) * (i - strt)
										+ fontHeight / 2, Graphics.TOP
										| Graphics.LEFT);
							}
						}
					}
				}
				flushGraphics();
			}
			pause(25);
		}

	}

	/**
	 * starts the main loop
	 */
	public void start() {
		while (!initialized) {
			pause(100);
		}
		Graphics g = this.getGraphics();
		int selectedItemPosition;
		do {
			// TODO: This block must be completely redesigned. It is the ugly
			// menu. :(

			selectedItemPosition = menu(g, menuFlags);

			if (selectedItemPosition == 0) {
				// new game
				initNewGame();
				game(g);
			} else if (selectedItemPosition == 1) {
				// return to game
				game(g);
			} else if (selectedItemPosition == 2) {
				// load game
				if (!loadGame()) {
					menuFlags &= 0x3b; // load is disabled
					continue;
				}
				game(g);
			} else if (selectedItemPosition == 3) {
				// save game
				if (!saveGame()) {
					menuFlags &= 0x3b; // load is enabled
					continue;
				}
				menuFlags |= 4; // load is anabled
				menuFlags &= 0x37; // save is disable
			} else if (selectedItemPosition == 4) {
				// showHiScore
				String scoreString;
				byte[] buf = getRecord(rsScoreName);
				if (buf == null)
					scoreString = "nothing";
				else {
					byte[] sc = new byte[4];
					for (int i = 0; i < 4; i++)
						sc[i] = buf[i + 3];
					scoreString = "" + bytes2Int(sc);
				}
				String dateString;
				if (buf != null) {
					dateString = dateToString(buf);
				} else {
					dateString = "";
				}

				itemDrawer.showHighScore(g, scoreString, dateString);
				flushGraphicsAndWaitKeyPres();
			}
		} while (selectedItemPosition != 5);
		stop(true);
		return;
	}

	/*
	 * Starts the game return true - end game false - pause
	 */
	private boolean game(Graphics g) {
		itemDrawer.showGameScreen(g);
		itemDrawer.showGameItem(g, gameItem, itmX, itemPositionY);
		itemDrawer.showNextItem(g, nextItem);
		itemDrawer.showScore(g, score);
		itemDrawer.showClock(g);
		itemDrawer.showTime(g, new Date().getTime());
		flushGraphics();
		while (true) {
			pause(delay);
			if (loopCount > 350) {
				itemDrawer.showTime(g, new Date().getTime());
				flushGraphics();
				continue;
			}
			if (loopCount % 2 == 0) {
				if (keyDown || key8) {
					if (itemDown(false, g)) {
						itemDrawer.showGameItem(g, gameItem, itmX,
								itemPositionY);
						flushGraphics();
					} else {
						loopCount2 = 0;
					}
				}
			}
			if (loopCount % 6 == 0) {
				itemDrawer.showTime(g, new Date().getTime());
				if (keyUp || key2) {
					itemTurning();
					itemDrawer.showGameItem(g, gameItem, itmX, itemPositionY);
					// showScore(g);
					flushGraphics();
				}
				if (keyLeft || key4) {
					itemLeft();
					itemDrawer.showGameItem(g, gameItem, itmX, itemPositionY);
					// showScore(g);
					flushGraphics();
				}
				if (keyRight || key6) {
					itemRight();
					itemDrawer.showGameItem(g, gameItem, itmX, itemPositionY);
					// showScore(g);
					flushGraphics();
				}

				if (keyStop) {
					// pause
					menuFlags |= 2; // +return
					menuFlags |= 8; // +save
					return false;
				}
			}
			loopCount++;

			if (loopCount2++ % 18 == 0) {
				if (!itemDown(true, g)) {
					if (topTest()) {
						// NO game over
						getNewItem();
						itemDrawer.showGameItem(g, gameItem, itmX,
								itemPositionY);
						itemDrawer.showNextItem(g, nextItem);
						itemDrawer.showScore(g, score);
						flushGraphics();
					} else {
						// game over
						menuFlags &= 0x35; // -return
						menuFlags |= 0x10; // +high score
						checkScore(g);
						return true;
					}
				} else {
					itemDrawer.showGameItem(g, gameItem, itmX, itemPositionY);
					flushGraphics();
				}
			}
		}
	}

	private void flushGraphicsAndWaitKeyPres() {
		flushGraphics();
		pause(1000);
		while (!anyKey(true))
			pause(100);
		pause(200);
	}

	// stops the midlet
	public void stop(boolean flag) {
		pause(100);
		midlet.stop();
	}

	/*
	 * Checks if score is the higher and saves it if it's true.
	 */
	private void checkScore(Graphics g) {
		byte[] buffer;
		byte[] buf2;
		if (getNumRecords(rsScoreName) != 0) {
			buffer = getRecord(rsScoreName);
			buf2 = new byte[4];
			for (int i = 0; i < 4; i++)
				buf2[i] = buffer[i + 3];
			int recScore = bytes2Int(buf2);
			if (score <= recScore) {
				itemDrawer.showGameScore(g, score, false);
				flushGraphicsAndWaitKeyPres();
				return;
			}
			deleteRecords(rsScoreName);
		}
		Date now = new Date();
		byte[] bd = getDateArray(now.getTime());
		buf2 = int2Bytes(score);

		buffer = new byte[7];
		for (int i = 0; i < 3; i++) {
			buffer[i] = bd[i];
			buffer[i + 3] = buf2[i];
		}
		buffer[6] = buf2[3];
		addRecord(buffer, rsScoreName);
		itemDrawer.showGameScore(g, score, true);
		flushGraphicsAndWaitKeyPres();
	}

	// keypress handler
	protected void keyPressed(int keyCode) {
		loopCount = 0;
		if (keyCode == KEY_NUM2)
			key2 = true;
		if (keyCode == KEY_NUM5)
			key5 = true;
		if (keyCode == KEY_NUM8)
			key8 = true;
		if (keyCode == KEY_NUM4)
			key4 = true;
		if (keyCode == KEY_NUM6)
			key6 = true;
		if (keyCode == -1)
			keyUp = true;
		if (keyCode == -2)
			keyDown = true;
		if (keyCode == -5)
			keyFire = true;
		if (keyCode == -3)
			keyLeft = true;
		if (keyCode == -4)
			keyRight = true;
		if (keyCode == -6)
			keyStop = true;
	}

	protected void keyReleased(int keyCode) {
		if (keyCode == KEY_NUM2)
			key2 = false;
		if (keyCode == KEY_NUM5)
			key5 = false;
		if (keyCode == KEY_NUM8)
			key8 = false;
		if (keyCode == KEY_NUM4)
			key4 = false;
		if (keyCode == KEY_NUM6)
			key6 = false;
		if (keyCode == -1)
			keyUp = false;
		if (keyCode == -2)
			keyDown = false;
		if (keyCode == -5)
			keyFire = false;
		if (keyCode == -3)
			keyLeft = false;
		if (keyCode == -4)
			keyRight = false;
		if (keyCode == -6)
			keyStop = false;
	}

	/*
	 * checks the availability of a movement to left
	 */
	private boolean checkLeft() {
		for (int i = 0; i < ITEM_SIZE; i++)
			for (int j = 0; j < ITEM_SIZE; j++)
				if (gameItem[i][j] != 0) {
					if (itmX + i < 1)
						return false;
					if (itemPositionY + j < 0)
						continue;
					if (gameFld[itmX + i - 1][itemPositionY + j] != 0)
						return false;
				}
		return true;
	}

	/*
	 * checks the availability of a movement to right
	 */
	private boolean checkRight() {
		for (int i = 0; i < ITEM_SIZE; i++)
			for (int j = 0; j < ITEM_SIZE; j++)
				if (gameItem[i][j] != 0) {
					if (itmX + i >= GAME_FIELD_H_SIZE - 1)
						return false;
					if (itemPositionY + j < 0)
						continue;
					if (gameFld[itmX + i + 1][itemPositionY + j] != 0)
						return false;
				}
		return true;
	}

	/*
	 * checks the availability of a movement down
	 */
	private boolean checkDown() {
		for (int i = 0; i < ITEM_SIZE; i++)
			for (int j = 0; j < ITEM_SIZE; j++)
				if (gameItem[i][j] != 0) {
					if (itemPositionY + j >= GAME_FIELD_V_SIZE - 1)
						return false;
					if (itemPositionY + j < 0)
						continue;
					if (gameFld[itmX + i][itemPositionY + j + 1] != 0)
						return false;
				}
		return true;
	}

	/*
	 * checks or the item on top
	 */
	private boolean topTest() {
		for (int i = 0; i < ITEM_SIZE; i++) {
			if (itemPositionY + i > 0)
				return true;
			for (int j = 0; j < ITEM_SIZE; j++) {
				if (gameItem[j][i] != 0) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * checks the ability to insert an item at the specified position
	 */
	private boolean checkInsert(int x, int y) {
		for (int i = 0; i < ITEM_SIZE; i++)
			for (int j = 0; j < ITEM_SIZE; j++) {
				if (turnItem[i][j] != 0) {
					if ((x + i < 0) || (x + i) >= GAME_FIELD_H_SIZE)
						return false;
					if (y + j >= GAME_FIELD_V_SIZE)
						return false;
					if (y + j < 0)
						continue;
					if (gameFld[x + i][y + j] != 0)
						return false;
					if (y + j < 1)
						continue;
					if (gameFld[x + i][y + j - 1] != 0)
						return false;
				}
			}
		return true;
	}

	/*
	 * verifies the ability to rotate the item at the specified position and
	 * turns it if true
	 */
	private void itemTurning() {
		for (int i = 0; i < ITEM_SIZE; i++) {
			for (int j = 0; j < ITEM_SIZE; j++) {
				turnItem[i][ITEM_SIZE - j - 1] = gameItem[j][i];
			}
		}
		// test turning
		int x = 0, y = 0;
		m1: for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				x = itmX + i;
				y = itemPositionY + j;
				if (checkInsert(x, y)) {
					break m1;
				}
				x = itmX - i;
				y = itemPositionY - j;
				if (checkInsert(x, y)) {
					break m1;
				}
			}
			x = ROTATION_IMPOSSIBLE_RESULT;
		}
		if (x == ROTATION_IMPOSSIBLE_RESULT)
			return;

		for (int i = 0; i < ITEM_SIZE; i++) {
			for (int j = 0; j < ITEM_SIZE; j++) {
				gameItem[i][j] = turnItem[i][j];
			}
		}
		itmX = x;
		itemPositionY = y;
	}

	// Cleans one row in the array from the blocks
	private void clearOneRow(int y) {
		for (int i = 0; i < GAME_FIELD_H_SIZE; i++) {
			gameFld[i][y] = 0;
		}
	}

	// moves all the blocks that are above the deleted row down for one row
	// and redraws screen
	private void delOneRow(int y, Graphics g) {
		for (int i = y; i > 0; i--) {
			for (int j = 0; j < GAME_FIELD_H_SIZE; j++) {
				gameFld[j][i] = gameFld[j][i - 1];
			}
		}
		itemDrawer.showGameField(g);
		flushGraphics();
		pause(40);
	}

	// Cleans the rows that is completed
	private void clearRows(Graphics g) {
		int num = 0;
		m1: for (int i = itemPositionY; i < itemPositionY + 4; i++) {
			if ((i < 0) || (i >= GAME_FIELD_V_SIZE))
				continue;
			for (int j = 0; j < GAME_FIELD_H_SIZE; j++) {
				if (gameFld[j][i] == 0) {
					continue m1;
				}
			}
			num++;
			clearOneRow(i);
		}

		// the score is more - the speed is more
		if (score > cntrScore) {
			delay = (int) (delay * 0.8 + 4);
			cntrScore *= 2;
		}
		if (num == 0) {
			score += 5;
			try {
				player1.deallocate();
				player1.start();
			} catch (Exception e) {
				// do nothing, mute
			}
			return;
		}

		myDisplay.vibrate(100);
		try {
			player2.deallocate();
			player2.start();
		} catch (Exception e) {
			// do nothing, mute
		}

		if (num == 1) {
			score += 25;
		}
		if (num == 2) {
			score += 100;
		}
		if (num == 3) {
			score += 250;
		}
		if (num == 4) {
			score += 500;
		}

		itemDrawer.showGameField(g);
		flushGraphics();
		pause(150);
		m2: for (int i = itemPositionY; i < itemPositionY + 4; i++) {
			if ((i < 0) || (i >= GAME_FIELD_V_SIZE))
				continue;
			for (int j = 0; j < GAME_FIELD_H_SIZE; j++) {
				if (gameFld[j][i] != 0) {
					continue m2;
				}
			}
			delOneRow(i, g);
		}
	}

	// moves item for one row down if it is possible
	private boolean itemDown(boolean action, Graphics g) {
		if (checkDown()) {
			itemPositionY++;
			return true;
		}
		if (!action)
			return false;
		stickItem();
		clearRows(g);
		pause(250);
		return false;
	}

	// moves item for one row to left if it is possible
	private void itemLeft() {
		if (checkLeft())
			itmX--;
	}

	// moves item for one row to right if it is possible
	private void itemRight() {
		if (checkRight())
			itmX++;
	}

	// sticks blocs of the current item on the game field
	private void stickItem() {
		for (int i = 0; i < ITEM_SIZE; i++)
			for (int j = 0; j < ITEM_SIZE; j++) {
				if (itemPositionY + j < 0)
					continue;
				if (gameItem[i][j] != 0) {
					gameFld[itmX + i][itemPositionY + j] = gameItem[i][j];
				}
			}
	}

	// inits new game
	private void initNewGame() {
		itmX = 3;
		itemPositionY = -2;
		score = 0;
		delay = 50;
		cntrScore = 1000;

		for (int i = 0; i < GAME_FIELD_H_SIZE; i++)
			for (int j = 0; j < GAME_FIELD_V_SIZE; j++) {
				gameFld[i][j] = 0;
			}
	}

	//
	private void getNewItem() {
		itemPositionY = -2;
		itmX = 3;
		nextNum = itemNum;
		itemNum = (byte) rand.nextInt(items.length);
		currentItemColor = (byte) (rand.nextInt(7) + 1);
		for (int i = 0; i < ITEM_SIZE; i++)
			for (int j = 0; j < ITEM_SIZE; j++) {
				gameItem[i][j] = (byte) (nextItem[i][j] * currentItemColor);
				nextItem[i][j] = (byte) items[itemNum][i][j];
			}
	}

	// // get a color 1 from 8
	// private int getColor8(int num, int intens) {
	// intens &= 255;
	// return (num & 1) * intens + (num & 2) * (intens << 7) + (num & 4)
	// * (intens << 14);
	// }

	// checks if any key is pressed
	private boolean anyKey() {
		return key2 || key5 || key8 || key4 || key6 || keyFire || keyDown
				|| keyUp || keyRight || keyLeft || keyStop;
	}

	// sets all the keys on unpressed state. return true if there are a pressed
	// keys
	private boolean anyKey(boolean reset) {
		boolean r = anyKey();
		if (reset)
			key2 = key5 = key8 = key4 = key6 = keyFire = keyDown = keyUp = keyRight = keyLeft = keyStop = false;
		return r;
	}

	// saves game state in the persistent RecordStore.
	private boolean saveGame() {
		if (!deleteRecords(rsGameName))
			return false;

		// forms the bytes array to save in RecordStore
		byte[] saveFld = new byte[200];
		for (int i = 1; i < GAME_FIELD_V_SIZE; i++) {
			for (int j = 0; j < GAME_FIELD_H_SIZE; j++) {
				saveFld[i * GAME_FIELD_H_SIZE + j] = gameFld[j][i];
			}
		}

		// split the score value on bytes
		// the stupid and complicated decision!
		byte[] buf = int2Bytes(score);
		for (int i = 0; i < ITEM_SIZE; i++)
			saveFld[i] = buf[i];
		buf = int2Bytes(cntrScore);

		for (int i = 0; i < ITEM_SIZE; i++)
			saveFld[i + ITEM_SIZE] = buf[i];

		saveFld[8] = (byte) (itemNum + (nextNum << 4));
		saveFld[9] = (byte) delay;

		return addRecord(saveFld, rsGameName);
	}

	// restore game from saved one in the RecoreStore
	private boolean loadGame() {
		byte[] saveFld = getRecord(rsGameName);
		if (saveFld == null)
			return false;
		score = bytes2Int(saveFld);
		byte[] buf = { saveFld[4], saveFld[5], saveFld[6], saveFld[7] };
		cntrScore = bytes2Int(buf);

		itemNum = (byte) (saveFld[8] & 0xF);
		nextNum = (byte) ((saveFld[8] & 0xF0) >> 4);
		delay = saveFld[9];

		for (int i = 1; i < GAME_FIELD_V_SIZE; i++) {
			for (int j = 0; j < GAME_FIELD_H_SIZE; j++) {
				gameFld[j][i] = saveFld[i * GAME_FIELD_H_SIZE + j];
			}
		}
		for (int i = 0; i < GAME_FIELD_H_SIZE; i++) {
			gameFld[i][0] = 0;
		}

		itemPositionY = -2;
		itmX = 3;
		currentItemColor = (byte) (rand.nextInt(7) + 1);
		for (int i = 0; i < ITEM_SIZE; i++)
			for (int j = 0; j < ITEM_SIZE; j++) {
				gameItem[i][j] = (byte) (items[nextNum][i][j] * currentItemColor);
				nextItem[i][j] = (byte) items[itemNum][i][j];
			}

		return true;
	}

	/**
	 * returns a number of records in a recordstore named rsName;
	 * 
	 * @param rsName
	 *            the record store name
	 * @return the number of records
	 */
	public int getNumRecords(String rsName) {
		try {
			RecordStore recordStore = RecordStore.openRecordStore(rsName, true);
			int n = recordStore.getNumRecords();
			recordStore.closeRecordStore();
			return n;
		} catch (RecordStoreException rse) {
			return 0;
		}
	}

	/**
	 * deletes a record store
	 * 
	 * @param rsName
	 *            the record store name
	 * @return true if success
	 */
	public boolean deleteRecords(String rsName) {
		try {
			RecordStore recordStore = RecordStore.openRecordStore(rsName, true);
			RecordEnumeration re = recordStore.enumerateRecords(null, null,
					false);
			while (re.hasNextElement()) {
				int id = re.nextRecordId();
				recordStore.deleteRecord(id);
			}
			recordStore.closeRecordStore();
		} catch (RecordStoreException rse) {
			return false;
		}
		return true;
	}

	/**
	 * adds a record in the record store
	 * 
	 * @param buf
	 *            the byte array to store
	 * @param rsName
	 *            the record store name
	 * @return true if success
	 */
	public boolean addRecord(byte[] buf, String rsName) {
		try {
			RecordStore recordStore = RecordStore.openRecordStore(rsName, true);
			recordStore.addRecord(buf, 0, buf.length);
			recordStore.closeRecordStore();
		} catch (RecordStoreException rse) {
			return false;
		}
		return true;
	}

	/**
	 * reads and returns the last record from the record store
	 * 
	 * @param rsName
	 *            the record store name
	 * @return gotten record or null
	 */
	public byte[] getRecord(String rsName) {
		byte[] buf;
		try {
			RecordStore recordStore = RecordStore.openRecordStore(rsName, true);
			RecordEnumeration re = recordStore.enumerateRecords(null, null,
					false);
			if (!re.hasNextElement())
				return null;
			int id = re.nextRecordId();
			buf = recordStore.getRecord(id);
			recordStore.closeRecordStore();
		} catch (RecordStoreException rse) {
			return null;
		}
		return buf;
	}

	private void pause(long msec) {
		try {
			Thread.sleep(msec);
		} catch (Exception e) {
		}
	}

	// creates the byte array to store 4 bytes an int value
	private byte[] int2Bytes(int num) {
		byte[] buf = new byte[4];
		buf[0] = (byte) (num & 0xff);
		buf[1] = (byte) ((num >>> 8) & 0xff);
		buf[2] = (byte) ((num >>> 16) & 0xff);
		buf[3] = (byte) (num >>> 24);
		return buf;
	}

	// restore integer value from 4 bytes from the byte array
	private int bytes2Int(byte[] buf) {
		if ((buf == null) || (buf.length < 4))
			return 0;
		int num = (buf[0] & 0xff) + ((buf[1] & 0xff) << 8)
				+ ((buf[2] & 0xff) << 16) + ((buf[3] & 0xff) << 24);
		return num;
	}

	// inits game items
	private void initItems() {
		items = new byte[7][ITEM_SIZE][ITEM_SIZE];

		for (int i = 0; i < 7; i++)
			for (int j = 0; j < ITEM_SIZE; j++)
				for (int k = 0; k < ITEM_SIZE; k++)
					items[i][j][k] = 0;

		// ||
		items[0][1][1] = 1;
		items[0][1][2] = 1;
		items[0][2][1] = 1;
		items[0][2][2] = 1;

		// |_
		items[1][1][0] = 1;
		items[1][1][1] = 1;
		items[1][1][2] = 1;
		items[1][2][2] = 1;

		// _|
		items[2][2][0] = 1;
		items[2][2][1] = 1;
		items[2][2][2] = 1;
		items[2][1][2] = 1;

		// _|_
		items[3][1][0] = 1;
		items[3][1][1] = 1;
		items[3][1][2] = 1;
		items[3][2][1] = 1;

		// -_
		items[4][1][0] = 1;
		items[4][1][1] = 1;
		items[4][2][1] = 1;
		items[4][2][2] = 1;

		// _-
		items[5][2][0] = 1;
		items[5][2][1] = 1;
		items[5][1][1] = 1;
		items[5][1][2] = 1;
		// ----

		items[6][0][1] = 1;
		items[6][1][1] = 1;
		items[6][2][1] = 1;
		items[6][3][1] = 1;

		getNewItem();
		getNewItem();
	}

	// creates byte array with the date (day, month, year)
	private byte[] getDateArray(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(time));

		byte year = (byte) (calendar.get(Calendar.YEAR) - 2000);
		byte month = (byte) (calendar.get(Calendar.MONTH) + 1);
		byte day = (byte) calendar.get(Calendar.DATE);
		return new byte[] { day, month, year };

	}

	private String dateToString(byte[] date) {
		String sresultString;
		if (date[0] <= 9)
			sresultString = "0" + date[0] + ".";
		else
			sresultString = "" + date[0] + ".";
		if (date[1] <= 9)
			sresultString += "0" + date[1] + "." + (2000 + date[2]);
		else
			sresultString += "" + date[1] + "." + (2000 + date[2]);

		return sresultString;
	}

}
