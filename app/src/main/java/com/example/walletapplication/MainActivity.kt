@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.walletapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.walletapplication.ui.theme.WalletApplicationTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.navigation.compose.rememberNavController

object GlobalData {
    val hareketListe = mutableListOf<Hareket>()
    var totalAmount:Int = 0;
}



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletApplicationTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    // Hareketler sayfasını ekleyin
                    composable("home") {

                        WalletContent(navController)
                    }
                    composable("hareketEkle") {
                        HareketEklePage(navController)
                    }
                    composable("gelirleriGoster") {
                        GelirGosterPage(navController)
                    }
                    composable("giderleriGoster") {
                        GiderGosterPage(navController)
                    }
                }
            }
        }
    }
}





@Composable
fun ActionButtonf(
    text: String,
    actionType: ActionType,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            when (actionType) {
                ActionType.gelirleriGoster -> navigate(navController,"gelirleriGoster")
                ActionType.giderleriGoster -> navigate(navController,"giderleriGoster")
                ActionType.hareketEkle -> navigate(navController,"hareketEkle")
            }
        },
         modifier = modifier.padding(8.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = getColor(actionType),
            contentColor = Color.White
        )
    ) {
        Text(text = text)
    }
}
enum class ActionType{
    gelirleriGoster,
    giderleriGoster,
    hareketEkle
}

