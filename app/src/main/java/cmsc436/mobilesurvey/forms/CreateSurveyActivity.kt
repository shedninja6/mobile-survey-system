package cmsc436.mobilesurvey.forms

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*

import cmsc436.mobilesurvey.R
import cmsc436.mobilesurvey.utils.QRCodeUtils

class CreateSurveyActivity : AppCompatActivity() {

    enum class FormType{
        RETAIL
    }

    private var createButton: Button? = null
    private var name: EditText? = null


    internal var bitmap: Bitmap? = null
    private var QRCodeUtils: QRCodeUtils? = null

    private var db: SurveyDatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("TAG", "INFO: INSIDE ON CREATE")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_createsurvey)

        db = SurveyDatabaseHandler()
        name = findViewById(R.id.name)
        createButton = findViewById(R.id.create)
        QRCodeUtils = QRCodeUtils()

        //button will generate code and store it in the database
        createButton!!.setOnClickListener {

            val qrcode = QRCodeUtils!!.generateQRCode(name!!.text.toString())
            val bytes = QRCodeUtils!!.convertBitmapToByteArray(qrcode!!)
            db!!.addFormToDatabase(name!!.text.toString(), "", bytes)
            Toast.makeText(
                this@CreateSurveyActivity,
                "QRCode " + QRCodeUtils!!.convertCompressedByteArrayToBitmap(bytes) + " stringified",
                Toast.LENGTH_SHORT
            ).show()
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.survey_type, android.R.layout.simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter



            //TODO Actually create the surveys
            //TODO Link survey to current login
    }


    }