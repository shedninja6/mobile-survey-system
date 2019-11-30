package cmsc436.mobilesurvey.forms

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cmsc436.mobilesurvey.R
import kotlinx.android.synthetic.main.activity_restaurant.*
import cmsc436.mobilesurvey.utils.*
import cmsc436.mobilesurvey.utils.Database.db
import com.google.firebase.firestore.DocumentSnapshot

class SurveyActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var surveyType: String? = null
    private var selectedPlaceId: String? = null
    private var users: ArrayList<User>? = arrayListOf()
    private var spinnerValues: ArrayList<String>? = arrayListOf()

    private var submitButton: Button? = null
    private var answerOne: String? = null // first question
    private var answerTwo: String? = null // second question
    private var answerThree: String? = null // third question
    private var answerFour: String? = null // fourth question
    private var answerFive: String? = null // fifth question
    private var answerSix: EditText? = null
    private var additionalComments: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val restaurant: String = getFirstWord(getString(R.string.restaurant))
        val retail: String = getFirstWord(getString(R.string.retail))
        val amusement: String = getFirstWord(getString(R.string.amusement))
        val type:String = intent.getStringExtra("type")

        when (type) {
            restaurant -> {
                surveyType=restaurant
                setContentView(R.layout.activity_restaurant)
            }

            retail -> {
                surveyType=retail
                setContentView(R.layout.activity_retail)
            }

            amusement -> {
                surveyType=amusement
                setContentView(R.layout.activity_amusement)
            }

            else -> {
                return
            }
        }

        // query database for place. If there are none, close activity
        db.collection("users").whereEqualTo("type", type)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(
                        applicationContext, "No restaurants to review!",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                } else {
                    for (doc in documents) {
                        var user = User(doc)
                        users!!.add(user)
                        spinnerValues!!.add(user.name as String)
                    }

                    val spinner = findViewById<Spinner>(R.id.restaurant_spinner)

                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item, spinnerValues
                    )

                    adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner!!.onItemSelectedListener = this
                    spinner!!.setSelection(0)
                    spinner!!.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext, "Error getting documents",
                    Toast.LENGTH_SHORT
                ).show()
            }

        one_group.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            var value = radio.text
            answerOne = value as String?

            Toast.makeText(
                applicationContext, "$value",
                Toast.LENGTH_SHORT
            ).show()
        }

        two_group.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            var value = radio.text
            answerTwo = value as String?

            Toast.makeText(
                applicationContext, "$value",
                Toast.LENGTH_SHORT
            ).show()
        }

        three_group.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            var value = radio.text
            answerThree = value as String?

            Toast.makeText(
                applicationContext, "$value",
                Toast.LENGTH_SHORT
            ).show()
        }

        four_group.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            var value = radio.text
            answerFour= value as String?

            Toast.makeText(
                applicationContext, "$value",
                Toast.LENGTH_SHORT
            ).show()
        }

        five_group.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            var value = radio.text
            answerFive = value as String?

            Toast.makeText(
                applicationContext, "$answerFive",
                Toast.LENGTH_SHORT
            ).show()
        }

        answerSix = findViewById(R.id.six_text)
        additionalComments = findViewById(R.id.seven_comments)

        submitButton = findViewById(R.id.submit)

        submitButton!!.setOnClickListener {
            submitData()
        }

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // parent.getItemAtPosition(pos) to get value
        var value = parent.getItemAtPosition(pos)
        selectedPlaceId = value.toString()

        Toast.makeText(applicationContext, "$value", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    private fun submitData() {
        if (answerOne == null ||
            answerTwo == null ||
            answerThree == null ||
            answerFour == null ||
            answerFive == null) {

            Toast.makeText(
                this, "Please fill out all questions",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val noRecComment: String = answerSix!!.text.toString()
        val addComment: String = additionalComments!!.text.toString()

        surveyType?.let { Database.submitForm(applicationContext, it, HashMap()) }

        // Update one field, creating the document if it does not already exist.
//        val data = hashMapOf("capital" to true)
//
//        db.collection("cities").document("BJ")
//            .set(data, SetOptions.merge())
    }
}