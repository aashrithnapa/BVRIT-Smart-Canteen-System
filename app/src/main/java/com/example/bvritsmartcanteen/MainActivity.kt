@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.bvritsmartcanteen

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.bvritsmartcanteen.ui.theme.BVRITSmartCanteenTheme
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.concurrent.TimeUnit
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.ui.zIndex

enum class Screen {
    SPLASH,
    LOGIN_PHONE,      // ✅ NEW
    LOGIN_OTP,

    LOCATION,
    MODE,
    HOME,
    ITEM,
    CART,
    COMING_SOON,
    ORDER_SUCCESS,
    PROFILE,
    STAFF_LOGIN,   // ✅ add
    ADMIN_LOGIN,   // ✅ add
    STAFF_HOME,    // (for next step)
    ADMIN_HOME,
    STAFF_ITEM_CONTROL,   // 🔥 ADD THIS
    ADMIN_DASHBOARD,
    ADMIN_ADD_ITEM,
    ADMIN_UPDATE_ITEMS,
    ADMIN_STOCK,
    ADMIN_REPORTS,
    ADMIN_FEEDBACK,
    ADMIN_ITEM_EDIT,
    ORDER_DETAIL,
    ORDER_TRACKING,
    ORDER_HISTORY
}

enum class UserRole {
    STUDENT,
    STAFF,
    ADMIN
}

data class FoodItem(
    val name: String,
    val price: Int,
    val categories: List<String>,
    val cookingTime: Int,
    val rating: Double,
    val id: String = "",
    val isAvailable: Boolean = true,
    val location: String = "D2",
    val imageUrl: String = ""   // 🔥 NEW
)

data class CartItem(
    val food: FoodItem,
    val quantity: Int,
    val note: String,
    val cookingTime: Int
)

data class Admin(
    val id: String,
    val password: String,
    val location: String
)

fun generateQrCode(data: String): ImageBitmap {

    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)

    val bitmap = createBitmap(512, 512, Bitmap.Config.RGB_565)

    for (x in 0 until 512) {
        for (y in 0 until 512) {
            bitmap[x, y] = if (bitMatrix[x, y]) android.graphics.Color.BLACK
            else android.graphics.Color.WHITE
        }
    }

    return bitmap.asImageBitmap()
}

fun generateOrderId(location: String, total: Int): String {
    val time = System.currentTimeMillis().toString().takeLast(6)

    val locCode = if (location == "D2") "D2" else "FC"

    return "BVRN-$locCode-$time-$total"
}

fun payWithUpi(
    context: android.content.Context,
    amount: Int,
    upiId: String,
    packageName: String? = null   // 🔥 NEW
) {

    val uri = "upi://pay?pa=$upiId&pn=BVRIT Smart Canteen&am=$amount&cu=INR".toUri()

    val intent = Intent(Intent.ACTION_VIEW, uri)

    // 🔥 If specific app needed
    if (packageName != null) {
        intent.setPackage(packageName)
    }

    try {
        context.startActivity(intent)
    } catch (_: Exception) {
        // fallback to chooser
        context.startActivity(Intent.createChooser(intent, "Pay with"))
    }
}

fun addToCart(
    food: FoodItem,
    qty: Int,
    note: String,
    cartItems: List<CartItem>
): List<CartItem> {

    val cleanNote = note.trim()

    val existing = cartItems.find {
        it.food.name == food.name && it.note == cleanNote
    }

    return if (existing != null) {
        cartItems.map {
            if (it.food.name == food.name && it.note == cleanNote) {
                it.copy(quantity = it.quantity + qty)
            } else it
        }
    } else {
        cartItems + CartItem(
            food = food,
            quantity = qty,
            note = cleanNote,
            cookingTime = food.cookingTime
        )
    }
}

val d2Menu = listOf(

    // 🍳 TIFFINS (live cooking)
    FoodItem("Hostel Tiffins", 40, listOf("Tiffins", "Veg"), 4, 4.0),
    FoodItem("Poori", 45, listOf("Tiffins", "Veg"), 4, 4.1),
    FoodItem("Idli", 35, listOf("Tiffins", "Veg"), 3, 4.0),
    FoodItem("Plain Dosa", 40, listOf("Tiffins", "Veg"), 7, 4.1),
    FoodItem("Onion Dosa", 45, listOf("Tiffins", "Veg"), 7, 4.2),
    FoodItem("Masala Dosa", 45, listOf("Tiffins", "Veg"), 9, 4.3),
    FoodItem("Egg Dosa", 50, listOf("Tiffins", "Egg"), 10, 4.2),

    // 🍛 MEALS (ready + serve)
    FoodItem("Meals", 70, listOf("Meals", "Veg"), 4, 4.1),
    FoodItem("Meals Parcel", 80, listOf("Meals", "Veg"), 6, 4.0),
    FoodItem("One Curry", 20, listOf("Meals", "Veg"), 2, 3.9),
    FoodItem("One Curd Cup", 15, listOf("Meals", "Veg"), 1, 3.8),

    // 🍗 NON-VEG
    FoodItem("Chicken Biryani", 150, listOf("NonVeg", "Biryani"), 10, 4.7),
    FoodItem("Fry Chicken Biryani", 160, listOf("NonVeg", "Biryani"), 12, 4.6),
    FoodItem("Chicken Fried Rice", 90, listOf("NonVeg", "Chinese"), 8, 4.5),
    FoodItem("Chicken Noodles", 70, listOf("NonVeg", "Chinese"), 8, 4.3),
    FoodItem("Chicken Manchurian", 70, listOf("NonVeg", "Chinese"), 9, 4.4),
    FoodItem("Chicken Parota", 100, listOf("NonVeg"), 10, 4.2),
    FoodItem("White Chicken Fried Rice", 100, listOf("NonVeg", "Chinese"), 9, 4.3),

    // 🥚 EGG
    FoodItem("Egg Fried Rice", 70, listOf("Egg", "Chinese"), 8, 4.2),
    FoodItem("White Egg Fried Rice", 80, listOf("Egg", "Chinese"), 8, 4.2),
    FoodItem("Egg Noodles", 60, listOf("Egg", "Chinese"), 8, 4.1),
    FoodItem("Egg Rolls", 60, listOf("Egg", "Snacks"), 6, 4.2),
    FoodItem("Egg Burji", 50, listOf("Egg", "Snacks"), 5, 4.0),

    // 🥦 VEG
    FoodItem("Veg Fried Rice", 60, listOf("Veg", "Chinese"), 7, 4.1),
    FoodItem("White Veg Fried Rice", 70, listOf("Veg", "Chinese"), 7, 3.9 ),
    FoodItem("Veg Noodles", 45, listOf("Veg", "Chinese"), 7, 4.0),
    FoodItem("Veg Manchuria", 60, listOf("Veg", "Chinese"), 9, 4.2),
    FoodItem("Veg Manchurian Fried Rice", 90, listOf("Veg", "Chinese"), 9, 4.2),
    FoodItem("Paneer Fried Rice", 100, listOf("Veg", "Chinese"), 9, 4.3),
    FoodItem("Paneer Parota", 90, listOf("Veg"), 10, 4.2),
    FoodItem("Veg Rolls", 50, listOf("Veg", "Snacks"), 6, 4.1),

    // 🌯 SNACKS
    FoodItem("Chicken Roll", 75, listOf("NonVeg", "Snacks"), 7, 4.4),
    FoodItem("Big Samosa", 15, listOf("Veg", "Snacks"), 3, 3.9),

    // ☕ DRINKS
    FoodItem("Tea", 12, listOf("Drinks"), 2, 3.8),
    FoodItem("Coffee", 15, listOf("Drinks"), 2, 3.9),
    FoodItem("Special Tea", 15, listOf("Drinks"), 2, 4.0),
    FoodItem("Special Coffee", 20, listOf("Drinks"), 3, 4.1),
    FoodItem("Butter Milk", 15, listOf("Drinks"), 2, 3.9)
)

val fcMenu = listOf(

    // 🍗 BIRYANI
    FoodItem("Chicken Dum Biryani", 140, listOf("Biryani", "NonVeg"), 12, 4.7),
    FoodItem("Egg Biryani", 110, listOf("Biryani", "Egg"), 15, 4.3),
    FoodItem("Paneer Biryani", 110, listOf("Biryani", "Veg"), 15, 4.2),

    // 🍜 CHINESE
    FoodItem("Double Egg Chicken Fried Rice", 120, listOf("Chinese", "NonVeg", "Egg"), 13, 4.6),
    FoodItem("Double Egg Chicken Noodles", 120, listOf("Chinese", "NonVeg", "Egg"), 13, 4.5),
    FoodItem("Double Egg Fried Rice", 100, listOf("Chinese", "Egg"), 12, 4.3),
    FoodItem("Double Egg Noodles", 100, listOf("Chinese", "Egg"), 12, 4.3),
    FoodItem("Chicken Fried Rice", 100, listOf("Chinese", "NonVeg"), 12, 4.5),
    FoodItem("Chicken Noodles", 100, listOf("Chinese", "NonVeg"), 12, 4.4),
    FoodItem("Egg Fried Rice", 80, listOf("Chinese", "Egg"), 11, 4.2),
    FoodItem("Egg Noodles", 80, listOf("Chinese", "Egg"), 11, 4.2),
    FoodItem("Chicken Manchurian", 100, listOf("Chinese", "NonVeg"), 14, 4.5),
    FoodItem("Chicken 65", 100, listOf("Chinese", "NonVeg"), 14, 4.6),

    // 🥦 VEG
    FoodItem("Veg Manchurian", 70, listOf("Veg", "Chinese"), 12, 4.2),
    FoodItem("Veg Manchurian Fried Rice", 90, listOf("Veg", "Chinese"), 13, 4.2),
    FoodItem("Paneer Fried Rice", 100, listOf("Veg", "Chinese"), 9, 4.3),
    FoodItem("Veg Manchurian Noodles", 90, listOf("Veg", "Chinese"), 13, 4.2),
    FoodItem("Veg Fried Rice", 70, listOf("Veg", "Chinese"), 11, 4.1),
    FoodItem("Veg Noodles", 70, listOf("Veg", "Chinese"), 11, 4.1),
    FoodItem("Jeera Rice", 80, listOf("Veg"), 8, 4.0),

    // 🌯 ROLLS
    FoodItem("Chicken Roll", 90, listOf("Rolls", "NonVeg", "Snacks"), 10, 4.5),
    FoodItem("Double Egg Chicken Roll", 100, listOf("Rolls", "NonVeg", "Egg"), 11, 4.6),
    FoodItem("Egg Roll", 40, listOf("Rolls", "Egg", "Snacks"), 8, 4.2),
    FoodItem("Egg Manchurian Roll", 70, listOf("Rolls", "Egg", "Chinese"), 10, 4.3),
    FoodItem("Double Egg Roll", 70, listOf("Rolls", "Egg"), 9, 4.3),
    FoodItem("Paneer Roll", 80, listOf("Rolls", "Veg"), 9, 4.3),
    FoodItem("Egg Paneer Roll", 90, listOf("Rolls", "Egg", "Veg"), 10, 4.4),
    FoodItem("Veg Manchurian Roll", 40, listOf("Rolls", "Veg", "Chinese"), 9, 4.1),

    // ⭐ SPECIAL
    FoodItem("Fry Piece Chicken Biryani", 150, listOf("Special", "NonVeg"), 10, 4.7),
    FoodItem("Lollipop Chicken Biryani", 180, listOf("Special", "NonVeg"), 15, 4.8),
    FoodItem("Chicken Curry Paratha (2)", 120, listOf("Special", "NonVeg"), 15, 4.5),
    FoodItem("Paneer Curry Paratha (2)", 120, listOf("Special", "Veg"), 14, 4.3),
    FoodItem("Egg Burji Curry Paratha (2)", 120, listOf("Special", "Egg"), 14, 4.4)
)

val staffAccounts = mapOf(
    "d2staff" to Pair("1997", "D2"),
    "fcstaff" to Pair("2026", "FC")
)

