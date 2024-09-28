package edu.unicauca.moneytrack.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.unicauca.moneytrack.R

@Composable
fun LoginScreen(){

    var correo by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "MoneyTrack", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Image(painter = painterResource(id = R.drawable.moneytrack), contentDescription = "Logo",
            modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Entra a tu cuenta")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = "correo", onValueChange = {
            correo = it
        }, label = {
            Text(text = "Correo Electronico")
        })

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = "contraseña", onValueChange = {
            contrasenia = it
        }, label = {
            Text(text = "Contraseña")
        }, visualTransformation = PasswordVisualTransformation())

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            Log.i("Credenciales", "correo: $correo, contraseña: $contrasenia")
        }) {
            Text(text = "Entrar")
        }
        Spacer(modifier = Modifier.height(32.dp))

        Text(text= "¿Olvidaste tu contraseña?", modifier = Modifier.clickable {

        })

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "O entra con ")
        Image(painter = painterResource(id = R.drawable.google), contentDescription = "Google",
            modifier = Modifier.size(60.dp).clickable {
            //Google clicked
            })


    }


}
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}