@Composable
private fun getColor(actionType: ActionType): Color {
    return when (actionType) {
        ActionType.gelirleriGoster -> Color.Green
        ActionType.giderleriGoster -> Color.Red
        ActionType.hareketEkle -> MaterialTheme.colorScheme.primary

        else -> {return Color.White}
    }
}
@Composable
fun ListeGoster(hareketListe: List<Hareket>) {
    LazyColumn {
        items(items = hareketListe.reversed()) { hareket ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Açıklama
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = hareket.date,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Açıklama: ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(text = hareket.aciklama)
                    }


                    // Tarih


                    Spacer(modifier = Modifier.height(8.dp))

                    // Tutar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tutar:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "${hareket.tutar} TL",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (hareket.gelirMi) Color.Green else Color.Red
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}












@Composable
fun WalletContent(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WalletHeader()

        // Butonlar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ActionButtonf(
                text = "Gelirleri Göster",
                actionType = ActionType.gelirleriGoster,
                navController = navController,
                modifier = Modifier
                    .weight(1f)

            )
            Spacer(modifier = Modifier.width(5.dp))
            ActionButtonf(
                text = "Giderleri Göster",
                actionType = ActionType.giderleriGoster,
                navController= navController,
                modifier = Modifier
                    .weight(1f)

            )
            Spacer(modifier = Modifier.width(5.dp))
            ActionButtonf(
                text = "Hareket Ekle",
                actionType = ActionType.hareketEkle,
                navController= navController,
                modifier = Modifier
                    .weight(1f)

            )
        }
        ListeGoster(hareketListe = GlobalData.hareketListe)
    }
}
data class Hareket(
    var gelirMi:Boolean,
    var aciklama:String,
    var tutar:Int,
    var date: String
)



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HareketEklePage(navController: NavHostController) {
    var aciklama by remember { mutableStateOf("") }
    var tutar by remember { mutableStateOf("") }
    var gelirMi by remember { mutableStateOf(true) }
    val hareketListe = remember { mutableStateListOf<Hareket>() }
    var uyarıMesajı by remember { mutableStateOf<String?>(null) }
    var currentDate = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("tr","TR")).format(Date())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Anasayfa") },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                            .width(35.dp)
                            .height(35.dp)
                    ) {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Geri Dön")
                        }
                    }

                },

            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Açıklama Girişi
            OutlinedTextField(
                value = aciklama,
                onValueChange = { newValue -> aciklama = newValue },
                label = { Text("Açıklama") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Tutar Girişi
            OutlinedTextField(
                value = tutar,
                onValueChange = { newValue ->
                    // Sadece sayısal değerlere izin vermek için
                    val newText = newValue.filter { char -> char.isDigit() }
                    tutar = newText
                },
                label = { Text("Tutar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            uyarıMesajı?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
            }

            // Gelir Ekle ve Gider Ekle Butonları
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        // Gelir Ekle butonuna tıklanınca yapılacak işlemler
                        if (aciklama.isBlank() && !tutar.isBlank()) {
                            uyarıMesajı = "Lütfen açıklama alanını doldurunuz"
                        } else if (tutar.isBlank() && !aciklama.isBlank()) {
                            uyarıMesajı = "Lütfen tutar alanını doldurunuz"
                        } else if (tutar.isBlank() && aciklama.isBlank()) {
                            uyarıMesajı = "Lütfen boş kalan alanları doldurunuz"
                        } else {
                            uyarıMesajı = null

                            val yeniHareket = Hareket(
                                gelirMi = true,
                                aciklama = aciklama,
                                tutar = tutar.toIntOrNull() ?: 0,
                                date = currentDate
                            )
                            GlobalData.hareketListe.add(yeniHareket)
                            // Hareket eklendikten sonra inputları sıfırla
                            aciklama = ""
                            tutar = ""
                            val gelirTutar = GlobalData.hareketListe
                                .filter { hareket -> hareket.gelirMi == true }
                                .sumOf { hareket -> hareket.tutar }

                            val giderTutar = GlobalData.hareketListe
                                .filter { hareket -> hareket.gelirMi == false }
                                .sumOf { hareket -> hareket.tutar }

                            val totalAmount = gelirTutar - giderTutar
                            GlobalData.totalAmount = totalAmount;
                            navController.navigate("home");

                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = getColor(ActionType.gelirleriGoster),
                        contentColor = Color.White
                    )
                ) {
                    Text("Gelir Ekle", color = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        // Gider Ekle butonuna tıklanınca yapılacak işlemler
                        if (aciklama.isBlank() && !tutar.isBlank()) {
                            uyarıMesajı = "Lütfen açıklama alanını doldurunuz"
                        } else if (tutar.isBlank() && !aciklama.isBlank()) {
                            uyarıMesajı = "Lütfen tutar alanını doldurunuz"
                        } else if (tutar.isBlank() && aciklama.isBlank()) {
                            uyarıMesajı = "Lütfen boş kalan alanları doldurunuz"
                        } else {
                            uyarıMesajı = null
                            val yeniHareket = Hareket(
                                gelirMi = false,
                                aciklama = aciklama,
                                tutar = tutar.toIntOrNull() ?: 0,
                                date = currentDate
                            )
                            GlobalData.hareketListe.add(yeniHareket)


                            // Hareket eklendikten sonra inputları sıfırla
                            val gelirTutar = GlobalData.hareketListe
                                .filter { hareket -> hareket.gelirMi == true }
                                .sumOf { hareket -> hareket.tutar }

                            val giderTutar = GlobalData.hareketListe
                                .filter { hareket -> hareket.gelirMi == false }
                                .sumOf { hareket -> hareket.tutar }

                            val totalAmount = gelirTutar - giderTutar
                            GlobalData.totalAmount = totalAmount;

                            aciklama = ""
                            tutar = ""
                            navController.navigate("home");
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = getColor(ActionType.giderleriGoster),
                        contentColor = Color.White
                    )
                ) {
                    Text("Gider Ekle", color = Color.White)
                }
            }
        }

    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GelirGosterPage(navController: NavHostController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Anasayfa") },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                            .width(35.dp)
                            .height(35.dp)
                    ) {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Geri Dön"
                            )
                        }
                    }

                },

                )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 80.dp, end = 16.dp, bottom = 16.dp),


            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        ListeGoster(hareketListe = GlobalData.hareketListe.filter { hareket -> hareket.gelirMi == true })

        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GiderGosterPage(navController: NavHostController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Anasayfa") },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                            .width(35.dp)
                            .height(35.dp)
                    ) {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Geri Dön")
                        }
                    }

                },

                )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 80.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            ListeGoster(hareketListe = GlobalData.hareketListe.filter { hareket -> hareket.gelirMi == false })
        }
    }
}


private fun navigate(navController: NavHostController,route:String) {
    navController.navigate(route)
}

@Composable
fun WalletHeader() {
    val currentDate = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("tr","TR")).format(Date())

    Column(
        modifier = Modifier

            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Cüzdan İkonu ve Başlık
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.wallet_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Cüzdan Uygulaması",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 0.05.em
                ),
                modifier = Modifier.padding(8.dp)
            )
        }

        // Toplam Para ve Tarih/Saat
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape((30.dp)))
                .padding(16.dp)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Toplam Para
                Text(
                    text = "Toplam Para: ${GlobalData.totalAmount}", // Değiştirilebilir
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White // Toplam para metni rengi
                    ),
                    modifier = Modifier.padding(8.dp)
                )

                // Tarih ve Saat
                Text(
                    text = currentDate,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White // Tarih ve saat metni rengi
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun ActionButton(text: String, color: Color) {
    Button(
        onClick = { /* Handle button click */ },
        modifier = Modifier

            .padding(8.dp)
            .height(50.dp)
            .background(color),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WalletApplicationTheme {
        WalletHeader()

    }
}
