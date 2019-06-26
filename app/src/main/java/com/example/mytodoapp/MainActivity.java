 package com.example.mytodoapp;

 import android.os.Bundle;
 import android.util.Log;
 import android.view.View;
 import android.widget.AdapterView;
 import android.widget.ArrayAdapter;
 import android.widget.EditText;
 import android.widget.ListView;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;

 import org.apache.commons.io.FileUtils;

 import java.io.File;
 import java.io.IOException;
 import java.nio.charset.Charset;
 import java.util.ArrayList;

 public class MainActivity extends AppCompatActivity {

    // declaring stateful objects here; these will be null before onCreate is called
    ArrayList<String> items ;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // obtain a reference to elements the ListView created with layout
        lvItems = (ListView) findViewById(R.id.lvItems);
        // obtain a reference to string in addBar created with layout
        etNewItem = (EditText) findViewById(R.id.addBar);
        // initialize items list
        readItems();
        // initialize adapter using the items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        // wires the adapter to view
        lvItems.setAdapter(itemsAdapter);



        // mock data
        // items.add("Clean Room");


        //set up listener on creation
        setupListViewListener();
    }

    public void setupListViewListener() {

        // set the ListView's itemLongClickListener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // removes the item in the list at the index given position
                items.remove(position);
                //notify the adapter that the underlying dataset changed
                itemsAdapter.notifyDataSetChanged();
                // similar to toast, log.i gives more info (alerts item was deleted)
                Log.i("MainActivity","Item deleted at position " + position);
                // store updated list
                writeItems();
                // return true to tell framework that long click was consumed
                return true;
            }
        });
    }

    public void onAddItem (View v) {

        // grabs the EditText content as string
        String itemText = etNewItem.getText().toString();

        // adds item text to list via adapter
        itemsAdapter.add(itemText);

        //store updated list
        writeItems();

        // clear the EditText by setting it to an empty string
        etNewItem.setText("");

        // display alert to user
        Toast.makeText(getApplicationContext(), "Item added!", Toast.LENGTH_SHORT).show();
    }

    //persistence helper functions below....

     // returns the file in which the data is stored

     private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
     }

     //read the items rom the file system
     private void readItems() {
        try {
            // create array using content in the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // print error
             e.printStackTrace();
            //load new list
            items = new ArrayList<>();
         };
     }

     private void writeItems() {
        try {
            // save item list as line delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            // print the error to console
            e.printStackTrace();
        }
     }
}
