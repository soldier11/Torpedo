﻿import java.awt.BorderLayout;
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

	boolean key_t[] = { false, false, false, false, false, false, false, false,
			false, false, false, false }; // キーのオン・オフ
	int phase_1P = 0, phase_2P = 0;// 1P2Pそれぞれのターンにおけるフェーズの位置
	int select_1P = 0, select_2P = 0;// 選択肢で選んでいる項目の位置
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
			System.out.println("1Pフェーズ:" + phase_1P + "select_1P" + select_1P);// デバック
			switch (phase_1P) {
			case 0:// Title
				//タイトル画面の表示　未実装
				isSelect[0] = false;// 選択フェーズフラグ初期化
				break;
			case 1:// introduction
				isSelect[0] = false;// 選択フェーズフラグ初期化
				label_1P.setText("<HTML>駆逐艦のコマンドを選んで相手と戦うゲームです▼");
				break;
			case 2:// Command01
				isSelect[0] = true;// 選択フェーズである
				switch (select_1P) {
				case 0:
					label_1P.setText("<HTML>コマンドを選んで下さい<br>?装填<br>　攻撃<br>　回避行動");
					break;
				case 1:
					label_1P.setText("<HTML>コマンドを選んで下さい<br>　装填<br>?攻撃<br>　回避行動");
					break;
				case 2:
					label_1P.setText("<HTML>コマンドを選んで下さい<br>　装填<br>　攻撃<br>?回避行動");
					break;
				}
				break;
			case 3:// 工事中
				isSelect[0] = false;// 選択フェーズフラグ初期化
				label_1P.setText(null);
				break;
			}

			/*
			 * if (phase == 0) {// TODO 配置する艦を選択（今後実装） isShip[0] = false; } else
			 * if (phase == 1) {// 艦の召喚　可動状態 isShip[0] = true; posLC.x =
			 * pos.x;// posLC =pos;はオブジェクトのコピーではなく参照コピーとなるため× posLC.y = pos.y;
			 * g.drawImage(img[2], pos.x, pos.y, this);
			 * 
			 * } else if (quar == true && phase == 2) {// 艦の抜錨　可動状態へ isShip[0] =
			 * false; AffineTransform at = new AffineTransform();
			 * at.setToRotation(Math.toRadians(-90), 15, 15);
			 * g2.translate(posLC.x, posLC.y); g2.drawImage(img[2], at, this);
			 * g2.translate(-posLC.x, -posLC.y);
			 * 
			 * } else if (phase == 2) {// 艦の投錨　固定状態へ isShip[0] = false;
			 * g.drawImage(img[2], posLC.x, posLC.y, this);
			 * jl.setText("<HTML>投錨しました。ここに停泊させます。<br>SPACE：抜錨（移動）");
			 * 
			 * } else if (phase == 3 && posLC.x == pos.x && posLC.y == pos.y)
			 * {// 艦の抜錨　可動状態へ isShip[0] = true; g.drawImage(img[2], pos.x,
			 * pos.y, this);
			 * jl.setText("<HTML>抜錨しました。停泊場所を決定してください。<br>SPACE：投錨（停泊）"); phase
			 * = 1; } else if (phase == 3) {// 艦の先端を選べていない　可動状態にするには船首を選ぶ
			 * g.drawImage(img[2], posLC.x, posLC.y, this);
			 * jl.setText("<HTML>艦の船首を選べていません。<br>可動状態にするには船首を選んでSPACEを押してください。"
			 * ); phase = 2; }
			 */
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
				select_1P -= 1;
			}
			return true;
		}
		if (key_t[1] == true) { // 1P 右操作 RIGHT
			// pos.x += 30;
			return true;
		}
		if (key_t[2] == true) { // 1P 下操作 DOWN
			if (isSelect[0] == true) {// 選択肢を選ぶ場面
				select_1P += 1;
			}
			return true;
		}
		if (key_t[3] == true) { // 1P 左操作 LEFT
			// pos.x -= 30;
			return true;
		}
		if (key_t[4] == true) { // 1P 決定操作 ENTER
			phase_1P += 1; // フェーズを1進める
			return true;
		}
		if (key_t[5] == true) { // 1P キャンセル操作 0キー
			phase_1P -= 1; // フェーズを1戻す
			return true;
		}
		if (key_t[6] == true) { // 2P 上操作 W
			// phase += 1;
			return true;
		}
		if (key_t[7] == true) { // 2P 右操作 D
			// phase += 1;
			return true;
		}
		if (key_t[8] == true) { // 2P 下操作 S
			// phase += 1;
			return true;
		}
		if (key_t[9] == true) { // 2P 左操作 A
			// phase += 1;
			return true;
		}
		if (key_t[10] == true) {// 2P 決定操作 SPACE
			phase_2P += 1;
			return true;
		}
		if (key_t[11] == true) {// 2P キャンセル操作 C
			// phase += 1;
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