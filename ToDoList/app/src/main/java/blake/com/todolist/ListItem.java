package blake.com.todolist;

/**
 * Created by Raiders on 3/2/16.
 */
public class ListItem {

    private String itemText;
    private boolean isStuckThrough;

    public ListItem(String itemText, boolean isStuckThrough) {
        this.itemText = itemText;
        this.isStuckThrough = isStuckThrough;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public boolean isStuckThrough() {
        return isStuckThrough;
    }

    public void setIsStuckThrough(boolean isStuckThrough) {
        this.isStuckThrough = isStuckThrough;
    }
}
