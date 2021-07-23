package com.abhi.bingo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var count = 0
    var user = "null"
    var roomcode = "null"
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    private lateinit var bingoimg: ImageView
    private lateinit var button00: Button
    private lateinit var button01: Button
    private lateinit var button02: Button
    private lateinit var button03: Button
    private lateinit var button04: Button
    private lateinit var button10: Button
    private lateinit var button11: Button
    private lateinit var button12: Button
    private lateinit var button13: Button
    private lateinit var button14: Button
    private lateinit var button20: Button
    private lateinit var button21: Button
    private lateinit var button22: Button
    private lateinit var button23: Button
    private lateinit var button24: Button
    private lateinit var button30: Button
    private lateinit var button31: Button
    private lateinit var button32: Button
    private lateinit var button33: Button
    private lateinit var button34: Button
    private lateinit var button40: Button
    private lateinit var button41: Button
    private lateinit var button42: Button
    private lateinit var button43: Button
    private lateinit var button44: Button
    private lateinit var buttonStart: Button
    private lateinit var helpertext: TextView
    private lateinit var lllogin: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button00 = findViewById(R.id.btn00)
        button01 = findViewById(R.id.btn01)
        button02 = findViewById(R.id.btn02)
        button03 = findViewById(R.id.btn03)
        button04 = findViewById(R.id.btn04)
        button10 = findViewById(R.id.btn10)
        button11 = findViewById(R.id.btn11)
        button12 = findViewById(R.id.btn12)
        button13 = findViewById(R.id.btn13)
        button14 = findViewById(R.id.btn14)
        button20 = findViewById(R.id.btn20)
        button21 = findViewById(R.id.btn21)
        button22 = findViewById(R.id.btn22)
        button23 = findViewById(R.id.btn23)
        button24 = findViewById(R.id.btn24)
        button30 = findViewById(R.id.btn30)
        button31 = findViewById(R.id.btn31)
        button32 = findViewById(R.id.btn32)
        button33 = findViewById(R.id.btn33)
        button34 = findViewById(R.id.btn34)
        button40 = findViewById(R.id.btn40)
        button41 = findViewById(R.id.btn41)
        button42 = findViewById(R.id.btn42)
        button43 = findViewById(R.id.btn43)
        button44 = findViewById(R.id.btn44)
        helpertext = findViewById(R.id.tvHelper)
        val buttonHost = findViewById<Button>(R.id.btn_host)
        buttonStart = findViewById(R.id.start)
        val buttonJoin = findViewById<Button>(R.id.btn_join)
        val etCode = findViewById<EditText>(R.id.editTextNumber)
        lllogin = findViewById(R.id.llinvitation)
        bingoimg = findViewById(R.id.imgBingo)
        progressBar = findViewById(R.id.progressBar)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = VISIBLE
        progressBar.visibility = VISIBLE

        gameSetup()

        buttonStart.setOnClickListener {
            if (count > 25) {
                Log.d("Abhi", "Value is this : user is $user ready")
                dbWrite("${roomcode}/Stats/playerstat/$user", "Ready")
                dbReadOnce("${roomcode}/Stats/playerstat/Active")
                if (user == "host")
                    dbRead("${roomcode}/Stats/playerstat/join")
                else
                    dbRead("${roomcode}/Stats/playerstat/host")
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Fill the squares",
                    Toast.LENGTH_SHORT
                )                           //Toast
                    .show()
            }
        }

        buttonHost.setOnClickListener {


            if (etCode.text.isNotEmpty()) {
                progressLayout.visibility = VISIBLE
                progressBar.visibility = VISIBLE

                if (buttonHost.text == "Cancel") {
                    etCode.text.clear()
                    buttonHost.text = "Host"
                    buttonJoin.visibility = VISIBLE
                    etCode.isFocusableInTouchMode = true
                    etCode.isEnabled = true
                    val database = Firebase.database
                    val myRef = database.getReference(roomcode)
                    myRef.removeValue()
                    progressLayout.visibility = GONE
                    progressBar.visibility = GONE

                } else {
                    user = "host"
                    roomcode = etCode.text.toString()
                    val database = Firebase.database
                    val myRef = database.getReference("$roomcode/Room")
                    myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val value = dataSnapshot.getValue<String>()
                            if (value == null) {
                                dbWrite("$roomcode/Room", "waiting")
                                dbRead("${roomcode}/Room")
                                helpertext.text = "Waiting for player to join"
                                buttonJoin.visibility = GONE
                                buttonHost.text = "Cancel"
                                etCode.isFocusable = false
                                etCode.isEnabled = false
                                Toast.makeText(
                                    this@MainActivity,
                                    "Room Hosted",
                                    Toast.LENGTH_SHORT
                                )                           //Toast
                                    .show()
                                progressLayout.visibility = GONE
                                progressBar.visibility = GONE
                            } else {
                                etCode.text.clear()
                                Toast.makeText(
                                    this@MainActivity,
                                    "Room already exist",
                                    Toast.LENGTH_SHORT
                                )                           //Toast
                                    .show()
                                progressLayout.visibility = GONE
                                progressBar.visibility = GONE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            progressLayout.visibility = GONE
                            progressBar.visibility = GONE
                            Log.w("Abhi", "Failed to read value.", error.toException())
                        }
                    })
                }
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Enter a Valid Room code",
                    Toast.LENGTH_SHORT
                )                           //Toast
                    .show()
            }
        }

        buttonJoin.setOnClickListener {
            if (etCode.text.isNotEmpty()) {
                progressLayout.visibility = VISIBLE
                progressBar.visibility = VISIBLE
                user = "join"
                roomcode = etCode.text.toString()
                etCode.isFocusable = false
                etCode.isEnabled = false
                val database = Firebase.database
                val myRef = database.getReference("$roomcode/Room")
                myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        progressLayout.visibility = GONE
                        progressBar.visibility = GONE
                        val value = dataSnapshot.getValue<String>()
                        if (value == null) {
                            etCode.isFocusableInTouchMode = true
                            etCode.isEnabled = true
                            Toast.makeText(
                                this@MainActivity,
                                "Room Does not Exist",
                                Toast.LENGTH_SHORT
                            )                           //Toast
                                .show()
                        } else {
                            etCode.isFocusableInTouchMode = true
                            etCode.isEnabled = true
                            etCode.text.clear()
                            dbRead("${roomcode}/Room")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        etCode.isFocusableInTouchMode = true
                        etCode.isEnabled = true
                        Log.w("Abhi", "Failed to read value.", error.toException())
                    }
                })
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Enter a Valid Room code",
                    Toast.LENGTH_SHORT
                )                           //Toast
                    .show()
            }
        }
    }

    private fun gameSetup() {

        count = 0
        user = "null"
        if (roomcode != "nulll") {
            val database = Firebase.database
            val myRef = database.getReference(roomcode)
            myRef.removeValue()
        }
        roomcode = "null"
        progressLayout.visibility = GONE
        progressBar.visibility = GONE
    }


    private fun dbWrite(path: String, msg: String) {
        val database = Firebase.database
        val myRef = database.getReference(path)
        if (msg === "X") {
            val myRef2 = database.getReference("$roomcode/Stats/playerstat/Active")
            myRef2.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val value = dataSnapshot.getValue<String>()
                    Log.d("Abhi", "Value is this : $value")
                    if (value == user) {
                        myRef.setValue(msg)
                        if (user == "host")
                            myRef2.setValue("join")
                        else
                            myRef2.setValue("host")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                    Log.w("Abhi", "Failed to read value.", error.toException())

                }
            })
        } else
            myRef.setValue(msg)
    }

    private fun dbReadOnce(path: String) {
        val database = Firebase.database
        val myRef = database.getReference(path)
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val value = dataSnapshot.getValue<String>()
                Log.d("Abhi", "Value is this : $value")
                if (value.toString() != "join" && value.toString() != "host") {
                    helpertext.setText("Waiting for opponent...")

                    myRef.setValue(user)
                    for (x in 1 until 26) {
                        dbWrite("$roomcode/Match/$x", "Active")
                    }

                }
                buttonStart.visibility = GONE
            }

            override fun onCancelled(error: DatabaseError) {

                Log.w("Abhi", "Failed to read value.", error.toException())

            }
        })
    }

    private fun dbRead(path: String) {
        val database = Firebase.database
        val myRef = database.getReference(path)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue<String>()
                val key = dataSnapshot.key.toString()
                if (value.toString() == "waiting" && user == "join") {
                    myRef.setValue("Joined")
                } else
                    if (value.toString() == "Joined") {
                        buttonStart.visibility = VISIBLE
                        lllogin.visibility = GONE
                        helpertext.setText("Tap to fill the numbers")
                        Toast.makeText(
                            this@MainActivity,
                            "A Player Joined",
                            Toast.LENGTH_SHORT
                        )                           //Toast
                            .show()
                        myRef.setValue("Full")
                        startthegame()
                    } else {

                        if (value.toString() == "Ready") {
                            dbWrite("${roomcode}/Stats/playerstat/Bingo", "null")
                            bingoStart()
                        } else if (value.toString() == "Full" && user == "join") {
                            Toast.makeText(
                                this@MainActivity,
                                " Sorry Match in Progress",
                                Toast.LENGTH_SHORT
                            )                           //Toast
                                .show()
                        } else if (key == "Active") {
                            if (value == user) {
                                Toast.makeText(
                                    this@MainActivity,
                                    " Your Turn",
                                    Toast.LENGTH_SHORT
                                )                           //Toast
                                    .show()
                                helpertext.setText("Your turn")
                            } else
                                helpertext.setText("Opponents turn")

                        } else if (key == "Bingo" && value != "null" && user != "null") {
                            if (value == user) {
                                val builder = AlertDialog.Builder(this@MainActivity)
                                //set title for alert dialog
                                builder.setTitle(R.string.dialogTitle)
                                //set message for alert dialog
                                builder.setMessage(R.string.dialogMessage)
                                builder.setIcon(R.drawable.ic_baseline_money_24)
                                //performing positive action
                                builder.setPositiveButton("Play Again") { dialogInterface, which ->
                                    val database = Firebase.database
                                    val myRef = database.getReference(roomcode)
                                    myRef.removeValue()
                                    val context = this@MainActivity
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                    if (context is Activity) {
                                        (context as Activity).finish()
                                    }
                                    Runtime.getRuntime().exit(0)
                                }
                                //performing negative action
                                builder.setNegativeButton("Exit") { dialogInterface, which ->
                                    finish()
                                }
                                // Create the AlertDialog
                                val alertDialog: AlertDialog = builder.create()
                                // Set other dialog properties
                                alertDialog.setCancelable(false)
                                alertDialog.show()

                            } else {
                                val builder = AlertDialog.Builder(this@MainActivity)
                                //set title for alert dialog
                                builder.setTitle(R.string.dialogTitleF)
                                //set message for alert dialog
                                builder.setMessage(R.string.dialogMessageF)
                                builder.setIcon(R.drawable.ic_baseline_money_24)

                                //performing positive action
                                builder.setPositiveButton("Play Again") { dialogInterface, which ->
                                    val database = Firebase.database
                                    val myRef = database.getReference(roomcode)
                                    myRef.removeValue()
                                    val context = this@MainActivity
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                    if (context is Activity) {
                                        (context as Activity).finish()
                                    }
                                    Runtime.getRuntime().exit(0)
                                }
                                //performing cancel action

                                //performing negative action
                                builder.setNegativeButton("Exit") { dialogInterface, which ->
                                    finish()
                                }
                                // Create the AlertDialog
                                val alertDialog: AlertDialog = builder.create()
                                // Set other dialog properties
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                            }

                        }
                    }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Abhi", "Failed to read value.", error.toException())
            }
        })
    }

    private fun bingoStart() {
        dbRead("${roomcode}/Stats/playerstat/Active")
        dbRead("${roomcode}/Stats/playerstat/Bingo")
        Log.w("Abhi", "Stared bingo start")
        val database = Firebase.database
        val myRef = database.getReference("$roomcode/Match")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (state in dataSnapshot.children) {
                    Log.w("Abhifor", "Foorloop ${state.value} ${state.key}")
                    if (state.value == "X")
                        disablbutton(state.key.toString())
                }
                winchecker()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Abhi", "Failed to read value.", error.toException())
            }
        })
        findthenumber()
    }

    private fun findthenumber() {
        button00.setOnClickListener {
            dbWrite("$roomcode/Match/${button00.text}", "X")
        }
        button01.setOnClickListener {
            dbWrite("$roomcode/Match/${button01.text}", "X")
        }
        button02.setOnClickListener {
            dbWrite("$roomcode/Match/${button02.text}", "X")
        }
        button03.setOnClickListener {
            dbWrite("$roomcode/Match/${button03.text}", "X")
        }
        button04.setOnClickListener {
            dbWrite("$roomcode/Match/${button04.text}", "X")
        }
        button10.setOnClickListener {
            dbWrite("$roomcode/Match/${button10.text}", "X")
        }
        button11.setOnClickListener {
            dbWrite("$roomcode/Match/${button11.text}", "X")
        }
        button12.setOnClickListener {
            dbWrite("$roomcode/Match/${button12.text}", "X")
        }
        button13.setOnClickListener {
            dbWrite("$roomcode/Match/${button13.text}", "X")
        }
        button14.setOnClickListener {
            dbWrite("$roomcode/Match/${button14.text}", "X")
        }
        button20.setOnClickListener {
            dbWrite("$roomcode/Match/${button20.text}", "X")
        }
        button21.setOnClickListener {
            dbWrite("$roomcode/Match/${button21.text}", "X")
        }
        button22.setOnClickListener {
            dbWrite("$roomcode/Match/${button22.text}", "X")
        }
        button23.setOnClickListener {
            dbWrite("$roomcode/Match/${button23.text}", "X")
        }
        button24.setOnClickListener {
            dbWrite("$roomcode/Match/${button24.text}", "X")
        }
        button30.setOnClickListener {
            dbWrite("$roomcode/Match/${button30.text}", "X")
        }
        button31.setOnClickListener {
            dbWrite("$roomcode/Match/${button31.text}", "X")
        }
        button32.setOnClickListener {
            dbWrite("$roomcode/Match/${button32.text}", "X")
        }
        button33.setOnClickListener {
            dbWrite("$roomcode/Match/${button33.text}", "X")
        }
        button34.setOnClickListener {
            dbWrite("$roomcode/Match/${button34.text}", "X")
        }
        button40.setOnClickListener {
            dbWrite("$roomcode/Match/${button40.text}", "X")
        }
        button41.setOnClickListener {
            dbWrite("$roomcode/Match/${button41.text}", "X")
        }
        button42.setOnClickListener {
            dbWrite("$roomcode/Match/${button42.text}", "X")
        }
        button43.setOnClickListener {
            dbWrite("$roomcode/Match/${button43.text}", "X")
        }
        button44.setOnClickListener {
            dbWrite("$roomcode/Match/${button44.text}", "X")
        }

    }

    private fun pointcal(): Int {
        var points = 0
        if (!button00.isEnabled && !button01.isEnabled && !button02.isEnabled && !button03.isEnabled && !button04.isEnabled) ///rows
        {
            button00.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button01.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button02.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button03.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button04.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }
        if (!button10.isEnabled && !button11.isEnabled && !button12.isEnabled && !button13.isEnabled && !button14.isEnabled) {
            button10.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button11.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button12.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button13.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button14.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }
        if (!button20.isEnabled && !button21.isEnabled && !button22.isEnabled && !button23.isEnabled && !button24.isEnabled) {
            button20.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button21.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button22.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button23.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button24.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }
        if (!button30.isEnabled && !button31.isEnabled && !button32.isEnabled && !button33.isEnabled && !button34.isEnabled) {
            button30.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button31.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button32.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button33.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button34.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }
        if (!button40.isEnabled && !button41.isEnabled && !button42.isEnabled && !button43.isEnabled && !button44.isEnabled) {
            button40.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button41.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button42.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button43.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button44.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }

        if (!button00.isEnabled && !button10.isEnabled && !button20.isEnabled && !button30.isEnabled && !button40.isEnabled)///column
        {
            button00.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button10.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button20.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button30.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button40.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }
        if (!button01.isEnabled && !button11.isEnabled && !button21.isEnabled && !button31.isEnabled && !button41.isEnabled) {
            button01.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button11.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button21.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button31.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button41.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }
        if (!button02.isEnabled && !button12.isEnabled && !button22.isEnabled && !button32.isEnabled && !button42.isEnabled) {
            button02.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button12.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button22.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button32.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button42.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }
        if (!button03.isEnabled && !button13.isEnabled && !button23.isEnabled && !button33.isEnabled && !button43.isEnabled) {
            button03.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button13.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button23.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button33.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button43.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }
        if (!button04.isEnabled && !button14.isEnabled && !button24.isEnabled && !button34.isEnabled && !button44.isEnabled) {
            button04.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button14.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button24.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button34.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button44.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }

        if (!button00.isEnabled && !button11.isEnabled && !button22.isEnabled && !button33.isEnabled && !button44.isEnabled)///diagonal
        {
            button00.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button11.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button22.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button33.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button44.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }
        if (!button04.isEnabled && !button13.isEnabled && !button22.isEnabled && !button31.isEnabled && !button40.isEnabled) {
            button04.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button13.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button22.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button31.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            button40.setBackgroundResource(R.drawable.ic_baseline_stars_24);
            points += 1
        }
        return points
    }

    private fun winchecker() {
        val score = pointcal()
        Log.w("Abhi2", "Won be points $score")
        if (score == 1) {
            bingoimg.visibility = VISIBLE
            bingoimg.setImageResource(R.drawable.b)
        } else if (score == 2) {
            bingoimg.visibility = VISIBLE
            bingoimg.setImageResource(R.drawable.bi)
        } else if (score == 3) {
            bingoimg.visibility = VISIBLE
            bingoimg.setImageResource(R.drawable.bin)
        } else if (score == 4) {
            bingoimg.visibility = VISIBLE
            bingoimg.setImageResource(R.drawable.bing)
        } else if (score >= 5) {
            bingoimg.visibility = VISIBLE
            bingoimg.setImageResource(R.drawable.bingo)
            dbWrite("${roomcode}/Stats/playerstat/Bingo", user)
            Log.w("Abhi2", "Won be points $score")
        }
    }

    private fun disablbutton(btnnumber: String) {
        Log.w("Abhi2", "Disabling $btnnumber")
        if (button00.text.toString() == btnnumber) {
            button00.isEnabled = false
            button00.setTextColor(0xC0C0C0)
        }
        if (button01.text.toString() == btnnumber) {
            button01.isEnabled = false
            button01.setTextColor(0xC0C0C0)
        }
        if (button02.text.toString() == btnnumber) {
            button02.isEnabled = false
            button02.setTextColor(0xC0C0C0)
        }
        if (button03.text.toString() == btnnumber) {
            button03.isEnabled = false
            button03.setTextColor(0xC0C0C0)
        }
        if (button04.text.toString() == btnnumber) {
            button04.isEnabled = false
            button04.setTextColor(0xC0C0C0)
        }
        if (button10.text.toString() == btnnumber) {
            button10.isEnabled = false
            button10.setTextColor(0xC0C0C0)
        }
        if (button11.text.toString() == btnnumber) {
            button11.isEnabled = false
            button11.setTextColor(0xC0C0C0)
        }
        if (button12.text.toString() == btnnumber) {
            button12.isEnabled = false
            button12.setTextColor(0xC0C0C0)
        }
        if (button13.text.toString() == btnnumber) {
            button13.isEnabled = false
            button13.setTextColor(0xC0C0C0)
        }
        if (button14.text.toString() == btnnumber) {
            button14.isEnabled = false
            button14.setTextColor(0xC0C0C0)
        }
        if (button20.text.toString() == btnnumber) {
            button20.isEnabled = false
            button20.setTextColor(0xC0C0C0)
        }
        if (button21.text.toString() == btnnumber) {
            button21.isEnabled = false
            button21.setTextColor(0xC0C0C0)
        }
        if (button22.text.toString() == btnnumber) {
            button22.isEnabled = false
            button22.setTextColor(0xC0C0C0)
        }
        if (button23.text.toString() == btnnumber) {
            button23.isEnabled = false
            button23.setTextColor(0xC0C0C0)
        }
        if (button24.text.toString() == btnnumber) {
            button24.isEnabled = false
            button24.setTextColor(0xC0C0C0)
        }
        if (button30.text.toString() == btnnumber) {
            button30.isEnabled = false
            button30.setTextColor(0xC0C0C0)

        }
        if (button31.text.toString() == btnnumber) {
            button31.isEnabled = false
            button31.setTextColor(0xC0C0C0)
        }
        if (button32.text.toString() == btnnumber) {
            button32.isEnabled = false
            button32.setTextColor(0xC0C0C0)
        }
        if (button33.text.toString() == btnnumber) {
            button33.isEnabled = false
            button33.setTextColor(0xC0C0C0)
        }
        if (button34.text.toString() == btnnumber) {
            button34.isEnabled = false
            button34.setTextColor(0xC0C0C0)
        }
        if (button40.text.toString() == btnnumber) {
            button40.isEnabled = false
            button40.setTextColor(0xC0C0C0)
        }
        if (button41.text.toString() == btnnumber) {
            button41.isEnabled = false
            button41.setTextColor(0xC0C0C0)
        }
        if (button42.text.toString() == btnnumber) {
            button42.isEnabled = false
            button42.setTextColor(0xC0C0C0)
        }
        if (button43.text.toString() == btnnumber) {
            button43.isEnabled = false
            button43.setTextColor(0xC0C0C0)
        }
        if (button44.text.toString() == btnnumber) {
            button44.isEnabled = false
            button44.setTextColor(0xC0C0C0)
        }
    }

    private fun startthegame() {
        Log.w("Abhi", "Stared the count")
        count = 1
        button00.setOnClickListener {
            button00.text = count.toString()
            ++count
            button00.setOnClickListener(null)
        }
        button01.setOnClickListener {
            button01.text = count.toString()
            ++count
            button01.setOnClickListener(null)
        }
        button02.setOnClickListener {
            button02.text = count.toString()
            ++count
            button02.setOnClickListener(null)
        }
        button03.setOnClickListener {
            button03.text = count.toString()
            ++count
            button03.setOnClickListener(null)
        }
        button04.setOnClickListener {
            button04.text = count.toString()
            ++count
            button04.setOnClickListener(null)
        }
        button10.setOnClickListener {
            button10.text = count.toString()
            ++count
            button10.setOnClickListener(null)
        }
        button11.setOnClickListener {
            button11.text = count.toString()
            ++count
            button11.setOnClickListener(null)
        }
        button12.setOnClickListener {
            button12.text = count.toString()
            ++count
            button12.setOnClickListener(null)
        }
        button13.setOnClickListener {
            button13.text = count.toString()
            ++count
            button13.setOnClickListener(null)
        }
        button14.setOnClickListener {
            button14.text = count.toString()
            ++count
            button14.setOnClickListener(null)
        }
        button20.setOnClickListener {
            button20.text = count.toString()
            ++count
            button20.setOnClickListener(null)
        }
        button21.setOnClickListener {
            button21.text = count.toString()
            ++count
            button21.setOnClickListener(null)
        }
        button22.setOnClickListener {
            button22.text = count.toString()
            ++count
            button22.setOnClickListener(null)
        }
        button23.setOnClickListener {
            button23.text = count.toString()
            ++count
            button23.setOnClickListener(null)
        }
        button24.setOnClickListener {
            button24.text = count.toString()
            ++count
            button24.setOnClickListener(null)
        }
        button30.setOnClickListener {
            button30.text = count.toString()
            ++count
            button30.setOnClickListener(null)
        }
        button31.setOnClickListener {
            button31.text = count.toString()
            ++count
            button31.setOnClickListener(null)
        }
        button32.setOnClickListener {
            button32.text = count.toString()
            ++count
            button32.setOnClickListener(null)
        }
        button33.setOnClickListener {
            button33.text = count.toString()
            ++count
            button33.setOnClickListener(null)
        }
        button34.setOnClickListener {
            button34.text = count.toString()
            ++count
            button34.setOnClickListener(null)
        }
        button40.setOnClickListener {
            button40.text = count.toString()
            ++count
            button40.setOnClickListener(null)
        }
        button41.setOnClickListener {
            button41.text = count.toString()
            ++count
            button41.setOnClickListener(null)
        }
        button42.setOnClickListener {
            button42.text = count.toString()
            ++count
            button42.setOnClickListener(null)
        }
        button43.setOnClickListener {
            button43.text = count.toString()
            ++count
            button43.setOnClickListener(null)
        }
        button44.setOnClickListener {
            button44.text = count.toString()
            ++count
            button44.setOnClickListener(null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val database = Firebase.database
        val myRef = database.getReference(roomcode)
        myRef.removeValue()
    }
}