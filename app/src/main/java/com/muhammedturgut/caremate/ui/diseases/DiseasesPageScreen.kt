package com.muhammedturgut.caremate.ui.diseases

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
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
fun DiseasesPageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(16.dp)
    ) {

        Text(
            text = "Diseases",
            fontSize = 24.sp,
            fontFamily = PoppinBold,
            color = Color(0xFF70A056)
        )

        Spacer(modifier = Modifier.height(8.dp))


        CategoryFilters()

        Spacer(modifier = Modifier.height(16.dp))


        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 164.dp),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(diseasesItemList) { item ->
                DiseasesCard(item = item)
            }
        }
    }
}

@Composable
private fun CategoryFilters() {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CategoryChip(
            icon = R.drawable.women_healthy_life_icon,
            text = "women's health"
        )

        CategoryChip(
            icon = R.drawable.gender_man_icon,
            text = "men's health"
        )

        CategoryChip(
            icon = R.drawable.virus_icon,
            text = "Infectious Diseases"
        )
    }
}

@Composable
private fun CategoryChip(icon: Int, text: String) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .clip(CircleShape)
            .border(1.dp, Color(0xFFE4E3E3), CircleShape)
            .clickable { /* Handle click */ }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            fontFamily = PoppinMedium,
            fontSize = 12.sp,
            color = Color(0xFF787878)
        )
    }
}

@Composable
private fun DiseasesCard(item: DiseasesItem) {
    Box(
        modifier = Modifier
            .size(164.dp) // Sabit boyut
            .clip(RoundedCornerShape(8.dp))
            .background(item.color)
            .clickable { /* Handle click */ }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(item.image),
                contentDescription = item.name,
                modifier = Modifier.size(82.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = item.name,
                fontFamily = PoppinSemiBold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

data class DiseasesItem(val name: String, val image: Int, val color: Color)

val diseasesItemList = listOf(
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
private fun DiseasesPageScreenPreview() {
    DiseasesPageScreen()
}