package cmsc436.mobilesurvey.utils

import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import cmsc436.mobilesurvey.R
import cmsc436.mobilesurvey.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

import java.io.ByteArrayOutputStream
import java.io.IOException

class QRCodeUtils {

    var white = -0x1
    var black = -0x1000000


    fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        var baos: ByteArrayOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            return baos!!.toByteArray()
        } finally {
            if (baos != null) {
                try {
                    baos!!.close()
                } catch (e: IOException) {
                    Log.e(
                        QRCodeUtils::class.java!!.getSimpleName(),
                        "ByteArrayOutputStream was not closed"
                    )
                }

            }
        }
    }

    /**
     * Converts compressed byte array to bitmap
     * @param src source array
     * @return result bitmap
     */
    fun convertCompressedByteArrayToBitmap(src: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(src, 0, src.size)
    }

    fun getUser(): String{
        return  FirebaseAuth.getInstance().currentUser!!.email!!.split("@")[0]
    }

//    fun getType(): String {
//
//       fun testtest(): String {
//           lateinit var user: User;
//           Database.db.collection("users").whereEqualTo("name", getUser())
//               .get()
//               .addOnSuccessListener { documents ->
//                   for (doc in documents) {
//                       user = User(doc)
//                       Log.i("TAG", "DEBUG: Current user = " + user.name)
//                       Log.i("TAG", "DEBUG: User type = " + user.type)
//                       Log.i("TAG", "DEBUG: Doc = " + doc)
//                       break
//                   }
//                   user.type.toString()
//                   val bitmap = TextToImageEncode(getUser() + "&&&" + user.type.toString())
//
//               }
//       }
//
//
//        return testtest()
//    }

    fun doAll(){
        Database.db.collection("users").whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
            .get()
            .addOnSuccessListener { documents ->
                lateinit var user: User
                for (doc in documents) {
                    user = User(doc)
                    Log.i("TAG", "DEBUG: Current user = " + user.name)
                    Log.i("TAG", "DEBUG: User type = " + user.type)
                    Log.i("TAG", "DEBUG: Doc = " + doc)
                    break
                }
                user.type.toString()
                val bitmap = TextToImageEncode(getUser() + "&&&" + user.type.toString())
                val bytes = convertBitmapToByteArray(bitmap!!)
                val code = convertCompressedByteArrayToBitmap(bytes)
            }
        return doAll()
    }

    fun TextToImageEncode(Value: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(
                Value,
                BarcodeFormat.QR_CODE,
                500, 500, null
            )
        } catch (Illegalargumentexception: IllegalArgumentException) {
            return null
        }

        val bitMatrixWidth = bitMatrix.getWidth()
        val bitMatrixHeight = bitMatrix.getHeight()
        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) black else white
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

    companion object {

    }
}
