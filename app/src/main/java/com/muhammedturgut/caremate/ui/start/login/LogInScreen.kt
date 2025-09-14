package com.muhammedturgut.caremate.ui.start.login

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.ui.start.login.LoginUiState.Success
import com.muhammedturgut.caremate.ui.theme.PoppinBold
import com.muhammedturgut.caremate.ui.theme.PoppinMedium
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold



@Composable
fun LogInScreen( maxWidth: Dp,
                 maxHeight: Dp,
                 isTablet:Boolean,
                 navControllerAppHost: NavController,
                 logInViewModel: LogInViewModel = hiltViewModel()
      ){

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFCFCFC))){

        val context = LocalContext.current
        val activity = context as? Activity

        var textEmail by remember { mutableStateOf("") }
        var textPassword by remember { mutableStateOf("")}
        val contentMaxWidth = if (maxWidth < 600.dp) maxWidth else 600.dp

        val uiState by logInViewModel.uiState.collectAsState()
        LaunchedEffect(uiState) {
            when(uiState){
                is Success -> navControllerAppHost.navigate("NavBarHostScreen"){
                    popUpTo(navControllerAppHost.graph.id){
                        inclusive = true
                    }
                }
                is LoginUiState.Error ->  Toast.makeText(context,"Hatalı işlem", Toast.LENGTH_SHORT).show()

                else -> Unit
            }
        }

        BackHandler {

            activity?.finish()

        }



        ConstraintLayout(modifier = Modifier.fillMaxSize()){

            val (logo, title1,title2,textFiled1,textFiled2,btn,title4) = createRefs()

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(232.dp,90.dp)
                    .constrainAs(logo) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 90.dp)
                    })

            Text(text = "Welcome Back",
                fontSize = 24.sp,
                fontFamily = PoppinMedium,
                color = Color(0xFF70A056),
                modifier = Modifier.constrainAs(title1) {

                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(logo.bottom, margin = 16.dp)

                }
            )

            Text(text = "Log in ",
                fontSize = 24.sp,
                fontFamily = PoppinSemiBold,
                color = Color(0xFF4BA9E4),
                modifier = Modifier.constrainAs(title2) {

                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(title1.bottom)

                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(textFiled1) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(
                            title2.top,
                            margin = if (isTablet) 72.dp else 82.dp
                        )
                    }
                    .padding(horizontal = min(maxWidth * 0.1f, 24.dp))
            ) {
                // Üst etiket
                Text(
                    text = "Email addresi",
                    fontSize = 14.sp,
                    color = Color(0xFF6B6B6B),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp, start = 12.dp)
                )

                // TextField
                OutlinedTextField(
                    value = textEmail,
                    onValueChange = { textEmail = it },
                    placeholder = { Text("username@gmail.com", color = Color.Gray) },
                    singleLine = true,
                    shape = RoundedCornerShape(min(maxWidth * 0.03f, 12.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color(0xFFFFFFFF), // açık gri arka plan
                        unfocusedContainerColor = Color(0xFFFFFFFF),
                        focusedBorderColor = Color(0xFFFFFFFF), // çok açık kenarlık
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        cursorColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp, max = 84.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(textFiled2) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(
                            textFiled1.bottom,
                            margin = min(maxHeight * 0.15f, 18.dp)
                        )
                    }
                    .padding(horizontal = min(maxWidth * 0.1f, 24.dp))
            ) {
                // Üst etiket
                Text(
                    text = "password",
                    fontSize = 14.sp,
                    color = Color(0xFF6B6B6B),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp, start = 12.dp)
                )

                // TextField
                OutlinedTextField(
                    value = textPassword,
                    onValueChange = { textPassword = it },
                    placeholder = { Text("********", color = Color.Gray) },
                    singleLine = true,
                    shape = RoundedCornerShape(min(maxWidth * 0.03f, 12.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = Color(0xFFFFFFFF), // açık gri arka plan
                        unfocusedContainerColor = Color(0xFFFFFFFF),
                        focusedBorderColor = Color(0xFFFFFFFF), // çok açık kenarlık
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        cursorColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp, max = 84.dp)
                )

                Text(
                    text = "forgot  password",
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline,
                    fontFamily = PoppinMedium,
                    modifier = Modifier
                        .clickable{
                            //Buraya şifre yenileme ilgili metodlar eklenecek.
                        }
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                )
            }

            Button(
                onClick = {
                    logInViewModel.login(email = textEmail, password = textPassword)
                },
                modifier = Modifier
                    .constrainAs(btn) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(textFiled2.bottom, margin = 32.dp)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = min(maxWidth * 0.1f, 40.dp)) // responsive yatay padding
                    .heightIn(min = max(maxHeight * 0.06f, 48.dp), max = max(maxHeight * 0.08f, 64.dp)) // responsive yükseklik
                    .widthIn(max = contentMaxWidth)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FA5E3))
            ) {
                Text(
                    text = "Log in",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = PoppinBold
                )
            }



            Row(modifier = Modifier.constrainAs(title4) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 24.dp)
            }) {

                Text(text = "Don't have an account? ",
                    fontSize = 12.sp,
                    color = Color(0xFF6B6B6B),
                    fontFamily = PoppinBold,
                )
                Text(text = " Sign Up",
                    fontSize = 12.sp,
                    modifier = Modifier.clickable(onClick = {
                        navControllerAppHost.navigate("SignUpScreen"){
                            popUpTo(navControllerAppHost.graph.id){
                                inclusive = true
                            }
                        }
                    }),
                    color = Color(0xFF4BA9E6),
                    fontFamily = PoppinBold,
                )

            }


        }


    }
}
