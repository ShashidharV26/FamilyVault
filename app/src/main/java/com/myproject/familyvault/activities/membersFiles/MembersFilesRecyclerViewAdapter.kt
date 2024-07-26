package com.myproject.familyvault.activities.membersFiles


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.myproject.familyvault.R
import com.myproject.familyvault.utils.AppData
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


internal class MembersFilesRecyclerViewAdapter(private var context : Context, private var filesList : MutableList<String>, private var selectedMember : String) :
    RecyclerView.Adapter<MembersFilesRecyclerViewAdapter.MemberFilesViewHolder>() {
    internal inner class MemberFilesViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val fileName : TextView = view.findViewById(R.id.file_name)
        val fileImg : ImageView = view.findViewById(R.id.file_img)
        val fileCardView : CardView = view.findViewById(R.id.file_cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersFilesRecyclerViewAdapter.MemberFilesViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.custom_members_file_layout,parent,false)
        return MemberFilesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MembersFilesRecyclerViewAdapter.MemberFilesViewHolder, position: Int) {
        val files = filesList[position]
        if(files.endsWith(".jpg")){
            holder.fileName.text = files
            setImage(files,holder.fileImg)
        }else if(files.endsWith(".pdf")){
            generateThumbnailFromPDF(files,holder.fileImg)
        }

       holder.fileCardView.setOnClickListener(View.OnClickListener {

       } )

    }

    override fun getItemCount(): Int {
       return filesList.size
    }

    private fun setImage(imageName: String,image : ImageView) {
        try {
            val path = AppData.EXTERNAL_STORAGE_PUBLIC_DIRECTORY.toString()
            val f = File(path+"/"+AppData.appName+"/"+selectedMember, imageName)
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            image.setImageBitmap(b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }


    private fun generateThumbnailFromPDF(fileName: String?,image : ImageView) {
        var pdfRenderer: PdfRenderer? = null;
        var pdfFileDescriptor: ParcelFileDescriptor? = null;
        try {
            val path = AppData.EXTERNAL_STORAGE_PUBLIC_DIRECTORY.toString()
            val f = path+"/"+AppData.appName+"/"+selectedMember+"/"+fileName
            //Open the PDF file descriptor
            //here "r" = read.
            pdfFileDescriptor =
                context.contentResolver.openFileDescriptor(Uri.parse(f), "r")
            if (pdfFileDescriptor != null) {
                // Create a PdfRenderer from the file descriptor
                pdfRenderer = PdfRenderer(pdfFileDescriptor)
                // Ensure the page index is within bounds
                // i want to create page 0 thumbnail so given 0 if you want  other you can give according to you
                val pageIndex = 0;
                if (pageIndex < pdfRenderer.pageCount) {
                    val page = pdfRenderer.openPage(pageIndex)
                    // Create a bitmap for the thumbnail
                    val thumbnail = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
                    // Render the page to the bitmap
                    page.render(thumbnail, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    // Close the page and renderer
                    page.close()
                    pdfRenderer.close()
                    image.setImageBitmap(thumbnail)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                pdfFileDescriptor?.close()
            } catch (e: Exception) {

            }
        }

    }
}