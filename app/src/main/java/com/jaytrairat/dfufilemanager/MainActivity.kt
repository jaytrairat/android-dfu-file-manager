package com.jaytrairat.dfufilemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var currentDirectory: File
    private lateinit var pathTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView = findViewById<GridView>(R.id.grid_view)
        pathTextView = findViewById<TextView>(R.id.url_path)

        val homeDirectory = File(System.getProperty("user.home"))
        currentDirectory = homeDirectory


        Log.e("DEB", currentDirectory.path)
        loadFiles()

        // Set the path text view to the current directory
        pathTextView.text = currentDirectory.path
    }

    private fun loadFiles() {
        val defaultFolder = File("/storage/emulated/0")
        val files = defaultFolder.listFiles()?.toList() ?: emptyList()
//        val files = currentDirectory.listFiles()?.toList() ?: emptyList()
        val adapter = FileGridAdapter(files)

        // Set the adapter for the grid view
        gridView.adapter = adapter
    }

    class FileGridAdapter(private val files: List<File>) : BaseAdapter() {
        override fun getCount() = files.size

        override fun getItem(position: Int) = files[position]

        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val file = files[position]
            val view = convertView ?: LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_file, parent, false)
            view.findViewById<TextView>(R.id.file_name).text = file.name
            view.findViewById<ImageView>(R.id.file_icon).setImageResource(getFileIcon(file))
            return view
        }

        private fun getFileIcon(file: File): Int {
            return when {
                file.isDirectory -> R.drawable.ic_folder
                file.extension.equals("jpg", true) || file.extension.equals(
                    "png",
                    true
                ) -> R.drawable.ic_image
                file.extension.equals("mp3", true) -> R.drawable.ic_audio
                file.extension.equals("mp4", true) -> R.drawable.ic_video
                else -> R.drawable.ic_file
            }
        }
    }
}