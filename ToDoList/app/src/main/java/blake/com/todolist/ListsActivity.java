package blake.com.todolist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

/**
 * Created by Raiders on 3/1/16.
 */
public class ListsActivity extends AppCompatActivity {

    EditText itemEntryET;
    FloatingActionButton addItemFAB;
    ArrayAdapter<String> itemsArrayAdapter;
    LinkedList<String> itemsLinkedList = new LinkedList<>();
    ListView listViewItems;
    TextView titleOfToDoList;
    Button backToMainButton;
    Intent intent;
    Button instructionsButton;
    Snackbar undoSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists_activity);

        instantiateMethods();
        changeTitleText();
        setOnClickListenerFAB();
        setOnItemListLongClick();
        setBackToMainButton();
        setItemsArrayAdapter();
        completedTask();
        setInstructionsButton();
    }

    private void instantiateMethods() {
        itemEntryET = (EditText) findViewById(R.id.listItems_edittext);
        titleOfToDoList = (TextView) findViewById(R.id.listTitle);
        backToMainButton = (Button) findViewById(R.id.backToAllLists_button);
        addItemFAB = (FloatingActionButton) findViewById(R.id.fabListItems);
        listViewItems = (ListView) findViewById(R.id.items_listView);
        instructionsButton = (Button) findViewById(R.id.instructionsItem);
    }

    private void fillListItems() {
        String listItems = itemEntryET.getText().toString();
        if (listItems.isEmpty()) {
            Toast.makeText(ListsActivity.this, "Please enter item", Toast.LENGTH_SHORT).show();
        }
        else {
            itemsLinkedList.add(listItems);
        }
    }

    private void setItemsArrayAdapter() {
        itemsArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, itemsLinkedList);
        listViewItems.setAdapter(itemsArrayAdapter);
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
        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                String itemToBeRemoved = itemsArrayAdapter.getItem(position);
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
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeColor(view);
            }
        });
    }

    private void changeColor(View view) {
        view.setBackgroundColor(Color.GREEN);
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

    private void setUndoSnackBar(final View view) {
        undoSnackBar = Snackbar.make(view, "List is deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbar1 = Snackbar.make(view, "List is restored!", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                });
        undoSnackBar.show();
    }
}


