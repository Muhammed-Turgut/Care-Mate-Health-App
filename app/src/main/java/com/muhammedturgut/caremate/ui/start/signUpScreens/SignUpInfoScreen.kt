package com.muhammedturgut.caremate.ui.start.signUpScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.ui.theme.PoppinBold
import com.muhammedturgut.caremate.ui.theme.PoppinMedium
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold


@Composable
fun SignUpScreenInfo(maxWidth: Dp,
                     maxHeight: Dp,
                     isTablet: Boolean,
                     navControllerAppHost: NavController){

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF7F7F7))){

        var textJop by remember { mutableStateOf("") }
        var textDateOfBirth by remember { mutableStateOf("")}

        var selectedGender by remember { mutableStateOf(false) } //false durum erkek olacak


        val contentMaxWidth = if (maxWidth < 600.dp) maxWidth else 600.dp

        ConstraintLayout(modifier = Modifier.fillMaxSize()){

            val (logo, title1,textField,textFiled1,radio,btn,title2,image1,title4,backBtn) = createRefs()

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

            Text(text = "Welcome",
                fontSize = 24.sp,
                fontFamily = PoppinMedium,
                color = Color(0xFF70A056),
                modifier = Modifier.constrainAs(title1) {

                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(logo.bottom, margin = 16.dp)

                }
            )

            Text(text = "Sign up",
                fontSize = 24.sp,
                color = Color(0xFF4BA9E4),
                fontFamily = PoppinSemiBold,
                modifier = Modifier.constrainAs(title2) {

                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(title1.bottom)

                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(textField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(
                            title1.top,
                            margin = if (isTablet) 72.dp else 82.dp
                        )
                    }
                    .padding(horizontal = min(maxWidth * 0.1f, 24.dp))
            ) {
                // Üst etiket
                Text(
                    text = "job",
                    fontSize = 12.sp,
                    color = Color.Black,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp, start = 12.dp)
                )

                // TextField
                OutlinedTextField(
                    value = textJop,
                    onValueChange = { textJop = it },
                    placeholder = { Text("Mühendis, Doktor, Öğrenci vb.", color = Color.Gray) },
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
                    .constrainAs(textFiled1) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(textField.bottom, margin = min(maxHeight * 0.15f, 16.dp))
                    }
                    .padding(horizontal = min(maxWidth * 0.1f, 24.dp))
            ) {
                // Üst etiket
                Text(
                    text = "Date of birth",
                    lineHeight = 12.sp,
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp, start = 12.dp)
                )

                // TextField
                OutlinedTextField(
                    value = textDateOfBirth,
                    onValueChange = { textDateOfBirth = it },
                    placeholder = { Text("14/09/2005", color = Color.Gray) },
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
                    .constrainAs(radio) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(textFiled1.bottom, margin = min(maxHeight * 0.15f, 16.dp))
                    }
                    .padding(horizontal = min(maxWidth * 0.1f, 18.dp))
            ) {
                // Üst etiket
                Text(
                    text = "Choose gender",
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp, start = 12.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                ) {


                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable(onClick = {
                                selectedGender = !selectedGender
                            })) {

                            Icon(painter = painterResource(R.drawable.gender_man_icon),
                                contentDescription = null,
                                tint = if (selectedGender) Color(0xFFB1B1B1) else  Color(0xFF4FA5E3),
                                modifier = Modifier.size(32.dp))

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Male",
                                fontSize = 14.sp,
                                fontFamily = PoppinBold,
                                color = if (selectedGender) Color(0xFFB1B1B1) else  Color(0xFF4FA5E3),
                                modifier = Modifier.padding(start = 8.dp)
                            )


                        }
                        Spacer(modifier = Modifier.width(12.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(onClick = {
                            selectedGender = !selectedGender
                        })) {

                        Icon(painter = painterResource(R.drawable.gender_female_icon),
                            contentDescription = null,
                            tint = if (selectedGender) Color(0xFF4FA5E3) else Color(0xFFB1B1B1),
                            modifier = Modifier.size(32.dp))

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Female",
                            fontSize = 14.sp,
                            fontFamily = PoppinBold,
                            color = if (selectedGender) Color(0xFF4FA5E3) else Color(0xFFB1B1B1),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }


            Button(
                onClick = {
                    navControllerAppHost.navigate("OnBoardingScreenStart")
                },
                modifier = Modifier
                    .constrainAs(btn) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(radio.bottom, margin = 12.dp)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = min(maxWidth * 0.1f, 40.dp)) // responsive yatay padding
                    .heightIn(min = max(maxHeight * 0.06f, 48.dp), max = max(maxHeight * 0.08f, 64.dp)) // responsive yükseklik
                    .widthIn(max = contentMaxWidth)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4BA9E6))
            ) {
                Row {
                    Text(
                        text = "Sing Up",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = PoppinBold
                    )

                }


            }
            Text(text="back",
                fontFamily = PoppinSemiBold,
                fontSize = 20.sp,
                color = Color(0xFFB1B1B1),
                modifier = Modifier.clickable(onClick = {
                    navControllerAppHost.navigate("SignUpScreen"){
                        popUpTo(navControllerAppHost.graph.id) { inclusive = true } //stacde biriken ekranları silemye yarıyor.
                    }
                }).constrainAs(backBtn) {

                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(btn.bottom, margin = 16.dp)

                }
            )



            Row(modifier = Modifier.constrainAs(title4) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 24.dp)
            }) {

                Text(text = "Do you have an account ",
                    fontSize = 12.sp,
                    color = Color(0xFF909090),
                    fontFamily = PoppinBold,
                )
                Text(text = " Log in",
                    fontSize = 12.sp,
                    modifier = Modifier.clickable(onClick = {
                        navControllerAppHost.navigate("LogInScreen"){
                            popUpTo(navControllerAppHost.graph.id) { inclusive = true } //stacde biriken ekranları silemye yarıyor.
                        }
                    }),
                    color =Color(0xFF4BA9E6),
                    fontFamily = PoppinBold,
                )

            }


        }


    }

}

