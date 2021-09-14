package com.peerbitskuldeep.chatapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.peerbitskuldeep.chatapp.adapters.SectionPageAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var sectionPageAdapter: SectionPageAdapter
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        sectionPageAdapter = SectionPageAdapter(supportFragmentManager)
        supportActionBar!!.title = "Dash Board"
        mAuth = FirebaseAuth.getInstance()

        if (intent.extras!=null)
        {
            var name = intent.extras!!.get("name")

            Toast.makeText(
                applicationContext,
                "Logged In! $name",
                Toast.LENGTH_SHORT
            ).show()
        }

        dashBoardViewPager.adapter = sectionPageAdapter
        mainTabs.setTabTextColors(Color.WHITE, Color.GREEN)
        mainTabs.setupWithViewPager(dashBoardViewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.logout -> {
                mAuth.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}