package br.com.vinicius.bankapp.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.vinicius.bankapp.R
import br.com.vinicius.bankapp.data.repository.UserRepository
import br.com.vinicius.bankapp.domain.user.User
import br.com.vinicius.bankapp.internal.*
import com.google.android.material.snackbar.Snackbar

class LoginPresenter(override val view: LoginContract.View): LoginContract.Presenter {

    @Suppress("UNREACHABLE_CODE")
    override fun startLogin(username: String, password: String) {

        try {
            verifyNetwork(view.getActivity())
            val user: User =
                User(username, password)
            user.repository = UserRepository()
            view.showProgressBar(true)
            user.startLogin(object : BaseCallback<User>{
                override fun onSuccessful(value: User) {
                    view.saveUserPreferences(value)
                    view.showProgressBar(false)
                }

                override fun onUnsuccessful(error: String) {
                    view.notification(error)
                    view.showProgressBar(false)
                }

            })
        }catch(e:ValidationException){
            e.message?.let { view.notification(it) }
            view.showProgressBar(false)
        }catch(e:NotConnectionNetwork) {
            view.showSnack(CONNECTION_INTERNTET_ERROR)
        }
    }

    private fun verifyNetwork(activity:AppCompatActivity){
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if(!(networkInfo!= null && networkInfo.isConnected)) throw NotConnectionNetwork()
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun showSnack(view: View, message: String, length: Int) {
        val snackbar = Snackbar.make(view, message, length).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        snackbar.setTextColor(Color.WHITE, 18f)
        snackbar.show()
    }

    fun Snackbar.setTextColor(color: Int, number: Float): Snackbar {
        val tv = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        tv.setTextColor(color)
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tv.textSize = number
        return this
    }


}