val admins = listOf(
    Admin("admind2", "bvritnd2", "D2"),
    Admin("adminfc", "bvritnfc", "FC")
)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window = this.window
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView)
            .isAppearanceLightStatusBars = true

        fun deleteItem(itemId: String) {
            val db = FirebaseFirestore.getInstance()

            db.collection("items")
                .document(itemId)
                .delete()
        }

        fun uploadAllItemsToFirebase(
            d2Menu: List<FoodItem>,
            fcMenu: List<FoodItem>
        ) {
            val db = FirebaseFirestore.getInstance()

            val allItems = d2Menu.map {
                hashMapOf(
                    "name" to it.name,
                    "price" to it.price,
                    "category" to it.categories,
                    "location" to "D2",
                    "cookingTime" to it.cookingTime,
                    "isAvailable" to it.isAvailable,
                    "imageUrl" to ""
                )
            } + fcMenu.map {
                hashMapOf(
                    "name" to it.name,
                    "price" to it.price,
                    "category" to it.categories,
                    "location" to "FC",
                    "cookingTime" to it.cookingTime,
                    "isAvailable" to it.isAvailable,
                    "imageUrl" to ""
                )
            }

            allItems.forEach { item ->
                val docId = "${item["name"]}_${item["location"]}"

                db.collection("items")
                    .document(docId)
                    .set(item)
            }
        }

        setContent {
            BVRITSmartCanteenTheme {

                var currentScreen by rememberSaveable { mutableStateOf(Screen.SPLASH) }
                var selectedItem by remember { mutableStateOf<FoodItem?>(null) }
                var cartItems by remember { mutableStateOf(listOf<CartItem>()) }
                var selectedLocation by remember { mutableStateOf("D2") }
                var selectedMode by remember { mutableStateOf("Dine In") }
                var pendingItem by remember { mutableStateOf<Triple<FoodItem, Int, String>?>(null) }
                var showRepeatSheet by remember { mutableStateOf(false) }
                var isRepeatItem by remember { mutableStateOf(false) }
                var orderId by remember { mutableStateOf("") }
                var orderTotal by remember { mutableIntStateOf(0) }
                var orderLocation by remember { mutableStateOf("") }
                var verificationId by remember { mutableStateOf("") }
                var phoneNumber by remember { mutableStateOf("") }
                var resendToken by remember { mutableStateOf<PhoneAuthProvider.ForceResendingToken?>(null) }
                var isLoading by remember { mutableStateOf(false) }
                var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }
                var adminLocation by remember { mutableStateOf<String?>(null) }
                var password by remember { mutableStateOf("") }
                var passwordVisible by remember { mutableStateOf(false) }
                var firebaseItems by remember { mutableStateOf<List<FoodItem>>(emptyList()) }
                var showLogoutSheet by remember { mutableStateOf(false) }
                var showConfirmDialog by remember { mutableStateOf(false) }
                var selectedOrderId by remember { mutableStateOf("") }
                var selectedOrderData by remember { mutableStateOf<Map<String, Any>?>(null) }
                var activeOrderId by remember { mutableStateOf("") }
                var activeOrderData by remember { mutableStateOf<Map<String, Any>?>(null) }
                var trackingTimerSeconds by remember { mutableIntStateOf(0) }
                var trackingTimerStarted by remember { mutableStateOf(false) }
                var confirmTitle by remember { mutableStateOf("") }
                var confirmMessage by remember { mutableStateOf("") }
                var onConfirmAction by remember { mutableStateOf<() -> Unit>({}) }

                // 🔥 ACTIVE ORDER LISTENER
                // 🔥 ACTIVE ORDER LISTENER
                LaunchedEffect(activeOrderId) {
                    if (activeOrderId.isNotEmpty()) {
                        val db = FirebaseFirestore.getInstance()
                        db.collection("orders")
                            .document(activeOrderId)
                            .addSnapshotListener { snapshot, _ ->
                                if (snapshot != null && snapshot.exists()) {
                                    val data = snapshot.data?.toMutableMap()
                                    if (data != null) {
                                        data["docId"] = snapshot.id
                                        activeOrderData = data

                                        // Auto clear when Ready
                                        val status = data["status"] as? String
                                        if (status == "Ready") {
                                            // Keep showing for 30 more seconds then clear
                                        }
                                    }
                                }
                            }
                    }
                }

                // 🔥 RESTORE ACTIVE ORDER ON APP RESTART
                LaunchedEffect(Unit) {
                    val currentUid = FirebaseAuth.getInstance().currentUser?.uid
                    if (currentUid != null) {
                        FirebaseFirestore.getInstance()
                            .collection("orders")
                            .whereEqualTo("userId", currentUid)
                            .whereIn("status", listOf("Placed", "Preparing"))
                            .get()
                            .addOnSuccessListener { snapshot ->
                                val recent = snapshot.documents
                                    .mapNotNull { it.data?.toMutableMap()?.also { d -> d["docId"] = it.id } }
                                    .maxByOrNull {
                                        (it["timestamp"] as? com.google.firebase.Timestamp)?.seconds ?: 0L
                                    }
                                if (recent != null) {
                                    activeOrderId = recent["orderId"] as? String ?: ""
                                    activeOrderData = recent
                                }
                            }
                    }
                }

                LaunchedEffect(Unit) {

                    val db = FirebaseFirestore.getInstance()

                    db.collection("items")
                        .addSnapshotListener { snapshot, error ->

                            if (error != null) {
                                error.printStackTrace()
                                return@addSnapshotListener
                            }

                            val list = snapshot?.documents?.mapNotNull { doc ->

                                val name = doc.getString("name") ?: return@mapNotNull null

                                val price = when (val p = doc.get("price")) {
                                    is Long -> p.toInt()
                                    is String -> p.toIntOrNull() ?: 0
                                    else -> 0
                                }

                                FoodItem(
                                    id = doc.id,
                                    name = name,
                                    price = price,
                                    categories = doc.get("category") as? List<String> ?: listOf("Other"),
                                    cookingTime = (doc.getLong("cookingTime") ?: 5).toInt(),
                                    rating = 4.0,
                                    isAvailable = doc.getBoolean("isAvailable") ?: true,
                                    location = doc.getString("location") ?: "D2",
                                    imageUrl = doc.getString("imageUrl") ?: ""
                                )

                            } ?: emptyList()

                            firebaseItems = list
                        }
                }

                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        slideInHorizontally(animationSpec = tween(400)) { it } +
                                fadeIn(animationSpec = tween(400)) togetherWith
                                slideOutHorizontally(animationSpec = tween(400)) { -it } +
                                fadeOut(animationSpec = tween(400))
                    }
                ) { screen ->

                    when (screen) {

                        Screen.SPLASH -> SplashScreen(
                            onFinish = { currentScreen = Screen.LOGIN_PHONE }
                        )

                        Screen.LOCATION -> LocationScreen(
                            onD2Click = {
                                selectedLocation = "D2"
                                currentScreen = Screen.MODE
                            },
                            onFCClick = {
                                selectedLocation = "FC"
                                currentScreen = Screen.MODE
                            }
                        )

                        Screen.MODE -> ModeScreen(
                            onOptionClick = { mode ->
                                selectedMode = mode
                                currentScreen = Screen.HOME
                            },
                            onBack = { currentScreen = Screen.LOCATION }
                        )

                        Screen.COMING_SOON -> ComingSoonScreen(
                            onBack = { currentScreen = Screen.LOGIN_PHONE }
                        )

                        Screen.HOME -> HomeScreen(
                            menu = firebaseItems.filter {
                                it.location == selectedLocation
                            },
                            onBack = { currentScreen = Screen.MODE },
                            onItemClick = {
                                selectedItem = it
                                currentScreen = Screen.ITEM
                            },
                            onCartClick = { currentScreen = Screen.CART },
                            cartCount = cartItems.size,
                            onProfileClick = { currentScreen = Screen.PROFILE },
                            onLogoutClick = { showLogoutSheet = true },
                            selectedLocation = selectedLocation,
                            selectedMode = selectedMode,
                            activeOrderId = activeOrderId,
                            activeOrderData = activeOrderData,
                            onTrackOrder = { currentScreen = Screen.ORDER_TRACKING }
                        )

                        Screen.ITEM -> selectedItem?.let { selectedFood ->

                            ItemScreen(
                                item = selectedFood,
                                onBack = { currentScreen = Screen.HOME },

                                onAddToCart = { food, qty, note ->

                                    val cleanNote = note.trim()

                                    val existing = cartItems.find {
                                        it.food.name == food.name && it.note == cleanNote
                                    }

                                    pendingItem = Triple(food, qty, cleanNote)

                                    // 🔥 differentiate cases
                                    isRepeatItem = existing != null   // ✅ NEW BOOLEAN
                                    showRepeatSheet = true
                                }
                            )
                        }

                        Screen.CART -> CartScreen(
                            cartItems = cartItems,
                            selectedLocation = selectedLocation,
                            selectedMode = selectedMode,
                            userPhone = FirebaseAuth.getInstance().currentUser?.phoneNumber ?: "",
                            onBack = { currentScreen = Screen.HOME },
                            onUpdateCart = { cartItems = it },
                            onOrderSuccess = { id ->
                                orderId = id
                                orderTotal = cartItems.sumOf { it.food.price * it.quantity }
                                orderLocation = selectedLocation
                                activeOrderId = id
                                cartItems = emptyList()
                                currentScreen = Screen.ORDER_SUCCESS
                            }
                        )

                        Screen.ORDER_SUCCESS -> OrderSuccessScreen(
                            orderId = orderId,
                            total = orderTotal,
                            location = orderLocation,
                            onClose = { currentScreen = Screen.HOME }
                        )

                        Screen.PROFILE -> ProfileScreen(
                            onBack = { currentScreen = Screen.HOME },
                            onLogoutClick = { showLogoutSheet = true },
                            phoneNumber = FirebaseAuth.getInstance().currentUser?.phoneNumber ?: "",
                            onOrderHistoryClick = { currentScreen = Screen.ORDER_HISTORY }
                        )

                        Screen.LOGIN_PHONE -> PhoneLoginScreen(
                            isLoading = isLoading,
                            onSendOtp = { phone ->

                                isLoading = true
                                phoneNumber = "+91$phone"

                                val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                                    .setPhoneNumber(phoneNumber)
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(this@MainActivity)
                                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                            isLoading = false
                                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        currentScreen = Screen.LOCATION
                                                    } else {
                                                        Toast.makeText(
                                                            this@MainActivity,
                                                            "Invalid OTP",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        }

                                        override fun onVerificationFailed(e: FirebaseException) {   // ✅ IMPORTANT FIX
                                            isLoading = false

                                            Toast.makeText(
                                                this@MainActivity,
                                                "OTP Failed: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                        override fun onCodeSent(
                                            verificationIdParam: String,
                                            token: PhoneAuthProvider.ForceResendingToken
                                        ) {
                                            verificationId = verificationIdParam
                                            resendToken = token  // 👈 save it
                                            isLoading = false
                                            currentScreen = Screen.LOGIN_OTP
                                        }
                                    })
                                    .build()

                                PhoneAuthProvider.verifyPhoneNumber(options)
                            },
                            onBack = { currentScreen = Screen.LOGIN_PHONE },
                            onStaffClick = { currentScreen = Screen.STAFF_LOGIN },   // ✅ ADD
                            onAdminClick = { currentScreen = Screen.ADMIN_LOGIN }    // ✅ ADD
                        )

                        Screen.LOGIN_OTP -> OtpScreen(
                            onVerify = { otp ->

                                val credential = PhoneAuthProvider.getCredential(
                                    verificationId,
                                    otp
                                )

                                FirebaseAuth.getInstance().signInWithCredential(credential)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val user = FirebaseAuth.getInstance().currentUser
                                            val uid = user?.uid ?: ""
                                            val phone = user?.phoneNumber ?: ""

                                            val db = FirebaseFirestore.getInstance()

                                            // Check if user already exists
                                            db.collection("users").document(uid).get()
                                                .addOnSuccessListener { doc ->
                                                    if (!doc.exists()) {
                                                        // First time login — create profile
                                                        val newUser = hashMapOf(
                                                            "uid" to uid,
                                                            "phone" to phone,
                                                            "name" to "",
                                                            "rollNumber" to "",
                                                            "email" to "",
                                                            "createdAt" to com.google.firebase.Timestamp.now()
                                                        )
                                                        db.collection("users").document(uid).set(newUser)
                                                    }
                                                    currentScreen = Screen.LOCATION
                                                }
                                                .addOnFailureListener {
                                                    // Even if save fails, let them in
                                                    currentScreen = Screen.LOCATION
                                                }
                                        }
                                    }
                            },
                            onBack = { currentScreen = Screen.LOGIN_PHONE },
                            phoneNumber = phoneNumber,
                            onResendOtp = {
                                resendToken?.let { token ->
                                    PhoneAuthProvider.verifyPhoneNumber(
                                        PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                                            .setPhoneNumber(phoneNumber)
                                            .setTimeout(60L, TimeUnit.SECONDS)
                                            .setActivity(this@MainActivity)
                                            .setForceResendingToken(token)  // 👈 use saved token
                                            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                override fun onVerificationCompleted(credential: PhoneAuthCredential) {}
                                                override fun onVerificationFailed(e: FirebaseException) {}
                                                override fun onCodeSent(
                                                    newVerificationId: String,
                                                    newToken: PhoneAuthProvider.ForceResendingToken
                                                ) {
                                                    verificationId = newVerificationId
                                                    resendToken = newToken
                                                }
                                            })
                                            .build()
                                    )
                                }
                            }
                        )

                        Screen.STAFF_LOGIN -> StaffLoginScreen(
                            onLoginSuccess = { location ->
                                selectedLocation = location
                                currentScreen = Screen.STAFF_HOME
                            },
                            onBack = { currentScreen = Screen.LOGIN_PHONE }
                        )

                        Screen.ADMIN_LOGIN -> AdminLoginScreen(
                            onLoginSuccess = { location ->
                                adminLocation = location
                                currentScreen = Screen.ADMIN_DASHBOARD   // 🔥 FIX HERE
                            },
                            onBack = { currentScreen = Screen.LOGIN_PHONE }
                        )

                        Screen.STAFF_HOME -> StaffHomeScreen(
                            menu = firebaseItems.filter { it.location == selectedLocation },
                            onItemClick = { food ->
                                selectedItem = food
                                currentScreen = Screen.STAFF_ITEM_CONTROL
                            },
                            onBack = { currentScreen = Screen.LOGIN_PHONE },
                            onLogoutClick = { showLogoutSheet = true },
                            selectedLocation = selectedLocation,
                            onFeedbackClick = { currentScreen = Screen.ADMIN_FEEDBACK } // 👈 add this
                        )

                        Screen.STAFF_ITEM_CONTROL -> selectedItem?.let {
                            StaffItemControlScreen(
                                item = it,
                                onBack = { currentScreen = Screen.STAFF_HOME }
                            )
                        }

                        Screen.ADMIN_HOME -> AdminDashboardScreen(
                            d2Menu = d2Menu,
                            fcMenu = fcMenu,
                            onAdd = { currentScreen = Screen.ADMIN_ADD_ITEM },
                            onUpdate = { currentScreen = Screen.ADMIN_UPDATE_ITEMS },
                            onStock = { currentScreen = Screen.ADMIN_STOCK },
                            onFeedback = { _, _ -> currentScreen = Screen.ADMIN_FEEDBACK },
                            onLogout = { showLogoutSheet = true },
                            location = adminLocation ?: "D2"
                        )

                        Screen.ADMIN_DASHBOARD -> AdminDashboardScreen(
                            d2Menu = d2Menu,
                            fcMenu = fcMenu,
                            onAdd = { currentScreen = Screen.ADMIN_ADD_ITEM },
                            onUpdate = { currentScreen = Screen.ADMIN_UPDATE_ITEMS },
                            onStock = { currentScreen = Screen.ADMIN_STOCK },
                            onFeedback = { _, _ -> currentScreen = Screen.ADMIN_FEEDBACK },
                            onLogout = { showLogoutSheet = true },
                            location = adminLocation ?: "D2"
                        )

                        Screen.ADMIN_ADD_ITEM -> AddItemScreen(
                            onBack = { currentScreen = Screen.ADMIN_DASHBOARD },
                            onSave = { name, price, cookingTime, category, imageUri ->

                                confirmTitle = "Add Item?"
                                confirmMessage = "This will add item to menu."

                                onConfirmAction = {

                                    val db = FirebaseFirestore.getInstance()
                                    val storage = FirebaseStorage.getInstance()

                                    if (imageUri != null) {

                                        val imageRef = storage.reference
                                            .child("food_images/${System.currentTimeMillis()}.jpg")

                                        imageRef.putFile(imageUri)
                                            .addOnSuccessListener {

                                                imageRef.downloadUrl.addOnSuccessListener { uri: Uri ->

                                                    val item = hashMapOf(
                                                        "name" to name.trim(),
                                                        "price" to (price.toIntOrNull() ?: 0),
                                                        "category" to listOf(category),
                                                        "location" to (adminLocation ?: "D2"),
                                                        "cookingTime" to (cookingTime.toIntOrNull() ?: 5),
                                                        "isAvailable" to true,
                                                        "imageUrl" to uri.toString()
                                                    )

                                                    val docId = "${name.trim()}_${adminLocation ?: "D2"}"

                                                    db.collection("items")
                                                        .document(docId)
                                                        .set(item)
                                                        .addOnSuccessListener {
                                                            Toast.makeText(
                                                                this@MainActivity,
                                                                "Item added with image!",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                        .addOnFailureListener { e ->
                                                            Toast.makeText(
                                                                this@MainActivity,
                                                                "Failed: ${e.message}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                }
                                            }
                                    } else {
                                        // No image selected — save item with empty imageUrl
                                        val item = hashMapOf(
                                            "name" to name.trim(),
                                            "price" to (price.toIntOrNull() ?: 0),
                                            "category" to listOf(category),
                                            "location" to (adminLocation ?: "D2"),
                                            "cookingTime" to (cookingTime.toIntOrNull() ?: 5),
                                            "isAvailable" to true,
                                            "imageUrl" to ""
                                        )

                                        val docId = "${name.trim()}_${adminLocation ?: "D2"}"

                                        db.collection("items")
                                            .document(docId)
                                            .set(item)
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Item added successfully!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Failed: ${e.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }

                                    currentScreen = Screen.ADMIN_DASHBOARD   // ✅ MOVE HERE
                                }

                                showConfirmDialog = true   // 🔥 IMPORTANT
                            }
                        )

                        Screen.ADMIN_UPDATE_ITEMS -> UpdateItemsScreen(
                            items = firebaseItems.filter {
                                it.location == adminLocation
                            },   // 🔥 ADD THIS
                            onBack = { currentScreen = Screen.ADMIN_DASHBOARD },

                            onItemClick = { item ->   // 🔥 FIXED
                                selectedItem = item
                                currentScreen = Screen.ADMIN_ITEM_EDIT
                            },

                            onDelete = { id ->

                                confirmTitle = "Delete Item?"
                                confirmMessage = "This action cannot be undone."

                                onConfirmAction = {
                                    deleteItem(id)
                                }

                                showConfirmDialog = true
                            }
                        )

                        Screen.ADMIN_STOCK -> StockScreen {
                            currentScreen = Screen.ADMIN_DASHBOARD
                        }

                        Screen.ADMIN_FEEDBACK -> FeedbackScreen {
                            currentScreen = Screen.ADMIN_DASHBOARD
                        }

                        Screen.ADMIN_REPORTS -> ComingSoonScreen(
                            onBack = { currentScreen = Screen.ADMIN_DASHBOARD }
                        )

                        Screen.ORDER_TRACKING -> OrderTrackingScreen(
                            orderId = activeOrderId,
                            orderData = activeOrderData,
                            timerSeconds = trackingTimerSeconds,
                            timerStarted = trackingTimerStarted,
                            onTimerUpdate = { secs, started ->
                                trackingTimerSeconds = secs
                                trackingTimerStarted = started
                            },
                            onBack = { currentScreen = Screen.HOME },
                            onDismiss = {
                                activeOrderId = ""
                                activeOrderData = null
                                trackingTimerSeconds = 0
                                trackingTimerStarted = false
                                currentScreen = Screen.HOME
                            },
                            menuItems = firebaseItems
                        )

                        Screen.ORDER_HISTORY -> OrderHistoryScreen(
                            onBack = { currentScreen = Screen.PROFILE },
                            onOrderClick = { orderId, orderData ->
                                selectedOrderId = orderId
                                selectedOrderData = orderData
                                currentScreen = Screen.ORDER_DETAIL
                            }
                        )

                        Screen.ORDER_DETAIL -> OrderDetailScreen(
                            orderId = selectedOrderId,
                            orderData = selectedOrderData,
                            onBack = { currentScreen = Screen.ORDER_HISTORY }
                        )

                        Screen.ADMIN_ITEM_EDIT -> selectedItem?.let {

                            AdminItemEditScreen(
                                item = it,
                                onBack = { currentScreen = Screen.ADMIN_UPDATE_ITEMS },

                                onSave = { updatedItem ->

                                    confirmTitle = "Update Item?"
                                    confirmMessage = "Are you sure you want to save changes?"

                                    onConfirmAction = {

                                        val db = FirebaseFirestore.getInstance()

                                        db.collection("items")
                                            .document(updatedItem.id)
                                            .update(
                                                mapOf(
                                                    "name" to updatedItem.name,
                                                    "price" to updatedItem.price,
                                                    "category" to updatedItem.categories,
                                                    "cookingTime" to updatedItem.cookingTime,
                                                    "isAvailable" to updatedItem.isAvailable,
                                                    "imageUrl" to updatedItem.imageUrl
                                                )
                                            )


                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Item Updated",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        currentScreen = Screen.ADMIN_UPDATE_ITEMS   // ✅ AFTER CONFIRM
                                    }

                                    showConfirmDialog = true   // 🔥 IMPORTANT
                                }
                            )
                        }
                    }
                }

                // ✅ BOTTOM SHEET (INSIDE THEME - FULL VERSION)
                if (showRepeatSheet && pendingItem != null) {

                    pendingItem?.let { (food, qty, note) ->

                        ModalBottomSheet(
                            onDismissRequest = { showRepeatSheet = false },
                            containerColor = Color.White,
                            sheetState = rememberModalBottomSheetState(
                                skipPartiallyExpanded = true
                            ),
                            dragHandle = null   // 🔥 THIS REMOVES EXTRA SPACE
                        ){

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                            ){

                                //BOX DRAG
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 2.dp, bottom = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(48.dp)
                                            .height(5.dp)
                                            .background(Color.LightGray, RoundedCornerShape(50))
                                    )
                                }

                                // 🔥 TITLE
                                Text(
                                    text = if (isRepeatItem) "Already added to cart"
                                    else "Add this item to cart?",
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // 🔥 SUBTEXT
                                Text(
                                    text = if (isRepeatItem)
                                        "Do you want to increase quantity?"
                                    else
                                        "Do you want to proceed?",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // 🔥 ITEM NAME
                                Text(
                                    text = food.name,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                                    color = Color(0xFF1F2937)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // 🔥 BUTTONS
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {

                                    // CANCEL
                                    OutlinedButton(
                                        onClick = { showRepeatSheet = false },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(52.dp),
                                        shape = RoundedCornerShape(14.dp)
                                    ) {
                                        Text(
                                            "Cancel",
                                            color = Color.Gray,
                                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                                        )
                                    }

                                    // ADD MORE
                                    Button(
                                        onClick = {

                                            cartItems = addToCart(food, qty, note, cartItems)

                                            showRepeatSheet = false
                                            currentScreen = Screen.HOME
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(52.dp),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF1565C0)
                                        )
                                    ) {
                                        Text(
                                            if (isRepeatItem) "Add More" else "Add to Cart",
                                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                }

                if (showLogoutSheet) {

                    ModalBottomSheet(
                        onDismissRequest = { showLogoutSheet = false },
                        containerColor = Color.White,
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                        dragHandle = null
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {

                            Text(
                                "Logout?",
                                fontSize = 18.sp,
                                color = Color(0xFFD32F2F),
                                fontFamily = FontFamily(Font(R.font.montserrat_bold))
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Are you sure you want to logout?", color = Color.Gray)

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                                OutlinedButton(
                                    onClick = { showLogoutSheet = false },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancel")
                                }

                                Button(
                                    onClick = {
                                        FirebaseAuth.getInstance().signOut()
                                        showLogoutSheet = false
                                        currentScreen = Screen.LOGIN_PHONE
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFD32F2F) // 👈 red
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text("Logout", color = Color.White,
                                        fontFamily = FontFamily(Font(R.font.montserrat_bold)))
                                }
                            }
                        }
                    }
                }

                if (showConfirmDialog) {
                    ModalBottomSheet(
                        onDismissRequest = { showConfirmDialog = false },
                        containerColor = Color.White,
                        dragHandle = null,
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {

                            Text(
                                text = confirmTitle,
                                color = Color(0xFFD32F2F),
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold))
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(confirmMessage, color = Color.Gray)

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                                OutlinedButton(
                                    onClick = { showConfirmDialog = false },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancel")
                                }

                                Button(
                                    onClick = {
                                        onConfirmAction()
                                        showConfirmDialog = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFD32F2F)
                                    )
                                ) {
                                    Text("Confirm", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onFinish: () -> Unit) {

    var titleVisible by remember { mutableStateOf(false) }
    var captionVisible by remember { mutableStateOf(false) }

    val titleAlpha by animateFloatAsState(
        targetValue = if (titleVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1200)
    )

    val captionAlpha by animateFloatAsState(
        targetValue = if (captionVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1200)
    )

    LaunchedEffect(Unit) {
        @Suppress("UNUSED_VARIABLE")
        titleVisible = true
        delay(700)
        captionVisible = true
        delay(2000) // after animation
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        // 🔵 Background
        Image(
            painter = painterResource(id = R.drawable.bg_blue_pattern),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🔤 Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "BVRIT",
                fontSize = 42.sp,
                letterSpacing = 2.sp,
                color = Color.White,
                modifier = Modifier.alpha(titleAlpha),
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Smart Canteen",
                fontSize = 20.sp,
                color = Color.White.copy(alpha = 0.85f),   // 🔥 CHANGED
                modifier = Modifier.alpha(captionAlpha),
                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
            )
        }
    }
}

@Composable
fun LocationScreen(
    onD2Click: () -> Unit,
    onFCClick: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        // ✅ Background (KEEP THIS)
        Image(
            painter = painterResource(R.drawable.bg_white_pattern),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🔥 VERY LIGHT OVERLAY (fix visibility, NOT dull)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.05f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.75f)) // glass effect
                    .padding(20.dp)
            ) {

                Column {

                    Text(
                        text = "Welcome to BVRIT Canteen 👋",
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Choose your dining location",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OptionCard("Daffodils 2 (D2)", onClick = onD2Click)

                    Spacer(modifier = Modifier.height(20.dp))

                    OptionCard("Food Court (FC)", onClick = onFCClick)
                }
            }
        }
    }
}

@Composable
fun OptionCard(title: String, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(20.dp))
            .shadow(6.dp, RoundedCornerShape(20.dp)) // 🔥 added
            .clickable { onClick() }
    ) {

        Image(
            painter = painterResource(R.drawable.bg_blue_pattern),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f)) // 🔥 improved
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
        }
    }
}

@Composable
fun ModeScreen(onOptionClick: (String) -> Unit, onBack: () -> Unit) {

    BackHandler {
        onBack()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ✅ Background
        Image(
            painter = painterResource(R.drawable.bg_white_pattern),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🔥 LIGHT OVERLAY (same as location)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.05f))
        )

        // 🔙 Back button (UNCHANGED)
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(WindowInsets.systemBars.asPaddingValues())
                .padding(start = 8.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.75f)) // same UI
                    .padding(20.dp)
            ) {

                Column {

                    Text(
                        text = "How would you like to order?",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    OptionCard("Dine In", onClick = { onOptionClick("Dine In") })

                    Spacer(modifier = Modifier.height(20.dp))

                    OptionCard("Take Away", onClick = { onOptionClick("Take Away") })
                }
            }
        }
    }
}

