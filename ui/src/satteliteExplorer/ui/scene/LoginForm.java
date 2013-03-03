package satteliteExplorer.ui.scene;

import satteliteExplorer.db.EntityContext;
import satteliteExplorer.db.entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: т
 * Date: 19.12.12
 * Time: 21:53
 * To change this template use File | Settings | File Templates.
 */
public class LoginForm extends JDialog implements ActionListener {
  private JButton SUBMIT;
  private JPanel panel;
  private JLabel label1, label2;
  private final JTextField text1, text2;
  private User user;

  public LoginForm() {
    setSize(new Dimension(300, 120));
    label1 = new JLabel();
    label1.setText("Логин:");
    text1 = new JTextField(15);

    label2 = new JLabel();
    label2.setText("Пароль:");
    text2 = new JPasswordField(15);

    SUBMIT = new JButton("Принять");

    panel = new JPanel(new GridLayout(3, 1));
    panel.add(label1);
    panel.add(text1);
    panel.add(label2);
    panel.add(text2);
    panel.add(SUBMIT);
    add(panel, BorderLayout.CENTER);
    SUBMIT.addActionListener(this);
    setTitle("Авторизация");
  }

  public void actionPerformed(ActionEvent ae) {
    String login = text1.getText();
    String password = text2.getText();
    user = findUser(login, password);
    if (user == null) {
      JOptionPane.showMessageDialog(this, "Неверный логин или пароль",
          "Error", JOptionPane.ERROR_MESSAGE);
      user = null;
    } else {
      LoginForm.this.setVisible(false);
    }
  }

  private User findUser(String login, String password) {
    Collection<Object> users = EntityContext.get().getAllEntities(User.class);
    for (Object o : users) {
      User user = (User) o;
      if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
        return user;
      }
    }
    return null;
  }

  public User getUser() {
    return user;
  }
}
