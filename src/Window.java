import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

public class Window extends Canvas{

  private JTextField username = new JTextField(15);
  private JTextField password = new JPasswordField (15);
  private JLabel header = new JLabel("<html><span style='font-size:22px;'>"+"Dokpi"+"</span></html>", JLabel.CENTER);
  private JLabel lbl1 = new JLabel("<html><span style='font-size:14px;'>"+"User Name"+"</span></html>", JLabel.CENTER);
  private JLabel lbl2 = new JLabel("<html><span style='font-size:14px;'>"+"Password"+"</span></html>", JLabel.CENTER);
  private JButton button = new JButton("Login");
  private JButton msgSend = new JButton(); //invisible button for message sending
  protected boolean msgFocus = false; //focus bool for text box
  protected static JTextField userInput = new JTextField();
  private JFrame frame = new JFrame("Dokpi");
  private Container pane = new Container();
  private JPanel panel = new JPanel();
  private LayoutManager overlay = new OverlayLayout(panel);

  public Window(int w, int h, Dokpi dokpi)
  {
    frame.setSize(new Dimension(w,h));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setFocusable(true);
    frame.setVisible(true);

    pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    header.setAlignmentX(frame.CENTER_ALIGNMENT);
    lbl1.setAlignmentX(frame.CENTER_ALIGNMENT);
    lbl2.setAlignmentX(frame.CENTER_ALIGNMENT);
    username.setAlignmentX(frame.CENTER_ALIGNMENT);
    password.setAlignmentX(frame.CENTER_ALIGNMENT);
    button.setAlignmentX(frame.CENTER_ALIGNMENT);

    username.setMaximumSize(new Dimension(w/5, 28));
    password.setMaximumSize(new Dimension(w/5, 28));

    pane.add(Box.createVerticalStrut(h/6));
    pane.add(header);
    pane.add(Box.createVerticalStrut(25));
    pane.add(lbl1);
    pane.add(Box.createVerticalStrut(5));
    pane.add(username);
    pane.add(Box.createVerticalStrut(25));
    pane.add(lbl2);
    pane.add(Box.createVerticalStrut(5));
    pane.add(password);
    pane.add(Box.createVerticalStrut(25));
    pane.add(button);

    frame.add(pane);

    frame.getRootPane().setDefaultButton(button);

    panel.setLayout(overlay);
    panel.setOpaque(false);
    panel.setSize(new Dimension(w, h));

    userInput.setEditable(true);
    userInput.setMaximumSize(new Dimension((int)(w*0.4)-75-1, 25));
    userInput.setAlignmentX(0.0f);
    userInput.setAlignmentY(1.0f);
    userInput.setBorder(null);
    userInput.setForeground(Color.BLACK);;
    userInput.setBackground(new Color(0xcea03b));
    /*
    NOT WORKING CURRENTLY (the insets)
     */
    userInput.setMargin(new Insets(0, 10, 0, 0));

    userInput.addFocusListener(new FocusListener() {

      @Override
      public void focusGained(FocusEvent e) {
        Dokpi.setFocus(true);
      }

      @Override
      public void focusLost(FocusEvent e) {
        //Dokpi.setFocus(false);
      }
    });

    panel.add(userInput);
    panel.add(dokpi);

    button.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        String user = username.getText().toString();
        String pass = password.getText().toString();

        System.out.println("Attempting to login in for user: " + user + "\nWith password: " + pass);

        //check in db if matches

        System.out.println("Login successful.");

        //remove login content pane
        frame.remove(pane);

        frame.add(msgSend);
        frame.getRootPane().setDefaultButton(msgSend);

        System.out.println("Starting up Dokpi.");
        /*if(dokpi.login())
        {
          //remove login stuff, start game

          dokpi.start();
        }*/
        dokpi.start();

        frame.add(panel, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
      }
    });

    msgSend.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        //person was already focused on it
        if(msgFocus)
        {
          String text = userInput.getText();
          //don't send empty strings
          if(!text.equals("")) {
            InWindow.Message msg = new InWindow.Message(text, "Me", Dokpi.getHour(), Dokpi.getMinute(), w / 2, h / 2);
            InWindow.addText(msg);
            userInput.setText("");
          }
        }
        else
        {
          setFocus(true);
          userInput.grabFocus();
        }
      }
    });
  }

  public void setFocus(boolean focus)
  {
    if(focus)
    {
      userInput.grabFocus();
    }
    msgFocus = focus;
    Dokpi.setFocus(msgFocus);
  }
}
