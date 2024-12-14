package com.example.sportclubapp
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.example.sportclubapp.ui.theme.SportClubAppTheme

// Author Tharushi Kasumini

data class Member(
    val firstName: String,
    val lastName: String,
    val school: String,
    val age: Int,
    val sport: String
)


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )

        setContentView(R.layout.main_activity)

        val loginInput = findViewById<EditText>(R.id.etLoginInput)
        val loginButton = findViewById<Button>(R.id.btnLogin)

        loginButton.setOnClickListener {
            val input = loginInput.text.toString()
            if (input.length == 12) {
                val intent = Intent(this, MemberListActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, ErrorActivity::class.java)
                startActivity(intent)
            }
        }
    }
}

class ErrorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.error_activity)

        val retryButton = findViewById<Button>(R.id.btnRetry)

        retryButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}

class MemberListActivity : AppCompatActivity() {
    private lateinit var members: List<Member>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_list)

        members = listOf(
            Member("John", "Doe", "Westfield High", 16, "Soccer"),
            Member("Jane", "Smith", "Eastside Academy", 17, "Basketball"),
            Member("Michael", "Brown", "Central High", 15, "Tennis"),
            Member("Emily", "Davis", "Northview School", 16, "Swimming")
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            members.map { "${it.firstName} ${it.lastName}" }
        )

        val listView = findViewById<ListView>(R.id.memberListView)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedMember = members[position]

            // Create the fragment
            val fragment = MemberDetailsFragment.newInstance(selectedMember)

            // Replace fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}

class MemberDetailsFragment : Fragment() {

    companion object {
        // Factory method to create a new instance of the fragment with member details
        fun newInstance(member: Member): MemberDetailsFragment {
            val fragment = MemberDetailsFragment()
            val args = Bundle().apply {
                putString("firstName", member.firstName)
                putString("lastName", member.lastName)
                putString("school", member.school)
                putInt("age", member.age)
                putString("sport", member.sport)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_member_details, container, false)

        // Retrieve arguments
        val firstName = arguments?.getString("firstName") ?: "N/A"
        val lastName = arguments?.getString("lastName") ?: "N/A"
        val school = arguments?.getString("school") ?: "N/A"
        val age = arguments?.getInt("age", 0) ?: 0
        val sport = arguments?.getString("sport") ?: "N/A"

        // Bind data to TextViews
        view.findViewById<TextView>(R.id.tvFirstName).text = "First Name: $firstName"
        view.findViewById<TextView>(R.id.tvLastName).text = "Last Name: $lastName"
        view.findViewById<TextView>(R.id.tvSchool).text = "School: $school"
        view.findViewById<TextView>(R.id.tvAge).text = "Age: $age"
        view.findViewById<TextView>(R.id.tvSport).text = "Sport: $sport"

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            // Pop the current fragment from the back stack
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }
}