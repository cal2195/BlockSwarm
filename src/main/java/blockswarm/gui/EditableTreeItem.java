package blockswarm.gui;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 *
 * @author cal
 */
public class EditableTreeItem extends TreeCell<String>
{

    private TextField textField;
    FXMLController controller;

    public EditableTreeItem(FXMLController controller)
    {
        this.controller = controller;
    }

    @Override
    public void startEdit()
    {
        super.startEdit();

        if (textField == null)
        {
            createTextField();
        }
        textField.setText(getString());
        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    @Override
    public void cancelEdit()
    {
        super.cancelEdit();
        setText(getItem());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    public void updateItem(String item, boolean empty)
    {
        super.updateItem(item, empty);

        if (empty)
        {
            setText(null);
            setGraphic(null);
        } else if (isEditing())
        {
            if (textField != null)
            {
                textField.setText(getString());
            }
            setText(null);
            setGraphic(textField);
        } else
        {
            setText(getString());
            setGraphic(getTreeItem().getGraphic());
        }
    }

    private void createTextField()
    {
        textField = new TextField(getString());
        textField.setOnKeyReleased(new EventHandler<KeyEvent>()
        {

            @Override
            public void handle(KeyEvent t)
            {
                if (t.getCode() == KeyCode.ENTER)
                {
                    commitEdit(textField.getText());
                    controller.generateSearchTerms();
                } else if (t.getCode() == KeyCode.ESCAPE)
                {
                    cancelEdit();
                }
            }
        });
    }

    private String getString()
    {
        return getItem() == null ? "" : getItem().toString();
    }

}
