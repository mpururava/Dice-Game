import java.util.Random;

class Dice {
	Random r = new Random();
	int i;
	public synchronized int dice(int playerNo) {
		i = r.nextInt(7);
		if(i==0) i=1;
		if(playerNo==Game.current_player||Game.on) {
			if((playerNo==Game.current_player&&Game.on)||(playerNo==Game.current_player&&!Game.on)) {
				System.out.println(" Player :"+playerNo+" Throwing a Dice.");
				System.out.println(" Random number is :"+i);
				Game.nextCurrentPlayer();
				Game.on = true;
				notifyAll();
				return i;
			}
			else if(playerNo!=Game.current_player&&Game.on) Game.on = false;
		}
		if(!Game.on) {
			try {
				wait();
			}catch(Exception e) {}
		}
		return 0;
	}
}
class Player extends Thread {
	Dice dice;
	int calc;
	int no,score;
	public Player(Dice d,int no) {
		dice = d;
		score = 0;
		this.no = no;
	}
	public void run() {
		go:while(true) {
			try {
				calc = dice.dice(no);
				score +=calc;
				if(calc==0) {continue go;}
				if(score>=25) {
					System.out.println("Winner is Player "+no+" score is :"+score);
					System.exit(0);
				}
				else {
					System.out.println(" Player :"+no+" score is :"+score+"\n");
					//Thread.sleep(2200);
				}
			}catch(Exception e) {}
		}
	}
}
public class Game {
	public static int current_player = 1;
	public static boolean on = true;
	public static void main(String[] my) {
		Dice d = new Dice();
		Player p1 = new Player(d,1);
		Player p2 = new Player(d,2);
		Player p3 = new Player(d,3);
		Player p4 = new Player(d,4);
		p1.start();
		p2.start();
		p3.start();
		p4.start();
	}
	public static void nextCurrentPlayer() {
		if(current_player>=1&&current_player<4) current_player++;
		else current_player = 1;
	}
}