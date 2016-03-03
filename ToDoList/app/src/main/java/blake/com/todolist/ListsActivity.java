package blake.com.todolist;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Raiders on 3/1/16.
 */
public class ListsActivity extends AppCompatActivity {

    private static final String TAG = "ListsActivity: ";

    EditText itemEntryET;
    FloatingActionButton addItemFAB;
    ListItemAdapter itemsArrayAdapter;
    ArrayList<ListItem> itemsList = new ArrayList<>();
    ListView listView;
    TextView titleOfToDoList;
    ImageButton backToMainButton;
    Intent intent;
    Button instructionsButton;
    Snackbar undoSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists_activity);

        instantiateViewElements();
        changeTitleText();
        setOnClickListenerFAB();
        setOnItemListLongClick();
        setBackToMainButton();
        setItemsArrayAdapter();
        completedTask();
        setInstructionsButton();
    }

    private void instantiateViewElements() {
        itemEntryET = (EditText) findViewById(R.id.listItems_edittext);
        titleOfToDoList = (TextView) findViewById(R.id.listTitle);
        backToMainButton = (ImageButton) findViewById(R.id.backToAllLists_button);
        addItemFAB = (FloatingActionButton) findViewById(R.id.fabListItems);
        listView = (ListView) findViewById(R.id.items_listView);
        instructionsButton = (Button) findViewById(R.id.instructionsItem);
    }

    private void fillListItems() {
        String listItemString = itemEntryET.getText().toString();
        if (listItemString.isEmpty()) {
            Toast.makeText(ListsActivity.this, "Please enter item", Toast.LENGTH_SHORT).show();
        }
        else {
            ListItem listItem = new ListItem(listItemString, false);
            itemsList.add(listItem);
        }
    }

    private void setItemsArrayAdapter() {
        itemsArrayAdapter = new ListItemAdapter(this, itemsList);
        listView.setAdapter(itemsArrayAdapter);
    }

    private void setOnClickListenerFAB() {
        addItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillListItems();
                itemsArrayAdapter.notifyDataSetChanged();
                itemEntryET.getText().clear();
            }
        });
    }

    private void setOnItemListLongClick() {

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                ListItem itemToBeRemoved = itemsArrayAdapter.getItem(position);
                itemsArrayAdapter.remove(itemToBeRemoved);
                setUndoSnackBar(view);
                itemsArrayAdapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    private void changeTitleText() {
        String extra = getIntent().getStringExtra("Title");
        titleOfToDoList.setText(extra);
    }

    private  void setBackToMainButton() {
        intent = new Intent(this, MainActivity.class);
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    private void completedTask() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView strikeThroughView = (TextView) view.findViewById(R.id.item_text);
                Log.d(TAG, "Clicked item at pos: " + position);
                ListItem listItem = itemsList.get(position);
                if (listItem.isStuckThrough()){
                    strikeThroughView.setPaintFlags(0);
                    listItem.setIsStuckThrough(false);
                } else {
                    strikeThroughView.setPaintFlags(strikeThroughView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    listItem.setIsStuckThrough(true);
                }
            }
        });

    }

    private void setInstructionsButton() {
        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListsActivity.this, "Tap Item to mark as complete \nLong tap to delete item",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUndoSnackBar(View view) {
        undoSnackBar = Snackbar.make(view, "Item is deleted", Snackbar.LENGTH_LONG)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                    }
                })
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbar1 = Snackbar.make(v, "Item is restored!", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }

                });

        undoSnackBar.show();
    }
}