@Composable
fun ComingSoonScreen(onBack: () -> Unit) {

    BackHandler {
        onBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F4F7))
    ) {

        // 🔙 Back Button (better placement)
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    WindowInsets.systemBars.asPaddingValues()
                )
                .padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    WindowInsets.systemBars.asPaddingValues()
                )
                .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🍔 Animated burger
            val infiniteTransition = rememberInfiniteTransition()
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -20f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Text(
                text = "🍔",
                fontSize = 64.sp,
                modifier = Modifier.offset(y = offsetY.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Smart Canteen is Cooking Something...",
                fontSize = 20.sp, // slightly reduced
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "We’re setting things up for you.\nIt’ll be ready very soon ⏳",
                fontSize = 15.sp,
                color = Color(0xFF6E6E6E),
                fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun HomeScreen(
    menu: List<FoodItem>,
    onBack: () -> Unit,
    onItemClick: (FoodItem) -> Unit,
    onCartClick: () -> Unit,
    cartCount: Int,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    selectedLocation: String,
    selectedMode: String,
    activeOrderId: String = "",
    activeOrderData: Map<String, Any>? = null,
    onTrackOrder: () -> Unit = {}
) {
    BackHandler { onBack() }

    var selectedCategory by remember { mutableStateOf("All") }
    val currentTime = remember { mutableStateOf(Calendar.getInstance()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60000)
            currentTime.value = Calendar.getInstance()
        }
    }

    val hour = currentTime.value.get(Calendar.HOUR_OF_DAY)
    val baseCategories = menu.flatMap { it.categories }.distinct()

    val orderedCategories = when {
        baseCategories.contains("Tiffins") -> {
            when {
                hour < 11 -> listOf("Tiffins", "Meals", "Biryani", "Chinese", "Rolls", "Snacks", "Drinks")
                hour < 16 -> listOf("Meals", "Biryani", "Chinese", "Snacks", "Rolls", "Drinks", "Tiffins")
                else -> listOf("Snacks", "Rolls", "Biryani", "Chinese", "Meals", "Drinks", "Tiffins")
            }.filter { baseCategories.contains(it) }
        }
        else -> baseCategories.filter { it !in listOf("Veg", "NonVeg", "Egg") }
    }

    var searchQuery by remember { mutableStateOf("") }

    val prioritizedMenu = menu
        .filter { it.isAvailable }
        .sortedBy { item ->
            when (hour) {
                in 7..11 -> when {
                    item.categories.contains("Tiffins") -> 0
                    item.categories.contains("Snacks") -> 1
                    item.categories.contains("Drinks") -> 2
                    else -> 3
                }
                in 12..15 -> when {
                    item.categories.contains("Meals") -> 0
                    item.categories.contains("Biryani") -> 1
                    item.categories.contains("Chinese") -> 2
                    else -> 3
                }
                in 16..18 -> when {
                    item.categories.contains("Snacks") -> 0
                    item.categories.contains("Chinese") -> 1
                    item.categories.contains("Rolls") -> 2
                    else -> 3
                }
                in 19..22 -> when {
                    item.categories.contains("Meals") -> 0
                    item.categories.contains("Biryani") -> 1
                    item.categories.contains("Tiffins") -> 2
                    else -> 3
                }
                else -> 4
            }
        }
        .filter { it.name.contains(searchQuery, ignoreCase = true) }
        .distinctBy { it.id }

    // Banner animation — must be at composable scope, never inside loops
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        )
    )

    // OUTER BOX — lets banner float over everything
    Box(modifier = Modifier.fillMaxSize()) {

        // STICKY HEADER + SCROLLABLE LIST in a Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues())
        ) {

            // ═══════════════════════════════════
            // 🔒 STICKY HEADER (never scrolls)
            // ═══════════════════════════════════
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                // Top bar row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Column {
                            Text(
                                text = "BVRIT",
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold))
                            )
                            Text(
                                text = "Smart Canteen",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        }
                    }
                    Row {
                        IconButton(onClick = onCartClick) {
                            Box {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 6.dp, y = (-6).dp)
                                        .size(16.dp)
                                        .background(Color.Red, RoundedCornerShape(50)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "$cartCount", fontSize = 9.sp, color = Color.White)
                                }
                            }
                        }
                        IconButton(onClick = onProfileClick) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                        }
                        IconButton(onClick = onLogoutClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = Color(0xFFD32F2F)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Location + Mode row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📍 $selectedLocation",
                        fontSize = 13.sp,
                        color = Color(0xFF1565C0),
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                    Text(text = "•", fontSize = 13.sp, color = Color.Gray)
                    Text(
                        text = "🪑 $selectedMode",
                        fontSize = 13.sp,
                        color = Color(0xFF2E7D32),
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "What do you want to eat?",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Search bar
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search for food...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clip(RoundedCornerShape(25.dp)),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xFF1565C0)
                    ),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category chips
                val categories = listOf("All") + orderedCategories
                LazyRow {
                    items(categories) { category ->
                        Box(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .shadow(
                                    if (selectedCategory == category) 4.dp else 0.dp,
                                    RoundedCornerShape(20.dp)
                                )
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (selectedCategory == category) Color(0xFF1565C0)
                                    else Color.White
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (selectedCategory == category) Color.Transparent
                                    else Color.LightGray,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = category,
                                color = if (selectedCategory == category) Color.White
                                else Color.DarkGray,
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
            // ═══════════════════════════════════
            // END STICKY HEADER
            // ═══════════════════════════════════

            // ═══════════════════════════════════
            // 🔄 SCROLLABLE FOOD LIST
            // ═══════════════════════════════════
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                val categoriesToShow =
                    if (selectedCategory == "All") {
                        orderedCategories.filter { category ->
                            prioritizedMenu.any { it.categories.contains(category) }
                        }
                    } else {
                        listOf(selectedCategory)
                    }

                categoriesToShow.forEach { category ->
                    val shownItems = mutableSetOf<String>()
                    val categoryItems = prioritizedMenu.filter {
                        it.categories.contains(category) && !shownItems.contains(it.name)
                    }.onEach { shownItems.add(it.name) }

                    if (categoryItems.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = category,
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold))
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        items(categoryItems.chunked(2)) { rowItems ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                rowItems.forEach { food ->
                                    FoodCard(
                                        item = food,
                                        modifier = Modifier.weight(1f),
                                        onClick = { onItemClick(food) }
                                    )
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                // Bottom padding so banner doesn't cover last item
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
            // ═══════════════════════════════════
            // END SCROLLABLE LIST
            // ═══════════════════════════════════

        } // ← Column closes here

        // ═══════════════════════════════════
        // 🔔 FLOATING BANNER
        // Outside Column, inside Box — correct placement
        // ═══════════════════════════════════
        if (activeOrderId.isNotEmpty() && activeOrderData != null) {

            val status = activeOrderData["status"] as? String ?: "Placed"
            val total = (activeOrderData["total"] as? Long)?.toInt() ?: 0

            val bannerColor = when (status) {
                "Ready" -> Color(0xFF2E7D32)
                "Preparing" -> Color(0xFF1565C0)
                else -> Color(0xFF37474F)
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .zIndex(10f)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(pulseAlpha)
                        .clickable { onTrackOrder() },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(containerColor = bannerColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = when (status) {
                                    "Ready" -> "✅"
                                    "Preparing" -> "🍳"
                                    else -> "🕐"
                                },
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = when (status) {
                                        "Ready" -> "Your order is Ready! 🎉"
                                        "Preparing" -> "Preparing your order..."
                                        else -> "Order Placed!"
                                    },
                                    color = Color.White,
                                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "₹$total  •  Tap to track",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                                )
                            }
                        }
                        if (status == "Ready") {
                            IconButton(
                                onClick = { onTrackOrder() },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        // ═══════════════════════════════════
        // END FLOATING BANNER
        // ═══════════════════════════════════

    } // ← outer Box closes here
}


@Composable
fun FoodCard(
    item: FoodItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onClick() }
            .animateContentSize()
            .padding(8.dp)
    ) {

        if (item.imageUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.imageUrl)
                    .crossfade(true)
                    .size(400)
                    .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                    .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        } else {
            Image(
                painter = painterResource(R.drawable.food_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🟢🔴🟡 Veg/NonVeg/Egg indicator
        val dotColor = when {
            item.categories.contains("NonVeg") -> Color(0xFFD32F2F)
            item.categories.contains("Egg") -> Color(0xFFFFA000)
            else -> Color(0xFF2E7D32)
        }

        val dotLabel = when {
            item.categories.contains("NonVeg") -> "Non-Veg"
            item.categories.contains("Egg") -> "Egg"
            else -> "Veg"
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .border(1.dp, dotColor, RoundedCornerShape(2.dp))
                    .padding(2.dp)
                    .background(dotColor, RoundedCornerShape(1.dp))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                dotLabel,
                fontSize = 10.sp,
                color = dotColor,
                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = item.name,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
        )

        Spacer(modifier = Modifier.height(4.dp)) //

        Text(
            text = "₹${item.price}",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)) //
        )

        Spacer(modifier = Modifier.height(4.dp))

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = "⏱ ${item.cookingTime} mins",
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Text(
                    text = "⭐ ${item.rating}",
                    fontSize = 11.sp,
                    color = Color(0xFFFFA000)
                )
            }

            Button(
                onClick = onClick,
                modifier = Modifier.height(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1565C0)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Add",
                    color = Color.White,   // 🔥 important
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                )
            }
        }
    }
}


