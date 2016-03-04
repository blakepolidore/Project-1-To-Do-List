package blake.com.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    ArrayAdapter<String> itemsArrayAdapter;
    ArrayList<String> itemsList = new ArrayList<>();
    ListView listView;
    TextView titleOfToDoList;
    ImageButton backToMainButton;
    Button instructionsButton;
    Snackbar undoSnackBar;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists_activity);

        instantiateViewElements();
        setOnClickListenerFAB();
        changeTitleText();
        setOnItemListLongClick();
        setBackToMainButton();
        itemsList = getData();
        index = getDataIndex();
        setItemsArrayAdapter();
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
            itemsList.add(listItemString);
        }
    }

    private void setItemsArrayAdapter() {
        itemsArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, itemsList);
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

        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendListBack();
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

    private void setUndoSnackBar(View view) { //Doesn't actually work for undoing an action
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

    private ArrayList<String> getData(){
        Intent listIntent = getIntent();
        if (listIntent == null){
            return new ArrayList<>();
        }
        return listIntent.getStringArrayListExtra(MainActivity.DATA_KEY);
    }

    private int getDataIndex(){
        Intent intent2 = getIntent();
        if (intent2 == null){
            return MainActivity.ERROR_INDEX;
        }
        return intent2.getIntExtra(MainActivity.DATA_INDEX_KEY, MainActivity.ERROR_INDEX);
    }

    private void sendListBack(){
        Intent intent1 = getIntent();
        if (intent1 == null){
            return;
        }
        intent1.putExtra(MainActivity.DATA_KEY, itemsList);
        intent1.putExtra(MainActivity.DATA_INDEX_KEY, index);
        setResult(RESULT_OK, intent1);
        finish();
    }

    @Override
    public void onBackPressed() {
        sendListBack();
    }

}


