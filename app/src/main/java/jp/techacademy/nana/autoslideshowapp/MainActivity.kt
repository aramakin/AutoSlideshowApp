package jp.techacademy.nana.autoslideshowapp

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.sql.Types.NULL
import kotlin.concurrent.timer



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //パーミッションの判定・許可
        val PERMISSIONS_REQUEST_CODE = 100

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // 許可されている
            getContentsInfo()
        } else {
            // 許可されていないので許可ダイアログを表示
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_CODE
            )
            Log.d("TEST", "NO")
        }

        //ページの自動送り


        //進むボタン
        move.setOnClickListener {
           moveContentsInfo()

            Log.d("TEST", "start")

        }


        //戻るボタン
        back.setOnClickListener {
            backContentsInfo()
            Log.d("TEST", "back")
        }

        //再生停止ボタン
        start_stop.setOnClickListener {
        //    timer.strat()
        //    mTimer!!.cancel()

        //}, 100, 100) 
            Log.d("TEST", "start_stop")
        }

    }


    private fun getContentsInfo() {
        // 画像の情報を取得する
       var cursor =contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        )

            if (cursor!!.moveToFirst()) {
            // indexからIDを取得し、そのIDから画像のURIを取得する
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            Log.d("error", "画像を取得した")
            imageView.setImageURI(imageUri)
        }

        cursor.close()
    }


    //進む
    private fun moveContentsInfo() {
        var cursor =contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        )


        if (cursor!!.moveToNext()) {
            cursor.moveToNext()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                 imageView.setImageURI(imageUri)

                Log.d("test", imageUri.toString())
        }
        cursor.close()
    }


    //戻る
    private fun backContentsInfo() {
        // 画像の情報を取得する

        var cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        )



      if (cursor.moveToFirst()) {
            do {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                Log.d("TEST", "URI : " + imageUri.toString())
            } while (cursor.moveToPosition(-1))
        }
        cursor.close()
    }
}