@Composable
fun ItemScreen(
    item: FoodItem,
    onBack: () -> Unit,
    onAddToCart: (FoodItem, Int, String) -> Unit
) {

    var quantity by remember { mutableIntStateOf(1) }

    var note by remember { mutableStateOf("") }

    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF9F9F9))   // light gray background
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {

        // 🔙 Back Button (clean positioning)
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🍔 Food Image
        if (item.imageUrl.isNotEmpty()) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.imageUrl)
                    .crossfade(true)
                    .size(600)
                    .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                    .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        } else {
            Image(
                painter = painterResource(R.drawable.food_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📝 Item Name
        // 📝 Item Name
        Text(
            text = item.name,
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Text(
            text = "₹${item.price}",
            fontSize = 20.sp,
            color = Color(0xFF2E7D32), // green = price feel
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 📝 NOTE INPUT (ADD THIS HERE)
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            placeholder = {
                Text(
                    "Add note (less spicy, no onion...)",
                    color = Color.Gray
                )
            },
            textStyle = TextStyle(
                color = Color.Black,   // 🔥 FIX
                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFB8C00),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                cursorColor = Color(0xFF1565C0)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // ⭐ Extra Info (makes it feel real app)
        Text(
            text = "⏱ ${item.cookingTime} mins • ⭐ ${item.rating}",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔢 Quantity Selector (IMPROVED)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = { if (quantity > 1) quantity-- }
            ) {
                Text("-", fontSize = 18.sp)
            }

            Text(
                text = "$quantity",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )

            Button(
                onClick = { quantity++ }
            ) {
                Text("+", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🛒 Add to Cart Button
        Button(
            onClick = {
                onAddToCart(item, quantity, note)
                note = ""   // reset
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1565C0),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Add to Cart • ₹${item.price * quantity}",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
        }
    }
}

@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    onBack: () -> Unit,
    onUpdateCart: (List<CartItem>) -> Unit,
    onOrderSuccess: (String) -> Unit,
    selectedLocation: String,
    selectedMode: String,
    userPhone: String
) {

    BackHandler { onBack() }

    val total = cartItems.sumOf { it.food.price * it.quantity }

    fun saveOrderToFirebase(
        orderId: String,
        cartItems: List<CartItem>,
        total: Int,
        selectedLocation: String,
        selectedMode: String,
        userPhone: String,
        paymentMethod: String
    ) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val itemsList = cartItems.map { cartItem ->
            hashMapOf(
                "name" to cartItem.food.name,
                "price" to cartItem.food.price,
                "quantity" to cartItem.quantity,
                "note" to cartItem.note
            )
        }

        val order = hashMapOf(
            "orderId" to orderId,
            "userId" to (auth.currentUser?.uid ?: "guest"),
            "phoneNumber" to userPhone,
            "location" to selectedLocation,
            "mode" to selectedMode,
            "items" to itemsList,
            "total" to total,
            "paymentMethod" to paymentMethod,
            "timestamp" to com.google.firebase.Timestamp.now(),
            "status" to "Placed"
        )

        db.collection("orders")
            .document(orderId)
            .set(order)
            .addOnSuccessListener {
                android.util.Log.d("FIREBASE", "✅ Order saved: $orderId")
            }
            .addOnFailureListener { e ->
                android.util.Log.e("FIREBASE", "❌ Order failed: ${e.message}")
            }

        // Update cooking time per item based on quantity
        cartItems.forEach { cartItem ->
            if (cartItem.food.id.isNotEmpty()) {
                val newTime = cartItem.food.cookingTime + (cartItem.quantity * 2)
                db.collection("items")
                    .document(cartItem.food.id)
                    .update("cookingTime", newTime)
            }
        }
    }

    var selectedPayment by remember { mutableStateOf<String?>(null) }

    var upiId by remember { mutableStateOf("") }

    val methods = listOf(
        "PhonePe",
        "Google Pay",
        "Pay via UPI Apps",
        "Enter UPI ID",
        "Pay at Counter"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {

        // 🔙 HEADER
        item {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Your Order",
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )

            Text(
                text = "📍 $selectedLocation",
                fontSize = 13.sp,
                color = Color(0xFF1565C0),
                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 🛒 EMPTY STATE
        if (cartItems.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🛒", fontSize = 40.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        "Your cart is empty",
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                }
            }
        }

        // 🧾 ITEMS
        itemsIndexed(cartItems) { index, cartItem ->

            val item = cartItem.food
            val qty = cartItem.quantity

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (item.imageUrl.isNotEmpty()) {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl)
                            .crossfade(true)
                            .size(300)
                            .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                            .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(70.dp)   // ✅ FIXED LINE
                            .clip(RoundedCornerShape(10.dp))
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.food_placeholder),
                        contentDescription = null,
                        modifier = Modifier.size(70.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = item.name,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "₹${item.price}",
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )

                    // 🔥 ADD THIS PART (7D)

                    if (cartItem.note.isNotEmpty()) {
                        Text(
                            text = "Note: ${cartItem.note}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Text(
                        text = "⏱ ${cartItem.cookingTime} mins",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Button(
                        onClick = {
                            val updated = cartItems.toMutableList()

                            if (qty > 1) {
                                updated[index] = cartItem.copy(quantity = qty - 1)
                            } else {
                                updated.removeAt(index)
                            }

                            onUpdateCart(updated)
                        }
                    ) {
                        Text("-")
                    }

                    Text(
                        text = "$qty",
                        modifier = Modifier.padding(horizontal = 10.dp),
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Button(
                        onClick = {
                            val updated = cartItems.toMutableList()
                            updated[index] = cartItem.copy(quantity = qty + 1)
                            onUpdateCart(updated)
                        }
                    ) {
                        Text("+")
                    }
                }
            }
        }

        // 💰 TOTAL
        item {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Total: ₹$total",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        // 💳 PAYMENT SECTION
        if (cartItems.isNotEmpty()) {

            item {

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Choose Payment Method",
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                )

                Spacer(modifier = Modifier.height(10.dp))

                methods.forEach { method ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (selectedPayment == method) Color(0xFFE3F2FD)
                                else Color.White
                            )
                            .border(
                                1.dp,
                                if (selectedPayment == method) Color(0xFF1565C0) else Color.LightGray,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedPayment = method }
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            val icon = when (method) {
                                "PhonePe" -> R.drawable.ic_phonepe
                                "Google Pay" -> R.drawable.ic_gpay
                                "Pay via UPI Apps" -> R.drawable.ic_upi
                                "Enter UPI ID" -> R.drawable.ic_bhim
                                else -> R.drawable.ic_cash
                            }

                            Image(
                                painter = painterResource(id = icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = method,
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        }
                    }
                }

                if (selectedPayment == "Enter UPI ID") {

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = upiId,
                        onValueChange = { upiId = it },
                        placeholder = { Text("Enter UPI ID (example@upi)") },

                        textStyle = TextStyle(
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        ),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1565C0),
                            unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color(0xFF1565C0)
                        ),

                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .padding(vertical = 12.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "+",
                            fontSize = 20.sp,
                            color = Color(0xFF1565C0),
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Add Payment Method",
                            fontSize = 14.sp,
                            color = Color(0xFF1565C0),
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // 🚀 BUTTON
        item {

            val context = LocalContext.current

            Button(
                onClick = {

                    if (selectedPayment == null) {
                        Toast.makeText(context, "Please select a payment method", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    when (selectedPayment) {

                        "PhonePe" -> {
                            val id = generateOrderId(selectedLocation, total)
                            saveOrderToFirebase(id, cartItems, total, selectedLocation, selectedMode, userPhone, "PhonePe")
                            payWithUpi(context, total, "aashrithnapa@fam", "com.phonepe.app")
                            Toast.makeText(context, "Complete payment in PhonePe. Order ID: $id", Toast.LENGTH_LONG).show()
                            onOrderSuccess(id)
                        }

                        "Google Pay" -> {
                            val id = generateOrderId(selectedLocation, total)
                            saveOrderToFirebase(id, cartItems, total, selectedLocation, selectedMode, userPhone, "Google Pay")
                            payWithUpi(context, total, "aashrithnapa@fam", "com.google.android.apps.nbu.paisa.user")
                            Toast.makeText(context, "Complete payment in Google Pay. Order ID: $id", Toast.LENGTH_LONG).show()
                            onOrderSuccess(id)
                        }

                        "Pay via UPI Apps" -> {
                            val id = generateOrderId(selectedLocation, total)
                            saveOrderToFirebase(id, cartItems, total, selectedLocation, selectedMode, userPhone, "UPI Apps")
                            payWithUpi(context, total, "aashrithnapa@fam", null)
                            Toast.makeText(context, "Complete payment in UPI app. Order ID: $id", Toast.LENGTH_LONG).show()
                            onOrderSuccess(id)
                        }

                        "Enter UPI ID" -> {
                            if (upiId.isBlank()) {
                                Toast.makeText(context, "Enter UPI ID", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val id = generateOrderId(selectedLocation, total)
                            saveOrderToFirebase(id, cartItems, total, selectedLocation, selectedMode, userPhone, "UPI - $upiId")
                            payWithUpi(context, total, upiId, null)
                            Toast.makeText(context, "Complete payment in UPI app. Order ID: $id", Toast.LENGTH_LONG).show()
                            onOrderSuccess(id)
                        }

                        "Pay at Counter" -> {
                            val id = generateOrderId(selectedLocation, total)
                            saveOrderToFirebase(id, cartItems, total, selectedLocation, selectedMode, userPhone, "Pay at Counter")
                            onOrderSuccess(id)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1565C0)
                )
            ) {
                Text(
                    text = "Place Order",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun OrderSuccessScreen(
    orderId: String,
    total: Int,
    location: String,
    onClose: () -> Unit
) {
    BackHandler { onClose() }  // 👈 add this

    var showTick by remember { mutableStateOf(false) }
    var showOrderId by remember { mutableStateOf(false) }
    var showQr by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }
    var showPreparing by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        delay(300)
        showTick = true
        delay(800)
        showOrderId = true
        delay(600)
        showQr = true
        delay(600)
        showPreparing = true
        delay(400)
        showButtons = true
    }

    // Pulsing animation for preparing text
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(20.dp)
    ) {
        // ✅ CLOSE BUTTON TOP RIGHT
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .zIndex(10f)  // 👈 add this
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                tint = Color(0xFF2E7D32)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // ✅ TICK
            AnimatedVisibility(
                visible = showTick,
                enter = scaleIn(
                    initialScale = 0.3f,
                    animationSpec = tween(600)
                ) + fadeIn(tween(600))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(110.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Order Confirmed! 🎉",
                        color = Color(0xFF2E7D32),
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🆔 ORDER ID
            AnimatedVisibility(
                visible = showOrderId,
                enter = slideInVertically { it / 2 } + fadeIn(tween(500))
            ) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Order ID",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = orderId,
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                            color = Color(0xFF1565C0)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📍", fontSize = 18.sp)
                                Text(location, fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.montserrat_semibold)))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("💰", fontSize = 18.sp)
                                Text("₹$total", fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                    color = Color(0xFF2E7D32))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 📱 QR CODE
            AnimatedVisibility(
                visible = showQr,
                enter = scaleIn(initialScale = 0.7f, animationSpec = tween(500)) + fadeIn()
            ) {
                val fixedQrData = remember(orderId) {
                    "ORDER_ID:$orderId|LOCATION:$location|TOTAL:$total"
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Show this at counter",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        bitmap = generateQrCode(fixedQrData),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shadow(6.dp, RoundedCornerShape(16.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ⏳ PULSING PREPARING
            AnimatedVisibility(
                visible = showPreparing,
                enter = fadeIn(tween(600))
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1565C0).copy(alpha = 0.1f))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        "⏳ Preparing your order...",
                        color = Color(0xFF1565C0),
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                        modifier = Modifier.alpha(pulseAlpha)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔘 BUTTONS
            AnimatedVisibility(
                visible = showButtons,
                enter = slideInVertically { it / 2 } + fadeIn(tween(500))
            ) {
                val context = LocalContext.current

                Column(modifier = Modifier.fillMaxWidth()) {

                    OutlinedButton(
                        onClick = {
                            val shareText = """
                                BVRIT Smart Canteen 🍽️
                                
                                Order ID: $orderId
                                Location: $location
                                Total: ₹$total
                                
                                Status: Confirmed ✅
                            """.trimIndent()

                            // Save QR bitmap to cache and share as image
                            val qrData = "ORDER_ID:$orderId|LOCATION:$location|TOTAL:$total"
                            val qrBitmap = android.graphics.Bitmap.createBitmap(512, 512, android.graphics.Bitmap.Config.RGB_565)
                            val writer = com.google.zxing.qrcode.QRCodeWriter()
                            val bitMatrix = writer.encode(qrData, com.google.zxing.BarcodeFormat.QR_CODE, 512, 512)
                            for (x in 0 until 512) {
                                for (y in 0 until 512) {
                                    qrBitmap.setPixel(x, y,
                                        if (bitMatrix[x, y]) android.graphics.Color.BLACK
                                        else android.graphics.Color.WHITE
                                    )
                                }
                            }

                            val cachePath = java.io.File(context.cacheDir, "images").also { it.mkdirs() }
                            val file = java.io.File(cachePath, "order_qr.png")
                            file.outputStream().use { qrBitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it) }

                            val fileUri = androidx.core.content.FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                file
                            )

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "image/png"
                                putExtra(Intent.EXTRA_STREAM, fileUri)
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share Order"))
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFF1565C0))
                    ) {
                        Text(
                            "Share Ticket",
                            color = Color(0xFF1565C0),
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onClose,
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32)
                        )
                    ) {
                        Text(
                            "Back to Home",
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogoutClick: () -> Unit,
    phoneNumber: String = "",
    onOrderHistoryClick: () -> Unit = {}
) {
    BackHandler { onBack() }

    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var name by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Load from Firebase
    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        name = doc.getString("name") ?: ""
                        rollNumber = doc.getString("rollNumber") ?: ""
                    } else {
                        // Document doesn't exist — create it now
                        val phone = FirebaseAuth.getInstance().currentUser?.phoneNumber ?: ""
                        val newUser = hashMapOf(
                            "uid" to uid,
                            "phone" to phone,
                            "name" to "",
                            "rollNumber" to "",
                            "email" to "",
                            "createdAt" to com.google.firebase.Timestamp.now()
                        )
                        db.collection("users").document(uid).set(newUser)
                    }
                    isLoading = false
                }
                .addOnFailureListener {
                    isLoading = false
                }
        } else {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF5F9FF))
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // PROFILE CARD
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(90.dp),
                    tint = Color(0xFF1565C0)
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isEditing) {

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = rollNumber,
                        onValueChange = { rollNumber = it },
                        label = { Text("Roll Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            db.collection("users").document(uid)
                                .update(mapOf(
                                    "name" to name,
                                    "rollNumber" to rollNumber
                                ))
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()
                                    isEditing = false
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                    ) {
                        Text("Save", color = Color.White,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)))
                    }

                } else {

                    Text(
                        text = if (name.isEmpty()) "Tap Edit to add name" else name,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = phoneNumber.ifEmpty { "No phone" },
                        color = Color.Gray, fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (rollNumber.isEmpty()) "Roll No: Not set" else "Roll No: $rollNumber",
                        color = Color.Gray, fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { isEditing = true },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Edit Profile",
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold)))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // OPTIONS
        val options = listOf("Order History", "Payment Methods", "Feedback", "Settings")

        options.forEach { option ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        if (option == "Order History") onOrderHistoryClick()
                    },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White) // 👈 Add this
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (option) {
                            "Order History" -> Icons.Default.History
                            "Payment Methods" -> Icons.Default.AccountBalanceWallet
                            "Feedback" -> Icons.Default.Feedback
                            else -> Icons.Default.Settings
                        },
                        contentDescription = null,
                        tint = Color(0xFF1565C0)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(option, fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold)))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text("Logout", color = Color.White,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)))
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PhoneLoginScreen(
    isLoading: Boolean,
    onSendOtp: (String) -> Unit,
    onBack: () -> Unit,
    onStaffClick: () -> Unit,     // ✅ ADD
    onAdminClick: () -> Unit      // ✅ ADD
) {

    var phone by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    BackHandler { onBack() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🔵 BACKGROUND
        Image(
            painter = painterResource(R.drawable.bg_blue_pattern),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
        ) {

            // 🔥 TOP WELCOME SECTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 20.dp, end = 20.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color(0xFFE2E8F0),
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Welcome 👋",
                    fontSize = 26.sp,
                    color = Color(0xFFF8FAFC),
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )

                Text(
                    "Select your role to continue",
                    color = Color(0xFFF8FAFC),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ⚪ WHITE CARD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {

                Column {

                    Text(
                        text = "Login",
                        fontSize = 28.sp,
                        color = Color(0xFF111827),
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Enter your mobile number",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 📱 PHONE FIELD
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            "+91",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = phone,
                            onValueChange = { if (it.length <= 10 && !it.contains(" ")) phone = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = {
                                Text(
                                    "Enter mobile number",
                                    color = Color(0xFF9CA3AF),
                                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                                )
                            },
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color(0xFF16A34A)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 🔥 SEND OTP BUTTON
                    Button(
                        onClick = {
                            if (phone.length == 10 && !isLoading) {
                                onSendOtp(phone)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF16A34A)
                        )
                    ) {

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            if (isLoading) "Sending..." else "Send OTP",
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 🔘 REMEMBER + HELP
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF16A34A)
                                )
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                "Remember me",
                                fontSize = 13.sp,
                                color = Color(0xFF6B7280),
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                    Spacer(modifier = Modifier.height(50.dp))

                    // 🔥 ROLE BUTTONS
                    OutlinedButton(
                        onClick = { onStaffClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFDC2626))
                    ) {
                        Text(
                            "Login as Canteen Staff",
                            color = Color(0xFF2563EB),  // 🔵 blue
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        )

                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    OutlinedButton(
                        onClick = { onAdminClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFDC2626))   // 🔴 red border
                    ) {
                        Text(
                            "Login as Admin",
                            color = Color(0xFFDC2626),   // 🔴 red text
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OtpScreen(
    onVerify: (String) -> Unit,
    onBack: () -> Unit,
    phoneNumber: String = "",
    onResendOtp: () -> Unit = {} // 👈 add this
) {

    var otp by remember { mutableStateOf("") }

    BackHandler { onBack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(WindowInsets.systemBars.asPaddingValues())
                .padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // 🔵 Background
        Image(
            painter = painterResource(R.drawable.bg_blue_pattern),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 💳 Glass Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)   // 🔥 THIS FIXES POSITION
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {

                var timeLeft by remember { mutableIntStateOf(60) }
                var canResend by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    while (timeLeft > 0) {
                        delay(1000)
                        timeLeft--
                    }
                    canResend = true
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Verify OTP",
                        fontSize = 28.sp,
                        color = Color(0xFF111827),
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Code sent to $phoneNumber",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        value = otp,
                        onValueChange = {
                            if (it.length <= 6 && !it.contains(" ")) otp = it
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp),
                        placeholder = {
                            Text(
                                "Enter OTP",
                                color = Color(0xFF9CA3AF),
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        },
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // ⏱ TIMER / RESEND
                    if (!canResend) {
                        Text(
                            text = "Resend OTP in ${timeLeft}s",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        )
                    } else {
                        TextButton(onClick = {
                            timeLeft = 60
                            canResend = false
                            onResendOtp()  // ✅ this
                        }) {
                            Text(
                                text = "Resend OTP",
                                color = Color(0xFF16A34A),
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (otp.length == 6) {
                                onVerify(otp)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF16A34A)
                        )
                    ) {
                        Text(
                            text = "Confirm",
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StaffLoginScreen(
    onLoginSuccess: (String) -> Unit,
    onBack: () -> Unit
) {

    var id by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    BackHandler { onBack() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🔵 Background
        Image(
            painter = painterResource(R.drawable.bg_blue_pattern),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔲 Card UI (same style as student)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        "Staff Login",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        fontSize = 20.sp,
                        color = Color(0xFF1F2937)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AppTextField(
                        value = id,
                        onValueChange = { id = it.trimStart() },   // ← change this
                        placeholder = "Enter Staff ID"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },

                        placeholder = {
                            Text(
                                "Enter Access Code",
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        },

                        textStyle = TextStyle(
                            color = Color(0xFF1F2937),
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        ),

                        visualTransformation =
                            if (passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),

                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector =
                                        if (passwordVisible) Icons.Default.Visibility
                                        else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password"
                                )
                            }
                        },

                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF9FAFB),
                            unfocusedContainerColor = Color(0xFFF9FAFB),
                            focusedBorderColor = Color(0xFF1565C0),
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            cursorColor = Color(0xFF1565C0)
                        ),

                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val trimmedId = id.trim()
                            val trimmedPassword = password.trim()
                            val account = staffAccounts[trimmedId]
                            if (account != null && account.first == trimmedPassword) {
                                onLoginSuccess(account.second)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Invalid Credentials",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            "Login",
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AdminLoginScreen(
    onLoginSuccess: (String) -> Unit,
    onBack: () -> Unit
) {

    var id by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current


    BackHandler { onBack() }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.bg_blue_pattern),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        "Admin Login",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AppTextField(
                        value = id,
                        onValueChange = { id = it.trimStart() },   // ← change this
                        placeholder = "Enter Admin ID"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },

                        placeholder = {
                            Text(
                                "Enter Password",
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        },

                        textStyle = TextStyle(
                            color = Color(0xFF1F2937),
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        ),

                        visualTransformation =
                            if (passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),

                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector =
                                        if (passwordVisible) Icons.Default.Visibility
                                        else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password"
                                )
                            }
                        },

                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF9FAFB),
                            unfocusedContainerColor = Color(0xFFF9FAFB),
                            focusedBorderColor = Color(0xFF1565C0),
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            cursorColor = Color(0xFF1565C0)
                        ),

                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val trimmedId = id.trim()
                            val trimmedPassword = password.trim()
                            val admin = admins.find {
                                it.id == trimmedId && it.password == trimmedPassword
                            }

                            if (admin != null) {
                                onLoginSuccess(admin.location)
                            } else {
                                Toast.makeText(context, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show()
                            }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            "Login",
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,

        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
            )
        },

        textStyle = TextStyle(
            color = Color(0xFF1F2937),   // 🔥 proper dark text
            fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
            fontSize = 15.sp
        ),

        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),

        shape = RoundedCornerShape(14.dp),

        colors = OutlinedTextFieldDefaults.colors(

            // 🔥 BACKGROUND
            focusedContainerColor = Color(0xFFF9FAFB),
            unfocusedContainerColor = Color(0xFFF9FAFB),

            // 🔥 BORDER
            focusedBorderColor = Color(0xFF1565C0),
            unfocusedBorderColor = Color(0xFFE0E0E0),

            // 🔥 TEXT COLORS (IMPORTANT FIX)
            focusedTextColor = Color(0xFF1F2937),
            unfocusedTextColor = Color(0xFF1F2937),

            // 🔥 CURSOR
            cursorColor = Color(0xFF1565C0)
        ),

        singleLine = true
    )
}

@Composable
fun StaffHomeScreen(
    menu: List<FoodItem>,
    onItemClick: (FoodItem) -> Unit,
    onBack: () -> Unit,
    onLogoutClick: () -> Unit,
    selectedLocation: String,
    onFeedbackClick: () -> Unit = {} // 👈 add this
) {
    BackHandler { onBack() }

    val availableCount = menu.count { it.isAvailable }
    val unavailableCount = menu.count { !it.isAvailable }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {
        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Staff Panel",
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
                Text(
                    "📍 $selectedLocation",
                    fontSize = 13.sp,
                    color = Color(0xFF1565C0),
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                )
            }
            IconButton(onClick = onLogoutClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color(0xFFD32F2F)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // STATS ROW
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2E7D32).copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "$availableCount",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        "Available",
                        fontSize = 12.sp,
                        color = Color(0xFF2E7D32),
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD32F2F).copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "$unavailableCount",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        color = Color(0xFFD32F2F)
                    )
                    Text(
                        "Unavailable",
                        fontSize = 12.sp,
                        color = Color(0xFFD32F2F),
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                }
            }
        }

        // 🔥 FEEDBACK BUTTON FOR STAFF
        val context = LocalContext.current

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 ACTIVE ORDERS SECTION
        var activeOrders by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

        LaunchedEffect(selectedLocation) {
            val db = FirebaseFirestore.getInstance()
            db.collection("orders")
                .whereEqualTo("location", selectedLocation)
                .whereIn("status", listOf("Placed", "Preparing"))
                .addSnapshotListener { snapshot, _ ->
                    activeOrders = snapshot?.documents?.mapNotNull { doc ->
                        doc.data?.toMutableMap()?.also { it["docId"] = doc.id }
                    } ?: emptyList()
                }
        }

        if (activeOrders.isNotEmpty()) {

            Text(
                "Active Orders (${activeOrders.size})",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                color = Color(0xFFD32F2F)
            )

            Spacer(modifier = Modifier.height(8.dp))

            activeOrders.forEach { order ->

                val docId = order["docId"] as? String ?: ""
                val orderId = order["orderId"] as? String ?: ""
                val status = order["status"] as? String ?: "Placed"
                val total = (order["total"] as? Long)?.toInt() ?: 0
                val db = FirebaseFirestore.getInstance()

                Card(
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (status) {
                            "Placed" -> Color(0xFFFFF3E0)
                            "Preparing" -> Color(0xFFE3F2FD)
                            else -> Color.White
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                orderId.takeLast(14),
                                fontSize = 13.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                            Text(
                                "₹$total",
                                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                color = Color(0xFF2E7D32)
                            )
                        }

                        // 📞 User phone
                        val phone = order["phoneNumber"] as? String ?: ""
                        if (phone.isNotEmpty()) {
                            Text(
                                "📞 $phone",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        }

                        // 🗒️ Item notes
                        val orderItems = order["items"] as? List<Map<String, Any>> ?: emptyList()
                        val notedItems = orderItems.filter {
                            (it["note"] as? String)?.isNotEmpty() == true
                        }
                        if (notedItems.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "📝 Notes:",
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                color = Color(0xFF1565C0)
                            )
                            notedItems.forEach { noteItem ->
                                Text(
                                    "• ${noteItem["name"]}: ${noteItem["note"]}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        // 📦 Items list
                        Spacer(modifier = Modifier.height(6.dp))
                        orderItems.forEach { oi ->
                            val iName = oi["name"] as? String ?: ""
                            val iQty = (oi["quantity"] as? Long)?.toInt() ?: 1
                            Text(
                                "• $iName × $iQty",
                                fontSize = 12.sp,
                                color = Color(0xFF444444)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // STATUS BUTTONS
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Placed", "Preparing", "Ready").forEach { s ->
                                Button(
                                    onClick = {
                                        db.collection("orders")
                                            .document(docId)
                                            .update("status", s)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(34.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (status == s)
                                            Color(0xFF1565C0)
                                        else Color(0xFFE0E0E0)
                                    )
                                ) {
                                    Text(
                                        s,
                                        fontSize = 11.sp,
                                        color = if (status == s) Color.White else Color.DarkGray,
                                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            "All Items",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(menu) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item) },
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // IMAGE
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF1F5F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (item.imageUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = item.imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text("🍽️", fontSize = 24.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                item.name,
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                                fontSize = 15.sp
                            )
                            Text(
                                "₹${item.price}  •  ⏱ ${item.cookingTime} mins",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // AVAILABILITY DOT
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        if (item.isAvailable) Color(0xFF2E7D32)
                                        else Color(0xFFD32F2F),
                                        RoundedCornerShape(50)
                                    )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                if (item.isAvailable) "On" else "Off",
                                fontSize = 10.sp,
                                color = if (item.isAvailable)
                                    Color(0xFF2E7D32) else Color(0xFFD32F2F),
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StaffItemControlScreen(
    item: FoodItem,
    onBack: () -> Unit
) {

    var cookingTime by remember { mutableStateOf(item.cookingTime.toString()) }
    var isAvailable by remember { mutableStateOf(item.isAvailable) }

    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {

        // 🔙 Back
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 🍔 Image
        if (item.imageUrl.isNotEmpty()) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        } else {
            Image(
                painter = painterResource(R.drawable.food_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📝 Name
        Text(
            text = item.name,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 💰 Price (READ ONLY for staff)
        Text(
            text = "₹${item.price}",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ⏱ Cooking Time Edit
        Text("Cooking Time (mins)")

        OutlinedTextField(
            value = cookingTime,
            onValueChange = { cookingTime = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔘 Availability Toggle
        Text("Availability")

        Row(verticalAlignment = Alignment.CenterVertically) {

            RadioButton(
                selected = isAvailable,
                onClick = { isAvailable = true }
            )
            Text("Available")

            Spacer(modifier = Modifier.width(20.dp))

            RadioButton(
                selected = !isAvailable,
                onClick = { isAvailable = false }
            )
            Text("Not Available")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 💾 Save Button
        val context = LocalContext.current

        Button(
            onClick = {
                if (item.id.isEmpty()) {
                    Toast.makeText(context, "Cannot update — item ID missing", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val db = FirebaseFirestore.getInstance()
                db.collection("items")
                    .document(item.id)
                    .update(mapOf(
                        "cookingTime" to (cookingTime.toIntOrNull() ?: item.cookingTime),
                        "isAvailable" to isAvailable
                    ))
                    .addOnSuccessListener {
                        Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Update Failed!", Toast.LENGTH_SHORT).show()
                    }
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
        ) {
            Text(
                "Save Changes",
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                color = Color.White
            )
        }
    }
}

@Composable
fun AdminDashboardScreen(
    d2Menu: List<FoodItem>,
    fcMenu: List<FoodItem>,
    onAdd: () -> Unit,
    onUpdate: () -> Unit,
    onStock: () -> Unit,
    onFeedback: (List<FoodItem>, List<FoodItem>) -> Unit,
    onLogout: () -> Unit,
    location: String = "D2"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Admin Panel",
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
                Text(
                    "📍 Managing: $location",
                    fontSize = 13.sp,
                    color = Color(0xFF1565C0),
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                )
            }
            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color(0xFFD32F2F)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val adminOptions = listOf(
            Triple("➕", "Add Items", "Add new food items to menu"),
            Triple("✏️", "Update Items", "Edit or delete existing items"),
            Triple("📦", "Stock Detail", "View and manage stock levels"),
            Triple("💬", "Feedbacks", "View customer feedback & ratings")
        )

        val actions = listOf(onAdd, onUpdate, onStock, { onFeedback(d2Menu, fcMenu) })

        adminOptions.forEachIndexed { index, (emoji, title, subtitle) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { actions[index]() },
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color(0xFF1565C0).copy(alpha = 0.1f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emoji, fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            title,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                        Text(
                            subtitle,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier
                            .size(18.dp)
                            .then(Modifier) // flip arrow to point right
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

// Summary stats at bottom
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${d2Menu.size + fcMenu.size}",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                    Text("Total Items", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${(d2Menu + fcMenu).count { it.isAvailable }}",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                    Text("Available", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        location,
                        fontSize = 24.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                    Text("Location", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
    }
}

@Composable
fun AdminButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )
    }
}

@Composable
fun AddItemScreen(
    onBack: () -> Unit,
    onSave: (String, String, String, String, Uri?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var cookingTime by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Veg") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    val categories = listOf("Veg", "NonVeg", "Egg", "Tiffins", "Meals",
        "Biryani", "Chinese", "Snacks", "Rolls", "Drinks", "Special")

    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .verticalScroll(rememberScrollState())
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Add Item",
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 📸 IMAGE PICKER — full width, tappable
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF1F5F9))
                .border(
                    width = 1.5.dp,
                    color = if (imageUri != null) Color(0xFF1565C0) else Color.LightGray,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,   // Fit shows full image
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
                // Edit overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("📷 Change", color = Color.White, fontSize = 12.sp)
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📷", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Tap to upload image",
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                    Text(
                        "Optional",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 📝 Item Name
        Text(
            "Item Name",
            fontSize = 13.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("e.g. Chicken Biryani") },
            textStyle = TextStyle(
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1565C0),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color(0xFF1565C0)
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 💰 Price + ⏱ Cooking Time side by side
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Price (₹)",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    placeholder = { Text("e.g. 120") },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1565C0),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color(0xFF1565C0)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Cooking Time (mins)",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = cookingTime,
                    onValueChange = { cookingTime = it },
                    placeholder = { Text("e.g. 10") },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1565C0),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color(0xFF1565C0)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 🏷️ Category selector
        Text(
            "Category",
            fontSize = 13.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Chip style category selector
        val rows = categories.chunked(3)
        rows.forEach { rowCats ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                rowCats.forEach { cat ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (category == cat) Color(0xFF1565C0)
                                else Color.White
                            )
                            .border(
                                1.dp,
                                if (category == cat) Color.Transparent else Color.LightGray,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { category = cat }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            cat,
                            fontSize = 12.sp,
                            color = if (category == cat) Color.White else Color.DarkGray,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                // Fill empty slots in last row
                repeat(3 - rowCats.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 💾 Save button
        Button(
            onClick = {
                if (name.isNotEmpty() && price.isNotEmpty()) {
                    onSave(name.trim(), price.trim(), cookingTime.trim(), category, imageUri)
                }
            },
            enabled = name.isNotEmpty() && price.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1565C0),
                disabledContainerColor = Color.LightGray
            )
        ) {
            Text(
                "Save Item",
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun UpdateItemsScreen(
    items: List<FoodItem>,
    onBack: () -> Unit,
    onItemClick: (FoodItem) -> Unit,
    onDelete: (String) -> Unit
) {
    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }

        Text(
            "Update Items",
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Text(
            "${items.size} items",
            fontSize = 13.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // IMAGE
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF1F5F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (item.imageUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = item.imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text("🍽️", fontSize = 28.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // INFO
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                item.name,
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "₹${item.price}  •  ⏱ ${item.cookingTime} mins",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            // Availability badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (item.isAvailable)
                                            Color(0xFF2E7D32).copy(alpha = 0.1f)
                                        else Color(0xFFD32F2F).copy(alpha = 0.1f)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    if (item.isAvailable) "● Available" else "● Unavailable",
                                    fontSize = 11.sp,
                                    color = if (item.isAvailable)
                                        Color(0xFF2E7D32) else Color(0xFFD32F2F),
                                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                                )
                            }
                        }

                        // BUTTONS
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Button(
                                onClick = { onItemClick(item) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1565C0)
                                ),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text(
                                    "Edit",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                                )
                            }
                            OutlinedButton(
                                onClick = { onDelete(item.id) },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color(0xFFD32F2F)),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text(
                                    "Delete",
                                    fontSize = 12.sp,
                                    color = Color(0xFFD32F2F),
                                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminItemEditScreen(
    item: FoodItem,
    onBack: () -> Unit,
    onSave: (FoodItem) -> Unit
) {

    var name by remember { mutableStateOf(item.name) }
    var price by remember { mutableStateOf(item.price.toString()) }
    var cookingTime by remember { mutableStateOf(item.cookingTime.toString()) }
    var isAvailable by remember { mutableStateOf(item.isAvailable) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        imageUri = it
    }

    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {

        // 🔙 BACK
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Edit Item",
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🖼 IMAGE PICKER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF1F1F1))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {

            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // 👉 show existing image OR placeholder
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📷", fontSize = 28.sp)
                    Text("Add Image")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 📝 NAME
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 💰 PRICE
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ⏱ COOKING TIME
        OutlinedTextField(
            value = cookingTime,
            onValueChange = { cookingTime = it },
            label = { Text("Cooking Time (mins)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔘 AVAILABILITY
        Text("Availability")

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = isAvailable,
                onClick = { isAvailable = true }
            )
            Text("Available")

            Spacer(modifier = Modifier.width(20.dp))

            RadioButton(
                selected = !isAvailable,
                onClick = { isAvailable = false }
            )
            Text("Not Available")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 💾 SAVE BUTTON
        val context = LocalContext.current

        Button(
            onClick = {
                val db = FirebaseFirestore.getInstance()
                val storage = FirebaseStorage.getInstance()

                if (imageUri != null) {
                    // Upload new image first
                    val imageRef = storage.reference
                        .child("food_images/${System.currentTimeMillis()}.jpg")

                    imageRef.putFile(imageUri!!)
                        .addOnSuccessListener {
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                val updatedItem = item.copy(
                                    name = name.trim(),
                                    price = price.toIntOrNull() ?: item.price,
                                    cookingTime = cookingTime.toIntOrNull() ?: item.cookingTime,
                                    isAvailable = isAvailable,
                                    imageUrl = uri.toString()
                                )
                                onSave(updatedItem)
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // No new image — save without changing imageUrl
                    val updatedItem = item.copy(
                        name = name.trim(),
                        price = price.toIntOrNull() ?: item.price,
                        cookingTime = cookingTime.toIntOrNull() ?: item.cookingTime,
                        isAvailable = isAvailable
                    )
                    onSave(updatedItem)
                }
            },

            enabled = name.isNotEmpty() && price.isNotEmpty(),

            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Save Changes")
        }
    }
}

@Composable
fun StockScreen(onBack: () -> Unit) {

    BackHandler { onBack() }

    val db = FirebaseFirestore.getInstance()

    val staticStock = listOf(
        mapOf("name" to "Rice Bags", "quantity" to 8, "unit" to "bags", "threshold" to 3),
        mapOf("name" to "Cooking Oil", "quantity" to 2, "unit" to "cans", "threshold" to 3),
        mapOf("name" to "Eggs", "quantity" to 120, "unit" to "pcs", "threshold" to 30),
        mapOf("name" to "Gas Cylinder", "quantity" to 1, "unit" to "units", "threshold" to 2),
        mapOf("name" to "Chicken", "quantity" to 5, "unit" to "kg", "threshold" to 3),
        mapOf("name" to "Vegetables", "quantity" to 12, "unit" to "kg", "threshold" to 4),
        mapOf("name" to "Wheat Flour", "quantity" to 6, "unit" to "kg", "threshold" to 3),
        mapOf("name" to "Paneer", "quantity" to 2, "unit" to "kg", "threshold" to 2),
        mapOf("name" to "Tomatoes", "quantity" to 8, "unit" to "kg", "threshold" to 3),
        mapOf("name" to "Onions", "quantity" to 15, "unit" to "kg", "threshold" to 5)
    )

    // Mutable quantities — editable
    var stockQuantities by remember {
        mutableStateOf(staticStock.associate {
            (it["name"] as String) to (it["quantity"] as Int)
        })
    }

    var search by remember { mutableStateOf("") }
    var editingItem by remember { mutableStateOf<String?>(null) }
    var editingQty by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            Text(
                "Stock Detail",
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        // LEGEND
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(start = 4.dp)
        ) {
            listOf(
                Color(0xFF2E7D32) to "Good",
                Color(0xFFFFA000) to "Low",
                Color(0xFFD32F2F) to "Critical"
            ).forEach { (color, label) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(color, RoundedCornerShape(50))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(label, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // SEARCH BAR
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search stock...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1565C0),
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Tap any item to update quantity",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LIST — weight(1f) so it doesn't crop
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(staticStock.filter {
                (it["name"] as String).contains(search, ignoreCase = true)
            }) { item ->

                val name = item["name"] as String
                val unit = item["unit"] as String
                val threshold = item["threshold"] as Int
                val qty = stockQuantities[name] ?: (item["quantity"] as Int)

                val (statusColor, statusLabel) = when {
                    qty <= 0 -> Color(0xFFD32F2F) to "Out of Stock"
                    qty <= threshold -> Color(0xFFFFA000) to "Low Stock"
                    else -> Color(0xFF2E7D32) to "In Stock"
                }

                Card(
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            editingItem = name
                            editingQty = qty.toString()
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(statusColor, RoundedCornerShape(50))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    name,
                                    fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                                    fontSize = 15.sp
                                )
                                Text(
                                    statusLabel,
                                    fontSize = 12.sp,
                                    color = statusColor,
                                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "$qty $unit",
                                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                fontSize = 16.sp,
                                color = statusColor
                            )
                            Text(
                                "min: $threshold $unit",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

    // EDIT DIALOG — shown when item tapped
    if (editingItem != null) {

        val item = staticStock.find { it["name"] == editingItem }
        val unit = item?.get("unit") as? String ?: ""

        ModalBottomSheet(
            onDismissRequest = { editingItem = null },
            containerColor = Color.White,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(5.dp)
                            .background(Color.LightGray, RoundedCornerShape(50))
                    )
                }

                Text(
                    "Update Stock",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    editingItem ?: "",
                    fontSize = 15.sp,
                    color = Color(0xFF1565C0),
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = editingQty,
                    onValueChange = { editingQty = it },
                    label = { Text("Quantity ($unit)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1565C0),
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = Color(0xFF1565C0)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { editingItem = null },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val newQty = editingQty.toIntOrNull()
                            if (newQty != null && newQty >= 0) {
                                stockQuantities = stockQuantities.toMutableMap().also {
                                    it[editingItem!!] = newQty
                                }
                            }
                            editingItem = null
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1565C0)
                        )
                    ) {
                        Text(
                            "Update",
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ReportScreen(onBack: () -> Unit) {
    Text("Report Screen")
}

@Composable
fun FeedbackScreen(onBack: () -> Unit) {

    BackHandler { onBack() }

    val db = FirebaseFirestore.getInstance()
    var feedbackList by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var averageRating by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        db.collection("feedback")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { it.data }
                feedbackList = list.sortedByDescending {
                    (it["timestamp"] as? com.google.firebase.Timestamp)?.seconds ?: 0L
                }
                averageRating = if (list.isNotEmpty()) {
                    list.mapNotNull {
                        (it["rating"] as? Long)?.toDouble()
                    }.average()
                } else 0.0
                isLoading = false
            }
            .addOnFailureListener { isLoading = false }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }

        Text(
            "Feedback",
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // AVERAGE RATING CARD
        if (!isLoading && feedbackList.isNotEmpty()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Average Rating",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )
                        Text(
                            "%.1f / 5.0".format(averageRating),
                            color = Color.White,
                            fontSize = 28.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                        Text(
                            "${feedbackList.size} reviews",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                    Text("⭐", fontSize = 48.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading feedback...", color = Color.Gray)
            }
        } else if (feedbackList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💬", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "No feedback yet",
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(feedbackList) { feedback ->
                    val rating = (feedback["rating"] as? Long)?.toInt() ?: 0
                    val comment = feedback["comment"] as? String ?: ""
                    val orderId = feedback["orderId"] as? String ?: ""
                    val location = feedback["location"] as? String ?: ""
                    val timestamp = feedback["timestamp"] as? com.google.firebase.Timestamp
                    val dateStr = timestamp?.let {
                        java.text.SimpleDateFormat(
                            "dd MMM, hh:mm a",
                            java.util.Locale.getDefault()
                        ).format(it.toDate())
                    } ?: ""

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    orderId.takeLast(12),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Row {
                                    repeat(rating) {
                                        Text("⭐", fontSize = 14.sp)
                                    }
                                }
                            }

                            if (comment.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    comment,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "📍 $location",
                                    fontSize = 12.sp,
                                    color = Color(0xFF1565C0)
                                )
                                Text(dateStr, fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderHistoryScreen(
    onBack: () -> Unit,
    onOrderClick: (String, Map<String, Any>) -> Unit
) {
    BackHandler { onBack() }

    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var orders by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(uid) {
        db.collection("orders")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    data.toMutableMap().also { it["docId"] = doc.id }
                }
                // Sort by timestamp descending (newest first)
                orders = list.sortedByDescending {
                    (it["timestamp"] as? com.google.firebase.Timestamp)?.seconds ?: 0L
                }
                isLoading = false
            }
            .addOnFailureListener { isLoading = false }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Order History",
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...", color = Color.Gray)
            }
        } else if (orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛒", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "No orders yet!",
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(orders) { order ->

                    val orderId = order["orderId"] as? String ?: ""
                    val total = (order["total"] as? Long)?.toInt() ?: 0
                    val location = order["location"] as? String ?: ""
                    val status = order["status"] as? String ?: "Placed"
                    val mode = order["mode"] as? String ?: ""
                    val timestamp = order["timestamp"] as? com.google.firebase.Timestamp
                    val dateStr = timestamp?.let {
                        val sdf = java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault())
                        sdf.format(it.toDate())
                    } ?: "Unknown date"

                    val statusColor = when (status) {
                        "Placed" -> Color(0xFF1565C0)
                        "Preparing" -> Color(0xFFFFA000)
                        "Ready" -> Color(0xFF2E7D32)
                        else -> Color.Gray
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOrderClick(orderId, order) },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = orderId,
                                    fontSize = 13.sp,
                                    color = Color.Gray,
                                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(statusColor.copy(alpha = 0.12f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = status,
                                        fontSize = 12.sp,
                                        color = statusColor,
                                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "📍 $location  •  🪑 $mode",
                                    fontSize = 13.sp,
                                    color = Color(0xFF444444),
                                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                                )
                                Text(
                                    text = "₹$total",
                                    fontSize = 16.sp,
                                    color = Color(0xFF2E7D32),
                                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = dateStr,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Tap to view details & rate →",
                                fontSize = 12.sp,
                                color = Color(0xFF1565C0),
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderDetailScreen(
    orderId: String,
    orderData: Map<String, Any>?,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val context = LocalContext.current

    var rating by remember { mutableIntStateOf(0) }
    var feedbackText by remember { mutableStateOf("") }
    var feedbackSubmitted by remember { mutableStateOf(false) }

    // Check if feedback already submitted
    LaunchedEffect(orderId) {
        db.collection("feedback").document(orderId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    feedbackSubmitted = true
                    rating = (doc.getLong("rating") ?: 0).toInt()
                    feedbackText = doc.getString("comment") ?: ""
                }
            }
    }

    val items = orderData?.get("items") as? List<Map<String, Any>> ?: emptyList()
    val total = (orderData?.get("total") as? Long)?.toInt() ?: 0
    val location = orderData?.get("location") as? String ?: ""
    val mode = orderData?.get("mode") as? String ?: ""
    val status = orderData?.get("status") as? String ?: "Placed"
    val paymentMethod = orderData?.get("paymentMethod") as? String ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF5F9FF))
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Order Details",
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = orderId,
            fontSize = 13.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // INFO CARD
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Location", color = Color.Gray, fontSize = 13.sp)
                    Text(
                        location,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mode", color = Color.Gray, fontSize = 13.sp)
                    Text(
                        mode,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Payment", color = Color.Gray, fontSize = 13.sp)
                    Text(
                        paymentMethod,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Status", color = Color.Gray, fontSize = 13.sp)
                    Text(
                        status,
                        color = when (status) {
                            "Placed" -> Color(0xFF1565C0)
                            "Preparing" -> Color(0xFFFFA000)
                            "Ready" -> Color(0xFF2E7D32)
                            else -> Color.Gray
                        },
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ITEMS
        Text(
            "Items Ordered",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )

        Spacer(modifier = Modifier.height(8.dp))

        items.forEach { item ->
            val itemName = item["name"] as? String ?: ""
            val itemPrice = (item["price"] as? Long)?.toInt() ?: 0
            val itemQty = (item["quantity"] as? Long)?.toInt() ?: 1
            val itemNote = item["note"] as? String ?: ""

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        itemName,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                    if (itemNote.isNotEmpty()) {
                        Text("Note: $itemNote", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Text(
                    "x$itemQty  ₹${itemPrice * itemQty}",
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    color = Color(0xFF2E7D32)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                "Total: ₹$total",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // FEEDBACK SECTION
        if (status == "Ready" || feedbackSubmitted) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        if (feedbackSubmitted) "Your Feedback" else "Rate this Order",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // ⭐ STAR RATING
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..5).forEach { star ->
                            Text(
                                text = if (star <= rating) "⭐" else "☆",
                                fontSize = 28.sp,
                                modifier = Modifier.clickable(enabled = !feedbackSubmitted) {
                                    rating = star
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { if (!feedbackSubmitted) feedbackText = it },
                        placeholder = { Text("Share your experience...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !feedbackSubmitted,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        ),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!feedbackSubmitted) {
                        Button(
                            onClick = {
                                if (rating == 0) {
                                    Toast.makeText(
                                        context,
                                        "Please select a rating", Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }

                                val feedback = hashMapOf(
                                    "orderId" to orderId,
                                    "userId" to uid,
                                    "rating" to rating,
                                    "comment" to feedbackText,
                                    "location" to location,
                                    "timestamp" to com.google.firebase.Timestamp.now()
                                )

                                db.collection("feedback")
                                    .document(orderId)
                                    .set(feedback)
                                    .addOnSuccessListener {
                                        feedbackSubmitted = true
                                        Toast.makeText(
                                            context,
                                            "Thanks for your feedback! 🙏", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1565C0)
                            )
                        ) {
                            Text(
                                "Submit Feedback",
                                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                color = Color.White
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "✅ Feedback submitted. Thank you!",
                                color = Color(0xFF2E7D32),
                                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Spacer(modifier = Modifier.height(30.dp))
        } else {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⏳", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "You can rate this order once it's Ready",
                        fontSize = 13.sp,
                        color = Color(0xFFFFA000),
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun OrderTrackingScreen(
    orderId: String,
    orderData: Map<String, Any>?,
    timerSeconds: Int = 0,
    timerStarted: Boolean = false,
    onTimerUpdate: (Int, Boolean) -> Unit = { _, _ -> },
    onBack: () -> Unit,
    onDismiss: () -> Unit,
    menuItems: List<FoodItem> = emptyList()
) {

    BackHandler { onBack() }

    val db = FirebaseFirestore.getInstance()

    var status by remember { mutableStateOf("Placed") }
    var items by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var total by remember { mutableStateOf(0) }
    var location by remember { mutableStateOf("") }
    var timerSeconds by remember(orderId) { mutableIntStateOf(timerSeconds) }
    var timerStarted by remember(orderId) { mutableStateOf(timerStarted) }

    // Listen to real time order status from Firebase
    LaunchedEffect(orderId) {
        if (orderId.isNotEmpty()) {
            db.collection("orders")
                .document(orderId)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && snapshot.exists()) {
                        status = snapshot.getString("status") ?: "Placed"
                        total = (snapshot.getLong("total") ?: 0).toInt()
                        location = snapshot.getString("location") ?: ""
                        items = snapshot.get("items") as? List<Map<String, Any>> ?: emptyList()
                    }
                }
        }
    }

    // Calculate cooking time from items
    LaunchedEffect(items) {
        if (items.isNotEmpty() && !timerStarted) {

            // Find the max cooking time across all ordered items
            // Match order item names against actual menu items to get cookingTime
            val maxCookingTime = items.maxOfOrNull { orderItem ->
                val itemName = orderItem["name"] as? String ?: ""
                val qty = (orderItem["quantity"] as? Long)?.toInt() ?: 1

                // Find this item in the real menu
                val menuItem = menuItems.find { it.name == itemName }
                val baseCookTime = menuItem?.cookingTime ?: 10

                // Total time = base cooking time + 1 min per extra quantity
                baseCookTime + ((qty - 1) * 1)
            } ?: 10

            timerSeconds = maxCookingTime * 60
            timerStarted = true
            onTimerUpdate(timerSeconds, true)
        }
    }

    // Countdown timer
    LaunchedEffect(timerStarted) {
        if (timerStarted) {
            while (timerSeconds > 0 && status != "Ready") {
                delay(1000)
                timerSeconds--
                onTimerUpdate(timerSeconds, true)
            }
// ✅ After loop ends — mark as Ready
            if (status != "Ready" && timerSeconds <= 0) {
                db.collection("orders").document(orderId)
                    .update("status", "Ready")
            }
        }
    }

    // Auto set Preparing after 60s
    LaunchedEffect(timerStarted) {
        if (timerStarted && status == "Placed") {
            delay(60000)
            if (status == "Placed") {
                db.collection("orders").document(orderId)
                    .update("status", "Preparing")
            }
        }
    }

    val steps = listOf("Placed", "Preparing", "Ready")
    val currentStep = steps.indexOf(status).coerceAtLeast(0)

    val minutes = timerSeconds / 60
    val seconds = timerSeconds % 60

    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {
        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            Text(
                "Order Tracking",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            orderId,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ⏱ TIMER CARD
        if (status != "Ready") {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Estimated Time",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "%02d:%02d".format(minutes, seconds),
                        color = Color.White,
                        fontSize = 48.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                    Text(
                        "mins : secs",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "🎉",
                        fontSize = 40.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Your Order is Ready!",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                    Text(
                        "Please collect from counter",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 📊 STATUS STEPS
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    "Order Status",
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                steps.forEachIndexed { index, step ->

                    val isDone = index <= currentStep
                    val isCurrent = index == currentStep

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        // Circle
                        Box(
                            modifier = Modifier
                                .size(if (isCurrent) 28.dp else 24.dp)
                                .background(
                                    when {
                                        isDone -> Color(0xFF1565C0)
                                        else -> Color(0xFFE0E0E0)
                                    },
                                    RoundedCornerShape(50)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Text("✓", color = Color.White, fontSize = 12.sp)
                            } else {
                                Text(
                                    "${index + 1}",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                step,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                fontSize = 14.sp,
                                color = if (isDone) Color(0xFF1565C0) else Color.Gray
                            )
                            Text(
                                when (step) {
                                    "Placed" -> "Order received by canteen"
                                    "Preparing" -> "Chef is cooking your food"
                                    "Ready" -> "Collect from counter"
                                    else -> ""
                                },
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Connector line
                    if (index < steps.size - 1) {
                        Box(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .width(2.dp)
                                .height(24.dp)
                                .background(
                                    if (index < currentStep) Color(0xFF1565C0)
                                    else Color(0xFFE0E0E0)
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🧾 ITEMS SUMMARY
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    "Items",
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                items.forEach { item ->
                    val name = item["name"] as? String ?: ""
                    val qty = (item["quantity"] as? Long)?.toInt() ?: 1
                    val price = (item["price"] as? Long)?.toInt() ?: 0

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "$name x$qty",
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                            fontSize = 14.sp
                        )
                        Text(
                            "₹${price * qty}",
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                    Text(
                        "₹$total",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        color = Color(0xFF2E7D32),
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // DISMISS when ready
        if (status == "Ready") {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                )
            ) {
                Text(
                    "Done — Back to Home",
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    color = Color.White
                )
            }
        }
    }
}

