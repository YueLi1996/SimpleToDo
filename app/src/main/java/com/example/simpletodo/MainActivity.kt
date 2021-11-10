package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils.writeLines
import org.apache.commons.io.FileUtils.readLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // remove item from list
                listOfTasks.removeAt(position)
                // Notify adapter that data set changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        loadItems()

        //Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // craete adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(LinearLayoutManager(this));

        val inputTextField = findViewById<EditText>(R.id.editText)
        // set up the button and input field function
        findViewById<Button>(R.id.button).setOnClickListener {
            // grab text from text field
            val userInputtedTask = inputTextField.text.toString()

            // add string to list of tasks
            listOfTasks.add(userInputtedTask)

            // Notify the adapter that data is updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save the data the user has inputted by writing and reading from a file

    // Create a method to get file we need
    fun getDataFile() : File {

        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in data file
    fun loadItems() {
        try {
            listOfTasks = readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save items by writing them into file
    fun saveItems() {
        try{
            writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}