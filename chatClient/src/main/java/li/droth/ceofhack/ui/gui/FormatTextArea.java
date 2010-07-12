package li.droth.ceofhack.ui.gui;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: droth
 * Date: Jul 10, 2010
 * Time: 1:32:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class FormatTextArea extends JTextArea{
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            
    public FormatTextArea() {
        super();

    }

    public void setChatText(String newText) {
        Date date = new Date(System.currentTimeMillis());

        setText(getText() + "\n" + format.format(date) + "  " +newText);

    }
}
