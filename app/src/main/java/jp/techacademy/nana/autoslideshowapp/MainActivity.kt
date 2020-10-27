package jp.techacademy.nana.autoslideshowapp

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var cursor: Cursor

    // タイマー
    private var mTimer: Timer? = null

    private var mTimerSec = 0.0

    private var mHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //パーミッションの判定・許可
        val PERMISSIONS_REQUEST_CODE = 100


            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {

                // 許可されていない＝許可ダイアログを表示
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
        //進むボタン
        move.setOnClickListener {
            moveContentsInfo()

            Log.d("TEST", "move")

        }

        //戻るボタン
        back.setOnClickListener {
            backContentsInfo()
            Log.d("TEST", "back")
        }

        //再生停止ボタン
        start_stop.setOnClickListener {
            startStopContentsInfo()
            Log.d("TEST", "back")

        }

    }


    private fun getContentsInfo() {
        cursor = contentResolver.query(
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
            Log.d("ANDROID", "URI : " + imageUri.toString())
            imageView.setImageURI(imageUri)
        }

    }


    //進む
    private fun moveContentsInfo() {
        if (cursor!!.moveToNext() == false) {
            cursor!!.moveToFirst()

        }
        val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val id = cursor.getLong(fieldIndex)
        val imageUri =
            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

        imageView.setImageURI(imageUri)
        Log.d("ANDROID", "URI : " + imageUri.toString())
    }


    //戻る
    private fun backContentsInfo() {
        // 画像の情報を取得する
            if (cursor!!.moveToPrevious() == false) {
                cursor!!.moveToFirst()

            }
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            imageView.setImageURI(imageUri)
            Log.d("ANDROID", "URI : " + imageUri.toString())
        }

    private fun startStopContentsInfo(){

        mTimer = Timer()
        mTimer!!.schedule(object : TimerTask() {
            override fun run() {
                mTimerSec += 0.1
                mHandler.post {

                    //ボタンを押せないようにする
                    move.isClickable = false
                    back.isClickable = false

                    //ボタンの表示「停止」
                    start_stop.text="停止"

                    if (cursor!!.moveToNext() == false) {
                        cursor!!.moveToFirst()

                    }
                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    Log.d("ANDROID", "URI : " + imageUri.toString())
                    imageView.setImageURI(imageUri)


                    //再生中にボタンが押されたらスライドショーを停止
                    start_stop.setOnClickListener{

                        mTimer!!.cancel()
                        mTimerSec = 0.0
                        //ボタンの表示「停止」
                        start_stop.text="再生"

                        move.isClickable = true
                        back.isClickable = true

                        start_stop.setOnClickListener{
                            startStopContentsInfo()
                        }

                    }
                }
            }
        }, 100, 2000)

        }

    }


