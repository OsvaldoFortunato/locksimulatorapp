package com.example.lockappemulation


import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.lockappemulation.data.Password
import com.example.lockappemulation.data.PasswordViewModel
import com.example.lockappemulation.databinding.ActivityMainBinding
import com.example.lockappemulation.resources.States
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var statusState = 0
    lateinit var binding: ActivityMainBinding
    lateinit var passwordList: passwordList
    lateinit var lista: ArrayList<String>
    lateinit var vibrator: Vibrator

    lateinit var androidViewModel: AndroidViewModel

    lateinit var liveData: LiveData<ArrayList<String>>
    lateinit var passwordViewModel: PasswordViewModel

    var total = 0
    var totalViews = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initial()

        //
        androidViewModel.viewModelScope.launch(Dispatchers.Main) {

            val state = passwordViewModel.getCount().await().toInt()
            stateCheck(state)
        }

        liveData.observe(this, Observer {
            total = it.size
            Log.i("TOTAL_SIZE_IT", it.size.toString())
            if (total != 0) {
                totalViews.get(total - 1).setBackgroundResource(R.drawable.circle_on)

                //atraso
                androidViewModel.viewModelScope.launch(Dispatchers.Main) {
                    delay(500)
                    if (total == totalViews.size) {
                        sendingInput(it)
                    }
                }
            }
        })

    }

    private fun sendingInput(list: List<String>) {

        var aux = "".trim()
        for (value in list) {
            aux += value
        }
        val password = Password(0, aux)

        if (statusState == States.NEW_PASSWORD) {
            androidViewModel.viewModelScope.launch(Dispatchers.IO) {
                val resultTrans = passwordViewModel.insert(password).await()

                withContext(Dispatchers.Main) {
                    if (resultTrans) {
                        val snackbar = Snackbar.make(binding.root,"Palavra-Passe Salva!",
                        Snackbar
                            .LENGTH_SHORT).show()

                        resetAll()
                        delay(1000)
                        finish()
                    } else {
                        Toast.makeText(binding.root.context, "Erro!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else if (statusState > States.NEW_PASSWORD) {
            androidViewModel.viewModelScope.launch(Dispatchers.Main) {
                val resultTrans = passwordViewModel.getPassword().await()
                val firstRegister = resultTrans[0]

                if (password.password == firstRegister.password) {
                    Toast.makeText(
                            binding.root.context,
                            "Palavra-Passe Correta! ",
                            Toast.LENGTH_SHORT
                    ).show()

                    androidViewModel.viewModelScope.launch(Dispatchers.Main) {
                        delay(500)
                    }
                    var bundle = Bundle()
                    bundle.putString("nome", "<Colocar o nome Aqui!>")
                    startActivity(Intent(this@MainActivity, Welcome::class.java).putExtras(bundle))
                    resetAll()


                } else {
                    Toast.makeText(
                            binding.root.context,
                            "Palavra-Passe Errada! ",
                            Toast.LENGTH_SHORT
                    ).show()
                    resetAll()
                }
            }
        }
    }

    private fun stateCheck(state: Int) {
        when (state) {
            States.NEW_PASSWORD -> {
                binding.textStatus.visibility = View.VISIBLE
                statusState = 0
            }
            else -> {
                statusState = state
            }
        }

    }

    fun initialSystem() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun initialResource() {
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        androidViewModel = AndroidViewModel(binding.root.context.applicationContext as Application)
        passwordViewModel = PasswordViewModel(binding.root.context)
    }

    fun initialLogics() {
        lista = ArrayList()
        passwordList = passwordList()
        liveData = passwordList.geListLiveData()
        //
        totalViews.add(binding.viewEnables1)
        totalViews.add(binding.viewEnables2)
        totalViews.add(binding.viewEnables3)
        totalViews.add(binding.viewEnables4)
        totalViews.add(binding.viewEnables5)
        totalViews.add(binding.viewEnables6)
    }

    fun initial() {
        initialSystem()
        initialResource()
        initialLogics()
    }

    fun resetAll() {
        vibratePhone()
        passwordList.resetPassword()
        lista.clear()
        total = 0
        resetViews(totalViews)
    }

    fun resetViews(listView: ArrayList<View>) {
        for (view in listView) {
            view.setBackgroundResource(R.drawable.circle_off)
        }
    }

    fun vibratePhone() {
        if (vibrator.hasVibrator()) {
            val pattern = longArrayOf(0, 40)
            vibrator.vibrate(pattern, -1)
        }
    }

    fun buttonClicked(infoButton: String) {
        if (total < totalViews.size) {
            vibratePhone()
            lista.add(infoButton.toString())
            passwordList.setPasswordList(lista)
        }


    }

    fun buttonPressed(view: View) {
        when (view.id) {
            binding.btn1.id -> buttonClicked("1")
            binding.btn2.id -> buttonClicked("2")
            binding.btn3.id -> buttonClicked("3")
            binding.btn4.id -> buttonClicked("4")
            binding.btn5.id -> buttonClicked("5")
            binding.btn6.id -> buttonClicked("6")
            binding.btn7.id -> buttonClicked("7")
            binding.btn8.id -> buttonClicked("8")
            binding.btn9.id -> buttonClicked("9")
            binding.btn0.id -> buttonClicked("0")
            binding.btnDelete.id -> resetAll()
        }
    }
}
