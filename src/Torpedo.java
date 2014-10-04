import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

class Torpedo extends JFrame implements KeyListener {
	boolean[] key_t = new boolean[12];
	int phase_1P = 0, phase_2P = 0;// 1P2Pそれぞれのターンにおけるフェーズの位置
	int select_1P = 0, select_2P = 0;// 選択肢で選んでいる項目の位置
	int phase_Game = 4;// ゲームのフェーズ数
	int selectLength[] = new int[2];// 選択肢の長さ
	int[][] selectAction = new int[2][2];// 選んだ選択肢を記憶[プレイヤー][選択肢のID]
	boolean isSelect[] = { false, false };// 1P2Pそれぞれの選択肢が表示されているフラグ

	// Main
	public static void main(String args[]) throws IOException {
		// 安全なJFrameの生成
		SwingUtilities.invokeLater(new Runnable() { // よく分からんけど読み込むのを待つ？
					@Override
					public void run() {
						try {
							new Torpedo();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
	}

	// Constructor
	public Torpedo() throws IOException {
		super("海戦ゲーム－魚雷発射！－");// スーパークラスであるJFrameのコンストラクタを呼び出す "フレームタイトル"
		addKeyListener(this);// キー操作のインターフェースを呼び出す
		new Thread(new ThreadClass()).start();// Threadを開始する
		setDefaultCloseOperation(EXIT_ON_CLOSE);// ウィンドウの閉じるボタンを押すとプログラムが終了するようにする
		setSize(800, 600);// Dimension(ウィンドウ）のサイズセットする
		setResizable(false);// ウィンドウリサイズ無効化
		MainPanel p = new MainPanel();
		getContentPane().add(p);
		setVisible(true);// ウィンドウを描画する
	}

	// パネル・ラベルなどのウィンドウに表示するパーツの作成
	public class MainPanel extends JPanel {
		Font font;
		JLabel label_1P, label_2P, label_Common;
		JPanel panel_1P, panel_2P, panel_Common, waitPanel;

		public MainPanel() throws IOException {
			setLayout(null);
			setBackground(Color.BLACK);

			font = new Font(Font.MONOSPACED, Font.BOLD, 15); // フォントの設定・定義

			// TODO 汚いのでそのうちメソッドにまとめる
			// 1Pコマンドパネルに表示するラベルの作成
			label_Common = new JLabel();
			label_Common.setFont(font);
			label_Common.setHorizontalTextPosition(JLabel.CENTER);
			label_Common.setVerticalAlignment(JLabel.TOP);
			label_Common.setForeground(Color.WHITE);
			label_Common.setText("<HTML>共通会話ウィンドウ<br>テスト表示");

			// 1Pコマンドパネルに表示するラベルの作成
			label_1P = new JLabel();
			label_1P.setFont(font);
			label_1P.setHorizontalTextPosition(JLabel.CENTER);
			label_1P.setVerticalAlignment(JLabel.TOP);
			label_1P.setForeground(Color.WHITE);
			label_1P.setText("<HTML>　1P会話ウィンドウ<br>テスト表示");

			// 2Pコマンドパネルに表示するラベルの作成
			label_2P = new JLabel();
			label_2P.setFont(font);
			label_2P.setHorizontalTextPosition(JLabel.CENTER);
			label_2P.setVerticalAlignment(JLabel.TOP);
			label_2P.setForeground(Color.WHITE);
			label_2P.setText("<HTML>2P会話ウィンドウ<br>テスト表示");

			// 共通会話ウィンドウの作成
			panel_Common = new JPanel();
			panel_Common.setBackground(Color.BLUE); // 会話ウインドウの色設定
			panel_Common.setBounds(0, 301, 795, 120); // 会話ウィンドウの描画(位置xy 大きさxy)
			panel_Common.setBorder(new LineBorder(Color.WHITE, 5, false));
			panel_Common.setLayout(new BorderLayout());
			panel_Common.add(label_Common);// ラベルを会話ウィンドウに追加
			add(panel_Common); // 会話ウィンドウをフレームに追加

			// 1Pコマンドパネルの作成
			panel_1P = new JPanel();
			panel_1P.setBackground(Color.BLUE); // 会話ウインドウの色設定
			panel_1P.setBounds(398, 422, 397, 150); // 会話ウィンドウの描画(位置xy 大きさxy)
			panel_1P.setBorder(new LineBorder(Color.WHITE, 5, false));
			panel_1P.setLayout(new BorderLayout());
			panel_1P.add(label_1P);// ラベルを会話ウィンドウに追加
			add(panel_1P); // 会話ウィンドウをフレームに追加

			// 2Pコマンドパネルの作成
			panel_2P = new JPanel();
			panel_2P.setBackground(Color.BLUE); // 会話ウインドウの色設定
			panel_2P.setBounds(0, 422, 397, 150); // 会話ウィンドウの描画(位置xy 大きさxy)
			panel_2P.setBorder(new LineBorder(Color.WHITE, 5, false));
			panel_2P.setLayout(new BorderLayout());
			panel_2P.add(label_2P);// ラベルを会話ウィンドウに追加
			add(panel_2P); // 会話ウィンドウをフレームに追加
		}

		// ターンの交代など待機時に表示する画面　未実装
		public void waitPanel() throws IOException {
			waitPanel = new JPanel();
			waitPanel.setBackground(Color.BLACK);

		}

		@Override
		// 描画処理
		public void paintComponent(Graphics g) {// gのフィールドはここに書いてある
			super.paintComponent(g);
			g.setFont(font);// フォントをセットする
			// g.setColor(Color.WHITE);//意味は分からない
			windowMessage(g);// フェーズごとのパネルのメッセージ表示のメソッド
		}

		// フェーズごとのパネルのメッセージ表示
		public void windowMessage(Graphics g) {
			// Graphics2D g2 = (Graphics2D) g;

			// 1Pのフェーズごとの表示 TODO 表示系以外はあとで別へ移動
			System.out
					.println("1Pフェーズ:" + phase_1P + "　select_1P:" + select_1P);// デバック
			switch (phase_1P) {
			case 0:// Title
					// タイトル画面の表示　未実装
				isSelect[0] = false;// 選択フェーズフラグ初期化
				break;
			case 1:// introduction
				isSelect[0] = false;// 選択フェーズフラグ初期化
				label_1P.setText("<HTML>駆逐艦のコマンドを選んで相手と戦うゲームです▼");
				break;
			case 2:// Command01　選択肢ID0
				isSelect[0] = true;// 選択フェーズである
				selectLength[0] = 3;
				switch (select_1P) {
				case 0:
					label_1P.setText("<HTML>コマンドを選んで下さい<br>▶装填<br>　攻撃<br>　回避行動");
					break;
				case 1:
					label_1P.setText("<HTML>コマンドを選んで下さい<br>　装填<br>▶攻撃<br>　回避行動");
					break;
				case 2:
					label_1P.setText("<HTML>コマンドを選んで下さい<br>　装填<br>　攻撃<br>▶回避行動");
					break;
				}
				selectAction[0][0] = select_1P;// 選択肢を記憶
				break;
			case 3:// 確認　選択肢ID1
				isSelect[0] = true;// 選択フェーズである
				selectLength[0] = 2;
				switch (select_1P) {
				case 0:
					label_1P.setText("<HTML>本当によろしいですか？<br>▶はい<br>　いいえ");
					break;
				case 1:
					label_1P.setText("<HTML>本当によろしいですか？<br>　はい<br>▶いいえ");
					break;
				}
				selectAction[0][1] = select_1P;
				break;
			case 4:// 待機
				isSelect[0] = false;// 選択フェーズフラグ初期化
				label_1P.setText("<HTML>作戦準備中...　そのままお待ちください");
				break;
			}

			switch (phase_2P) {
			case 0:// Title
					// タイトル画面の表示　未実装
				isSelect[1] = false;// 選択フェーズフラグ初期化
				break;
			case 1:// introduction
				isSelect[1] = false;// 選択フェーズフラグ初期化
				label_2P.setText("<HTML>駆逐艦のコマンドを選んで相手と戦うゲームです▼");
				break;
			case 2:// Command01　選択肢ID0
				isSelect[1] = true;// 選択フェーズである
				selectLength[1] = 3;
				switch (select_2P) {
				case 0:
					label_2P.setText("<HTML>コマンドを選んで下さい<br>▶装填<br>　攻撃<br>　回避行動");
					break;
				case 1:
					label_2P.setText("<HTML>コマンドを選んで下さい<br>　装填<br>▶攻撃<br>　回避行動");
					break;
				case 2:
					label_2P.setText("<HTML>コマンドを選んで下さい<br>　装填<br>　攻撃<br>▶回避行動");
					break;
				}
				selectAction[1][0] = select_2P;// 選択肢を記憶
				break;
			case 3:// 確認　選択肢ID1
				isSelect[1] = true;// 選択フェーズである
				selectLength[1] = 2;
				switch (select_2P) {
				case 0:
					label_2P.setText("<HTML>本当によろしいですか？<br>▶はい<br>　いいえ");
					break;
				case 1:
					label_2P.setText("<HTML>本当によろしいですか？<br>　はい<br>▶いいえ");
					break;
				}
				selectAction[1][1] = select_2P;
				break;
			case 4:// 待機
				isSelect[1] = false;// 選択フェーズフラグ初期化
				label_2P.setText("<HTML>作戦準備中...　そのままお待ちください");
				break;
			}
			System.out.println("選択肢で選んだもの1,2 : " + selectAction[0][0] + ","
					+ "" + selectAction[0][1]);// デバック
		}
	}

	// Runnable Class
	class ThreadClass implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					if (action())// キー入力が行われた場合
						repaint();// paint()の再実行
					Thread.sleep(140);// スレッドスリープによる画面の切り替えタイミングの決定
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// キー入力による動作
	public boolean action() {
		if (key_t[0] == true) { // 1P 上操作 UP
			if (isSelect[0] == true) {// 選択肢を選ぶ場面
				// 選択肢の項目より上へカーソルが移動したら下にループする
				if (select_1P <= 0) {
					select_1P = selectLength[0] - 1;
				} else {
					select_1P -= 1;//
				}
			}
			return true;
		}
		if (key_t[1] == true) { // 1P 右操作 RIGHT
			// pos.x += 30;
			return true;
		}
		if (key_t[2] == true) { // 1P 下操作 DOWN
			if (isSelect[0] == true) {// 選択肢を選ぶ場面
				if (select_1P >= (selectLength[0] - 1)) {// 選択肢の項目より下へカーソルが移動したら上にループする
					select_1P = 0;
				} else {
					select_1P += 1;
				}
			}
			return true;
		}
		if (key_t[3] == true) { // 1P 左操作 LEFT
			// pos.x -= 30;
			return true;
		}
		if (key_t[4] == true) { // 1P 決定操作 ENTER
			if (phase_1P == 3 && selectAction[0][1] == 1) {
				phase_1P -= 1;// 選択肢を記憶
			} else if (phase_1P < phase_Game) {
				phase_1P += 1; // フェーズを1進める
			}
			select_1P = 0;
			return true;
		}
		if (key_t[5] == true) { // 1P キャンセル操作 0キー
			select_1P = 0;// 選択位置をリセット
			if (phase_1P == 4) {
				phase_1P = 2; // コマンド選択へ戻す
			} else {
				phase_1P -= 1; // フェーズを1戻す
			}
			return true;
		}
		if (key_t[6] == true) { // 2P 上操作 W
			if (isSelect[1] == true) {// 選択肢を選ぶ場面
				// 選択肢の項目より上へカーソルが移動したら下にループする
				if (select_2P <= 0) {
					select_2P = selectLength[1] - 1;
				} else {
					select_2P -= 1;//
				}
			}
			return true;
		}
		if (key_t[7] == true) { // 2P 右操作 D
			// phase += 1;
			return true;
		}
		if (key_t[8] == true) { // 2P 下操作 S
			if (isSelect[1] == true) {// 選択肢を選ぶ場面
				if (select_2P >= (selectLength[1] - 1)) {// 選択肢の項目より下へカーソルが移動したら上にループする
					select_2P = 0;
				} else {
					select_2P += 1;
				}
			}
			return true;
		}
		if (key_t[9] == true) { // 2P 左操作 A
			// phase += 1;
			return true;
		}
		if (key_t[10] == true) {// 2P 決定操作 SPACE
			if (phase_2P == 3 && selectAction[1][1] == 1) {
				phase_2P -= 1;// 選択肢を記憶
			} else if (phase_2P < phase_Game) {
				phase_2P += 1; // フェーズを1進める
			}
			select_2P = 0;
			return true;
		}
		if (key_t[11] == true) {// 2P キャンセル操作 C
			select_2P = 0;// 選択位置をリセット
			if (phase_2P == 4) {
				phase_2P = 2; // コマンド選択へ戻す
			} else {
				phase_2P -= 1; // フェーズを1戻す
			}
			return true;
		}
		return false;
	}

	// KeyEvent Listener
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP: // 1P 上操作
			key_t[0] = true;
			break;
		case KeyEvent.VK_RIGHT: // 1P 右操作
			key_t[1] = true;
			break;
		case KeyEvent.VK_DOWN: // 1P 下操作
			key_t[2] = true;
			break;
		case KeyEvent.VK_LEFT: // 1P 左操作
			key_t[3] = true;
			break;
		case KeyEvent.VK_ENTER: // 1P 決定操作
			key_t[4] = true;
			break;
		case KeyEvent.VK_0: // 1P キャンセル操作
			key_t[5] = true;
			break;
		case KeyEvent.VK_W: // 2P 上操作
			key_t[6] = true;
			break;
		case KeyEvent.VK_D: // 2P 右操作
			key_t[7] = true;
			break;
		case KeyEvent.VK_S: // 2P 下操作
			key_t[8] = true;
			break;
		case KeyEvent.VK_A: // 2P 左操作
			key_t[9] = true;
			break;
		case KeyEvent.VK_SPACE: // 2P 決定操作
			key_t[10] = true;
			break;
		case KeyEvent.VK_C: // 2P キャンセル操作
			key_t[11] = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP: // 1P 上操作
			key_t[0] = false;
			break;
		case KeyEvent.VK_RIGHT: // 1P 右操作
			key_t[1] = false;
			break;
		case KeyEvent.VK_DOWN: // 1P 下操作
			key_t[2] = false;
			break;
		case KeyEvent.VK_LEFT: // 1P 左操作
			key_t[3] = false;
			break;
		case KeyEvent.VK_ENTER: // 1P 決定操作
			key_t[4] = false;
			break;
		case KeyEvent.VK_0: // 1P キャンセル操作
			key_t[5] = false;
			break;
		case KeyEvent.VK_W: // 2P 上操作
			key_t[6] = false;
			break;
		case KeyEvent.VK_D: // 2P 右操作
			key_t[7] = false;
			break;
		case KeyEvent.VK_S: // 2P 下操作
			key_t[8] = false;
			break;
		case KeyEvent.VK_A: // 2P 左操作
			key_t[9] = false;
			break;
		case KeyEvent.VK_SPACE: // 2P 決定操作
			key_t[10] = false;
			break;
		case KeyEvent.VK_C: // 2P キャンセル操作
			key_t[11] = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}