import android.util.Log
import com.tawajood.snail.utils.Constants
import com.tawajood.vet.pojo.MainResponse
import retrofit2.Response

fun <T> handleResponse(response: Response<MainResponse<T>>): MainResponse<T> {
    return if (response.isSuccessful) {
        if (response.body()!!.status) {
            MainResponse(
                true,
                response.body()!!.msg,
                response.body()!!.data
            )

        } else {
            MainResponse(
                false,
                response.body()!!.msg,
                response.body()!!.data
            )
        }
    } else {
        val message =
            when (response.code()) {
                in 400..499 -> {
                    if (PrefsHelper.getLanguage() == Constants.LANG)
                        "Bad Request"
                    else
                        "غير موجود"
                }
                in 500..599 -> {
                    if (PrefsHelper.getLanguage() == Constants.LANG)
                        "Our server is sick right now, please try again later."
                    else
                        "خطأ في مقدم الخدمة، من فضلك حاول في وقت لاحق."
                }
                else -> ""
            }
        MainResponse(
            false,
            message,
            null
        )
    }
}