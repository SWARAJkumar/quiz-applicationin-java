import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import javax.sound.midi.*;

class Question implements Serializable{
	String description;
	String option1,option2,option3,option4;
	int answer;
	transient int userAnswer;
	transient Scanner s=new Scanner(System.in);
	void getData(){
		System.out.println(description+"?\n"+option1+"\t"+option2+"\n"+option3+"\t"+option4);
	}
	void setData(){
		System.out.println("enter the question followed by options and finally the answer :");
		description=s.nextLine();
		option1=s.nextLine();
		option2=s.nextLine();
		option3=s.nextLine();
		option4=s.nextLine();
		answer=s.nextInt();
	}
	void getUserAnswer(){
		Scanner s=new Scanner(System.in);
		userAnswer=s.nextInt();
	}

	boolean check(){
		if(answer==userAnswer)
			return true;
		else
			return false;
	}
}

class Read{
	void readQuestion(){
		try{
			FileInputStream fin = new FileInputStream("quiz.ser");
			ObjectInputStream ofin = new ObjectInputStream(fin);
					Object a=ofin.readObject();
					Question q1=(Question)a;
					q1.getData();
					q1.getUserAnswer();
					boolean temp=q1.check();
					if(temp)
						System.out.println("correct answer");
					else
						System.out.println("Wrong answer");
			ofin.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

class Write{
	void writeQuestion(){
		try{
			FileOutputStream fout = new FileOutputStream("quiz.ser");
			ObjectOutputStream ofout = new ObjectOutputStream(fout);
			Question q=new Question();
				q.setData();
				ofout.writeObject(q);

			Question q2=new Question();
				q2.setData();
				ofout.writeObject(q2);
				ofout.close();		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
class MidiMusic{
	void play() throws Exception{
		Sequencer player=MidiSystem.getSequencer();
		player.open();
		Sequence sequence = new Sequence(Sequence.PPQ,4);
		Track track = sequence.createTrack();

		ShortMessage message1 = new ShortMessage();
		message1.setMessage(144,2,34,100);
		MidiEvent event1 = new MidiEvent(message1,1);
		track.add(event1);
		ShortMessage message2 = new ShortMessage();
		message2.setMessage(144,1,43,100);
		MidiEvent event2 = new MidiEvent(message2,5);
		track.add(event2);

		ShortMessage message = new ShortMessage();
		message.setMessage(144,1,99,100);
		MidiEvent event = new MidiEvent(message,10);
		track.add(event);
		player.setSequence(sequence);
		player.start();
	}
}

class Panel extends JPanel{
	public void paintComponent(Graphics g){
		Graphics2D graphics2d =  (Graphics2D)(g);
		GradientPaint paint = new GradientPaint(70,70,Color.yellow,100,100,Color.black);
		graphics2d.setPaint(paint);
		g.fillRect(0,0,400,400);
		System.out.println("outer panel");

	}
}
 class PanelInner extends JPanel{
		public void paintComponent(Graphics g){
			Graphics2D graphics2d =  (Graphics2D)(g);
			GradientPaint paint = new GradientPaint(70,70,Color.orange,100,100,Color.red);
			graphics2d.setPaint(paint);
			g.fillRect(0,0,300,300);
		System.out.println("inner panel");

		}
	}

class Display extends Question implements ActionListener{
	int alreadySelected=-1;
	JCheckBox[] box = new JCheckBox[4];
	Question[] q1=new Question[3];
	int count;
 int p=0;

	
	
		public class CheckBoxEvent implements ItemListener{
			CheckBoxEvent(){
				System.out.println("i am check box listner");
			}

			public void itemStateChanged(ItemEvent e){
			
				if(alreadySelected!=-1){
					box[alreadySelected].setSelected(false);
					box[alreadySelected].setBackground(Color.orange);
					alreadySelected=-1;
				}
				for(int i=0;i<4;i++){
					if(box[i].isSelected()){
						box[i].setBackground(Color.blue);
						alreadySelected=i;
					}
				}
			}
		}
	public void actionPerformed(ActionEvent e){
		int score=0;
		
		try{
			MidiMusic m=new MidiMusic();
			m.play();
		}catch(Exception exp){
			exp.printStackTrace();
		}
		if(q1[count].answer-1==alreadySelected){
			score++;
			box[alreadySelected].setBackground(Color.green);
		}
		else{
			box[alreadySelected].setBackground(Color.red);
			box[q1[count].answer-1].setBackground(Color.green);
		}
		//create a dialog box to give the score and also save the score and date in a separate file for history(as on 27/7/16)			
	}
	public void gui() throws Exception{

		FileInputStream fin = new FileInputStream("quiz.ser");
		ObjectInputStream ofin = new ObjectInputStream(fin);
		Object a;
		for(count=0;count<2;count++){
		JFrame frame = new JFrame("quiz: Question "+(count+1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CheckBoxEvent e=new CheckBoxEvent();
		alreadySelected=-1;
		a=ofin.readObject();

		q1[count]=(Question)a;
		Panel panel=new Panel();
		PanelInner pInner= new PanelInner();
		JButton button = new JButton("SUBMIT");
		JTextArea area = new JTextArea(1,1);
		JScrollPane pane = new JScrollPane(area);

		box[0]=new JCheckBox(q1[count].option1);
		box[1]=new JCheckBox(q1[count].option2);
		box[2]=new JCheckBox(q1[count].option3);
		box[3]=new JCheckBox(q1[count].option4);
			
			
		area.setFont(new Font("sansserif",Font.BOLD,18));
		area.setBackground(new Color(103,99,99));
		button.addActionListener(this);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		area.setLineWrap(true);
		area.append(q1[count].description);
		area.setBounds(10,10,350,50);
		button.setBackground(new Color(48,156,213));
		panel.add(area);
		pInner.setLayout(new GridLayout(2,2));
		pInner.setBounds(10,150,400,150);
		panel.add(button);
		button.setBounds(140,320,100,40);

		for(int i=0;i<4;i++){
			box[i].addItemListener(e);
			box[i].setBackground(Color.orange);
			pInner.add(box[i]);
		}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		panel.add(pInner);
		panel.setLayout(null);

		frame.setLayout(null);
		panel.setBounds(0,0,400,400);
		frame.setSize(400,400);
		frame.setVisible(true);
		try{
			Thread.sleep(10000);
		}catch(Exception ep){
			ep.printStackTrace();
		}
		p++;
		frame.setVisible(false);
		frame.dispose();
		//area.setText("");
	}//end of for loop
	}
}

class EntrancePanel implements ActionListener{
		JFrame frame = new JFrame("Quiz");
		boolean startQuiz=false;
		Display d=new Display();


	class ActionEvent1 implements ActionListener{
		synchronized public void actionPerformed(ActionEvent e){
		Display d1=new Display();

			frame.setVisible(false);
			frame.dispose();
			startQuiz=true;
			System.out.println(startQuiz);
			try{
				//notify();
				d1.gui();
			System.out.println(startQuiz);

			}catch(Exception et){
				et.printStackTrace();
			}
		}
	}
	synchronized public void actionPerformed(ActionEvent e){
		Display d=new Display();

			frame.setVisible(false);
			frame.dispose();
			startQuiz=true;
			System.out.println(startQuiz);
			try{

				//notify();
			System.out.println("i am stuck");

			}catch(Exception et){
				et.printStackTrace();
			}
		}

	 void dis(){

		Panel panel=new Panel();
		JButton player = new JButton("player");
		JButton admin = new JButton("adminstrator");
		panel.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		panel.setBounds(0,0,400, 400);
		player.setBounds(170,100,100,50);
		admin.setBounds(170,180,100,50);
		panel.add(player);
		panel.add(admin);
		player.addActionListener(this);
		admin.addActionListener(new ActionEvent1());
		frame.add(panel);
		frame.setVisible(true);
		synchronized(this){
			try{
				wait(10000);
				d.gui();

			}catch(Exception et){
				et.printStackTrace();
			}
		}
	}
}
class Quiz {
	public static void main(String[] args)throws Exception{

		EntrancePanel e=new EntrancePanel();
		e.dis();
	}
}