package is2017.kr.ac.korea.ccs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

class MyFrame implements ActionListener
{
	private JFrame frm = new JFrame();
	private JFileChooser fileChooser = new JFileChooser();
	private JPanel northPanel = new JPanel();
	private JPanel westPanel = new JPanel();
	private JPanel eastPanel = new JPanel();
	private JButton openBtn = new JButton();
	private JButton saveBtn = new JButton();
	private JButton choosefileBtn = new JButton();
	private JButton doBtn = new JButton();
	private JRadioButton b64Radio = new JRadioButton();
	private JRadioButton aesRadio = new JRadioButton();
	private ButtonGroup radioGroup = new ButtonGroup();
	private JLabel fileLabel = new JLabel();
	private JLabel cryptLabel = new JLabel();
	private JTextField filename = new JTextField();
	private JTextField address = new JTextField();

	private File choosedFile = null;
	private String whatRadio = null;


	public MyFrame()
	{
		//기본 컴포넌트 설정
		fileLabel.setText("파일 경로");
		cryptLabel.setText("암호화 방식");
		address.setColumns(30);
		filename.setColumns(30);
		filename.setEditable(false);
		choosefileBtn.setText("파일 선택");
		doBtn.setText("Just Do it");
		openBtn.setText("열기");
		saveBtn.setText("변경 후 저장");
		b64Radio.setText("base64");
		aesRadio.setText("AES");
		radioGroup.add(b64Radio);
		radioGroup.add(aesRadio);

		//액션 리스너 장착
		openBtn.addActionListener(this);
		saveBtn.addActionListener(this);
		choosefileBtn.addActionListener(this);
		doBtn.addActionListener(this);

		//각 패널 설정 및 컴포넌트 장착
		northPanel.add(choosefileBtn);
		northPanel.add(filename);

		westPanel.add(cryptLabel);
		westPanel.add(b64Radio);
		westPanel.add(aesRadio);

		eastPanel.add(doBtn);

		//프레임에 패널 장착
		frm.add(northPanel, "North");
		frm.add(westPanel, "West");
		frm.add(eastPanel, "East");
		//기본 프래임 셋팅
		frm.setTitle("ccs.korea.ac.kr Term Project");
		frm.setLocation(120, 120);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.pack();
		frm.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		if(e.getSource() == choosefileBtn) {
			int returnVal = fileChooser.showOpenDialog(frm);
			if( returnVal == JFileChooser.APPROVE_OPTION)
			{
				//열기 버튼을 누르면
				choosedFile = fileChooser.getSelectedFile();
				filename.setText(choosedFile.toString());
			}
			else
			{
				//취소 버튼을 누르면
				System.out.println("취소합니다");
			}
		}
		else if(e.getSource() == doBtn) {
			Enumeration<AbstractButton> enums = radioGroup.getElements();
			while(enums.hasMoreElements()) {
				JRadioButton jb = (JRadioButton)(enums.nextElement());
				if(jb.isSelected()){
					whatRadio = jb.getText();
					if(whatRadio.equals("base64")){
						JavaEncryptString.main(new String[]{"-b", choosedFile.getName().replace(".java", "")});
						break;
					}
					else if(whatRadio.equals("AES")){
						JavaEncryptString.main(new String[]{"-a", choosedFile.getName().replace(".java", "")});
						break;
					}
				}
			}
		}
	}
}

public class MainUI
{
	public static void main(String[] args)
	{
		MyFrame my = new MyFrame();
	}
}