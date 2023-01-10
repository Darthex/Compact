package com.example.a12020319_harshul_sharma

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.math.sign

class MainActivity2 : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference
    private lateinit var pb:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //For night mode theme
        setContentView(R.layout.activity_main2)
        pb = findViewById(R.id.idPBLoading)
        pb.visibility = View.VISIBLE
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        adapter = UserAdapter(this, userList)
        userRecyclerView = findViewById(R.id.chatHome)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter


        val ename:TextView = findViewById(R.id.textView2)
        mAuth.currentUser?.uid?.let {
            mDbRef.child("user").child(it).child("name").get().addOnSuccessListener{
                ename.text = it.value.toString()
                pb.visibility = View.GONE
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }

        val drawer:DrawerLayout = findViewById(R.id.drawer_layout)
        val menu:ImageView = findViewById(R.id.menu)
        val add:FloatingActionButton = findViewById(R.id.addbutton)
        val ll:LinearLayout = findViewById(R.id.mainBody)
        val signout:ImageView = findViewById(R.id.imageView)

        menu.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        //-------------------------------------------------------------------------------
        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(mAuth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //pass
            }

        })
        //-------------------------------------------------------------------------------

        //-------------------------------------------------------------
        val status:ImageView = findViewById(R.id.online_status)
        registerForContextMenu(status)

        //-------------------------------------------------------------
        add.setOnClickListener {
            val pf: TextView = TextView(this)
            pf.text = "New Post"
            pf.setTextColor(Color.WHITE)
            pf.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

            pf.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                pf.gravity = Gravity.CENTER_HORIZONTAL
                setMargins(50, 48, 50, 0)
            }
            ll.addView(pf)
        }
        //-------------------------------------------------------------

        signout.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }


    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.status_menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val status:ImageView = findViewById(R.id.online_status)
        when(item.itemId){
            R.id.online -> status.setImageResource(R.drawable.ellipse_4)
            R.id.idle -> status.setImageResource(R.drawable.ellipse_3)
            R.id.invisible -> status.setImageResource(R.drawable.ellipse_2)
        }
        return super.onContextItemSelected(item)
    }
}