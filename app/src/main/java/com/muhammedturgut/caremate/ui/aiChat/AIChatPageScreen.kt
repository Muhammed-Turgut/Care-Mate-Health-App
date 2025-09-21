package com.muhammedturgut.caremate.ui.aiChat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.muhammedturgut.caremate.R
import com.muhammedturgut.caremate.data.local.room.RoomViewModel
import com.muhammedturgut.caremate.ui.theme.PoppinRegular
import com.muhammedturgut.caremate.ui.theme.PoppinSemiBold
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AIChatPageScreen(
    navControllerAppHost: NavController,
    chatViewModel: ChatViewModel = hiltViewModel(),
    roomViewModel: RoomViewModel = hiltViewModel()
) {
    // ViewModel state'lerini observe et
    val messages by chatViewModel.messages.observeAsState(emptyList())
    val aiResponse by chatViewModel.getAiMessage.observeAsState()
    val isLoading by chatViewModel.isLoading.observeAsState(false)
    val getAllItemChatList by roomViewModel.getAllChatItemList.collectAsState()
    val error by chatViewModel.error.observeAsState()
    var text by remember { mutableStateOf("") }

    val dateTime = DateTimeUtils.getCurrentDateTimeFormatted()
    LaunchedEffect(aiResponse) {
        roomViewModel.insertChatData(send = if (aiResponse!!.isUser) "User" else "AI", contentMessage = aiResponse!!.text, date = dateTime)
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFAFAFA),
                        Color(0xFFFAFAFA),
                        Color(0xFF70A056),
                        Color(0xFF4FA5E3),
                        Color(0xFFFAFAFA),
                        Color(0xFFFAFAFA),
                    )
                )
            )
    ) {
        BackHandler {
            navControllerAppHost.navigate("NavBarHostScreen") {
                popUpTo(navControllerAppHost.graph.id) {
                    inclusive = true
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Image(
                    painter = painterResource(R.drawable.arrow_back_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(onClick = {
                            navControllerAppHost.navigate("NavBarHostScreen") {
                                popUpTo(navControllerAppHost.graph.id) {
                                    inclusive = true
                                }
                            }
                        })
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = {
                            roomViewModel.deleteAllChatDataItem()
                        })
                        .background(Color(0xFFF3F3F3))
                ) {

                    Text(
                        text = "AI Doktor",
                        fontFamily = PoppinSemiBold,
                        fontSize = 20.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF70A056),
                        modifier = Modifier.padding(start = 12.dp, top = 4.dp, bottom = 4.dp)
                    )

                    Image(
                        painter = painterResource(R.drawable.plus_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp, top = 4.dp, bottom = 4.dp)
                            .size(20.dp)
                    )
                }

                Image(
                    painter = painterResource(R.drawable.choice_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            // DÜZELTME: Chat temizleme özelliği
                            chatViewModel.clearChat()
                        }
                )
            }

            // Chat Messages Area
            // Chat Messages Area - Düzeltilmiş kısım
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 12.dp),
                contentAlignment = if (getAllItemChatList.isEmpty() && !isLoading) Alignment.Center else Alignment.TopCenter
            ) {

                if (getAllItemChatList.isEmpty() && !isLoading) {
                    Text(
                        text = "Merhaba! Ben AI Doktorunuzum.\nBelirtilerinizi anlatın, size yardımcı olmaya çalışayım.",
                        color = Color(0xFF666666),
                        fontFamily = PoppinRegular,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        reverseLayout = false,  // ✅ Normal sıralama
                        verticalArrangement = Arrangement.Top  // ✅ Üstten başla
                    ) {
                        // Mesajları normal sırayla göster (ters çevirme)
                        items(getAllItemChatList) { msg ->
                            msg?.let {
                                ChatBubble(
                                    text = it.contentsMessage ?: "",
                                    isUser = (it.senderMessege ?: "") == "User",
                                    dateTime = it.dateTime ?: dateTime  // ✅ Mesajın kendi tarihini kullan
                                )
                            }
                        }

                        // Loading indicator en altta
                        if (isLoading) {
                            item {
                                ChatBubble(
                                    text = "Belirtileriniz analiz ediliyor...",
                                    isUser = false,
                                    isLoading = true,
                                    dateTime = dateTime
                                )
                            }
                        }
                    }
                }
            }
        }

        // Input Area - Bottom Fixed
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .height(52.dp)
                    .width(320.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color(0xFFE4E3E3), CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.paperclip_icon),
                        contentDescription = "Attachment",
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicTextField(
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                        },
                        enabled = !isLoading,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            fontFamily = FontFamily.SansSerif
                        ),
                        decorationBox = { innerTextField ->
                            if (text.isEmpty()) {
                                Text(
                                    text = if (isLoading) "Analiz ediliyor..." else "Belirtilerinizi yazın (ör: baş ağrısı, ateş)...",
                                    color = Color(0xFFC6C6C6),
                                    fontSize = 14.sp,
                                    fontFamily = PoppinRegular
                                )
                            }
                            innerTextField()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier.size(54.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = Color(0xFF70A056),
                        strokeWidth = 3.dp
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.send_icon),
                        contentDescription = "Send",
                        modifier = Modifier
                            .size(54.dp)
                            .clickable(
                                enabled = text.isNotBlank() && !isLoading
                            ) {
                                if (text.isNotBlank()) {
                                    // ViewModel'deki sendMessage metodunu kullan
                                    chatViewModel.sendMessage(text.trim())
                                    roomViewModel.insertChatData(send = "User", contentMessage = text, date = dateTime)
                                    // Input'u temizle
                                    text = ""
                                }

                            }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    text: String,
    isUser: Boolean,
    isLoading: Boolean = false,
    dateTime: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        // Ana mesaj balonu
        Box(
            modifier = Modifier
                .background(
                    color = if (isUser) Color(0xFF70A056) else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(
                        topStart = if (isUser) 16.dp else 4.dp,
                        topEnd = if (isUser) 4.dp else 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .padding(12.dp)
        ) {
            if (isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color(0xFF70A056),
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = text,
                        color = Color.Black,
                        fontFamily = PoppinRegular,
                        fontSize = 14.sp
                    )
                }
            } else {
                Text(
                    text = text,
                    color = if (isUser) Color.White else Color.Black,
                    fontFamily = PoppinRegular,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }

        // Tarih/saat bilgisi - balonun altında
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = dateTime,
            fontFamily = PoppinRegular,
            fontSize = 10.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}



object DateTimeUtils {

    /**
     * Şu anki tarih ve saati döndürür
     * Format: "Mon Sep 21, 2025 - 14:30"
     */
    fun getCurrentDateTimeFormatted(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("EEE MMM dd, yyyy - HH:mm", Locale.ENGLISH)
        return dateFormat.format(currentDate)
    }

    /**
     * Sadece tarihi kısaltılmış formatta döndürür
     * Format: "Mon Sep 21, 2025"
     */
    fun getCurrentDateFormatted(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("EEE MMM dd, yyyy", Locale.ENGLISH)
        return dateFormat.format(currentDate)
    }

    /**
     * Sadece saati döndürür
     * Format: "14:30"
     */
    fun getCurrentTimeFormatted(): String {
        val currentDate = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(currentDate)
    }

    /**
     * Timestamp'i formatlanmış tarihe çevirir
     * @param timestamp: Long (milisaniye)
     * @return "Mon Sep 21, 2025 - 14:30"
     */
    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("EEE MMM dd, yyyy - HH:mm", Locale.ENGLISH)
        return dateFormat.format(date)
    }

    /**
     * ChatMessage için timestamp formatı
     * @param timestamp: Long (milisaniye)
     * @return "Mon Sep 21 - 14:30"
     */
    fun formatMessageTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("EEE MMM dd - HH:mm", Locale.ENGLISH)
        return dateFormat.format(date)
    }

    /**
     * Özelleştirilebilir format
     * @param format: Istenen format (örn: "EEE MMM dd, yyyy")
     * @param timestamp: Opsiyonel timestamp, yoksa şu anki zaman
     */
    fun getCustomFormattedDateTime(format: String, timestamp: Long? = null): String {
        val date = if (timestamp != null) Date(timestamp) else Date()
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return dateFormat.format(date)
    }

    /**
     * Türkçe gün isimleriyle format (İsteğe bağlı)
     * Format: "Pts Eyl 21, 2025 - 14:30"
     */
    fun getCurrentDateTimeFormattedTurkish(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("EEE MMM dd, yyyy - HH:mm", Locale("tr", "TR"))
        return dateFormat.format(currentDate)
    }
}

// Extension fonksiyonlar
fun Long.toFormattedDateTime(): String {
    return DateTimeUtils.formatTimestamp(this)
}

fun Long.toMessageTimestamp(): String {
    return DateTimeUtils.formatMessageTimestamp(this)
}