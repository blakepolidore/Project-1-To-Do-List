package blake.com.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText listNameEntryET;
    FloatingActionButton addListFAB;
    ArrayAdapter<String> nameOfListsArrayAdapter;
    ArrayList<String> listArrayList = new ArrayList<>();
    ListView listViewMain;
    Intent intent;
    Button instructionsButton;
    Snackbar undoSnackBar;
    private ArrayList<String> myDataList;
    private static final int MAIN_REQUEST_CODE = 27;
    public static final String DATA_KEY = "myDataKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiateMethods();
        setNameOfListsArrayAdapter();
        setOnClickListenerFAB();
        setOnItemListLongClick();
        toList();
        setInstructionsButton();

    }

    private void instantiateMethods() {
        listNameEntryET = (EditText) findViewById(R.id.listName_edittext);
        addListFAB = (FloatingActionButton) findViewById(R.id.fabLists);
        listViewMain = (ListView) findViewById(R.id.listNames_listView);
        instructionsButton = (Button) findViewById(R.id.instructionsMain);
    }

    private void fillListofLists() {
        String listName = listNameEntryET.getText().toString();
        if (listName.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter list name", Toast.LENGTH_SHORT).show();
        } else {
            listArrayList.add(listName);
        }
    }

    private void setNameOfListsArrayAdapter() {
        nameOfListsArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, listArrayList);
        listViewMain.setAdapter(nameOfListsArrayAdapter);
    }

    private void setOnClickListenerFAB() {
        addListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillListofLists();
                nameOfListsArrayAdapter.notifyDataSetChanged();
                listNameEntryET.getText().clear();
            }
        });
    }

    private void setOnItemListLongClick() {
        listViewMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String listToBeRemoved = nameOfListsArrayAdapter.getItem(position);
                nameOfListsArrayAdapter.remove(listToBeRemoved);
                setUndoSnackBar(view);
                nameOfListsArrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void toList() {
        intent = new Intent(this, ListsActivity.class);
        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listTitle = (String) parent.getAdapter().getItem((position));
                intent.putExtra("Title", listTitle);
                intent.putExtra(DATA_KEY, listArrayList);
                startActivityForResult(intent, MAIN_REQUEST_CODE);
            }
        });
    }

    private void setInstructionsButton() {
        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Tap List Name to access list \nLong tap to delete list",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUndoSnackBar(View view) {
        undoSnackBar = Snackbar.make(view, "List is deleted", Snackbar.LENGTH_LONG)
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
                        Snackbar snackbar1 = Snackbar.make(v, "List is restored!", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }

                });

        undoSnackBar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MAIN_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                if (data != null) {
                    myDataList = data.getStringArrayListExtra(DATA_KEY);
                }
            } else  if (requestCode == RESULT_CANCELED){
                Log.w("Main", "Failed to get new list back");
            }
        }
    }
}
