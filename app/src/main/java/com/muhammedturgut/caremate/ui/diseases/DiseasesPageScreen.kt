package com.muhammedturgut.caremate.ui.diseases

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.ui.theme.PoppinBold
import com.muhammedturgut.caremate.ui.theme.PoppinMedium
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold


@Composable
fun DiseasesPageScreen(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFAFAFA))){

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)){

            Text(text = "Diseases",
                fontSize = 24.sp,
                fontFamily = PoppinBold,
                color= Color(0xFF70A056)
            )

            val scrollState = rememberScrollState()
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .horizontalScroll(scrollState)){

                Row(modifier = Modifier
                    .background(Color.White)
                    .clip(CircleShape)
                    .border(1.dp, Color(0xFFE4E3E3), CircleShape),
                    verticalAlignment = Alignment.CenterVertically){

                    Image(painter = painterResource(R.drawable.women_healthy_life_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                            .size(24.dp))

                    Text(text = "women's health",
                        fontFamily = PoppinMedium,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        color = Color(0xFF787878),
                        modifier = Modifier.padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(modifier = Modifier
                    .background(Color.White)
                    .clip(CircleShape)
                    .border(1.dp, Color(0xFFE4E3E3), CircleShape),
                    verticalAlignment = Alignment.CenterVertically){

                    Image(painter = painterResource(R.drawable.gender_man_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                            .size(24.dp))

                    Text(text = "men's health",
                        fontFamily = PoppinMedium,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        color = Color(0xFF787878),
                        modifier = Modifier.padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(modifier = Modifier
                    .background(Color.White)
                    .clip(CircleShape)
                    .border(1.dp, Color(0xFFE4E3E3), CircleShape),
                    verticalAlignment = Alignment.CenterVertically){

                    Image(painter = painterResource(R.drawable.virus_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                            .size(24.dp))

                    Text(text = "Infectious Diseases",
                        fontFamily = PoppinMedium,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        color = Color(0xFF787878),
                        modifier = Modifier.padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                    )
                }


            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 164.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(diseasesItemList){ item ->

                    DiseasesRow(item)
                    Spacer(modifier = Modifier.width(32.dp))

                }
            }



        }

    }
}

@Composable
private fun DiseasesRow(item:DiseasesItem){
    Box(modifier = Modifier
        .size(164.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(item.color)){

        Column(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Image(painter = painterResource(item.image),
                contentDescription = null,
                modifier = Modifier.size(82.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = item.name,
                fontFamily = PoppinSemiBold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.White)

        }
    }
}

data class DiseasesItem(val name:String, val image: Int, val color: Color)

val diseasesItemList = listOf<DiseasesItem>(
    DiseasesItem(name = "General Health & Common", image = R.drawable.stethoscope_icon, color = Color(0xFFCB0404)),
    DiseasesItem(name = "Skin Diseases", image = R.drawable.skin_diseases_icon, color = Color(0xFFF4631E)),
    DiseasesItem(name = "Respiratory System", image = R.drawable.respiratory_system_icon, color = Color(0xFF309898)),
    DiseasesItem(name = "Heart and Circulation", image = R.drawable.heart_and_circulation_icon, color = Color(0xFFFF9F00)),
    DiseasesItem(name = "Digestive system", image = R.drawable.digestive_system_icon, color = Color(0xFF70A056)),
    DiseasesItem(name = "Nervous System & Psychology", image = R.drawable.nervous__system_psychology_icon, color = Color(0xFF4FA5E3)),
    DiseasesItem(name = "Musculoskeletal System", image = R.drawable.musculoskeletal_system_icon, color = Color(0xFFF18E35)),
    DiseasesItem(name = "Child health", image = R.drawable.child_health_icon, color = Color(0xFFFFD400)),
)

@Preview(showBackground = true)
@Composable
private fun Show(){
    DiseasesPageScreen()
}